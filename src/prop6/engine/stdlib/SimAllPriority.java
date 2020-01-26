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

import java.util.ArrayList;

import esl2.types.ArrayValue;
import esl2.types.FatalException;
import esl2.types.ProgrammingException;
import esl2.types.TypedOperationException;
import esl2.types.ValueType;
import prop6.engine.CallingContext;

public final class SimAllPriority extends StandardUnaryFunction
{

    public static final class SimThread implements Runnable
    {

        public TypedOperationException exception;
        public ValueType result;

        private final CallingContext context;
        private final ValueType arg1;
        private final ValueType arg2;

        public SimThread(CallingContext context, ValueType arg1, ValueType arg2)
        {
            exception = null;
            result = null;
            this.context = new CallingContext();
            this.context.copyFromHere(context);
            this.arg1 = arg1;
            this.arg2 = arg2;

            if (false == context.noModel())
            {
                // We'll need this Model's state to properly initialize the flights.
                this.context.pushModel(context.current());
            }
        }

        @Override
        public void run()
        {
            try
            {
                result = SimPriority.function(context, arg1, arg2);
            }
            catch (TypedOperationException e)
            {
                exception = e;
            }
        }
    }

    @Override
    public ValueType fun(CallingContext context, ValueType arg) throws FatalException, TypedOperationException
    {
        if (arg instanceof ArrayValue)
        {
            ArrayList<ValueType> src = ((ArrayValue)arg).value;
            // Early-out on empty array: nothing to simulate.
            if (true == src.isEmpty())
            {
                throw new TypedOperationException("Error trying to simulate all in empty Array.");
            }
            // Do real work: first, validate that this is an array of pairs.
            // SimPriority will validate that the first is an Object.
            ArrayList<SimThread> threads = new ArrayList<SimThread>(src.size());
            for (ValueType val : src)
            {
                if (false == (val instanceof ArrayValue))
                {
                    throw new TypedOperationException("Error trying to simulate all: element of Array is not also Array.");
                }

                if (2 != ((ArrayValue)val).value.size())
                {
                    throw new TypedOperationException("Error trying to simulate all: inner Array size is not exactly two.");
                }

                threads.add(new SimThread(context, ((ArrayValue)val).value.get(0), ((ArrayValue)val).value.get(1)));
            }
            ArrayList<Thread> pants = new ArrayList<Thread>(threads.size());
            for (SimThread thread : threads)
            {
                Thread newThread = new Thread(thread);
                newThread.start();
                pants.add(newThread);
            }
            for (Thread thread : pants)
            {
                try
                {
                    thread.join();
                }
                catch (InterruptedException e)
                {
                    throw new ProgrammingException("Someone interrupted me: " + e.getLocalizedMessage());
                }
            }
            ArrayList<ValueType> result = new ArrayList<ValueType>(pants.size());
            for (SimThread thread : threads)
            {
                if ((null == thread.exception) && (null == thread.result))
                {
                    throw new ProgrammingException("Didn't get anything from thread run.");
                }
                if (null != thread.result)
                {
                    result.add(thread.result);
                }
                else
                {
                    throw thread.exception;
                }
            }
            return new ArrayValue(result);
        }
        else
        {
            throw new TypedOperationException("Error trying to simulate all in non-Array.");
        }
    }

}
