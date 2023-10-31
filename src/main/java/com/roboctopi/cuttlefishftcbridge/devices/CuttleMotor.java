package com.roboctopi.cuttlefishftcbridge.devices;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.roboctopi.cuttlefish.components.Motor;
import com.roboctopi.cuttlefish.utils.Direction;
import com.roboctopi.cuttlefish.controller.MotorPositionController;

/**
 * Cuttlefish DCMotor implementation.
 * */
public class CuttleMotor implements Motor
{
    CuttleRevHub hub;
    int mPort;
    int sign = 1;
    public MotorPositionController positionController;
    boolean holdPosition = false;
    public double power;
    public boolean interlaced;

    public double speed = 0.0f;
    double lastPos = 0.0f;

    /**
     * @param revHub
     * @param port
     * */
    public CuttleMotor(CuttleRevHub revHub,int port)
    {
        hub = revHub;
        mPort = port;
    }
    /**
     * @param revHub
     * @param port
     * @param positionController Preconfigured motor position controller for positional control of the motor
     * */
    public CuttleMotor(CuttleRevHub revHub,int port, MotorPositionController positionController)
    {
        hub = revHub;
        mPort = port;
        this.positionController = positionController;
    }

    /**
     * @param power
     * */
    @Override
    public void setPower(double power) {
        this.power = power;
        if(!interlaced)
        {
            sendPower();
        }
    }

    /**
     * Send cached motor power to the hub.
     * <br>
     * This is not necessary and should not be used under ordinary conditions.
     * */
    public void sendPower()
    {
        hub.setMotorPower(mPort,sign*power);
    }


    /**
     * Set the direction of the motor
     * @param direction
     * */
    @Override
    public void setDirection(Direction direction) {
        if(direction == Direction.FORWARD)
        {
            sign=1;
        }
        else
        {
            sign=-1;
        }
    }

    /**
     * Enable or disablePID position hold if motorPositionController has been set
     * @param enable Enable if true, disable if false
     * */
    public void enablePositionPID(boolean enable)
    {
        if(positionController != null)
        {
            if(enable)
            {
                positionController.enable();
            }
            else
            {
                positionController.disable();
            }
            holdPosition = enable;
        }
    }

    private long pTime = System.nanoTime();
    /**
     * Execute the positionController loop
     * */
    public void loop()
    {
        if(positionController != null&&holdPosition)
        {
            positionController.loop();
            Long time = System.nanoTime();
            double dT = ((double) (time - pTime)) / (1000.0 * 1000.0 * 1000.0);
            pTime = time;

            speed = (positionController.getPosition() - lastPos)/(dT);

            lastPos = positionController.getPosition();
        }

    }

    /**
     * Set the angle of the MotorPositionController.
     * <br>
     * NOTE: This will bypass the upper and lower position limits
     * @see MotorPositionController
     * @param angle Angle to set in radians
     * */
    public void setAngle(double angle)
    {
        if(positionController != null) {
            positionController.setAngle(angle);
        }
    }

    /**
     * Set the position of the motor using the MotorPositionController with scale applied
     * @see MotorPositionController
     * @param position Position to go to in the units set by the scale varible inside the MotorPositionController
     * */
    public void setPosition(double position)
    {

        if(positionController != null) {
            positionController.setPosition(position);
        }
    }


    /**
     * Get motor current in milli-amps.
     * <br>
     * WARNING: This will poll the hub an extra time costing about 3ms.
     * @return Motor current in milli-amps
     * */
    public int getCurrent()
    {
        return this.hub.getMotorCurrent(this.mPort);
    }

    /**
     * Set the zero power behaviour of the motor.
     * @param behaviour
     * */
    public void setZeroPowerBehaviour(DcMotor.ZeroPowerBehavior behaviour)
    {
        this.hub.setMotorZeroPowerBehaviour(this.mPort,behaviour);
    }


//    public double getEncoderRotation()
//    {
//        if(!countsDefined)
//        {
//            for(int i = 0; i < 25; i++)
//            {
//                System.err.println("WARNING: MOTOR ON HUB \""+hub.revHub.getDeviceName()+"\" PORT "+mPort+" IS USING ENCODERS WITHOUT SET COUNTS");
//            }
//        }
//        return enc.getRotation();
//    }

}
