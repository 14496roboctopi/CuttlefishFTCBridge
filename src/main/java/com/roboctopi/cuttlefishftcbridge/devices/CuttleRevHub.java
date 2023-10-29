package com.roboctopi.cuttlefishftcbridge.devices;
import android.graphics.Color;

import com.qualcomm.hardware.lynx.LynxCommExceptionHandler;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.hardware.lynx.LynxNackException;
import com.qualcomm.hardware.lynx.commands.LynxCommand;
import com.qualcomm.hardware.lynx.commands.LynxMessage;
import com.qualcomm.hardware.lynx.commands.LynxRespondable;

import com.qualcomm.hardware.lynx.commands.core.LynxGetADCCommand;
import com.qualcomm.hardware.lynx.commands.core.LynxGetADCResponse;
import com.qualcomm.hardware.lynx.commands.core.LynxI2cConfigureChannelCommand;
import com.qualcomm.hardware.lynx.commands.core.LynxSetMotorChannelModeCommand;
import com.qualcomm.hardware.lynx.commands.core.LynxSetMotorConstantPowerCommand;

import com.qualcomm.hardware.lynx.commands.core.LynxDekaInterfaceCommand;
import com.qualcomm.hardware.lynx.commands.core.LynxGetBulkInputDataCommand;
import com.qualcomm.hardware.lynx.commands.core.LynxGetBulkInputDataResponse;
import com.qualcomm.hardware.lynx.commands.core.LynxSetServoConfigurationCommand;
import com.qualcomm.hardware.lynx.commands.core.LynxSetServoEnableCommand;
import com.qualcomm.hardware.lynx.commands.core.LynxSetServoPulseWidthCommand;
import com.qualcomm.hardware.lynx.commands.standard.LynxSetModuleLEDColorCommand;
import com.qualcomm.hardware.lynx.commands.standard.LynxSetModuleLEDPatternCommand;
import com.qualcomm.hardware.lynx.commands.standard.LynxStandardCommand;
import com.qualcomm.robotcore.hardware.Blinker;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PwmControl;
import com.qualcomm.robotcore.hardware.configuration.LynxConstants;
import com.qualcomm.robotcore.hardware.usb.RobotArmingStateNotifier;
import com.qualcomm.robotcore.util.Range;

import java.util.concurrent.TimeUnit;

/**
 * Object that represents a Rev Robotics Control or Expansion hub. This is the basis of the Cuttlefish device architecture.
 * */
public class CuttleRevHub  extends LynxCommExceptionHandler implements RobotArmingStateNotifier.Callback {

   boolean armed = false;

   long last_bulk_pull_time_ns = System.nanoTime();
   @Override
   public synchronized void onModuleStateChange(RobotArmingStateNotifier module, RobotArmingStateNotifier.ARMINGSTATE state) {
      armed = module.getArmingState() == RobotArmingStateNotifier.ARMINGSTATE.ARMED;
   }

   public enum HubTypes
   {
      CONTROL_HUB,
      EXPANSION_HUB
   }

   /**
    * <strong>I2C Channel Speeds</strong>
    *<br>
    * Standard:   up to 100 Kb/s<br>
    * Fast:       up to 400 Kb/s<br>
    * Fast Plus:  up to 1 Mb/s<br>
    * High Speed: up to 3.4 Mb/s<br><br>
    *
    */
   public enum I2CSpeed
   {
      STANDARD,
      FAST,
      FAST_PLUS,
      HIGH_SPEED
   }

   public LynxModule revHub;

   public CuttleRevBulkData bulkData;

   /**
    * Create Rev hub object using the name of the hub (this can be found in the robot config).
    * @param hardwareMap The opmode's hardwareMap
    * @param hubName "Control Hub" or "Expansion Hub"
    */
   public CuttleRevHub(HardwareMap hardwareMap,String hubName)
   {
      revHub = hardwareMap.get(LynxModule.class,hubName);
      revHub.registerCallback(this,true);
      while(!armed)
      {
         try {
            Thread.sleep(20);
         } catch (InterruptedException e) {
            e.printStackTrace();
         }
      }
      bulkData = new CuttleRevBulkData();
      pullBulkData();


   }
   /**
    * Create Rev hub object using using HubTypes enum. If this doesn't work get the hub by name.
    * @param hardwareMap The opmode's hardwareMap
    * @param hubType HubTypes enum automatically sets name to "Control Hub" or "Expansion Hub". If this doesn't work check config and set the name manually.
    */
   public CuttleRevHub(HardwareMap hardwareMap,HubTypes hubType)
   {
      switch(hubType)
      {
         case CONTROL_HUB:
            revHub = hardwareMap.get(LynxModule.class,"Control Hub");
            break;
         case EXPANSION_HUB:
            revHub = hardwareMap.get(LynxModule.class,"Expansion Hub");
            break;
      }
      bulkData = new CuttleRevBulkData();
      pullBulkData();
   }

   /**
    * Set the speed of all I2C channels. Specify channel as second argument to only set one I2C channel
    * @param speed I2C Bus Speed - For more info hover over I2CSpeed
    */
   public void setI2CBusSpeed(I2CSpeed speed)
   {
      for (int i = 0; i < LynxConstants.NUMBER_OF_I2C_BUSSES; i++)
      {
         setI2CBusSpeed(speed, i);
      }
   }

   /**
    * Set speed of a specific I2C channel. Remove the 2nd argument to set the speed of all I2C channels
    * @param speed I2C Bus Speed - For more info hover over I2CSpeed
    * @param bus I2C Channel to set
    */
   public void setI2CBusSpeed(I2CSpeed speed, int bus)
   {
      LynxI2cConfigureChannelCommand.SpeedCode speedCode = null;

      switch (speed)
      {
         case STANDARD:
            speedCode = LynxI2cConfigureChannelCommand.SpeedCode.STANDARD_100K;
            break;
         case FAST:
            speedCode = LynxI2cConfigureChannelCommand.SpeedCode.FAST_400K;
            break;
         case FAST_PLUS:
            speedCode = LynxI2cConfigureChannelCommand.SpeedCode.FASTPLUS_1M;
            break;
         case HIGH_SPEED:
            speedCode = LynxI2cConfigureChannelCommand.SpeedCode.HIGH_3_4M;
            break;
      }

      if (speedCode == null) {
         System.out.println("[CUTTLE FTC EXTENSION WELPED] - Failed to set I2C hub speed: speed not specified in function call");
         return;
      }

      LynxI2cConfigureChannelCommand cmd = new LynxI2cConfigureChannelCommand(revHub, bus, speedCode);
      sendCommand(cmd);
   }

   /**
    * Return a motor with port and hub set
    * @param port The port of the motor
    */
   public CuttleMotor getMotor(int port)
   {
      return new CuttleMotor(this,port);
   }

   /**
    * Return a digital sensor with port and hub set
    * @param port The digital port that the sensor is on
    * @return Digital Sensor
    * */
   public CuttleDigital getDigital(int port)
   {
      return new CuttleDigital(this,port);
   }


   /**
    * Return a analog sensor with port and hub set
    * @param port The analog port that the sensor is on
    * */
   public CuttleAnalog getAnalog(int port) {return new CuttleAnalog(this,port);}

   /**
    * Return an encoder with port counts, and hub set
    * @param port The motor port of the encoder
    * @param counts Number of encoder counts per revolution
    */
   public CuttleEncoder getEncoder(int port,double counts) {return new CuttleEncoder(this,port,counts);}

   /**
    * Return a configured analog encoder
    * @param port The analog port that the encoder is connected to
    * @param offset The offset of the analog port
    * @param voltage Voltage of the sensor at its positive extreme. This is probably the supply voltage of the sensor.
    * */
   public CuttleAnalogEncoder getAnalogEncoder(int port,double offset, double voltage) {return new CuttleAnalogEncoder(this,port,offset,voltage);}

   /**
    * Set the zero power behaviour of the motor
    * @param port Port of the motor
    * @param behavior Zero power behaviour of the motor
    */
   public void setMotorZeroPowerBehaviour(int port, DcMotor.ZeroPowerBehavior behavior)
   {
      LynxConstants.validateMotorZ(port);
      LynxCommand command = new LynxSetMotorChannelModeCommand(this.revHub,port, DcMotor.RunMode.RUN_WITHOUT_ENCODER,behavior);
      sendCommand(command);
   }


   /**
    * Set the power of a motor
    * @param port Port of the motor
    * @param power The power of the motor ranging from -1.0 to 1.0
    */
   public void setMotorPower(int port,double power)
   {
      LynxConstants.validateMotorZ(port);
      int powerSet = (int)(Range.scale(Range.clip(power,-1.0,1.0), -1.0, 1.0, LynxSetMotorConstantPowerCommand.apiPowerFirst, LynxSetMotorConstantPowerCommand.apiPowerLast));
      LynxSetMotorConstantPowerCommand powerCommand = new LynxSetMotorConstantPowerCommand(revHub,port,powerSet);

      sendCommand(powerCommand);
   }

   /**
    * Returns the current being drawn by a motor
    * @param port Port of the motor
    * @return Returns motor current in milliamps
    */
   public int getMotorCurrent(int port)
   {
      LynxGetADCCommand cmd = new LynxGetADCCommand(this.revHub,LynxGetADCCommand.Channel.motorCurrent(port),LynxGetADCCommand.Mode.ENGINEERING);

      LynxGetADCResponse rsp = (LynxGetADCResponse) sendCommandReceiveSync(cmd);
      return rsp.getValue();
   }

   private static final int firstServo = LynxConstants.INITIAL_SERVO_PORT;
   private static final int lastServo = firstServo + LynxConstants.NUMBER_OF_SERVO_CHANNELS -1;
   /**
    * Set the position of a servo
    * @param port Port of the servo
    * @param position The position of the servo ranging from 0.0 to 1.0
    */
   public void setServoPosition(int port,double position)
   {
      double pwm = Range.clip(position, 0.0,1.0);
      if(port< LynxConstants.INITIAL_SERVO_PORT||port>lastServo)
      {
         throw new IllegalArgumentException(String.format("Servo %d is invalid; valid servos are %d..%d", port, firstServo, lastServo));
      }
      pwm = Range.scale(pwm,0,1, PwmControl.PwmRange.usPulseLowerDefault, PwmControl.PwmRange.usPulseUpperDefault);

      LynxSetServoPulseWidthCommand cmd = new LynxSetServoPulseWidthCommand(this.revHub,port,(int)pwm);

      sendCommand(cmd);
   }
   public void setServoPWM(int port)
   {
      if(port< LynxConstants.INITIAL_SERVO_PORT||port>lastServo)
      {
         throw new IllegalArgumentException(String.format("Servo %d is invalid; valid servos are %d..%d", port, firstServo, lastServo));
      }
      LynxSetServoConfigurationCommand cmd = new LynxSetServoConfigurationCommand(this.revHub,port,(int)PwmControl.PwmRange.usFrameDefault);
      sendCommand(cmd);
   }

   /**
    * Enable/disable the signal being sent to a servo
    * @param port Port of the servo
    * @param enable True to enable the servo and false to disable
    */
   public void enableServoPWM(int port,boolean enable)
   {
      LynxSetServoEnableCommand command = new LynxSetServoEnableCommand(this.revHub, port, enable);
      sendCommand(command);
   }

   /**
    * Return a configured servo by port
    * @param port Port of the servo
    */
   public CuttleServo getServo(int port)
   {
      setServoPWM(port);
      return new CuttleServo(this,port);
   }


   /**
    * Get the current being drawn by the hub
    * WARNING: This will poll the hub costing an extra 3ms
    * @return Hub current in milliamps
    */
   public int getBatteryCurrent()
   {
      LynxGetADCCommand cmd = new LynxGetADCCommand(this.revHub,LynxGetADCCommand.Channel.BATTERY_CURRENT,LynxGetADCCommand.Mode.ENGINEERING);

      LynxGetADCResponse rsp = (LynxGetADCResponse) sendCommandReceiveSync(cmd);
      return rsp.getValue();
   }

//   public void setMotorPower(int motor,double power)
//   {
//
//      LynxConstants.validateMotorZ(motor);
//      int powerSet = (int)(Range.scale(power, -1.0, 1.0, LynxSetMotorConstantPowerCommand.apiPowerFirst, LynxSetMotorConstantPowerCommand.apiPowerLast));
//      LynxSetMotorConstantPowerCommand powerCommand = new LynxSetMotorConstantPowerCommand(revHub,motor,powerSet);
//      try {
//         powerCommand.sendReceive();
//      } catch (InterruptedException e) {
//         e.printStackTrace();
//      } catch (LynxNackException e) {
//         e.printStackTrace();
//      }
//
//   }

   /**
    * Set the color of the LED on the hub
    * @param r Red channel
    * @param r Green channel
    * @param r Blue channel
    */
   public void setLedColor(byte r, byte g, byte b)
   {
      LynxSetModuleLEDColorCommand cmd = new LynxSetModuleLEDColorCommand(revHub, r, g, b);

      sendCommand(cmd);
   }

   /**
    * Set the blink pattern of the LED on the hub to a rainbow
    */
   public void rainbow()
   {
      LynxSetModuleLEDPatternCommand.Steps blinkerSteps = new LynxSetModuleLEDPatternCommand.Steps();

      blinkerSteps.add(new Blinker.Step((0 | (0 << 8)) | (255 << 16), 500/6, TimeUnit.MILLISECONDS));
      blinkerSteps.add(new Blinker.Step((0 | (255 << 8)) | (255 << 16), 500/6, TimeUnit.MILLISECONDS));
      blinkerSteps.add(new Blinker.Step((0 | (255 << 8)) | (0 << 16), 500/6, TimeUnit.MILLISECONDS));
      blinkerSteps.add(new Blinker.Step((255 | (255 << 8)) | (0 << 16), 500/6, TimeUnit.MILLISECONDS));
      blinkerSteps.add(new Blinker.Step((255 | (0 << 8)) | (0 << 16), 500/6, TimeUnit.MILLISECONDS));
      blinkerSteps.add(new Blinker.Step((255 | (0 << 8)) | (255 << 16), 500/6, TimeUnit.MILLISECONDS));

      LynxSetModuleLEDPatternCommand cmd = new LynxSetModuleLEDPatternCommand(revHub, blinkerSteps);

      sendCommand(cmd);
   }

   /**
    * Set the blink pattern of the LED to a 16-step rainbow
    */
   public void smoothRainbow()
   {
      LynxSetModuleLEDPatternCommand.Steps blinkerSteps = new LynxSetModuleLEDPatternCommand.Steps();


      for(int i = 0; i < 16; i++)
      {
         float[] hsv = {(i*360)/16,1,1};
         blinkerSteps.add(new Blinker.Step(Color.HSVToColor(hsv), 1000/16, TimeUnit.MILLISECONDS));
      }

//      blinkerSteps.add(new Blinker.Step(getCLRInt(255,0,255,255), 50, TimeUnit.MILLISECONDS));



      LynxSetModuleLEDPatternCommand cmd = new LynxSetModuleLEDPatternCommand(revHub, blinkerSteps);

      sendCommand(cmd);
   }
   /**
    * Make the LED on the hub blink a certain color
    * @param r Red channel
    * @param g Green channel
    * @param b Blue channel
    */
   public void blinkColor(int r, int g, int b)
   {
      LynxSetModuleLEDPatternCommand.Steps blinkerSteps = new LynxSetModuleLEDPatternCommand.Steps();


      blinkerSteps.add(new Blinker.Step(Color.rgb(r,g,b), 250, TimeUnit.MILLISECONDS));
      blinkerSteps.add(new Blinker.Step(Color.rgb(0,0,0), 250, TimeUnit.MILLISECONDS));

      LynxSetModuleLEDPatternCommand cmd = new LynxSetModuleLEDPatternCommand(revHub, blinkerSteps);

      sendCommand(cmd);
   }


   /**
    * Pull bulk data from the hub. This should be called every loop cycle if any non - i2c sensors are in use.
    * <br>
    */
   public void pullBulkData() {
      LynxGetBulkInputDataCommand cmd = new LynxGetBulkInputDataCommand(revHub);

      // TODO revHub.sendCommand(cmd);

      bulkData.updateData((LynxGetBulkInputDataResponse) sendCommandReceiveSync(cmd));
      //System.out.println(revHub.getDeviceName());

//      for (int i = 0; i < 4; i++)
//      {
//         System.out.println("Port " + i);
//         System.out.println(bulkData.getAnalogInput(i));
//      }
   }

   /**
    * Get the voltage of the battery
    * WARNING: This will poll the hub costing an extra 3ms
    * @return Battery voltage in millivolts
    * */
   public int getBatteryVoltage()
   {
      LynxGetADCCommand cmd = new LynxGetADCCommand(revHub, LynxGetADCCommand.Channel.BATTERY_MONITOR, LynxGetADCCommand.Mode.ENGINEERING);

      LynxGetADCResponse response = (LynxGetADCResponse) sendCommandReceiveSync(cmd);

      return response.getValue();
   }

   /**
    * Send command to the hub (If response needed, use sendCommandReceiveSync)
    * @param message The command and params
    * @return Returns true on success
    */
   public boolean sendCommand(LynxRespondable message)
   {
      try
      {
         message.send();
         return true;
      } catch (LynxNackException e) {
         e.printStackTrace();
      } catch (InterruptedException e) {
         e.printStackTrace();
      }

      return false;
   }

   /**
    * Send command to the hub and returns response (If no response needed, use sendCommand)
    * @param message The command and params
    * @return Response object. Will return null if sending/receiving message failed. Error will print in console
    */
   public LynxMessage sendCommandReceiveSync(LynxDekaInterfaceCommand message)
   {
      try
      {
         return message.sendReceive();
      } catch (LynxNackException e) {
         e.printStackTrace();
      } catch (InterruptedException e) {
         e.printStackTrace();
      }

      return null;
   }

   /**
    * Send command to the hub and returns response (If no response needed, use sendCommand)
    * @param message The command and params
    * @return Response object. Will return null if sending/receiving message failed. Error will print in console
    */
   public LynxMessage sendCommandReceiveSync(LynxStandardCommand message)
   {
      try
      {
         return message.sendReceive();
      } catch (LynxNackException e) {
         e.printStackTrace();
      } catch (InterruptedException e) {
         e.printStackTrace();
      }

      return null;
   }
}
