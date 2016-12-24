package com.udacity.gamedev.gigagal;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.udacity.gamedev.gigagal.entities.GigaGal;
import com.udacity.gamedev.gigagal.entities.Powerup;
import com.udacity.gamedev.gigagal.entities.TurboPowerup;
import com.udacity.gamedev.gigagal.overlays.IndicatorHud;
import com.udacity.gamedev.gigagal.overlays.GameOverOverlay;
import com.udacity.gamedev.gigagal.overlays.GaugeHud;
import com.udacity.gamedev.gigagal.overlays.InputControls;
import com.udacity.gamedev.gigagal.overlays.PauseOverlay;
import com.udacity.gamedev.gigagal.overlays.VictoryOverlay;
import com.udacity.gamedev.gigagal.util.Assets;
import com.udacity.gamedev.gigagal.util.ChaseCam;
import com.udacity.gamedev.gigagal.util.Constants;
import com.udacity.gamedev.gigagal.util.Enums;
import com.udacity.gamedev.gigagal.util.LevelLoader;
import com.udacity.gamedev.gigagal.util.Utils;
import org.apache.commons.lang3.time.StopWatch;
import java.util.Arrays;

public class GameplayScreen extends ScreenAdapter {

    // fields
    public static final String TAG = GameplayScreen.class.getName();
    private GigaGalGame game;
    private InputControls inputControls;
    private SpriteBatch batch;
    private ShapeRenderer renderer;
    private long levelEndOverlayStartTime;
    private int levelNumber;
    private Level level;
    private ChaseCam chaseCam;
    private GaugeHud meterHud;
    private IndicatorHud contextHud;
    private VictoryOverlay victoryOverlay;
    private GameOverOverlay gameOverOverlay;
    PauseOverlay pauseOverlay;
    private Array<String> completedLevels;
    private String levelName;
    private GigaGal gigaGal;
    private Array<TurboPowerup> powerups;
    private int totalScore;
    private StopWatch totalTime;
    private boolean paused;
    boolean levelEnded;
    long pauseTime;
    float pauseDuration;

    // default ctor
    public GameplayScreen(GigaGalGame game) {
        this.game = game;
        completedLevels = new Array<String>();
        totalTime = new StopWatch();
        totalTime.start();
        totalTime.suspend();
        paused = false;
        levelEnded = false;
        pauseTime = 0;
        pauseDuration = 0;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        renderer = new ShapeRenderer();
        renderer.setAutoShapeType(true);
        chaseCam = ChaseCam.getInstance();
        pauseOverlay = new PauseOverlay(this);
        victoryOverlay = new VictoryOverlay(this);
        gameOverOverlay = new GameOverOverlay();
        inputControls = new InputControls();
        powerups = new Array<TurboPowerup>();
        pauseOverlay.getCursor().setInputControls(inputControls);

        // : Use Gdx.input.setInputProcessor() to send touch events to inputControls
        Gdx.input.setInputProcessor(inputControls);
        // : When you're done testing, use onMobile() turn off the controls when not on a mobile device
        // onMobile();
        startNewLevel();
    }

    private boolean onMobile() {
        return Gdx.app.getType() == ApplicationType.Android || Gdx.app.getType() == ApplicationType.iOS;
    }

    @Override
    public void resize(int width, int height) {
        meterHud.getViewport().update(width, height, true);
        contextHud.getViewport().update(width, height, true);
        victoryOverlay.getViewport().update(width, height, true);
        gameOverOverlay.getViewport().update(width, height, true);
        pauseOverlay.getViewport().update(width, height, true);
        pauseOverlay.getCursor().getViewport().update(width, height, true);
        level.getViewport().update(width, height, true);
        chaseCam.camera = level.getViewport().getCamera();
        inputControls.getViewport().update(width, height, true);
        inputControls.recalculateButtonPositions();
        gigaGal.setInputControls(inputControls);
        chaseCam.setInputControls(inputControls);
    }

    @Override
    public void dispose() {
        Assets.getInstance().dispose();
    }

    @Override
    public void render(float delta) {

        // : When you're done testing, use onMobile() turn off the controls when not on a mobile device
        // onMobile();
        Gdx.gl.glClearColor(
                Constants.BACKGROUND_COLOR.r,
                Constants.BACKGROUND_COLOR.g,
                Constants.BACKGROUND_COLOR.b,
                Constants.BACKGROUND_COLOR.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        if (!chaseCam.getFollowing()) {
            level.render(batch);
            chaseCam.update(delta);
        }

        renderLevelEndOverlays(batch);
        if (level.gigaGalFailed()) {
            if (gigaGal.getLives() > -1) {
                restartLevel();
            }
        }

        if (!levelEnded) {
            if (paused) {
                pauseOverlay.render(batch);
                gigaGal.look(); // enables gigagal to toggle weapon during pause without enabling other gigagal features
                if (inputControls.pauseButtonJustPressed || inputControls.shootButtonJustPressed) {
                    if (pauseOverlay.getCursor().getPosition() == 73 && chaseCam.getFollowing()) {
                        unpause();
                    } else if (pauseOverlay.getCursor().getPosition() == 58) {
                        unpause();
                        totalTime.suspend();
                        game.setScreen(game.getLevelSelectScreen());
                    } else if (pauseOverlay.getCursor().getPosition() == 43) {
                        if (!chaseCam.getFollowing()) {
                            chaseCam.setFollowing(true);
                        } else {
                            chaseCam.setFollowing(false);
                        }
                    } else if (pauseOverlay.getCursor().getPosition() == 28) {
                        game.create();
                    }
                }
            } else if (inputControls.pauseButtonJustPressed) {
                level.getLevelTime().suspend();
                totalTime.suspend();
                paused = true;
                pauseOverlay.init();
                pauseTime = TimeUtils.nanoTime();
                pauseDuration = gigaGal.getPauseDuration();
            } else {
                level.update(delta);
                chaseCam.update(delta);
                level.render(batch);
            }
            meterHud.render(batch, renderer);
            contextHud.render(batch);
            inputControls.update();
            inputControls.render(batch);
        }
    }

    private void renderLevelEndOverlays(SpriteBatch batch) {
        if (level.isGameOver()) {
            levelEnded = true;
            if (levelEndOverlayStartTime == 0) {
                level.getLevelTime().suspend();
                totalTime.suspend();
                levelEndOverlayStartTime = TimeUtils.nanoTime();
                gameOverOverlay.init();
            }

            gameOverOverlay.render(batch);
            if (Utils.secondsSince(levelEndOverlayStartTime) > Constants.LEVEL_END_DURATION) {
                levelEndOverlayStartTime = 0;
                game.setScreen(game.getLevelSelectScreen());
            }
        } else if (level.isVictory()) {
            levelEnded = true;
            if (levelEndOverlayStartTime == 0) {
                level.getLevelTime().suspend();
                totalTime.suspend();
                totalScore += level.getLevelScore();
                levelEndOverlayStartTime = TimeUtils.nanoTime();
                victoryOverlay.init();
            }

            victoryOverlay.render(batch);
            if (Utils.secondsSince(levelEndOverlayStartTime) > Constants.LEVEL_END_DURATION) {
                levelEndOverlayStartTime = 0;
                levelComplete();
            }
        } else {
            levelEnded = false;
        }
    }

    private void startNewLevel() {

//      level = Level.debugLevel();
//      String levelName = Constants.LEVELS[levelNumber];
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
        meterHud = new GaugeHud(level);
        contextHud = new IndicatorHud(level);
        this.gigaGal = level.getGigaGal();
        for (String completedLevelName : completedLevels) {
            for (Enums.WeaponType weapon : Arrays.asList(Constants.weapons)) {
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
        totalTime.resume();
    }

    public void restartLevel() {
        gigaGal.respawn();
        level.getPowerups().addAll(powerups);
    }

    public void levelComplete() {
        completedLevels.add(levelName);
        game.setScreen(game.getLevelSelectScreen());
    }

    public void unpause() {
        gigaGal.setPauseDuration(Utils.secondsSincePause(pauseTime) + pauseDuration);
        level.getLevelTime().resume();
        totalTime.resume();
        paused = false;
    }

    public Level getLevel() { return level; }
    public void setGame(GigaGalGame game) { this.game = game;  }
    public void setLevelName(String levelName) { this.levelName = levelName; }
    public int getTotalScore() { return totalScore; }
    public StopWatch getTotalTime() { return totalTime; }
    public ChaseCam getChaseCam() { return chaseCam; }
    public InputControls getInputControls() { return inputControls; }
}

