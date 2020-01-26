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

public final class RKCKStepper extends RK45Stepper
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
                { 1.0 / 5.0 },
                { 3.0 / 40.0, 9.0 / 40.0 },
                { 3.0 / 10.0, -9.0 / 10.0, 6.0 / 5.0 },
                { -11.0 / 54.0, 5.0 / 2.0, -70.0 / 27.0, 35.0 / 27.0 },
                { 1631.0 / 55296.0, 175.0 / 512.0, 575.0 / 13824.0, 44275.0 / 110592.0, 253.0 / 4096.0 }
            };
        B = new double []
            {
                37.0 / 378.0, 0.0, 250.0 / 621.0, 125.0 / 594.0, 0.0, 512.0 / 1771.0
            };
        E = new double []
            {
                2825.0 / 27648.0, 0.0, 18575.0 / 48384.0, 13525.0 / 55296.0, 277.0 / 14336.0, 1.0 / 4.0
            };
        C = new double []
            {
                0.0, 1.0 / 5.0, 3.0 / 10.0, 3.0 / 5.0, 1.0, 7.0 / 8.0
            };
    }

    // Runge Kutta Cash Karp -- 5/4 ONLY, no adaptive order
    public RKCKStepper(double errorTol)
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
