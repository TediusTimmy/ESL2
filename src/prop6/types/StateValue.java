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
import esl2.types.MatrixValue;
import esl2.types.QuaternionValue;
import esl2.types.TypedOperationException;
import esl2.types.ValueType;
import esl2.types.VectorValue;

public final class StateValue extends ExtendedValue
{

    public final State value;

    public StateValue(State value)
    {
        this.value = value;
    }

    @Override
    public String getTypeName()
    {
        return "State";
    }

    @Override
    public boolean equal(ExtendedValue lhs) throws TypedOperationException
    {
        if (lhs instanceof StateValue)
        {
            return 0 == sort((StateValue)lhs);
        }
        throw new TypedOperationException("Error comparing " + lhs.getTypeName() + " to " + getTypeName() + " (=)");
    }

    @Override
    public int sort(ExtendedValue lhs)
    {
        if (lhs instanceof StateValue)
        {
            return sort((StateValue)lhs);
        }
        else if (lhs instanceof ObjectValue)
        {
            return 1;
        }
        return 1;
    }

    public int sort(StateValue lhs)
    {
        // We don't do nested ifs here, due to depth.
        if (lhs.value.time_s != value.time_s)
        {
            return lhs.value.time_s < value.time_s ? -1 : 1;
        }

        int sorted = VectorValue.sort(lhs.value.position, value.position);
        if (0 != sorted)
        {
            return sorted;
        }

        sorted = VectorValue.sort(lhs.value.velocity, value.velocity);
        if (0 != sorted)
        {
            return sorted;
        }

        sorted = VectorValue.sort(lhs.value.acceleration, value.acceleration);
        if (0 != sorted)
        {
            return sorted;
        }

        if (lhs.value.mass_kg != value.mass_kg)
        {
            return lhs.value.mass_kg < value.mass_kg ? -1 : 1;
        }

        if (lhs.value.flowRate_kg_s != value.flowRate_kg_s)
        {
            return lhs.value.flowRate_kg_s < value.flowRate_kg_s ? -1 : 1;
        }

        sorted = QuaternionValue.sort(lhs.value.orientation, value.orientation);
        if (0 != sorted)
        {
            return sorted;
        }

        // We won't be using derived data to sort on.

        sorted = VectorValue.sort(lhs.value.angularVelocity, value.angularVelocity);
        if (0 != sorted)
        {
            return sorted;
        }

        sorted = VectorValue.sort(lhs.value.angularAcceleration, value.angularAcceleration);
        if (0 != sorted)
        {
            return sorted;
        }

        sorted = MatrixValue.sort(lhs.value.MOI, value.MOI);
        if (0 != sorted)
        {
            return sorted;
        }

        return MatrixValue.sort(lhs.value.Idot, value.Idot);
    }

    @Override
    public int hashCode()
    {
        int hash = Double.hashCode(value.time_s);
        hash = ValueType.boost_hash_combine(hash, VectorValue.hashCode(value.position));
        hash = ValueType.boost_hash_combine(hash, VectorValue.hashCode(value.velocity));
        hash = ValueType.boost_hash_combine(hash, VectorValue.hashCode(value.acceleration));
        hash = ValueType.boost_hash_combine(hash, Double.hashCode(value.mass_kg));
        hash = ValueType.boost_hash_combine(hash, Double.hashCode(value.flowRate_kg_s));
        hash = ValueType.boost_hash_combine(hash, QuaternionValue.hashCode(value.orientation));
        hash = ValueType.boost_hash_combine(hash, VectorValue.hashCode(value.angularVelocity));
        hash = ValueType.boost_hash_combine(hash, VectorValue.hashCode(value.angularAcceleration));
        hash = ValueType.boost_hash_combine(hash, MatrixValue.hashCode(value.MOI));
        return ValueType.boost_hash_combine(hash, MatrixValue.hashCode(value.Idot));
    }

}
