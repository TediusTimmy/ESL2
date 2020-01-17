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

package esl2.types;

public final class Vector
{

    public final double x;
    public final double y;
    public final double z;

    public Vector(double x, double y, double z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public double magnitude ()
    {
        return Math.hypot(Math.hypot(x, y), z);
    }

    public String toString()
    {
        return "[" + Double.toString(x) + "," + Double.toString(y) + "," + Double.toString(z) + "]";
    }

    public boolean equals (Vector rhs)
    {
        boolean res = true;
        res &= x == rhs.x;
        res &= y == rhs.y;
        res &= z == rhs.z;
        return res;
    }

    public Vector neg ()
    {
        return new Vector(-x, -y, -z);
    }

    public Vector add (Vector rhs)
    {
        return new Vector(x + rhs.x, y + rhs.y, z + rhs.z);
    }

    public Vector sub (Vector rhs)
    {
        return new Vector(x - rhs.x, y - rhs.y, z - rhs.z);
    }

    public Vector mul (double rhs)
    {
        return new Vector(x * rhs, y * rhs, z * rhs);
    }

    // Treat this Vector as a Row Vector and return a Row Vector.
    public Vector mul (Matrix rhs)
    {
        return new Vector
            (
                x * rhs.a11 + y * rhs.a21 + z * rhs.a31,
                x * rhs.a12 + y * rhs.a22 + z * rhs.a32,
                x * rhs.a13 + y * rhs.a23 + z * rhs.a33
            );
    }

    // Create a Quaternion from this Vector and multiply.
    public Quaternion mul (Quaternion rhs)
    {
        Quaternion temp = new Quaternion(0.0, x, y, z);
        return temp.mul(rhs);
    }

    public Vector div (double rhs)
    {
        return new Vector(x / rhs, y / rhs, z / rhs);
    }

    public Vector cross (Vector rhs)
    {
        // | i j k |
        // | x y z |
        // | a b c |
        double a = rhs.x, b = rhs.y, c = rhs.z;
        return new Vector
            (
                y * c - b * z,
                a * z - x * c,
                x * b - a * y
            );
    }

    public double dot (Vector rhs)
    {
        return x * rhs.x + y * rhs.y + z * rhs.z;
    }

    public Vector unit ()
    {
        double mag = Math.hypot(Math.hypot(x, y), z);
        return new Vector(x / mag, y / mag, z / mag);
    }

}
