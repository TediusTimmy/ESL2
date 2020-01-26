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

import esl2.engine.CallingContext;
import esl2.engine.FlowControl;
import esl2.engine.statement.Statement;
import esl2.types.FatalException;
import esl2.types.ProgrammingException;
import prop6.types.Simulacrum;
import prop6.types.ObjectValue;

public final class GetCurrentObject extends Statement
{

    public GetCurrentObject()
    {
        super(null);
    }

    @Override
    public FlowControl execute(CallingContext context) throws FatalException
    {
        try
        {
            prop6.engine.CallingContext casted = (prop6.engine.CallingContext)context;
            if (Simulacrum.name == casted.top().name)
            {
                throw new FatalException("Error: GetCurrentObject called from getInitialObject, where there is no current object.");
            }
            else
            {
                return new FlowControl(FlowControl.Type.RETURN, FlowControl.NO_TARGET, new ObjectValue(casted.top().duplicate()), token);
            }
        }
        catch(ClassCastException e)
        {
            throw new ProgrammingException("ESL2 Context was not PROP6 Context");
        }
    }

}
