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

package prop6.random;

public final class ParkMiller
{

    private long seed;

    public ParkMiller(long seed)
    {
        this.seed = seed;
    }

    public long getNext()
    {
        // Park-Miller
        // See
        //    "Random Number Generators: Good Ones Are Hard To Find" Communications of the ACM 31 (10): 1192-1201
        //     "Technical correspondence" Communications of the ACM 36 (7): 105-110 (especially page 110)
        seed = (48271 * seed) % 2147483647;
        return seed;
    }

    public ParkMiller duplicate()
    {
        return new ParkMiller(seed);
    }

}
