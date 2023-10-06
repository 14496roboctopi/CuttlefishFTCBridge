package com.roboctopi.cuttlefishftcbridge.opmodeTypes;
// Most basic iterative Opmode with no gamepad or robot initialization

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

public abstract class RoboctopiOpmode extends LinearOpMode
{
    long start = System.nanoTime();
    public long runtime = 0;
    long lastFrame = 0;
    public int frameDelay=1*1000*1000;
    protected long dT = 0;
    long realLastFrame = 0;
    @Override
    public void runOpMode() throws InterruptedException
    {
        onInit();
        waitForStart();
        main();
        start=System.nanoTime();

        while (true)
        {
            runtime = System.nanoTime()-start;

            if(runtime-lastFrame >frameDelay )
            {
                //lastFrame = 2*runtime-lastFrame-frameDelay;
                if(!opModeIsActive())
                {
                    System.out.println("ENDENDENDENDENDENDENDENDENDENDENDENDENDENDENDENDENDENDENDENDENDEND");
                    onEnd();
                    return;
                }
                dT = runtime - realLastFrame;
                realLastFrame = runtime;
                lastFrame = lastFrame+frameDelay;
                mainLoop();
            }
            Thread.yield();
        }
    }

    abstract public void onInit();
    abstract public void main();
    abstract public void mainLoop();
    public void onEnd(){};
}
