package com.roboctopi.cuttlefishftcbridge.utils;

public class FTCTimer {
   long startT = 0;
   long endT = 0;
   public void start()
   {
      this.startT = System.nanoTime();
   }
   public void end()
   {
      this.endT = System.nanoTime();
   }
   public double getTime()
   {
      return (double)(this.endT-this.startT)/(1000*1000);
   }
}
