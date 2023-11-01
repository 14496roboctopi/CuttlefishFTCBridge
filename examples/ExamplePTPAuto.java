package com.roboctopi.cuttlefishftcbridge.examples;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.roboctopi.cuttlefish.controller.Waypoint;
import com.roboctopi.cuttlefish.queue.CustomTask;
import com.roboctopi.cuttlefish.queue.DelayTask;
import com.roboctopi.cuttlefish.queue.PointTask;
import com.roboctopi.cuttlefish.utils.Pose;


@TeleOp(name="Example Field-Centric Driver Encoders", group="Example ")
public class ExamplePTPAuto extends ExampleInitializedOpmode {
    public void onInit() {
        super.onInit();
    }
    public void main() {
        super.main();
        // Drive forward 500 mm at half power
        queue.addTask(new PointTask(
                new Waypoint(
                        new Pose(0.0,500.0,0.0)
                ),
                ptpController
        ));

        // Drive to the right 500 mm while turning 90 degrees clockwise at half power
        queue.addTask(new PointTask(
                new Waypoint(
                        new Pose(500.0,500.0,-Math.PI/2),
                        0.5
                ),
                ptpController
        ));

        // Spin around for a few seconds
        // Start spinning and wait 3 seconds
        queue.addTask(new CustomTask(()->{
            chassis.setVec(new Pose(0.0,0.0,0.6));
            return true;
        }));
        queue.addTask(new DelayTask(3000));

        // Stop spinning and wait 1 second
        queue.addTask(new CustomTask(()->{
            chassis.setVec(new Pose(0.0,0.0,0.6));
            return true;
        }));
        queue.addTask(new DelayTask(1000));

        // Go back to the starting position
        queue.addTask(new PointTask(
                new Waypoint(
                        new Pose(0.0,0.0,0.0),
                        0.5
                ),
                ptpController
        ));
    }

    public void mainLoop()
    {
        super.mainLoop();
    }
}
