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

public final class DoubleValue extends ValueType
{

    public final double value;

    public DoubleValue(double value)
    {
        this.value = value;
    }

    @Override
    public String getTypeName()
    {
        return "Double";
    }

    /////////////////////////////////////////////////////////////////////////
    /// Operations implemented by this data type
    /////////////////////////////////////////////////////////////////////////

    @Override
    public ValueType neg() throws TypedOperationException
    {
        return new DoubleValue(-value);
    }

    @Override
    public boolean logical() throws TypedOperationException
    {
        return (value == 0.0) ? false : true;
    }

    @Override
    public ValueType add(DoubleValue lhs) throws TypedOperationException
    {
        return new DoubleValue(lhs.value + value);
    }

    @Override
    public ValueType sub(DoubleValue lhs) throws TypedOperationException
    {
        return new DoubleValue(lhs.value - value);
    }

    @Override
    public ValueType mul(DoubleValue lhs) throws TypedOperationException
    {
        return new DoubleValue(lhs.value * value);
    }

    @Override
    public ValueType mul(VectorValue lhs) throws TypedOperationException
    {
        return new VectorValue(lhs.value.mul(value));
    }

    @Override
    public ValueType mul(QuaternionValue lhs) throws TypedOperationException
    {
        return new QuaternionValue(lhs.value.mul(value));
    }

    @Override
    public ValueType mul(MatrixValue lhs) throws TypedOperationException
    {
        return new MatrixValue(lhs.value.mul(value));
    }

    @Override
    public ValueType div(DoubleValue lhs) throws TypedOperationException
    {
        return new DoubleValue(lhs.value / value);
    }

    @Override
    public ValueType div(VectorValue lhs) throws TypedOperationException
    {
        return new VectorValue(lhs.value.div(value));
    }

    @Override
    public ValueType div(QuaternionValue lhs) throws TypedOperationException
    {
        return new QuaternionValue(lhs.value.div(value));
    }

    @Override
    public ValueType div(MatrixValue lhs) throws TypedOperationException
    {
        return new MatrixValue(lhs.value.div(value));
    }

    @Override
    public ValueType power(DoubleValue lhs) throws TypedOperationException
    {
        return new DoubleValue(Math.pow(lhs.value, value));
    }

    @Override
    public ValueType power(QuaternionValue lhs) throws TypedOperationException
    {
        if (-1.0 == value)
        {
            return new QuaternionValue(lhs.value.invert());
        }
        else
        {
            throw new TypedOperationException("Error: taking a Quaternion to an arbitrary power is unsupported");
        }
    }

    @Override
    public ValueType power(MatrixValue lhs) throws TypedOperationException
    {
        if (-1.0 == value)
        {
            return new MatrixValue(lhs.value.invert());
        }
        else
        {
            throw new TypedOperationException("Error: taking a Matrix to an arbitrary power is unsupported");
        }
    }

    @Override
    public boolean greater(DoubleValue lhs) throws TypedOperationException
    {
        return lhs.value > value;
    }

    @Override
    public boolean less(DoubleValue lhs) throws TypedOperationException
    {
        return lhs.value < value;
    }

    @Override
    public boolean geq(DoubleValue lhs) throws TypedOperationException
    {
        return lhs.value >= value;
    }

    @Override
    public boolean leq(DoubleValue lhs) throws TypedOperationException
    {
        return lhs.value <= value;
    }

    @Override
    public boolean equal(DoubleValue lhs) throws TypedOperationException
    {
        return lhs.value == value;
    }

    @Override
    public boolean notEqual(DoubleValue lhs) throws TypedOperationException
    {
        return lhs.value != value;
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
    /// Sort implementation functions
    /////////////////////////////////////////////////////////////////////////

    @Override
    public int sort(DoubleValue lhs)
    {
        if (lhs.value == value)
        {
            return 0;
        }
        else
        {
            return lhs.value < value ? -1 : 1;
        }
    }

    @Override
    public int sort(VectorValue lhs)
    {
        return 1;
    }

    @Override
    public int sort(QuaternionValue lhs)
    {
        return 1;
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

    @Override
    public int hashCode()
    {
        return Double.hashCode(value);
    }

}
