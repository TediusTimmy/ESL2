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

public final class RK4aStepper extends RK45Stepper
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
                { 0.5 },
                { 0.0, 0.5 },
                { 0.0, 0.0, 1.0 }
            };
        B = new double []
            {
                1.0 / 6.0, 1.0 / 3.0, 1.0 / 3.0, 1.0 / 6.0
            };
        E = new double []
            {
                1.0 / 6.0, 1.0 / 3.0, 1.0 / 3.0, 1.0 / 6.0
            };
        C = new double []
            {
                0.0, 0.5, 0.5, 1.0
            };
    }

    // 4th Order Runge Kutta using this architecture.
    public RK4aStepper(double errorTol)
    {
        super(errorTol);
    }

    @Override
    protected int getS()
    {
        return 4;
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
