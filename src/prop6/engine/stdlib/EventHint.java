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

import esl2.engine.ConstantsSingleton;
import esl2.types.DoubleValue;
import esl2.types.TypedOperationException;
import esl2.types.ValueType;
import prop6.engine.CallingContext;
import prop6.engine.Stepper;

public final class EventHint extends StandardUnaryFunction
{

    @Override
    public ValueType fun(CallingContext context, ValueType arg) throws TypedOperationException
    {
        if (arg instanceof DoubleValue)
        {
            double time = ((DoubleValue)arg).value;
            time = Math.rint(time * Stepper.HAIRS_PER_SECOND);
            if ((time >= context.current().currentTime) || (time <= (context.current().currentTime - context.current().deltaTime)))
            {
                // We can only hint at events in the current step.
                return ConstantsSingleton.getInstance().DOUBLE_ZERO;
            }
            else
            {
                // Only save the earliest hint for when the event will be.
                // We have to stop at all events, but we stop at the first event first.
                if ((0.0 == context.current().eventHint) || (time < context.current().eventHint))
                {
                    context.current().eventHint = time;
                }
                return ConstantsSingleton.getInstance().DOUBLE_ONE;
            }
        }
        else
        {
            throw new TypedOperationException("Error trying to hint event at non-Double time.");
        }
    }

}
