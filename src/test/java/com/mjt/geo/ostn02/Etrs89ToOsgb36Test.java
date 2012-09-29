package com.mjt.geo.ostn02;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Michael Tandy
 */
public class Etrs89ToOsgb36Test {
    
    public Etrs89ToOsgb36Test() {
    }

    @Test
    public void testConvert() {
        double latitude = 52.658007833;
        double longitude = 1.716073973;
        Wgs84ToOstn02Impl instance = new Wgs84ToOstn02Impl();
        
        EastingNorthing expResult = new EastingNorthing(651409.792,313177.448);
        EastingNorthing result = instance.convert(latitude, longitude);
        
        assertEquals(expResult.getEast(), result.getEast(), 0.001);
        assertEquals(expResult.getNorth(), result.getNorth(), 0.001);
    }
    
    public class Wgs84ToOstn02Impl extends Etrs89ToOsgb36<EastingNorthing> {
        public EastingNorthing produceOutput(double east, double north) {
            return new EastingNorthing(east,north);
        }
    }
}
