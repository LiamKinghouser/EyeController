package com.kinghouser.eyecontroller.eyetracker;

public class EyeTracker {

    public static void setup() {
        // System.setProperty("java.awt.headless", "false");
        EyeTrackerThread eyeTrackerThread = new EyeTrackerThread();
        eyeTrackerThread.start();
    }
}