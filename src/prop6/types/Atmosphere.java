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

public final class Atmosphere
{

    public final double speedOfSound_m_s;
    public final double density_kg_m3;
    public final double pressure_Pa;
    public final double temperature_K;

    public Atmosphere (double speedOfSound_m_s, double density_kg_m3, double pressure_Pa, double temperature_K)
    {
        this.speedOfSound_m_s = speedOfSound_m_s;
        this.density_kg_m3 = density_kg_m3;
        this.pressure_Pa = pressure_Pa;
        this.temperature_K = temperature_K;
    }

}
