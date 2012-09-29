package com.mjt.geo.ostn02;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Michael Tandy
 */
public class Osgb36ToEtrs89Test {
    
    public Osgb36ToEtrs89Test() {
    }

    @Test
    public void testConvert() {
        double east = 651409.792;
        double north = 313177.448;
        
        Osgb36ToEtrs89Impl instance = new Osgb36ToEtrs89Impl();
        LatitudeLongitude result = instance.convert(east, north);
        assertEquals(52.658007833, result.getLat(), 0.00001);
        assertEquals(1.716073973, result.getLon(), 0.00001);
    }
    
    public class Osgb36ToEtrs89Impl extends Osgb36ToEtrs89<LatitudeLongitude> {
        public LatitudeLongitude produceOutput(double latitude, double longitude) {
            return new LatitudeLongitude(latitude,longitude);
        }
    }
}
