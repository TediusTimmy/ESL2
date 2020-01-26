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

import java.util.ArrayList;

import esl2.types.ArrayValue;
import esl2.types.FatalException;
import esl2.types.TypedOperationException;
import esl2.types.ValueType;
import prop6.engine.CallingContext;

public final class SimAllPriorityST extends StandardUnaryFunction
{

    @Override
    public ValueType fun(CallingContext context, ValueType arg) throws FatalException, TypedOperationException
    {
        if (arg instanceof ArrayValue)
        {
            ArrayList<ValueType> src = ((ArrayValue)arg).value;
            // Early-out on empty array: nothing to simulate.
            if (true == src.isEmpty())
            {
                throw new TypedOperationException("Error trying to simulate all in empty Array.");
            }
            // Do real work: first, validate that this is an array of pairs.
            // SimPriority will validate that the first is an Object.
            for (ValueType val : src)
            {
                if (false == (val instanceof ArrayValue))
                {
                    throw new TypedOperationException("Error trying to simulate all: element of Array is not also Array.");
                }
    
                if (2 != ((ArrayValue)val).value.size())
                {
                    throw new TypedOperationException("Error trying to simulate all: inner Array size is not exactly two.");
                }
            }
            // Simulate each in the list and collect it's result.
            ArrayList<ValueType> result = new ArrayList<ValueType>(src.size());
            for (ValueType val : src)
            {
                result.add(SimPriority.function(context, ((ArrayValue)val).value.get(0), ((ArrayValue)val).value.get(1)));
            }
            return new ArrayValue(result);
        }
        else
        {
            throw new TypedOperationException("Error trying to simulate all in non-Array.");
        }
    }

}
