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

import java.util.ArrayList;

import esl2.engine.CallingContext;
import esl2.engine.FlowControl;
import esl2.engine.StackFrame;
import esl2.input.Token;
import esl2.types.FatalException;
import esl2.types.FunctionPointerValue;
import esl2.types.TypedOperationException;
import esl2.types.ValueType;

public final class FunctionCall extends Expression
{

    public final Expression location;
    public final ArrayList<Expression> args;

    public FunctionCall(Token token, Expression location, ArrayList<Expression> args)
    {
        super(token);
        this.location = location;
        this.args = args;
    }

    @Override
    public ValueType evaluate(CallingContext context) throws TypedOperationException, FatalException
    {
        ValueType LOC = location.evaluate(context);
        if (false == (LOC instanceof FunctionPointerValue))
        {
            String message = "Call to not a function in file " +
                token.sourceFile + " on line " + token.lineNumber + " at " + token.lineLocation;
            if (null != context.debugger)
            {
                context.debugger.EnterDebugger(message, context);
            }
            throw new FatalException(message);
        }
        int myLocation = ((FunctionPointerValue)LOC).value;
        if (args.size() != context.executor.args.get(myLocation).intValue())
        {
            String message = "Call to " + context.executor.funNames.get(myLocation) + " with " + Integer.toString(args.size()) +
                " arguments, but the function takes " + context.executor.locals.get(myLocation).toString() + " arguments in file " +
                token.sourceFile + " on line " + token.lineNumber + " at " + token.lineLocation;
            if (null != context.debugger)
            {
                context.debugger.EnterDebugger(message, context);
            }
            throw new FatalException(message);
        }
        StackFrame frame = new StackFrame(context, args.size(), context.executor.locals.get(myLocation).intValue());
        for (int i = 0; i < args.size(); ++i)
        {
            frame.args.set(i, args.get(i).evaluate(context));
        }
        context.pushContext(frame, myLocation, token);
        try
        {
            FlowControl result = null;
            try
            {
                result = context.executor.functions.get(myLocation).execute(context);
            }
            catch(TypedOperationException e)
            {    
                String rethrow = constructMessage(e);
                throw new TypedOperationException(rethrow);
            }
            if (null == result)
            {
                String message = "Function " + context.executor.funNames.get(myLocation) + " failed to return a value in file " +
                    token.sourceFile + " on line " + token.lineNumber + " at " + token.lineLocation;
                if (null != context.debugger)
                {
                    context.debugger.EnterDebugger(message, context);
                }
                throw new FatalException(message);
            }
            if (FlowControl.Type.RETURN != result.type)
            {
                String message = "Function " + context.executor.funNames.get(myLocation) + " had a break or continue outside a loop in file " +
                    result.source.sourceFile + " on line " + result.source.lineNumber + " at " + result.source.lineLocation;
                if (null != context.debugger)
                {
                    context.debugger.EnterDebugger(message, context);
                }
                throw new FatalException(message);
            }
            return result.value;
        }
        finally
        {
            context.popContext(frame);
        }
    }

}
