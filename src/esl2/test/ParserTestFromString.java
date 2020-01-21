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

import esl2.engine.CallingContext;
import esl2.engine.Executor;
import esl2.engine.StackFrame;
import esl2.engine.statement.Statement;
import esl2.input.Lexeme;
import esl2.input.Lexer;
import esl2.input.StringInput;
import esl2.input.Token;
import esl2.parser.ExecutorBuilder;
import esl2.parser.FunctionPairs;
import esl2.parser.GetterSetter;
import esl2.parser.Parser;
import esl2.parser.SymbolTable;
import esl2.types.FatalException;
import esl2.types.TypedOperationException;

public final class ParserTestFromString {

    public static void main(String[] args)
    {
        try
        {
            StringInput input = new StringInput
(

        "set y to x - 1\n" +
        "while y > 1 do\n" +
        "   set x to x * y\n" +
        "   set y to y - 1\n" +
        "end\n" +
        "set z to x\n" +
        "call Info(ToString(z))"
 
/*
        //"if 5 < 7 then\n" +
        "if 7 < 5 then\n" +
        "   call Info('True')\n" +
        "else\n" +
        "   call Info('False')\n" +
        "end"
 */
/*
        "select 5 from\n" +
        "   case 1 is\n" +
        "      call Info('Should not print')\n" +
        "   case 5 is\n" +
        "      call Info('Should print')\n" +
        "   case 6 is\n" +
        "      call Info('Case 6 should not print')\n" +
        "   case else is\n" +
        "      call Info('Default should not print')\n" +
        "end"
 */
/*
        "if 5 < 7 then\n" +
        //"if 7 < 5 then\n" +
        "else\n" +
        "   call Info('False')\n" +
        "end\n" +
        "call Info('Done')"
 */
);
            DummyLogger dummy = new DummyLogger();
            Lexer lexer = new Lexer(input, "Input String", 1, 1);

            SymbolTable table = new SymbolTable();
            table.pushContext(); // We need a base context to operate on.
            table.gs = new GetterSetter(); // _We_ must allocate this for the table.
    ///* For Test 1 Only
            table.addArgument("x");
    //*/

            CallingContext context = new CallingContext();
            context.executor = new Executor();

            // These new lines may not make sense here, but NTE will use this.
            FunctionPairs funs = new FunctionPairs();
            ExecutorBuilder.createDefaultFunctions(context.executor, funs, null);
            table.addAll(funs);
            ExecutorBuilder.finalizeTable(table);

            Statement parse = Parser.Parse(lexer, table, context.executor, dummy);
            if (null != parse)
            {
    ///* For Test 1 Only
                StackFrame frame = new StackFrame(context, 1, 2);
                frame.args.set(0, new esl2.types.DoubleValue(6.0));
                context.pushContext(frame, 0, new Token(Lexeme.INVALID, "", "", 0, 0));
    //*/
                parse.execute(context);
            }
            else
            {
                System.out.println("Parse returned null");
            }
        }
        catch (FatalException e)
        {
            e.printStackTrace();
        }
        catch (TypedOperationException e)
        {
            e.printStackTrace();
        }
    }

}
