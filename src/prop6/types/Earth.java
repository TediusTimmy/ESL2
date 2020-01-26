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

import esl2.types.FatalException;
import esl2.types.Vector;

public final class Earth
{

    public final ReferenceEllipsoid ellipsoid;
    public final double rotationRate_rad_s;
    public final GravityFunctor gravity;
    public final double g0_m_s, mu_Nm2_kg;
    public final AtmosphereFunctor atmosphere;

    public Earth (ReferenceEllipsoid ellipsoid, double rotationRate_rad_s, GravityFunctor gravity, double g0, double mu, AtmosphereFunctor atmosphere)
    {
        this.ellipsoid = ellipsoid;
        this.rotationRate_rad_s = rotationRate_rad_s;
        this.gravity = gravity;
        this.g0_m_s = g0;
        this.mu_Nm2_kg = mu;
        this.atmosphere = atmosphere;
    }

    public static final double
        ROTATING     = 0.000072921150, // WGS-84
        ROTATING_IAU = 0.000072921151467,
        NON_ROTATING = 0.0,
        G0           = 9.80665, // m_s
        WGS_84_MU    = 3986005.000E8, // Nm2_kg
        EGM_96_MU    = 3986004.418E8; // Also m3_s2


    public Vector ECItoECR(Vector eciVec, double time)
    {
        final double newx, newy, angle;

        if (0.0 != rotationRate_rad_s)
        {
            angle = rotationRate_rad_s * time;
            newx = eciVec.x * Math.cos(angle) + eciVec.y * Math.sin(angle);
            newy = -eciVec.x * Math.sin(angle) + eciVec.y * Math.cos(angle);
        }
        else
        {
            newx = eciVec.x;
            newy = eciVec.y;
        }

        return new Vector(newx, newy, eciVec.z);
    }

    public Vector ECRtoECI(Vector ecefVec, double time)
    {
        final double newx, newy, angle;

        if (0.0 != rotationRate_rad_s)
        {
            angle = rotationRate_rad_s * time;
            newx = ecefVec.x * Math.cos(angle) - ecefVec.y * Math.sin(angle);
            newy = ecefVec.x * Math.sin(angle) + ecefVec.y * Math.cos(angle);
        }
        else
        {
            newx = ecefVec.x;
            newy = ecefVec.y;
        }

        return new Vector(newx, newy, ecefVec.z);
    }

    public Vector ECRtoLLH(Vector ecefVec)
    {
        final double rx, ry, rz;

        if (0.0 != ellipsoid.flattening)
        {
            final double // Longitude is easy.
                    lon = Math.atan2(ecefVec.y, ecefVec.x),
                    p = Math.hypot(ecefVec.x, ecefVec.y),
                    theta = Math.atan2(ecefVec.z * ellipsoid.equatorialRadius_m, p * ellipsoid.polarRadius_m),
                    sinTheta = Math.sin(theta),
                    cosTheta = Math.cos(theta),
                    lat = Math.atan((ecefVec.z + ellipsoid.secondEccentricitySquared * ellipsoid.polarRadius_m * sinTheta * sinTheta * sinTheta) / 
                            (p - ellipsoid.eccentricitySquared * ellipsoid.equatorialRadius_m * cosTheta * cosTheta * cosTheta)),
                    sinLat = Math.sin(lat),
                    cosLat = Math.cos(lat),
                    N = ellipsoid.equatorialRadius_m / Math.sqrt(1.0 - ellipsoid.eccentricitySquared * sinLat * sinLat),
                    h = p / cosLat - N;

            rx = lat;
            ry = lon;
            rz = h;
        }
        else // Round-Earth
        {
            final double
                    dist = ecefVec.magnitude(),
                    lat = Math.asin(ecefVec.z / dist),
                    lon = Math.atan2(ecefVec.y, ecefVec.x),
                    h = dist - ellipsoid.equatorialRadius_m;

            rx = lat;
            ry = lon;
            rz = h;
        }

        return new Vector(rx, ry, rz);
    }

    public Vector LLHtoECR(Vector llhVec)
    {
        final double rx, ry, rz;

        if (0.0 != ellipsoid.flattening)
        {
            final double 
                    sinLat = Math.sin(llhVec.x),
                    cosLat = Math.cos(llhVec.x),
                    sinLon = Math.sin(llhVec.y),
                    cosLon = Math.cos(llhVec.y),
                    N = ellipsoid.equatorialRadius_m / Math.sqrt(1.0 - ellipsoid.eccentricitySquared * sinLat * sinLat),
                    h = llhVec.z;

            rx = (N + h) * cosLat * cosLon;
            ry = (N + h) * cosLat * sinLon;
            rz = (((ellipsoid.polarRadius_m * ellipsoid.polarRadius_m) /
                    (ellipsoid.equatorialRadius_m * ellipsoid.equatorialRadius_m)) * N + h) * sinLat;
        }
        else // Round-Earth
        {
            final double
                    sinLat = Math.sin(llhVec.x),
                    cosLat = Math.cos(llhVec.x),
                    sinLon = Math.sin(llhVec.y),
                    cosLon = Math.cos(llhVec.y),
                    N = ellipsoid.equatorialRadius_m,
                    h = llhVec.z;

            rx = (N + h) * cosLat * cosLon;
            ry = (N + h) * cosLat * sinLon;
            rz = (N + h) * sinLat;
        }

        return new Vector(rx, ry, rz);
    }

    public Vector ECItoLLH (Vector eciVec, double time)
    {
        return ECRtoLLH(ECItoECR(eciVec, time));
    }

    public Vector LLHtoECI (Vector llhVec, double time)
    {
        return ECRtoECI(LLHtoECR(llhVec), time);
    }


    public Vector ECItoECRVelocity(Vector eciVelVec, Vector eciPosVec, double time)
    {
        final double newx, newy;

        if (0.0 != rotationRate_rad_s)
        {
            final double
                    angle = rotationRate_rad_s * time,
                    S = Math.sin(angle),
                    C = Math.cos(angle),
                    wS = rotationRate_rad_s * S,
                    wC = rotationRate_rad_s * C;

            newx = eciVelVec.x * C + eciVelVec.y * S - eciPosVec.x * wS + eciPosVec.y * wC;
            newy = -eciVelVec.x * S + eciVelVec.y * C - eciPosVec.x * wC - eciPosVec.y * wS;
        }
        else
        {
            newx = eciVelVec.x;
            newy = eciVelVec.y;
        }

        return new Vector(newx, newy, eciVelVec.z);
    }

    public Vector ECRtoECIVelocity(Vector ecefVelVec, Vector ecefPosVec, double time)
    {
        final double newx, newy;

        if (0.0 != rotationRate_rad_s)
        {
            final double
                    angle = rotationRate_rad_s * time,
                    S = Math.sin(angle),
                    C = Math.cos(angle),
                    wS = rotationRate_rad_s * S,
                    wC = rotationRate_rad_s * C;

            newx = ecefVelVec.x * C - ecefVelVec.y * S - ecefPosVec.x * wS - ecefPosVec.y * wC;
            newy = ecefVelVec.x * S + ecefVelVec.y * C + ecefPosVec.x * wC - ecefPosVec.y * wS;
        }
        else
        {
            newx = ecefVelVec.x;
            newy = ecefVelVec.y;
        }

        return new Vector(newx, newy, ecefVelVec.z);
    }


    public Vector ECItoECRAcceleration(Vector eciAccVec, Vector eciVelVec, Vector eciPosVec, double time)
    {
        final double newx, newy;

        if (0.0 != rotationRate_rad_s)
        {
            final double
                    angle = rotationRate_rad_s * time,
                    S = Math.sin(angle),
                    C = Math.cos(angle),
                    wS = rotationRate_rad_s * S,
                    wC = rotationRate_rad_s * C,
                    w2S = rotationRate_rad_s * wS,
                    w2C = rotationRate_rad_s * wC;

            newx =  eciAccVec.x * C + eciAccVec.y * S - 2.0 * eciVelVec.x * wS + 2.0 * eciVelVec.y * wC - eciPosVec.x * w2C - eciPosVec.y *w2S;
            newy = -eciAccVec.x * S + eciAccVec.y * C - 2.0 * eciVelVec.x * wC - 2.0 * eciVelVec.y * wS + eciPosVec.x * w2S - eciPosVec.y *w2C;
        }
        else
        {
            newx = eciVelVec.x;
            newy = eciVelVec.y;
        }

        return new Vector(newx, newy, eciVelVec.z);
    }

    public Vector ECRtoECIAcceleration(Vector ecefAccVec, Vector ecefVelVec, Vector ecefPosVec, double time)
    {
        final double newx, newy;

        if (0.0 != rotationRate_rad_s)
        {
            final double
                    angle = rotationRate_rad_s * time,
                    S = Math.sin(angle),
                    C = Math.cos(angle),
                    wS = rotationRate_rad_s * S,
                    wC = rotationRate_rad_s * C,
                    w2S = rotationRate_rad_s * wS,
                    w2C = rotationRate_rad_s * wC;

            newx = ecefAccVec.x * C - ecefAccVec.y * S - 2.0 * ecefVelVec.x * wS - 2.0 * ecefVelVec.y * wC - ecefPosVec.x * w2C + ecefPosVec.y * w2S;
            newy = ecefAccVec.x * S + ecefAccVec.y * C + 2.0 * ecefVelVec.x * wC - 2.0 * ecefVelVec.y * wS - ecefPosVec.x * w2S - ecefPosVec.y * w2C;
        }
        else
        {
            newx = ecefVelVec.x;
            newy = ecefVelVec.y;
        }

        return new Vector(newx, newy, ecefVelVec.z);
    }


    public Vector inverse(Vector p1, Vector p2) throws FatalException
    {
        final double distance, bearing;

        if (0.0 != ellipsoid.flattening)
        {
            final double
                    L = p2.y - p1.y,
                    f = ellipsoid.flattening,
                    tanU1 = (1.0 - f) * Math.tan(p1.x),
                    cosU1 = 1.0 / Math.hypot(1.0, tanU1),
                    sinU1 = tanU1 * cosU1,
                    tanU2 = (1.0 - f) * Math.tan(p2.x),
                    cosU2 = 1.0 / Math.hypot(1.0, tanU2),
                    sinU2 = tanU2 * cosU2;

            double lam = L, lamPrev;
            int i = 0;
            do
            {
                lamPrev = lam;

                final double
                        sinLam = Math.sin(lam),
                        cosLam = Math.cos(lam),
                        sinSig = Math.hypot(cosU2 * sinLam, cosU1 * sinU2 - sinU1 * cosU2 * cosLam),
                        cosSig = sinU1 * sinU2 + cosU1 * cosU2 * cosLam,
                        sig = Math.atan2(sinSig, cosSig),
                        sinAlpha = cosU1 * cosU2 * sinLam / sinSig,
                        cos2Alpha = 1.0 - sinAlpha * sinAlpha,
                        C = f / 16.0 * cos2Alpha * (4.0 + f * (4.0 - 3.0 * cos2Alpha));
                double
                        cos2sigM;
                if (0.0 == cos2Alpha)
                {
                    cos2sigM = 0.0;
                }
                else
                {
                    cos2sigM = cosSig - 2.0 * sinU1 * sinU2 / cos2Alpha;
                }

                lam = L + (1.0 - C) * f * sinAlpha * (sig + C * sinSig * (cos2sigM + C * cosSig * (-1.0 + 2.0 * cos2sigM * cos2sigM)));
            }
            while ((Math.abs(lam - lamPrev) > (16 * Math.ulp(lam))) && (++i < 100)); // Arbitrary iteration limit

            if (i >= 100)
            {
                throw new FatalException("Could not determine azimuth.");
            }

            final double
                    sinLam = Math.sin(lam),
                    cosLam = Math.cos(lam),
                    sinSig = Math.hypot(cosU2 * sinLam, cosU1 * sinU2 - sinU1 * cosU2 * cosLam),
                    cosSig = sinU1 * sinU2 + cosU1 * cosU2 * cosLam,
                    sig = Math.atan2(sinSig, cosSig),
                    sinAlpha = cosU1 * cosU2 * sinLam / sinSig,
                    cos2Alpha = 1.0 - sinAlpha * sinAlpha,
                    u2 = cos2Alpha * ellipsoid.secondEccentricitySquared,
                    A = 1.0 + u2 / 16384.0 * (4096.0 + u2 * (-768.0 + u2 * (320.0 - 175.0 * u2))),
                    B = u2 / 1024.0 * (256.0 + u2 * (-128.0 + u2 * (74.0 - 47.0 * u2)));
            double
                    cos2sigM;
            if (0.0 == cos2Alpha)
            {
                cos2sigM = 0.0;
            }
            else
            {
                cos2sigM = cosSig - 2.0 * sinU1 * sinU2 / cos2Alpha;
            }
            final double
                    delSig = B * sinSig * (cos2sigM + 0.25 * B * (cosSig * (-1.0 + 2.0 * cos2sigM * cos2sigM) - B / 6.0 * cos2sigM * (-3.0 + 4.0 * sinSig * sinSig) * (-3.0 + 4.0 * cos2sigM * cos2sigM)));

            bearing = Math.atan2(cosU2 * sinLam, cosU1 * sinU2 - sinU1 * cosU2 * cosLam);
            distance = ellipsoid.polarRadius_m * A * (sig - delSig);
        }
        else // Round-Earth
        {
            final double
                    delLon = p2.y - p1.y,
                    sinLat1 = Math.sin(p1.x),
                    sinLat2 = Math.sin(p2.x),
                    cosLat1 = Math.cos(p1.x),
                    cosLat2 = Math.cos(p2.x),
                    sinLon = Math.sin(delLon),
                    cosLon = Math.cos(delLon),
                    bearingNum = cosLat2 * sinLon,
                    bearingDenom = cosLat1 * sinLat2 - sinLat1 * cosLat2 * cosLon,
                    distanceNum = Math.sqrt(bearingNum * bearingNum + bearingDenom * bearingDenom),
                    distanceDenom = sinLat1 * sinLat2 + cosLat1 * cosLat2 * cosLon;

            bearing = Math.atan2(bearingNum, bearingDenom);
            distance = Math.abs(ellipsoid.equatorialRadius_m * Math.atan2(distanceNum, distanceDenom));
        }

        return new Vector(distance, bearing, 0.0);
    }

}
