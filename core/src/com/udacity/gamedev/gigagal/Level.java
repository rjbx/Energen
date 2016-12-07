package com.udacity.gamedev.gigagal;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.DelayedRemovalArray;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.udacity.gamedev.gigagal.entities.Ammo;
import com.udacity.gamedev.gigagal.entities.AmmoPowerup;
import com.udacity.gamedev.gigagal.entities.Destructible;
import com.udacity.gamedev.gigagal.entities.Ground;
import com.udacity.gamedev.gigagal.entities.Hazard;
import com.udacity.gamedev.gigagal.entities.Indestructible;
import com.udacity.gamedev.gigagal.entities.Zoomba;
import com.udacity.gamedev.gigagal.entities.Portal;
import com.udacity.gamedev.gigagal.entities.Explosion;
import com.udacity.gamedev.gigagal.entities.GigaGal;
import com.udacity.gamedev.gigagal.entities.Platform;
import com.udacity.gamedev.gigagal.entities.Powerup;
import com.udacity.gamedev.gigagal.util.Constants;
import com.udacity.gamedev.gigagal.util.Enums;
import com.udacity.gamedev.gigagal.util.Enums.Direction;

// mutable
public class Level {

    // fields
    public static final String TAG = Level.class.getName();
    private Viewport viewport;
    private boolean victory;
    private boolean gameOver;
    private int score;
    private GigaGal gigaGal;
    private Portal portal;
    private Array<Platform> platforms;
    private Array<Indestructible> indestructibles;
    private DelayedRemovalArray<Destructible> destructibles;
    private Array<Hazard> hazards;
    private Array<Ground> grounds;
    private DelayedRemovalArray<Ammo> ammo;
    private DelayedRemovalArray<Explosion> explosions;
    private DelayedRemovalArray<Powerup> powerups;

    // default ctor
    public Level() {
        viewport = new ExtendViewport(Constants.WORLD_SIZE, Constants.WORLD_SIZE);
        gigaGal = new GigaGal(new Vector2(50, 50), this);
        platforms = new Array<Platform>();
        grounds = new Array<Ground>();
        destructibles = new DelayedRemovalArray<Destructible>();
        indestructibles = new DelayedRemovalArray<Indestructible>();
        hazards = new Array<Hazard>(indestructibles);
        ammo = new DelayedRemovalArray<Ammo>();
        explosions = new DelayedRemovalArray<Explosion>();
        powerups = new DelayedRemovalArray<Powerup>();
        portal = new Portal(new Vector2(200, 200));
        gameOver = false;
        victory = false;
        score = 0;
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

            // Update Bullets
            ammo.begin();
            for (Ammo chargeAmmo : ammo) {
                chargeAmmo.update(delta);
                if (!chargeAmmo.isActive()) {
                    ammo.removeValue(chargeAmmo, false);
                }
            }
            ammo.end();

            // Update Enemies
            destructibles.begin();
            for (int i = 0; i < destructibles.size; i++) {
                Destructible destructible = destructibles.get(i);
                destructible.update(delta);
                if (destructible.getHealth() < 1) {
                    spawnExplosion(destructible.getPosition());
                    destructibles.removeIndex(i);
                    score += destructible.getKillScore();
                }
            }
            destructibles.end();
            hazards.addAll(destructibles);

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

        for (Indestructible indestructible : indestructibles) {
            indestructible.render(batch);
        }

        for (Destructible destructible : destructibles) {
            destructible.render(batch);
        }
        gigaGal.render(batch);

        for (Ammo chargeAmmo : ammo) {
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

        platforms = new Array<Platform>();
        ammo = new DelayedRemovalArray<Ammo>();
        destructibles = new DelayedRemovalArray<Destructible>();
        explosions = new DelayedRemovalArray<Explosion>();
        powerups = new DelayedRemovalArray<Powerup>();


        platforms.add(new Platform(15, 100, 30, 20));

        Platform zoombaPlatform = new Platform(75, 90, 100, 65);

        destructibles.add(new Zoomba(zoombaPlatform));

        platforms.add(zoombaPlatform);
        platforms.add(new Platform(35, 55, 50, 20));
        platforms.add(new Platform(10, 20, 20, 9));

        powerups.add(new AmmoPowerup(new Vector2(20, 110)));
    }

    public void spawnAmmo(Vector2 position, Direction direction, Enums.ShotIntensity shotIntensity, Enums.Weapon weapon) {
        ammo.add(new Ammo(this, position, direction, shotIntensity, weapon));
    }

    public void spawnExplosion(Vector2 position) {
        explosions.add(new Explosion(position));
    }

    // Getters
    public final Array<Platform> getPlatforms() { return platforms; }
    public final Array<Indestructible> getIndestructibles() { return indestructibles; }
    public final DelayedRemovalArray<Destructible> getDestructibles() { return destructibles; }
    public final Array<Hazard> getHazards() { hazards = new Array<Hazard>(destructibles); hazards.addAll(indestructibles); return hazards; }
    public final Array<Ground> getGrounds() { return grounds; }
    public final DelayedRemovalArray<Powerup> getPowerups() { return powerups; }
    public final Viewport getViewport() { return viewport; }
    public final int getScore() { return score; }
    public final Portal getPortal() { return portal; }
    public final GigaGal getGigaGal() { return gigaGal; }
    public final boolean isGameOver() { return gameOver; }
    public final boolean isVictory() { return victory; }

    // Setters
    public final void setScore(int score) { this.score = score; }
    public final void setPortal(Portal portal) { this.portal = portal; }
    public final void setGigaGal(GigaGal gigaGal) { this.gigaGal = gigaGal; }
}
