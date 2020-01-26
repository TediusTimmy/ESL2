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

package prop6.test;

import java.util.ArrayList;

import esl2.test.DummyLogger;
import esl2.types.FatalException;
import esl2.types.TypedOperationException;
import prop6.driver.DebuggerHook;
import prop6.driver.EnterDebugger;
import prop6.engine.CallingContext;
import prop6.engine.Executor;
import prop6.input.TestCasesReader;
import prop6.input.FunctionDefinitions;
import prop6.input.SystemReader;
import prop6.input.SettingsReader;
import prop6.types.TestCase;
import prop6.types.Settings;

public final class TestReaders
{

    public static void main(String[] args)
    {
        try
        {
            CallingContext context = new CallingContext();
            context.executor = new Executor();
            context.fileOut = new DummyLogger();
            FunctionDefinitions funDefs = new FunctionDefinitions();
            funDefs.buildDefaultFunctions(context);

            if (true == SettingsReader.peekSettings("settings.xml"))
            {
                context.debugger = new DebuggerHook();
            }
            Settings settings = SettingsReader.readSettings("settings.xml", context, funDefs);
            context.settings = settings;

            System.out.println("Logging Level: " + settings.loggingLevel);
            System.out.println("Debug: " + settings.debugRun);
            System.out.println("Single Threaded: " + settings.singleThread);
            System.out.println("Atmosphere: " + (null != settings.earth.atmosphere));
            System.out.format("Rotation Rate: %1$.16e\n", settings.earth.rotationRate_rad_s);
            System.out.format("G0: %1$.16e\n", settings.earth.g0_m_s);
            System.out.format("Mu: %1$.16e\n", settings.earth.mu_Nm2_kg);
            System.out.format("Equatorial Radius: %1$.16e\n", settings.earth.ellipsoid.equatorialRadius_m);
            System.out.format("Inverse Flattening: %1$.16e\n", settings.earth.ellipsoid.inverseFlattening);
            System.out.println("Gravity: " + (null != settings.earth.gravity));
            System.out.format("Output Rate: %1$.16e\n", settings.rate);
            System.out.println("Output Function: " + settings.fun.location);

            ArrayList<TestCase> testCases = TestCasesReader.readTestCases("test_cases.xml", context, funDefs);

            for (TestCase tCase : testCases)
            {
                System.out.println("System: " + tCase.system);
                System.out.println("Output File: " + tCase.outFile);
                System.out.println("Random Seed: " + tCase.randomSeed);

                System.out.print("Input: ");
                EnterDebugger.printValue(tCase.input.evaluate(context), context);
            }
            System.out.println("");

            SystemReader.readSystem(testCases.get(0).system, context, funDefs);

            System.out.println("Simulacrum: " + (null != context.simulacrum));
        }
        catch (TypedOperationException e)
        {
            System.out.println("Caught a TypedOperationException: " + e.getLocalizedMessage());
        }
        catch (FatalException e)
        {
            System.out.println("Caught a FatalException: " + e.getLocalizedMessage());
        }
    }

}
