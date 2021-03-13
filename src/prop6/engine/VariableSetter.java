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

package prop6.engine;

import esl2.engine.CallingContext;
import esl2.engine.Setter;
import esl2.types.ProgrammingException;
import esl2.types.TypedOperationException;
import esl2.types.ValueType;

public final class VariableSetter extends Setter
{

    private final int location;
    private final String name;

    public VariableSetter(int location, String name)
    {
        this.location = location;
        this.name = name;
    }

    @Override
    public void set(CallingContext context, ValueType value) throws TypedOperationException
    {
        try
        {
            prop6.engine.CallingContext castedContext = ((prop6.engine.CallingContext)context);
            if (name != castedContext.top().name)
            {
                throw new TypedOperationException("Variable part of object '" + name + "' accessed from object '" + castedContext.top().name + "'.");
            }
            castedContext.top().variables.set(location, value);
        }
        catch(ClassCastException e)
        {
            throw new ProgrammingException("ESL2 Context was not PROP6 Context");
        }
    }

}
