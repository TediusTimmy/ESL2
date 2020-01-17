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

import esl2.types.Matrix;
import esl2.types.MatrixValue;
import esl2.types.TypedOperationException;
import esl2.types.ValueType;
import esl2.types.VectorValue;

public final class MakeMatRows extends StandardTernaryFunction
{

    @Override
    public ValueType fun(ValueType arg1, ValueType arg2, ValueType arg3) throws TypedOperationException
    {
        if (arg1 instanceof VectorValue)
        {
            if (arg2 instanceof VectorValue)
            {
                if (arg3 instanceof VectorValue)
                {
                    VectorValue src1 = (VectorValue)arg1, src2 = (VectorValue)arg2, src3 = (VectorValue)arg3;
                    return new MatrixValue(new Matrix(
                        src1.value.x, src1.value.y, src1.value.z,
                        src2.value.x, src2.value.y, src2.value.z,
                        src3.value.x, src3.value.y, src3.value.z
                        ));
                }
                else
                {
                    throw new TypedOperationException("Error building Matrix: argument three is non-Vector.");
                }
            }
            else
            {
                throw new TypedOperationException("Error building Matrix: argument two is non-Vector.");
            }
        }
        else
        {
            throw new TypedOperationException("Error building Matrix: argument one is non-Vector.");
        }
    }

}
