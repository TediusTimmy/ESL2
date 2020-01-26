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

public final class RKDPStepper extends RK45Stepper
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
                { 44.0 / 45.0, -56.0 / 15.0, 32.0 / 9.0 },
                { 19372.0 / 6561.0, -25360.0 / 2187.0, 64448.0 / 6561.0, -212.0 / 729.0 },
                { 9017.0 / 3168.0, -355.0 / 33.0, 46732.0 / 5247.0, 49.0 / 176.0, -5103.0 / 18656.0 },
                { 35.0 / 384.0, 0.0, 500.0 / 1113.0, 125.0 / 192.0, -2187.0 / 6784.0, 11.0 / 84.0 }
            };
        B = new double []
            { // Could just as easily be all zeros and a final 1.0
                35.0 / 384.0, 0.0, 500.0 / 1113.0, 125.0 / 192.0, -2187.0 / 6784.0, 11.0 / 84.0, 0.0
            };
        E = new double []
            {
                5179.0 / 57600.0, 0.0, 7571.0 / 16695.0, 393.0 / 640.0, -92097.0 / 339200.0, 187.0 / 2100.0, 1.0 / 40.0
            };
        C = new double []
            {
                0.0, 1.0 / 5.0, 3.0 / 10.0, 4.0 / 5.0, 8.0 / 9.0, 1.0, 1.0
            };
    }

    // Runge Kutta Dormand Prince
    public RKDPStepper(double errorTol)
    {
        super(errorTol);
    }

    @Override
    protected int getS()
    {
        return 7;
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
