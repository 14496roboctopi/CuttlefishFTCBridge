package com.roboctopi.cuttlefishftcbridge.opmodeTypes;

public abstract class GamepadOpMode extends CuttlefishOpMode {
    boolean a1Down = false;
    boolean b1Down = false;
    boolean x1Down = false;
    boolean y1Down = false;

    boolean rBumper1Down = false;
    boolean lBumper1Down  = false;

    boolean dpadU1Down = false;
    boolean dpadL1Down = false;
    boolean dpadD1Down = false;
    boolean dpadR1Down = false;

    boolean rTrigger1Down = false;
    boolean lTrigger1Down = false;




    boolean a2Down = false;
    boolean b2Down = false;
    boolean x2Down = false;
    boolean y2Down = false;

    boolean rBumper2Down = false;
    boolean lBumper2Down  = false;

    boolean dpadU2Down = false;
    boolean dpadL2Down = false;
    boolean dpadD2Down = false;
    boolean dpadR2Down = false;


    boolean lStick1U = false;
    boolean lStick1L = false;
    boolean lStick1D = false;
    boolean lStick1R = false;

    boolean rStick1U = false;
    boolean rStick1L = false;
    boolean rStick1D = false;
    boolean rStick1R = false;



    boolean lStick2U = false;
    boolean lStick2L = false;
    boolean lStick2D = false;
    boolean lStick2R = false;

    boolean rStick2U = false;
    boolean rStick2L = false;
    boolean rStick2D = false;
    boolean rStick2R = false;

    boolean rTrigger2Down = false;
    boolean lTrigger2Down = false;
    public void mainLoop()
    {

        //Gamepad 1 abxy
        if(gamepad1.a&&!a1Down){a1Down = true; onA1Down();}
        if(gamepad1.b&&!b1Down){b1Down = true; onB1Down();}
        if(gamepad1.x&&!x1Down){x1Down = true; onX1Down();}
        if(gamepad1.y&&!y1Down){y1Down = true; onY1Down();}

        if(!gamepad1.a&&a1Down){a1Down = false;}
        if(!gamepad1.b&&b1Down){b1Down = false;}
        if(!gamepad1.x&&x1Down){x1Down = false;}
        if(!gamepad1.y&&y1Down){y1Down = false;}

        //Gamepad 1 bumpers
        if(gamepad1.right_bumper&&!rBumper1Down){rBumper1Down = true; onRBumper1Down();}
        if(gamepad1.left_bumper &&!lBumper1Down){lBumper1Down = true; onLBumper1Down();}
        if(!gamepad1.right_bumper&&rBumper1Down){rBumper1Down = false;onRBumper1Up();}
        if(!gamepad1.left_bumper &&lBumper1Down){lBumper1Down = false;onLBumper1Up();}

        //Gamepad 1 dpad
        if(gamepad1.dpad_up   &&!dpadU1Down){dpadU1Down = true;onDPadU1Down();}
        if(gamepad1.dpad_left &&!dpadL1Down){dpadL1Down = true;onDPadL1Down();}
        if(gamepad1.dpad_down &&!dpadD1Down){dpadD1Down = true;onDPadD1Down();}
        if(gamepad1.dpad_right&&!dpadR1Down){dpadR1Down = true;onDPadR1Down();}
        if(!gamepad1.dpad_up   &&dpadU1Down){dpadU1Down = false;}
        if(!gamepad1.dpad_left &&dpadL1Down){dpadL1Down = false;}
        if(!gamepad1.dpad_down &&dpadD1Down){dpadD1Down = false;}
        if(!gamepad1.dpad_right&&dpadR1Down){dpadR1Down = false;}

        //Gamepad 1 left stick
        if(gamepad1.left_stick_y < -0.5 &&!lStick1U){lStick1U = true;onLStick1U();}
        else if(gamepad1.left_stick_y >= -0.5){lStick1U = false;}
        if(gamepad1.left_stick_x < -0.5 &&!lStick1L){lStick1L = true;onLStick1L();}
        else if(gamepad1.left_stick_x >= -0.5){lStick1L = false;}
        if(gamepad1.left_stick_y >  0.5 &&!lStick1D){lStick1D = true;onLStick1D();}
        else if(gamepad1.left_stick_y <=  0.5){lStick1D = false;}
        if(gamepad1.left_stick_x >  0.5 &&!lStick1R){lStick1R = true;onLStick1R();}
        else if(gamepad1.left_stick_x <=  0.5){lStick1R = false;}

        //Gamepad 1 right stick
        if(gamepad1.right_stick_y < -0.5 &&!rStick1U){rStick1U = true;onRStick1U();}
        else if(gamepad1.right_stick_y >= -0.5){rStick1U = false;}
        if(gamepad1.right_stick_x < -0.5 &&!rStick1L){rStick1L = true;onRStick1L();}
        else if(gamepad1.right_stick_x >= -0.5){rStick1L = false;}
        if(gamepad1.right_stick_y >  0.5 &&!rStick1D){rStick1D = true;onRStick1D();}
        else if(gamepad1.right_stick_y <=  0.5){rStick1D = false;}
        if(gamepad1.right_stick_x >  0.5 &&!rStick1R){rStick1R = true;onRStick1R();}
        else if(gamepad1.right_stick_x <=  0.5){rStick1R = false;}

        //Gamepad 1 Triggers
        if(gamepad1.right_trigger > 0.25 && !rTrigger1Down){rTrigger1Down = true; onRTrigger1Down();}
        if(gamepad1.left_trigger  > 0.25 && !lTrigger1Down){lTrigger1Down = true; onLTrigger1Down();}
        if(gamepad1.right_trigger <= 0.25 && rTrigger1Down){rTrigger1Down = false;}
        if(gamepad1.left_trigger  <= 0.25 && lTrigger1Down){lTrigger1Down = false;}



        //Gamepad 2 abxy
        if(gamepad2.a&&!a2Down){a2Down = true; onA2Down();}
        if(gamepad2.b&&!b2Down){b2Down = true; onB2Down();}
        if(gamepad2.x&&!x2Down){x2Down = true; onX2Down();}
        if(gamepad2.y&&!y2Down){y2Down = true; onY2Down();}

        if(!gamepad2.a&&a2Down){a2Down = false; onA2Up();}
        if(!gamepad2.b&&b2Down){b2Down = false; onB2Up();}
        if(!gamepad2.x&&x2Down){x2Down = false; onX2Up();}
        if(!gamepad2.y&&y2Down){y2Down = false; onY2Up();}

        //Gamepad 2 bumpers
        if(gamepad2.right_bumper&&!rBumper2Down){rBumper2Down = true; onRBumper2Down();}
        if(gamepad2.left_bumper &&!lBumper2Down){lBumper2Down = true; onLBumper2Down();}
        if(!gamepad2.right_bumper&&rBumper2Down){rBumper2Down = false;onRBumper2Up();}
        if(!gamepad2.left_bumper &&lBumper2Down){lBumper2Down = false;onLBumper2Up();}

        //Gamepad 2 dpad
        if(gamepad2.dpad_up   &&!dpadU2Down){dpadU2Down = true;onDPadU2Down();}
        if(gamepad2.dpad_left &&!dpadL2Down){dpadL2Down = true;onDPadL2Down();}
        if(gamepad2.dpad_down &&!dpadD2Down){dpadD2Down = true;onDPadD2Down();}
        if(gamepad2.dpad_right&&!dpadR2Down){dpadR2Down = true;onDPadR2Down();}
        if(!gamepad2.dpad_up   &&dpadU2Down){dpadU2Down = false;onDPadU2Up();}
        if(!gamepad2.dpad_left &&dpadL2Down){dpadL2Down = false;onDPadL2Up();}
        if(!gamepad2.dpad_down &&dpadD2Down){dpadD2Down = false;onDPadD2Up();}
        if(!gamepad2.dpad_right&&dpadR2Down){dpadR2Down = false;onDPadR2Up();}

        //Gamepad 2 left stick
        if(gamepad2.left_stick_y < -0.5 &&!lStick2U){lStick2U = true;onLStick2U();}
        else if(gamepad2.left_stick_y >= -0.5){lStick2U = false;}
        if(gamepad2.left_stick_x < -0.5 &&!lStick2L){lStick2L = true;onLStick2L();}
        else if(gamepad2.left_stick_x >= -0.5){lStick2L = false;}
        if(gamepad2.left_stick_y >  0.5 &&!lStick2D){lStick2D = true;onLStick2D();}
        else if(gamepad2.left_stick_y <=  0.5){lStick2D = false;}
        if(gamepad2.left_stick_x >  0.5 &&!lStick2R){lStick2R = true;onLStick2R();}
        else if(gamepad2.left_stick_x <=  0.5){lStick2R = false;}

        //Gamepad 2 right stick
        if(gamepad2.right_stick_y < -0.5 &&!rStick2U){rStick2U = true;onRStick2U();}
        else if(gamepad2.right_stick_y >= -0.5){rStick2U = false;}
        if(gamepad2.right_stick_x < -0.5 &&!rStick2L){rStick2L = true;onRStick2L();}
        else if(gamepad2.right_stick_x >= -0.5){rStick2L = false;}
        if(gamepad2.right_stick_y >  0.5 &&!rStick2D){rStick2D = true;onRStick2D();}
        else if(gamepad2.right_stick_y <=  0.5){rStick2D = false;}
        if(gamepad2.right_stick_x >  0.5 &&!rStick2R){rStick2R = true;onRStick2R();}
        else if(gamepad2.right_stick_x <=  0.5){rStick2R = false;}

        //Gamepad 2 triggers
        if(gamepad2.right_trigger > 0.25 && !rTrigger2Down){rTrigger2Down = true; onRTrigger2Down();}
        if(gamepad2.left_trigger  > 0.25 && !lTrigger2Down){lTrigger2Down = true; onLTrigger2Down();}

        if(gamepad2.right_trigger <= 0.25 && rTrigger2Down){rTrigger2Down = false;onRTrigger2Up();}
        if(gamepad2.left_trigger  <= 0.25 && lTrigger2Down){lTrigger2Down = false;onLTrigger2Up();}
    }

    public void rumble1(float power, int durationMs)
    {
        gamepad1.rumble(power, power, durationMs);
    }
    public void rumble1(float powerLeft, float powerRight, int durationMs)
    {
        gamepad1.rumble(powerLeft, powerRight, durationMs);
    }
    public void rumble2(float power, int durationMs)
    {
        gamepad2.rumble(power, power, durationMs);
    }
    public void rumble2(float powerLeft, float powerRight, int durationMs)
    {
        gamepad2.rumble(powerLeft, powerRight, durationMs);
    }

    public void onA1Down(){}
    public void onB1Down(){}
    public void onX1Down(){}
    public void onY1Down(){}

    public void onA1Up(){}
    public void onB1Up(){}
    public void onX1Up(){}
    public void onY1Up(){}


    public void onRBumper1Down(){}
    public void onLBumper1Down(){}

    public void onRBumper1Up(){}
    public void onLBumper1Up(){}


    public void onDPadU1Down(){}
    public void onDPadL1Down(){}
    public void onDPadD1Down(){}
    public void onDPadR1Down(){}

    public void onDPadU1Up(){}
    public void onDPadL1Up(){}
    public void onDPadD1Up(){}
    public void onDPadR1Up(){}


    public void onRTrigger1Down(){}
    public void onLTrigger1Down(){}

    public void onRTrigger1Up(){}
    public void onLTrigger1Up(){}




    public void onA2Down(){}
    public void onB2Down(){}
    public void onX2Down(){}
    public void onY2Down(){}

    public void onA2Up(){}
    public void onB2Up(){}
    public void onX2Up(){}
    public void onY2Up(){}



    public void onRBumper2Down(){}
    public void onLBumper2Down(){}

    public void onRBumper2Up(){}
    public void onLBumper2Up(){}


    public void onDPadU2Down(){}
    public void onDPadL2Down(){}
    public void onDPadD2Down(){}
    public void onDPadR2Down(){}

    public void onDPadU2Up(){}
    public void onDPadL2Up(){}
    public void onDPadD2Up(){}
    public void onDPadR2Up(){}

    public void onRTrigger2Down(){}
    public void onLTrigger2Down(){}
    public void onRTrigger2Up(){}
    public void onLTrigger2Up(){}



    public void onLStick1U(){}
    public void onLStick1L(){}
    public void onLStick1D(){}
    public void onLStick1R(){}

    public void onRStick1U(){}
    public void onRStick1L(){}
    public void onRStick1D(){}
    public void onRStick1R(){}




    public void onLStick2U(){}
    public void onLStick2L(){}
    public void onLStick2D(){}
    public void onLStick2R(){}

    public void onRStick2U(){}
    public void onRStick2L(){}
    public void onRStick2D(){}
    public void onRStick2R(){}
}
