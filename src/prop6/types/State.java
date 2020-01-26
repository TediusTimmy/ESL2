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

import esl2.types.Matrix;
import esl2.types.Quaternion;
import esl2.types.Vector;

public final class State
{

    public double time_s;              // Independant Variable

    public Vector position;            // Integrated from velocity : this is the position of the nose
    public Vector velocity;            // Integrated from acceleration
    public Vector acceleration;        // acceleration = Force / mass

    public double mass_kg;             // Note : Eulerian Integrated in all instances.
    public double flowRate_kg_s;       // Note : Given, updated after step only

    public Quaternion orientation;     // Integrated from angularVelocity (This is an ECI frame to body frame rotation Quaternion)
    public Vector angularVelocity;     // Integrated from angularAcceleration
    public Vector angularAcceleration; // angularAcceleration = MIO.inverse() * (Torque - angularVecocity % (MOI * angularVelocity) - Idot * angularVelocity)

    public Matrix MOI;                 // Note : Given, updated after step only
    public Matrix Idot;                // (MOI - MOI_prev) / timeStepSize : Only updated every step.

    public Vector position_ecr;        // Derived from position after sub-steps.
    public Vector llh;                 // Derived from position_ecr
    public Vector velocity_ecr;        // Derived from velocity and position
    public Atmosphere atmosphere;      // Derived from llh
    public double dynamicPressure_Pa;  // Derived from velocity_ecr and atmosphere
    public double mach;                // Derived from velocity_ecr and atmosphere

    public Vector gravity_N;           // Derived from position, mass_kg, and cg_offset_m; ECI frame

    public State ()
    {
        time_s = 0.0;

        position = new Vector(0.0, 0.0, 0.0);
        velocity = position;
        acceleration = position;

        mass_kg = 0.0;
        flowRate_kg_s = 0.0;

        orientation = new Quaternion(0.0, 0.0, 0.0, 0.0);
        angularVelocity = position;
        angularAcceleration = position;

        MOI = new Matrix(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
        Idot = MOI;

        position_ecr = position;
        llh = position;
        velocity_ecr = position;

        atmosphere = new Atmosphere(0.0, 0.0, 0.0, 0.0);

        dynamicPressure_Pa = 0.0;
        mach = 0.0;

        gravity_N = position;
    }

    public State (State src)
    {
        copyFromHere(src);
    }

    public void copyFromHere(State src)
    {
        time_s = src.time_s;

        position = src.position;
        velocity = src.velocity;
        acceleration = src.acceleration;

        mass_kg = src.mass_kg;
        flowRate_kg_s = src.flowRate_kg_s;

        orientation = src.orientation;
        angularVelocity = src.angularVelocity;
        angularAcceleration = src.angularAcceleration;

        MOI = src.MOI;
        Idot = src.Idot;

        position_ecr = src.position_ecr;
        llh = src.llh;
        velocity_ecr = src.velocity_ecr;
        atmosphere = src.atmosphere;
        dynamicPressure_Pa = src.dynamicPressure_Pa;
        mach = src.mach;

        gravity_N = src.gravity_N;
    }

    public State duplicate()
    {
        return new State(this);
    }

}
