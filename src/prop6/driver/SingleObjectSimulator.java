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

public final class SingleObjectSimulator extends ObjectSimulator
{

    @Override
    public void run()
    {
        try
        {
            ArrayList<Model> objects = new ArrayList<Model>();

            objects.add(initialModel);

            while (false == objects.isEmpty())
            {
                Model toSim = objects.remove(0);
                ArrayList<Model> thisObject = new ArrayList<Model>();
                context.propagator.propagate(thisObject, context, toSim, false);

                states.add(thisObject);

                ArrayList<Model> realized = new ArrayList<Model>(thisObject.get(thisObject.size() - 1).realizedChildren);
                if (false == realized.isEmpty())
                {
                    if (true == realized.get(0).priority)
                    {
                        objects.add(realized.remove(0));
                    }
                    objects.addAll(realized);
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
