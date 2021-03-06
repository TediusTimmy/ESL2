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
import esl2.types.TypedOperationException;
import esl2.types.ValueType;

public final class PopFront extends StandardUnaryFunction
{

    @Override
    public ValueType fun(ValueType arg) throws TypedOperationException
    {
        if (arg instanceof ArrayValue)
        {
            ArrayValue src = (ArrayValue)arg;
            if (false == src.value.isEmpty())
            {
                ArrayValue result = new ArrayValue(new ArrayList<ValueType>());
                result.value.addAll(src.value.subList(1, src.value.size()));
                return result;
            }
            else
            {
                throw new TypedOperationException("Error popping front of empty Array.");
            }
        }
        else
        {
            throw new TypedOperationException("Error popping back of non-Array.");
        }
    }

}
