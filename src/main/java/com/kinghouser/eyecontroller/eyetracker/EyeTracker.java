package com.kinghouser.eyecontroller.eyetracker;

public class EyeTracker {

    public static void setup() {
        // System.setProperty("java.awt.headless", "false");
        Client client = new Client();
        client.start();
    }
}