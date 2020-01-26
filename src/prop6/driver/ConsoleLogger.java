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
import java.io.IOException;
import java.util.concurrent.Semaphore;

import esl2.parser.ParserLogger;
import esl2.types.ProgrammingException;

public final class ConsoleLogger extends ParserLogger
{

    public static Semaphore lockScreen = new Semaphore(1);
    public static BufferedWriter logFile = null;

    public static void innerMessage(String msg)
    {
        try
        {
            lockScreen.acquire();
            try
            {
                if (null != logFile)
                {
                    logFile.write(msg);
                    logFile.newLine();
                }
                System.err.println(msg);
            }
            catch (IOException e)
            {
                throw new ProgrammingException("Failed to write: " + e.getLocalizedMessage());
            }
            finally
            {
                lockScreen.release();
            }
        }
        catch (InterruptedException e)
        {
            throw new ProgrammingException("Someone interrupted me: " + e.getLocalizedMessage());
        }
    }

    // I'd use DummyLogger, but this needs to be synchronized.
    @Override
    public void message(String msg)
    {
        innerMessage(msg);
    }

}
