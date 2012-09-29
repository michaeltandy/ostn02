package com.mjt.geo.ostn02;

import java.io.*;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Shift lookup table. The entire table loads into memory on the first query.
 * The data is stored as integers in the resource file ostn02.bin; so stored it
 * takes up 7 MB so it's not too memory-intensive.
 * 
 * Note that, according to the Ordnance Survey "Transformations and OSGM02 user 
 * guide":
 * <blockquote>Ordnance Survey [...] permit users to copy or incorporate 
 * copyrighted material from both OSTN02, [...] onto their own PCs or into 
 * their software. However, this is dependant on the appropriate copyright 
 * notice being displayed in the software. The appropriate notices are [...]
 * '© Crown copyright 2002. All rights reserved.' [...] The appropriate mapping 
 * agency logo must also appear next to the copyright notice. Details of 
 * mapping agency logos are found in the style guides available on the web 
 * site.</blockquote>
 * 
 * <img src="../../../../../resources/ostn005.jpg" />
 * 
 * @author Michael Tandy
 */
public class Ostn02LookupTable {
    private static final float SCALE_FACTOR = 10000;
    private static final int MIN_EAST_KM = 0;
    private static final int MAX_EAST_KM = 700;
    private static final int MIN_NORTH_KM = 0;
    private static final int MAX_NORTH_KM = 1250;
    private static final int RECORD_COUNT = (MAX_EAST_KM-MIN_EAST_KM+1)*(MAX_NORTH_KM-MIN_NORTH_KM+1);
    
    private static final AtomicReference<int[]> data = new AtomicReference<int[]>(null);
    
    private Ostn02LookupTable() { }
    
    /**
     * Get shift from ETRS89 to OSGB36. We find the four corners of the 1km
     * square the easting/northing is in, and perform bilinear interpolation.
     * @param easting
     * @param northing
     * @return 
     */
    public static Shift getShiftFrom(float easting, float northing) {
        float e = easting/1000;
        float n = northing/1000;
        int north=(int)Math.ceil(n);
        int south=(int)Math.floor(n);
        int east=(int)Math.ceil(e);
        int west=(int)Math.floor(e);
        
        Shift NW = getByKilometres(west,north);
        Shift NE = getByKilometres(east,north);
        Shift SW = getByKilometres(west,south);
        Shift SE = getByKilometres(east,south);
        
        float c = north-n;
        float d = n-south;
        float f = east-e;
        float g = e-west;
        
        float eastShift = (1-c)*(1-g)*NW.getEastShift() 
                + (1-d)*(1-g)*SW.getEastShift() 
                + (1-c)*(1-f)*NE.getEastShift() 
                + (1-d)*(1-f)*SE.getEastShift();
        
        float northShift = (1-c)*(1-g)*NW.getNorthShift() 
                + (1-d)*(1-g)*SW.getNorthShift() 
                + (1-c)*(1-f)*NE.getNorthShift() 
                + (1-d)*(1-f)*SE.getNorthShift();
        
        return new Shift(eastShift,northShift);
    }
    
    /**
     * Get shift from OSGB36 to ETRS89. Iteratively performs 
     * <code>getShiftFrom</code> to find an ETRS89 starting point that would 
     * shift to this OSGB36 point.
     * @param kmEast
     * @param kmNorth
     * @return 
     */
    public static Shift getShiftTo(final float easting, final float northing) {
        float iterEast = easting;
        float iterNorth = northing;
        Shift s = new Shift(0,0);
        
        for (int i=0 ; i<3 ; i++) {
            s = getShiftFrom(iterEast,iterNorth);
            float shiftedEast = iterEast+s.getEastShift();
            float shiftedNorth = iterNorth+s.getNorthShift();
            iterEast += easting-shiftedEast;
            iterNorth += northing-shiftedNorth;
        }
        
        return s;
    }
    
    private static Shift getByKilometres(int kmEast, int kmNorth) {
        if (kmEast > MAX_EAST_KM || kmEast < MIN_EAST_KM || kmNorth > MAX_NORTH_KM || kmNorth < MIN_NORTH_KM)
            return new Shift(0,0);
        int index = 1+kmEast+(MAX_EAST_KM+1)*kmNorth;
        return getByIndex(index);
    }
    
    private static Shift getByIndex(int index) {
        try {
            int[] backing = data.get();
            if (backing == null) {
                backing = loadData();
                data.set(backing);
            }
            int i = 2*(index-1);
            float east = backing[i]/SCALE_FACTOR;
            float north = backing[i+1]/SCALE_FACTOR;
            return new Shift(east,north);
        } catch (IOException e) {
            // This should never happen.
            return new Shift(0,0);
        }
    }
    
    private static int[] loadData() throws IOException {
        System.out.println("Ordnance Survey(r) OSTN02(tm) Enabled. OSTN02 data (c) Crown copyright 2002");
        int[] rawData = new int[2*RECORD_COUNT];
        DataInputStream is = new DataInputStream(new BufferedInputStream(Ostn02LookupTable.class.getResourceAsStream("/ostn02.bin")));
        for (int i=0 ; i<rawData.length ; i++) {
            rawData[i] = is.readInt();
        }
        return rawData;
    }
    
    public static class Shift {
        private final float eastShift;
        private final float northShift;
        Shift(float east,float north) {
            this.eastShift = east;
            this.northShift = north;
        }

        public float getEastShift() {
            return eastShift;
        }

        public float getNorthShift() {
            return northShift;
        }
    }
    
}
