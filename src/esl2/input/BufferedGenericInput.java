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

import esl2.types.FatalException;

public final class BufferedGenericInput
{

    private final GenericInput input;
    private final java.util.LinkedList<Character> buffer;
    private boolean endOfFile;

    public BufferedGenericInput(GenericInput toBuffer)
    {
        input = toBuffer;
        buffer = new java.util.LinkedList<Character>();
        endOfFile = false;
    }

    private void fill (int count) throws FatalException
    {
        if (true == endOfFile)
        {
            return;
        }

        for (int i = 0; i < count; ++i)
        {
            int it = input.getNextCharacter();
            
            if (GenericInput.ENDOFFILE == it)
            {
                endOfFile = true;
                return;
            }
            else if (true == Character.isValidCodePoint(it))
            {
                buffer.add(new Character( (char) it ));
            }
            else
            {
                throw new FatalException("Invalid character returned to BufferedGenericInput");
            }
        }
    }

    public int peek () throws FatalException
    {
        return peek(0);
    }

    public int peek (int lookahead) throws FatalException
    {
        // Do we have the data already?
        if (buffer.size() > lookahead)
        {
            return buffer.get(lookahead).charValue();
        }

        // Try to add the amount of needed data
        fill(lookahead - buffer.size() + 1);

        // Did we get enough input?
        if (buffer.size() > lookahead)
        {
            return buffer.get(lookahead).charValue();
        }

        // We must be at the End of the File.
        return GenericInput.ENDOFFILE;
    }

    public int consume () throws FatalException
    {
        // Do we have the data already?
        if (0 != buffer.size())
        {
            return buffer.removeFirst().charValue();
        }

        // Try to add a character
        fill(1);

        // Did we get new input?
        if (0 != buffer.size())
        {
            return buffer.removeFirst().charValue();
        }

        // We must be at the End of the File.
        return GenericInput.ENDOFFILE;
    }

}
