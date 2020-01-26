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

public final class NoGravity extends GravityFunctor
{

    @Override
    public Vector getGravityForce(Vector position, double time_s, double mass_kg, Earth earth)
    {
        return new Vector(0.0, 0.0, 0.0);
    }

}
