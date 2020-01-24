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

import esl2.engine.statement.Statement;
import esl2.parser.FrameDebugInfo;

/**
 * This class needs to go away.
 * It is a container for static data needed to execute an expression,
 * or debug one.
 */
public class Executor
{

    public final ArrayList<Integer> args;
    public final ArrayList<Integer> locals;
    public final ArrayList<Statement> functions;
    public final ArrayList<String> funNames;
    public final ArrayList<FrameDebugInfo> debugFrames;

    public Executor()
    {
        args = new ArrayList<Integer>();
        locals = new ArrayList<Integer>();
        functions = new ArrayList<Statement>();
        funNames = new ArrayList<String>();
        debugFrames = new ArrayList<FrameDebugInfo>();
    }

    public Executor duplicate()
    {
        Executor result = new Executor();
        result.args.addAll(args);
        result.locals.addAll(locals);
        result.functions.addAll(functions);
        result.funNames.addAll(funNames);
        result.debugFrames.addAll(debugFrames);
        return result;
    }

}
