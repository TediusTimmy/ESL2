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

package prop6.input;

import java.util.ArrayList;

import esl2.parser.FunctionPairs;
import esl2.parser.GetterSetter;
import esl2.parser.GlobalGetterSetter;
import prop6.engine.CallingContext;
import prop6.engine.VariableGetter;
import prop6.engine.VariableSetter;

public final class FunctionDefinitions
{

    public final FunctionPairs statelessStdLibFunctions;
        // These functions need a current MODEL to operate
        // Currently limited to: GetCurrentState, Terminate*, Event*, Deploy*, StopAt, InstantKick
    public final FunctionPairs statefullStdLibFunctions;

    public final FunctionPairs sharedFunctions;

    public final ArrayList<VariableGetter> sharedGetters; // getter at index x gets index x from object
    public final ArrayList<VariableSetter> sharedSetters;

    public final GetterSetter gs;
    public GlobalGetterSetter mgs;

    public FunctionDefinitions()
    {
        statelessStdLibFunctions = new FunctionPairs();
        statefullStdLibFunctions = new FunctionPairs();
        sharedFunctions = new FunctionPairs();
        sharedGetters = new ArrayList<VariableGetter>();
        sharedSetters = new ArrayList<VariableSetter>();
        gs = new GetterSetter();
        mgs = null;
    }

    private void moveFromStateLessToStateFullFunctions(String name)
    {
        statefullStdLibFunctions.funs.put(name, statelessStdLibFunctions.funs.remove(name));
        statefullStdLibFunctions.args.put(name, statelessStdLibFunctions.args.remove(name));
    }

    public void buildDefaultFunctions(CallingContext contextToPopulate)
    {
        ExecutorBuilder.createDefaultFunctions(contextToPopulate.executor, statelessStdLibFunctions,
                // Only pass in the frame list if we have a debugging stack.
            (null != contextToPopulate.debugger) ? contextToPopulate.executor.debugFrames : null);

        // Copy the state-full functions (the ones needing a current Model) over to their own list, and remove them from the stateless list.
        moveFromStateLessToStateFullFunctions("GetCurrentState");
        moveFromStateLessToStateFullFunctions("EventHit");
        moveFromStateLessToStateFullFunctions("EventPassed");
        moveFromStateLessToStateFullFunctions("TerminateNow");
        moveFromStateLessToStateFullFunctions("TerminatePassed");
        moveFromStateLessToStateFullFunctions("DeployNowCP");
        moveFromStateLessToStateFullFunctions("DeployPassedCP");
        moveFromStateLessToStateFullFunctions("DeployNowNCP");
        moveFromStateLessToStateFullFunctions("DeployPassedNCP");
        moveFromStateLessToStateFullFunctions("EventHint");
        moveFromStateLessToStateFullFunctions("StopAt");
        moveFromStateLessToStateFullFunctions("InstantKick");
    }

    public GlobalGetterSetter buildGetterSetter(ArrayList<String> variables)
    {
        while (sharedGetters.size() <= variables.size())
        {
            sharedGetters.add(new VariableGetter(sharedGetters.size()));
            sharedSetters.add(new VariableSetter(sharedSetters.size()));
        }

        GlobalGetterSetter result = new GlobalGetterSetter();
        for (int i = 0; i < variables.size(); ++i)
        {
            result.getters.put(variables.get(i), sharedGetters.get(i));
            result.setters.put(variables.get(i), sharedSetters.get(i));
        }

        return result;
    }

    private FunctionDefinitions(FunctionDefinitions src)
    {
        statelessStdLibFunctions = src.statelessStdLibFunctions;
        statefullStdLibFunctions = src.statefullStdLibFunctions;
        sharedFunctions = new FunctionPairs();
        sharedGetters = new ArrayList<VariableGetter>();
        sharedSetters = new ArrayList<VariableSetter>();
        gs = new GetterSetter();
        mgs = src.mgs;
    }

    public FunctionDefinitions duplicate_base()
    {
        FunctionDefinitions result = new FunctionDefinitions(this);
        return result;
    }

}
