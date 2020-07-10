package com.github.rjbx.energen.app;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.github.rjbx.energen.util.Enums;

public final class SaveData {

    public final static String TAG = SaveData.class.getName();
    private static Preferences preferences;

    // cannot be subclassed
    private SaveData() {}

    private static Preferences getPreferences() {
        if (preferences == null) {
            preferences = Gdx.app.getPreferences("avatar-prefs");
            SaveData.setTouchscreen(Gdx.app.getType() == Application.ApplicationType.Android || Gdx.app.getType() == Application.ApplicationType.iOS);
        }
        return preferences;
    }

    protected static void erase() { getPreferences().clear(); getPreferences().flush(); }

    public static boolean hasTouchscreen() { return getPreferences().getBoolean("Touchscreen", false); }
    public static boolean hasMusic() { return getPreferences().getBoolean("Music", true); }
    public static int getDifficulty() { return getPreferences().getInteger("Difficulty", -1); }
    public static int getTotalScore() { return getPreferences().getInteger("TotalScore", 0); }
    public static long getTotalTime() { return getPreferences().getLong("TotalTime", 0); }
    public static String getLevelScores() { return getPreferences().getString("LevelScores", "0, 0, 0, 0, 0, 0, 0, 0"); }
    public static String getLevelTimes() { return getPreferences().getString("LevelTimes", "0, 0, 0, 0, 0, 0, 0, 0"); }
    public static String getLevelRestore() { return getPreferences().getString("LevelRestores", "0:0, 0:0, 0:0, 0:0, 0:0, 0:0, 0:0, 0:0"); }
    public static String getLevelRemovals() { return getPreferences().getString("LevelRemovals", "-1, -1, -1, -1, -1, -1, -1, -1"); }
    public static String getEnergies() { return getPreferences().getString("Energies", Enums.Energy.NATIVE.name() + ", " + Enums.Energy.ORE + ", " + Enums.Energy.PLASMA + ", " + Enums.Energy.GAS + ", " + Enums.Energy.LIQUID + ", " + Enums.Energy.SOLID + ", " + Enums.Energy.ANTIMATTER + ", " + Enums.Energy.HYBRID); }
    public static String getUpgrades() { return getPreferences().getString("Upgrades", Enums.Upgrade.NONE.name()); }
    public static String getSuit() { return getPreferences().getString("Suit", Enums.Energy.NATIVE.name() + ";0"); }
    public static String getStyle() { return getPreferences().getString("Style", Enums.MusicStyle.CLASSIC.name()); }

    protected static void setTouchscreen(boolean touchscreen) { getPreferences().putBoolean("Touchscreen", touchscreen); getPreferences().flush(); }
    protected static void setMusic(boolean music) { getPreferences().putBoolean("Music", music); getPreferences().flush(); }
    protected static void setDifficulty(int difficulty) { getPreferences().putInteger("Difficulty", difficulty); getPreferences().flush(); }
    protected static void setTotalScore(int score) { getPreferences().putInteger("TotalScore", score); getPreferences().flush(); }
    protected static void setTotalTime(long time) { getPreferences().putLong("TotalTime", time); getPreferences().flush(); }
    protected static void setLevelScores(String scores) { getPreferences().putString("LevelScores", scores); getPreferences().flush(); }
    protected static void setLevelTimes(String times) { getPreferences().putString("LevelTimes", times); getPreferences().flush(); }
    protected static void setLevelRestore(String restores) { getPreferences().putString("LevelRestores", restores); getPreferences().flush(); }
    protected static void setLevelRemovals(String removals) { getPreferences().putString("LevelRemovals", removals); getPreferences().flush(); }
    protected static void setEnergies(String energies) { getPreferences().putString("Energies", energies); getPreferences().flush(); }
    protected static void setUpgrades(String upgrades) { getPreferences().putString("Upgrades", upgrades); getPreferences().flush(); }
    protected static void setSuit(String suit) { getPreferences().putString("Suit", suit); getPreferences().flush(); }
    protected static void setStyle(String style) { getPreferences().putString("Style", style); getPreferences().flush(); }
}
