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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map.Entry;

import esl2.engine.CallingContext;
import esl2.engine.ConstantsSingleton;
import esl2.engine.FlowControl;
import esl2.engine.statement.Statement;
import esl2.types.ArrayValue;
import esl2.types.DictionaryValue;
import esl2.types.Matrix;
import esl2.types.MatrixValue;
import esl2.types.ProgrammingException;
import esl2.types.Quaternion;
import esl2.types.QuaternionValue;
import esl2.types.DoubleValue;
import esl2.types.StringValue;
import esl2.types.ValueType;
import esl2.types.Vector;
import esl2.types.VectorValue;
import prop6.engine.DebugStackFrame;
import prop6.engine.Executor;
import prop6.input.ObjectFrame;
import prop6.types.SimObject;
import prop6.types.ObjectValue;
import prop6.types.State;
import prop6.types.StateValue;

public final class EnterDebugger extends Statement
{

    public EnterDebugger()
    {
        super(null);
    }

    public static synchronized ValueType function(prop6.engine.CallingContext context)
    {
        if (null == context.debugger)
        {
            context.fileOut.message("Cannot enter debugger when not debugging!");
            return ConstantsSingleton.getInstance().DOUBLE_ZERO;
        }
        // We don't want other threads printing to the screen while we are running.
        // Make them block if they try.
        try
        {
            ConsoleLogger.lockScreen.acquire();
            try
            {
                String line, prevLine = "";
                BufferedReader cin = new BufferedReader(new InputStreamReader(System.in));
                DebugStackFrame frame = ((DebugStackFrame)context.currentFrame.debug);
                System.out.print("In Function ");
                outputFrame(frame.depth, frame);
                line = cin.readLine();
                while (0 != "quit".compareTo(line))
                {
                    // Empty lines repeat the previous command.
                    if (true == line.isEmpty())
                    {
                        line = prevLine;
                    }

                    switch (line.substring(0, Math.min(5, line.length())))
                    {
                    case "up":
                        if (null == frame.frame.prev)
                        {
                            System.out.println("Already in top-most frame.");
                        }
                        else
                        {
                            frame = ((DebugStackFrame)frame.frame.prev.debug);
                            System.out.print("In Function ");
                            outputFrame(frame.depth, frame);
                        }
                        break;
                    case "down":
                        if (frame == context.currentFrame.debug)
                        {
                            System.out.println("Already in bottom-most frame.");
                        }
                        else
                        {
                            frame = ((DebugStackFrame)frame.next.debug);
                            System.out.print("In Function ");
                            outputFrame(frame.depth, frame);
                        }
                        break;
                    case "bt":
                        for (DebugStackFrame i = frame; null != i.frame.prev; i = ((DebugStackFrame)i.frame.prev.debug))
                        {
                            outputFrame(i.depth, i);
                        }
                        break;
                    case "print":
                        if (7 > line.length())
                        {
                            boolean printComma = false;
                            System.out.print("These are the variables in the current stack frame: ");
                            for (Entry<String, Integer> e : frame.info.args.entrySet())
                            {
                                if (true == printComma)
                                {
                                    System.out.print(", ");
                                }
                                System.out.print(e.getKey());
                                printComma = true;
                            }
                            for (Entry<String, Integer> e : frame.info.locals.entrySet())
                            {
                                if (true == printComma)
                                {
                                    System.out.print(", ");
                                }
                                System.out.print(e.getKey());
                                printComma = true;
                            }
                            if (null != frame.objFrame)
                            {
                                for (Entry<String, Integer> e : frame.objFrame.vars.entrySet())
                                {
                                    if (true == printComma)
                                    {
                                        System.out.print(", ");
                                    }
                                    System.out.print(e.getKey());
                                    printComma = true;
                                }
                            }
                            System.out.println("");
                        }
                        else
                        {
                            String name = line.substring(6, line.length());
                            Integer i = frame.info.args.get(name);
                            if (null != i)
                            {
                                printValue(frame.frame.args.get(i.intValue()), context);
                                System.out.println("");
                            }
                            else
                            {
                                i = frame.info.locals.get(name);
                                if (null != i)
                                {
                                    printValue(frame.frame.locals.get(i.intValue()), context);
                                    System.out.println("");
                                }
                                else
                                {
                                    if (null != frame.objFrame)
                                    {
                                        i = frame.objFrame.vars.get(name);
                                        if (null != i)
                                        {
                                            printValue(frame.obj.variables.get(i.intValue()), context);
                                            System.out.println("");
                                        }
                                        else
                                        {
                                            System.out.println("There is no variable >" + name + "< in scope.");
                                        }
                                    }
                                    else
                                    {
                                        System.out.println("There is no variable >" + name + "< in scope.");
                                    }
                                }
                            }
                        }
                        break;
                    default:
                        System.out.println("Did not understand command >" + line + "<.");
                        System.out.println("Known commands are:");
                        System.out.println("\tquit - exit the debugger and continue running");
                        System.out.println("\tbt - give a back trace to the current stack frame");
                        System.out.println("\tup - go up one calling stack frame");
                        System.out.println("\tdown - go down one callee stack frame");
                        System.out.println("\tprint - print the variables accessible in this stack frame");
                        System.out.println("\tprint variable_name - print the value in the given variable");
                    }
                    
                    prevLine = line;
                    line = cin.readLine();
                }
            }
            catch (IOException e)
            {
                throw new ProgrammingException("Failed to read: " + e.getLocalizedMessage());
            }
            finally
            {
                ConsoleLogger.lockScreen.release();
            }
        }
        catch (InterruptedException e)
        {
            throw new ProgrammingException("Someone interrupted me: " + e.getLocalizedMessage());
        }
        return ConstantsSingleton.getInstance().DOUBLE_ZERO;
    }

    public static void outputFrame(int location, esl2.engine.DebugStackFrame frame)
    {
        System.out.println("#" + location + ": " + frame.info.frameName + " from file " +
            frame.callingToken.sourceFile + " on line " + frame.callingToken.lineNumber + " at " + frame.callingToken.lineLocation);
    }

    public static void printVec(Vector vec)
    {
        System.out.format("[%1$.16e, %2$.16e, %3$.16e]", vec.x, vec.y, vec.z);
    }

    public static void printQuat(Quaternion quat)
    {
        System.out.format("[%1$.16e, %2$.16e, %3$.16e, %4$.16e]", quat.s, quat.i, quat.j, quat.k);
    }

    public static void printMat(Matrix mat)
    {
        System.out.format("[%1$.16e, %2$.16e, %3$.16e; %4$.16e, %5$.16e, %6$.16e; %7$.16e, %8$.16e, %9$.16e]",
            mat.a11, mat.a12, mat.a13, mat.a21, mat.a22, mat.a23, mat.a31, mat.a32, mat.a33);
    }

    public static void printValue(ValueType val, prop6.engine.CallingContext context)
    {
        if (null != val)
        {
            if (val instanceof DoubleValue)
            {
                System.out.format("%1$.16e", ((DoubleValue)val).value);
            }
            else if (val instanceof VectorValue)
            {
                printVec(((VectorValue)val).value);
            }
            else if (val instanceof QuaternionValue)
            {
                printQuat(((QuaternionValue)val).value);
            }
            else if (val instanceof MatrixValue)
            {
                printMat(((MatrixValue)val).value);
            }
            else if (val instanceof StringValue)
            {
                System.out.print("\"" + ((StringValue)val).value + "\"");
            }
            else if (val instanceof ArrayValue)
            {
                System.out.print("{ ");
                boolean printComma = false;
                for (ValueType v : ((ArrayValue)val).value)
                {
                    if (true == printComma)
                    {
                        System.out.print(", ");
                    }
                    else
                    {
                        printComma = true;
                    }
                    printValue(v, context);
                }
                System.out.print(" }");
            }
            else if (val instanceof DictionaryValue)
            {
                System.out.print("{ ");
                boolean printComma = false;
                for (Entry<ValueType, ValueType> e : ((DictionaryValue)val).value.entrySet())
                {
                    if (true == printComma)
                    {
                        System.out.print(", ");
                    }
                    else
                    {
                        printComma = true;
                    }
                    printValue(e.getKey(), context);
                    System.out.print(":");
                    printValue(e.getValue(), context);
                }
                System.out.print(" }");
            }
            else if (val instanceof ObjectValue)
            {
                SimObject object = ((ObjectValue)val).value;
                ObjectFrame frame = ((Executor)context.executor).objectDebugData.get(object.name);
                System.out.print("##Object## : " + object.name + " { ");
                if (null != frame)
                {
                    boolean printComma = false;
                    for (Entry<Integer, String> e : frame.varNames.entrySet())
                    {
                        if (true == printComma)
                        {
                            System.out.print(", ");
                        }
                        else
                        {
                            printComma = true;
                        }
                        System.out.print(e.getValue() + ":");
                        printValue(object.variables.get(e.getKey().intValue()), context);
                    }
                }
                else
                {
                    System.out.print(" No Object Debugging Data Found (Bug in Engine) ");
                }
                System.out.print(" }");
            }
            else if (val instanceof StateValue)
            {
                // I was going to turn this into a Dictionary and print it out that way,
                // but then I realized that would be Collectivizing the State.
                // We don't need that commie crap.
                State state = ((StateValue)val).value;
                System.out.format("##State## { Time_s:%1$.16e, ", state.time_s);
                System.out.print("Position_m:"); printVec(state.position);
                System.out.print(", Velocity_m_s:"); printVec(state.velocity);
                System.out.print(", Acceleration_m_s2:"); printVec(state.acceleration);
                System.out.format(", Mass_kg:%1$.16e, FlowRate_kg_s%2$.16e, ", state.mass_kg, state.flowRate_kg_s);
                System.out.print("Orientation_quat:"); printQuat(state.orientation);
                System.out.print(", AngularVelocity_deg_s:"); printVec(state.angularVelocity.mul(Math.toDegrees(1.0)));
                System.out.print(", AngularAcceleration_deg_s2:"); printVec(state.angularAcceleration.mul(Math.toDegrees(1.0)));
                System.out.print(", MOI:"); printMat(state.MOI);
                System.out.print(", Idot:"); printMat(state.Idot);
                System.out.print(", PositionECR_m:"); printVec(state.position_ecr);
                System.out.print(", VelocityECR_m_s:"); printVec(state.velocity_ecr);
                System.out.print(", LLH_deg_deg_m:"); printVec(new Vector(Math.toDegrees(state.llh.x), Math.toDegrees(state.llh.y), state.llh.z));
                System.out.format(", SpeedOfSound_m_s:%1$.16e, Density_kg_m3:%2$.16e, Pressure_Pa:%3$.16e, Temperature_K:%4$.16e, ",
                    state.atmosphere.speedOfSound_m_s, state.atmosphere.density_kg_m3, state.atmosphere.pressure_Pa, state.atmosphere.temperature_K);
                System.out.format("DynamicPressure_Pa:%1$.16e, Mach:%2$.16e, ", state.dynamicPressure_Pa, state.mach);
                System.out.print("CurrentGravity_N:"); printVec(state.gravity_N);
                System.out.print(" }");
                
            }
            else
            {
                System.out.print("Type not understood (Bug in Engine, not Model).");
            }
        }
        else
        {
            System.out.print("Variable is undefined (Normal), or a collection contains a NULL (Bug in Engine, not Model).");
        }
    }

    @Override
    public FlowControl execute(CallingContext context)
    {
        try
        {
        	return new FlowControl(FlowControl.Type.RETURN, FlowControl.NO_TARGET, function((prop6.engine.CallingContext)context), token);
        }
        catch(ClassCastException e)
        {
            throw new ProgrammingException("ESL2 Context was not PROP6 Context");
        }
    }

}
