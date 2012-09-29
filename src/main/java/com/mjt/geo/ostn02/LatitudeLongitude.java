package com.mjt.geo.ostn02;

/**
 * A simple data structure containing a latitude and longitude. If you already
 * have a lat/lon class and don't want another one, consider extending
 * <code>Osgb36ToEtrs89</code> and <code>Etrs89ToOsgb36</code>.
 * @author Michael Tandy
 */
public class LatitudeLongitude {
    private final double lat;
    private final double lon;

    public LatitudeLongitude(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
    }
    
    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }
    
    public EastingNorthing toEastingNorthing() {
        Etrs89ToOsgb36<EastingNorthing> converter = new Etrs89ToOsgb36<EastingNorthing>() {
            @Override
            EastingNorthing produceOutput(double east, double north) {
                return new EastingNorthing(east,north);
            }
        };
        
        return converter.convert(lat, lon);
    }
    
}
