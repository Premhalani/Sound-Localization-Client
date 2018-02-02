package com.a605.cse.audiosampler.dataobjects;

public class SyncDataObject extends NetworkDataObject {
    public String deviceID;
    public String timestamp;

    public SyncDataObject(String deviceID, String timestamp)
    {
        super("Christian");
        this.deviceID = deviceID;
        this.timestamp = timestamp;
    }
}
