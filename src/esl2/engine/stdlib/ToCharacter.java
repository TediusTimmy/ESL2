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

public final class ToCharacter extends StandardUnaryFunction
{

    @Override
    public ValueType fun(ValueType arg) throws TypedOperationException
    {
        if (arg instanceof DoubleValue)
        {
            double src = ((DoubleValue)arg).value;
            if ((Character.MAX_CODE_POINT >= src) && (Character.MIN_CODE_POINT <= src))
            {
                return new StringValue(Character.toString((char)src));
            }
            else
            {
                throw new TypedOperationException("Double is not a valid character code point.");
            }
        }
        else
        {
            throw new TypedOperationException("Error trying to convert non-Double to single character String.");
        }
    }

}
