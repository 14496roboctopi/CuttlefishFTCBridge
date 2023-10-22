package com.roboctopi.cuttlefishftcbridge.devices;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.roboctopi.cuttlefish.components.RotaryEncoder;
import com.roboctopi.cuttlefish.utils.Direction;

/**
 * Rotary encoder connected through a motor encoder port
 * */
public class CuttleEncoder implements RotaryEncoder
{
    public CuttleRevHub hub;
    private final double encTicks;
    private int direction = 1;
    public int mPort;

    /**
     * @param revHub
     * @param port Motor port of the encoder
     * @param countsPerRevolution Number of counts per revolution of the encoder
     * */
    public CuttleEncoder(CuttleRevHub revHub, int port, double countsPerRevolution) {
        hub = revHub;
        encTicks = countsPerRevolution;
        mPort = port;
    }

    /**
     * Get the rotation of the encoder in radians
     * */
    public double getRotation()
    {
        return 2*Math.PI*getCounts()/encTicks*direction;
    }
    public double getVelocity()
    {
        return 2*Math.PI*hub.bulkData.getEncoderVelocity(mPort)/encTicks*direction;
    }

    /**
     * Get the number of counts that the encoder has turned
     * */
    public int getCounts()
    {
        return hub.bulkData.getEncoderPosition(mPort);
    }


    /**
     * Set the direction of the encoder.
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
