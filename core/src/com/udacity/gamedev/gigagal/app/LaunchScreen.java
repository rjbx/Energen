package com.udacity.gamedev.gigagal.app;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.udacity.gamedev.gigagal.overlay.*;
import com.udacity.gamedev.gigagal.util.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// package-private singleton
final class LaunchScreen extends ScreenAdapter {

    // fields
    public static final String TAG = LaunchScreen.class.getName();
    private static final LaunchScreen INSTANCE = new LaunchScreen();
    private ScreenManager screenManager;
    private OverworldScreen overworldScreen;
    private SpriteBatch batch;
    private ExtendViewport viewport;
    private AssetManager assetManager;
    private BitmapFont font;
    private BitmapFont text;
    private BitmapFont title;
    private static InputControls inputControls;
    private static TouchInterface touchInterface;
    private static Cursor cursor;
    private static Menu menu;
    private static Enums.MenuType menuType;
    private Backdrop launchBackdrop;
    private List<String> choices;
    private long launchStartTime;
    private boolean launching;
    private boolean continuing;
    private Vector2 gigagalCenter;

    // cannot be subclassed
    private LaunchScreen() {}

    // static factory method
    protected static LaunchScreen getInstance() { return INSTANCE; }

    @Override
    public void show() {
        screenManager = ScreenManager.getInstance();

        batch = screenManager.getBatch();

        viewport = StaticCam.getInstance().getViewport();

        assetManager = AssetManager.getInstance();
        font = assetManager.getFontAssets().message;
        title = assetManager.getFontAssets().title;
        text = assetManager.getFontAssets().menu;

        touchInterface = TouchInterface.getInstance();

        inputControls = InputControls.getInstance();
        Gdx.input.setInputProcessor(inputControls); // sends touch events to inputControls

        cursor = Cursor.getInstance();
        menu = Menu.getInstance();

        overworldScreen = OverworldScreen.getInstance();

        launchBackdrop = new Backdrop(assetManager.getOverlayAssets().logo);

        gigagalCenter = new Vector2(Constants.AVATAR_STANCE_WIDTH / 2, Constants.AVATAR_HEIGHT / 2);
        choices = new ArrayList<String>();
        launchStartTime = TimeUtils.nanoTime();
        launching = true;
        continuing = SaveData.getDifficulty() != -1;
        choices.add("NO");
        choices.add("YES");

        if (Gdx.app.getType() == Application.ApplicationType.Android || Gdx.app.getType() == Application.ApplicationType.iOS) {
            SaveData.setTouchscreen(true);
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        touchInterface.getViewport().update(width, height, true);
        touchInterface.recalculateButtonPositions();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        if (!launching) {
            switch(menuType) {
                case START:
                    final Vector2 gigagalPosition = new Vector2(viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2.5f);

                    Helpers.drawTextureRegion(batch, viewport, assetManager.getOverlayAssets().globe, viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 1.625f, Constants.GLOBE_CENTER.x, Constants.GLOBE_CENTER.y);
                    Helpers.drawTextureRegion(batch, viewport, assetManager.getGigaGalAssets().fallRight, gigagalPosition, gigagalCenter);
                    Helpers.drawTextureRegion(batch, viewport, assetManager.getOverlayAssets().beast, viewport.getWorldWidth() / 3, viewport.getWorldHeight() / 1.625f, Constants.BEAST_CENTER.x, Constants.BEAST_CENTER.y);
                    Helpers.drawBitmapFont(batch, viewport, title, "ENERGRAFT", viewport.getWorldWidth() / 2, viewport.getWorldHeight() - Constants.HUD_MARGIN, Align.center);

                    menu.render(batch, font, viewport, Cursor.getInstance());

                    if (inputControls.shootButtonJustPressed) {
                        assetManager.getMusicAssets().intro.stop();
                        if (continuing) {
                            if (cursor.getPosition() == 35) {
                                inputControls.shootButtonJustPressed = false;
                                screenManager.setScreen(overworldScreen);
                                this.dispose();
                                return;
                            } else if (cursor.getPosition() == 20) {
                                setEraseMenu();
                            }
                        } else {
                            setDifficultyMenu();
                        }
                    }
                    break;
                case ERASE:
                    menu.render(batch, font, viewport, Cursor.getInstance());
                    if (inputControls.shootButtonJustPressed) {
                        if (cursor.getPosition() == (150)) {
                            SaveData.erase();
                            screenManager.dispose();
                            screenManager.create();
                            setBeginMenu();
                        } else {
                            setResumeMenu();
                        }
                    }
                    break;
                case DIFFICULTY:
                    menu.render(batch, font, viewport, Cursor.getInstance());
                    if (inputControls.shootButtonJustPressed) {
                        if (cursor.getPosition() == 75) {
                            SaveData.setDifficulty(0);
                        } else if (cursor.getPosition() == 60) {
                            SaveData.setDifficulty(1);
                        } else if (cursor.getPosition() == 45) {
                            SaveData.setDifficulty(2);
                        }
                        OverworldScreen overworldScreen = OverworldScreen.getInstance();
                        screenManager.setScreen(overworldScreen);
                        this.dispose();
                        return;
                    }
                    break;
            }
        } else {
            launchBackdrop.render(batch, viewport,
                    new Vector2(viewport.getWorldWidth() / 2, viewport.getWorldHeight() * .625f),
                    new Vector2(Constants.LOGO_CENTER.x * .375f, Constants.LOGO_CENTER.y * .375f), .375f);
            Helpers.drawBitmapFont(batch, viewport, font, Constants.LAUNCH_MESSAGE, viewport.getWorldWidth() / 2, Constants.HUD_MARGIN, Align.center);
            if (Helpers.secondsSince(launchStartTime) > 3) {
                assetManager.getMusicAssets().intro.play();
                assetManager.getMusicAssets().intro.setLooping(true);
                launching = false;
                if (continuing) {
                    setResumeMenu();
                } else {
                    setBeginMenu();
                }
            }
        }
        inputControls.update();
        touchInterface.render(batch);
    }

    private static void setResumeMenu() {
        cursor.setRange(35, 20);
        cursor.setOrientation(Enums.Orientation.Y);
        cursor.resetPosition();
        String[] optionStrings = {"START GAME", "ERASE GAME"};
        menu.clearStrings();
        menu.setOptionStrings(Arrays.asList(optionStrings));
        menu.TextAlignment(Align.center);
        menuType = Enums.MenuType.START;
    }

    private static void setEraseMenu() {
        cursor.setRange(50, 150);
        cursor.setOrientation(Enums.Orientation.X);
        cursor.resetPosition();
        String[] optionStrings = {"NO", "YES"};
        menu.setOptionStrings(Arrays.asList(optionStrings));
        menu.TextAlignment(Align.center);
        menu.setPromptString(Align.center, "Are you sure you want to start \na new game and erase all saved data?");
        menuType = Enums.MenuType.ERASE;
    }

    private static void setBeginMenu() {
        cursor.setRange(30, 30);
        menu.isSingleOption(true);
        String[] option = {"PRESS START"};
        menu.setOptionStrings(Arrays.asList(option));
        menu.TextAlignment(Align.center);
        menuType = Enums.MenuType.START;
    }

    private static void setDifficultyMenu() {
        cursor.setRange(75, 35);
        cursor.setOrientation(Enums.Orientation.Y);
        cursor.resetPosition();
        String[] optionStrings = {"NORMAL", "HARD", "VERY HARD"};
        menu.setOptionStrings(Arrays.asList(optionStrings));
        menu.isSingleOption(false);
        menuType = Enums.MenuType.DIFFICULTY;
    }

    @Override
    public void dispose() {
//        choices.clear();
//        inputControls.clear();
//        text.dispose();
//        title.dispose();
//        choices = null;
//        inputControls = null;
//        launchBackdrop = null;
//        text = null;
//        title = null;
//        batch = null;
//        this.hide();
//        super.dispose();
    }
}