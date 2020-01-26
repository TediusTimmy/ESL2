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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import esl2.types.FatalException;
import prop6.engine.CallingContext;
import prop6.engine.Executor;
import prop6.input.TestCasesReader;
import prop6.input.ExecutorBuilder;
import prop6.input.FunctionDefinitions;
import prop6.input.SettingsReader;
import prop6.types.TestCase;
import prop6.types.Settings;

public final class Driver
{

    public static void main(String[] args)
    {
        try
        {
            ConsoleLogger.logFile = new BufferedWriter(new FileWriter(new File("ConsoleLog.txt")));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        ConsoleLogger logger = new ConsoleLogger();
        try
        {
            boolean isDebugRun = SettingsReader.peekSettings("settings.xml");

            CallingContext context = new CallingContext();
            context.executor = new Executor();
            context.fileOut = logger;
            if (true == isDebugRun)
            {
                context.debugger = new DebuggerHook();
            }
            FunctionDefinitions funDefs = new FunctionDefinitions();
            funDefs.buildDefaultFunctions(context);
            context.executor.functions.set(funDefs.statelessStdLibFunctions.funs.get("EnterDebugger"), new EnterDebugger());

            Settings settings = SettingsReader.readSettings("settings.xml", context, funDefs);
            context.settings = settings;

            Class<? extends ObjectSimulator> sim = null;
            if (true == settings.singleThread)
            {
                ExecutorBuilder.setNoThreads(context.executor, funDefs.statelessStdLibFunctions,
                    (null != context.debugger) ? context.executor.debugFrames : null);
                sim = SingleObjectSimulator.class;
            }
            else
            {
                sim = MultiObjectSimulator.class;
            }

            ArrayList<TestCase> testCases = TestCasesReader.readTestCases("test_cases.xml", context, funDefs);

            ArrayList<ReadAndSimulateSystem> systems = new ArrayList<ReadAndSimulateSystem>();
            for (TestCase testCase : testCases)
            {
                ReadAndSimulateSystem system = new ReadAndSimulateSystem(testCase, context, funDefs.duplicate_base(), sim.getConstructor((Class<?>[])null).newInstance((Object[])null));

                systems.add(system);

                if (true == settings.singleThread)
                {
                    system.run();
                }
                else
                {
                    system.start();
                }
            }
            if (false == settings.singleThread)
            {
                for (ReadAndSimulateSystem system : systems)
                {
                    try
                    {
                        system.join();
                    }
                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }
        catch(FatalException e)
        {
            logger.message("Caught FatalException: " + e.getLocalizedMessage());
        }
        catch(InstantiationException | IllegalAccessException | IllegalArgumentException |
            InvocationTargetException | NoSuchMethodException | SecurityException e)
        {
            // This is what I get for trying to be fancy...
            e.printStackTrace();
        }

        try
        {
            ConsoleLogger.logFile.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

}
