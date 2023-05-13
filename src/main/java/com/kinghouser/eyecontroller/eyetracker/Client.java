package com.kinghouser.eyecontroller.eyetracker;

import java.io.*;
import java.net.Socket;

public class Client extends Thread {

    public void run() {
        try {
            Socket socket = new Socket("localhost", 54970);
            System.out.println("Connected to server");

            InputStream input = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            OutputStream output = socket.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(output));

            while (socket.isConnected()) {
                String message = reader.readLine();
                if (message != null) {
                    System.out.println("Received message from server: " + message);
                    String[] data = message.split(",");
                    EyeTrackerUtils.updateCamera(Integer.parseInt(data[0]), Integer.parseInt(data[1]), Integer.parseInt(data[2]), Integer.parseInt(data[3]));
                }
            }
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}