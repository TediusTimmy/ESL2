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

import esl2.types.ArrayValue;
import esl2.types.DoubleValue;
import esl2.types.TypedOperationException;
import esl2.types.ValueType;

public final class GetIndex extends StandardBinaryFunction
{

    @Override
    public ValueType fun(ValueType arg1, ValueType arg2) throws TypedOperationException
    {
        return function(arg1, arg2);
    }

    public static ValueType function(ValueType arg1, ValueType arg2) throws TypedOperationException
    {
        if (arg1 instanceof ArrayValue)
        {
            if (arg2 instanceof DoubleValue)
            {
                ArrayValue array = (ArrayValue)arg1;
                DoubleValue index = (DoubleValue)arg2;
                if ((0.0 <= index.value) && (array.value.size() > index.value))
                {
                    return array.value.get((int)index.value);
                }
                else
                {
                    throw new TypedOperationException("Array Index Out-of-Bounds.");
                }
            }
            else
            {
                throw new TypedOperationException("Error indexing with non-Double.");
            }
        }
        else
        {
            throw new TypedOperationException("Error indexing into non-Array.");
        }
    }

}
