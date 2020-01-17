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
import java.util.Collections;

import esl2.types.ValueType;

public final class StackFrame
{

    public final ArrayList<ValueType> args;
    public final ArrayList<ValueType> locals;
    public StackFrame prev;
    public DebugStackFrame debug;
    public final CallingContext context;

    public StackFrame(CallingContext context, int nargs, int nlocals)
    {
        args = new ArrayList<ValueType>(Collections.nCopies(nargs, null));
        locals = new ArrayList<ValueType>(Collections.nCopies(nlocals, null));
        prev = null;
        debug = null;
        this.context = context;
    }

}
