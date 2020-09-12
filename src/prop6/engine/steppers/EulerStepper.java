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

import esl2.engine.ConstantsSingleton;
import esl2.types.FatalException;
import esl2.types.Matrix;
import esl2.types.TypedOperationException;
import esl2.types.Vector;
import prop6.engine.CallingContext;
import prop6.engine.Stepper;
import prop6.types.SimObject;
import prop6.types.Model;

public final class EulerStepper extends Stepper
{

    @Override
    protected void stepInner(CallingContext context, Model model) throws FatalException, TypedOperationException
    {
        final double deltaTime_s = model.deltaTime / HAIRS_PER_SECOND;

        model.object.onModelDerivs.execute(context, ConstantsSingleton.getInstance().EMPTY_DICTIONARY);

        updateGravity(model, context.settings.earth);

        final Vector force = model.state.orientation.rotateVectorInverseAssumeUnit(
            getVector(model.object.variables.get(SimObject.FORCE_INDEX))).add(model.state.gravity_N);
        final Vector torque = model.state.orientation.rotateVectorInverseAssumeUnit(
            getVector(model.object.variables.get(SimObject.TORQUE_INDEX)));
        model.state.flowRate_kg_s = getDouble(model.object.variables.get(SimObject.FLOW_RATE_INDEX));

        // Symplectic Euler.
        model.state.acceleration = force.sub(model.state.velocity.mul(force.dot(model.state.velocity) * _c2)).div(model.state.mass_kg * gamma(model.state.velocity));
        model.state.velocity = model.state.velocity.add(model.state.acceleration.mul(deltaTime_s));
        model.state.position = model.state.position.add(model.state.velocity.mul(deltaTime_s));

        // This is messy...
        model.state.angularAcceleration = model.state.MOI.invert().mul(
            torque.sub(
            model.state.angularVelocity.cross(model.state.MOI.mul(model.state.angularVelocity))).sub(
            model.state.Idot.mul(model.state.angularVelocity)));
        model.state.angularVelocity = model.state.angularVelocity.add(model.state.angularAcceleration.mul(deltaTime_s));
        model.state.orientation = exponentialMap(model.state.angularVelocity, deltaTime_s).mul(model.state.orientation);

        final Matrix newMOI = getMatrix(model.object.variables.get(SimObject.MOI_INDEX));

        model.state.Idot = newMOI.sub(model.state.MOI).mul(deltaTime_s);
        model.state.MOI = newMOI;
        model.state.mass_kg -= model.state.flowRate_kg_s * deltaTime_s;

        model.success = true;
    }

}
