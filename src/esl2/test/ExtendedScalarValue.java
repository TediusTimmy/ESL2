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

public final class ExtendedScalarValue extends ExtendedValue
{

    final double value;

    public ExtendedScalarValue(double value)
    {
        this.value = value;
    }

    @Override
    public String getTypeName()
    {
        return "Extended Scalar";
    }

    @Override
    public boolean equal(ExtendedValue lhs) throws TypedOperationException
    {
        if (lhs instanceof ExtendedScalarValue)
        {
            return ((ExtendedScalarValue)lhs).value == value;
        }
        throw new TypedOperationException("Error comparing " + lhs.getTypeName() + " to " + getTypeName() + " (=)");
    }

    @Override
    public int sort(ExtendedValue lhs)
    {
        if (lhs instanceof ExtendedScalarValue)
        {
            ExtendedScalarValue LHS = (ExtendedScalarValue)lhs;
            if (LHS.value == value)
            {
                return 0;
            }
            return LHS.value < value ? -1 : 1;
        }
        else if (lhs instanceof ExtendedEmptyValue)
        {
            return 1;
        }
        return 1;
    }

    @Override
    public int hashCode()
    {
        return Double.hashCode(value);
    }

}
