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

import java.util.ArrayList;
import java.util.Map.Entry;

import esl2.engine.CallingContext;
import esl2.engine.ConstantsSingleton;
import esl2.engine.FlowControl;
import esl2.engine.Getter;
import esl2.engine.Setter;
import esl2.engine.expression.Constant;
import esl2.engine.expression.Expression;
import esl2.engine.expression.GEQ;
import esl2.engine.expression.LEQ;
import esl2.engine.expression.Plus;
import esl2.input.Token;
import esl2.types.ArrayValue;
import esl2.types.DictionaryValue;
import esl2.types.DoubleValue;
import esl2.types.FatalException;
import esl2.types.TypedOperationException;
import esl2.types.ValueType;

public final class ForStatement extends Statement
{

    public final Getter getter;
    public final Setter setter;
    public final Expression lower;
    public final boolean to;
    public final Expression upper;
    public final Expression step;
    public final Statement seq;
    public final int id;

    public ForStatement(Token token, Getter getter, Setter setter, Expression lower, boolean to, Expression upper, Expression step, Statement seq, int id)
    {
        super(token);
        this.getter = getter;
        this.setter = setter;
        this.lower = lower;
        this.to = to;
        this.upper = upper;
        this.step = step;
        this.seq = seq;
        this.id = id;
    }

    @Override
    public FlowControl execute(CallingContext context) throws TypedOperationException, FatalException
    {
        ValueType currentValue = lower.evaluate(context);

        if (null == upper)
        {
            return collIter(context, currentValue);
        }
        else
        {
            return loopIter(context, currentValue);
        }
    }

    private FlowControl loopIter(CallingContext context, ValueType currentValue) throws TypedOperationException, FatalException
    {
        ValueType UPPER = upper.evaluate(context);
        ValueType STEP = null;
        if (null == step)
        {
            if (true == to)
            {
                STEP = ConstantsSingleton.getInstance().DOUBLE_ONE;
            }
            else
            {
                STEP = new DoubleValue(-1.0);
            }
        }
        else
        {
            STEP = step.evaluate(context);
        }
        Expression del = new Constant(token, STEP);

        while (true)
        {
            setter.set(context, currentValue);

            Expression lcv = new Constant(token, currentValue);
            Expression limit = new Constant(token, UPPER);
            Expression comparator = null;
            if (true == to)
            {
                comparator = new LEQ(token, lcv, limit);
            }
            else
            {
                comparator = new GEQ(token, lcv, limit);
            }

            boolean conditional = false;
            try
            {
                conditional = comparator.evaluate(context).logical();
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

            if (false == conditional)
            {
                break;
            }

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

            Expression plus = new Plus(token, lcv, del);
            currentValue = plus.evaluate(context);
        }
        return null;
    }

    private FlowControl collIter(CallingContext context, ValueType currentValue) throws TypedOperationException, FatalException
    {
        if (currentValue instanceof ArrayValue)
        {
            return arrayIter(context, (ArrayValue) currentValue);
        }
        else if (currentValue instanceof DictionaryValue)
        {
            return dictIter(context, (DictionaryValue) currentValue);
        }
        else
        {
            TypedOperationException toThrow = new TypedOperationException("Error iterating over non-Collection.");
            String rethrow = Expression.constructMessage(toThrow, token);
            if (null != context.debugger)
            {
                context.debugger.EnterDebugger(rethrow, context);
            }
            throw new TypedOperationException(rethrow);
        }
    }

    private FlowControl arrayIter(CallingContext context, ArrayValue currentValue) throws TypedOperationException, FatalException
    {
        for (ValueType iter : currentValue.value)
        {
            setter.set(context, iter);

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
        }
        return null;
    }

    private FlowControl dictIter(CallingContext context, DictionaryValue currentValue) throws TypedOperationException, FatalException
    {
        for (Entry<ValueType, ValueType> iter : currentValue.value.entrySet())
        {
            ArrayList<ValueType> currImpl = new ArrayList<ValueType>();
            currImpl.add(iter.getKey());
            currImpl.add(iter.getValue());
            ArrayValue currIter = new ArrayValue(currImpl);
            setter.set(context, currIter);

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
        }
        return null;
    }

}
