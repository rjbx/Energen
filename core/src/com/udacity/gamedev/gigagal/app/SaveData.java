package com.udacity.gamedev.gigagal.app;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.udacity.gamedev.gigagal.util.Enums;

public final class SaveData {

    // cannot be subclassed
    private SaveData() {}

    private static Preferences getPreferences() { return Gdx.app.getPreferences("energraft-prefs"); }

    protected static void erase() { getPreferences().clear(); getPreferences().flush(); }

    public static boolean hasTouchscreen() { return getPreferences().getBoolean("Touchscreen", false); }
    public static String getWeapons() { return getPreferences().getString("Weapons", Enums.Material.NATIVE.name()); }
    public static int getDifficulty() { return getPreferences().getInteger("Difficulty", -1); }
    public static int getTotalScore() { return getPreferences().getInteger("Score", 0); }
    public static long getTotalTime() { return getPreferences().getLong("Time", 0); }
    public static float getTurboMultiplier() { return getPreferences().getFloat("TurboMultiplier", 1); }
    public static float getAmmoMultiplier() { return getPreferences().getFloat("AmmoMultiplier", 1); }
    public static float getHealthMultiplier() { return getPreferences().getFloat("HealthMultiplier", 1); }

    protected static void toggleTouchscreen(boolean touchscreen) { getPreferences().putBoolean("Touchscreen", touchscreen); getPreferences().flush(); }
    protected static void setWeapons(String weapons) { getPreferences().putString("Weapons", weapons); getPreferences().flush(); }
    protected static void setDifficulty(int difficulty) { getPreferences().putInteger("Difficulty", difficulty); getPreferences().flush(); }
    protected static void setTotalScore(int score) { getPreferences().putInteger("Score", score); getPreferences().flush(); }
    protected static void setTotalTime(long time) { getPreferences().putLong("Time", time); getPreferences().flush(); }
    protected static void setTurboMultiplier(float multiplier) { getPreferences().putFloat("TurboMultiplier", multiplier); getPreferences().flush(); }
    protected static void setAmmoMultiplier(float multiplier)  { getPreferences().putFloat("AmmoMultiplier", multiplier); getPreferences().flush(); }
    protected static void setHealthMultiplier(float multiplier) { getPreferences().putFloat("HealthMultiplier", multiplier); getPreferences().flush(); }
}
