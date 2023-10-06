package com.roboctopi.cuttlefishftcbridge.devices;

/**
 * Generic digital sensor
 * */
public class CuttleDigital {
    public int port;
    public CuttleRevHub hub;
    /**
     * @param revHub
     * @param digitalPort
     * */
    public CuttleDigital(CuttleRevHub revHub,int digitalPort)
    {
        hub = revHub;
        port = digitalPort;
    }
    /**
     * Get the state of the digital sensor.
     * */
    public boolean getState()
    {
        return hub.bulkData.getDigitalInput(port);
    }
}
