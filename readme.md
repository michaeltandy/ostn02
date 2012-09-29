# OSTN02

This is a Java library implementing the Ordnance Survey's OSTN02 conversion, 
between OS easting/northing (example: 528376.804,180799.396)
and what everyone else uses, latitude/longitude (example: 51.511547,-0.151412). 
In other words, the same conversion as [this online converter](http://gps.ordnancesurvey.co.uk/convert.asp)

## So how do I use it?

```Java
        EastingNorthing first = new EastingNorthing(528376.804,180799.396);
        LatitudeLongitude second = first.toLatitudeLongitude();
        EastingNorthing third = second.toEastingNorthing();
        
        System.out.println("First: "+first.getEast()+","+first.getNorth());
        System.out.println("Second: "+second.getLat()+","+second.getLon());
        System.out.println("Third: "+third.getEast()+","+third.getNorth());
```
Produces:
```
First: 528376.804,180799.396
Second: 51.51154701260418,-0.15141202586203606
Third: 528376.8020999633,180799.3969603534
```

## What license is it under?

We can't do this conversion without using some Ordnance Survey data.

The OSTN02 conversion table data is copyrighted by Ordnance Survey. You can 
copy it and integrate it into your software, but they require attribution;
for more info see [this documentation](http://www.ordnancesurvey.co.uk/oswebsite/gps/osnetfreeservices/furtherinfo/questdeveloper.html)
The data is (c) Crown copyright 2002, All rights reserved.

![Logo reading Ordnance Survey OSTN02 Enabled](src/main/resources/ostn005.jpg "OS logo")

The java files that comprise the rest of this project are (c) Michael Tandy; 
they are under the [MIT license](http://en.wikipedia.org/wiki/MIT_License).

## How does it compare to other similar projects?

I wanted something simple; this project only has 5 classes, and most users will
only need two of them - `EastingNorthing` and `LatitudeLongitude`.

* [Jcoord](http://www.jstott.me.uk/jcoord/) uses a Helmert transform instead of
the lookup table, so it's less accurate. Jcoord is smaller as it doesn't 
have to include the lookup table.

* [GeoTools](http://www.geotools.org/) is a library with lots of features but
it's big and has lots of dependencies.