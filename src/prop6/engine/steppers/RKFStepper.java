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

package prop6.engine.steppers;

public final class RKFStepper extends RK45Stepper
{

    private static final double A[][];
    private static final double B[];
    private static final double E[];
    private static final double C[];
    static
    {
        A = new double [][]
            {
                null,
                { 1.0 / 4.0 },
                { 3.0 / 32.0, 9.0 / 32.0 },
                { 1932.0 / 2197.0, -7200.0 / 2197.0, 7296.0 / 2197.0 },
                { 439.0 / 216.0, -8.0, 3680.0 / 513.0, -845.0 / 4104.0 },
                { -8.0 / 27.0, 2.0, -3544.0 / 2565.0, 1859.0 / 4104.0, -11.0 / 40.0 }
            };
        B = new double []
            {
                25.0 / 216.0, 0.0, 1408.0 / 2565.0, 2197.0 / 4104.0, -1.0 / 5.0, 0.0
            };
        E = new double []
            {
                16.0 / 135.0, 0.0, 6656.0 / 12825.0, 28561.0 / 56430.0, -9.0 / 50.0, 2.0 / 55.0
            };
        C = new double []
            {
                0.0, 1.0 / 4.0, 3.0 / 8.0, 12.0 / 13.0, 1.0, 1.0 / 2.0
            };
    }

    // Runge Kutta Felberg
    public RKFStepper(double errorTol)
    {
        super(errorTol);
    }

    @Override
    protected int getS()
    {
        return 6;
    }

    @Override
    protected double[][] getA()
    {
        return A;
    }

    @Override
    protected double[] getB()
    {
        return B;
    }

    @Override
    protected double[] getE()
    {
        return E;
    }

    @Override
    protected double[] getC()
    {
        return C;
    }

}
