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

public final class ANSIC
{

    private long seed;

    public ANSIC(long seed)
    {
        this.seed = seed;
    }

    public double getNext()
    {
        seed = (1103515245 * seed + 12345) % 2147483648L;
        // Lop off the lowest 8 bits, because they have poor qualities.
        // Return a value in [0,1)
        return (seed >> 8) / 8388608.0;
    }

    public ANSIC duplicate()
    {
        return new ANSIC(seed);
    }

}
