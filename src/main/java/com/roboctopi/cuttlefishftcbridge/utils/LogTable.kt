package com.roboctopi.cuttlefishftcbridge.utils

import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.util.*

class LogTable(val filename:String,val opmode:String,val columns:Int, private val writeFrequency:Int) {
    private var buffer = Array<String>(columns) { i -> "" };
    private var counter:Int = 0;
    var path:String = genPath(opmode);

    fun setCol(index:Int,data:String)
    {
        if(index>=0&&index<columns)
        {
            buffer.set(index,data);
        }
    }
    var tempDataBuffer = "";
    fun write()
    {
        for(entry in buffer)
        {
            tempDataBuffer += entry+",";
        }
        tempDataBuffer = tempDataBuffer.dropLast(1);
        tempDataBuffer+="\n";

        counter++;
        counter %= writeFrequency;

        if(counter == 0)
        {
            tempDataBuffer = tempDataBuffer.dropLast(1);
            val logFile = File("/sdcard/codelogs/logs"+path)
            if (!logFile.exists()) {
                try {
                    logFile.parentFile.mkdirs();
                    logFile.createNewFile()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
            try {
                val buf = BufferedWriter(FileWriter(logFile, true))
                buf.append(tempDataBuffer)
                buf.newLine()
                buf.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            tempDataBuffer = "";
        }
    }

    fun genPath(opmode: String): String {
        return "/" +opmode+ "/" + "log-" + (Math.floor(System.currentTimeMillis()/(1000.0)).toInt()).toString() + "-"+filename
    }
}
