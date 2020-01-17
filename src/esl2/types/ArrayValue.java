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

import java.util.ArrayList;

public final class ArrayValue extends ValueType
{

    /**
     * Woe be unto he who does not treat this as immutable.
     */
    public final ArrayList<ValueType> value;

    public ArrayValue(ArrayList<ValueType> value)
    {
        this.value = value;
    }

    @Override
    public String getTypeName()
    {
        return "Array";
    }

    /////////////////////////////////////////////////////////////////////////
    /// Operations implemented by this data type
    /////////////////////////////////////////////////////////////////////////

    @Override
    public ValueType neg() throws TypedOperationException
    {
        ArrayList<ValueType> newArray = new ArrayList<ValueType>(value.size());
        for (ValueType e : value)
        {
            newArray.add(e.neg());
        }
        return new ArrayValue(newArray);
    }

    @Override
    public ValueType add(DoubleValue lhs) throws TypedOperationException
    {
        ArrayList<ValueType> newArray = new ArrayList<ValueType>(value.size());
        for (ValueType e : value)
        {
            newArray.add(lhs.add(e));
        }
        return new ArrayValue(newArray);
    }

    @Override
    public ValueType add(VectorValue lhs) throws TypedOperationException
    {
        ArrayList<ValueType> newArray = new ArrayList<ValueType>(value.size());
        for (ValueType e : value)
        {
            newArray.add(lhs.add(e));
        }
        return new ArrayValue(newArray);
    }

    @Override
    public ValueType add(QuaternionValue lhs) throws TypedOperationException
    {
        ArrayList<ValueType> newArray = new ArrayList<ValueType>(value.size());
        for (ValueType e : value)
        {
            newArray.add(lhs.add(e));
        }
        return new ArrayValue(newArray);
    }

    @Override
    public ValueType add(MatrixValue lhs) throws TypedOperationException
    {
        ArrayList<ValueType> newArray = new ArrayList<ValueType>(value.size());
        for (ValueType e : value)
        {
            newArray.add(lhs.add(e));
        }
        return new ArrayValue(newArray);
    }

    @Override
    public ValueType add(StringValue lhs) throws TypedOperationException
    {
        ArrayList<ValueType> newArray = new ArrayList<ValueType>(value.size());
        for (ValueType e : value)
        {
            newArray.add(lhs.add(e));
        }
        return new ArrayValue(newArray);
    }

    @Override
    public ValueType add(ArrayValue lhs) throws TypedOperationException
    {
        ArrayList<ValueType> newArray = new ArrayList<ValueType>(value.size());
        for (ValueType e : value)
        {
            newArray.add(lhs.add(e));
        }
        return new ArrayValue(newArray);
    }

    @Override
    public ValueType add(DictionaryValue lhs) throws TypedOperationException
    {
        ArrayList<ValueType> newArray = new ArrayList<ValueType>(value.size());
        for (ValueType e : value)
        {
            newArray.add(lhs.add(e));
        }
        return new ArrayValue(newArray);
    }

    @Override
    public ValueType sub(DoubleValue lhs) throws TypedOperationException
    {
        ArrayList<ValueType> newArray = new ArrayList<ValueType>(value.size());
        for (ValueType e : value)
        {
            newArray.add(lhs.sub(e));
        }
        return new ArrayValue(newArray);
    }

    @Override
    public ValueType sub(VectorValue lhs) throws TypedOperationException
    {
        ArrayList<ValueType> newArray = new ArrayList<ValueType>(value.size());
        for (ValueType e : value)
        {
            newArray.add(lhs.sub(e));
        }
        return new ArrayValue(newArray);
    }

    @Override
    public ValueType sub(QuaternionValue lhs) throws TypedOperationException
    {
        ArrayList<ValueType> newArray = new ArrayList<ValueType>(value.size());
        for (ValueType e : value)
        {
            newArray.add(lhs.sub(e));
        }
        return new ArrayValue(newArray);
    }

    @Override
    public ValueType sub(MatrixValue lhs) throws TypedOperationException
    {
        ArrayList<ValueType> newArray = new ArrayList<ValueType>(value.size());
        for (ValueType e : value)
        {
            newArray.add(lhs.sub(e));
        }
        return new ArrayValue(newArray);
    }

    @Override
    public ValueType sub(ArrayValue lhs) throws TypedOperationException
    {
        ArrayList<ValueType> newArray = new ArrayList<ValueType>(value.size());
        for (ValueType e : value)
        {
            newArray.add(lhs.sub(e));
        }
        return new ArrayValue(newArray);
    }

    @Override
    public ValueType sub(DictionaryValue lhs) throws TypedOperationException
    {
        ArrayList<ValueType> newArray = new ArrayList<ValueType>(value.size());
        for (ValueType e : value)
        {
            newArray.add(lhs.sub(e));
        }
        return new ArrayValue(newArray);
    }

    @Override
    public ValueType mul(DoubleValue lhs) throws TypedOperationException
    {
        ArrayList<ValueType> newArray = new ArrayList<ValueType>(value.size());
        for (ValueType e : value)
        {
            newArray.add(lhs.mul(e));
        }
        return new ArrayValue(newArray);
    }

    @Override
    public ValueType mul(VectorValue lhs) throws TypedOperationException
    {
        ArrayList<ValueType> newArray = new ArrayList<ValueType>(value.size());
        for (ValueType e : value)
        {
            newArray.add(lhs.mul(e));
        }
        return new ArrayValue(newArray);
    }

    @Override
    public ValueType mul(QuaternionValue lhs) throws TypedOperationException
    {
        ArrayList<ValueType> newArray = new ArrayList<ValueType>(value.size());
        for (ValueType e : value)
        {
            newArray.add(lhs.mul(e));
        }
        return new ArrayValue(newArray);
    }

    @Override
    public ValueType mul(MatrixValue lhs) throws TypedOperationException
    {
        ArrayList<ValueType> newArray = new ArrayList<ValueType>(value.size());
        for (ValueType e : value)
        {
            newArray.add(lhs.mul(e));
        }
        return new ArrayValue(newArray);
    }

    @Override
    public ValueType mul(ArrayValue lhs) throws TypedOperationException
    {
        ArrayList<ValueType> newArray = new ArrayList<ValueType>(value.size());
        for (ValueType e : value)
        {
            newArray.add(lhs.mul(e));
        }
        return new ArrayValue(newArray);
    }

    @Override
    public ValueType mul(DictionaryValue lhs) throws TypedOperationException
    {
        ArrayList<ValueType> newArray = new ArrayList<ValueType>(value.size());
        for (ValueType e : value)
        {
            newArray.add(lhs.mul(e));
        }
        return new ArrayValue(newArray);
    }

    @Override
    public ValueType div(DoubleValue lhs) throws TypedOperationException
    {
        ArrayList<ValueType> newArray = new ArrayList<ValueType>(value.size());
        for (ValueType e : value)
        {
            newArray.add(lhs.div(e));
        }
        return new ArrayValue(newArray);
    }

    @Override
    public ValueType div(ArrayValue lhs) throws TypedOperationException
    {
        ArrayList<ValueType> newArray = new ArrayList<ValueType>(value.size());
        for (ValueType e : value)
        {
            newArray.add(lhs.div(e));
        }
        return new ArrayValue(newArray);
    }

    @Override
    public ValueType div(DictionaryValue lhs) throws TypedOperationException
    {
        ArrayList<ValueType> newArray = new ArrayList<ValueType>(value.size());
        for (ValueType e : value)
        {
            newArray.add(lhs.div(e));
        }
        return new ArrayValue(newArray);
    }

    @Override
    public ValueType cross(VectorValue lhs) throws TypedOperationException
    {
        ArrayList<ValueType> newArray = new ArrayList<ValueType>(value.size());
        for (ValueType e : value)
        {
            newArray.add(lhs.cross(e));
        }
        return new ArrayValue(newArray);
    }

    @Override
    public ValueType cross(ArrayValue lhs) throws TypedOperationException
    {
        ArrayList<ValueType> newArray = new ArrayList<ValueType>(value.size());
        for (ValueType e : value)
        {
            newArray.add(lhs.cross(e));
        }
        return new ArrayValue(newArray);
    }

    @Override
    public ValueType cross(DictionaryValue lhs) throws TypedOperationException
    {
        ArrayList<ValueType> newArray = new ArrayList<ValueType>(value.size());
        for (ValueType e : value)
        {
            newArray.add(lhs.cross(e));
        }
        return new ArrayValue(newArray);
    }

    @Override
    public boolean equal(ArrayValue lhs) throws TypedOperationException
    {
        boolean isEqual = true;
        if (lhs.value.size() == value.size())
        {
            for (int i = 0; (i < lhs.value.size()) && (true == isEqual); ++i)
            {
                isEqual &= lhs.value.get(i).compare(value.get(i));
            }
        }
        else
        {
            isEqual = false;
        }
        return isEqual;
    }

    @Override
    public boolean notEqual(ArrayValue lhs) throws TypedOperationException
    {
        return false == equal(lhs);
    }

    /////////////////////////////////////////////////////////////////////////
    /// Visitor implementation functions
    /////////////////////////////////////////////////////////////////////////

    @Override
    public ValueType add(ValueType rhs) throws TypedOperationException
    {
        ArrayList<ValueType> newArray = new ArrayList<ValueType>(value.size());
        for (ValueType e : value)
        {
            newArray.add(e.add(rhs));
        }
        return new ArrayValue(newArray);
    }

    @Override
    public ValueType sub(ValueType rhs) throws TypedOperationException
    {
        ArrayList<ValueType> newArray = new ArrayList<ValueType>(value.size());
        for (ValueType e : value)
        {
            newArray.add(e.sub(rhs));
        }
        return new ArrayValue(newArray);
    }

    @Override
    public ValueType mul(ValueType rhs) throws TypedOperationException
    {
        ArrayList<ValueType> newArray = new ArrayList<ValueType>(value.size());
        for (ValueType e : value)
        {
            newArray.add(e.mul(rhs));
        }
        return new ArrayValue(newArray);
    }

    @Override
    public ValueType div(ValueType rhs) throws TypedOperationException
    {
        ArrayList<ValueType> newArray = new ArrayList<ValueType>(value.size());
        for (ValueType e : value)
        {
            newArray.add(e.div(rhs));
        }
        return new ArrayValue(newArray);
    }

    @Override
    public ValueType cross(ValueType rhs) throws TypedOperationException
    {
        ArrayList<ValueType> newArray = new ArrayList<ValueType>(value.size());
        for (ValueType e : value)
        {
            newArray.add(e.cross(rhs));
        }
        return new ArrayValue(newArray);
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
        int isLess = 0;
        if (lhs.value.size() == value.size())
        {
            for (int i = 0; i < lhs.value.size(); ++i)
            {
                isLess = lhs.value.get(i).sort(value.get(i));
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
    public int sort(DictionaryValue lhs)
    {
        return 1;
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
