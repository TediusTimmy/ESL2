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

package prop6.engine.stdlib;

import esl2.engine.ConstantsSingleton;
import esl2.engine.stdlib.StandardUnaryFunction;
import esl2.types.ValueType;
import prop6.types.StateValue;

public final class IsState extends StandardUnaryFunction
{

    @Override
    public ValueType fun(ValueType arg)
    {
        if (arg instanceof StateValue)
        {
            return ConstantsSingleton.getInstance().DOUBLE_ONE;
        }
        else
        {
            return ConstantsSingleton.getInstance().DOUBLE_ZERO;
        }
    }

}
