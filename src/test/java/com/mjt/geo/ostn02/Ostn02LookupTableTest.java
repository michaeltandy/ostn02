package com.mjt.geo.ostn02;

import com.mjt.geo.ostn02.Ostn02LookupTable.Shift;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test values from "Transformations and OSGM02 user guide" appendix A.
 * @author Michael Tandy
 */
public class Ostn02LookupTableTest {
    
    public Ostn02LookupTableTest() {
    }

    @Test
    public void testGetShiftFrom() {
        float easting = 651307.003F;
        float northing = 313255.686F;
        Shift result = Ostn02LookupTable.getShiftFrom(easting, northing);
        assertEquals(102.789,result.getEastShift(),0.01);
        assertEquals(-78.238,result.getNorthShift(),0.01);
    }
    
    @Test
    public void testGetShiftTo() {
        float easting = 651307.003F+102.789F;
        float northing = 313255.686F-78.238F;
        Shift result = Ostn02LookupTable.getShiftTo(easting, northing);
        assertEquals(102.789,result.getEastShift(),0.01);
        assertEquals(-78.238,result.getNorthShift(),0.01);
    }
}
