package com.mjt.geo.ostn02;

import java.io.*;
import java.net.URL;
import java.net.URLDecoder;

/**
 * Populate ostn02.bin the compact binary format ostn02 lookup table file. You
 * should already have a copy of ostn02.bin - this regenerates it (useful if 
 * you want to change the encoding to make a smaller file or something similar).
 * 
 * Note that this will populate the file in the build folder rather than the 
 * source folder, so you'll need to copy that across.
 * @author Michael Tandy
 */
public class PopulateOstn02Table {
    private final static double SCALEFACTOR = 10000;
    
    public static void main(String[] args) throws Exception {
        InputStream s = PopulateOstn02Table.class.getResourceAsStream("/OSTN02_OSGM02_GB.txt");
        BufferedReader br = new BufferedReader(new InputStreamReader(s));
        
        URL outputUrl = PopulateOstn02Table.class.getResource("/ostn02.bin");
        String outputFilename = URLDecoder.decode(outputUrl.getPath(), "UTF-8");
        DataOutputStream out = new DataOutputStream(new FileOutputStream(outputFilename));
        
        double minEs = Double.POSITIVE_INFINITY;
        double maxEs = Double.NEGATIVE_INFINITY;
        double minNs = Double.POSITIVE_INFINITY;
        double maxNs = Double.NEGATIVE_INFINITY;
        
        String line = br.readLine();
        while (line != null) {
            String[] parts = line.split(",");
            // Format is: Record number, easting, northing, eastshift, northshift, height, datumflag
            // Easting range: 0 to 700k, steps of 1km.
            // Northing range: 0 to 1250k, steps of 1km
            // East shifts range 0 to 103.443
            // North shifts -81.603 to 0
            
            int recordNum = Integer.parseInt(parts[0]);
            int easting = Integer.parseInt(parts[1]);
            int northing = Integer.parseInt(parts[2]);
            double eastShift = Double.parseDouble(parts[3]);
            double northShift = Double.parseDouble(parts[4]);
            
            minEs = Math.min(minEs,eastShift);
            maxEs = Math.max(maxEs,eastShift);
            minNs = Math.min(minNs,northShift);
            maxNs = Math.max(maxNs,northShift);
            
            out.writeInt((int)Math.round(eastShift*SCALEFACTOR));
            out.writeInt((int)Math.round(northShift*SCALEFACTOR));
            
            line = br.readLine();
        }
        
        out.close();
        br.close();
        
        System.out.println("East shift " + minEs + " to " + maxEs);
        System.out.println("North shift " + minNs + " to " + maxNs);
        System.out.println("Populated " + outputFilename);
    }
    
}
