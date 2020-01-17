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

import java.util.ArrayList;

import esl2.types.ArrayValue;
import esl2.types.MatrixValue;
import esl2.types.TypedOperationException;
import esl2.types.ValueType;

public final class Transpose extends StandardUnaryFunction
{

    @Override
    public ValueType fun(ValueType arg) throws TypedOperationException
    {
        if (arg instanceof MatrixValue)
        {
            return new MatrixValue(((MatrixValue)arg).value.transpose());
        }
        else if (arg instanceof ArrayValue)
        {
            ArrayList<ValueType> src = ((ArrayValue)arg).value;
            // Early-out on empty array: its transpose is itself.
            if (true == src.isEmpty())
            {
                return arg;
            }
            // Do real work: first, validate that this is an array of arrays that are rectangular.
            int firstSize = -1;
            for (ValueType val : src)
            {
                if (false == (val instanceof ArrayValue))
                {
                    throw new TypedOperationException("Error trying to transpose Array: element of Array is not also Array.");
                }
                
                if (-1 == firstSize)
                {
                    firstSize = ((ArrayValue)val).value.size();
                }
                else if (((ArrayValue)val).value.size() != firstSize)
                {
                    throw new TypedOperationException("Error trying to transpose Array: Array of Arrays are not rectangular.");
                }
            }
            // Can't transpose nx0 Matrix
            if (0 == firstSize)
            {
                throw new TypedOperationException("Error trying to transpose Array: Contained Arrays are empty.");
            }
            // Good to go. Do Transpose.
            ArrayValue result = new ArrayValue(new ArrayList<ValueType>());
            for (int i = 0; i < firstSize; ++i)
            {
                ArrayValue temp = new ArrayValue(new ArrayList<ValueType>());
                for (ValueType val : src)
                {
                    temp.value.add(((ArrayValue)val).value.get(i));
                }
                result.value.add(temp);
            }
            return result;
        }
        else
        {
            throw new TypedOperationException("Error trying to transpose non- Matrix or Array.");
        }
    }

}
