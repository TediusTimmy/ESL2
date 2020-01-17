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

package esl2.types;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.TreeMap;

public final class DictionaryValue extends ValueType
{

    // If this is changed to a HashMap, the implementation of equal() needs to be changed, and sort() becomes unused.
    /**
     * Woe be unto he who does not treat this as immutable.
     */
    public final TreeMap<ValueType, ValueType> value;

    public DictionaryValue(TreeMap<ValueType, ValueType> value)
    {
        this.value = value;
    }

    @Override
    public String getTypeName()
    {
        return "Dictionary";
    }

    /////////////////////////////////////////////////////////////////////////
    /// Operations implemented by this data type
    /////////////////////////////////////////////////////////////////////////

    @Override
    public ValueType neg() throws TypedOperationException
    {
        TreeMap<ValueType, ValueType> newMap = new TreeMap<ValueType, ValueType>();
        for (Entry<ValueType, ValueType> e : value.entrySet())
        {
            newMap.put(e.getKey(), e.getValue().neg());
        }
        return new DictionaryValue(newMap);
    }

    @Override
    public ValueType add(DoubleValue lhs) throws TypedOperationException
    {
        TreeMap<ValueType, ValueType> newMap = new TreeMap<ValueType, ValueType>();
        for (Entry<ValueType, ValueType> e : value.entrySet())
        {
            newMap.put(e.getKey(), lhs.add(e.getValue()));
        }
        return new DictionaryValue(newMap);
    }

    @Override
    public ValueType add(VectorValue lhs) throws TypedOperationException
    {
        TreeMap<ValueType, ValueType> newMap = new TreeMap<ValueType, ValueType>();
        for (Entry<ValueType, ValueType> e : value.entrySet())
        {
            newMap.put(e.getKey(), lhs.add(e.getValue()));
        }
        return new DictionaryValue(newMap);
    }

    @Override
    public ValueType add(QuaternionValue lhs) throws TypedOperationException
    {
        TreeMap<ValueType, ValueType> newMap = new TreeMap<ValueType, ValueType>();
        for (Entry<ValueType, ValueType> e : value.entrySet())
        {
            newMap.put(e.getKey(), lhs.add(e.getValue()));
        }
        return new DictionaryValue(newMap);
    }

    @Override
    public ValueType add(MatrixValue lhs) throws TypedOperationException
    {
        TreeMap<ValueType, ValueType> newMap = new TreeMap<ValueType, ValueType>();
        for (Entry<ValueType, ValueType> e : value.entrySet())
        {
            newMap.put(e.getKey(), lhs.add(e.getValue()));
        }
        return new DictionaryValue(newMap);
    }

    @Override
    public ValueType add(StringValue lhs) throws TypedOperationException
    {
        TreeMap<ValueType, ValueType> newMap = new TreeMap<ValueType, ValueType>();
        for (Entry<ValueType, ValueType> e : value.entrySet())
        {
            newMap.put(e.getKey(), lhs.add(e.getValue()));
        }
        return new DictionaryValue(newMap);
    }

    @Override
    public ValueType add(ArrayValue lhs) throws TypedOperationException
    {
        TreeMap<ValueType, ValueType> newMap = new TreeMap<ValueType, ValueType>();
        for (Entry<ValueType, ValueType> e : value.entrySet())
        {
            newMap.put(e.getKey(), lhs.add(e.getValue()));
        }
        return new DictionaryValue(newMap);
    }

    @Override
    public ValueType add(DictionaryValue lhs) throws TypedOperationException
    {
        TreeMap<ValueType, ValueType> newMap = new TreeMap<ValueType, ValueType>();
        for (Entry<ValueType, ValueType> e : value.entrySet())
        {
            newMap.put(e.getKey(), lhs.add(e.getValue()));
        }
        return new DictionaryValue(newMap);
    }

    @Override
    public ValueType sub(DoubleValue lhs) throws TypedOperationException
    {
        TreeMap<ValueType, ValueType> newMap = new TreeMap<ValueType, ValueType>();
        for (Entry<ValueType, ValueType> e : value.entrySet())
        {
            newMap.put(e.getKey(), lhs.sub(e.getValue()));
        }
        return new DictionaryValue(newMap);
    }

    @Override
    public ValueType sub(VectorValue lhs) throws TypedOperationException
    {
        TreeMap<ValueType, ValueType> newMap = new TreeMap<ValueType, ValueType>();
        for (Entry<ValueType, ValueType> e : value.entrySet())
        {
            newMap.put(e.getKey(), lhs.sub(e.getValue()));
        }
        return new DictionaryValue(newMap);
    }

    @Override
    public ValueType sub(QuaternionValue lhs) throws TypedOperationException
    {
        TreeMap<ValueType, ValueType> newMap = new TreeMap<ValueType, ValueType>();
        for (Entry<ValueType, ValueType> e : value.entrySet())
        {
            newMap.put(e.getKey(), lhs.sub(e.getValue()));
        }
        return new DictionaryValue(newMap);
    }

    @Override
    public ValueType sub(MatrixValue lhs) throws TypedOperationException
    {
        TreeMap<ValueType, ValueType> newMap = new TreeMap<ValueType, ValueType>();
        for (Entry<ValueType, ValueType> e : value.entrySet())
        {
            newMap.put(e.getKey(), lhs.sub(e.getValue()));
        }
        return new DictionaryValue(newMap);
    }

    @Override
    public ValueType sub(ArrayValue lhs) throws TypedOperationException
    {
        TreeMap<ValueType, ValueType> newMap = new TreeMap<ValueType, ValueType>();
        for (Entry<ValueType, ValueType> e : value.entrySet())
        {
            newMap.put(e.getKey(), lhs.sub(e.getValue()));
        }
        return new DictionaryValue(newMap);
    }

    @Override
    public ValueType sub(DictionaryValue lhs) throws TypedOperationException
    {
        TreeMap<ValueType, ValueType> newMap = new TreeMap<ValueType, ValueType>();
        for (Entry<ValueType, ValueType> e : value.entrySet())
        {
            newMap.put(e.getKey(), lhs.sub(e.getValue()));
        }
        return new DictionaryValue(newMap);
    }

    @Override
    public ValueType mul(DoubleValue lhs) throws TypedOperationException
    {
        TreeMap<ValueType, ValueType> newMap = new TreeMap<ValueType, ValueType>();
        for (Entry<ValueType, ValueType> e : value.entrySet())
        {
            newMap.put(e.getKey(), lhs.mul(e.getValue()));
        }
        return new DictionaryValue(newMap);
    }

    @Override
    public ValueType mul(VectorValue lhs) throws TypedOperationException
    {
        TreeMap<ValueType, ValueType> newMap = new TreeMap<ValueType, ValueType>();
        for (Entry<ValueType, ValueType> e : value.entrySet())
        {
            newMap.put(e.getKey(), lhs.mul(e.getValue()));
        }
        return new DictionaryValue(newMap);
    }

    @Override
    public ValueType mul(QuaternionValue lhs) throws TypedOperationException
    {
        TreeMap<ValueType, ValueType> newMap = new TreeMap<ValueType, ValueType>();
        for (Entry<ValueType, ValueType> e : value.entrySet())
        {
            newMap.put(e.getKey(), lhs.mul(e.getValue()));
        }
        return new DictionaryValue(newMap);
    }

    @Override
    public ValueType mul(MatrixValue lhs) throws TypedOperationException
    {
        TreeMap<ValueType, ValueType> newMap = new TreeMap<ValueType, ValueType>();
        for (Entry<ValueType, ValueType> e : value.entrySet())
        {
            newMap.put(e.getKey(), lhs.mul(e.getValue()));
        }
        return new DictionaryValue(newMap);
    }

    @Override
    public ValueType mul(ArrayValue lhs) throws TypedOperationException
    {
        TreeMap<ValueType, ValueType> newMap = new TreeMap<ValueType, ValueType>();
        for (Entry<ValueType, ValueType> e : value.entrySet())
        {
            newMap.put(e.getKey(), lhs.mul(e.getValue()));
        }
        return new DictionaryValue(newMap);
    }

    @Override
    public ValueType mul(DictionaryValue lhs) throws TypedOperationException
    {
        TreeMap<ValueType, ValueType> newMap = new TreeMap<ValueType, ValueType>();
        for (Entry<ValueType, ValueType> e : value.entrySet())
        {
            newMap.put(e.getKey(), lhs.mul(e.getValue()));
        }
        return new DictionaryValue(newMap);
    }

    @Override
    public ValueType div(DoubleValue lhs) throws TypedOperationException
    {
        TreeMap<ValueType, ValueType> newMap = new TreeMap<ValueType, ValueType>();
        for (Entry<ValueType, ValueType> e : value.entrySet())
        {
            newMap.put(e.getKey(), lhs.div(e.getValue()));
        }
        return new DictionaryValue(newMap);
    }

    @Override
    public ValueType div(ArrayValue lhs) throws TypedOperationException
    {
        TreeMap<ValueType, ValueType> newMap = new TreeMap<ValueType, ValueType>();
        for (Entry<ValueType, ValueType> e : value.entrySet())
        {
            newMap.put(e.getKey(), lhs.div(e.getValue()));
        }
        return new DictionaryValue(newMap);
    }

    @Override
    public ValueType div(DictionaryValue lhs) throws TypedOperationException
    {
        TreeMap<ValueType, ValueType> newMap = new TreeMap<ValueType, ValueType>();
        for (Entry<ValueType, ValueType> e : value.entrySet())
        {
            newMap.put(e.getKey(), lhs.div(e.getValue()));
        }
        return new DictionaryValue(newMap);
    }

    @Override
    public ValueType cross(VectorValue lhs) throws TypedOperationException
    {
        TreeMap<ValueType, ValueType> newMap = new TreeMap<ValueType, ValueType>();
        for (Entry<ValueType, ValueType> e : value.entrySet())
        {
            newMap.put(e.getKey(), lhs.cross(e.getValue()));
        }
        return new DictionaryValue(newMap);
    }

    @Override
    public ValueType cross(ArrayValue lhs) throws TypedOperationException
    {
        TreeMap<ValueType, ValueType> newMap = new TreeMap<ValueType, ValueType>();
        for (Entry<ValueType, ValueType> e : value.entrySet())
        {
            newMap.put(e.getKey(), lhs.cross(e.getValue()));
        }
        return new DictionaryValue(newMap);
    }

    @Override
    public ValueType cross(DictionaryValue lhs) throws TypedOperationException
    {
        TreeMap<ValueType, ValueType> newMap = new TreeMap<ValueType, ValueType>();
        for (Entry<ValueType, ValueType> e : value.entrySet())
        {
            newMap.put(e.getKey(), lhs.cross(e.getValue()));
        }
        return new DictionaryValue(newMap);
    }

    @Override
    public boolean equal(DictionaryValue lhs) throws TypedOperationException
    {
        boolean isEqual = true;
        if (lhs.value.size() == value.size())
        {
            // This implementation assumes a sorted Map.
            // The unsorted Map version is more generic, but less efficient.
            Iterator<Entry<ValueType, ValueType>> iter1 = lhs.value.entrySet().iterator();
            Iterator<Entry<ValueType, ValueType>> iter2 = value.entrySet().iterator();
            while ((true == iter1.hasNext()) && (true == isEqual))
            {
                Entry<ValueType, ValueType> entry1 = iter1.next();
                Entry<ValueType, ValueType> entry2 = iter2.next();

                isEqual &= entry1.getKey().compare(entry2.getKey());
                if (true == isEqual)
                {
                    isEqual &= entry1.getValue().compare(entry2.getValue());
                }
            }
/*
             // This is the unsorted implementation.
             // A == B is true when A.size() == B.size() and
             //    for every Key in A, there exists the same Key in B
             //    whose mapped Value compares as Equal to the mapped Value in A
             for (Entry<ValueType, ValueType> e : lhs.value.entrySet())
             {
                 ValueType v2 = value.get(e.getKey());
                 if (null == v2) // There should never be nulls in Dictionaries.
                 {
                     isEqual = false;
                 }
                 else
                 {
                    isEqual = e.getValue().compare(v2);
                }
                if (false == isEqual)
                {
                    break;
                }
             }
*/
        }
        else
        {
            isEqual = false;
        }
        return isEqual;
    }

    @Override
    public boolean notEqual(DictionaryValue lhs) throws TypedOperationException
    {
        return false == equal(lhs);
    }

    /////////////////////////////////////////////////////////////////////////
    /// Visitor implementation functions
    /////////////////////////////////////////////////////////////////////////

    @Override
    public ValueType add(ValueType rhs) throws TypedOperationException
    {
        TreeMap<ValueType, ValueType> newMap = new TreeMap<ValueType, ValueType>();
        for (Entry<ValueType, ValueType> e : value.entrySet())
        {
            newMap.put(e.getKey(), e.getValue().add(rhs));
        }
        return new DictionaryValue(newMap);
    }

    @Override
    public ValueType sub(ValueType rhs) throws TypedOperationException
    {
        TreeMap<ValueType, ValueType> newMap = new TreeMap<ValueType, ValueType>();
        for (Entry<ValueType, ValueType> e : value.entrySet())
        {
            newMap.put(e.getKey(), e.getValue().sub(rhs));
        }
        return new DictionaryValue(newMap);
    }

    @Override
    public ValueType mul(ValueType rhs) throws TypedOperationException
    {
        TreeMap<ValueType, ValueType> newMap = new TreeMap<ValueType, ValueType>();
        for (Entry<ValueType, ValueType> e : value.entrySet())
        {
            newMap.put(e.getKey(), e.getValue().mul(rhs));
        }
        return new DictionaryValue(newMap);
    }

    @Override
    public ValueType div(ValueType rhs) throws TypedOperationException
    {
        TreeMap<ValueType, ValueType> newMap = new TreeMap<ValueType, ValueType>();
        for (Entry<ValueType, ValueType> e : value.entrySet())
        {
            newMap.put(e.getKey(), e.getValue().div(rhs));
        }
        return new DictionaryValue(newMap);
    }

    @Override
    public ValueType cross(ValueType rhs) throws TypedOperationException
    {
        TreeMap<ValueType, ValueType> newMap = new TreeMap<ValueType, ValueType>();
        for (Entry<ValueType, ValueType> e : value.entrySet())
        {
            newMap.put(e.getKey(), e.getValue().cross(rhs));
        }
        return new DictionaryValue(newMap);
    }


    @Override public ValueType power(ValueType rhs) throws TypedOperationException { return rhs.power(this); }
    @Override public boolean greater(ValueType rhs) throws TypedOperationException { return rhs.greater(this); }
    @Override public boolean less(ValueType rhs) throws TypedOperationException { return rhs.less(this); }
    @Override public boolean geq(ValueType rhs) throws TypedOperationException { return rhs.geq(this); }
    @Override public boolean leq(ValueType rhs) throws TypedOperationException { return rhs.leq(this); }
    @Override public boolean equal(ValueType rhs) throws TypedOperationException { return rhs.equal(this); }
    @Override public boolean notEqual(ValueType rhs) throws TypedOperationException { return rhs.notEqual(this); }
    @Override public int sort(ValueType rhs) { return rhs.sort(this); }

    /////////////////////////////////////////////////////////////////////////
    /// Sort (less than) implementation functions
    /////////////////////////////////////////////////////////////////////////

    @Override
    public int sort(DoubleValue lhs)
    {
        return -1;
    }

    @Override
    public int sort(VectorValue lhs)
    {
        return -1;
    }

    @Override
    public int sort(QuaternionValue lhs)
    {
        return -1;
    }

    @Override
    public int sort(MatrixValue lhs)
    {
        return -1;
    }

    @Override
    public int sort(StringValue lhs)
    {
        return -1;
    }

    @Override
    public int sort(ArrayValue lhs)
    {
        return -1;
    }

    @Override
    public int sort(DictionaryValue lhs)
    {
        int isLess = 0;
        if (lhs.value.size() == value.size())
        {
            Iterator<Entry<ValueType, ValueType>> iter1 = lhs.value.entrySet().iterator();
            Iterator<Entry<ValueType, ValueType>> iter2 = value.entrySet().iterator();
            while (true == iter1.hasNext())
            {
                Entry<ValueType, ValueType> entry1 = iter1.next();
                Entry<ValueType, ValueType> entry2 = iter2.next();

                isLess = entry1.getKey().sort(entry2.getKey());
                if (0 != isLess)
                {
                    break;
                }
                isLess = entry1.getValue().sort(entry2.getValue());
                if (0 != isLess)
                {
                    break;
                }
            }
        }
        else
        {
            isLess = lhs.value.size() < value.size() ? -1 : 1;
        }
        return isLess;
    }

    @Override
    public int sort(ExtendedValue lhs)
    {
        return 1;
    }

    @Override
    public int sort(FunctionPointerValue lhs)
    {
        return 1;
    }

    /////////////////////////////////////////////////////////////////////////
    /// Hash implementation function
    /////////////////////////////////////////////////////////////////////////

    @Override
    public int hashCode()
    {
        return value.hashCode();
    }

}
