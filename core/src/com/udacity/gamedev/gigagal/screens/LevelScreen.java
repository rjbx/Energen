package com.udacity.gamedev.gigagal.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.udacity.gamedev.gigagal.app.Energraft;
import com.udacity.gamedev.gigagal.overlays.TouchInterface;
import com.udacity.gamedev.gigagal.util.InputControls;
import com.udacity.gamedev.gigagal.app.Level;
import com.udacity.gamedev.gigagal.entities.GigaGal;
import com.udacity.gamedev.gigagal.overlays.Menu;
import com.udacity.gamedev.gigagal.overlays.Message;
import com.udacity.gamedev.gigagal.overlays.Cursor;
import com.udacity.gamedev.gigagal.overlays.IndicatorHud;
import com.udacity.gamedev.gigagal.overlays.GaugeHud;
import com.udacity.gamedev.gigagal.util.ChaseCam;
import com.udacity.gamedev.gigagal.util.Constants;
import com.udacity.gamedev.gigagal.util.Enums;
import com.udacity.gamedev.gigagal.util.LevelLoader;
import com.udacity.gamedev.gigagal.util.Helpers;

import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.Arrays;

import static com.udacity.gamedev.gigagal.util.Enums.LevelMenu.DEBUG;
import static com.udacity.gamedev.gigagal.util.Enums.LevelMenu.MAIN;
import static com.udacity.gamedev.gigagal.util.Enums.LevelMenu.OPTIONS;

public class LevelScreen extends ScreenAdapter {

    // fields
    public static final String TAG = LevelScreen.class.getName();
    private static final LevelScreen INSTANCE = new LevelScreen();
    private static InputControls inputControls;
    private static TouchInterface touchInterface;
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
    private static Enums.LevelMenu menu;
    private boolean paused;
    private boolean viewingOptions;
    private boolean levelEnded;
    private boolean viewingDebug;
    private long pauseTime;
    private float pauseDuration;

    // cannot be subclassed
    private LevelScreen() {}

    // static factory method
    public static LevelScreen getInstance() { return INSTANCE; }

    public void create() {
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
        victoryOverlay = new Message();
        defeatOverlay = new Message();
        defeatOverlay.setMessage(Constants.DEFEAT_MESSAGE);
        inputControls = InputControls.getInstance();
        touchInterface = TouchInterface.getInstance();
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
        menu = MAIN;
    }

    private static void setOptionsMenu() {
        Cursor.getInstance().setRange(73, 28);
        Cursor.getInstance().setOrientation(Enums.Orientation.Y);
        Cursor.getInstance().resetPosition();
        Menu.getInstance().isSingleOption(false);
        String[] optionStrings = {"BACK", "DEBUG CAM", "TOUCH PAD", "QUIT"};
        Menu.getInstance().setOptionStrings(Arrays.asList(optionStrings));
        Menu.getInstance().setAlignment(Align.center);
        menu = OPTIONS;
    }

    public static void setDebugMenu() {
        Cursor.getInstance().setRange(80, 80);
        Menu.getInstance().isSingleOption(true);
        String[] option = {Constants.DEBUG_MODE_MESSAGE};
        Menu.getInstance().setOptionStrings(Arrays.asList(option));
        Menu.getInstance().setAlignment(Align.center);
        menu = Enums.LevelMenu.DEBUG;
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
        touchInterface.getViewport().update(width, height, true);
        touchInterface.recalculateButtonPositions();
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

        viewingDebug = !chaseCam.getFollowing();

        if (!(level.isGameOver() || level.isVictory())) {
            if (!paused) {
                IndicatorHud.getInstance().render(batch, font, viewport, level);
                level.update(delta);
                chaseCam.update(delta);
                level.render(batch); // also rendered when viewingDebug; see setPause()
                if (inputControls.pauseButtonJustPressed) {
                    level.getTime().suspend();
                    Energraft.getInstance().getTime().suspend();
                    paused = true;
                    pauseTime = TimeUtils.nanoTime();
                    pauseDuration = GigaGal.getInstance().getPauseTimeSeconds();
                    setMainMenu();
                }
            } else if (paused) {
                setPause();
            }
            GaugeHud.getInstance().render(renderer, viewport, GigaGal.getInstance());
            touchInterface.render(batch, viewport);
        } else {
            setLevelEnd();
        }

        if (level.getLoadEx()) {
            font.getData().setScale(.25f);
            errorMessage.render(batch, font, viewport, new Vector2(viewport.getWorldWidth() / 2, Constants.HUD_MARGIN - 5));
            font.getData().setScale(.4f);
        }
        inputControls.update();
    }

    private void setPause() {
        Menu.getInstance().render(batch, font, viewport, Cursor.getInstance());
      if (menu == MAIN) {
            if (inputControls.jumpButtonJustPressed && GigaGal.getInstance().getAction() == Enums.Action.STANDING) {
                GigaGal.getInstance().toggleWeapon(Enums.Direction.DOWN); // enables gigagal to toggleWeapon weapon during pause without enabling other gigagal features
            }
            if (inputControls.shootButtonJustPressed) {
                if (Cursor.getInstance().getPosition() == 73 && chaseCam.getFollowing()) {
                    unpause();
                } else if (Cursor.getInstance().getPosition() == 58) {
                    OverworldScreen.getInstance().setMainMenu();
                    Energraft.getInstance().setScreen(OverworldScreen.getInstance());
                    this.dispose();
                    return;
                } else if (Cursor.getInstance().getPosition() == 43) {
                    setOptionsMenu();
                    viewingOptions = true;
                }
            } else if (inputControls.pauseButtonJustPressed) {
                unpause();
            }
        } else if (menu == OPTIONS) {
            if (inputControls.shootButtonJustPressed) {
                if (Cursor.getInstance().getPosition() == 73) {
                    setMainMenu();
                } else if (Cursor.getInstance().getPosition() == 58) {
                    if (chaseCam.getFollowing()) {
                        chaseCam.setFollowing(false);
                        setDebugMenu();
                    }
                } else if (Cursor.getInstance().getPosition() == 43) {
                    touchInterface.onMobile = Helpers.toggleBoolean(touchInterface.onMobile);
                    Energraft.getInstance().getPreferences().putBoolean("Mobile", touchInterface.onMobile);
                } else if (Cursor.getInstance().getPosition() == 28) {
                    Energraft.getInstance().setScreen(LaunchScreen.getInstance());
                }
                viewingOptions = false;
            } else if (inputControls.pauseButtonJustPressed) {
                viewingOptions = false;
            }
        } else if (menu == DEBUG){
          level.render(batch);
          if (inputControls.shootButtonJustPressed) {
              chaseCam.setFollowing(true);
              viewingOptions = true;
              setOptionsMenu();
          }
        }
    }

    private void setLevelEnd() {
        if (level.isGameOver()) {
            if (levelEndOverlayStartTime == 0) {
                level.getTime().suspend();
                Energraft.getInstance().getTime().suspend();
                levelEndOverlayStartTime = TimeUtils.nanoTime();
            }
            font.getData().setScale(1);
            defeatOverlay.render(batch, font, viewport, new Vector2(viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2.5f));
            font.getData().setScale(.4f);
            if (Helpers.secondsSince(levelEndOverlayStartTime) > Constants.LEVEL_END_DURATION / 2) {
                levelEndOverlayStartTime = 0;
                OverworldScreen.getInstance().setMainMenu();
                Energraft.getInstance().setScreen(OverworldScreen.getInstance());
            }
        } else if (level.isVictory()) {
            if (levelEndOverlayStartTime == 0) {
                level.getTime().suspend();
                Energraft.getInstance().getTime().suspend();
                Energraft.getInstance().getPreferences().putInteger("Score", Energraft.getInstance().getScore() + level.getScore());
                Energraft.getInstance().getPreferences().putLong("Time", Energraft.getInstance().getTime().getNanoTime());
                Energraft.getInstance().getPreferences().flush();
                levelEndOverlayStartTime = TimeUtils.nanoTime();
                victoryOverlay.setMessage(Constants.VICTORY_MESSAGE + "\n\n\n" + "GAME TOTAL\n" + "Time: " + Helpers.stopWatchToString(Energraft.getInstance().getTime()) + "\nScore: " + Energraft.getInstance().getScore() + "\n\nLEVEL TOTAL\n" + "Time: " + Helpers.stopWatchToString(level.getTime()) + "\n" + "Score: " + level.getScore());
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
        Energraft.getInstance().getPreferences().flush();

        // set level attributes
        String weaponListString = GigaGal.getInstance().getWeaponList().toString();
        weaponListString = weaponListString.substring(1, weaponListString.length() - 1);
        Energraft.getInstance().getPreferences().putString("Weapons", weaponListString);
        chaseCam.camera = level.getViewport().getCamera();
        chaseCam.target = GigaGal.getInstance();
        level.getTime().reset().start();
        Energraft.getInstance().getTime().resume();
        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    private void levelComplete() {
        OverworldScreen.getInstance().setMainMenu();
        Energraft.getInstance().setScreen(OverworldScreen.getInstance());
    }

    private void unpause() {
        GigaGal.getInstance().setPauseTimeSeconds(Helpers.secondsSincePause(pauseTime) + pauseDuration);
        level.getTime().resume();
        Energraft.getInstance().getTime().resume();
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

    public void level(Enums.LevelName level) { this.levelName = level; }
}