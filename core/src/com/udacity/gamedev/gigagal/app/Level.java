package com.udacity.gamedev.gigagal.app;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.udacity.gamedev.gigagal.entities.Ammo;
import com.udacity.gamedev.gigagal.entities.Cannon;
import com.udacity.gamedev.gigagal.entities.DestructibleHazard;
import com.udacity.gamedev.gigagal.entities.Ground;
import com.udacity.gamedev.gigagal.entities.Hazard;
import com.udacity.gamedev.gigagal.entities.Impact;
import com.udacity.gamedev.gigagal.entities.HoverableGround;
import com.udacity.gamedev.gigagal.entities.Orben;
import com.udacity.gamedev.gigagal.entities.Portal;
import com.udacity.gamedev.gigagal.entities.GigaGal;
import com.udacity.gamedev.gigagal.entities.Powerup;
import com.udacity.gamedev.gigagal.screens.LevelScreen;
import com.udacity.gamedev.gigagal.util.ChaseCam;
import com.udacity.gamedev.gigagal.util.Constants;
import com.udacity.gamedev.gigagal.util.Enums;
import com.udacity.gamedev.gigagal.util.Enums.Direction;
import com.udacity.gamedev.gigagal.util.Timer;
import com.udacity.gamedev.gigagal.util.Helpers;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.ListIterator;

// mutable
public class Level {

    // fields
    public static final String TAG = Level.class.getName();
    private static final Level INSTANCE = new Level();
    private Viewport viewport;
    private boolean loadEx;
    private boolean runEx;
    private float cannonOffset;
    private long cannonStartTime;
    private Portal portal;
    private List<Hazard> hazards;
    private List<Ground> grounds;
    private List<Impact> impacts;
    private List<Powerup> powerups;
    private Enums.WeaponType levelWeapon;
    private Enums.LevelName level;

    private boolean paused;
    private long pauseTime;
    private float pauseDuration;

    private int score;
    private long time;

    // cannot be subclassed
    private Level() {}

    // static factory method
    public static Level getInstance() { return INSTANCE; }

    public void create() {
        LevelScreen.getInstance().create();
        Timer.getInstance().create();
        viewport = new ExtendViewport(Constants.WORLD_SIZE, Constants.WORLD_SIZE);
        grounds = new ArrayList<Ground>();
        hazards = new ArrayList<Hazard>();
        impacts = new ArrayList<Impact>();
        powerups = new ArrayList<Powerup>();
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

    public void update(float delta) {
        if (continuing() && !paused()) {
            GigaGal.getInstance().update(delta);
            try {
                updateAssets(delta);
            } catch (ConcurrentModificationException ex) {
                runEx = true;
            }
        }
    }

    public void render(SpriteBatch batch) {

        viewport.apply();

        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();

        for (Ground ground : grounds) {
            ground.render(batch);
        }

        portal.render(batch);

        for (Powerup powerup : powerups) {
            powerup.render(batch);
        }

        for (Hazard hazard : hazards) {
            hazard.render(batch);
        }

        GigaGal.getInstance().render(batch);

        for (Impact impact : impacts) {
            impact.render(batch);
        }

        batch.end();
    }

    // level state handling

    public void begin() {

        levelWeapon = Enums.WeaponType.NATIVE;
        for (Enums.WeaponType weapon : Arrays.asList(Enums.WeaponType.values())) {
            if (weapon.levelName().equals(level)) {
                levelWeapon = weapon;
            }
        }

        GigaGal.getInstance().setLives(3);
        GigaGal.getInstance().respawn();

        // set level attributes
        ChaseCam.getInstance().camera = viewport.getCamera();
        ChaseCam.getInstance().target = GigaGal.getInstance();

        Timer.getInstance().reset().start();
    }

    public void end() {
        Timer.getInstance().suspend();
        if (completed()) {
            GigaGal.getInstance().addWeapon(levelWeapon);
            Energraft.getInstance().getPreferences().putInteger("Score", Energraft.getInstance().getScore() + score);
            Energraft.getInstance().getPreferences().putLong("Time", Energraft.getInstance().getTime() + Timer.getInstance().getSeconds());
            Energraft.getInstance().getPreferences().putString("Weapons", levelWeapon.name());
            Energraft.getInstance().getPreferences().flush();
        }
        removeAssets();
    }

    public void pause() {
        Timer.getInstance().suspend();

        pauseTime = TimeUtils.nanoTime();
        pauseDuration = GigaGal.getInstance().getPauseTimeSeconds();
        paused = true;
    }

    public void unpause() {
        GigaGal.getInstance().setPauseTimeSeconds(Helpers.secondsSincePause(pauseTime) + pauseDuration);
        paused = false;

        Timer.getInstance().resume();
    }

    public boolean restarted() {
        if (GigaGal.getInstance().getKillPlane() != -10000) {
            if (GigaGal.getInstance().getPosition().y < GigaGal.getInstance().getKillPlane() || GigaGal.getInstance().getHealth() < 1) {
                GigaGal.getInstance().setHealth(0);
                GigaGal.getInstance().setLives(GigaGal.getInstance().getLives() - 1);
                return true;
            }
        }
        return false;
    }

    public boolean failed() {
        if (restarted()) {
            if (GigaGal.getInstance().getLives() < 0) {
                return true;
            }
            GigaGal.getInstance().respawn();
        }
        return false;
    }

    public boolean completed() { return (GigaGal.getInstance().getPosition().dst(portal.getPosition()) < Constants.PORTAL_RADIUS); }


    public boolean continuing() { return !(completed() || failed()); }


    public boolean paused() {
        return paused;
    }


    // asset handling

    public void updateAssets(float delta) {
        // Update Grounds
        ListIterator<Ground> groundsIterator = grounds.listIterator();
        while (groundsIterator.hasNext()) {
            Ground ground = groundsIterator.next();
            if (ground instanceof HoverableGround) {
                HoverableGround hoverable = (HoverableGround) ground;
                hoverable.update(delta);
            }
            if (ground instanceof Cannon) {
                Cannon cannon = (Cannon) ground;
                if (cannon.getOffset() == 0) {
                    cannonOffset += 0.25f;
                    cannon.setOffset(cannonOffset);
                    cannon.setStartTime(TimeUtils.nanoTime() + ((long) (cannon.getOffset() / MathUtils.nanoToSec)));
                }
                if ((Helpers.secondsSince(cannon.getStartTime())  > 1.5f)) {
                    cannon.setStartTime(TimeUtils.nanoTime());
                    Enums.Orientation orientation = cannon.getOrientation();
                    if (orientation == Enums.Orientation.X) {
                        Vector2 ammoPositionLeft = new Vector2(cannon.getPosition().x - (cannon.getWidth() / 2), ground.getPosition().y);
                        Vector2 ammoPositionRight = new Vector2(cannon.getPosition().x + (cannon.getWidth() / 2), ground.getPosition().y);
                        if (GigaGal.getInstance().getPosition().x < (ammoPositionLeft.x - (cannon.getWidth() / 2))) {
                            spawnAmmo(ammoPositionLeft, Direction.LEFT, orientation, cannon.getIntensity(), levelWeapon, false);
                        } else if (GigaGal.getInstance().getPosition().x > (ammoPositionRight.x + (cannon.getWidth() / 2))) {
                            spawnAmmo(ammoPositionRight, Direction.RIGHT, orientation, cannon.getIntensity(), levelWeapon, false);
                        }
                    } else if (cannon.getOrientation() == Enums.Orientation.Y) {
                        Vector2 ammoPositionTop = new Vector2(ground.getPosition().x, cannon.getPosition().y + (cannon.getHeight() / 2));
                        Vector2 ammoPositionBottom = new Vector2(ground.getPosition().x, cannon.getPosition().y - (cannon.getHeight() / 2));
                        if (GigaGal.getInstance().getPosition().y < (ammoPositionBottom.y - (cannon.getHeight() / 2))) {
                            spawnAmmo(ammoPositionBottom, Direction.DOWN, orientation, cannon.getIntensity(), levelWeapon, false);
                        } else if (GigaGal.getInstance().getPosition().y > (ammoPositionTop.y + (cannon.getHeight() / 2))) {
                            spawnAmmo(ammoPositionTop, Direction.UP, orientation, cannon.getIntensity(), levelWeapon, false);
                        }
                    }
                }
            }
        }

        // Update Hazards
        ListIterator<Hazard> hazardIterator = hazards.listIterator();
        while (hazardIterator.hasNext()) {
            Hazard hazard = hazardIterator.next();
            if (hazard instanceof DestructibleHazard) {
                DestructibleHazard destructible = (DestructibleHazard) hazard;
                destructible.update(delta);
                if (destructible.getHealth() < 1) {
                    spawnExplosion(destructible.getPosition(), destructible.getType());
                    hazardIterator.remove();
                    score += (destructible.getKillScore() * Constants.DIFFICULTY_MULTIPLIER[Energraft.getInstance().getDifficulty()]);
                }
                if (destructible instanceof Orben) {
                    Orben orben = (Orben) destructible;
                    Enums.WeaponType weaponType = orben.getType();
                    float secondsSinceModOne = Helpers.secondsSince(orben.getStartTime()) % 1;
                    if ((secondsSinceModOne >= 0 && secondsSinceModOne < 0.01f) && orben.isActive()) {
                        Vector2 ammoPositionLeft = new Vector2(orben.getPosition().x - (orben.getWidth() * 1.1f), destructible.getPosition().y);
                        Vector2 ammoPositionRight = new Vector2(orben.getPosition().x + (orben.getWidth() * 1.1f), destructible.getPosition().y);
                        Vector2 ammoPositionTop = new Vector2(destructible.getPosition().x, orben.getPosition().y + (orben.getHeight() * 1.1f));
                        Vector2 ammoPositionBottom = new Vector2(destructible.getPosition().x, orben.getPosition().y - (orben.getHeight() * 1.1f));

                        spawnAmmo(ammoPositionLeft, Direction.LEFT, Enums.Orientation.X, Enums.AmmoIntensity.BLAST, weaponType, false);
                        spawnAmmo(ammoPositionRight, Direction.RIGHT, Enums.Orientation.X, Enums.AmmoIntensity.BLAST, weaponType, false);
                        spawnAmmo(ammoPositionBottom, Direction.DOWN, Enums.Orientation.Y, Enums.AmmoIntensity.BLAST, weaponType, false);
                        spawnAmmo(ammoPositionTop, Direction.UP, Enums.Orientation.Y, Enums.AmmoIntensity.BLAST, weaponType, false);
                    }
                }
            } else if (hazard instanceof Ammo) {
                Ammo ammo = (Ammo) hazard;
                ammo.update(delta);
                if (!ammo.isActive()) {
                    hazardIterator.remove();
                }
            }
        }

        // Update Explosions
        ListIterator<Impact> impactIterator = impacts.listIterator();
        while (impactIterator.hasNext()){
            Impact impact = impactIterator.next();
            if (impact.isFinished()) {
                impactIterator.remove();
            }
        }

        ListIterator<Powerup> iterator = powerups.listIterator();
        while (iterator.hasNext()) {
            Powerup powerup = iterator.next();
            if (!powerup.isActive()) {
                iterator.remove();
            }
        }
    }

    public void removeAssets() {
        ListIterator<Ground> groundsIterator = grounds.listIterator();
        while (groundsIterator.hasNext()) {
            groundsIterator.next();
            groundsIterator.remove();
        }

        // Update Hazards
        ListIterator<Hazard> hazardIterator = hazards.listIterator();
        while (hazardIterator.hasNext()) {
            hazardIterator.next();
            hazardIterator.remove();
        }

        // Update Explosions
        ListIterator<Impact> impactIterator = impacts.listIterator();
        while (impactIterator.hasNext()){
            impactIterator.next();
            impactIterator.remove();
        }
    }


    public void spawnAmmo(Vector2 position, Direction direction, Enums.Orientation orientation, Enums.AmmoIntensity ammoIntensity, Enums.WeaponType weapon, boolean targetsEnemies) {
        hazards.add(new Ammo(this, position, direction, orientation, ammoIntensity, weapon, targetsEnemies));
    }

    public void spawnExplosion(Vector2 position, Enums.WeaponType type) {
        impacts.add(new Impact(position, type));
    }

    public void dispose() {
    }

    // Getters
    public final long getTime() { return Timer.getInstance().getSeconds(); }
    public final int getScore() { return score; }
    public final List<Hazard> getHazards() { return hazards; }
    public final List<Ground> getGrounds() { return grounds; }
    public final List<Impact> getImpacts() { return impacts; }
    public final List<Powerup> getPowerups() { return powerups; }
    public final Viewport getViewport() { return viewport; }
    public final Portal getPortal() { return portal; }
    public final GigaGal getGigaGal() { return GigaGal.getInstance(); }
    public final Enums.WeaponType getType() { return levelWeapon; }
    public final boolean hasLoadEx() { return loadEx; }
    public final boolean hasRunEx() { return runEx; }

    // Setters
    public void setLevel(Enums.LevelName selectedLevel) { level = selectedLevel; }
    public void setScore(int score) { this.score = score; }
    public final void setPortal(Portal portal) { this.portal = portal; }
    public final void setLoadEx(boolean state) { loadEx = state; }
}