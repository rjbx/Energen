package com.udacity.gamedev.gigagal.app;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.DelayedRemovalArray;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.udacity.gamedev.gigagal.entity.Ammo;
import com.udacity.gamedev.gigagal.entity.BreakableBox;
import com.udacity.gamedev.gigagal.entity.Cannon;
import com.udacity.gamedev.gigagal.entity.Destructible;
import com.udacity.gamedev.gigagal.entity.Ground;
import com.udacity.gamedev.gigagal.entity.Hazard;
import com.udacity.gamedev.gigagal.entity.Impact;
import com.udacity.gamedev.gigagal.entity.Hoverable;
import com.udacity.gamedev.gigagal.entity.Orben;
import com.udacity.gamedev.gigagal.entity.Portal;
import com.udacity.gamedev.gigagal.entity.GigaGal;
import com.udacity.gamedev.gigagal.entity.Powerup;
import com.udacity.gamedev.gigagal.util.Constants;
import com.udacity.gamedev.gigagal.util.Enums;
import com.udacity.gamedev.gigagal.util.Enums.Direction;
import com.udacity.gamedev.gigagal.util.Timer;
import com.udacity.gamedev.gigagal.util.Helpers;
import java.util.Arrays;

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
    private Portal portal;
    private DelayedRemovalArray<Hazard> hazards;
    private DelayedRemovalArray<Ground> grounds;
    private DelayedRemovalArray<Impact> impacts;
    private DelayedRemovalArray<Powerup> powerups;
    private Enums.Material levelWeapon;
    private Enums.Theme level;

    private boolean paused;
    private long pauseTime;
    private float pauseDuration;

    private int score;
    private long time;

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
        portal = new Portal(new Vector2(200, 200));
        loadEx = false;
        runEx = false;
        cannonStartTime = TimeUtils.nanoTime();
        cannonOffset = 0;

        score = 0;
        time = 0;

        paused = false;
        pauseTime = 0;
        pauseDuration = 0;
    }

    protected void update(float delta) {

        if (continuing() && !paused()) {
            GigaGal.getInstance().update(delta);
            updateAssets(delta);
        }
    }

    protected void render(SpriteBatch batch, Viewport viewport) {

        for (Ground ground : grounds) {
            ground.render(batch, viewport);
        }

        portal.render(batch, viewport);

        for (Powerup powerup : powerups) {
            powerup.render(batch, viewport);
        }

        for (Hazard hazard : hazards) {
            hazard.render(batch, viewport);
        }

        GigaGal.getInstance().render(batch, viewport);

        for (Impact impact : impacts) {
            impact.render(batch, viewport);
        }
    }

    // asset handling

    private void updateAssets(float delta) {
        // Update Grounds
        grounds.begin();
        for (int i = 0; i < grounds.size ; i++) {
            if (grounds.get(i) instanceof Hoverable) {
                ((Hoverable) grounds.get(i)).update(delta);
            } else if (grounds.get(i) instanceof Cannon) {
                Cannon cannon = (Cannon) grounds.get(i);
                if (cannon.getOffset() == 0) {
                    cannonOffset += 0.25f;
                    cannon.setOffset(cannonOffset);
                    cannon.setStartTime(TimeUtils.nanoTime() + ((long) (cannon.getOffset() / MathUtils.nanoToSec)));
                }
                if ((Helpers.secondsSince(cannon.getStartTime()) > 1.5f)) {
                    cannon.setStartTime(TimeUtils.nanoTime());
                    Enums.Orientation orientation = cannon.getOrientation();
                    if (orientation == Enums.Orientation.X) {
                        Vector2 ammoPositionLeft = new Vector2(cannon.getPosition().x - (cannon.getWidth() / 2), cannon.getPosition().y);
                        Vector2 ammoPositionRight = new Vector2(cannon.getPosition().x + (cannon.getWidth() / 2), cannon.getPosition().y);
                        if (GigaGal.getInstance().getPosition().x < (ammoPositionLeft.x - (cannon.getWidth() / 2))) {
                            spawnAmmo(ammoPositionLeft, Direction.LEFT, orientation, cannon.getIntensity(), levelWeapon, false);
                        } else if (GigaGal.getInstance().getPosition().x > (ammoPositionRight.x + (cannon.getWidth() / 2))) {
                            spawnAmmo(ammoPositionRight, Direction.RIGHT, orientation, cannon.getIntensity(), levelWeapon, false);
                        }
                    } else if (cannon.getOrientation() == Enums.Orientation.Y) {
                        Vector2 ammoPositionTop = new Vector2(cannon.getPosition().x, cannon.getPosition().y + (cannon.getHeight() / 2));
                        Vector2 ammoPositionBottom = new Vector2(cannon.getPosition().x, cannon.getPosition().y - (cannon.getHeight() / 2));
                        if (GigaGal.getInstance().getPosition().y < (ammoPositionBottom.y - (cannon.getHeight() / 2))) {
                            spawnAmmo(ammoPositionBottom, Direction.DOWN, orientation, cannon.getIntensity(), levelWeapon, false);
                        } else if (GigaGal.getInstance().getPosition().y > (ammoPositionTop.y + (cannon.getHeight() / 2))) {
                            spawnAmmo(ammoPositionTop, Direction.UP, orientation, cannon.getIntensity(), levelWeapon, false);
                        }
                    }
                }
            } else if (grounds.get(i) instanceof BreakableBox) {
                BreakableBox box = (BreakableBox) grounds.get(i);
                if (!box.isActive()) {
                    grounds.removeIndex(i);
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
                    spawnExplosion(destructible.getPosition(), destructible.getType());
                    if (hazards.get(i) instanceof BreakableBox) {
                        BreakableBox box = (BreakableBox) hazards.get(i);
                        box.deactivate();
                        hazards.removeIndex(i);
                    } else {
                        hazards.removeIndex(i);
                        score += (destructible.getKillScore() * Constants.DIFFICULTY_MULTIPLIER[SaveData.getDifficulty()]);
                    }
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
                score += Constants.POWERUP_SCORE;
                powerups.removeIndex(i);
            }
        }
        powerups.end();
    }

    public void spawnAmmo(Vector2 position, Direction direction, Enums.Orientation orientation, Enums.ShotIntensity shotIntensity, Enums.Material weapon, boolean targetsEnemies) {
        hazards.add(new Ammo(this, position, direction, orientation, shotIntensity, weapon, targetsEnemies));
    }

    public void spawnExplosion(Vector2 position, Enums.Material type) {
        impacts.add(new Impact(position, type));
    }

    protected void dispose() {
        hazards.clear();
        grounds.clear();
        impacts.clear();
        powerups.clear();
    }


    // level state handling

    protected void begin() {

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

        Timer.getInstance().reset().start();
    }

    protected void end() {
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
        Timer.getInstance().suspend();

        pauseTime = TimeUtils.nanoTime();
        pauseDuration = GigaGal.getInstance().getPauseTimeSeconds();
        paused = true;
    }

    protected void unpause() {
        GigaGal.getInstance().setPauseTimeSeconds(Helpers.secondsSincePause(pauseTime) + pauseDuration);
        paused = false;

        Timer.getInstance().resume();
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
            GigaGal.getInstance().respawn();
        }
        return false;
    }

    protected boolean completed() { return (GigaGal.getInstance().getPosition().dst(portal.getPosition()) < Constants.PORTAL_RADIUS); }

    protected boolean continuing() { return !(completed() || failed()); }

    protected boolean paused() {
        return paused;
    }

    // Getters
    public final long getTime() { return Timer.getInstance().getSeconds(); }
    public final int getScore() { return score; }
    public final DelayedRemovalArray<Hazard> getHazards() { return hazards; }
    public final DelayedRemovalArray<Ground> getGrounds() { return grounds; }
    public final DelayedRemovalArray<Impact> getImpacts() { return impacts; }
    public final DelayedRemovalArray<Powerup> getPowerups() { return powerups; }
    public final Viewport getViewport() { return viewport; }
    public final Portal getPortal() { return portal; }
    public final GigaGal getGigaGal() { return GigaGal.getInstance(); }
    public final Enums.Material getType() { return levelWeapon; }
    public final boolean hasLoadEx() { return loadEx; }

    // Setters
    protected void setLevel(Enums.Theme selectedLevel) { level = selectedLevel; }
    protected final void setPortal(Portal portal) { this.portal = portal; }
    protected final void setLoadEx(boolean state) { loadEx = state; }
}