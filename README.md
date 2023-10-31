# CuttlefishFTCBridge
be extremely inconvenient to use as the config must be changed in the app and cannot be adjusted directly in code, in addition to several other issues. Next, there are often performance issues when using lots of sensors as each one has to poll the hub individually. Finally, there is a lot of functionality available through the LynxCommand API that is not exposed by the built-in device classes that can be extremely useful.
In order to combat these issues, we have created our own device system as an alternative to the stock device system that accesses features directly through LynxCommands.
This replaces classes like `DcMotor` with custom classes like `CuttleMotor`.

This allows devices to be retrieved directly by port number rather than config name, allows sensors to automatically use bulk data instead of direct polling increasing loop performance, and exposes much of the extended functionality not available in the stock device system.

This library also acts as a bridge between the main Cuttlefish library and the FTC SDK as all of the devices implement the Cuttlefish device interfaces.

# Documentation
See our [Documentation](https://14496roboctopi.github.io/Cuttlefish/)