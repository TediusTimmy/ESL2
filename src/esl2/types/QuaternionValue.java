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

public final class QuaternionValue extends ValueType
{

    public final Quaternion value;

    public QuaternionValue(Quaternion value)
    {
        this.value = value;
    }

    @Override
    public String getTypeName()
    {
        return "Quaternion";
    }

    /////////////////////////////////////////////////////////////////////////
    /// Operations implemented by this data type
    /////////////////////////////////////////////////////////////////////////

    @Override
    public ValueType neg() throws TypedOperationException
    {
        return new QuaternionValue(value.neg());
    }

    @Override
    public ValueType add(QuaternionValue lhs) throws TypedOperationException
    {
        return new QuaternionValue(lhs.value.add(value));
    }

    @Override
    public ValueType sub(QuaternionValue lhs) throws TypedOperationException
    {
        return new QuaternionValue(lhs.value.sub(value));
    }

    @Override
    public ValueType mul(DoubleValue lhs) throws TypedOperationException
    {
        return new QuaternionValue(value.mul(lhs.value));
    }

    @Override
    public ValueType mul(VectorValue lhs) throws TypedOperationException
    {
        return new QuaternionValue(lhs.value.mul(value));
    }

    @Override
    public ValueType mul(QuaternionValue lhs) throws TypedOperationException
    {
        return new QuaternionValue(lhs.value.mul(value));
    }

    @Override
    public boolean equal(QuaternionValue lhs) throws TypedOperationException
    {
        return lhs.value.equals(value);
    }

    @Override
    public boolean notEqual(QuaternionValue lhs) throws TypedOperationException
    {
        return false == lhs.value.equals(value);
    }

    /////////////////////////////////////////////////////////////////////////
    /// Visitor implementation functions
    /////////////////////////////////////////////////////////////////////////

    @Override public ValueType add(ValueType rhs) throws TypedOperationException { return rhs.add(this); }
    @Override public ValueType sub(ValueType rhs) throws TypedOperationException { return rhs.sub(this); }
    @Override public ValueType mul(ValueType rhs) throws TypedOperationException { return rhs.mul(this); }
    @Override public ValueType div(ValueType rhs) throws TypedOperationException { return rhs.div(this); }
    @Override public ValueType power(ValueType rhs) throws TypedOperationException { return rhs.power(this); }
    @Override public ValueType cross(ValueType rhs) throws TypedOperationException { return rhs.cross(this); }
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
    
    public static int sort (Quaternion lhs, Quaternion rhs)
    {
        if (lhs.s == rhs.s)
        {
            if (lhs.i == rhs.i)
            {
                if (lhs.j == rhs.j)
                {
                    if (lhs.k == rhs.k)
                    {
                        return 0;
                    }
                    else
                    {
                        return lhs.k < rhs.k ? -1 : 1;
                    }
                }
                else
                {
                    return lhs.j < rhs.j ? -1 : 1;
                }
            }
            else
            {
                return lhs.i < rhs.i ? -1 : 1;
            }
        }
        else
        {
            return lhs.s < rhs.s ? -1 : 1;
        }
    }

    @Override
    public int sort(QuaternionValue lhs)
    {
        return sort(lhs.value, value);
    }

    @Override
    public int sort(MatrixValue lhs)
    {
        return 1;
    }

    @Override
    public int sort(StringValue lhs)
    {
        return 1;
    }

    @Override
    public int sort(ArrayValue lhs)
    {
        return 1;
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

    public static int hashCode (Quaternion quat)
    {
        int temp = Double.hashCode(quat.s);
        temp = ValueType.boost_hash_combine(temp, Double.hashCode(quat.i));
        temp = ValueType.boost_hash_combine(temp, Double.hashCode(quat.j));
        return ValueType.boost_hash_combine(temp, Double.hashCode(quat.k));
    }

    @Override
    public int hashCode()
    {
        return hashCode(value);
    }

}
