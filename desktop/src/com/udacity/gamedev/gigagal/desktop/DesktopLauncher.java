package com.udacity.gamedev.gigagal.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.udacity.gamedev.gigagal.app.ScreenManager;

public class DesktopLauncher {
    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = 640;
        config.height = 480;
        new LwjglApplication(ScreenManager.getInstance(), config);
    }
}
