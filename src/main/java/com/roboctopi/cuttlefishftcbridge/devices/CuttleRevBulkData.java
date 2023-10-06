package com.roboctopi.cuttlefishftcbridge.devices;
import com.qualcomm.hardware.lynx.commands.core.LynxGetBulkInputDataResponse;

public class CuttleRevBulkData {
    // The point of this class is to format the response from the hubs and get it for usage
    // Nvm it doesnt need to, but this class is staying anyway for now
    LynxGetBulkInputDataResponse response;
    public boolean success = false;
    public CuttleRevBulkData() {}

    public void updateData(LynxGetBulkInputDataResponse response)
    {
        if (response == null)
        {
            this.success = false;
        }
        else {
            this.response = response;
            this.success = true;
        }
    }


    public CuttleRevBulkData(boolean success)
    {
        this.success = success;
    }

    /**
     * Get value of an Analog Input port
     * @param port Hardware Port Number
     * @return Analog input in mV
     */
    public int getAnalogInput(int port)
    {
        return response.getAnalogInput(port);
    }

    /**
     * Get value of a Digital Input port
     * @param port Hardware Port
     * @return Value of the port (probably 0 or 1)
     */
    public boolean getDigitalInput(int port)
    {
        return response.getDigitalInput(port);
    }

    /**
     * Get the position of an encoder
     * @param port Hardware Port
     * @return Encoder position in encoder counts
     */
    public int getEncoderPosition(int port)
    {
        return response.getEncoder(port);
    }

    /**
     *
     * @param port Encoder Hardware port
     * @return Returns encoder velocity in  encoder counts/second
     */
    public int getEncoderVelocity(int port)
    {
        return response.getVelocity(port);
    }
}
