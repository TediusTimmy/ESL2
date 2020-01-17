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
import java.util.Collections;

import esl2.types.ArrayValue;
import esl2.types.DoubleValue;
import esl2.types.TypedOperationException;
import esl2.types.ValueType;

public final class NewArrayDefault extends StandardBinaryFunction
{

    @Override
    public ValueType fun(ValueType arg1, ValueType arg2) throws TypedOperationException
    {
        if (arg1 instanceof DoubleValue)
        {
            DoubleValue index = (DoubleValue)arg1;
            if ((0.0 <= index.value) && (Integer.MAX_VALUE >= index.value))
            {
                return new ArrayValue(new ArrayList<ValueType>(Collections.nCopies((int)index.value, arg2)));
            }
            else
            {
                throw new TypedOperationException("Error creating array size either negative, or too big.");
            }
        }
        else
        {
            throw new TypedOperationException("Error creating array with non-Double size.");
        }
    }

}
