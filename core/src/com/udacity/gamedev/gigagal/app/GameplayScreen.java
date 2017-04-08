package com.udacity.gamedev.gigagal.app;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.TimeUtils;
import com.udacity.gamedev.gigagal.entities.GigaGal;
import com.udacity.gamedev.gigagal.overlays.ControlsOverlay;
import com.udacity.gamedev.gigagal.overlays.IndicatorHud;
import com.udacity.gamedev.gigagal.overlays.DefeatOverlay;
import com.udacity.gamedev.gigagal.overlays.GaugeHud;
import com.udacity.gamedev.gigagal.overlays.MessageOverlay;
import com.udacity.gamedev.gigagal.overlays.OptionsOverlay;
import com.udacity.gamedev.gigagal.overlays.PauseOverlay;
import com.udacity.gamedev.gigagal.overlays.VictoryOverlay;
import com.udacity.gamedev.gigagal.util.ChaseCam;
import com.udacity.gamedev.gigagal.util.Constants;
import com.udacity.gamedev.gigagal.util.Enums;
import com.udacity.gamedev.gigagal.util.LevelLoader;
import com.udacity.gamedev.gigagal.util.Timer;
import com.udacity.gamedev.gigagal.util.Utils;

import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class GameplayScreen extends ScreenAdapter {

    // fields
    public static final String TAG = GameplayScreen.class.getName();
    private static InputControls inputControls;
    private static ControlsOverlay controlsOverlay;
    private GigaGalGame game;
    private Preferences prefs;
    private GaugeHud gaugeHud;
    private IndicatorHud indicatorHud;
    private VictoryOverlay victoryOverlay;
    private DefeatOverlay defeatOverlay;
    private PauseOverlay pauseOverlay;
    private OptionsOverlay optionsOverlay;
    private MessageOverlay messageOverlay;
    private SpriteBatch batch;
    private long levelEndOverlayStartTime;
    private Level level;
    private ChaseCam chaseCam;
    private Array<Enums.LevelName> completedLevels;
    private Enums.LevelName levelName;
    private GigaGal gigaGal;
    private int totalScore;
    private Timer totalTime;
    private boolean paused;
    private boolean optionsVisible;
    private boolean levelEnded;
    private long pauseTime;
    private float pauseDuration;

    // default ctor
    public GameplayScreen(GigaGalGame game, Enums.LevelName levelName) {
        this.game = game;
        this.levelName = levelName;
        prefs = game.getPreferences();
        completedLevels = new Array<Enums.LevelName>();
        totalTime = new Timer();
        paused = false;
        optionsVisible = false;
        levelEnded = false;
        pauseTime = 0;
        pauseDuration = 0;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        chaseCam = ChaseCam.getInstance();
        pauseOverlay = new PauseOverlay(this);
        optionsOverlay = new OptionsOverlay(this);
        messageOverlay = new MessageOverlay("");
        victoryOverlay = new VictoryOverlay(this);
        defeatOverlay = new DefeatOverlay();
        inputControls = InputControls.getInstance();
        controlsOverlay = ControlsOverlay.getInstance();

        // : Use Gdx.input.setInputProcessor() to send touch events to inputControls
        Gdx.input.setInputProcessor(inputControls);
        startNewLevel();
    }

    @Override
    public void resize(int width, int height) {
        gaugeHud.getViewport().update(width, height, true);
        indicatorHud.getViewport().update(width, height, true);
        victoryOverlay.getViewport().update(width, height, true);
        defeatOverlay.getViewport().update(width, height, true);
        pauseOverlay.getViewport().update(width, height, true);
        pauseOverlay.getCursor().getViewport().update(width, height, true);
        optionsOverlay.getViewport().update(width, height, true);
        optionsOverlay.getCursor().getViewport().update(width, height, true);
        messageOverlay.getViewport().update(width, height, true);
        level.getViewport().update(width, height, true);
        chaseCam.camera = level.getViewport().getCamera();
        controlsOverlay.getViewport().update(width, height, true);
        controlsOverlay.recalculateButtonPositions();
        gigaGal.setInputControls(inputControls);
        chaseCam.setInputControls(inputControls);
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

        renderLevelEndOverlays();
        if (level.gigaGalFailed()) {
            if (gigaGal.getLives() > -1) {
                restartLevel();
            }
        }

        if (!levelEnded) {
            if (paused) {
                if (!optionsVisible) {
                    pauseOverlay.render();
                    if (inputControls.jumpButtonJustPressed && gigaGal.getAction() == Enums.Action.STANDING) {
                        gigaGal.toggleWeapon(Enums.Direction.DOWN); // enables gigagal to toggleWeapon weapon during pause without enabling other gigagal features
                    }
                    if (inputControls.shootButtonJustPressed) {
                        if (pauseOverlay.getCursor().getPosition() == 73 && chaseCam.getFollowing()) {
                            unpause();
                        } else if (pauseOverlay.getCursor().getPosition() == 58) {
                            unpause();
                            totalTime.suspend();
                            game.setScreen(new LevelSelectScreen(game));
                            this.dispose();
                            return;
                        } else if (pauseOverlay.getCursor().getPosition() == 43) {
                            optionsVisible = true;
                        }
                    } else if (inputControls.pauseButtonJustPressed) {
                        unpause();
                    }
                } else {
                    optionsOverlay.render();
                    if (inputControls.shootButtonJustPressed) {
                        if (optionsOverlay.getCursor().getPosition() == 73) {
                            optionsVisible = false;
                        } else if (optionsOverlay.getCursor().getPosition() == 58) {
                            if (!chaseCam.getFollowing()) {
                                optionsOverlay.setDebugMode(false);
                                chaseCam.setFollowing(true);
                            } else {
                                optionsOverlay.setDebugMode(true);
                                chaseCam.setFollowing(false);
                            }
                        } else if (optionsOverlay.getCursor().getPosition() == 43) {
                            controlsOverlay.onMobile = Utils.toggleBoolean(controlsOverlay.onMobile);
                            prefs.putBoolean("Mobile", controlsOverlay.onMobile);
                        } else if (optionsOverlay.getCursor().getPosition() == 28) {
                            game.dispose();
                            game.create();
                            return;
                        }
                    } else if (inputControls.pauseButtonJustPressed) {
                        optionsVisible = false;
                    }
                }
            } else if (inputControls.pauseButtonJustPressed) {
                level.getLevelTime().suspend();
                totalTime.suspend();
                paused = true;
                pauseTime = TimeUtils.nanoTime();
                pauseDuration = gigaGal.getPauseTimeSeconds();
            } else {
                level.update(delta);
                chaseCam.update(delta);
                level.render(batch);
            }
            gaugeHud.render();
            indicatorHud.render();
            controlsOverlay.render();
        }
        if (level.getLoadEx()) {
            messageOverlay.setMessage(Constants.LEVEL_KEY_MESSAGE);
            messageOverlay.render();
        }
        inputControls.update();
    }

    private void renderLevelEndOverlays() {
        if (level.isGameOver()) {
            levelEnded = true;
            if (levelEndOverlayStartTime == 0) {
                level.getLevelTime().suspend();
                totalTime.suspend();
                levelEndOverlayStartTime = TimeUtils.nanoTime();
            }

            defeatOverlay.render();
            if (Utils.secondsSince(levelEndOverlayStartTime) > Constants.LEVEL_END_DURATION / 2) {
                levelEndOverlayStartTime = 0;
                game.setScreen(new LevelSelectScreen(game));
                this.dispose();
                return;
            }
        } else if (level.isVictory()) {
            levelEnded = true;
            if (levelEndOverlayStartTime == 0) {
                level.getLevelTime().suspend();
                totalTime.suspend();
                totalScore += level.getLevelScore();
                game.getPreferences().putInteger("Score", totalScore);
                game.getPreferences().putLong("Time", totalTime.getNanoTime());
                game.getPreferences().flush();
                levelEndOverlayStartTime = TimeUtils.nanoTime();
            }
            victoryOverlay.render();
            if (Utils.secondsSince(levelEndOverlayStartTime) > Constants.LEVEL_END_DURATION) {
                levelEndOverlayStartTime = 0;
                levelComplete();
            }
        } else {
            levelEnded = false;
        }
    }

    public void readLevelFile() throws IOException, ParseException, GdxRuntimeException {
        level = LevelLoader.load("levels/" + levelName + ".dt");

    }

    private void startNewLevel() {
//      level = Level.debugLevel();
//      String levelName = Constants.LEVELS[levelNumber];

        // get prefs
        String savedWeapons = game.getPreferences().getString("Weapons", Enums.WeaponType.NATIVE.name());
        if (!savedWeapons.equals(Enums.WeaponType.NATIVE.name())) {
            List<String> savedWeaponsList = Arrays.asList(savedWeapons.split(", "));
            for (String weaponString : savedWeaponsList) {
                if (!completedLevels.contains(Enums.WeaponType.valueOf(weaponString).levelName(), false)) {
                    completedLevels.add(Enums.WeaponType.valueOf(weaponString).levelName());
                }
            }
        }
        totalScore = game.getPreferences().getInteger("Score", totalScore);
        totalTime.start(game.getPreferences().getLong("Time", totalTime.getNanoTime()));
        totalTime.suspend();
        game.getPreferences().flush();

        // set level attributes
        level.setLevelName(levelName);
        level.setDifficulty(prefs.getInteger("Difficulty", 0));
        gaugeHud = new GaugeHud(level);
        indicatorHud = new IndicatorHud(level);
        this.gigaGal = level.getGigaGal();
        for (Enums.LevelName completedLevelName : completedLevels) {
            for (Enums.WeaponType weapon : Arrays.asList(Enums.WeaponType.values())) {
                if (completedLevelName.equals(weapon.levelName())) {
                    if (!gigaGal.getWeaponList().contains(weapon)) {
                        gigaGal.addWeapon(weapon);
                    }
                }
            }
        }
        String weaponListString = gigaGal.getWeaponList().toString();
        weaponListString = weaponListString.substring(1, weaponListString.length() - 1);
        game.getPreferences().putString("Weapons", weaponListString);
        chaseCam.camera = level.getViewport().getCamera();
        chaseCam.target = gigaGal;
        totalTime.resume();
        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    private void restartLevel() {
        gigaGal.respawn();
    }

    private void levelComplete() {
        if (!completedLevels.contains(levelName, false)) {
            completedLevels.add(levelName);
        }

        game.setScreen(new LevelSelectScreen(game));
        this.dispose();
        return;
    }

    private void unpause() {
        gigaGal.setPauseTimeSeconds(Utils.secondsSincePause(pauseTime) + pauseDuration);
        level.getLevelTime().resume();
        totalTime.resume();
        paused = false;
    }

    @Override
    public void dispose() {
        completedLevels.clear();
        inputControls.clear();
        totalTime.stop();
        totalTime = null;
        victoryOverlay.dispose();
        defeatOverlay.dispose();
        pauseOverlay.dispose();
        optionsOverlay.dispose();
        indicatorHud.dispose();
        messageOverlay.dispose();
        gaugeHud.dispose();
        batch.dispose();
        level.dispose();
        System.gc();
        super.dispose();
    }

    public Level getLevel() { return level; }
    public Enums.LevelName getLevelName() { return levelName; }
    public int getTotalScore() { return totalScore; }
    public Timer getTotalTime() { return totalTime; }
    public ChaseCam getChaseCam() { return chaseCam; }

    public void setGame(GigaGalGame game) { this.game = game;  }
}