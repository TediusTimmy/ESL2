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

package esl2.parser;

import java.util.List;

import esl2.engine.Executor;
import esl2.engine.statement.Statement;
import esl2.engine.stdlib.*;
import esl2.engine.stdlib.Error;
import esl2.types.FunctionPointerValue;

public class ExecutorBuilder
{

    public static void createDefaultFunctions(Executor executor, FunctionPairs funs, List<FrameDebugInfo> frameInfo)
    {
            // 5
        addFunction("PI", new PI(), 0, executor, funs, frameInfo);
        addFunction("Date", new Date(), 0, executor, funs, frameInfo);
        addFunction("Time", new Time(), 0, executor, funs, frameInfo);
        addFunction("NewArray", new NewArray(), 0, executor, funs, frameInfo);
        addFunction("NewDictionary", new NewDictionary(), 0, executor, funs, frameInfo);

            // 61
        addFunction("Sin", new Sin(), 1, executor, funs, frameInfo);
        addFunction("Cos", new Cos(), 1, executor, funs, frameInfo);
        addFunction("Tan", new Tan(), 1, executor, funs, frameInfo);
        addFunction("Asin", new Asin(), 1, executor, funs, frameInfo);
        addFunction("Acos", new Acos(), 1, executor, funs, frameInfo);
        addFunction("Atan", new Atan(), 1, executor, funs, frameInfo);
        addFunction("Sinh", new Sinh(), 1, executor, funs, frameInfo);
        addFunction("Cosh", new Cosh(), 1, executor, funs, frameInfo);
        addFunction("Tanh", new Tanh(), 1, executor, funs, frameInfo);
        addFunction("Exp", new Exp(), 1, executor, funs, frameInfo);
        addFunction("Ln", new Ln(), 1, executor, funs, frameInfo);
        addFunction("Sqrt", new Sqrt(), 1, executor, funs, frameInfo);
        addFunction("Cbrt", new Cbrt(), 1, executor, funs, frameInfo);
        addFunction("Sqr", new Sqr(), 1, executor, funs, frameInfo);
        addFunction("Abs", new Abs(), 1, executor, funs, frameInfo);
        addFunction("Round", new Round(), 1, executor, funs, frameInfo);
        addFunction("Floor", new Floor(), 1, executor, funs, frameInfo);
        addFunction("Ceil", new Ceil(), 1, executor, funs, frameInfo);
        addFunction("GetX", new GetX(), 1, executor, funs, frameInfo);
        addFunction("GetY", new GetY(), 1, executor, funs, frameInfo);
        addFunction("GetZ", new GetZ(), 1, executor, funs, frameInfo);
        addFunction("Unit", new Unit(), 1, executor, funs, frameInfo);
        addFunction("Magnitude", new Magnitude(), 1, executor, funs, frameInfo);
        addFunction("Determinant", new Determinant(), 1, executor, funs, frameInfo);
        addFunction("Transpose", new Transpose(), 1, executor, funs, frameInfo);
        addFunction("Invert", new Invert(), 1, executor, funs, frameInfo);
        addFunction("Conjugate", new Conjugate(), 1, executor, funs, frameInfo);
        addFunction("GetScalar", new GetScalar(), 1, executor, funs, frameInfo);
        addFunction("GetVec", new GetVec(), 1, executor, funs, frameInfo);
        addFunction("ToString", new ToString(), 1, executor, funs, frameInfo);
        addFunction("Error", new Error(), 1, executor, funs, frameInfo);
        addFunction("Warn", new Warn(), 1, executor, funs, frameInfo);
        addFunction("Info", new Info(), 1, executor, funs, frameInfo);
        addFunction("Fatal", new Fatal(), 1, executor, funs, frameInfo);
        addFunction("Length", new Length(), 1, executor, funs, frameInfo);
        addFunction("DegToRad", new DegToRad(), 1, executor, funs, frameInfo);
        addFunction("RadToDeg", new RadToDeg(), 1, executor, funs, frameInfo);
        addFunction("XRotQuat", new XRotQuat(), 1, executor, funs, frameInfo);
        addFunction("YRotQuat", new YRotQuat(), 1, executor, funs, frameInfo);
        addFunction("ZRotQuat", new ZRotQuat(), 1, executor, funs, frameInfo);
        addFunction("XRotMat", new XRotMat(), 1, executor, funs, frameInfo);
        addFunction("YRotMat", new YRotMat(), 1, executor, funs, frameInfo);
        addFunction("ZRotMat", new ZRotMat(), 1, executor, funs, frameInfo);
        addFunction("IsNaN", new IsNaN(), 1, executor, funs, frameInfo);
        addFunction("IsInfinity", new IsInfinity(), 1, executor, funs, frameInfo);
        addFunction("ValueOf", new ValueOf(), 1, executor, funs, frameInfo);
        addFunction("ToCharacter", new ToCharacter(), 1, executor, funs, frameInfo);
        addFunction("FromCharacter", new FromCharacter(), 1, executor, funs, frameInfo);
        addFunction("IsDouble", new IsDouble(), 1, executor, funs, frameInfo);
        addFunction("IsVec", new IsVec(), 1, executor, funs, frameInfo);
        addFunction("IsQuat", new IsQuat(), 1, executor, funs, frameInfo);
        addFunction("IsMat", new IsMat(), 1, executor, funs, frameInfo);
        addFunction("IsString", new IsString(), 1, executor, funs, frameInfo);
        addFunction("IsArray", new IsArray(), 1, executor, funs, frameInfo);
        addFunction("IsDictionary", new IsDictionary(), 1, executor, funs, frameInfo);
        addFunction("Size", new Size(), 1, executor, funs, frameInfo);
        addFunction("PopFront", new PopFront(), 1, executor, funs, frameInfo);
        addFunction("PopBack", new PopBack(), 1, executor, funs, frameInfo);
        addFunction("GetKeys", new GetKeys(), 1, executor, funs, frameInfo);
        addFunction("MatToQuat", new MatToQuat(), 1, executor, funs, frameInfo);
        addFunction("QuatToMat", new QuatToMat(), 1, executor, funs, frameInfo);

            // 15
        addFunction("Atan2", new Atan2(), 2, executor, funs, frameInfo);
        addFunction("Hypot", new Hypot(), 2, executor, funs, frameInfo);
        addFunction("Log", new Log(), 2, executor, funs, frameInfo);
        addFunction("Min", new Min(), 2, executor, funs, frameInfo);
        addFunction("Max", new Max(), 2, executor, funs, frameInfo);
        addFunction("MakeQuat", new MakeQuat(), 2, executor, funs, frameInfo);
        addFunction("RotVecQuat", new RotVecQuat(), 2, executor, funs, frameInfo);
        addFunction("RotScaleVecQuat", new RotScaleVecQuat(), 2, executor, funs, frameInfo);
        addFunction("GetIndex", new GetIndex(), 2, executor, funs, frameInfo);
        addFunction("NewArrayDefault", new NewArrayDefault(), 2, executor, funs, frameInfo);
        addFunction("PushBack", new PushBack(), 2, executor, funs, frameInfo);
        addFunction("PushFront", new PushFront(), 2, executor, funs, frameInfo);
        addFunction("ContainsKey", new ContainsKey(), 2, executor, funs, frameInfo);
        addFunction("RemoveKey", new RemoveKey(), 2, executor, funs, frameInfo);
        addFunction("GetValue", new GetValue(), 2, executor, funs, frameInfo);

            // 6
        addFunction("MakeVec", new MakeVec(), 3, executor, funs, frameInfo);
        addFunction("MakeMatColumns", new MakeMatColumns(), 3, executor, funs, frameInfo);
        addFunction("MakeMatRows", new MakeMatRows(), 3, executor, funs, frameInfo);
        addFunction("SubString", new SubString(), 3, executor, funs, frameInfo);
        addFunction("SetIndex", new SetIndex(), 3, executor, funs, frameInfo);
        addFunction("Insert", new Insert(), 3, executor, funs, frameInfo);
    }

    public static void finalizeTable(SymbolTable table)
    {
        table.insertIndex = new FunctionPointerValue(table.getFunctionLocation("Insert"));
        table.pushBackIndex = new FunctionPointerValue(table.getFunctionLocation("PushBack"));
    }

    public static void addFunction(String name, Statement function, int nargs,
            Executor executor, FunctionPairs funs, List<FrameDebugInfo> frameInfo)
    {
        funs.funs.put(name, Integer.valueOf(executor.functions.size()));
        funs.args.put(name, Integer.valueOf(nargs));

        executor.functions.add(function);
        executor.args.add(Integer.valueOf(nargs));
        executor.locals.add(Integer.valueOf(0));
        executor.funNames.add(name);

        if (null != frameInfo)
        {
            FrameDebugInfo frame = new FrameDebugInfo();
            frame.frameName = name;
            for (int i = 0; i < nargs; ++i)
            {
                String argName = "Automatic_Argument_" + Integer.toString(i + 1);
                frame.args.put(argName, Integer.valueOf(i + 1));
                frame.argNames.add(argName);
            }
            frameInfo.add(frame);
        }
    }

}
