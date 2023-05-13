package com.kinghouser.eyecontroller.eyetracker;

import net.minecraft.client.MinecraftClient;

public class EyeTrackerUtils {

    private static boolean hasAligned = false;

    public static void updateCamera(int y, int x, int height, int width) {
        MinecraftClient minecraftClient = MinecraftClient.getInstance();

        if (minecraftClient.player == null || minecraftClient.isPaused()) return;
        int pitch = convertToMinecraftPitch(y, height);
        int yaw = convertToMinecraftYaw(x, width);
        // System.out.println(pitch + ", " + yaw);
        float currentPitch = minecraftClient.player.getPitch();
        float currentYaw = minecraftClient.player.getYaw();
        double distance = getDistance((int) currentPitch, (int) currentYaw, pitch, yaw);
        // System.out.println(distance);
        if (hasAligned && distance > 50) return;

        minecraftClient.player.setPitch(pitch);
        minecraftClient.player.setYaw(yaw);
        hasAligned = true;
        // minecraftClient.player.changeLookDirection(convertToMinecraftPitch(y, height) - currentPitch, convertToMinecraftYaw(x, width) - currentYaw);
    }

    private static double getDistance(int pitch, int yaw, int newPitch, int newYaw) {
        int dP = newPitch - pitch;
        int dY = newYaw - yaw;
        return Math.sqrt((dP * dP) + (dY * dY));
    }

    private static int convertToMinecraftPitch(int y, int height) {
        double mappedValue = ((double) y / height) * 180.0 - 90.0;
        return (int) mappedValue;
    }

    private static int convertToMinecraftYaw(int x, int width) {
        return (int) ((double) x  * (360.0 / (double) width));
    }
}