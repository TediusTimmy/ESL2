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
import esl2.engine.statement.Statement;
import esl2.input.BufferedGenericInput;
import esl2.input.ConsoleInput;
import esl2.input.FileInput;
import esl2.input.Lexeme;
import esl2.input.Lexer;
import esl2.parser.ExecutorBuilder;
import esl2.parser.FunctionPairs;
import esl2.parser.GetterSetter;
import esl2.parser.Parser;
import esl2.parser.ParserException;
import esl2.parser.SymbolTable;
import esl2.types.FatalException;
import esl2.types.TypedOperationException;

public final class ParserTestFromFile
{

    public static void main(String[] args)
    {
        try
        {
            DummyLogger dummy = new DummyLogger();
            SymbolTable table = new SymbolTable();
            table.pushContext(); // We need a base context to operate on.
            table.gs = new GetterSetter(); // _We_ must allocate this for the table.

            Executor executor = new Executor();

            // These new lines may not make sense here, but NTE will use this.
            FunctionPairs funs = new FunctionPairs();
            ExecutorBuilder.createDefaultFunctions(executor, funs, null);
            table.addAll(funs);
            ExecutorBuilder.finalizeTable(table);

            for (int i = 0; i < args.length; ++i)
            {
                FileInput file = new FileInput(args[i]);
                BufferedGenericInput bgi = new BufferedGenericInput(file);
                Lexer lexer = new Lexer(bgi, args[i], 1, 1);

                if (false == Parser.ParseFunctions(lexer, table, executor, dummy))
                {
                    System.out.println("Error parsing file: " + args[i] + "\nExiting...");
                    return;
                }
            }

            ConsoleInput console = new ConsoleInput();
            BufferedGenericInput bgi = new BufferedGenericInput(console);
            Lexer lexer = new Lexer(bgi, "Console", 1, 1);

            while (Lexeme.END_OF_FILE != lexer.peekNextToken().tokenType)
            {
                try
                {
                    Statement res = Parser.ParseStatement(lexer, table, executor, dummy);

                    if (null != res)
                    {
                        try
                        {
                            CallingContext context = new CallingContext(executor, false, null);

                            res.execute(context);
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
                        Parser.recoverStatement(lexer);
                    }

                    lexer.getNextToken(); // Assume we are at a sequence point.
                }
                catch (ParserException e)
                {
                    System.out.println("Caught a ParserException: " + e.getLocalizedMessage());
                    Parser.recoverStatement(lexer);
                }
            }
        }
        catch (FatalException e)
        {
            e.printStackTrace();
        }
    }

}
