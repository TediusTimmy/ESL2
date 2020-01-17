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

package esl2.engine.stdlib;

import esl2.engine.CallingContext;
import esl2.engine.ConstantsSingleton;
import esl2.engine.FlowControl;
import esl2.engine.statement.Statement;

public final class PI extends Statement
{

    public PI()
    {
        super(null);
    }

    @Override
    public FlowControl execute(CallingContext context)
    {
        return new FlowControl(FlowControl.Type.RETURN, FlowControl.NO_TARGET, ConstantsSingleton.getInstance().PI, token);
    }

}
