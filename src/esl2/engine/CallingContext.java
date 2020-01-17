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

import esl2.input.Token;

public class CallingContext
{

    public CallingContext()
    {
        currentFrame = null;

        executor = null;
        debugging = false;

        // Debugger Hook
        debugger = null;
    }

    public CallingContext(Executor executor, boolean debugging, DebugFunctor debugger)
    {
        this.executor = executor;
        this.debugging = debugging;
        this.debugger = debugger;
    }

    public void pushContext(StackFrame frame, int location, Token callingToken)
    {
        frame.prev = currentFrame;
        currentFrame = frame;
        if (true == debugging)
        {
            frame.debug = getFrame(location, callingToken);
            if (null != frame.prev)
            {
                frame.prev.debug.next = frame;
                frame.debug.depth = frame.prev.debug.depth + 1;
            }
        }
    }

    public void popContext(StackFrame frame)
    {
        if (currentFrame == frame)
        {
            currentFrame = currentFrame.prev;
        }
    }

    public DebugStackFrame getFrame(int location, Token callingToken)
    {
        return new DebugStackFrame(currentFrame, callingToken, executor.debugFrames.get(location));
    }

    public StackFrame currentFrame;

    public Executor executor;
    public boolean debugging;

    public DebugFunctor debugger;
}
