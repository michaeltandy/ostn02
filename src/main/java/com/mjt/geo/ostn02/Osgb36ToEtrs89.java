package com.mjt.geo.ostn02;

/**
 * Converts easting/northing (OSGB36 datum) to latitude/longitude (ETRS89
 * datum). Based on calculations documented in the Ordnance Survey 
 * "Transformations and OSGM02 user guide". For more information, see
 * <a href="http://www.ordnancesurvey.co.uk/oswebsite/gps/osnetfreeservices/furtherinfo/questdeveloper.html">
 * OSTN02 and OSGM02 for Developers</a>.
 * 
 * @author Michael Tandy
 */
public abstract class Osgb36ToEtrs89<E> {
    
    public E convert(double east, double north) {
        return findAndApplyShift(east,north);
    }
    
    private E findAndApplyShift(double east,double north) {
        Ostn02LookupTable.Shift s = Ostn02LookupTable.getShiftFrom((float)east, (float)north);
        return TransverseMercatorToLatLon(east-s.getEastShift(), north-s.getNorthShift());
    }
    
    private E TransverseMercatorToLatLon(double east, double north) {
        // GRS80 parameters:
        double a = 6378137.000;
        double b = 6356752.3141;
        double eSquared = (sq(a)-sq(b))/sq(a);
        double n0 = -100000;
        double e0 = 400000;
        double f0 = 0.9996012717;
        double lat0 = Math.PI/180.0 * 49;
        double lon0 = Math.PI/180.0 * -2;
        
        double n = (a-b)/(a+b);
        double latPrime = (north-n0)/(a*f0) +lat0;
        double M;
        
        for (int i=0 ; i<5 ; i++) {
            double ma = (1 + n + (5.0/4.0)*sq(n) + (5.0/4.0)*cube(n)) * (latPrime-lat0);
            double mb = (3*n + 3*sq(n) + (21.0/8.0)*cube(n)) * Math.sin(latPrime-lat0) * Math.cos(latPrime+lat0);
            double mc = ((15.0/8.0)*sq(n) + (15.0/8.0)*cube(n)) * Math.sin(2*(latPrime-lat0)) * Math.cos(2*(latPrime+lat0));
            double md = (35.0/24.0)*cube(n) * Math.sin(3*(latPrime-lat0)) * Math.cos(3*(latPrime+lat0));
            M = b*f0*(ma-mb+mc-md);
            latPrime = (north-n0-M)/(a*f0) + latPrime;
        }
        
        double sinLat = Math.sin(latPrime);
        double secLat = 1/Math.cos(latPrime);
        double tanLat = Math.tan(latPrime);
        
        double v = a*f0 * Math.pow(1-eSquared*sq(sinLat),-0.5);
        double rho = a*f0*(1-eSquared)*Math.pow(1-eSquared*sq(sinLat),-1.5);
        double etaSquared = v/rho-1;
        
        double VII = tanLat / (2*rho*v);
        double VIII = tanLat / (24*rho*cube(v)) * (5 + 3*sq(tanLat) + etaSquared - 9*sq(tanLat)*etaSquared);
        double IX = tanLat / (720*rho*five(v)) * (61 + 90*sq(tanLat) + 45*four(tanLat));
        double X = secLat / v;
        double XI = secLat / (6*cube(v)) * (v/rho + 2*sq(tanLat));
        double XII = secLat / (120*five(v)) * (5 + 28*sq(tanLat) + 24*four(tanLat));
        double XIIA = secLat / (5040*seven(v)) * (61 + 662*sq(tanLat) + 1320*four(tanLat) + 720*six(tanLat));
        
        double lat = latPrime - VII*sq(east-e0) + VIII*four(east-e0) - IX*six(east-e0);
        double lon = lon0 + X*(east-e0) - XI*cube(east-e0) + XII*five(east-e0) - XIIA*seven(east-e0);
        
        return produceOutput(180/Math.PI * lat,180/Math.PI * lon);
    }
    
    private static double sq(double a) {
        return a*a;
    }
    private static double cube(double a) {
        return a*a*a;
    }
    private static double four(double a) {
        return sq(sq(a));
    }
    private static double five(double a) {
        return four(a)*a;
    }
    private static double six(double a) {
        return sq(cube(a));
    }
    private static double seven(double a) {
        return sq(cube(a))*a;
    }
    
    abstract E produceOutput(double latitude,double longitude);
    
}
