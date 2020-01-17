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

import esl2.types.DoubleValue;
import esl2.types.StringValue;
import esl2.types.TypedOperationException;
import esl2.types.ValueType;

public final class SubString extends StandardTernaryFunction
{

    @Override
    public ValueType fun(ValueType arg1, ValueType arg2, ValueType arg3) throws TypedOperationException
    {
        if (arg1 instanceof StringValue)
        {
            if (arg2 instanceof DoubleValue)
            {
                if (arg3 instanceof DoubleValue)
                {
                    String value = ((StringValue)arg1).value;
                    double start = ((DoubleValue)arg2).value;
                    double end = ((DoubleValue)arg3).value;
                    if ((start >= 0.0) && (start <= value.length()) &&
                        (end >= 0.0) && (end <= value.length()) &&
                        (end >= start))
                    {
                        return new StringValue(value.substring((int)start, (int)end));
                    }
                    else
                    {
                        throw new TypedOperationException("Error getting substring with either beginning or ending index not in string, or ending before beginning.");
                    }
                }
                else
                {
                    throw new TypedOperationException("Error getting substring with non-Double ending position.");
                }
            }
            else
            {
                throw new TypedOperationException("Error getting substring with non-Double starting position.");
            }
        }
        else
        {
            throw new TypedOperationException("Error getting substring of non-String.");
        }
    }

}
