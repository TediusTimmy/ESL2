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

import prop6.engine.CallingContext;
import prop6.types.Model;

public abstract class ObjectSimulator extends Thread
{

    public final ArrayList<ArrayList<Model>> states;
    protected CallingContext context;
    protected Model initialModel;
    protected ArrayList<String> message;

    public ObjectSimulator()
    {
        states = new ArrayList<ArrayList<Model>>();
        context = null;
        initialModel = null;
        message = null;
    }
    
    public void setContext(CallingContext context)
    {
        this.context = new CallingContext();
        this.context.copyFromHere(context);
    }

    public void setInitialModel(Model initialModel)
    {
        this.initialModel = initialModel;
    }

}
