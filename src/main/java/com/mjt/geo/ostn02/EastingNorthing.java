package com.mjt.geo.ostn02;

/**
 * A simple data structure containing an easting and northing. If you already
 * have an easting/northing class and don't want another one, consider extending
 * <code>Osgb36ToEtrs89</code> and <code>Etrs89ToOsgb36</code>.
 * @author Michael Tandy
 */
public class EastingNorthing {
    private final double east;
    private final double north;
    
    public EastingNorthing(double east, double north) {
        this.east = east;
        this.north = north;
    }

    public double getEast() {
        return east;
    }

    public double getNorth() {
        return north;
    }
    
    public LatitudeLongitude toLatitudeLongitude() {
        Osgb36ToEtrs89<LatitudeLongitude> converter = new Osgb36ToEtrs89<LatitudeLongitude>() {
            public LatitudeLongitude produceOutput(double latitude, double longitude) {
                return new LatitudeLongitude(latitude,longitude);
            }
        };
        
        return converter.convert(east, north);
    }
}
