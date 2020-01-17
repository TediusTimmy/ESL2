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

public abstract class ValueType implements Comparable<ValueType>
{
    public abstract String getTypeName();
    
    public ValueType neg() throws TypedOperationException
    {
        throw new TypedOperationException("Error negating " + getTypeName());
    }

    public boolean logical() throws TypedOperationException
    {
        throw new TypedOperationException("Error converting " + getTypeName() + " to a boolean value");
    }

    private ValueType die1(ValueType lhs, String y, String z) throws TypedOperationException
    {
        throw new TypedOperationException("Error " + y + " " + lhs.getTypeName() + " " + z + " " + getTypeName());
    }
    private ValueType die2(ValueType lhs, String x, String y, String z) throws TypedOperationException
    {
        throw new TypedOperationException("Error " + x + " " + lhs.getTypeName() + " " + y + " " + getTypeName() + " " + z);
    }
    private boolean die3(ValueType lhs, String x) throws TypedOperationException
    {
        throw new TypedOperationException("Error comparing " + lhs.getTypeName() + " to " + getTypeName() + " (" + x + ")");
    }

    public abstract ValueType add (ValueType rhs) throws TypedOperationException;
    public ValueType add (DoubleValue lhs) throws TypedOperationException { return die1(lhs, "adding", "to"); }
    public ValueType add (VectorValue lhs) throws TypedOperationException { return die1(lhs, "adding", "to"); }
    public ValueType add (QuaternionValue lhs) throws TypedOperationException { return die1(lhs, "adding", "to"); }
    public ValueType add (MatrixValue lhs) throws TypedOperationException { return die1(lhs, "adding", "to"); }
    public ValueType add (StringValue lhs) throws TypedOperationException { return die1(lhs, "adding", "to"); }
    public ValueType add (ArrayValue lhs) throws TypedOperationException { return die1(lhs, "adding", "to"); }
    public ValueType add (DictionaryValue lhs) throws TypedOperationException { return die1(lhs, "adding", "to"); }
    public ValueType add (ExtendedValue lhs) throws TypedOperationException { return die1(lhs, "adding", "to"); }
    public ValueType add (FunctionPointerValue lhs) throws TypedOperationException { return die1(lhs, "adding", "to"); }

    public abstract ValueType sub (ValueType rhs) throws TypedOperationException;
    public ValueType sub (DoubleValue lhs) throws TypedOperationException { return die1(lhs, "subtracting", "from"); }
    public ValueType sub (VectorValue lhs) throws TypedOperationException { return die1(lhs, "subtracting", "from"); }
    public ValueType sub (QuaternionValue lhs) throws TypedOperationException { return die1(lhs, "subtracting", "from"); }
    public ValueType sub (MatrixValue lhs) throws TypedOperationException { return die1(lhs, "subtracting", "from"); }
    public ValueType sub (StringValue lhs) throws TypedOperationException { return die1(lhs, "subtracting", "from"); }
    public ValueType sub (ArrayValue lhs) throws TypedOperationException { return die1(lhs, "subtracting", "from"); }
    public ValueType sub (DictionaryValue lhs) throws TypedOperationException { return die1(lhs, "subtracting", "from"); }
    public ValueType sub (ExtendedValue lhs) throws TypedOperationException { return die1(lhs, "subtracting", "from"); }
    public ValueType sub (FunctionPointerValue lhs) throws TypedOperationException { return die1(lhs, "subtracting", "from"); }

    public abstract ValueType mul (ValueType rhs) throws TypedOperationException;
    public ValueType mul (DoubleValue lhs) throws TypedOperationException { return die1(lhs, "multiplying", "by"); }
    public ValueType mul (VectorValue lhs) throws TypedOperationException { return die1(lhs, "multiplying", "by"); }
    public ValueType mul (QuaternionValue lhs) throws TypedOperationException { return die1(lhs, "multiplying", "by"); }
    public ValueType mul (MatrixValue lhs) throws TypedOperationException { return die1(lhs, "multiplying", "by"); }
    public ValueType mul (StringValue lhs) throws TypedOperationException { return die1(lhs, "multiplying", "by"); }
    public ValueType mul (ArrayValue lhs) throws TypedOperationException { return die1(lhs, "multiplying", "by"); }
    public ValueType mul (DictionaryValue lhs) throws TypedOperationException { return die1(lhs, "multiplying", "by"); }
    public ValueType mul (ExtendedValue lhs) throws TypedOperationException { return die1(lhs, "multiplying", "by"); }
    public ValueType mul (FunctionPointerValue lhs) throws TypedOperationException { return die1(lhs, "multiplying", "by"); }

    public abstract ValueType div (ValueType rhs) throws TypedOperationException;
    public ValueType div (DoubleValue lhs) throws TypedOperationException { return die1(lhs, "dividing", "by"); }
    public ValueType div (VectorValue lhs) throws TypedOperationException { return die1(lhs, "dividing", "by"); }
    public ValueType div (QuaternionValue lhs) throws TypedOperationException { return die1(lhs, "dividing", "by"); }
    public ValueType div (MatrixValue lhs) throws TypedOperationException { return die1(lhs, "dividing", "by"); }
    public ValueType div (StringValue lhs) throws TypedOperationException { return die1(lhs, "dividing", "by"); }
    public ValueType div (ArrayValue lhs) throws TypedOperationException { return die1(lhs, "dividing", "by"); }
    public ValueType div (DictionaryValue lhs) throws TypedOperationException { return die1(lhs, "dividing", "by"); }
    public ValueType div (ExtendedValue lhs) throws TypedOperationException { return die1(lhs, "dividing", "by"); }
    public ValueType div (FunctionPointerValue lhs) throws TypedOperationException { return die1(lhs, "dividing", "by"); }

    public abstract ValueType power (ValueType rhs) throws TypedOperationException;
    public ValueType power (DoubleValue lhs) throws TypedOperationException { return die2(lhs, "taking", "to", "power"); }
    public ValueType power (VectorValue lhs) throws TypedOperationException { return die2(lhs, "taking", "to", "power"); }
    public ValueType power (QuaternionValue lhs) throws TypedOperationException { return die2(lhs, "taking", "to", "power"); }
    public ValueType power (MatrixValue lhs) throws TypedOperationException { return die2(lhs, "taking", "to", "power"); }
    public ValueType power (StringValue lhs) throws TypedOperationException { return die2(lhs, "taking", "to", "power"); }
    public ValueType power (ArrayValue lhs) throws TypedOperationException { return die2(lhs, "taking", "to", "power"); }
    public ValueType power (DictionaryValue lhs) throws TypedOperationException { return die2(lhs, "taking", "to", "power"); }
    public ValueType power (ExtendedValue lhs) throws TypedOperationException { return die2(lhs, "taking", "to", "power"); }
    public ValueType power (FunctionPointerValue lhs) throws TypedOperationException { return die2(lhs, "taking", "to", "power"); }

    public abstract ValueType cross (ValueType rhs) throws TypedOperationException;
    public ValueType cross (DoubleValue lhs) throws TypedOperationException { return die1(lhs, "taking cross product of", "and"); }
    public ValueType cross (VectorValue lhs) throws TypedOperationException { return die1(lhs, "taking cross product of", "and"); }
    public ValueType cross (QuaternionValue lhs) throws TypedOperationException { return die1(lhs, "taking cross product of", "and"); }
    public ValueType cross (MatrixValue lhs) throws TypedOperationException { return die1(lhs, "taking cross product of", "and"); }
    public ValueType cross (StringValue lhs) throws TypedOperationException { return die1(lhs, "taking cross product of", "and"); }
    public ValueType cross (ArrayValue lhs) throws TypedOperationException { return die1(lhs, "taking cross product of", "and"); }
    public ValueType cross (DictionaryValue lhs) throws TypedOperationException { return die1(lhs, "taking cross product of", "and"); }
    public ValueType cross (ExtendedValue lhs) throws TypedOperationException { return die1(lhs, "taking cross product of", "and"); }
    public ValueType cross (FunctionPointerValue lhs) throws TypedOperationException { return die1(lhs, "taking cross product of", "and"); }

    public abstract boolean greater (ValueType rhs) throws TypedOperationException;
    public boolean greater (DoubleValue lhs) throws TypedOperationException { return die3(lhs, ">"); }
    public boolean greater (VectorValue lhs) throws TypedOperationException { return die3(lhs, ">"); }
    public boolean greater (QuaternionValue lhs) throws TypedOperationException { return die3(lhs, ">"); }
    public boolean greater (MatrixValue lhs) throws TypedOperationException { return die3(lhs, ">"); }
    public boolean greater (StringValue lhs) throws TypedOperationException { return die3(lhs, ">"); }
    public boolean greater (ArrayValue lhs) throws TypedOperationException { return die3(lhs, ">"); }
    public boolean greater (DictionaryValue lhs) throws TypedOperationException { return die3(lhs, ">"); }
    public boolean greater (ExtendedValue lhs) throws TypedOperationException { return die3(lhs, ">"); }
    public boolean greater (FunctionPointerValue lhs) throws TypedOperationException { return die3(lhs, ">"); }

    public abstract boolean less (ValueType rhs) throws TypedOperationException;
    public boolean less (DoubleValue lhs) throws TypedOperationException { return die3(lhs, "<"); }
    public boolean less (VectorValue lhs) throws TypedOperationException { return die3(lhs, "<"); }
    public boolean less (QuaternionValue lhs) throws TypedOperationException { return die3(lhs, "<"); }
    public boolean less (MatrixValue lhs) throws TypedOperationException { return die3(lhs, "<"); }
    public boolean less (StringValue lhs) throws TypedOperationException { return die3(lhs, "<"); }
    public boolean less (ArrayValue lhs) throws TypedOperationException { return die3(lhs, "<"); }
    public boolean less (DictionaryValue lhs) throws TypedOperationException { return die3(lhs, "<"); }
    public boolean less (ExtendedValue lhs) throws TypedOperationException { return die3(lhs, "<"); }
    public boolean less (FunctionPointerValue lhs) throws TypedOperationException { return die3(lhs, "<"); }

    public abstract boolean geq (ValueType rhs) throws TypedOperationException;
    public boolean geq (DoubleValue lhs) throws TypedOperationException { return die3(lhs, ">="); }
    public boolean geq (VectorValue lhs) throws TypedOperationException { return die3(lhs, ">="); }
    public boolean geq (QuaternionValue lhs) throws TypedOperationException { return die3(lhs, ">="); }
    public boolean geq (MatrixValue lhs) throws TypedOperationException { return die3(lhs, ">="); }
    public boolean geq (StringValue lhs) throws TypedOperationException { return die3(lhs, ">="); }
    public boolean geq (ArrayValue lhs) throws TypedOperationException { return die3(lhs, ">="); }
    public boolean geq (DictionaryValue lhs) throws TypedOperationException { return die3(lhs, ">="); }
    public boolean geq (ExtendedValue lhs) throws TypedOperationException { return die3(lhs, ">="); }
    public boolean geq (FunctionPointerValue lhs) throws TypedOperationException { return die3(lhs, ">="); }

    public abstract boolean leq (ValueType rhs) throws TypedOperationException;
    public boolean leq (DoubleValue lhs) throws TypedOperationException { return die3(lhs, "<="); }
    public boolean leq (VectorValue lhs) throws TypedOperationException { return die3(lhs, "<="); }
    public boolean leq (QuaternionValue lhs) throws TypedOperationException { return die3(lhs, "<="); }
    public boolean leq (MatrixValue lhs) throws TypedOperationException { return die3(lhs, "<="); }
    public boolean leq (StringValue lhs) throws TypedOperationException { return die3(lhs, "<="); }
    public boolean leq (ArrayValue lhs) throws TypedOperationException { return die3(lhs, "<="); }
    public boolean leq (DictionaryValue lhs) throws TypedOperationException { return die3(lhs, "<="); }
    public boolean leq (ExtendedValue lhs) throws TypedOperationException { return die3(lhs, "<="); }
    public boolean leq (FunctionPointerValue lhs) throws TypedOperationException { return die3(lhs, "<="); }

    public abstract boolean equal (ValueType rhs) throws TypedOperationException;
    public boolean equal (DoubleValue lhs) throws TypedOperationException { return die3(lhs, "=="); }
    public boolean equal (VectorValue lhs) throws TypedOperationException { return die3(lhs, "=="); }
    public boolean equal (QuaternionValue lhs) throws TypedOperationException { return die3(lhs, "=="); }
    public boolean equal (MatrixValue lhs) throws TypedOperationException { return die3(lhs, "=="); }
    public boolean equal (StringValue lhs) throws TypedOperationException { return die3(lhs, "=="); }
    public boolean equal (ArrayValue lhs) throws TypedOperationException { return die3(lhs, "=="); }
    public boolean equal (DictionaryValue lhs) throws TypedOperationException { return die3(lhs, "=="); }
    public boolean equal (ExtendedValue lhs) throws TypedOperationException { return die3(lhs, "=="); }
    public boolean equal (FunctionPointerValue lhs) throws TypedOperationException { return die3(lhs, "=="); }

    public abstract boolean notEqual (ValueType rhs) throws TypedOperationException;
    public boolean notEqual (DoubleValue lhs) throws TypedOperationException { return die3(lhs, "<>"); }
    public boolean notEqual (VectorValue lhs) throws TypedOperationException { return die3(lhs, "<>"); }
    public boolean notEqual (QuaternionValue lhs) throws TypedOperationException { return die3(lhs, "<>"); }
    public boolean notEqual (MatrixValue lhs) throws TypedOperationException { return die3(lhs, "<>"); }
    public boolean notEqual (StringValue lhs) throws TypedOperationException { return die3(lhs, "<>"); }
    public boolean notEqual (ArrayValue lhs) throws TypedOperationException { return die3(lhs, "<>"); }
    public boolean notEqual (DictionaryValue lhs) throws TypedOperationException { return die3(lhs, "<>"); }
    public boolean notEqual (ExtendedValue lhs) throws TypedOperationException { return die3(lhs, "<>"); }
    public boolean notEqual (FunctionPointerValue lhs) throws TypedOperationException { return die3(lhs, "<>"); }

    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof ValueType)
        {
            return compare((ValueType)obj);
        }
        return false;
    }
    public boolean compare(ValueType rhs)
    {
        if (true == rhs.getClass().equals(getClass()))
        {
            try
            {
                return equal(rhs);
            }
            catch(TypedOperationException e)
            {
                throw new ProgrammingException("objects of same type threw TypedOperationException when compared");
            }
        }
        return false;
    }

    @Override
    public int compareTo(ValueType rhs)
    {
        return sort(rhs);
    }

    public abstract int sort (ValueType rhs);
    public abstract int sort (DoubleValue lhs);
    public abstract int sort (VectorValue lhs);
    public abstract int sort (QuaternionValue lhs);
    public abstract int sort (MatrixValue lhs);
    public abstract int sort (StringValue lhs);
    public abstract int sort (ArrayValue lhs);
    public abstract int sort (DictionaryValue lhs);
    public abstract int sort (ExtendedValue lhs);
    public abstract int sort (FunctionPointerValue lhs);

    @Override
    public abstract int hashCode();

    // This is the algorithm that the Boost C++ libraries use for combining hashes.
    public static int boost_hash_combine(int seed, int value)
    {
        return seed ^ (value + 0x9E3779B9 + (seed << 6) + (seed >>> 2));
    }

}
