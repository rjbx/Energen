package com.udacity.gamedev.gigagal.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
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
import com.udacity.gamedev.gigagal.util.Helpers;

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
    private Message errorMessage;
    private SpriteBatch batch;
    private ShapeRenderer renderer;
    private BitmapFont font;
    private ExtendViewport viewport;
    private long levelEndOverlayStartTime;
    private Enums.LevelName levelName;
    private static Enums.LevelMenu menu;
    private boolean paused;
    private long pauseTime;
    private float pauseDuration;

    // cannot be subclassed
    private LevelScreen() {}

    // static factory method
    public static LevelScreen getInstance() { return INSTANCE; }

    public void create() {
        paused = false;
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
        inputControls = InputControls.getInstance();
        touchInterface = TouchInterface.getInstance();

        paused = false;

        // : Use Gdx.input.setInputProcessor() to send touch events to inputControls
        Gdx.input.setInputProcessor(inputControls);
        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Level.getInstance().begin();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
//        gaugeHud.getViewport().update(width, height, true);
//        indicatorHud.getViewport().update(width, height, true);
//
//        defeatMessage.getViewport().update(width, height, true);
//        mainMenu.getViewport().update(width, height, true);
//        mainMenu.getCursor().getViewport().update(width, height, true);
//        optionsMenu.getViewport().update(width, height, true);
//        optionsMenu.getCursor().getViewport().update(width, height, true);
//        errorMessage.getViewport().update(width, height, true);
        Level.getInstance().getViewport().update(width, height, true);
        ChaseCam.getInstance().camera = Level.getInstance().getViewport().getCamera();
        touchInterface.getViewport().update(width, height, true);
        touchInterface.recalculateButtonPositions();
        GigaGal.getInstance().setInputControls(inputControls);
        ChaseCam.getInstance().setInputControls(inputControls);
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

    private static void setDebugMenu() {
        Cursor.getInstance().setRange(80, 80);
        Menu.getInstance().isSingleOption(true);
        String[] option = {Constants.DEBUG_MODE_MESSAGE};
        Menu.getInstance().setOptionStrings(Arrays.asList(option));
        Menu.getInstance().setAlignment(Align.center);
        menu = DEBUG;
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(
                Constants.BACKGROUND_COLOR.r,
                Constants.BACKGROUND_COLOR.g,
                Constants.BACKGROUND_COLOR.b,
                Constants.BACKGROUND_COLOR.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (Level.getInstance().continuing()) {
            if (!Level.getInstance().paused()) {
                IndicatorHud.getInstance().render(batch, font, viewport, Level.getInstance());
                Level.getInstance().update(delta);
                ChaseCam.getInstance().update(delta);
                Level.getInstance().render(batch); // also rendered when viewingDebug; see pause()
                if (inputControls.pauseButtonJustPressed) {
                    Level.getInstance().pause();
                    setMainMenu();
                }
            } else {
                showPauseMenu(delta);
            }
            GaugeHud.getInstance().render(renderer, viewport, GigaGal.getInstance());
            touchInterface.render(batch, viewport);
        } else {
            showExitOverlay();
        }

        if (Level.getInstance().getLoadEx()) {
            font.getData().setScale(.25f);
            errorMessage.render(batch, font, viewport, new Vector2(viewport.getWorldWidth() / 2, Constants.HUD_MARGIN - 5));
            font.getData().setScale(.4f);
        }
        inputControls.update();
    }

    private void showPauseMenu(float delta) {
        Menu.getInstance().render(batch, font, viewport, Cursor.getInstance());
        if (menu == MAIN) {
            if (inputControls.jumpButtonJustPressed && GigaGal.getInstance().getAction() == Enums.Action.STANDING) {
                GigaGal.getInstance().toggleWeapon(Enums.Direction.DOWN); // enables gigagal to toggleWeapon weapon during pause without enabling other gigagal features
            }
            if (inputControls.shootButtonJustPressed) {
                if (Cursor.getInstance().getPosition() == 73 && ChaseCam.getInstance().getFollowing()) {
                    Level.getInstance().unpause();
                } else if (Cursor.getInstance().getPosition() == 58) {
                    Level.getInstance().end();
                    OverworldScreen.getInstance().setMainMenu();
                    Energraft.getInstance().setScreen(OverworldScreen.getInstance());
                    return;
                } else if (Cursor.getInstance().getPosition() == 43) {
                    setOptionsMenu();
                }
            } else if (inputControls.pauseButtonJustPressed) {
                Level.getInstance().unpause();
            }
        } else if (menu == OPTIONS) {
            if (inputControls.shootButtonJustPressed) {
                if (Cursor.getInstance().getPosition() == 73) {
                    setMainMenu();
                } else if (Cursor.getInstance().getPosition() == 58) {
                    if (ChaseCam.getInstance().getFollowing()) {
                        ChaseCam.getInstance().setFollowing(false);
                        setDebugMenu();
                    }
                } else if (Cursor.getInstance().getPosition() == 43) {
                    touchInterface.onMobile = Helpers.toggleBoolean(touchInterface.onMobile);
                    Energraft.getInstance().getPreferences().putBoolean("Mobile", touchInterface.onMobile);
                } else if (Cursor.getInstance().getPosition() == 28) {
                    Level.getInstance().end();
                    Energraft.getInstance().create();
                    return;
                }
            } else if (inputControls.pauseButtonJustPressed) {
                setMainMenu();
            }
        } else if (menu == DEBUG){
            Level.getInstance().render(batch);
            ChaseCam.getInstance().update(delta);
            if (inputControls.shootButtonJustPressed) {
                ChaseCam.getInstance().setFollowing(true);
                setOptionsMenu();
            }
        }
    }

    private void showExitOverlay() {
        Message message = new Message();
        if (Level.getInstance().aborted()) {
            message.setMessage(Constants.DEFEAT_MESSAGE);
            font.getData().setScale(1);
            if (levelEndOverlayStartTime == 0) {
                Level.getInstance().getTime().suspend();
                levelEndOverlayStartTime = TimeUtils.nanoTime();
            }
            if (Helpers.secondsSince(levelEndOverlayStartTime) > Constants.LEVEL_END_DURATION / 2) {
                levelEndOverlayStartTime = 0;
                OverworldScreen.getInstance().setMainMenu();
                Energraft.getInstance().setScreen(OverworldScreen.getInstance());
                font.getData().setScale(.4f);
                Level.getInstance().end();
                return;
            }
        } else if (Level.getInstance().completed()) {
            message.setMessage(Constants.VICTORY_MESSAGE /*+ "\n\n\n" + "GAME TOTAL\n" + "Time: " + Helpers.stopWatchToString(Level.getInstance().getTime()) + "\nScore: " + Energraft.getInstance().getScore() + "\n\nLEVEL TOTAL\n" + "Time: " + Helpers.stopWatchToString(Level.getInstance().getTime()) + "\n" + "Score: " + Level.getInstance().getScore()*/);
            font.getData().setScale(1);
            if (levelEndOverlayStartTime == 0) {
                Level.getInstance().getTime().suspend();
                Energraft.getInstance().getPreferences().putInteger("Score", Energraft.getInstance().getScore() + Level.getInstance().getScore());
                Energraft.getInstance().getPreferences().putLong("Time", Level.getInstance().getTime().getNanoTime());
                Energraft.getInstance().getPreferences().flush();
                levelEndOverlayStartTime = TimeUtils.nanoTime();

            }
            if (Helpers.secondsSince(levelEndOverlayStartTime) > Constants.LEVEL_END_DURATION) {
                levelEndOverlayStartTime = 0;
                OverworldScreen.getInstance().setMainMenu();
                Energraft.getInstance().setScreen(OverworldScreen.getInstance());
                font.getData().setScale(.4f);
                Level.getInstance().end();
                return;
            }
        }
        message.render(batch, font, viewport, new Vector2(viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2.5f));
    }

    @Override
    public void dispose() {
//        totalTime.stop();
//        completedLevels.clear();
//        inputControls.clear();
//        victoryMessage.dispose();
//        defeatMessage.dispose();
//        indicatorHud.dispose();
//        errorMessage.dispose();
//        gaugeHud.dispose();
//        batch.dispose();
//        level.dispose();
//        totalTime = null;
//        completedLevels = null;
//        inputControls = null;
//        totalTime = null;
//        victoryMessage = null;
//        defeatMessage = null;
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