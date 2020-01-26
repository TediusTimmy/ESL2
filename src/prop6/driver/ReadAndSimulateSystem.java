 /*
   Copyright (C) 2017 Thomas DiModica <ricinwich@yahoo.com>

   This program is free software: you can redistribute it and/or modify
   it under the terms of the GNU General Public License as published by
   the Free Software Foundation, either version 3 of the License, or
   (at your option) any later version.

   This program is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU General Public License for more details.

   You should have received a copy of the GNU General Public License
   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package prop6.driver;

import java.util.ArrayList;

import esl2.types.FatalException;
import esl2.types.TypedOperationException;
import esl2.types.ValueType;
import prop6.engine.CallingContext;
import prop6.engine.Propagator;
import prop6.engine.Stepper;
import prop6.engine.stdlib.SimToTerm;
import prop6.engine.steppers.RKDPStepper;
import prop6.input.FunctionDefinitions;
import prop6.input.SystemReader;
import prop6.input.SettingsReader;
import prop6.random.ANSIC;
import prop6.random.ParkMiller;
import prop6.types.TestCase;
import prop6.types.LogMessage;
import prop6.types.LogMessageType;
import prop6.types.Simulacrum;
import prop6.types.Model;
import prop6.types.ObjectValue;

public final class ReadAndSimulateSystem extends Thread
{

    private final TestCase testCase;
    public final CallingContext system;
    private final FunctionDefinitions funDefs;
    public final ObjectSimulator objectSimulator;

    public ReadAndSimulateSystem(TestCase testCase, CallingContext context, FunctionDefinitions funDefs, ObjectSimulator objectSimulator)
    {
        this.testCase = testCase;
        system = new CallingContext();
        system.copyFromHere(context);
        system.executor = system.executor.duplicate();
        this.funDefs = funDefs;
        this.objectSimulator = objectSimulator;
    }

    @Override
    public void run()
    {
        try
        {
            ANSIC rng = new ANSIC(testCase.randomSeed);

            SystemReader.readSystem(testCase.system, system, funDefs);
            system.pushObject(system.simulacrum.objects.get(Simulacrum.name));
            system.top().objRNG = new ParkMiller((long)(rng.getNext() * 8388608.0) + 1L); // Cannot pass 0 to this constructor!
            system.top().myRNG = new ANSIC((long)(rng.getNext() * 8388608.0));

            final double stepSize = 1.0; // Min step 1 Hair : 1 nanosec.
            final double checkPoint = Math.round(system.settings.rate * Stepper.HAIRS_PER_SECOND);
            RKDPStepper stepper = new RKDPStepper(0.000000000001);
            Propagator prop = new Propagator(stepper, stepSize, checkPoint, checkPoint);
            system.propagator = prop;

            ValueType initialObject = system.simulacrum.getInitialObject.execute(system, testCase.input.evaluate(system));

            if (initialObject instanceof ObjectValue)
            {
                Model initialModel = new Model(((ObjectValue)initialObject).value,
                    new prop6.types.State(), true, 0.0, prop.upperBounds);
                system.pushModel(initialModel);
                system.pushObject(initialModel.object);
                try
                {
                    SimToTerm.InitialObjectInitialization(initialModel, system);
                }
                finally
                {
                    system.popModel();
                    system.popObject();
                }

                objectSimulator.setContext(system);
                objectSimulator.setInitialModel(initialModel);
                objectSimulator.run();

                genMessages();

                system.pushObject(system.simulacrum.objects.get(Simulacrum.name));
                FileLogger fileOut = new FileLogger(testCase.outFile);
                system.fileOut = fileOut;
                try
                {
                    system.settings.fun.execute(system, SettingsReader.convert(objectSimulator.states));
                }
                finally
                {
                    system.popObject();
                }
                fileOut.close();
            }
            else
            {
                throw new FatalException("Launch object is not an object.");
            }
        }
        catch (FatalException | TypedOperationException e)
        {
            system.fileOut.message(e.getLocalizedMessage());
        }
    }

    protected String priorStr(LogMessageType priority)
    {
        switch (priority)
        {
        case LOG_FATAL:
            return "FATAL";
        case LOG_INFO:
            return "INFO";
        case LOG_WARNING:
            return "WARN";
        case LOG_ERROR:
            return "ERROR";
        }
        return null;
    }

    protected void genMessages()
    {
        if (0 != system.simulacrum.messages.size())
        {
            system.fileOut.message("The simulacrum produced these messages: ");
            for (LogMessage msg : system.simulacrum.messages)
            {
                system.fileOut.message(priorStr(msg.priority) +  " : " + msg.message);
            }
        }
        if (null != objectSimulator.message)
        {
            system.fileOut.message("Propagation produced these messages: ");
            for (String msg : objectSimulator.message)
            {
                system.fileOut.message(msg);
            }
        }
        for (ArrayList<Model> states : objectSimulator.states)
        {
            Model mod = states.get(states.size() - 1);
            if (0 != mod.numBadSteps)
            {
                system.fileOut.message("Object >" + mod.object.name + "< had " + mod.numBadSteps + " bad steps.");
            }
            if (0 != mod.messages.size())
            {
                system.fileOut.message("Object >" + mod.object.name + "< produced these messages: ");
                for (LogMessage msg : mod.messages)
                {
                    system.fileOut.message(priorStr(msg.priority) +  " : " + msg.message);
                }
            }
        }
    }

}
