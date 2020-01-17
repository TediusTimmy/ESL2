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

package esl2.engine.expression;

import esl2.engine.CallingContext;
import esl2.engine.stdlib.GetIndex;
import esl2.engine.stdlib.GetValue;
import esl2.input.Token;
import esl2.types.ArrayValue;
import esl2.types.DictionaryValue;
import esl2.types.FatalException;
import esl2.types.TypedOperationException;
import esl2.types.ValueType;

public final class DerefVar extends Expression
{

    public final Expression lhs, rhs;

    public DerefVar(Token token, Expression lhs, Expression rhs)
    {
        super(token);
        this.lhs = lhs;
        this.rhs = rhs;
    }

    @Override
    public ValueType evaluate(CallingContext context) throws TypedOperationException, FatalException
    {
        ValueType LHS = lhs.evaluate(context);
        ValueType RHS = rhs.evaluate(context);
        ValueType result = null;
        try
        {
            if (LHS instanceof ArrayValue)
            {
                result = GetIndex.function(LHS, RHS);
            }
            else if (LHS instanceof DictionaryValue)
            {
                result = GetValue.function(LHS, RHS);
            }
            else
            {
                throw new TypedOperationException("Error indexing non-Collection.");
            }
        }
        catch(TypedOperationException e)
        {
            String rethrow = constructMessage(e);
            if (null != context.debugger)
            {
                context.debugger.EnterDebugger(rethrow, context);
            }
            throw new TypedOperationException(rethrow);
        }
        return result;
    }

}
