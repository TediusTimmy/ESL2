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

package prop6.types;

public final class ReferenceEllipsoid
{

    public final double polarRadius_m;
    public final double equatorialRadius_m;
    public final double inverseFlattening;
    public final double flattening;
    public final double eccentricitySquared;
    public final double eccentricity;
    public final double secondEccentricitySquared;
    public final double secondEccentricity;

    public ReferenceEllipsoid (double equatorialRadius_m, double inverseFlattening)
    {
        polarRadius_m = equatorialRadius_m - equatorialRadius_m / inverseFlattening;
        this.equatorialRadius_m = equatorialRadius_m;
        this.inverseFlattening = inverseFlattening;
        flattening = 1.0 / inverseFlattening;
        eccentricitySquared = (equatorialRadius_m * equatorialRadius_m - polarRadius_m * polarRadius_m) / (equatorialRadius_m * equatorialRadius_m);
        eccentricity = Math.sqrt(eccentricitySquared);
        secondEccentricitySquared = (equatorialRadius_m * equatorialRadius_m - polarRadius_m * polarRadius_m) / (polarRadius_m * polarRadius_m);
        secondEccentricity = Math.sqrt(secondEccentricitySquared);
    }

    public ReferenceEllipsoid (double polarRadius_m, double equatorialRadius_m, double inverseFlattening, double flattening,
            double eccentricitySquared, double eccentricity, double secondEccentricitySquared, double secondEccentricity)
    {
        this.polarRadius_m = polarRadius_m;
        this.equatorialRadius_m = equatorialRadius_m;
        this.inverseFlattening = inverseFlattening;
        this.flattening = flattening;
        this.eccentricitySquared = eccentricitySquared;
        this.eccentricity = eccentricity;
        this.secondEccentricitySquared = secondEccentricitySquared;
        this.secondEccentricity = secondEccentricity;
    }

    public static final ReferenceEllipsoid
         SE = new ReferenceEllipsoid (6371008.7714150598, Double.POSITIVE_INFINITY), // Mean Radius
        SE2 = new ReferenceEllipsoid (6371007.1809184738, Double.POSITIVE_INFINITY), // Authalic Radius : Equal Area
        SE3 = new ReferenceEllipsoid (6371000.7900091592, Double.POSITIVE_INFINITY), // Volumetric Radius
         OE = new ReferenceEllipsoid (6378137.0, 298.257223563); // WGS-84 / EGM-96

}
