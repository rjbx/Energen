package com.udacity.gamedev.gigagal;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.DelayedRemovalArray;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.udacity.gamedev.gigagal.entities.Ammo;
import com.udacity.gamedev.gigagal.entities.AmmoPowerup;
import com.udacity.gamedev.gigagal.entities.Cannon;
import com.udacity.gamedev.gigagal.entities.DestructibleHazard;
import com.udacity.gamedev.gigagal.entities.Ground;
import com.udacity.gamedev.gigagal.entities.Hazard;
import com.udacity.gamedev.gigagal.entities.IndestructibleHazard;
import com.udacity.gamedev.gigagal.entities.HoverableGround;
import com.udacity.gamedev.gigagal.entities.Orben;
import com.udacity.gamedev.gigagal.entities.Zoomba;
import com.udacity.gamedev.gigagal.entities.Portal;
import com.udacity.gamedev.gigagal.entities.Explosion;
import com.udacity.gamedev.gigagal.entities.GigaGal;
import com.udacity.gamedev.gigagal.entities.BoxGround;
import com.udacity.gamedev.gigagal.entities.Powerup;
import com.udacity.gamedev.gigagal.util.Constants;
import com.udacity.gamedev.gigagal.util.Enums;
import com.udacity.gamedev.gigagal.util.Enums.Direction;
import com.udacity.gamedev.gigagal.util.Utils;

import org.apache.commons.lang3.time.StopWatch;

import java.util.Arrays;

// mutable
public class Level {

    // fields
    public static final String TAG = Level.class.getName();
    private String levelName;
    private Viewport viewport;
    private boolean victory;
    private boolean gameOver;
    private int levelScore;
    private float cannonOffset;
    private long cannonStartTime;
    private GigaGal gigaGal;
    private Portal portal;
    private Array<BoxGround> platforms;
    private Array<IndestructibleHazard> indestructibles;
    private DelayedRemovalArray<DestructibleHazard> destructibles;
    private Array<Hazard> hazards;
    private Array<Ground> grounds;
    private DelayedRemovalArray<Ammo> ammoList;
    private DelayedRemovalArray<Explosion> explosions;
    private DelayedRemovalArray<Powerup> powerups;
    private StopWatch levelTime;

    // default ctor
    public Level() {
        viewport = new ExtendViewport(Constants.WORLD_SIZE, Constants.WORLD_SIZE);
        gigaGal = new GigaGal(new Vector2(50, 50), this);
        levelScore = 0;
        levelTime = new StopWatch();
        levelTime.start();
        platforms = new Array<BoxGround>();
        grounds = new Array<Ground>();
        destructibles = new DelayedRemovalArray<DestructibleHazard>();
        indestructibles = new DelayedRemovalArray<IndestructibleHazard>();
        hazards = new Array<Hazard>(indestructibles);
        ammoList = new DelayedRemovalArray<Ammo>();
        explosions = new DelayedRemovalArray<Explosion>();
        powerups = new DelayedRemovalArray<Powerup>();
        portal = new Portal(new Vector2(200, 200));
        gameOver = false;
        victory = false;
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

            Enums.WeaponType levelWeapon = Enums.WeaponType.NATIVE;
            for (Enums.WeaponType weapon : Arrays.asList(Enums.WeaponType.values())) {
                if (("levels/" + weapon.name() + ".dt").equals(levelName)) {
                    levelWeapon = weapon;
                }
            }

            for (Ground ground : grounds) {
                if (ground instanceof HoverableGround) {
                    HoverableGround m = (HoverableGround) ground;
                    m.update(delta);
                }
                if (ground instanceof Cannon) {
                    Cannon cannon = (Cannon) ground;
                    if (cannon.getOffset() == 0) {
                        cannonOffset += 0.25f;
                        cannon.setOffset(cannonOffset);
                        cannon.setStartTime(TimeUtils.nanoTime() + ((long) (cannon.getOffset() / MathUtils.nanoToSec)));
                    }
                    if ((Utils.secondsSince(cannon.getStartTime())  > 1.5f)) {
                        cannon.setStartTime(TimeUtils.nanoTime());
                        Enums.Orientation orientation = cannon.getOrientation();
                        if (orientation == Enums.Orientation.X) {
                            Vector2 ammoPositionLeft = new Vector2(cannon.getPosition().x - (cannon.getWidth() / 2), ground.getPosition().y);
                            Vector2 ammoPositionRight = new Vector2(cannon.getPosition().x + (cannon.getWidth() / 2), ground.getPosition().y);
                            if (gigaGal.getPosition().x < (ammoPositionLeft.x - (cannon.getWidth() / 2))) {
                                spawnAmmo(ammoPositionLeft, Direction.LEFT, orientation, Enums.AmmoIntensity.SHOT, levelWeapon, false);
                            } else if (gigaGal.getPosition().x > (ammoPositionRight.x + (cannon.getWidth() / 2))) {
                                spawnAmmo(ammoPositionRight, Direction.RIGHT, orientation, Enums.AmmoIntensity.SHOT, levelWeapon, false);
                            }
                        } else if (cannon.getOrientation() == Enums.Orientation.Y) {
                            Vector2 ammoPositionTop = new Vector2(ground.getPosition().x, cannon.getPosition().y + (cannon.getHeight() / 2));
                            Vector2 ammoPositionBottom = new Vector2(ground.getPosition().x, cannon.getPosition().y - (cannon.getHeight() / 2));
                            if (gigaGal.getPosition().y < (ammoPositionBottom.y - (cannon.getHeight() / 2))) {
                                spawnAmmo(ammoPositionBottom, Direction.DOWN, orientation, Enums.AmmoIntensity.SHOT, levelWeapon, false);
                            } else if (gigaGal.getPosition().y > (ammoPositionTop.y + (cannon.getHeight() / 2))) {
                                spawnAmmo(ammoPositionTop, Direction.UP, orientation, Enums.AmmoIntensity.SHOT, levelWeapon, false);
                            }
                        }
                    }
                }
            }

            // Update Enemies
            destructibles.begin();
            for (DestructibleHazard destructible : destructibles) {
                destructible.update(delta);
                if (destructible.getHealth() < 1) {
                    spawnExplosion(destructible.getPosition());
                    destructibles.removeValue(destructible, true);
                    levelScore += destructible.getKillScore();
                }
                if (destructible instanceof Orben) {
                    Orben orben = (Orben) destructible;
                    Enums.WeaponType weaponType = orben.getType();
                    float secondsSinceModOne = Utils.secondsSince(orben.getStartTime()) % 1;
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
            }
            destructibles.end();
            hazards.addAll(destructibles);

            // Update Bullets
            ammoList.begin();
            for (Ammo ammo : ammoList) {
                ammo.update(delta);
                if (!ammo.isActive()) {
                    ammoList.removeValue(ammo, false);
                }
            }
            ammoList.end();
            
            // Update Explosions
            explosions.begin();
            for (int i = 0; i < explosions.size; i++) {
                if (explosions.get(i).isFinished()) {
                    explosions.removeIndex(i);
                }
            }
            explosions.end();
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

        for (IndestructibleHazard indestructible : indestructibles) {
            indestructible.render(batch);
        }

        for (DestructibleHazard destructible : destructibles) {
            destructible.render(batch);
        }
        gigaGal.render(batch);

        for (Ammo chargeAmmo : ammoList) {
            chargeAmmo.render(batch);
        }

        for (Explosion explosion : explosions) {
            explosion.render(batch);
        }

        batch.end();
    }

    private void initializeDebugLevel() {

        gigaGal = new GigaGal(new Vector2(15, 40), this);

        portal = new Portal(new Vector2(150, 150));

        platforms = new Array<BoxGround>();
        ammoList = new DelayedRemovalArray<Ammo>();
        destructibles = new DelayedRemovalArray<DestructibleHazard>();
        explosions = new DelayedRemovalArray<Explosion>();
        powerups = new DelayedRemovalArray<Powerup>();


        platforms.add(new BoxGround(15, 100, 30, 20));

        BoxGround zoombaBoxGround = new BoxGround(75, 90, 100, 65);

        destructibles.add(new Zoomba(zoombaBoxGround));

        platforms.add(zoombaBoxGround);
        platforms.add(new BoxGround(35, 55, 50, 20));
        platforms.add(new BoxGround(10, 20, 20, 9));

        powerups.add(new AmmoPowerup(new Vector2(20, 110)));
    }

    public void spawnAmmo(Vector2 position, Direction direction, Enums.Orientation orientation, Enums.AmmoIntensity ammoIntensity, Enums.WeaponType weapon, boolean targetsEnemies) {
        ammoList.add(new Ammo(this, position, direction, orientation, ammoIntensity, weapon, targetsEnemies));
    }

    public boolean gigaGalFailed() {
        if (gigaGal.getPosition().y < Constants.KILL_PLANE || gigaGal.getHealth() < 1) {
            gigaGal.setHealth(0);
            gigaGal.setLives(gigaGal.getLives() - 1);
            return true;
        }
        return false;
    }

    public void spawnExplosion(Vector2 position) {
        explosions.add(new Explosion(position));
    }

    // Getters
    public final Array<BoxGround> getPlatforms() { return platforms; }
    public final Array<IndestructibleHazard> getIndestructibles() { return indestructibles; }
    public final DelayedRemovalArray<DestructibleHazard> getDestructibles() { return destructibles; }
    public final Array<Hazard> getHazards() { hazards = new Array<Hazard>(destructibles); hazards.addAll(indestructibles); hazards.addAll(ammoList); return hazards; }
    public final Array<Ground> getGrounds() { return grounds; }
    public final DelayedRemovalArray<Powerup> getPowerups() { return powerups; }
    public final Viewport getViewport() { return viewport; }
    public final Portal getPortal() { return portal; }
    public final GigaGal getGigaGal() { return gigaGal; }
    public final boolean isGameOver() { return gameOver; }
    public final boolean isVictory() { return victory; }
    public final StopWatch getLevelTime() { return levelTime; }
    public final int getLevelScore() { return levelScore; }

    // Setters
    public void setLevelScore(int levelScore) { this.levelScore = levelScore; }
    public final void setPortal(Portal portal) { this.portal = portal; }
    public final void setGigaGal(GigaGal gigaGal) { this.gigaGal = gigaGal; }
    public final void setLevelName(String levelName) { this.levelName = levelName; }
}
