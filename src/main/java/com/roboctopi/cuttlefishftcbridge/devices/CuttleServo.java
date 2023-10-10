package com.roboctopi.cuttlefishftcbridge.devices;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.roboctopi.cuttlefish.components.Servo;

import java.util.ArrayList;

/**
 * Cuttlefish compatible servo.
 * Contains a list of preset positions which can be appended to using the addPreset() function.
 *
 * */
public class CuttleServo implements Servo {

    private double pos = 0.0;
    public int port;
    boolean enabled = false;
    final boolean FTCServo;
    /**
     * Array of preset positions.
     * */
    public ArrayList<Double> presetsArr;
    CuttleRevHub hub;
    com.qualcomm.robotcore.hardware.Servo ftcServoDevice;

    /**
     * Initialize servo using cuttlefish direct access system
     * @param revHub
     * @param servoPort
     * */
    public CuttleServo(CuttleRevHub revHub, int servoPort)
    {
        port = servoPort;
        hub = revHub;
        presetsArr = new ArrayList<Double>();
        FTCServo= false;
    }
    /**
     * Initialize servo using hardwareMap
     * @param hardwareMap hardwareMap object
     * @param name Name of the servo in the config
     * */
    public CuttleServo(HardwareMap hardwareMap, String name)
    {
        FTCServo= true;
        presetsArr = new ArrayList<Double>();
        ftcServoDevice = hardwareMap.get(com.qualcomm.robotcore.hardware.Servo.class,name);

    }

    /**
     * Set the target position of the servo
     * @param position Target position
     * */
    @Override
    public void setPosition(double position) {
        pos = position;
        if(!FTCServo)
        {
            hub.setServoPosition(port,pos);
            if(!enabled)
            {
                enablePWM(true);
            }
        }
        else
        {
            ftcServoDevice.setPosition(position);
        }
    }

    /**
     * Add a position to the list of presets.
     * @param position
     * */
    public void addPreset(double position)
    {
        presetsArr.add(position);
    }
    /**
     * Go to a preset position.
     * @param preset index of the preset position
     * */
    public void goToPreset(int preset)
    {
        if(preset>= presetsArr.size())
        {
            new Throwable("Preset For servo on port "+ this.port+ " is out of bounds").printStackTrace();
            return;
        }
        setPosition(presetsArr.get(preset));
    }
    /**
     * Get the value of a preset position. Will return -1.0 if position the index is out of bounds.
     * @param preset index of the preset position
     * */
    public double getPreset(int preset)
    {
        if(preset>= presetsArr.size())
        {
            new Throwable("Preset For servo on port "+ this.port+ " is out of bounds").printStackTrace();
            return -1.0;
        }
        return presetsArr.get(preset);
    }

    /**
     * Enable or disable PWM on the servo port. This will not work if the servo was obtained using hardwareMap.
     * @param  enable If set to true PWM will be enabled, and if set to false PWM will be disabled
     * */
    public void enablePWM(boolean enable)
    {
        if(!FTCServo)
        {
            hub.enableServoPWM(port,enable);
            enabled = enable;
        }
    }

    /**
     * Get the target position of the servo.
     * <br>
     * IMPORTANT: This will not give the actual position of the servo.
     * */
    @Override
    public double getPosition() {
        return pos;
    }
}
