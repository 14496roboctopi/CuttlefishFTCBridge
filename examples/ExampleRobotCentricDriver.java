package com.roboctopi.cuttlefishftcbridge.examples;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.roboctopi.cuttlefish.utils.Pose;


@TeleOp(name="Example Robot-Centric Driver", group="Example ")
public class ExampleRobotCentricDriver extends ExampleInitializedOpmode {
    public void onInit() {
        super.onInit();
    }
    public void main() {
        super.main();
    }
    public void mainLoop()
    {
        super.mainLoop();
        chassis.setVec(new Pose(gamepad1.left_stick_x,-gamepad1.left_stick_y,-gamepad1.right_stick_x));
    }
}
