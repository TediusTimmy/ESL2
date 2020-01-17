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
import esl2.engine.expression.Expression;
import esl2.input.Token;
import esl2.types.FatalException;
import esl2.types.TypedOperationException;

public final class WhileStatement extends Statement
{

    public final Expression condition;
    public final Statement seq;
    public final int id;

    public WhileStatement(Token token, Expression condition, Statement seq, int id)
    {
        super(token);
        this.condition = condition;
        this.seq = seq;
        this.id = id;
    }

    @Override
    public FlowControl execute(CallingContext context) throws TypedOperationException, FatalException
    {
        boolean conditional = true;
        try
        {
            conditional = condition.evaluate(context).logical();
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
        while (true == conditional)
        {
            FlowControl temp = seq.execute(context);

            if (null != temp)
            {
                switch (temp.type)
                {
                case RETURN:
                    // Pass it up the chain.
                    return temp;
                case BREAK:
                    if (id == temp.target)
                    {
                        // I am now done.
                        return null;
                    }
                    else
                    {
                        // Pass it up the chain.
                        return temp;
                    }
                case CONTINUE:
                    if (id == temp.target)
                    {
                        // Do nothing: the previous iteration has stopped and we will move on to the next.
                        break;
                    }
                    else
                    {
                        // Pass it up the chain.
                        return temp;
                    }
                }
            }

            try
            {
                conditional = condition.evaluate(context).logical();
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
        }
        return null;
    }

}
