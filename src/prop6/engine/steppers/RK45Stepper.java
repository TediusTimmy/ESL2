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

package prop6.engine.steppers;

import java.util.ArrayList;

import esl2.engine.ConstantsSingleton;
import esl2.types.FatalException;
import esl2.types.Matrix;
import esl2.types.TypedOperationException;
import esl2.types.Vector;
import prop6.engine.CallingContext;
import prop6.engine.Stepper;
import prop6.types.SimObject;
import prop6.types.Model;
import prop6.types.State;

public abstract class RK45Stepper extends Stepper
{

    public final double errorTol;

    public RK45Stepper(double errorTol)
    {
        this.errorTol = errorTol;
    }

    protected abstract int getS();
    protected abstract double[][] getA();
    protected abstract double[] getB();
    protected abstract double[] getE(); // B for the error solution
    protected abstract double[] getC();

    @Override
    protected void stepInner(CallingContext context, Model model) throws FatalException, TypedOperationException
    {
        State n = new State(model.state);
        ArrayList<State> k = new ArrayList<State>(getS());
        double deltaTime_s = model.deltaTime / HAIRS_PER_SECOND;

        for (int phaseNum = 0; phaseNum < getS(); ++phaseNum)
        {
            k.add(new State());
            phase(context, model, deltaTime_s, k.get(phaseNum), phaseNum, k);
            model.state.copyFromHere(n);
        }


        model.state.flowRate_kg_s = getDouble(model.object.variables.get(SimObject.FLOW_RATE_INDEX));

        computeState(model.state, deltaTime_s, getS(), k, getB());
        computeState(n, deltaTime_s, getS(), k, getE());

        // Compute the error
        double errorVal = 0.0;
        errorVal = Math.max(errorVal, Math.abs(model.state.position.x - n.position.x));
        errorVal = Math.max(errorVal, Math.abs(model.state.position.y - n.position.y));
        errorVal = Math.max(errorVal, Math.abs(model.state.position.z - n.position.z));
        errorVal = Math.max(errorVal, Math.abs(model.state.velocity.x - n.velocity.x));
        errorVal = Math.max(errorVal, Math.abs(model.state.velocity.y - n.velocity.y));
        errorVal = Math.max(errorVal, Math.abs(model.state.velocity.z - n.velocity.z));
        errorVal = Math.max(errorVal, Math.abs(model.state.angularVelocity.x - n.angularVelocity.x));
        errorVal = Math.max(errorVal, Math.abs(model.state.angularVelocity.y - n.angularVelocity.y));
        errorVal = Math.max(errorVal, Math.abs(model.state.angularVelocity.z - n.angularVelocity.z));
        errorVal = Math.max(errorVal, Math.abs(model.state.orientation.s - n.orientation.s));
        errorVal = Math.max(errorVal, Math.abs(model.state.orientation.i - n.orientation.i));
        errorVal = Math.max(errorVal, Math.abs(model.state.orientation.j - n.orientation.j));
        errorVal = Math.max(errorVal, Math.abs(model.state.orientation.k - n.orientation.k));

        final Matrix newMOI = getMatrix(model.object.variables.get(SimObject.MOI_INDEX));

        model.state.Idot = newMOI.sub(model.state.MOI).mul(deltaTime_s);
        model.state.MOI = newMOI;
        model.state.mass_kg -= model.state.flowRate_kg_s * deltaTime_s;

        if (errorVal > errorTol)
        {
            model.success = false;
        }
        else
        {
            model.success = true;
        }
    }

    private void phase(CallingContext context, Model model, double deltaTime,
        State outDerivs, int phaseNum, ArrayList<State> inDerivs) throws FatalException, TypedOperationException
    {
        model.state.time_s += deltaTime * getC()[phaseNum];

        for (int phaseIter = 0; phaseIter < phaseNum; ++phaseIter)
        {
            model.state.position = model.state.position.add(inDerivs.get(phaseIter).velocity.mul(deltaTime * getA()[phaseNum][phaseIter]));
            model.state.velocity = model.state.velocity.add(inDerivs.get(phaseIter).acceleration.mul(deltaTime * getA()[phaseNum][phaseIter]));

            model.state.orientation = exponentialMap(inDerivs.get(phaseIter).angularVelocity, deltaTime * getA()[phaseNum][phaseIter]).mul(model.state.orientation);
            model.state.angularVelocity = model.state.angularVelocity.add(inDerivs.get(phaseIter).angularAcceleration.mul(deltaTime * getA()[phaseNum][phaseIter]));
        }

        update(model.state, context.settings.earth);
        model.object.onModelDerivs.execute(context, ConstantsSingleton.getInstance().EMPTY_DICTIONARY);

        updateGravity(model, context.settings.earth);

        final Vector force = model.state.orientation.rotateVectorInverseAssumeUnit(
            getVector(model.object.variables.get(SimObject.FORCE_INDEX))).add(model.state.gravity_N);
        final Vector torque = model.state.orientation.rotateVectorInverseAssumeUnit(
            getVector(model.object.variables.get(SimObject.TORQUE_INDEX)));

        outDerivs.velocity = model.state.velocity;
        outDerivs.acceleration = force.sub(model.state.velocity.mul(force.dot(model.state.velocity) * _c2)).div(model.state.mass_kg * gamma(model.state.velocity));

        outDerivs.angularAcceleration = model.state.MOI.invert().mul(
            torque.sub(
            model.state.angularVelocity.cross(model.state.MOI.mul(model.state.angularVelocity))).sub(
            model.state.Idot.mul(model.state.angularVelocity)));
    }

    private static void computeState(State inOutState, double deltaTime_s, int points, ArrayList<State> k, double[] b)
    {
        inOutState.acceleration = new Vector(0.0, 0.0, 0.0);
        Vector velocity = new Vector(0.0, 0.0, 0.0);
        inOutState.angularAcceleration = new Vector(0.0, 0.0, 0.0);
        Vector averageAngVel = new Vector(0.0, 0.0, 0.0);

        for (int phaseNum = 0; phaseNum < points; ++phaseNum)
        {
            inOutState.acceleration = inOutState.acceleration.add(k.get(phaseNum).acceleration.mul(b[phaseNum]));
            velocity = velocity.add(k.get(phaseNum).velocity.mul(b[phaseNum]));
            inOutState.angularAcceleration = inOutState.angularAcceleration.add(k.get(phaseNum).angularAcceleration.mul(b[phaseNum]));
            averageAngVel = averageAngVel.add(k.get(phaseNum).angularVelocity.mul(b[phaseNum]));
        }

        inOutState.velocity = inOutState.velocity.add(inOutState.acceleration.mul(deltaTime_s));
        inOutState.position = inOutState.position.add(velocity.mul(deltaTime_s));
        inOutState.angularVelocity = inOutState.angularVelocity.add(inOutState.angularAcceleration.mul(deltaTime_s));

        inOutState.orientation = exponentialMap(averageAngVel, deltaTime_s).mul(inOutState.orientation);
    }

}
