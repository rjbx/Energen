package com.udacity.gamedev.gigagal;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
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
    private String newLevelName;

    // default ctor
    public GameplayScreen(String levelName) {
        this.newLevelName = levelName;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        chaseCam = ChaseCam.getInstance();
        hud = new GigaGalHud();
        victoryOverlay = new VictoryOverlay();
        gameOverOverlay = new GameOverOverlay();
        onscreenControls = new OnscreenControls();
        completedLevels = new Array<String>();

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
        onscreenControls.setGigaGal(level.getGigaGal());
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

        hud.render(batch, level.getGigaGal().getLives(), level.getGigaGal().getAmmo(), level.getGigaGal(). getHealth(), level.getScore(), level.getGigaGal().getWeaponList(), level.getGigaGal().getWeapon());
        renderLevelEndOverlays(batch);
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
                levelFailed();
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
 //     String newLevelName = Constants.LEVELS[levelNumber];

        AssetManager am = new AssetManager();
        level = LevelLoader.load(newLevelName);
        level.setLevelName(newLevelName);
        int levelIndex = (Arrays.asList(Constants.LEVELS)).indexOf(newLevelName);
        Assets.getInstance().init(am, levelIndex);
        for (String completedLevelName : completedLevels) {
            for (Enums.Weapon weapon : Arrays.asList(Constants.weapons)) {
                if (completedLevelName.equals("levels/" + weapon.name() + ".dt")) {
                    if (!level.getGigaGal().getWeaponList().contains(weapon)) {
                        level.getGigaGal().addWeapon(weapon);
                    }
                }
            }
        }
        chaseCam.camera = level.getViewport().getCamera();
        chaseCam.target = level.getGigaGal();
        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    public void levelComplete() {
        String completedLevelName = Constants.LEVELS[levelNumber];
        completedLevels.add(completedLevelName);
        levelNumber++;
        startNewLevel();
    }

    public void levelFailed() {
        levelNumber = 0;
        startNewLevel();
    }
}
