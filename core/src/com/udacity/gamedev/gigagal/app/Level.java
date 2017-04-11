package com.udacity.gamedev.gigagal.app;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.DelayedRemovalArray;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.udacity.gamedev.gigagal.entities.Ammo;
import com.udacity.gamedev.gigagal.entities.AmmoPowerup;
import com.udacity.gamedev.gigagal.entities.Boss;
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
import com.udacity.gamedev.gigagal.util.Constants;
import com.udacity.gamedev.gigagal.util.Enums;
import com.udacity.gamedev.gigagal.util.Enums.Direction;
import com.udacity.gamedev.gigagal.util.Timer;
import com.udacity.gamedev.gigagal.util.Helpers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

// mutable
public class Level {

    // fields
    public static final String TAG = Level.class.getName();
    private static final Level INSTANCE = new Level();
    private Enums.LevelName levelName;
    private Viewport viewport;
    private boolean victory;
    private boolean gameOver;
    private boolean loadEx;
    private int levelScore;
    private float cannonOffset;
    private long cannonStartTime;
    private GigaGal gigaGal;
    private Portal portal;
    private List<Hazard> hazards;
    private List<Ground> grounds;
    private List<Impact> impacts;
    private List<Powerup> powerups;
    private Timer levelTime;
    private Enums.WeaponType levelWeapon;
    private int difficulty;

    private Boss boss;

    // cannot be subclassed
    private Level() {}

    // static factory method
    public static Level getInstance() { return INSTANCE; }

    public void create() {
        viewport = new ExtendViewport(Constants.WORLD_SIZE, Constants.WORLD_SIZE);
        gigaGal = new GigaGal(this, new Vector2(50, 50));
        boss = new Boss(this, new Vector2(300, 300));
        levelScore = 0;
        levelTime = new Timer();
        levelTime.start();
        grounds = new ArrayList<Ground>();
        hazards = new ArrayList<Hazard>();
        impacts = new ArrayList<Impact>();
        powerups = new ArrayList<Powerup>();
        portal = new Portal(new Vector2(200, 200));
        gameOver = false;
        victory = false;
        loadEx = false;
        cannonStartTime = TimeUtils.nanoTime();
        cannonOffset = 0;
    }

    public static Level debugLevel() {
        Level level = new Level();
        level.initializeDebugLevel();
        return level;
    }

    public void update(float delta) {

        if (gigaGal.getLives() < 0) {
            gameOver = true;
        } else if (gigaGal.getPosition().dst(portal.getPosition()) < Constants.PORTAL_RADIUS) {
            victory = true;
        }

        if (!gameOver && !victory) {

            gigaGal.update(delta);
            boss.update(delta);

            levelWeapon = Enums.WeaponType.NATIVE;
            for (Enums.WeaponType weapon : Arrays.asList(Enums.WeaponType.values())) {
                if (weapon.levelName().equals(levelName)) {
                    levelWeapon = weapon;
                }
            }

            // Update Grounds
            for (Ground ground : grounds) {
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
                            if (gigaGal.getPosition().x < (ammoPositionLeft.x - (cannon.getWidth() / 2))) {
                                spawnAmmo(ammoPositionLeft, Direction.LEFT, orientation, cannon.getIntensity(), levelWeapon, false);
                            } else if (gigaGal.getPosition().x > (ammoPositionRight.x + (cannon.getWidth() / 2))) {
                                spawnAmmo(ammoPositionRight, Direction.RIGHT, orientation, cannon.getIntensity(), levelWeapon, false);
                            }
                        } else if (cannon.getOrientation() == Enums.Orientation.Y) {
                            Vector2 ammoPositionTop = new Vector2(ground.getPosition().x, cannon.getPosition().y + (cannon.getHeight() / 2));
                            Vector2 ammoPositionBottom = new Vector2(ground.getPosition().x, cannon.getPosition().y - (cannon.getHeight() / 2));
                            if (gigaGal.getPosition().y < (ammoPositionBottom.y - (cannon.getHeight() / 2))) {
                                spawnAmmo(ammoPositionBottom, Direction.DOWN, orientation, cannon.getIntensity(), levelWeapon, false);
                            } else if (gigaGal.getPosition().y > (ammoPositionTop.y + (cannon.getHeight() / 2))) {
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
                        levelScore += (destructible.getKillScore() * Constants.DIFFICULTY_MULTIPLIER[difficulty]);
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

        gigaGal.render(batch);
        boss.render(batch);

        for (Impact impact : impacts) {
            impact.render(batch);
        }

        batch.end();
    }

    private void initializeDebugLevel() {

        gigaGal = new GigaGal(this, new Vector2(15, 40));

        portal = new Portal(new Vector2(150, 150));

        powerups.add(new AmmoPowerup(new Vector2(20, 110)));
    }

    public void spawnAmmo(Vector2 position, Direction direction, Enums.Orientation orientation, Enums.AmmoIntensity ammoIntensity, Enums.WeaponType weapon, boolean targetsEnemies) {
        hazards.add(new Ammo(this, position, direction, orientation, ammoIntensity, weapon, targetsEnemies));
    }

    public boolean gigaGalFailed() {
        if (gigaGal.getKillPlane() != -10000) {
            if (gigaGal.getPosition().y < gigaGal.getKillPlane() || gigaGal.getHealth() < 1) {
                gigaGal.setHealth(0);
                gigaGal.setLives(gigaGal.getLives() - 1);
                return true;
            }
        }
        return false;
    }

    public void spawnExplosion(Vector2 position, Enums.WeaponType type) {
        impacts.add(new Impact(position, type));
    }

    public void dispose() {
        boss.dispose();
        gigaGal.dispose();
        hazards = null;
        grounds = null;
        impacts = null;
        boss = null;
        gigaGal = null;
    }

    // Getters
    public final List<Hazard> getHazards() { return hazards; }
    public final List<Ground> getGrounds() { return grounds; }
    public final List<Impact> getImpacts() { return impacts; }
    public final List<Powerup> getPowerups() { return powerups; }
    public final Viewport getViewport() { return viewport; }
    public final Portal getPortal() { return portal; }
    public final GigaGal getGigaGal() { return gigaGal; }
    public final boolean isGameOver() { return gameOver; }
    public final boolean isVictory() { return victory; }
    public final Timer getLevelTime() { return levelTime; }
    public final int getLevelScore() { return levelScore; }
    public final int getDifficulty() { return difficulty; }
    public final Enums.WeaponType getType() { return levelWeapon; }
    public final boolean getLoadEx() { return loadEx; }

    // Setters
    public void setLevelScore(int levelScore) { this.levelScore = levelScore; }
    public final void setDifficulty(int difficulty) { this.difficulty = difficulty; }
    public final void setPortal(Portal portal) { this.portal = portal; }
    public final void setGigaGal(GigaGal gigaGal) { this.gigaGal = gigaGal; }
    public final void setBoss(Boss boss) { this.boss = boss; }
    public final void setLevelName(Enums.LevelName levelName) { this.levelName = levelName; }
    public final void setLoadEx(boolean state) { loadEx = state; }
}