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

package prop6.engine.atmospheres;

import prop6.types.Atmosphere;
import prop6.types.AtmosphereFunctor;
import prop6.types.State;

public final class BrokenUSSA1976 extends AtmosphereFunctor
{

    @Override
    public Atmosphere getAtmosphereConditions(State state)
    {
        final double earthRadius = 6356766.0; // USSA 1976 uses it's own reference sphere.
        final double g0prime = 9.80665E3; /* (m/s^2)(m/km') : vertical increment to give 1kg 9.80665J potential energy */
        final double M0 = 28.9644; /* mean molecular weight of air */
        final double Rstar = 8.31432E3; /* universal gas constant */
        final double GMR = g0prime * M0 / Rstar;
        // Define the 7 layers of the 1976 atmosphere and a post-7th layer (truncated)
        final double baseHeight [] =       {      0.0, 11000.0, 20000.0, 32000.0, 47000.0, 51000.0, 71000.0, 84852.0 };
        final double temperatureDelta [] = {  -0.0065,  0.0000,  0.0010,  0.0028,  0.0000, -0.0028, -0.0020, 0.0000  };
        // Temperatures obtained using temperature of 288.15 at 0 altitude and extrapolating up using delta constants.
        final double baseTemperature [] =  {   288.15,  216.65,  216.65,  228.65,  270.65,  270.65,  214.65, 186.946 };
        // Pressures obtained using pressure of 101325.0 at 0 altitude and extrapolating up using later equations.
        final double basePressure [] =     {               101325.0, 22632.0639734629301986, 5474.88866967777955811, 868.018684755227330115,
                                             110.906305554965877301, 66.9388731186872660410, 3.95642042804072865364, 0.373383589976215784312 };

        final double geopotentialHeight = state.llh.z * earthRadius / (state.llh.z + earthRadius);

        int index; // This is an unrolled binary search.
        if (geopotentialHeight < baseHeight[4])
        {
            if (geopotentialHeight < baseHeight[2])
            {
                if (geopotentialHeight < baseHeight[1])
                    index = 0;
                else
                    index = 1;
            }
            else
            {
                if (geopotentialHeight < baseHeight[3])
                    index = 2;
                else
                    index = 3;
            }
        }
        else
        {
            if (geopotentialHeight < baseHeight[6])
            {
                if (geopotentialHeight < baseHeight[5])
                    index = 4;
                else
                    index = 5;
            }
            else
            {
                if (geopotentialHeight < baseHeight[7])
                    index = 6;
                else
                    index = 7;
            }
        }

        final double heightDelta = geopotentialHeight - baseHeight[index];

        final double temperature_K = baseTemperature[index] + heightDelta * temperatureDelta[index];
        double pressure_Pa;

        if (0.0 == temperatureDelta[index])
        {
            // heightDelta is in m, equation expects it in km
            pressure_Pa = basePressure[index] * Math.exp(-GMR * (heightDelta / 1000.0) / baseTemperature[index]);
        }
        else
        {
            // temperatureDelta is in K/m, equation expects it in K/km
            pressure_Pa = basePressure[index] * Math.pow(baseTemperature[index] / temperature_K, GMR / (temperatureDelta[index] * 1000.0));
        }

        final double density_kgPerm3 = M0 * pressure_Pa / (Rstar * temperature_K);

        final double speedOfSound_mPers = Math.sqrt(1.40 * Rstar * temperature_K / M0);

        return new Atmosphere(speedOfSound_mPers, density_kgPerm3, pressure_Pa, temperature_K);
    }

}
