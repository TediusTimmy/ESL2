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
import esl2.engine.expression.Expression;
import esl2.input.Token;
import esl2.types.FatalException;
import esl2.types.TypedOperationException;
import esl2.types.ValueType;

public final class CaseContainer
{

    public enum Type
    {
        AT,
        ABOVE,
        BELOW
    }

    public final Token token;

    public final Type type;
    public final boolean breaking;
    public final Expression condition;
    public final Expression lower;
    public final Statement seq;

    public CaseContainer(Token token, Type type, boolean breaking, Expression condition, Expression lower, Statement seq)
    {
        this.token = token;
        this.type = type;
        this.breaking = breaking;
        this.condition = condition;
        this.lower = lower;
        this.seq = seq;
    }

    public boolean evaluate(CallingContext context, ValueType control) throws TypedOperationException, FatalException
    {
        boolean result = false;
        try
        {
            if (null == condition) // case else
            {
                result = true;
            }
            else if (null == lower) // one comparison
            {
                switch (type)
                {
                case AT:
                    result = condition.evaluate(context).equal(control);
                    break;
                case ABOVE: // This is inverted because we have inverted the condition.
                    result = condition.evaluate(context).leq(control);
                    break;
                case BELOW: // This is inverted because we have inverted the condition.
                    result = condition.evaluate(context).geq(control);
                    break;
                }
            }
            else // Two comparisons
            {
                ValueType TOP = condition.evaluate(context);
                ValueType BOTTOM = lower.evaluate(context);
                result = BOTTOM.leq(control) && TOP.geq(control);
            }
        }
        catch(TypedOperationException e)
        {
            String rethrow = Expression.constructMessage(e, token);
            if (null != context.debugger)
            {
                context.debugger.EnterDebugger(rethrow, context);
            }
            throw new TypedOperationException(rethrow);
        }
        return result;
    }

}
