package com.mjt.geo.ostn02;

import java.util.Random;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Michael Tandy
 */
public class EastingNorthingTest {
    
    public EastingNorthingTest() {
    }

    @Test
    public void testToLatitudeLongitude() {
        EastingNorthing instance = new EastingNorthing(651409.903,313177.270);
        EastingNorthing result = instance.toLatitudeLongitude().toEastingNorthing();
        
        assertEquals(instance.getEast(),result.getEast(),0.01);
        assertEquals(instance.getNorth(),result.getNorth(),0.01);
    }
    
    @Test
    public void testRandomly() {
        System.out.println("Testing 100 random eastings/northings...");
        for (int i=0 ; i<100 ; i++) {
            float east = randBetween(384000,557000);
            float north = randBetween(128000,302000);
            EastingNorthing instance = new EastingNorthing(east,north);
            EastingNorthing result = instance.toLatitudeLongitude().toEastingNorthing();
            assertEquals(instance.getEast(),result.getEast(),0.01);
            assertEquals(instance.getNorth(),result.getNorth(),0.01);
        }
        System.out.println("...random test OK.");
    }
    
    public float randBetween(float low, float high) {
        return low + (high-low)*new Random().nextFloat();
    }
}
