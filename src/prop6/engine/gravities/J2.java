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

package prop6.engine.gravities;

import esl2.types.Vector;
import prop6.types.Earth;
import prop6.types.GravityFunctor;
import prop6.types.ReferenceEllipsoid;

public final class J2 extends GravityFunctor
{

    // From Wikipedia: Nodal Precession
    final double J2 = (2.0 * ReferenceEllipsoid.OE.flattening / 3.0 -
        ReferenceEllipsoid.OE.equatorialRadius_m * ReferenceEllipsoid.OE.equatorialRadius_m * ReferenceEllipsoid.OE.equatorialRadius_m *
        Earth.ROTATING_IAU * Earth.ROTATING_IAU / 3.0 / Earth.EGM_96_MU) * Earth.EGM_96_MU *
        ReferenceEllipsoid.OE.equatorialRadius_m * ReferenceEllipsoid.OE.equatorialRadius_m;

    public J2()
    {
    }

    @Override
    public Vector getGravityForce(Vector position, double time_s, double mass_kg, Earth earth)
    {
        // From: Analysis of J2-Perturbed Relative Orbits for Satellite Formation Flying
        final double r = position.magnitude(),
            mu_r3 = -earth.mu_Nm2_kg / (r * r * r),
            J2term = -1.5 * J2 / earth.mu_Nm2_kg / (r * r),
            correction_xy = 5.0 * position.z * position.z / (r * r) - 1.0,
            correction_z = 5.0 * position.z * position.z / (r * r) - 3.0;
        return new Vector
            (
                position.x * mu_r3 * (1.0 - J2term * correction_xy),
                position.y * mu_r3 * (1.0 - J2term * correction_xy),
                position.z * mu_r3 * (1.0 - J2term * correction_z)
            ).mul(mass_kg);
    }

}
