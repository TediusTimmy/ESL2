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

package esl2.parser;

import java.util.ArrayList;
import java.util.TreeMap;

public final class FrameDebugInfo
{

    public String frameName;

    public final TreeMap<String, Integer> args;
    public final TreeMap<String, Integer> locals;

    public final ArrayList<String> argNames;
    public final ArrayList<String> localNames;

    FrameDebugInfo()
    {
        args = new TreeMap<String, Integer>();
        locals = new TreeMap<String, Integer>();
        argNames = new ArrayList<String>();
        localNames = new ArrayList<String>();
    }

}
