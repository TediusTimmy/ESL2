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

public final class Matrix
{

    public final double
        a11, a12, a13,
        a21, a22, a23,
        a31, a32, a33;

    public Matrix
        (
            double a11, double a12, double a13,
            double a21, double a22, double a23,
            double a31, double a32, double a33
        )
    {
        this.a11 = a11;
        this.a12 = a12;
        this.a13 = a13;
        this.a21 = a21;
        this.a22 = a22;
        this.a23 = a23;
        this.a31 = a31;
        this.a32 = a32;
        this.a33 = a33;
    }

    public Matrix (Quaternion quat)
    {
        double  i2 = quat.i * quat.i,
                j2 = quat.j * quat.j,
                k2 = quat.k * quat.k,
                ij = quat.i * quat.j,
                ik = quat.i * quat.k,
                jk = quat.j * quat.k,
                si = quat.s * quat.i,
                sj = quat.s * quat.j,
                sk = quat.s * quat.k;
        this.a11 = 1.0 - 2.0 * (j2 + k2);
        this.a12 = 2.0 * (ij - sk);
        this.a13 = 2.0 * (ik + sj);
        this.a21 = 2.0 * (ij + sk);
        this.a22 = 1.0 - 2.0 * (i2 + k2);
        this.a23 = 2.0 * (jk - si);
        this.a31 = 2.0 * (ik - sj);
        this.a32 = 2.0 * (si + jk);
        this.a33 = 1.0 - 2.0 * (i2 + j2);
    }

    public String toString()
    {
        return "[" + Double.toString(a11) + "," + Double.toString(a12) + "," + Double.toString(a13) + ";" +
            Double.toString(a21) + "," + Double.toString(a22) + "," + Double.toString(a23) + ";" +
            Double.toString(a31) + "," + Double.toString(a32) + "," + Double.toString(a33) + "]";
    }

    public boolean equals (Matrix rhs)
    {
        boolean res = true;
        res &= a11 == rhs.a11;
        res &= a12 == rhs.a12;
        res &= a13 == rhs.a13;
        res &= a21 == rhs.a21;
        res &= a22 == rhs.a22;
        res &= a23 == rhs.a23;
        res &= a31 == rhs.a31;
        res &= a32 == rhs.a32;
        res &= a33 == rhs.a33;
        return res;
    }

    public Matrix neg ()
    {
        return new Matrix
            (
                -a11, -a12, -a13,
                -a21, -a22, -a23,
                -a31, -a32, -a33
            );
    }

    public Matrix add (Matrix rhs)
    {
        return new Matrix
            (
                a11 + rhs.a11, a12 + rhs.a12, a13 + rhs.a13,
                a21 + rhs.a21, a22 + rhs.a22, a23 + rhs.a23,
                a31 + rhs.a31, a32 + rhs.a32, a33 + rhs.a33
            );
    }

    public Matrix sub (Matrix rhs)
    {
        return new Matrix
            (
                a11 - rhs.a11, a12 - rhs.a12, a13 - rhs.a13,
                a21 - rhs.a21, a22 - rhs.a22, a23 - rhs.a23,
                a31 - rhs.a31, a32 - rhs.a32, a33 - rhs.a33
            );
    }

    public Matrix mul (double rhs)
    {
        return new Matrix
            (
                a11 * rhs, a12 * rhs, a13 * rhs,
                a21 * rhs, a22 * rhs, a23 * rhs,
                a31 * rhs, a32 * rhs, a33 * rhs
            );
    }

    // Treat this Vector as a Column Vector and return a Column Vector.
    public Vector mul (Vector rhs)
    { 
        return new Vector
            (
                a11 * rhs.x + a12 * rhs.y + a13 * rhs.z,
                a21 * rhs.x + a22 * rhs.y + a23 * rhs.z,
                a31 * rhs.x + a32 * rhs.y + a33 * rhs.z
            );
    }

    public Matrix mul (Matrix rhs)
    {
        return new Matrix
            (
                a11 * rhs.a11 + a12 * rhs.a21 + a13 * rhs.a31,
                a11 * rhs.a12 + a12 * rhs.a22 + a13 * rhs.a32,
                a11 * rhs.a13 + a12 * rhs.a23 + a13 * rhs.a33,
                a21 * rhs.a11 + a22 * rhs.a21 + a23 * rhs.a31,
                a21 * rhs.a12 + a22 * rhs.a22 + a23 * rhs.a32,
                a21 * rhs.a13 + a22 * rhs.a23 + a23 * rhs.a33,
                a31 * rhs.a11 + a32 * rhs.a21 + a33 * rhs.a31,
                a31 * rhs.a12 + a32 * rhs.a22 + a33 * rhs.a32,
                a31 * rhs.a13 + a32 * rhs.a23 + a33 * rhs.a33
            );
    }

    public Matrix div (double rhs)
    {
        return new Matrix
            (
                a11 / rhs, a12 / rhs, a13 / rhs,
                a21 / rhs, a22 / rhs, a23 / rhs,
                a31 / rhs, a32 / rhs, a33 / rhs
            );
    }

    public Matrix transpose ()
    {
        return new Matrix
            (
                a11, a21, a31,
                a12, a22, a32,
                a13, a23, a33
            );
    }

    public Matrix invert ()
    {
        // | a b c |
        // | d e f |
        // | g h i |
        double a = a11, b = a12, c = a13, d = a21, e = a22, f = a23, g = a31, h = a32, i = a33;
        double A = e * i - h * f, B = g * f - d * i, C = d * h - g * e;
        double det = a * A + b * B + c * C;
        return new Matrix
            (
                A / det,
                (h * c - b * i) / det,
                (b * f - e * c) / det,
                B / det,
                (a * i - g * c) / det,
                (d * c - a * f) / det,
                C / det,
                (g * b - a * h) / det,
                (a * e - d * b) / det
            );
    }

    public double determinant ()
    {
        double a = a11, b = a12, c = a13, d = a21, e = a22, f = a23, g = a31, h = a32, i = a33;
        double A = e * i - h * f, B = g * f - d * i, C = d * h - g * e;
        return a * A + b * B + c * C; 
    }

    public static Matrix xRotationMatrix (double theta)
    {
        return new Matrix
            (
                1.0,             0.0,              0.0,
                0.0, Math.cos(theta), -Math.sin(theta),
                0.0, Math.sin(theta),  Math.cos(theta)
            );
    }

    public static Matrix yRotationMatrix (double theta)
    {
        return new Matrix
            (
                 Math.cos(theta), 0.0, Math.sin(theta),
                             0.0, 1.0,             0.0, 
                -Math.sin(theta), 0.0,  Math.cos(theta)
            );
    }

    public static Matrix zRotationMatrix (double theta)
    {
        return new Matrix
            (
                Math.cos(theta), -Math.sin(theta), 0.0,
                Math.sin(theta),  Math.cos(theta), 0.0,
                            0.0,              0.0, 1.0
            );
    }

}
