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

public final class Quaternion
{

    public final double s;
    public final double i;
    public final double j;
    public final double k;

    public Quaternion(double s, double i, double j, double k)
    {
        this.s = s;
        this.i = i;
        this.j = j;
        this.k = k;
    }

    public Quaternion(Matrix mat)
    {
        // FUTURE : have a decision tree that picks the most stable form of this conversion.
        double qs = 0.5 * Math.sqrt(1.0 + mat.a11 + mat.a22 + mat.a33);
        this.s = qs;
        this.i = (mat.a32 - mat.a23) / (4.0 * qs);
        this.j = (mat.a13 - mat.a31) / (4.0 * qs);
        this.k = (mat.a21 - mat.a12) / (4.0 * qs);
    }

    public double magnitude ()
    {
        return Math.hypot(Math.hypot(s, i), Math.hypot(j, k));
    }

    public String toString()
    {
        return "[" + Double.toString(s) + "," + Double.toString(i) + "," + Double.toString(j) + "," + Double.toString(k) + "]";
    }

    public boolean equals (Quaternion rhs)
    {
        boolean res = true;
        res &= s == rhs.s;
        res &= i == rhs.i;
        res &= j == rhs.j;
        res &= k == rhs.k;
        return res;
    }

    public Quaternion neg ()
    {
        return new Quaternion(-s, -i, -j, -k);
    }

    public Quaternion add (Quaternion rhs)
    {
        return new Quaternion(s + rhs.s, i + rhs.i, j + rhs.j, k + rhs.k);
    }

    public Quaternion sub (Quaternion rhs)
    {
        return new Quaternion(s - rhs.s, i - rhs.i, j - rhs.j, k - rhs.k);
    }

    public Quaternion mul (double rhs)
    {
        return new Quaternion(s * rhs, i * rhs, j * rhs, k * rhs);
    }

    public Quaternion mul (Vector rhs)
    {
        return this.mul(new Quaternion(0.0, rhs.x, rhs.y, rhs.z));
    }

    public Quaternion mul (Quaternion rhs)
    {
        return new Quaternion
            (
                s * rhs.s - i * rhs.i - j * rhs.j - k * rhs.k,
                s * rhs.i + i * rhs.s + j * rhs.k - k * rhs.j,
                s * rhs.j - i * rhs.k + j * rhs.s + k * rhs.i,
                s * rhs.k + i * rhs.j - j * rhs.i + k * rhs.s
            );
    }

    public Quaternion div (double rhs)
    {
        return new Quaternion(s / rhs, i / rhs, j / rhs, k / rhs);
    }

    public Quaternion conjugate ()
    {
        return new Quaternion(s, -i, -j, -k);
    }

    public Quaternion invert ()
    {
        double magSquared = s * s + i * i + j * j + k * k;
        return new Quaternion(s / magSquared, -i / magSquared, -j / magSquared, -k / magSquared);
    }

    public Quaternion unit ()
    {
        double mag = Math.hypot(Math.hypot(s, i), Math.hypot(j, k));
        return new Quaternion(s / mag, i / mag, j / mag, k / mag);
    }

    public Vector rotateVector (Vector toRotate)
    {
        return this.unit().rotateVectorAssumeUnit(toRotate);
    }

    public Vector rotateVectorAssumeUnit (Vector toRotate)
    {
        /*
        Quaternion res = this.mul(toRotate).mul(this.conjugate());
        return new Vector (res.i, res.j, res.k);
        */
        // While the above formula is simpler, this one should save us operations for the same result.
        // A quaternion multiplication is 16 multiplies and 12 adds, and we do two for 32 multiplies and 24 adds
        // Cross product is 6 multiplies and 3 adds, so this does 15 multiplies and 15 adds.
        Vector r = new Vector(i, j, k);
        return toRotate.add(r.add(r).cross(r.cross(toRotate).add(toRotate.mul(s))));
    }

    public Vector rotateVectorInverseAssumeUnit (Vector toRotate)
    {
        Vector r = new Vector(-i, -j, -k);
        return toRotate.add(r.add(r).cross(r.cross(toRotate).add(toRotate.mul(s))));
    }

    public static Quaternion xRotationQuat(double theta)
    {
        return new Quaternion(Math.cos(0.5 * theta), Math.sin(0.5 * theta), 0.0, 0.0);
    }

    public static Quaternion yRotationQuat(double theta)
    {
        return new Quaternion(Math.cos(0.5 * theta), 0.0, Math.sin(0.5 * theta), 0.0);
    }

    public static Quaternion zRotationQuat(double theta)
    {
        return new Quaternion(Math.cos(0.5 * theta), 0.0, 0.0, Math.sin(0.5 * theta));
    }

}
