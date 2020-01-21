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

package esl2.test;

import java.util.Map.Entry;

import esl2.engine.CallingContext;
import esl2.engine.Executor;
import esl2.engine.expression.Expression;
import esl2.input.ConsoleInput;
import esl2.input.Lexeme;
import esl2.input.Lexer;
import esl2.parser.ExecutorBuilder;
import esl2.parser.FunctionPairs;
import esl2.parser.GetterSetter;
import esl2.parser.Parser;
import esl2.parser.SymbolTable;
import esl2.types.*;

public final class ParserTestFromInput
{

    public static void printValue(ValueType val)
    {
        if (null != val)
        {
            if (val instanceof DoubleValue)
            {
                System.out.format("%1$.16e", ((DoubleValue)val).value);
            }
            else if (val instanceof VectorValue)
            {
                Vector vec = ((VectorValue)val).value;
                System.out.format("[%1$.16e, %2$.16e, %3$.16e]", vec.x, vec.y, vec.z);
            }
            else if (val instanceof QuaternionValue)
            {
                Quaternion quat = ((QuaternionValue)val).value;
                System.out.format("[%1$.16e, %2$.16e, %3$.16e, %4$.16e]", quat.s, quat.i, quat.j, quat.k);
            }
            else if (val instanceof MatrixValue)
            {
                Matrix mat = ((MatrixValue)val).value;
                System.out.format("[%1$.16e, %2$.16e, %3$.16e; %4$.16e, %5$.16e, %6$.16e; %7$.16e, %8$.16e, %9$.16e]",
                    mat.a11, mat.a12, mat.a13, mat.a21, mat.a22, mat.a23, mat.a31, mat.a32, mat.a33);
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
                    printValue(v);
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
                    printValue(e.getKey());
                    System.out.print(":");
                    printValue(e.getValue());
                }
                System.out.print(" }");
            }
            else
            {
                System.out.print("Type not understood.");
            }
        }
        else
        {
            System.out.print("A collection contains a null.");
        }
    }

    public static void main(String[] args)
    {
        try
        {
            DummyLogger dummy = new DummyLogger();
            Lexer lexer = new Lexer(new ConsoleInput(), "Console", 1, 1);

            SymbolTable table = new SymbolTable();
            table.pushContext(); // We need a base context to operate on.
            table.gs = new GetterSetter(); // _We_ must allocate this for the table.

            CallingContext context = new CallingContext();
            context.executor = new Executor();

            // These new lines may not make sense here, but NTE will use this.
            FunctionPairs funs = new FunctionPairs();
            ExecutorBuilder.createDefaultFunctions(context.executor, funs, null);
            table.addAll(funs);
            ExecutorBuilder.finalizeTable(table);

            while (Lexeme.END_OF_FILE != lexer.peekNextToken().tokenType)
            {
                Expression res = Parser.parseExpression(lexer, table, dummy);

                if (null != res)
                {
                    try
                    {
                        ValueType val = res.evaluate(context);

                        if (null != val)
                        {
                            printValue(val);
                            System.out.println("");
                        }
                        else
                        {
                            System.out.println("Evaluate returned null.");
                        }
                    }
                    catch(TypedOperationException e)
                    {
                        System.out.println("Caught a TypedOperationException: " + e.getLocalizedMessage());
                    }
                    catch(FatalException e)
                    {
                        System.out.println("Caught a FatalException: " + e.getLocalizedMessage());
                    }
                }
                else
                {
                    System.out.print("Parse returned null.");
                    Parser.recoverExpression(lexer);
                }

                lexer.getNextToken(); // Assume we are at a sequence point.
            }
        }
        catch (FatalException e)
        {
            e.printStackTrace();
        }
    }

}
