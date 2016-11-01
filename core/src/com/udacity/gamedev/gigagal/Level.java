package com.udacity.gamedev.gigagal;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.DelayedRemovalArray;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.udacity.gamedev.gigagal.entities.Ammo;
import com.udacity.gamedev.gigagal.entities.Zoomba;
import com.udacity.gamedev.gigagal.entities.ExitPortal;
import com.udacity.gamedev.gigagal.entities.Explosion;
import com.udacity.gamedev.gigagal.entities.GigaGal;
import com.udacity.gamedev.gigagal.entities.Platform;
import com.udacity.gamedev.gigagal.entities.Powerup;
import com.udacity.gamedev.gigagal.util.Constants;
import com.udacity.gamedev.gigagal.util.Enums;
import com.udacity.gamedev.gigagal.util.Enums.DirectionalState;

// mutable
public class Level {

    public static final String TAG = Level.class.getName();

    private Viewport viewport;
    private boolean victory;
    private boolean gameOver;
    public int score;
    private GigaGal gigaGal;
    private ExitPortal exitPortal;
    private Array<Platform> platforms;
    private DelayedRemovalArray<Zoomba> enemies;
    private DelayedRemovalArray<Ammo> bullets;
    private DelayedRemovalArray<Explosion> explosions;
    private DelayedRemovalArray<Powerup> powerups;

    public Level() {
        viewport = new ExtendViewport(Constants.WORLD_SIZE, Constants.WORLD_SIZE);
        gigaGal = new GigaGal(new Vector2(50, 50), this);
        platforms = new Array<Platform>();
        enemies = new DelayedRemovalArray<Zoomba>();
        bullets = new DelayedRemovalArray<Ammo>();
        explosions = new DelayedRemovalArray<Explosion>();
        powerups = new DelayedRemovalArray<Powerup>();
        exitPortal = new ExitPortal(new Vector2(200, 200));
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
        } else if (gigaGal.getPosition().dst(exitPortal.position) < Constants.EXIT_PORTAL_RADIUS) {
            victory = true;
        }

        if (!gameOver && !victory) {

            gigaGal.update(delta, platforms);

            // Update Bullets
            bullets.begin();
            for (Ammo chargeAmmo : bullets) {
                chargeAmmo.update(delta);
                if (!chargeAmmo.active) {
                    bullets.removeValue(chargeAmmo, false);
                }
            }
            bullets.end();

            // Update Enemies
            enemies.begin();
            for (int i = 0; i < enemies.size; i++) {
                Zoomba zoomba = enemies.get(i);
                zoomba.update(delta);
                if (zoomba.health < 1) {
                    spawnExplosion(zoomba.position);
                    enemies.removeIndex(i);
                    score += Constants.ZOOMBA_KILL_SCORE;
                }
            }
            enemies.end();

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

        for (Platform platform : platforms) {
            platform.render(batch);
        }

        exitPortal.render(batch);

        for (Powerup powerup : powerups) {
            powerup.render(batch);
        }

        for (Zoomba zoomba : enemies) {
            zoomba.render(batch);
        }
        gigaGal.render(batch);

        for (Ammo chargeAmmo : bullets) {
            chargeAmmo.render(batch);
        }

        for (Explosion explosion : explosions) {
            explosion.render(batch);
        }

        batch.end();
    }

    private void initializeDebugLevel() {

        gigaGal = new GigaGal(new Vector2(15, 40), this);

        exitPortal = new ExitPortal(new Vector2(150, 150));

        platforms = new Array<Platform>();
        bullets = new DelayedRemovalArray<Ammo>();
        enemies = new DelayedRemovalArray<Zoomba>();
        explosions = new DelayedRemovalArray<Explosion>();
        powerups = new DelayedRemovalArray<Powerup>();


        platforms.add(new Platform(15, 100, 30, 20));

        Platform zoombaPlatform = new Platform(75, 90, 100, 65);

        enemies.add(new Zoomba(zoombaPlatform));

        platforms.add(zoombaPlatform);
        platforms.add(new Platform(35, 55, 50, 20));
        platforms.add(new Platform(10, 20, 20, 9));

        powerups.add(new Powerup(new Vector2(20, 110)));
    }

    public void spawnBullet(Vector2 position, DirectionalState directionalState, Enums.AmmoType ammoType) {
        bullets.add(new Ammo(this, position, directionalState, ammoType));
    }

    public void spawnExplosion(Vector2 position) {
        explosions.add(new Explosion(position));
    }

    // Getters
    public Array<Platform> getPlatforms() { return platforms; }
    public DelayedRemovalArray<Zoomba> getEnemies() { return enemies; }
    public DelayedRemovalArray<Powerup> getPowerups() {
        return powerups;
    }
    public ExitPortal getExitPortal() { return exitPortal; }
    public Viewport getViewport() { return viewport; }
    public GigaGal getGigaGal() { return gigaGal; }
    public boolean isGameOver() { return gameOver; }
    public boolean isVictory() { return victory; }
    public int getScore() { return score; }

    public void setVictory(boolean victory) {
        this.victory = victory;
    }

    public void setGameOver(boolean gameOver) { this.gameOver = gameOver; }
    public void setScore(int score) {
        this.score = score;
    }

    public void setExitPortal(ExitPortal exitPortal) {
        this.exitPortal = exitPortal;
    }

    public void setViewport(Viewport viewport) {
        this.viewport = viewport;
    }

    public void setGigaGal(GigaGal gigaGal) {
        this.gigaGal = gigaGal;
    }
}
