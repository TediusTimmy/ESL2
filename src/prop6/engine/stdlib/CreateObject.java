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
import esl2.types.StringValue;
import esl2.types.TypedOperationException;
import esl2.types.ValueType;
import prop6.engine.CallingContext;
import prop6.random.ANSIC;
import prop6.random.ParkMiller;
import prop6.types.SimObject;
import prop6.types.Simulacrum;
import prop6.types.ObjectValue;

public final class CreateObject extends StandardBinaryFunction {

    @Override
    public ValueType fun(CallingContext context, ValueType arg1, ValueType arg2) throws TypedOperationException, FatalException
    {
        if (arg1 instanceof StringValue)
        {
            String name = ((StringValue)arg1).value;
            if (name.equals(Simulacrum.name)) // Don't allow anyone to create this object.
            {
                throw new FatalException("Cannot find object \"" + name + "\" in simulacrum.");
            }
            SimObject object = context.simulacrum.objects.get(name);
            if (null == object)
            {
                throw new FatalException("Cannot find object \"" + name + "\" in simulacrum.");
            }
            ObjectValue result = new ObjectValue(object.duplicate());
            result.value.mySeed = context.top().objRNG.duplicate();
            ParkMiller temp = result.value.mySeed.duplicate();
            context.top().objRNG.getNext();
            result.value.objRNG = new ParkMiller(temp.getNext());
            result.value.myRNG = new ANSIC(temp.getNext());
            context.pushObject(result.value);
            try
            {
                result.value.onObjectInit.execute(context, arg2);
            }
            finally
            {
                context.popObject();
            }
            return result;
        }
        else
        {
            throw new TypedOperationException("Object name not String in CreateObject.");
        }
    }

}
