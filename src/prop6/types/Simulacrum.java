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

import java.util.ArrayList;
import java.util.TreeMap;

import prop6.engine.CallWrapper;

public final class Simulacrum // I wanted to call this System, but what a headache!
{

    public static final String name = "--SYSTEM--";

    // There is one, global function list, in CallingContext.
    public final CallWrapper getInitialObject;
    public final TreeMap<String, SimObject> objects;

    public final ArrayList<LogMessage> messages;

    public Simulacrum(CallWrapper getInitialObject)
    {
        this.getInitialObject = getInitialObject;
        objects = new TreeMap<String, SimObject>();
        messages = new ArrayList<LogMessage>();
    }

}
