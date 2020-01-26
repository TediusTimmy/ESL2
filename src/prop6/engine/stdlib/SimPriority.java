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

import esl2.types.FatalException;
import esl2.types.StringValue;
import esl2.types.TypedOperationException;
import esl2.types.ValueType;
import prop6.engine.CallingContext;
import prop6.types.Model;
import prop6.types.ObjectValue;
import prop6.types.State;
import prop6.types.StateValue;

public final class SimPriority extends StandardBinaryFunction
{

    public static ValueType function(CallingContext context, ValueType arg1, ValueType arg2) throws TypedOperationException
    {
        if (arg1 instanceof ObjectValue)
        {
            ValueType result;
            Model model = new Model();

            boolean noModel = context.noModel();
            if (false == noModel)
            {
                model.copyFromHere(context.current());
            }

            context.pushModel(model);
            context.pushObject(model.object);
            try
            {
                if (false == noModel)
                {
                    model.copyFromHere(new Model(((ObjectValue)arg1).value, model.state, true, model.currentTime, model.prevDelta));
                    model.object.realizeArg = arg2;

                    model.object.onModelInit.execute(context, model.object.realizeArg);
                    model.object.onModelRealize.execute(context, model.object.realizeArg);
                }
                else
                {
                    model.copyFromHere(new Model(((ObjectValue)arg1).value, new State(), true, 0.0, context.propagator.upperBounds));
                    model.object.realizeArg = arg2;
                    
                    SimToTerm.InitialObjectInitialization(model, context);
                }

                ArrayList<Model> states = new ArrayList<Model>();
                boolean turnThePage = true;
                while (true == turnThePage)
                {
                    states.clear();
                    context.propagator.propagate(states, context, model, false);

                    // Continue propagation while we have children that are priority.
                    if ((true == states.get(states.size() - 1).realizedChildren.isEmpty()) ||
                        (false == states.get(states.size() - 1).realizedChildren.get(0).priority))
                    {
                        turnThePage = false;
                    }
                    else
                    {
                        // Here I am, on the road again...
                        // There I am, up on stage...
                        // Here I go, playin' a star again...
                        // There I go, turn the page...
                        model.copyFromHere(states.get(states.size() - 1).realizedChildren.get(0));
                    }
                }

                result = new StateValue(states.get(states.size() - 1).state);
            }
            catch (FatalException e)
            {
                result = new StringValue(e.getLocalizedMessage());
            }
            finally
            {
                context.popObject();
                context.popModel();
            }
            return result;
        }
        else
        {
            throw new TypedOperationException("Cannot simulate non-Object to end of priority chain.");
        }
    }

    @Override
    public ValueType fun(CallingContext context, ValueType arg1, ValueType arg2) throws TypedOperationException
    {
        return function(context, arg1, arg2);
    }

}
