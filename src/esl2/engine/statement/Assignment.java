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

package esl2.engine.statement;

import esl2.engine.CallingContext;
import esl2.engine.FlowControl;
import esl2.engine.Getter;
import esl2.engine.Setter;
import esl2.engine.expression.Expression;
import esl2.input.Token;
import esl2.types.FatalException;
import esl2.types.TypedOperationException;

public final class Assignment extends Statement
{

    public final Getter getter;
    public final Setter setter;
    public final RecAssignmentState index;
    public final Expression rhs;

    public Assignment(Token token, Getter getter, Setter setter, RecAssignmentState index, Expression rhs)
    {
        super(token);
        this.getter = getter;
        this.setter = setter;
        this.index = index;
        this.rhs = rhs;
    }

    @Override
    public FlowControl execute(CallingContext context) throws TypedOperationException, FatalException
    {
        if (null == index)
        {
            setter.set(context, rhs.evaluate(context));
        }
        else
        {
            setter.set(context, index.evaluate(context, getter.get(context), rhs));
        }
        return null;
    }

}
