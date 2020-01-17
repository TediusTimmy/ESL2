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

package esl2.input;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import esl2.types.FatalException;

public final class ConsoleInput extends GenericInput
{

    private BufferedReader br;
    private String input;
    private int index;

    public ConsoleInput()
    {
        br = new BufferedReader(new InputStreamReader(System.in));
        this.input = "";
        index = 1;
    }

    @Override
    public int getNextCharacter() throws FatalException
    {
        // Did we start at end of input?
        if (null == input)
        {
            return ENDOFFILE;
        }

        // Buffer a new line the next request after returning the end of line.
        if ((input.length() + 1) == index)
        {
            input = null;
            try
            {
                index = 0;
                input = br.readLine();
            }
            catch (IOException e)
            {
                throw new FatalException(e.getLocalizedMessage());
            }
        }

        // Are we now at end of input?
        if (null == input)
        {
            return ENDOFFILE;
        }

        // Are we at the end of the line?
        if (input.length() == index)
        {
            ++index; // Flag having given the newline
            return '\n';
        }

        // Return the next character.
        return input.charAt(index++);
    }

}
