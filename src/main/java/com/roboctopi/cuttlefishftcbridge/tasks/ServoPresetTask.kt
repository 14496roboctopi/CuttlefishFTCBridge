package com.roboctopi.cuttlefishftcbridge.tasks

import com.roboctopi.cuttlefish.Queue.Task
import com.roboctopi.cuttlefishftcbridge.devices.CuttleServo

class ServoPresetTask(val servo: CuttleServo, val preset: Int): Task
{
    override fun loop(): Boolean
    {
        servo.goToPreset(preset);
        return true;
    }
}