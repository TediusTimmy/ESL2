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

package prop6.test;

import esl2.types.Vector;
import prop6.engine.atmospheres.BrokenUSSA1976;
import prop6.engine.gravities.J2;
import prop6.engine.gravities.UniformGravity;
import prop6.types.Earth;
import prop6.types.ReferenceEllipsoid;
import prop6.types.State;

public final class EGMTest
{

    // As cool as this is, I have no clue if the results are RIGHT (correct).

    public static void main(String[] args)
    {
        J2 testor = new J2();

//        Earth earth = new Earth(ReferenceEllipsoid.SE1, Earth.ROTATING,
//                new UniformGravity(), Earth.G0, Earth.EGM_96_MU,
//                new BrokenUSSA1976());

        Earth earth = new Earth(ReferenceEllipsoid.OE, Earth.ROTATING,
                new UniformGravity(), Earth.G0, Earth.EGM_96_MU,
                new BrokenUSSA1976());

        State state = new State();
        state.mass_kg = 1.0;

        for (int increment = 0; increment < 21; ++increment)
        {
            double lat = 0.0;
            if (increment < 11)
            {
                lat = -89.9 - increment / 100.0;
                state.llh = new Vector(Math.toRadians(lat), Math.toRadians(0.0), 200.0); // 135.0
            }
            else
            {
                lat = -90.0 + (increment - 10) / 100.0;
                state.llh = new Vector(Math.toRadians(lat), Math.toRadians(180.0), 200.0); // -45.0
            }
            state.position = earth.LLHtoECR(state.llh);
            Vector grav = testor.getGravityForce(state.position, state.time_s, state.mass_kg, earth);
            Vector ugrav = earth.gravity.getGravityForce(state.position, state.time_s, state.mass_kg, earth);
            state.llh = new Vector(state.llh.x, state.llh.y, state.llh.z - 105.0);
            state.position = earth.LLHtoECR(state.llh);
            Vector ugravl = earth.gravity.getGravityForce(state.position, state.time_s, state.mass_kg, earth);
            state.llh = new Vector(state.llh.x, state.llh.y, state.llh.z + 105.0 + 85.0);
            state.position = earth.LLHtoECR(state.llh);
            Vector ugravh = earth.gravity.getGravityForce(state.position, state.time_s, state.mass_kg, earth);
            System.out.println(((increment - 10) / 10.0) + " " + grav.magnitude() + " " + grav + " " + ugrav.magnitude() + " " + ugrav +
                " " + ugravl.magnitude() + " " + ugravl + " " + ugravh.magnitude() + " " + ugravh);
        }

        state.llh = new Vector(0.0, 0.0, 200.0);
        state.position = earth.LLHtoECR(state.llh);
        Vector grav = testor.getGravityForce(state.position, state.time_s, state.mass_kg, earth);
        System.out.println(grav.magnitude() + " " + grav);
    }

}
