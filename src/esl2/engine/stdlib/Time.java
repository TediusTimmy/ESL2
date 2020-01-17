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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import esl2.engine.CallingContext;
import esl2.engine.FlowControl;
import esl2.engine.statement.Statement;
import esl2.types.StringValue;

public final class Time extends Statement
{

    static final DateFormat format = new SimpleDateFormat("HH:mm:ss");

    public Time()
    {
        super(null);
    }

    @Override
    public FlowControl execute(CallingContext context)
    {
        return new FlowControl(FlowControl.Type.RETURN, FlowControl.NO_TARGET, new StringValue(format.format(Calendar.getInstance().getTime())), token);
    }

}
