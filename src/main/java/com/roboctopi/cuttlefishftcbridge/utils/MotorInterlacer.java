package com.roboctopi.cuttlefishftcbridge.utils;

import com.roboctopi.cuttlefishftcbridge.devices.CuttleMotor;


import java.util.ArrayList;

public class MotorInterlacer {
    public ArrayList<CuttleMotor[]> groups = new ArrayList<>();

    public void addMotorGroup(CuttleMotor... motors)
    {
        groups.add(motors);
        for(int j = 0; j < motors.length; j++)
        {
            motors[j].interlaced = true;
        }
    }
    private int i = 0;
    public void loop()
    {
        CuttleMotor[] group = groups.get(i);
        for(int j = 0; j < group.length; j++)
        {
            group[j].sendPower();
        }
        i++;
        i %= group.length;
    }

}
