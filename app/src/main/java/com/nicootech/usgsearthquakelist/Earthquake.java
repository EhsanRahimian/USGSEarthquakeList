package com.nicootech.usgsearthquakelist;

public class Earthquake {

    private double mag;
    private long time;
    private String location;
    private String url;
    public Earthquake(double mag,String location,long time,String url)
    {
        this.mag=mag;
        this.time=time;
        this.location=location;
        this.url=url;
    }
    public double getMag()
    {
        return mag;
    }
    public String getLocation()
    {
        return location;
    }
    public long getTime()
    {
        return time;
    }
    public String getUrl()
    {
        return url;
    }

}
