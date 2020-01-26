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

import esl2.parser.ParserLogger;

public final class FileLogger extends ParserLogger
{

    private final BufferedWriter writer;

    // Have to wrap it like this or Java complains.
    private BufferedWriter initWriter(String fileName)
    {
        BufferedWriter result = null;
        try
        {
            result = new BufferedWriter(new FileWriter(new File(fileName)));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return result;
    }

    public FileLogger(String fileName)
    {
        writer = initWriter(fileName);
    }

    @Override
    public void message(String msg)
    {
        try
        {
            writer.write(msg);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void close()
    {
        try
        {
            writer.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

}
