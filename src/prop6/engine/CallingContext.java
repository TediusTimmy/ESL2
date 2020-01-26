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

package prop6.engine;

import java.util.ArrayList;

import esl2.input.Token;
import esl2.parser.ParserLogger;
import prop6.types.SimObject;
import prop6.types.Simulacrum;
import prop6.types.Model;
import prop6.types.Settings;

public class CallingContext extends esl2.engine.CallingContext
{

    private final ArrayList<SimObject> objectStack;
    private final ArrayList<Model> modelStack;

    public CallingContext()
    {
        objectStack = new ArrayList<SimObject>();
        modelStack = new ArrayList<Model>();
    }

    public Settings settings;
    public Simulacrum simulacrum;
    public ParserLogger fileOut; // DebugPrint writes to here.
    public Propagator propagator;

    public SimObject top()
    {
        return objectStack.get(objectStack.size() - 1);
    }

    public void pushObject(SimObject obj)
    {
        objectStack.add(obj);
    }

    public void popObject()
    {
        objectStack.remove(objectStack.size() - 1);
    }

    public Model current()
    {
        return modelStack.get(modelStack.size() - 1);
    }

    public void pushModel(Model mod)
    {
        modelStack.add(mod);
    }

    public void popModel()
    {
        modelStack.remove(modelStack.size() - 1);
    }

    public boolean noModel()
    {
        return modelStack.isEmpty();
    }

    @Override
    public DebugStackFrame getFrame(int location, Token callingToken)
    {
        return new DebugStackFrame(currentFrame, callingToken, executor.debugFrames.get(location),
            top(), ((Executor)executor).objectDebugData.get(top().name));
    }

    public void copyFromHere(CallingContext src)
    {
        // Given that this only copies public members, it is a method of convenience alone.
        // ESL2::Engine::CallingContext
        // Ignore currentFrame, it should be null.
        executor = src.executor;
        debugger = src.debugger;
        // Engine::CallingContext
        settings = src.settings;
        simulacrum = src.simulacrum;
        fileOut = src.fileOut;
        propagator = src.propagator;
        // Don't touch the object/model stacks. They should be empty.
    }

}
