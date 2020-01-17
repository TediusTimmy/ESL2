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

import esl2.types.MatrixValue;
import esl2.types.QuaternionValue;
import esl2.types.TypedOperationException;
import esl2.types.ValueType;

public final class Invert extends StandardUnaryFunction
{

    @Override
    public ValueType fun(ValueType arg) throws TypedOperationException
    {
        if (arg instanceof QuaternionValue)
        {
            return new QuaternionValue(((QuaternionValue)arg).value.invert());
        }
        else if (arg instanceof MatrixValue)
        {
            return new MatrixValue(((MatrixValue)arg).value.invert());
        }
        else
        {
            throw new TypedOperationException("Error trying to invert non- Quaternion or Matrix.");
        }
    }

}
