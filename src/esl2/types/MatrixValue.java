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

public final class MatrixValue extends ValueType
{

    public final Matrix value;

    public MatrixValue(Matrix value)
    {
        this.value = value;
    }

    @Override
    public String getTypeName()
    {
        return "Matrix";
    }

    /////////////////////////////////////////////////////////////////////////
    /// Operations implemented by this data type
    /////////////////////////////////////////////////////////////////////////

    @Override
    public ValueType neg() throws TypedOperationException
    {
        return new MatrixValue(value.neg());
    }

    @Override
    public ValueType add(MatrixValue lhs) throws TypedOperationException
    {
        return new MatrixValue(lhs.value.add(value));
    }

    @Override
    public ValueType sub(MatrixValue lhs) throws TypedOperationException
    {
        return new MatrixValue(lhs.value.sub(value));
    }

    @Override
    public ValueType mul(DoubleValue lhs) throws TypedOperationException
    {
        return new MatrixValue(value.mul(lhs.value));
    }

    @Override
    public ValueType mul(VectorValue lhs) throws TypedOperationException
    {
        return new VectorValue(lhs.value.mul(value));
    }

    @Override
    public ValueType mul(MatrixValue lhs) throws TypedOperationException
    {
        return new MatrixValue(lhs.value.mul(value));
    }

    @Override
    public boolean equal(MatrixValue lhs) throws TypedOperationException
    {
        return lhs.value.equals(value);
    }

    @Override
    public boolean notEqual(MatrixValue lhs) throws TypedOperationException
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

    @Override
    public int sort(QuaternionValue lhs)
    {
        return -1;
    }
    
    public static int sort (Matrix lhs, Matrix rhs)
    {
        if (lhs.a11 == rhs.a11)
        {
            if (lhs.a12 == rhs.a12)
            {
                if (lhs.a13 == rhs.a13)
                {
                    if (lhs.a21 == rhs.a21)
                    {
                        if (lhs.a22 == rhs.a22)
                        {
                            if (lhs.a23 == rhs.a23)
                            {
                                if (lhs.a31 == rhs.a31)
                                {
                                    if (lhs.a32 == rhs.a32)
                                    {
                                        if (lhs.a33 == rhs.a33)
                                        {
                                            return 0;
                                        }
                                        else
                                        {
                                            return lhs.a33 < rhs.a33 ? -1 : 1;
                                        }
                                    }
                                    else
                                    {
                                        return lhs.a32 < rhs.a32 ? -1 : 1;
                                    }
                                }
                                else
                                {
                                    return lhs.a31 < rhs.a31 ? -1 : 1;
                                }
                            }
                            else
                            {
                                return lhs.a23 < rhs.a23 ? -1 : 1;
                            }
                        }
                        else
                        {
                            return lhs.a22 < rhs.a22 ? -1 : 1;
                        }
                    }
                    else
                    {
                        return lhs.a21 < rhs.a21 ? -1 : 1;
                    }
                }
                else
                {
                    return lhs.a13 < rhs.a13 ? -1 : 1;
                }
            }
            else
            {
                return lhs.a12 < rhs.a12 ? -1 : 1;
            }
        }
        else
        {
            return lhs.a11 < rhs.a11 ? -1 : 1;
        }
    }

    @Override
    public int sort(MatrixValue lhs)
    {
        return sort(lhs.value, value);
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

    public static int hashCode (Matrix mat)
    {
        int temp = Double.hashCode(mat.a11);
        temp = ValueType.boost_hash_combine(temp, Double.hashCode(mat.a12));
        temp = ValueType.boost_hash_combine(temp, Double.hashCode(mat.a13));
        temp = ValueType.boost_hash_combine(temp, Double.hashCode(mat.a21));
        temp = ValueType.boost_hash_combine(temp, Double.hashCode(mat.a22));
        temp = ValueType.boost_hash_combine(temp, Double.hashCode(mat.a23));
        temp = ValueType.boost_hash_combine(temp, Double.hashCode(mat.a31));
        temp = ValueType.boost_hash_combine(temp, Double.hashCode(mat.a32));
        return ValueType.boost_hash_combine(temp, Double.hashCode(mat.a33));
    }

    @Override
    public int hashCode()
    {
        return hashCode(value);
    }

}
