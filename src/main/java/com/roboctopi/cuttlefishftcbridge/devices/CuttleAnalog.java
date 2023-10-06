package com.roboctopi.cuttlefishftcbridge.devices;

/**
 * Generic Analog Sensor
 * */
public class CuttleAnalog {
    public int port;
    public CuttleRevHub hub;

    /**
     * Generic analog sensor.
     * @param revHub
     * @param analogPort Analog port number
     * */
    public CuttleAnalog(CuttleRevHub revHub, int analogPort)
    {
        hub = revHub;
        port = analogPort;
    }

    /**
     * Get the voltage of the sensor in volts
     * */
    public double getVoltage()
    {
        return ((double)hub.bulkData.getAnalogInput(port))/1000.0;
    }

    /**
     * Get the voltage of the sensor in millivolts
     * */
    public int getVoltageMillivolts()
    {
        return hub.bulkData.getAnalogInput(port);
    }
}
