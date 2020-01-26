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

import esl2.types.FatalException;
import esl2.types.TypedOperationException;
import esl2.types.ValueType;
import esl2.types.Vector;
import esl2.types.VectorValue;
import prop6.engine.CallingContext;

public final class InverseGeo extends StandardBinaryFunction
{

    @Override
    public ValueType fun(CallingContext context, ValueType arg1, ValueType arg2) throws TypedOperationException, FatalException
    {
        if (arg1 instanceof VectorValue)
        {
            if (arg2 instanceof VectorValue)
            {
                Vector temp1 = ((VectorValue)arg1).value, temp2 = ((VectorValue)arg2).value;
                Vector p1 = new Vector(Math.toRadians(temp1.x), Math.toRadians(temp1.y), 0.0);
                Vector p2 = new Vector(Math.toRadians(temp2.x), Math.toRadians(temp2.y), 0.0);
                Vector res = context.settings.earth.inverse(p1, p2);
                double y = 90.0 - Math.toDegrees(res.y); // Convert bearing to canonical degrees.
                if (y < 0.0)
                {
                    y += 360.0; // Normalize it [0,360)
                }
                return new VectorValue(new Vector(res.x, y, 0.0));
            }
            else
            {
                throw new TypedOperationException("End point was not Vector.");
            }
        }
        else
        {
            throw new TypedOperationException("Start point was not Vector.");
        }
    }

}
