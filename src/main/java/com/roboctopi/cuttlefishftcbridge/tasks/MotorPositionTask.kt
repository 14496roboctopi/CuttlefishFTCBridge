package com.roboctopi.cuttlefishftcbridge.tasks

import com.roboctopi.cuttlefish.Queue.Task
import com.roboctopi.cuttlefishftcbridge.devices.CuttleMotor

class MotorPositionTask(val position:Double, val motor: CuttleMotor, val wait:Boolean = false, val epsilon: Float = 0.05f):Task{

    override fun loop(): Boolean {
        motor.setPosition(position);
        if(wait)
        {
            return motor.positionController.isAtGoal(epsilon);
        }
        else
        {
            return true
        }
    }
}