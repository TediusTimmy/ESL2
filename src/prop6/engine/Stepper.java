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

package prop6.engine;

import esl2.types.FatalException;
import esl2.types.Matrix;
import esl2.types.MatrixValue;
import esl2.types.Quaternion;
import esl2.types.QuaternionValue;
import esl2.engine.ConstantsSingleton;
import esl2.types.DoubleValue;
import esl2.types.TypedOperationException;
import esl2.types.ValueType;
import esl2.types.Vector;
import esl2.types.VectorValue;
import prop6.types.Earth;
import prop6.types.SimObject;
import prop6.types.Model;
import prop6.types.State;

public abstract class Stepper
{

    // One over the speed of light squared. Exact as defined in the 17th CGPM (General Conference on Weights and Measures)
    protected static double _c2 = 1.0 / (299792458.0 * 299792458.0);

    protected abstract void stepInner(CallingContext context, Model model) throws FatalException, TypedOperationException;

    // The hair is the smallest timed step that the propagator will take, and a stepper can be asked for.
    // All time in the propagator must be quantized in hairs. One hair is, presently, one nanosecond.
    public static final double HAIRS_PER_SECOND = 1000000000.0;
    public static final double MAX_TIME =   4503599627370496.0; // About 52 Days.

    public void step(CallingContext context, Model model) throws FatalException, TypedOperationException
    {
        // Advance Time (This needs to happen now so that EventHint will work in onModelDerivs).
        model.currentTime += model.deltaTime;
        // Clear the event flag before continuing.
        model.eventThisStep = false;
        // Take step using integration method
        stepInner(context, model);
        // Set current time in state.
        model.state.time_s = model.currentTime / HAIRS_PER_SECOND;
        // Update dependent parts of state
        update(model.state, context.settings.earth);
        updateGravity(model, context.settings.earth);
        // Notify model of update
        model.object.onModelUpdate.execute(context, ConstantsSingleton.getInstance().EMPTY_DICTIONARY);
    }

    // Update the components of state that are dependent on other components of state.
    // This is called by a Stepper after each derivs.
    public static void update(State state, Earth earth)
    {
        state.position_ecr = earth.ECItoECR(state.position, state.time_s);

        state.llh = earth.ECRtoLLH(state.position_ecr);

        state.velocity_ecr = earth.ECItoECRVelocity(state.velocity, state.position, state.time_s);

        state.atmosphere = earth.atmosphere.getAtmosphereConditions(state);

        double v = state.velocity_ecr.magnitude();

        state.dynamicPressure_Pa = 0.5 * state.atmosphere.density_kg_m3 * v * v;

        state.mach = v / state.atmosphere.speedOfSound_m_s;
    }

    public static void updateGravity(Model model, Earth earth) throws TypedOperationException
    {
        final Vector cg = model.state.position.sub(model.state.orientation.rotateVectorInverseAssumeUnit(
                getVector(model.object.variables.get(SimObject.CG_OFFSET_INDEX))));
        model.state.gravity_N = earth.gravity.getGravityForce(cg, model.state.time_s, model.state.mass_kg, earth);
    }

    public static double getDouble(ValueType value) throws TypedOperationException
    {
        if (value instanceof DoubleValue)
        {
            return ((DoubleValue)value).value;
        }
        else
        {
            throw new TypedOperationException("Tried to retrieve Double from variable containing non-Double.");
        }
    }

    public static Vector getVector(ValueType value) throws TypedOperationException
    {
        if (value instanceof VectorValue)
        {
            return ((VectorValue)value).value;
        }
        else
        {
            throw new TypedOperationException("Tried to retrieve Vector from variable containing non-Vector.");
        }
    }

    public static Quaternion getQuaternion(ValueType value) throws TypedOperationException
    {
        if (value instanceof QuaternionValue)
        {
            return ((QuaternionValue)value).value;
        }
        else
        {
            throw new TypedOperationException("Tried to retrieve Quaternion from variable containing non-Quaternion.");
        }
    }

    public static Matrix getMatrix(ValueType value) throws TypedOperationException
    {
        if (value instanceof MatrixValue)
        {
            return ((MatrixValue)value).value;
        }
        else
        {
            throw new TypedOperationException("Tried to retrieve Matrix from variable containing non-Matrix.");
        }
    }

    public static Quaternion exponentialMap(Vector angularVelocity, double timeDelta_s)
    {
        double theta = angularVelocity.magnitude() * timeDelta_s;
        if (Math.abs(theta) > Math.sqrt(Math.ulp(0.5)))
        {
            Vector axis = angularVelocity.unit().mul(Math.sin(theta * 0.5));
            return new Quaternion(Math.cos(theta * 0.5), axis.x, axis.y, axis.z);
        }
        else
        {
            // Convert sin(theta / 2) / theta to sinc(theta / 2) / 2
            // Then, use the Taylor series expansion for sinc, using only the first two terms.
            double sinc_theta_2_2 = 0.5 + theta * theta / 48.0;
            // timedelta reappears here because theta is magnitude times time
            Vector axis = angularVelocity.mul(sinc_theta_2_2 * timeDelta_s); 
            return new Quaternion(Math.cos(theta * 0.5), axis.x, axis.y, axis.z);
        }
    }

    public static double gamma(Vector vel)
    {
        return 1.0 / Math.sqrt(1.0 - vel.magnitude() * _c2);
    }

}
