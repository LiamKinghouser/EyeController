package com.kinghouser.eyecontroller;

import com.kinghouser.eyecontroller.eyetracker.EyeTracker;
import net.fabricmc.api.ModInitializer;

public class EyeController implements ModInitializer {

    public static String MOD_ID = "eyecontroller";

    @Override
    public void onInitialize() {
        EyeTracker.setup();
    }
}