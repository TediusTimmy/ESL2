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

package prop6.engine.stdlib;

import esl2.types.StringValue;
import esl2.types.TypedOperationException;
import esl2.types.ValueType;
import prop6.engine.CallingContext;
import prop6.types.LogMessage;
import prop6.types.LogMessageType;

public final class Info extends StandardUnaryFunction
{

    @Override
    public ValueType fun(CallingContext context, ValueType arg) throws TypedOperationException
    {
        if (arg instanceof StringValue)
        {
            switch(context.settings.loggingLevel)
            {
            case LOG_INFO:
                if (true == context.noModel())
                {
                    context.simulacrum.messages.add(new LogMessage(LogMessageType.LOG_INFO, ((StringValue)arg).value));
                }
                else
                {
                    context.current().messages.add(new LogMessage(LogMessageType.LOG_INFO, ((StringValue)arg).value));
                }
                break;
            case LOG_WARNING:
            case LOG_ERROR:
            case LOG_FATAL:
                break;
            }
        }
        else
        {
            throw new TypedOperationException("Error logging non-String.");
        }
        return arg;
    }

}
