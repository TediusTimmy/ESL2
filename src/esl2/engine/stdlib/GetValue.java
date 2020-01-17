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

import esl2.types.DictionaryValue;
import esl2.types.TypedOperationException;
import esl2.types.ValueType;

public final class GetValue extends StandardBinaryFunction
{

    @Override
    public ValueType fun(ValueType arg1, ValueType arg2) throws TypedOperationException
    {
        return function(arg1, arg2);
    }

    public static ValueType function(ValueType arg1, ValueType arg2) throws TypedOperationException
    {
        if (arg1 instanceof DictionaryValue)
        {
            ValueType result = ((DictionaryValue)arg1).value.get(arg2);
            if (null == result)
            {
                throw new TypedOperationException("Key not found in Dictionary.");
            }
            return result;
        }
        else
        {
            throw new TypedOperationException("Error trying to retrieve key from non-Dictionary.");
        }
    }

}
