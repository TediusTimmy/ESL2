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
import esl2.engine.stdlib.GetIndex;
import esl2.engine.stdlib.GetValue;
import esl2.engine.stdlib.Insert;
import esl2.engine.stdlib.SetIndex;
import esl2.input.Token;
import esl2.types.ArrayValue;
import esl2.types.DictionaryValue;
import esl2.types.FatalException;
import esl2.types.TypedOperationException;
import esl2.types.ValueType;

public final class RecAssignmentState
{

    public final Token token;

    public final Expression index;
    public RecAssignmentState next;

    public RecAssignmentState(Token token, Expression index)
    {
        this.token = token;
        this.index = index;
    }

    ValueType evaluate(CallingContext context, ValueType lhs, Expression rhs) throws TypedOperationException, FatalException
    {
        ValueType result = null;
        if (null == next)
        {
            result = setIndex(lhs, index.evaluate(context), rhs.evaluate(context), context);
        }
        else
        {
            ValueType arrayIndex = index.evaluate(context);
            result = setIndex(lhs, arrayIndex, next.evaluate(context, getIndex(lhs, arrayIndex, context), rhs), context);
        }
        return result;
    }

    ValueType getIndex(ValueType container, ValueType index, CallingContext context) throws TypedOperationException, FatalException
    {
        ValueType result = null;
        try
        {
            if (container instanceof ArrayValue)
            {
                result = GetIndex.function(container, index);
            }
            else if (container instanceof DictionaryValue)
            {
                result = GetValue.function(container, index);
            }
            else
            {
                throw new TypedOperationException("Error indexing non-Collection.");
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

    ValueType setIndex(ValueType container, ValueType index, ValueType value, CallingContext context) throws TypedOperationException, FatalException
    {
        ValueType result = null;
        try
        {
            if (container instanceof ArrayValue)
            {
                result = SetIndex.function(container, index, value);
            }
            else if (container instanceof DictionaryValue)
            {
                result = Insert.function(container, index, value);
            }
            else
            {
                throw new TypedOperationException("Error indexing non-Collection.");
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
