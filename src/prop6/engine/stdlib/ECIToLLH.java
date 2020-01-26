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

import esl2.types.DoubleValue;
import esl2.types.TypedOperationException;
import esl2.types.ValueType;
import esl2.types.Vector;
import esl2.types.VectorValue;
import prop6.engine.CallingContext;

public final class ECIToLLH extends StandardBinaryFunction
{

    @Override
    public ValueType fun(CallingContext context, ValueType arg1, ValueType arg2) throws TypedOperationException
    {
        if (arg1 instanceof VectorValue)
        {
            if (arg2 instanceof DoubleValue)
            {
                Vector temp = context.settings.earth.ECItoLLH(((VectorValue)arg1).value, ((DoubleValue)arg2).value);
                return new VectorValue(new Vector(Math.toDegrees(temp.x), Math.toDegrees(temp.y), temp.z));
            }
            else
            {
                throw new TypedOperationException("Time was not Double.");
            }
        }
        else
        {
            throw new TypedOperationException("Error transforming frame of non-Vector.");
        }
    }

}
