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

package esl2.engine;

import java.util.ArrayList;
import java.util.TreeMap;

import esl2.engine.statement.NOP;
import esl2.types.ArrayValue;
import esl2.types.DictionaryValue;
import esl2.types.DoubleValue;
import esl2.types.ValueType;

public final class ConstantsSingleton
{

    private ConstantsSingleton()
    {
        // Double Zero
        DOUBLE_ZERO = new DoubleValue(0.0);
        // Double One
        DOUBLE_ONE = new DoubleValue(1.0);
        // Empty Array
        EMPTY_ARRAY = new ArrayValue(new ArrayList<ValueType>());
        // Empty Dictionary
        EMPTY_DICTIONARY = new DictionaryValue(new TreeMap<ValueType, ValueType>());
        // One True NOP
        ONE_TRUE_NOP = new NOP();
        // PI
        PI = new DoubleValue(Math.PI);
    }

    public final DoubleValue DOUBLE_ZERO;
    public final DoubleValue DOUBLE_ONE;
    public final ArrayValue EMPTY_ARRAY;
    public final DictionaryValue EMPTY_DICTIONARY;

    public final NOP ONE_TRUE_NOP;

    public final DoubleValue PI;

    // Utilize initialize-on-demand holder idiom.
    private static final class ConstantsHolder
    {
        private static final ConstantsSingleton INSTANCE = new ConstantsSingleton();
    }

    public static ConstantsSingleton getInstance()
    {
        return ConstantsHolder.INSTANCE;
    }

}
