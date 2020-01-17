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
import esl2.types.TypedOperationException;
import esl2.types.ValueType;

public final class Max extends StandardBinaryFunction
{

    @Override
    public ValueType fun(ValueType arg1, ValueType arg2) throws TypedOperationException
    {
        if (arg1 instanceof DoubleValue)
        {
            if (arg2 instanceof DoubleValue)
            {
                double src1 = ((DoubleValue)arg1).value, src2 = ((DoubleValue)arg2).value;
                if (true == Double.isNaN(src1))
                {
                    return arg1;
                }
                else if (true == Double.isNaN(src2))
                {
                    return arg2;
                }
                return (src1 >= src2) ? arg1 : arg2;
            }
            else
            {
                throw new TypedOperationException("Error computing Max with non-Double second argument.");
            }
        }
        else
        {
            throw new TypedOperationException("Error computing Max with non-Double first argument.");
        }
    }

}
