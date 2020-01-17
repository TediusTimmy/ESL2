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

import esl2.types.QuaternionValue;
import esl2.types.TypedOperationException;
import esl2.types.ValueType;
import esl2.types.VectorValue;

public final class RotScaleVecQuat extends StandardBinaryFunction
{

    @Override
    public ValueType fun(ValueType arg1, ValueType arg2) throws TypedOperationException
    {
        if (arg1 instanceof VectorValue)
        {
            if (arg2 instanceof QuaternionValue)
            {
                return new VectorValue(((QuaternionValue)arg2).value.rotateVector(((VectorValue)arg1).value));
            }
            else
            {
                throw new TypedOperationException("Error rotating with non-Quaternion.");
            }
        }
        else
        {
            throw new TypedOperationException("Error rotating non-Vector.");
        }
    }

}
