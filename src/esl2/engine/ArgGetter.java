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

import esl2.types.ValueType;

public final class ArgGetter extends Getter
{

    private final int location;

    public ArgGetter(int location)
    {
        this.location = location;
    }

    @Override
    public ValueType get(CallingContext context)
    {
        return context.currentFrame.args.get(location);
    }

}
