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

import esl2.types.ValueType;
import prop6.engine.CallWrapper;
import prop6.random.ANSIC;
import prop6.random.ParkMiller;

public final class SimObject
{

    public static final int POSITION_OFFSET_INDEX = 0;
    public static final int VELOCITY_OFFSET_INDEX = 1;
    public static final int ORIENTATION_OFFSET_INDEX = 2;
    public static final int RATES_OFFSET_INDEX = 3;
    public static final int INITIAL_MASS_INDEX = 4;
    public static final int MOI_INDEX = 5;
    public static final int CG_OFFSET_INDEX = 6;
    public static final int FORCE_INDEX = 7;
    public static final int TORQUE_INDEX = 8;
    public static final int FLOW_RATE_INDEX = 9;
    public static final int OUTPUTS_INDEX = 10;

    public String name;

    public final ArrayList<ValueType> variables;
    public ValueType realizeArg;
    // There is one, global function list.
    public CallWrapper onObjectInit;
    public CallWrapper onModelInit;
    public CallWrapper onModelRealize;
    public CallWrapper onModelUpdate;
    public CallWrapper onModelDerivs;

    public ANSIC myRNG;
    public ParkMiller objRNG;
    public ParkMiller mySeed;

    public SimObject()
    {
        variables = new ArrayList<ValueType>();
    }

    public SimObject (SimObject src)
    {
        variables = new ArrayList<ValueType>();
        copyFromHere(src);
    }

    public void copyFromHere(SimObject src)
    {
        name = src.name;

        variables.clear();
        variables.addAll(src.variables);
        realizeArg = src.realizeArg;

        onObjectInit = src.onObjectInit;
        onModelInit = src.onModelInit;
        onModelRealize = src.onModelRealize;
        onModelUpdate = src.onModelUpdate;
        onModelDerivs = src.onModelDerivs;

        if (null != src.myRNG)
        {
            myRNG = src.myRNG.duplicate();
            objRNG = src.objRNG.duplicate();
        }
        else
        {
            myRNG = src.myRNG;
            objRNG = src.objRNG;
        }
        mySeed = src.mySeed; // If we aren't treating this as constant, something is wrong.
    }

    public SimObject duplicate()
    {
        return new SimObject(this);
    }

}
