package com.udacity.gamedev.gigagal.app;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.udacity.gamedev.gigagal.entities.GigaGal;
import com.udacity.gamedev.gigagal.overlays.Menu;
import com.udacity.gamedev.gigagal.overlays.Message;
import com.udacity.gamedev.gigagal.overlays.OnscreenControls;
import com.udacity.gamedev.gigagal.overlays.Cursor;
import com.udacity.gamedev.gigagal.overlays.IndicatorHud;
import com.udacity.gamedev.gigagal.overlays.GaugeHud;
import com.udacity.gamedev.gigagal.util.ChaseCam;
import com.udacity.gamedev.gigagal.util.Constants;
import com.udacity.gamedev.gigagal.util.Enums;
import com.udacity.gamedev.gigagal.util.LevelLoader;
import com.udacity.gamedev.gigagal.util.Timer;
import com.udacity.gamedev.gigagal.util.Helpers;

import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class GameplayScreen extends ScreenAdapter {

    // fields
    public static final String TAG = GameplayScreen.class.getName();
    private static final GameplayScreen INSTANCE = new GameplayScreen();
    private static InputControls inputControls;
    private static OnscreenControls onscreenControls;
    private GigaGalGame game;
    private Preferences prefs;
    private Message victoryOverlay;
    private Message defeatOverlay;
    private Message errorMessage;
    private SpriteBatch batch;
    private ShapeRenderer renderer;
    private BitmapFont font;
    private ExtendViewport viewport;
    private long levelEndOverlayStartTime;
    private Level level;
    private ChaseCam chaseCam;
    private Enums.LevelName levelName;
    private int totalScore;
    private Timer totalTime;
    private boolean paused;
    private boolean viewingOptions;
    private boolean levelEnded;
    private boolean debugMode;
    private long pauseTime;
    private float pauseDuration;

    // cannot be subclassed
    private GameplayScreen() {}

    // static factory method
    public static GameplayScreen getInstance() { return INSTANCE; }

    public void create() {
        this.game = GigaGalGame.getInstance();
        prefs = game.getPreferences();
        totalTime = new Timer();
        paused = false;
        viewingOptions = false;
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
        errorMessage = new Message();
        errorMessage.setMessage(Constants.LEVEL_KEY_MESSAGE);
        victoryOverlay = new Message();defeatOverlay = new Message();
        defeatOverlay.setMessage(Constants.DEFEAT_MESSAGE);
        inputControls = InputControls.getInstance();
        onscreenControls = OnscreenControls.getInstance();
        chaseCam = ChaseCam.getInstance();

        // : Use Gdx.input.setInputProcessor() to send touch events to inputControls
        Gdx.input.setInputProcessor(inputControls);
        startNewLevel();
    }

    private static void setMainMenu() {
        Cursor.getInstance().setRange(73, 43);
        Cursor.getInstance().setOrientation(Enums.Orientation.Y);
        Cursor.getInstance().resetPosition();
        String[] optionStrings = {"RESUME", "EXIT", "OPTIONS"};
        Menu.getInstance().setOptionStrings(Arrays.asList(optionStrings));
        Menu.getInstance().setAlignment(Align.center);
    }

    private static void setOptionsMenu() {
        Cursor.getInstance().setRange(73, 28);
        Cursor.getInstance().setOrientation(Enums.Orientation.Y);
        Cursor.getInstance().resetPosition();
        Menu.getInstance().isSingleOption(false);
        String[] optionStrings = {"BACK", "DEBUG CAM", "TOUCH PAD", "QUIT"};
        Menu.getInstance().setOptionStrings(Arrays.asList(optionStrings));
        Menu.getInstance().setAlignment(Align.center);
    }

    public static void setDebugMenu() {
        Cursor.getInstance().setRange(80, 80);
        Menu.getInstance().isSingleOption(true);
        String[] option = {Constants.DEBUG_MODE_MESSAGE};
        Menu.getInstance().setOptionStrings(Arrays.asList(option));
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);


//        gaugeHud.getViewport().update(width, height, true);
//        indicatorHud.getViewport().update(width, height, true);
//
//        defeatOverlay.getViewport().update(width, height, true);
//        mainMenu.getViewport().update(width, height, true);
//        mainMenu.getCursor().getViewport().update(width, height, true);
//        optionsMenu.getViewport().update(width, height, true);
//        optionsMenu.getCursor().getViewport().update(width, height, true);
//        errorMessage.getViewport().update(width, height, true);
        level.getViewport().update(width, height, true);
        chaseCam.camera = level.getViewport().getCamera();
        onscreenControls.getViewport().update(width, height, true);
        onscreenControls.recalculateButtonPositions();
        GigaGal.getInstance().setInputControls(inputControls);
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

        if (level.gigaGalFailed()) {
            if (GigaGal.getInstance().getLives() > -1) {
                restartLevel();
            }
        }

        if (!(level.isGameOver() || level.isVictory())) {
            if (paused) {
                renderPause();
            }
            if (!paused || (paused && !chaseCam.getFollowing())) {
                chaseCam.update(delta);
                level.render(batch);
            }
            if (inputControls.pauseButtonJustPressed && chaseCam.getFollowing()) {
                level.getLevelTime().suspend();
                totalTime.suspend();
                paused = true;
                pauseTime = TimeUtils.nanoTime();
                pauseDuration = GigaGal.getInstance().getPauseTimeSeconds();
                setMainMenu();
            }
            if (chaseCam.getFollowing()) {
                level.update(delta);
                GaugeHud.getInstance().render(renderer, viewport, GigaGal.getInstance());
                IndicatorHud.getInstance().render(batch, font, viewport, level);
            }
            onscreenControls.render(batch, viewport);
        } else {
            renderLevelEnd();
            return;
        }

        if (level.getLoadEx()) {
            font.getData().setScale(.25f);
            errorMessage.render(batch, font, viewport, new Vector2(viewport.getWorldWidth() / 2, Constants.HUD_MARGIN - 5));
            font.getData().setScale(.4f);
        }
        inputControls.update();
    }

    private void renderPause() {
        if (!viewingOptions) {
            Menu.getInstance().render(batch, font, viewport, Cursor.getInstance());
            if (inputControls.jumpButtonJustPressed && GigaGal.getInstance().getAction() == Enums.Action.STANDING) {
                GigaGal.getInstance().toggleWeapon(Enums.Direction.DOWN); // enables gigagal to toggleWeapon weapon during pause without enabling other gigagal features
            }
            if (inputControls.shootButtonJustPressed) {
                if (Cursor.getInstance().getPosition() == 73 && chaseCam.getFollowing()) {
                    unpause();
                } else if (Cursor.getInstance().getPosition() == 58) {
                    unpause();
                    totalTime.suspend();
                    LevelSelectScreen.getInstance().create();
                    game.setScreen(LevelSelectScreen.getInstance());
                    this.dispose();
                    return;
                } else if (Cursor.getInstance().getPosition() == 43) {
                    setOptionsMenu();
                    viewingOptions = true;
                }
            } else if (inputControls.pauseButtonJustPressed) {
                unpause();
            }
        } else {
            if (chaseCam.getFollowing()) {
                Menu.getInstance().render(batch, font, viewport, Cursor.getInstance());
                if (inputControls.shootButtonJustPressed) {
                    if (Cursor.getInstance().getPosition() == 73) {
                        viewingOptions = false;
                        setMainMenu();
                    } else if (Cursor.getInstance().getPosition() == 58) {
                        if (chaseCam.getFollowing()) {
                            chaseCam.setFollowing(false);
                            setDebugMenu();
                        }
                    } else if (Cursor.getInstance().getPosition() == 43) {
                        onscreenControls.onMobile = Helpers.toggleBoolean(onscreenControls.onMobile);
                        prefs.putBoolean("Mobile", onscreenControls.onMobile);
                    } else if (Cursor.getInstance().getPosition() == 28) {
                        game.dispose();
                        game.create();
                        return;
                    }
                } else if (inputControls.pauseButtonJustPressed) {
                    viewingOptions = false;
                }
            } else {
                Menu.getInstance().render(batch, font, viewport, Cursor.getInstance());
                if (inputControls.shootButtonJustPressed) {
                    chaseCam.setFollowing(true);
                    setOptionsMenu();
                }
            }
        }
    }

    private void renderLevelEnd() {
        if (level.isGameOver()) {
            if (levelEndOverlayStartTime == 0) {
                level.getLevelTime().suspend();
                totalTime.suspend();
                levelEndOverlayStartTime = TimeUtils.nanoTime();
            }
            font.getData().setScale(1);
            defeatOverlay.render(batch, font, viewport, new Vector2(viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2.5f));
            font.getData().setScale(.4f);
            if (Helpers.secondsSince(levelEndOverlayStartTime) > Constants.LEVEL_END_DURATION / 2) {
                levelEndOverlayStartTime = 0;
                LevelSelectScreen.getInstance().create();
                game.setScreen(LevelSelectScreen.getInstance());
                this.dispose();
                return;
            }
        } else if (level.isVictory()) {
            if (levelEndOverlayStartTime == 0) {
                level.getLevelTime().suspend();
                totalTime.suspend();
                totalScore += level.getLevelScore();
                game.getPreferences().putInteger("Score", totalScore);
                game.getPreferences().putLong("Time", totalTime.getNanoTime());
                game.getPreferences().flush();
                levelEndOverlayStartTime = TimeUtils.nanoTime();
                victoryOverlay.setMessage(Constants.VICTORY_MESSAGE + "\n\n\n" + "GAME TOTAL\n" + "Time: " + Helpers.stopWatchToString(getTotalTime()) + "\nScore: " + getTotalScore() + "\n\nLEVEL TOTAL\n" + "Time: " + Helpers.stopWatchToString(getLevel().getLevelTime()) + "\n" + "Score: " + getLevel().getLevelScore());
            }
            victoryOverlay.render(batch, font, viewport, new Vector2(viewport.getWorldWidth() / 2, viewport.getWorldHeight() * .9f));
            if (Helpers.secondsSince(levelEndOverlayStartTime) > Constants.LEVEL_END_DURATION) {
                levelEndOverlayStartTime = 0;
                levelComplete();
            }
        }
    }

    public void readLevelFile() throws IOException, ParseException, GdxRuntimeException {
        level = LevelLoader.load("levels/" + levelName + ".dt");
    }

    private void startNewLevel() {
//      level = Level.debugLevel();
//      String levelName = Constants.LEVELS[levelNumber];
        GigaGal.getInstance().respawn();

        // get prefs
        totalScore = game.getPreferences().getInteger("Score", totalScore);
        totalTime.start(game.getPreferences().getLong("Time", totalTime.getNanoTime()));
        totalTime.suspend();
        game.getPreferences().flush();

        // set level attributes
        String weaponListString = GigaGal.getInstance().getWeaponList().toString();
        weaponListString = weaponListString.substring(1, weaponListString.length() - 1);
        game.getPreferences().putString("Weapons", weaponListString);
        chaseCam.camera = level.getViewport().getCamera();
        chaseCam.target = GigaGal.getInstance();
        totalTime.resume();
        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    private void restartLevel() {
        GigaGal.getInstance().respawn();
    }

    private void levelComplete() {
        LevelSelectScreen.getInstance().create();
        game.setScreen(LevelSelectScreen.getInstance());
        this.dispose();
        return;
    }

    private void unpause() {
        GigaGal.getInstance().setPauseTimeSeconds(Helpers.secondsSincePause(pauseTime) + pauseDuration);
        level.getLevelTime().resume();
        totalTime.resume();
        paused = false;
    }

    @Override
    public void dispose() {
//        totalTime.stop();
//        completedLevels.clear();
//        inputControls.clear();
//        victoryOverlay.dispose();
//        defeatOverlay.dispose();
//        indicatorHud.dispose();
//        errorMessage.dispose();
//        gaugeHud.dispose();
//        batch.dispose();
//        level.dispose();
//        totalTime = null;
//        completedLevels = null;
//        inputControls = null;
//        totalTime = null;
//        victoryOverlay = null;
//        defeatOverlay = null;
//        indicatorHud = null;
//        errorMessage = null;
//        gaugeHud = null;
//        batch = null;
//        level = null;
//        this.hide();
//        super.dispose();
//        System.gc();
    }

    public int getTotalScore() { return totalScore; }
    public Timer getTotalTime() { return totalTime; }
    public ChaseCam getChaseCam() { return chaseCam; }
    public Level getLevel() { return level; }
    public void setLevel(Enums.LevelName level) { this.levelName = level; }
}