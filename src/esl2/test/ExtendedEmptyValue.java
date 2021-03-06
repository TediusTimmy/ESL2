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

package esl2.test;

import esl2.types.ExtendedValue;
import esl2.types.TypedOperationException;

public final class ExtendedEmptyValue extends ExtendedValue
{

    @Override
    public String getTypeName()
    {
        return "Empty";
    }

    @Override
    public boolean equal(ExtendedValue lhs) throws TypedOperationException
    {
        if (lhs instanceof ExtendedEmptyValue)
        {
            return true;
        }
        throw new TypedOperationException("Error comparing " + lhs.getTypeName() + " to " + getTypeName() + " (=)");
    }

    @Override
    public int sort(ExtendedValue lhs)
    {
        if (lhs instanceof ExtendedScalarValue)
        {
            return -1;
        }
        else if (lhs instanceof ExtendedEmptyValue)
        {
            return 0;
        }
        return 1;
    }

    @Override
    public int hashCode()
    {
        return 0;
    }

}
