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

package prop6.driver;

import java.util.ArrayList;

import esl2.types.FatalException;
import esl2.types.TypedOperationException;
import prop6.types.Model;

public final class MultiObjectSimulator extends ObjectSimulator
{

    @Override
    public void run()
    {
        try
        {
            ArrayList<Model> thisObject = new ArrayList<Model>();
            context.propagator.propagate(thisObject, context, initialModel, false);

            states.add(thisObject);

            ArrayList<Model> realized = new ArrayList<Model>(thisObject.get(thisObject.size() - 1).realizedChildren);
            ArrayList<MultiObjectSimulator> objects = new ArrayList<MultiObjectSimulator>();
            for (Model model : realized)
            {
                MultiObjectSimulator sim = new MultiObjectSimulator();
                sim.setContext(context);
                sim.setInitialModel(model);
                sim.start();
                objects.add(sim);
            }
            for (MultiObjectSimulator thread : objects)
            {
                try
                {
                    thread.join();

                    states.addAll(thread.states);
                    if (null != thread.message)
                    {
                        if (null == message)
                        {
                            message = new ArrayList<String>();
                        }
                        message.addAll(thread.message);
                    }
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        }
        catch (TypedOperationException | FatalException e)
        {
            message = new ArrayList<String>();
            message.add(e.getLocalizedMessage());
        }
    }

}
