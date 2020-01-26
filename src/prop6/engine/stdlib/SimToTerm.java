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

package prop6.engine.stdlib;

import java.util.ArrayList;

import esl2.types.FatalException;
import esl2.types.StringValue;
import esl2.types.TypedOperationException;
import esl2.types.ValueType;
import esl2.types.Vector;
import prop6.engine.CallingContext;
import prop6.engine.Stepper;
import prop6.types.SimObject;
import prop6.types.Model;
import prop6.types.ObjectValue;
import prop6.types.State;
import prop6.types.StateValue;

public final class SimToTerm extends StandardBinaryFunction
{
    
    public static void InitialObjectInitialization(Model model, CallingContext context) throws FatalException, TypedOperationException
    {
        model.object.onModelInit.execute(context, model.object.realizeArg);

        model.state.mass_kg = Stepper.getDouble(model.object.variables.get(SimObject.INITIAL_MASS_INDEX));
        model.state.position = Stepper.getVector(model.object.variables.get(SimObject.POSITION_OFFSET_INDEX));
        model.state.velocity = Stepper.getVector(model.object.variables.get(SimObject.VELOCITY_OFFSET_INDEX));
        model.state.orientation = Stepper.getQuaternion(model.object.variables.get(SimObject.ORIENTATION_OFFSET_INDEX));
        Vector temp = Stepper.getVector(model.object.variables.get(SimObject.RATES_OFFSET_INDEX));
        model.state.angularVelocity = new Vector(Math.toRadians(temp.x), Math.toRadians(temp.y), Math.toRadians(temp.z));
        model.state.MOI = Stepper.getMatrix(model.object.variables.get(SimObject.MOI_INDEX));
        Stepper.update(model.state, context.settings.earth);
        Stepper.updateGravity(model, context.settings.earth);

        model.object.onModelRealize.execute(context, model.object.realizeArg);
    }

    @Override
    public ValueType fun(CallingContext context, ValueType arg1, ValueType arg2) throws TypedOperationException
    {
        if (arg1 instanceof ObjectValue)
        {
            ValueType result;
            Model model = new Model();

            boolean noModel = context.noModel();
            if (false == noModel)
            {
                model.copyFromHere(context.current());
            }

            context.pushModel(model);
            context.pushObject(model.object);
            try
            {
                if (false == noModel)
                {
                    model.copyFromHere(new Model(((ObjectValue)arg1).value, model.state, false, model.currentTime, model.prevDelta));
                    model.object.realizeArg = arg2;

                    model.object.onModelInit.execute(context, model.object.realizeArg);
                    model.object.onModelRealize.execute(context, model.object.realizeArg);
                }
                else
                {
                    model.copyFromHere(new Model(((ObjectValue)arg1).value, new State(), false, 0.0, context.propagator.upperBounds));
                    model.object.realizeArg = arg2;
                    
                    InitialObjectInitialization(model, context);
                }

                ArrayList<Model> states = new ArrayList<Model>();
                context.propagator.propagate(states, context, model, false);

                result = new StateValue(states.get(states.size() - 1).state);
            }
            catch (FatalException e)
            {
                result = new StringValue(e.getLocalizedMessage());
            }
            finally
            {
                context.popObject();
                context.popModel();
            }
            return result;
        }
        else
        {
            throw new TypedOperationException("Cannot simulate non-Object to termination.");
        }
    }

}
