package com.udacity.gamedev.gigagal.app;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.udacity.gamedev.gigagal.entities.GigaGal;
import com.udacity.gamedev.gigagal.overlays.ControlsOverlay;
import com.udacity.gamedev.gigagal.overlays.CursorOverlay;
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
    private OptionsOverlay pauseOverlay;
    private OptionsOverlay optionsOverlay;
    private MessageOverlay messageOverlay;
    private CursorOverlay cursorOverlay;
    private SpriteBatch batch;
    private ShapeRenderer renderer;
    private BitmapFont font;
    private ExtendViewport viewport;
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
        batch = new SpriteBatch(); // shared by all overlays instantiated from this class
        renderer = new ShapeRenderer(); // shared by all overlays instantiated from this class
        renderer.setAutoShapeType(true);
        font = new BitmapFont(Gdx.files.internal(Constants.FONT_FILE)); // shared by all overlays instantiated from this class
        font.getData().setScale(.4f); // shared by all overlays instantiated from this class
        viewport = new ExtendViewport(Constants.WORLD_SIZE, Constants.WORLD_SIZE); // shared by all overlays instantiated from this class
        cursorOverlay = new CursorOverlay(73, 43, Enums.Orientation.Y); // shared by all overlays instantiated from this class
        pauseOverlay = new OptionsOverlay(this);
        optionsOverlay = new OptionsOverlay(this);
        messageOverlay = new MessageOverlay("");
        victoryOverlay = new VictoryOverlay(this);
        defeatOverlay = new DefeatOverlay();
        inputControls = InputControls.getInstance();
        controlsOverlay = ControlsOverlay.getInstance();
        chaseCam = ChaseCam.getInstance();

        // : Use Gdx.input.setInputProcessor() to send touch events to inputControls
        Gdx.input.setInputProcessor(inputControls);
        startNewLevel();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);


//        gaugeHud.getViewport().update(width, height, true);
//        indicatorHud.getViewport().update(width, height, true);
//
//        defeatOverlay.getViewport().update(width, height, true);
//        pauseOverlay.getViewport().update(width, height, true);
//        pauseOverlay.getCursor().getViewport().update(width, height, true);
//        optionsOverlay.getViewport().update(width, height, true);
//        optionsOverlay.getCursor().getViewport().update(width, height, true);
//        messageOverlay.getViewport().update(width, height, true);
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

        if (level.gigaGalFailed()) {
            if (gigaGal.getLives() > -1) {
                restartLevel();
            }
        }

        renderLevelEndOverlays();
        if (!levelEnded) {
            if (paused) {
                if (!optionsVisible) {
                    String[] optionStrings = {"RESUME", "EXIT", "OPTIONS"};
                    pauseOverlay.setOptionStrings(optionStrings);
                    pauseOverlay.render(batch, font, viewport, cursorOverlay);
                    if (inputControls.jumpButtonJustPressed && gigaGal.getAction() == Enums.Action.STANDING) {
                        gigaGal.toggleWeapon(Enums.Direction.DOWN); // enables gigagal to toggleWeapon weapon during pause without enabling other gigagal features
                    }
                    if (inputControls.shootButtonJustPressed) {
                        if (cursorOverlay.getPosition() == 73 && chaseCam.getFollowing()) {
                            unpause();
                        } else if (cursorOverlay.getPosition() == 58) {
                            unpause();
                            totalTime.suspend();
                            game.setScreen(new LevelSelectScreen(game));
                            this.dispose();
                            return;
                        } else if (cursorOverlay.getPosition() == 43) {
                            cursorOverlay.setRange(73, 28);
                            cursorOverlay.resetPosition();
                            cursorOverlay.update();
                            optionsVisible = true;
                        }
                    } else if (inputControls.pauseButtonJustPressed) {
                        unpause();
                    }
                } else {
                    String[] optionStrings = {"BACK", "DEBUG CAM", "TOUCH PAD", "QUIT"};
                    optionsOverlay.setOptionStrings(optionStrings);
                    optionsOverlay.render(batch, font, viewport, cursorOverlay);
                    if (inputControls.shootButtonJustPressed) {
                        if (cursorOverlay.getPosition() == 73) {
                            optionsVisible = false;
                        } else if (cursorOverlay.getPosition() == 58) {
                            if (!chaseCam.getFollowing()) {
                                optionsOverlay.isSingleOption(false);
                                chaseCam.setFollowing(true);
                            } else {
                                optionsOverlay.isSingleOption(true);
                                chaseCam.setFollowing(false);
                            }
                        } else if (cursorOverlay.getPosition() == 43) {
                            controlsOverlay.onMobile = Utils.toggleBoolean(controlsOverlay.onMobile);
                            prefs.putBoolean("Mobile", controlsOverlay.onMobile);
                        } else if (cursorOverlay.getPosition() == 28) {
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
            gaugeHud.render(renderer, viewport);
            indicatorHud.render(batch, font, viewport);
            controlsOverlay.render(batch, viewport);
        } else {
            return;
        }

        if (level.getLoadEx()) {
            messageOverlay.setMessage(Constants.LEVEL_KEY_MESSAGE);
            messageOverlay.render(batch, font, viewport);
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

            defeatOverlay.render(batch, font, viewport);
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
            victoryOverlay.render(batch, font, viewport);
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
        totalTime.stop();
        completedLevels.clear();
        inputControls.clear();
        victoryOverlay.dispose();
        defeatOverlay.dispose();
        pauseOverlay.dispose();
        optionsOverlay.dispose();
        indicatorHud.dispose();
        messageOverlay.dispose();
        gaugeHud.dispose();
        batch.dispose();
        level.dispose();
        totalTime = null;
        completedLevels = null;
        inputControls = null;
        totalTime = null;
        victoryOverlay = null;
        defeatOverlay = null;
        pauseOverlay = null;
        optionsOverlay = null;
        indicatorHud = null;
        messageOverlay = null;
        gaugeHud = null;
        batch = null;
        level = null;
        this.hide();
        super.dispose();
        System.gc();
    }

    public Level getLevel() { return level; }
    public int getTotalScore() { return totalScore; }
    public Timer getTotalTime() { return totalTime; }
    public ChaseCam getChaseCam() { return chaseCam; }
}