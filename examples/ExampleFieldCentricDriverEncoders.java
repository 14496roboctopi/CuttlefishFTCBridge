package com.roboctopi.cuttlefishftcbridge.examples;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.roboctopi.cuttlefish.utils.Pose;


@TeleOp(name="Example Field-Centric Driver Encoders", group="Example ")
public class ExampleFieldCentricDriverEncoders extends ExampleInitializedOpmode {
    public void onInit() {
        super.onInit();
    }
    public void main() {
        super.main();
    }

    public void mainLoop()
    {
        super.mainLoop();

        // Create a pose representing the direction that the robot should go relative to the field
        Pose direction = new Pose(gamepad1.left_stick_x,-gamepad1.left_stick_y,-gamepad1.right_stick_x);

        /*
        Get direction that the bot it facing in radians from the encoder localizer
        You can replace this with the heading obtained from a different source such as the IMU as long as you convert it to radians
        */
        double heading = encoderLocalizer.getPos().getR();

        // Rotate the direction of movement to cancel out the rotation of the bot
        direction.rotate(-heading, new Pose(0.0,0.0,0.0));

        // Tell the bot to move
        chassis.setVec(direction);
    }
}
