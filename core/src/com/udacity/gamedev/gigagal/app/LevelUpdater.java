package com.udacity.gamedev.gigagal.app;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.DelayedRemovalArray;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.udacity.gamedev.gigagal.entity.Ammo;
import com.udacity.gamedev.gigagal.entity.Chamber;
import com.udacity.gamedev.gigagal.entity.Chargeable;
import com.udacity.gamedev.gigagal.entity.Destructible;
import com.udacity.gamedev.gigagal.entity.Ground;
import com.udacity.gamedev.gigagal.entity.Hazard;
import com.udacity.gamedev.gigagal.entity.Impact;
import com.udacity.gamedev.gigagal.entity.Nonstatic;
import com.udacity.gamedev.gigagal.entity.Orben;
import com.udacity.gamedev.gigagal.entity.Portal;
import com.udacity.gamedev.gigagal.entity.GigaGal;
import com.udacity.gamedev.gigagal.entity.Powerup;
import com.udacity.gamedev.gigagal.entity.Swoopa;
import com.udacity.gamedev.gigagal.entity.Teleport;
import com.udacity.gamedev.gigagal.entity.Transport;
import com.udacity.gamedev.gigagal.entity.Vines;
import com.udacity.gamedev.gigagal.overlay.Backdrop;
import com.udacity.gamedev.gigagal.util.Assets;
import com.udacity.gamedev.gigagal.util.Constants;
import com.udacity.gamedev.gigagal.util.Enums;
import com.udacity.gamedev.gigagal.util.Enums.Direction;
import com.udacity.gamedev.gigagal.util.InputControls;
import com.udacity.gamedev.gigagal.util.Timer;
import com.udacity.gamedev.gigagal.util.Helpers;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// mutable
public class LevelUpdater {

    // fields
    public static final String TAG = LevelUpdater.class.getName();
    private static final LevelUpdater INSTANCE = new LevelUpdater();
    private Viewport viewport;
    private boolean loadEx;
    private boolean runEx;
    private float cannonOffset;
    private long cannonStartTime;
    private Backdrop backdrop;
    private DelayedRemovalArray<Transport> transports;
    private DelayedRemovalArray<Hazard> hazards;
    private DelayedRemovalArray<Ground> grounds;
    private DelayedRemovalArray<Impact> impacts;
    private DelayedRemovalArray<Powerup> powerups;
    private Enums.Material levelWeapon;
    private Enums.Theme level;
    private Music music;
    private String removedHazards;
    private boolean paused;
    private boolean musicEnabled;
    private int score;
    private long time;
    private int savedScore;
    private long savedTime;

    // cannot be subclassed
    private LevelUpdater() {}

    // static factory method
    public static LevelUpdater getInstance() { return INSTANCE; }

    protected void create() {
        LevelScreen.getInstance().create();
        Timer.getInstance().create();
        grounds = new DelayedRemovalArray<Ground>();
        hazards = new DelayedRemovalArray<Hazard>();
        impacts = new DelayedRemovalArray<Impact>();
        powerups = new DelayedRemovalArray<Powerup>();
        transports = new DelayedRemovalArray<Transport>();
        loadEx = false;
        runEx = false;
        musicEnabled = false;
        cannonStartTime = TimeUtils.nanoTime();
        cannonOffset = 0;
        removedHazards = "-1";
        score = 0;
        time = 0;
        paused = false;
    }

    protected void update(float delta) {
        if (continuing() && !paused()) {
            updateAssets(delta);
            GigaGal.getInstance().update(delta);
        }
    }

    protected void render(SpriteBatch batch, Viewport viewport) {

        backdrop.render(batch, viewport, GigaGal.getInstance().getPosition(), Constants.BACKGROUND_CENTER, 1);

        for (Ground ground : grounds) {
            if (!ground.isDense()) {
                ground.render(batch, viewport);
            }
        }

        for (Transport transport : transports) {
            transport.render(batch, viewport);
        }

        for (Powerup powerup : powerups) {
            powerup.render(batch, viewport);
        }

        for (Ground ground : grounds) {
            if (ground instanceof Vines) {
                ground.render(batch, viewport);
            }
        }

        for (Hazard hazard : hazards) {
            hazard.render(batch, viewport);
        }

        for (Ground ground : grounds) {
            if (ground.isDense()) {
                ground.render(batch, viewport);
            }
        }

        GigaGal.getInstance().render(batch, viewport);

        for (Impact impact : impacts) {
            impact.render(batch, viewport);
        }
    }

    // asset handling
    private void updateAssets(float delta) {

        time = Timer.getInstance().getNanos();
        
        // Update Restore Points
        transports.begin();
        for (int i = 0; i < transports.size; i++) {
            Transport transport = (Transport) transports.get(i);
            if (GigaGal.getInstance().getPosition().dst(transports.get(i).getPosition()) < transport.getWidth() / 2 && InputControls.getInstance().upButtonPressed && InputControls.getInstance().jumpButtonJustPressed) {
                if (transport instanceof Portal) {
                    Assets.getInstance().getSoundAssets().life.play();
                    int level = Arrays.asList(Enums.Theme.values()).indexOf(this.level);
                    List<String> allRestores = Arrays.asList(SaveData.getLevelRestores().split(", "));
                    List<String> allTimes = Arrays.asList(SaveData.getLevelTimes().split(", "));
                    List<String> allScores = Arrays.asList(SaveData.getLevelScores().split(", "));
                    List<String> allRemovals = Arrays.asList(SaveData.getLevelRemovals().split(", "));
                    int restores = Integer.parseInt(allRestores.get(level));
                    if (restores == 0) {
                        allRestores.set(level, Integer.toString(i + 1));
                    } else if (restores != (i + 1)) {
                        allRestores.set(level, Integer.toString(3));
                    }
                    allTimes.set(level, Long.toString(time));
                    allScores.set(level, Integer.toString(score));
                    allRemovals.set(level, removedHazards);
                    SaveData.setLevelRestores(allRestores.toString().replace("[", "").replace("]", ""));
                    SaveData.setLevelTimes(allTimes.toString().replace("[", "").replace("]", ""));
                    SaveData.setLevelScores(allScores.toString().replace("[", "").replace("]", ""));
                    SaveData.setLevelRemovals(allRemovals.toString().replace("[", "").replace("]", ""));

                    SaveData.setTotalTime(Helpers.numStrToSum(allTimes));
                    SaveData.setTotalScore((int) Helpers.numStrToSum(allScores));

                    savedTime = time;
                    savedScore = score;
                } else if (transport instanceof Teleport) {
                    GigaGal.getInstance().getPosition().set(transport.getDestination());
                }
            }
        }
        transports.end();

        // Update Grounds
        grounds.begin();
        for (int i = 0; i < grounds.size ; i++) {
            Ground ground = grounds.get(i);
            if (ground instanceof Nonstatic) {
                ((Nonstatic) ground).update();
            } else if (ground instanceof Destructible) {
                if (((Destructible) ground).getHealth() < 1) {
                    Assets.getInstance().getSoundAssets().breakGround.play();
                    grounds.removeIndex(i);
                }
            } else if (ground instanceof Chargeable) {
                if (ground instanceof Chamber) {
                    Chamber chamber = (Chamber) ground;
                    if (!chamber.isActive() && chamber.wasCharged()) {
                        Assets.getInstance().getSoundAssets().upgrade.play();
                        dispenseUpgrade(chamber.getUpgrade());
                        chamber.uncharge();
                    }
                }
            }
        }
        grounds.end();

        // Update Hazards
        hazards.begin();
        for (int i = 0; i < hazards.size; i++) {
            if (hazards.get(i) instanceof Destructible) {
                Destructible destructible = (Destructible) hazards.get(i);
                destructible.update(delta);
                if (destructible.getHealth() < 1) {
                    if (destructible instanceof Swoopa) {
                        ((Swoopa) destructible).dispose();
                    }
                    spawnExplosion(destructible.getPosition(), destructible.getType());
                    hazards.removeIndex(i);
                    removedHazards += (";" + i); // ';' delimeter prevents conflict with higher level parse (for str containing all level removal lists)
                    score += (destructible.getKillScore() * Constants.DIFFICULTY_MULTIPLIER[SaveData.getDifficulty()]);
                }
                if (destructible instanceof Orben) {
                    Orben orben = (Orben) destructible;
                    Enums.Material material = orben.getType();
                    float secondsSinceModOne = Helpers.secondsSince(orben.getStartTime()) % 1;
                    if ((secondsSinceModOne >= 0 && secondsSinceModOne < 0.01f) && orben.isActive()) {
                        Vector2 ammoPositionLeft = new Vector2(orben.getPosition().x - (orben.getWidth() * 1.1f), destructible.getPosition().y);
                        Vector2 ammoPositionRight = new Vector2(orben.getPosition().x + (orben.getWidth() * 1.1f), destructible.getPosition().y);
                        Vector2 ammoPositionTop = new Vector2(destructible.getPosition().x, orben.getPosition().y + (orben.getHeight() * 1.1f));
                        Vector2 ammoPositionBottom = new Vector2(destructible.getPosition().x, orben.getPosition().y - (orben.getHeight() * 1.1f));

                        spawnAmmo(ammoPositionLeft, Direction.LEFT, Enums.Orientation.X, Enums.ShotIntensity.BLAST, material, false);
                        spawnAmmo(ammoPositionRight, Direction.RIGHT, Enums.Orientation.X, Enums.ShotIntensity.BLAST, material, false);
                        spawnAmmo(ammoPositionBottom, Direction.DOWN, Enums.Orientation.Y, Enums.ShotIntensity.BLAST, material, false);
                        spawnAmmo(ammoPositionTop, Direction.UP, Enums.Orientation.Y, Enums.ShotIntensity.BLAST, material, false);
                    }
                }
            } else if (hazards.get(i) instanceof Ammo) {
                Ammo ammo = (Ammo) hazards.get(i);
                ammo.update(delta);
                if (!ammo.isActive()) {
                    score += ammo.getHitScore();
                    hazards.removeIndex(i);
                }
            }
        }
        hazards.end();

        // Update Impacts
        impacts.begin();
        for (int i = 0; i < impacts.size; i++) {
            if (impacts.get(i).isFinished()) {
                impacts.removeIndex(i);
            }
        }
        impacts.end();

        // Update Powerups
        powerups.begin();
        for (int i = 0; i < powerups.size; i++) {
            if (!powerups.get(i).isActive()) {
                powerups.removeIndex(i);
            }
        }
        powerups.end();
    }

    private void dispenseUpgrade(Enums.Upgrade upgrade) {
        switch (upgrade) {
            case AMMO:
                SaveData.setAmmoMultiplier(.9f);
                GigaGal.getInstance().updateMultipliers();
                break;
            case HEALTH:
                SaveData.setHealthMultiplier(.8f);
                GigaGal.getInstance().updateMultipliers();
                break;
            case TURBO:
                SaveData.setTurboMultiplier(.7f);
                GigaGal.getInstance().updateMultipliers();
                break;
            case CANNON:
                String savedWeapons = SaveData.getWeapons();
                if (!savedWeapons.contains(Enums.Material.HYBRID.name())) {
                    GigaGal.getInstance().addWeapon(Enums.Material.HYBRID);
                    SaveData.setWeapons(Enums.Material.HYBRID.name() + ", " + savedWeapons);
                }
                break;
            default:
        }
        GigaGal.getInstance().setHealth(Constants.MAX_HEALTH);
    }

    protected void restoreRemovals(String removals) {
        removedHazards = removals;
        List<String> levelRemovalStrings = Arrays.asList(removedHazards.split(";"));
        List<Integer> levelRemovals = new ArrayList<Integer>();
        for (String removalStr : levelRemovalStrings) {
            levelRemovals.add(Integer.parseInt(removalStr));
        }

        for (Integer removal : levelRemovals) {
            if (removal != -1) {
                hazards.removeIndex(removal);
            }
        }
    }

    protected void dispose() {
        hazards.clear();
        grounds.clear();
        impacts.clear();
        powerups.clear();
        transports.clear();
    }


    // level state handling

    protected void begin() {
        backdrop = new Backdrop(Assets.getInstance().getBackgroundAssets().getBackground(level));
        music = Assets.getInstance().getMusicAssets().getThemeMusic(level);
        music.setLooping(true);
        if (musicEnabled) {
            music.play();
        }
        runEx = false;
        levelWeapon = Enums.Material.NATIVE;
        for (Enums.Material weapon : Arrays.asList(Enums.Material.values())) {
            if (weapon.theme().equals(level)) {
                levelWeapon = weapon;
            }
        }

        GigaGal.getInstance().setLives(3);
        GigaGal.getInstance().respawn();

        // set level attributes
        viewport = LevelScreen.getInstance().getViewport();

        Timer.getInstance().reset().start(time);

        savedTime = time;
        savedScore = score;
    }

    protected void end() {
        music.stop();
        Timer.getInstance().suspend();
        if (completed()) {
            SaveData.setTotalScore(SaveData.getTotalScore() + score);
            SaveData.setTotalTime(SaveData.getTotalTime() + Timer.getInstance().getSeconds());
            String savedWeapons = SaveData.getWeapons();
            if (!savedWeapons.contains(levelWeapon.name())) {
                GigaGal.getInstance().addWeapon(levelWeapon);
                SaveData.setWeapons(levelWeapon.name() + ", " + savedWeapons);
            }
        }
        dispose();
    }

    protected void pause() {
        music.pause();
        Timer.getInstance().suspend();
        paused = true;
    }

    protected void unpause() {
        if (musicEnabled) {
            music.play();
        }
        paused = false;
        Timer.getInstance().resume();
    }

    protected void reset() {
        int level = Arrays.asList(Enums.Theme.values()).indexOf(this.level);
        List<String> allRestores = Arrays.asList(SaveData.getLevelRestores().split(", "));
        List<String> allTimes = Arrays.asList(SaveData.getLevelTimes().split(", "));
        List<String> allScores = Arrays.asList(SaveData.getLevelScores().split(", "));
        List<String> allRemovals = Arrays.asList(SaveData.getLevelRemovals().split(", "));
        allTimes.set(level, "0");
        allScores.set(level, "0");
        allRestores.set(level, "0");
        allRemovals.set(level, "-1");
        SaveData.setLevelRestores(allRestores.toString().replace("[", "").replace("]", ""));
        SaveData.setLevelTimes(allTimes.toString().replace("[", "").replace("]", ""));
        SaveData.setLevelScores(allScores.toString().replace("[", "").replace("]", ""));
        SaveData.setLevelRemovals(allRemovals.toString().replace("[", "").replace("]", ""));
    }

    protected boolean restarted() {
        if (GigaGal.getInstance().getKillPlane() != -10000) {
            if (GigaGal.getInstance().getPosition().y < GigaGal.getInstance().getKillPlane() || GigaGal.getInstance().getHealth() < 1) {
                GigaGal.getInstance().setHealth(0);
                GigaGal.getInstance().setLives(GigaGal.getInstance().getLives() - 1);
                return true;
            }
        }
        return false;
    }

    protected boolean failed() {
        if (restarted()) {
            if (GigaGal.getInstance().getLives() < 0) {
                return true;
            }
            if (musicEnabled) {
                music.stop();
                music.play();
            }
            GigaGal.getInstance().respawn();
        }
        return false;
    }

    protected boolean completed() { return GigaGal.getInstance().getPosition().dst(transports.get(transports.size - 1).getPosition()) < Constants.PORTAL_RADIUS; }

    protected boolean continuing() { return !(completed() || failed()); }

    protected boolean paused() {
        return paused;
    }

    public void spawnAmmo(Vector2 position, Direction direction, Enums.Orientation orientation, Enums.ShotIntensity shotIntensity, Enums.Material weapon, boolean targetsEnemies) {
        hazards.add(new Ammo(this, position, direction, orientation, shotIntensity, weapon, targetsEnemies));
    }

    public void spawnExplosion(Vector2 position, Enums.Material type) {
        impacts.add(new Impact(position, type));
    }

    // Getters
    public final long getUnsavedTime() { return time - savedTime; }
    public final int getUnsavedScore() { return score - savedScore; }
    public final long getTime() { return time; }
    public final int getScore() { return score; }
    public final DelayedRemovalArray<Hazard> getHazards() { return hazards; }
    public final DelayedRemovalArray<Ground> getGrounds() { return grounds; }
    public final DelayedRemovalArray<Impact> getImpacts() { return impacts; }
    public final DelayedRemovalArray<Powerup> getPowerups() { return powerups; }
    public final Viewport getViewport() { return viewport; }
    public final DelayedRemovalArray<Transport> getTransports() { return transports; }
    public final GigaGal getGigaGal() { return GigaGal.getInstance(); }
    public final Enums.Material getType() { return levelWeapon; }
    protected Enums.Theme getLevel() { return level; }
    public final boolean hasLoadEx() { return loadEx; }

    // Setters
    protected void setTime(long time) { this.time = time; }
    protected void setScore(int score) {this.score = score; }
    protected void setLevel(Enums.Theme selectedLevel) { level = selectedLevel; }
    protected void toggleMusic() { musicEnabled = !musicEnabled; }
    protected final void setLoadEx(boolean state) { loadEx = state; }
}