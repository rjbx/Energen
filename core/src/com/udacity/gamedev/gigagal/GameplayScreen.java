package com.udacity.gamedev.gigagal;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.udacity.gamedev.gigagal.entities.GigaGal;
import com.udacity.gamedev.gigagal.entities.Powerup;
import com.udacity.gamedev.gigagal.entities.TurboPowerup;
import com.udacity.gamedev.gigagal.overlays.GameOverOverlay;
import com.udacity.gamedev.gigagal.overlays.GigaGalHud;
import com.udacity.gamedev.gigagal.overlays.OnscreenControls;
import com.udacity.gamedev.gigagal.overlays.VictoryOverlay;
import com.udacity.gamedev.gigagal.util.Assets;
import com.udacity.gamedev.gigagal.util.ChaseCam;
import com.udacity.gamedev.gigagal.util.Constants;
import com.udacity.gamedev.gigagal.util.Enums;
import com.udacity.gamedev.gigagal.util.LevelLoader;
import com.udacity.gamedev.gigagal.util.Utils;
import java.util.Arrays;

// immutable
public final class GameplayScreen extends ScreenAdapter {

    // fields
    public static final String TAG = GameplayScreen.class.getName();
    private GigaGalGame game;
    private OnscreenControls onscreenControls;
    private SpriteBatch batch;
    private long levelEndOverlayStartTime;
    private int levelNumber;
    private Level level;
    private ChaseCam chaseCam;
    private GigaGalHud hud;
    private VictoryOverlay victoryOverlay;
    private GameOverOverlay gameOverOverlay;
    private Array<String> completedLevels;
    private String levelName;
    private GigaGal gigaGal;
    private Array<TurboPowerup> powerups;

    // default ctor
    public GameplayScreen(GigaGalGame game) {
        this.game = game;
        completedLevels = new Array<String>();
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        chaseCam = ChaseCam.getInstance();
        victoryOverlay = new VictoryOverlay();
        gameOverOverlay = new GameOverOverlay();
        onscreenControls = new OnscreenControls();
        powerups = new Array<TurboPowerup>();

        // : Use Gdx.input.setInputProcessor() to send touch events to onscreenControls
        Gdx.input.setInputProcessor(onscreenControls);
        // : When you're done testing, use onMobile() turn off the controls when not on a mobile device
        // onMobile();
        startNewLevel();
    }

    private boolean onMobile() {
        return Gdx.app.getType() == ApplicationType.Android || Gdx.app.getType() == ApplicationType.iOS;
    }

    @Override
    public void resize(int width, int height) {
        hud.getViewport().update(width, height, true);
        victoryOverlay.getViewport().update(width, height, true);
        gameOverOverlay.viewport.update(width, height, true);
        level.getViewport().update(width, height, true);
        chaseCam.camera = level.getViewport().getCamera();
        onscreenControls.setGigaGal(gigaGal);
        onscreenControls.getViewport().update(width, height, true);
        onscreenControls.recalculateButtonPositions();
    }

    @Override
    public void dispose() {
        Assets.getInstance().dispose();
    }

    @Override
    public void render(float delta) {

        level.update(delta);
        chaseCam.update(delta);


        Gdx.gl.glClearColor(
                Constants.BACKGROUND_COLOR.r,
                Constants.BACKGROUND_COLOR.g,
                Constants.BACKGROUND_COLOR.b,
                Constants.BACKGROUND_COLOR.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        level.render(batch);

        // : When you're done testing, use onMobile() turn off the controls when not on a mobile device
        // onMobile();
        onscreenControls.render(batch);

        hud.render(batch);
        renderLevelEndOverlays(batch);
        if (level.gigaGalFailed()) {
            if (gigaGal.getLives() > -1) {
                restartLevel();
            }
        }
    }

    private void renderLevelEndOverlays(SpriteBatch batch) {
        if (level.isGameOver()) {
            if (levelEndOverlayStartTime == 0) {
                levelEndOverlayStartTime = TimeUtils.nanoTime();
                gameOverOverlay.init();
            }

            gameOverOverlay.render(batch);
            if (Utils.secondsSince(levelEndOverlayStartTime) > Constants.LEVEL_END_DURATION) {
                levelEndOverlayStartTime = 0;
                game.create();
            }
        } else if (level.isVictory()) {
            if (levelEndOverlayStartTime == 0) {
                levelEndOverlayStartTime = TimeUtils.nanoTime();
                victoryOverlay.init();
            }

            victoryOverlay.render(batch);
            if (Utils.secondsSince(levelEndOverlayStartTime) > Constants.LEVEL_END_DURATION) {
                levelEndOverlayStartTime = 0;
                levelComplete();
            }
        }
    }

    private void startNewLevel() {

//        level = Level.debugLevel();
 //     String levelName = Constants.LEVELS[levelNumber];

        AssetManager am = new AssetManager();
        level = LevelLoader.load(levelName);
        level.setLevelName(levelName);
        levelNumber = (Arrays.asList(Constants.LEVELS)).indexOf(levelName);
        powerups = new Array<TurboPowerup>();
        for (Powerup powerup : level.getPowerups()) {
            if (powerup instanceof TurboPowerup) {
                powerups.add((TurboPowerup) powerup);
            }
        }
        Assets.getInstance().init(am, levelNumber);
        hud = new GigaGalHud(level);
        this.gigaGal = level.getGigaGal();
        for (String completedLevelName : completedLevels) {
            for (Enums.Weapon weapon : Arrays.asList(Constants.weapons)) {
                if (completedLevelName.equals("levels/" + weapon.name() + ".dt")) {
                    if (!gigaGal.getWeaponList().contains(weapon)) {
                        gigaGal.addWeapon(weapon);
                    }
                }
            }
        }
        chaseCam.camera = level.getViewport().getCamera();
        chaseCam.target = gigaGal;
        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    public void restartLevel() {
        gigaGal.respawn();
        level.getPowerups().addAll(powerups);
    }

    public void levelComplete() {
        completedLevels.add(levelName);
        game.setScreen(game.getLevelSelectScreen());
    }

    public void setGame(GigaGalGame game) { this.game = game; }
    public void setLevelName(String levelName) { this.levelName = levelName; }
}
