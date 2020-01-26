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

import esl2.engine.ConstantsSingleton;
import esl2.types.FatalException;
import esl2.types.TypedOperationException;
import esl2.types.ValueType;
import esl2.types.Vector;
import esl2.types.VectorValue;
import prop6.engine.CallingContext;

public final class InstantKick extends StandardBinaryFunction
{

    @Override
    public ValueType fun(CallingContext context, ValueType arg1, ValueType arg2) throws TypedOperationException, FatalException
    {
        if (arg1 instanceof VectorValue)
        {
            if (arg2 instanceof VectorValue)
            {
                context.current().velocityKick = context.current().velocityKick.add(((VectorValue)arg1).value);
                Vector rates_deg_s = ((VectorValue)arg2).value;
                Vector rates_rad_s = new Vector(Math.toRadians(rates_deg_s.x), Math.toRadians(rates_deg_s.y), Math.toRadians(rates_deg_s.z));
                context.current().ratesKick = context.current().ratesKick.add(rates_rad_s);
                context.current().instantKick = true;
                return ConstantsSingleton.getInstance().DOUBLE_ONE;
            }
            else
            {
                throw new TypedOperationException("Angular Velocity kick was not Vector.");
            }
        }
        else
        {
            throw new TypedOperationException("Velocity kick was not Vector.");
        }
    }

}
