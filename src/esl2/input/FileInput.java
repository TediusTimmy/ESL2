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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import esl2.types.FatalException;

public final class FileInput extends GenericInput
{

    private final List<String> input;
    private int line, character;

    public FileInput(String fileName) throws FatalException
    {
        List<String> temp = null;
        try
        {
            temp = Files.readAllLines(Paths.get(fileName));
        }
        catch (IOException e)
        {
            throw new FatalException("Exception thrown reading file \"" + fileName + "\" : " + e.getLocalizedMessage());
        }
        input = temp;
        line = 0;
        character = 0;
    }

    @Override
    public int getNextCharacter() throws FatalException
    {
        if (null == input)
        {
            return ENDOFFILE;
        }
        if (input.size() <= line)
        {
            return ENDOFFILE;
        }
        if (input.get(line).length() <= character)
        {
            ++line;
            character = 0;
            return '\n';
        }
        return input.get(line).charAt(character++);
    }

}
