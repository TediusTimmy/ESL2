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

package prop6.types;

import esl2.types.ExtendedValue;
import esl2.types.TypedOperationException;
import esl2.types.ValueType;

public final class ObjectValue extends ExtendedValue
{

    public final SimObject value;

    public ObjectValue(SimObject value)
    {
        this.value = value;
    }

    @Override
    public String getTypeName()
    {
        return "Object";
    }

    @Override
    public boolean equal(ExtendedValue lhs) throws TypedOperationException
    {
        if (lhs instanceof ObjectValue)
        {
            return equal((ObjectValue)lhs);
        }
        throw new TypedOperationException("Error comparing " + lhs.getTypeName() + " to " + getTypeName() + " (=)");
    }

    private boolean checkedCompare(ValueType lhs, ValueType rhs)
    {
        if ((null == lhs) && (null == rhs))
        {
            return true;
        }
        if ((null != lhs) && (null != rhs))
        {
            return lhs.compare(rhs);
        }
        return false;
    }

    public boolean equal(ObjectValue lhs) throws TypedOperationException
    {
        if (false == lhs.value.name.equals(value.name))
        {
            return false;
        }

        if (lhs.value.variables.size() != value.variables.size())
        {
            return false;
        }

        for (int i = 0; i < lhs.value.variables.size(); ++i)
        {
            if (false == checkedCompare(lhs.value.variables.get(i), value.variables.get(i)))
            {
                return false;
            }
        }

        return true;
    }

    @Override
    public int sort(ExtendedValue lhs)
    {
        if (lhs instanceof StateValue)
        {
            return -1;
        }
        else if (lhs instanceof ObjectValue)
        {
            return sort((ObjectValue)lhs);
        }
        return 1;
    }

    private int checkedSort(ValueType lhs, ValueType rhs)
    {
        if (null == lhs)
        {
            return -1;
        }
        if (null == rhs)
        {
            return 1;
        }
        return lhs.sort(rhs);
    }

    public int sort(ObjectValue lhs)
    {
        int sorted = lhs.value.name.compareTo(value.name);
        if (0 != sorted)
        {
            return sorted;
        }

        if (lhs.value.variables.size() != value.variables.size())
        {
            return lhs.value.variables.size() < value.variables.size() ? -1 : 1;
        }

        for (int i = 0; i < lhs.value.variables.size(); ++i)
        {
            sorted = checkedSort(lhs.value.variables.get(i), value.variables.get(i));
            if (0 != sorted)
            {
                return sorted;
            }
        }

        return 0;
    }

    private int checkedHashCode(ValueType val)
    {
        if (null == val)
        {
            return 0xDEADBEEF;
        }
        return val.hashCode();
    }

    @Override
    public int hashCode()
    {
        int hash = value.name.hashCode();
        hash = ValueType.boost_hash_combine(hash, Integer.hashCode(value.variables.size()));
        for (ValueType val : value.variables)
        {
            hash = ValueType.boost_hash_combine(hash, checkedHashCode(val));
        }
        return hash;
    }

}
