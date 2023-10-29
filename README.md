# Module CuttlefishFTCBridge

### Overview
Throughout our FTC journey, we have noticed certain shortcomings in the stock rev robotics device system. First, the config system can be extremely inconvenient to use as the config must be changed in the app and cannot be adjusted directly in code, in addition to several other issues. Next, there are often performance issues when using lots of sensors as each one has to poll the hub individually. Finally, there is a lot of functionality available through the LynxCommand API that is not exposed by the built-in device classes that can be extremely useful.
In order to combat these issues, we have created our own device system as an alternative to the stock device system that accesses features directly through LynxCommands.
This replaces classes like `DcMotor` with custom classes like `CuttleMotor`.

This allows devices to be retrieved directly by port number rather than config name, allows sensors to automatically use bulk data instead of direct polling increasing loop performance, and exposes much of the extended functionality not available in the stock device system.

This library also acts as a bridge between the main Cuttlefish library and the FTC SDK as all of the devices implement the Cuttlefish device interfaces.

## Installation

### Basic

### Advanced
Here is how to add CuttlefishFTCBridge to your project directly as a repository. This is useful if you are planning to modify the library yourself

Clone CuttlefishFTCBridge into the top-level folder of your project. This can be done with the following command in git bash:
```bash
git clone https://github.com/14496roboctopi/CuttlefishFTCBridge
```
If you have forked the library then you can use the URL to your library instead. Note: Copy and pasting into the Windows git bash is sometimes buggy, meaning that if you copy and paste this into the Windows git bash it might throw an error if you don't retype certain parts of it.

Another option instead of cloning the library directly is to add it as a submodule. This is useful if you are using git for your project as it tells git to retrieve the project from a separate repository rather than including it in your main repository. It can be added as a submodule using the following command:
```bash
git submodule add https://github.com/14496roboctopi/CuttlefishFTCBridge
```
DO NOT RUN THIS COMMAND IN ADDITION TO THE FIRST COMMAND. 
If you choose to go with this option then you will need to push and pull the submodule separately from the rest of your git. You will also need to run the commands git submodule init and git submodule update whenever you set up a new copy of the repo on a different computer in order to pull the submodule into your project.

Next, look for your project-level build.gradle file. It can be found under gradle scripts and should say (Project: the_name_of_your_project) in parentheses after build.gradle. In this file in the dependencies block, add the following line:
```groovy
classpath 'org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.10'
```
This tells Gradle to include the Kotlin plugin in your project.

Now open settings.gradle, which can also be found under gradle scripts, and add the following line:
```groovy
include ':CuttlefishFTCBridge'
```
This tells Gradle that the CuttlefishFTCBridge folder is a module in your project.

Finally, locate the TeamCode build.gradle file. This is the build.gradle file that says (Moduke: TeamCode) in parentheses. In the dependencies block of this file, add the following line:
```groovy
implementation project(path: ':CuttlefishFTCBridge')
```
This adds CuttlefishFTCBridge as a dependency of your teamcode module, allowing it to be used in teamcode.
After making changes to the Gradle scripts, you click `sync now` in the top right of your code window.


### CuttleRevHub
The basis of the Cuttlefish device system is the CuttleRevHub object. It is an object that provides all extended functionality having to do with the rev hub (expansion or control), such as adjusting LED color and measuring battery voltage, and it is used to obtain other devices, such as motors and servos. If you are using two hubs they will both have to be obtained separately. The hubs can be obtained as follows:
```java
CuttleRevHub controlHub = new CuttleRevHub(hardwareMap,CuttleRevHub.HubTypes.CONTROL_HUB);
CuttleRevHub expansionHub = new CuttleRevHub(hardwareMap,CuttleRevHub.HubTypes.EXPANSION_HUB);
```
If that doesn't work, you can also obtain hubs directly by name. The name of each hub can be found in the robot config file. Here is an example of retrieving a hub by name:
```java
CuttleRevHub exHub = new CuttleRevHub(hardwareMap,"Expansion Hub 2");
```
Make sure to define the hub in init, as when the constructor is called it will get the control hub using hardwareMap.
A detailed description of available features can be found in the Cuttlefish <a href="/CuttlefishFTCBridge/com.roboctopi.cuttlefishftcbridge.devices/-cuttle-rev-hub/index.html">reference documentation</a>. 

### Obtaining Devices
Once a CuttleRevHub has been obtained it can be used to obtain devices. This can be done simply as follows:
```java
CuttleMotor motor = controlHub.getMotor(1 /*Motor Port Number*/ );
CuttleEncoder encoder = hub.getEncoder(3 /*Encoder Motor Port Number*/, 512 /*Counter Per Revolution*/ );
CuttleServo servo = hub.getServo(2 /*Servo Port Number*/) ;
CuttleAnalog analog_sensor = hub.getAnalog(2 /*Analog Port Number*/ );
CuttleDigital digital_sensor = hub.getDigital(3 /*Digital Port Number*/ );
```
***If you are using any sensors obtained from the hub, you must call the pullBulkData function of the hub every loop cycle in order for the sensors to function.*** This is because the sensors automatically use cached bulk data from the hub, meaning you must tell the hub to get new bulk data each cycle, or the devices won't update.
These devices can be used like their stock counterparts. For a detailed description of their functionality, see their <a href="/CuttlefishFTCBridge/com.roboctopi.cuttlefishftcbridge.devices/index.html">reference documentation</a>.

<h2 id = "initialized-opmode">Initialized OpMode</h2>

The purpose of the built-in device config is to allow you to change the port that a device is plugged into without having to update every opmode that uses that device. This is desirable as manually changing every opmode every time you change hardware is bad. Since this library negates the built-in config system, it is a good idea to put something else in place to allow for devices to be initialized the same across all opmodes. There are different ways that you can do this, but we recommend creating an "initialized opmode". This is an abstract class that initializes everything on your robot that you can extend instead of extending the default `OpMode` or `LinearOpMode` classes. This can be created in the same manner as a normal opmode would be created, except that `@TeleOp` or `@Autonomous` is omitted, and that it is declared as an abstract class instead of a normal class. We also recommend that in your initialized opmode, you extend `GamepadOpmode` or `CuttlefishOpMode`, which are similar to the default iterative opmode except that it internally uses its own while loop as we have noticed intermittent performance problems in the iterative opmode loop. `GamepadOpMode` has built-in functions that are called when buttons on the gamepad are pressed or released, which is useful for TeleOp programming. Here is a basic example of an Initialized OpMode:
```java
public abstract class InitializedOpmode extends GamepadOpMode {
    public CuttleRevHub ctrlHub;
    public CuttleRevHub expHub;

    public CuttleMotor leftFrontMotor ;
    public CuttleMotor rightFrontMotor;
    public CuttleMotor rightBackMotor ;
    public CuttleMotor leftBackMotor  ;
    MecanumController chassis;

    @Override
    public void onInit(){
        //Runs when the OpMode is initialized
        ctrlHub = new CuttleRevHub(hardwareMap,CuttleRevHub.HubTypes.CONTROL_HUB);
        expHub = new CuttleRevHub(hardwareMap,"Expansion Hub 2");

        leftFrontMotor  = ctrlHub.getMotor(3);
        rightFrontMotor = ctrlHub.getMotor(2);
        rightBackMotor  = expHub.getMotor(2);
        leftBackMotor   = expHub.getMotor(3);

        leftBackMotor.setDirection(Direction.REVERSE);
        leftFrontMotor.setDirection(Direction.REVERSE);
        chassis = new MecanumController(rightFrontMotor,rightBackMotor,leftFrontMotor,leftBackMotor);
    }
    @Override
    public void main() {
        //Runs when the OpMode is started
    }
    public void mainLoop()
    {
        //Runs repeatedly after the OpMode is started
        ctrlHub.pullBulkData();
        expHub.pullBulkData();
    }
}
```
Here is how you would create a basic driver opmode using that initialized opmode:
```java
@TeleOp(name="Driver Opmode", group="Example")
public abstract class DriverOpMode extends InitializedOpmode {
    @Override
    public void onInit()
    {
        super.onInit();
        // Put OpMode specific init code here
    }

    @Override
    public void main() {
        super.main();
        // Put OpMode specific main code here
    }

    public void mainLoop(){
        super.mainLoop();
        chassis.setVec(new Pose(gamepad1.left_stick_x,-gamepad1.left_stick_y,-gamepad1.right_stick_x));
    }
}
```
If you don't have any opmode specific init or main code, those functions can be ommited:
```java
@TeleOp(name="Driver Opmode", group="Example")
public abstract class DriverOpMode extends InitializedOpmode {
    public void mainLoop(){
        super.mainLoop();
        chassis.setVec(new Pose(gamepad1.left_stick_x,-gamepad1.left_stick_y,-gamepad1.right_stick_x));
    }
}
```

***Make sure to run super.{insert function name here}() at the top of the onInit, main, and mainLoop functions in opmodes implementing your Initialized Opmode***. If you don't do this the code from your initialized opmode will not be executed.

