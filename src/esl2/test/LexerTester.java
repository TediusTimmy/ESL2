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

import esl2.input.ConsoleInput;
import esl2.input.Lexeme;
import esl2.input.Lexer;
import esl2.input.Token;
import esl2.types.FatalException;

public final class LexerTester
{

    public static void main(String[] args)
    {
        try
        {
            Lexer lexer = new Lexer(new ConsoleInput(), "Console", 1, 1);
    
            while (Lexeme.END_OF_FILE != lexer.peekNextToken().tokenType)
            {
                Token token = lexer.getNextToken();
                System.out.println(token.tokenType + " >" + token.text + "< " + token.sourceFile + " " + token.lineNumber + " " + token.lineLocation);
            }
            Token token = lexer.getNextToken();
            System.out.println(token.tokenType + " >" + token.text + "< " + token.sourceFile + " " + token.lineNumber + " " + token.lineLocation);
            token = lexer.getNextToken();
            System.out.println(token.tokenType + " >" + token.text + "< " + token.sourceFile + " " + token.lineNumber + " " + token.lineLocation);
        }
        catch (FatalException e)
        {
            e.printStackTrace();
        }
    }

}
