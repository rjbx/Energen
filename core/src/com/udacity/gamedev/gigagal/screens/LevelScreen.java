package com.udacity.gamedev.gigagal.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.udacity.gamedev.gigagal.app.Energraft;
import com.udacity.gamedev.gigagal.overlays.TouchInterface;
import com.udacity.gamedev.gigagal.util.InputControls;
import com.udacity.gamedev.gigagal.app.Level;
import com.udacity.gamedev.gigagal.entities.GigaGal;
import com.udacity.gamedev.gigagal.overlays.Menu;
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
    private SpriteBatch batch;
    private ShapeRenderer renderer;
    private BitmapFont font;
    private ExtendViewport viewport;
    private long levelEndOverlayStartTime;
    private static Enums.LevelMenu menu;

    // cannot be subclassed
    private LevelScreen() {}

    // static factory method
    public static LevelScreen getInstance() { return INSTANCE; }

    public void create() {}

    @Override
    public void show() {
        batch = new SpriteBatch(); // shared by all overlays instantiated from this class
        renderer = new ShapeRenderer(); // shared by all overlays instantiated from this class
        renderer.setAutoShapeType(true);
        font = new BitmapFont(Gdx.files.internal(Constants.FONT_FILE)); // shared by all overlays instantiated from this class
        font.getData().setScale(.4f); // shared by all overlays instantiated from this class
        viewport = new ExtendViewport(Constants.WORLD_SIZE, Constants.WORLD_SIZE); // shared by all overlays instantiated from this class

        // : Use Gdx.input.setInputProcessor() to send touch events to inputControls
        Gdx.input.setInputProcessor(InputControls.getInstance());
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
        TouchInterface.getInstance().getViewport().update(width, height, true);
        TouchInterface.getInstance().recalculateButtonPositions();
        GigaGal.getInstance().setInputControls(InputControls.getInstance());
        ChaseCam.getInstance().setInputControls(InputControls.getInstance());
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
                Level.getInstance().render(batch, viewport); // also rendered when viewingDebug; see pause()
                if (InputControls.getInstance().pauseButtonJustPressed) {
                    Level.getInstance().pause();
                    setMainMenu();
                }
            } else {

                showPauseMenu(delta);
            }
            GaugeHud.getInstance().render(renderer, viewport, GigaGal.getInstance());
            TouchInterface.getInstance().render(batch, viewport);
        } else {
            showExitOverlay();
        }

        if (Level.getInstance().hasLoadEx() || Level.getInstance().hasRunEx()) {
            font.getData().setScale(.25f);
            Helpers.drawBitmapFont(batch, viewport, font, Constants.LEVEL_KEY_MESSAGE, viewport.getWorldWidth() / 2, Constants.HUD_MARGIN - 5, Align.center);
            font.getData().setScale(.4f);
        }
        InputControls.getInstance().update();
    }

    private void showPauseMenu(float delta) {
        Menu.getInstance().render(batch, font, viewport, Cursor.getInstance());
        if (menu == MAIN) {
            String gauges = Constants.HUD_AMMO_LABEL + GigaGal.getInstance().getAmmo() + "\n" +
                    Constants.HUD_HEALTH_LABEL + GigaGal.getInstance().getHealth() + "\n" +
                    "Turbo: " + GigaGal.getInstance().getTurbo();
            String totals = "GAME TOTAL\n" + "Time: " + Helpers.secondsToString(Level.getInstance().getTime()) + "\n" + "Score: " + Level.getInstance().getScore();
            String weapons = GigaGal.getInstance().getWeaponList().toString().replaceAll(", ", "\n");
            weapons = weapons.substring(1, weapons.length() - 1);
            batch.setProjectionMatrix(viewport.getCamera().combined);
            batch.begin();
            font.draw(batch, gauges, viewport.getScreenX() + 5, viewport.getWorldHeight() * .8f, 0, Align.left, false);
            font.draw(batch, totals, viewport.getWorldWidth() / 2, viewport.getWorldHeight() * .8f, 0, Align.center, false);
            font.draw(batch, weapons, viewport.getWorldWidth() - Constants.HUD_MARGIN, viewport.getWorldHeight() * .8f, 0, weapons.length(), 10, Align.right, false);
            batch.end();
            if (InputControls.getInstance().jumpButtonJustPressed && GigaGal.getInstance().getAction() == Enums.Action.STANDING) {
                GigaGal.getInstance().toggleWeapon(Enums.Direction.DOWN); // enables gigagal to toggleWeapon weapon during pause without enabling other gigagal features
            }
            if (InputControls.getInstance().shootButtonJustPressed) {
                if (Cursor.getInstance().getPosition() == 73 && ChaseCam.getInstance().getFollowing()) {
                    Level.getInstance().unpause();
                } else if (Cursor.getInstance().getPosition() == 58) {
                    OverworldScreen.getInstance().setMainMenu();
                    Energraft.getInstance().setScreen(OverworldScreen.getInstance());
                    Level.getInstance().unpause();
                    Level.getInstance().end();
                    return;
                } else if (Cursor.getInstance().getPosition() == 43) {
                    setOptionsMenu();
                }
            } else if (InputControls.getInstance().pauseButtonJustPressed) {
                Level.getInstance().unpause();
            }
        } else if (menu == OPTIONS) {
            if (InputControls.getInstance().shootButtonJustPressed) {
                if (Cursor.getInstance().getPosition() == 73) {
                    setMainMenu();
                } else if (Cursor.getInstance().getPosition() == 58) {
                    if (ChaseCam.getInstance().getFollowing()) {
                        ChaseCam.getInstance().setFollowing(false);
                        setDebugMenu();
                    }
                } else if (Cursor.getInstance().getPosition() == 43) {
                    TouchInterface.getInstance().onMobile = Helpers.toggleBoolean(TouchInterface.getInstance().onMobile);
                    Energraft.getInstance().getPreferences().putBoolean("Mobile", TouchInterface.getInstance().onMobile);
                } else if (Cursor.getInstance().getPosition() == 28) {
                    Level.getInstance().unpause();
                    Level.getInstance().end();
                    Energraft.getInstance().create();
                    return;
                }
            } else if (InputControls.getInstance().pauseButtonJustPressed) {
                setMainMenu();
            }
        } else if (menu == DEBUG){
            Level.getInstance().render(batch, viewport);
            ChaseCam.getInstance().update(delta);
            if (InputControls.getInstance().shootButtonJustPressed) {
                ChaseCam.getInstance().setFollowing(true);
                setOptionsMenu();
            }
        }
    }

    private void showExitOverlay() {
        String endMessage = "";
        if (Level.getInstance().failed()) {
            endMessage = Constants.DEFEAT_MESSAGE;
            font.getData().setScale(.6f);
            if (levelEndOverlayStartTime == 0) {
                Level.getInstance().end();
                levelEndOverlayStartTime = TimeUtils.nanoTime();
            }
            if (Helpers.secondsSince(levelEndOverlayStartTime) > Constants.LEVEL_END_DURATION / 2) {
                levelEndOverlayStartTime = 0;
                OverworldScreen.getInstance().setMainMenu();
                Energraft.getInstance().setScreen(OverworldScreen.getInstance());
                font.getData().setScale(.4f);
                return;
            }
        } else if (Level.getInstance().completed()) {
            endMessage = Constants.VICTORY_MESSAGE + "\n\n\n" + "GAME TOTAL\n" + "Time: " + Helpers.secondsToString(Energraft.getInstance().getTime()) + "\nScore: " + Energraft.getInstance().getScore() + "\n\nLEVEL TOTAL\n" + "Time: " + Helpers.secondsToString(Level.getInstance().getTime()) + "\n" + "Score: " + Level.getInstance().getScore();
            if (levelEndOverlayStartTime == 0) {
                Level.getInstance().end();
                levelEndOverlayStartTime = TimeUtils.nanoTime();
            }
            if (Helpers.secondsSince(levelEndOverlayStartTime) > Constants.LEVEL_END_DURATION) {
                levelEndOverlayStartTime = 0;
                OverworldScreen.getInstance().setMainMenu();
                Energraft.getInstance().setScreen(OverworldScreen.getInstance());
                return;
            }
        }
        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();
        font.draw(batch, endMessage, viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 1.25f, 0, Align.center, false);
        batch.end();
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
}