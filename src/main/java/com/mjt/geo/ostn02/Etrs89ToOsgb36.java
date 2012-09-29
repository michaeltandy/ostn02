package com.mjt.geo.ostn02;

/**
 * Converts latitude/longitude (ETRS89 datum) to easting/northing (OSGB36 
 * datum). Based on calculations documented in the Ordnance Survey 
 * "Transformations and OSGM02 user guide". For more information, see
 * <a href="http://www.ordnancesurvey.co.uk/oswebsite/gps/osnetfreeservices/furtherinfo/questdeveloper.html">
 * OSTN02 and OSGM02 for Developers</a>.
 * 
 * @author Michael Tandy
 */
public abstract class Etrs89ToOsgb36<E> {
    
    public E convert(double latitude, double longitude) {
        return LatLonToTransverseMercator(latitude,longitude);
    }
    
    private E LatLonToTransverseMercator(double latitude, double longitude) {
        double lat = Math.PI/180.0 * latitude;
        double lon = Math.PI/180.0 * longitude;
        
        // GRS80 parameters:
        double a = 6378137.000;
        double b = 6356752.3141;
        double eSquared = (sq(a)-sq(b))/sq(a);
        double n0 = -100000;
        double e0 = 400000;
        double f0 = 0.9996012717;
        double lat0 = Math.PI/180.0 * 49;
        double lon0 = Math.PI/180.0 * -2;
        
        double sinLat = Math.sin(lat);
        double cosLat = Math.cos(lat);
        double tanLat = Math.tan(lat);
        
        double n = (a-b)/(a+b);
        double v = a*f0 * Math.pow(1-eSquared*sq(sinLat),-0.5);
        double rho = a*f0*(1-eSquared)*Math.pow(1-eSquared*sq(sinLat),-1.5);
        double etaSquared = v/rho-1;
        
        
        
        double ma = (1 + n + (5.0/4.0)*sq(n) + (5.0/4.0)*cube(n)) * (lat-lat0);
        double mb = (3*n + 3*sq(n) + (21.0/8.0)*cube(n)) * Math.sin(lat-lat0) * Math.cos(lat+lat0);
        double mc = ((15.0/8.0)*sq(n) + (15.0/8.0)*cube(n)) * Math.sin(2*(lat-lat0)) * Math.cos(2*(lat+lat0));
        double md = (35.0/24.0)*cube(n) * Math.sin(3*(lat-lat0)) * Math.cos(3*(lat+lat0));
        double M = b*f0*(ma-mb+mc-md);
        
        double I = M + n0;
        double II = v/2*sinLat*cosLat;
        double III = v/24*sinLat*cube(cosLat)*(5-sq(tanLat)+9*etaSquared);
        double IIIA = v/720*sinLat*five(cosLat)*(61-58*sq(tanLat)+four(tanLat));
        double IV = v*cosLat;
        double V = (v/6)*cube(cosLat)*(v/rho - sq(tanLat));
        double VI = (v/120)*five(cosLat)*(5-18*sq(tanLat)+four(tanLat)+14*etaSquared-58*sq(tanLat)*etaSquared);
        
        double north = I + II*sq(lon-lon0) + III*four(lon-lon0) + IIIA*six(lon-lon0);
        double east = e0 + IV*(lon-lon0) + V*cube(lon-lon0) + VI*five(lon-lon0);
        
        return findAndApplyShift(east,north);
    }
    
    private E findAndApplyShift(double east,double north) {
        Ostn02LookupTable.Shift s = Ostn02LookupTable.getShiftFrom((float)east, (float)north);
        return produceOutput(east+s.getEastShift(), north+s.getNorthShift());
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
    
    abstract E produceOutput(double east,double north);
    
}
