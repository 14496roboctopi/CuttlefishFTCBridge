package com.roboctopi.cuttlefishftcbridge.devices;
import com.roboctopi.cuttlefish.components.RotaryEncoder;
import com.roboctopi.cuttlefish.utils.Direction;

import static com.roboctopi.cuttlefish.utils.MathUtilsKt.rFullToHalf;

/**
 * Analog rotary encoder.
 * */
public class CuttleAnalogEncoder implements RotaryEncoder
{
    public CuttleRevHub hub;
    private int direction = 1;
    public int aPort;

    /** Maximum voltage of the sensor. */
    public double maxVoltage = 5000.0;
    public double rotationOffset = 0.0;

    /**
     * @param revHub
     * @param port Analog port number
     * @param offset Amount of offset the angle of the encoder in radians
     * @param maxVoltage Voltage of the sensor in its furthest position in milli-volts. This is probably the supply voltage of the sensor.
     * */
    public CuttleAnalogEncoder(CuttleRevHub revHub, int port, double offset,double maxVoltage) {
        hub = revHub;
        aPort = port;
        rotationOffset = offset;
        this.maxVoltage = maxVoltage;
    }

    /**
     * Get the angle of the encoder.
     * */
    public double getRotation()
    {
        return  rFullToHalf(2*Math.PI*getVoltage()/maxVoltage*direction+rotationOffset);
    }
    private long pT = System.nanoTime();
    private double pPos = 0;
    private double velocity = 0;
    /**
     * Get the velocity of the encoder. Will not account for roll over and assumes that this function is being run every at least once every cycle
     * */
    @Override
    public double getVelocity()
    {
        if(hub.last_bulk_pull_time_ns != pT)
        {
            double dT = (double)(hub.last_bulk_pull_time_ns - pT)/(1000.0*1000.0*1000.0);
            velocity = (this.getRotation()-pPos)/dT;
            pPos = this.getRotation();
        }
        return velocity;
    }

    /**
     * Get the voltage of the analog sensor.
     * */
    public int getVoltage()
    {
        return hub.bulkData.getAnalogInput(aPort);
    }


    /**
     * Set the direction of the encoder
     * @param direction
     * */
    public void setDirection(Direction direction)
    {
        if(direction == Direction.REVERSE)
        {
            this.direction = -1;
        }
        else
        {
            this.direction = 1;
        }
    }

}
