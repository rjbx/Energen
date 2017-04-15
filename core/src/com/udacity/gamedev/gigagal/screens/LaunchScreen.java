package com.udacity.gamedev.gigagal.screens;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.udacity.gamedev.gigagal.app.Energraft;
import com.udacity.gamedev.gigagal.overlays.TouchInterface;
import com.udacity.gamedev.gigagal.util.InputControls;
import com.udacity.gamedev.gigagal.overlays.Cursor;
import com.udacity.gamedev.gigagal.overlays.Backdrop;
import com.udacity.gamedev.gigagal.overlays.Menu;
import com.udacity.gamedev.gigagal.util.Assets;
import com.udacity.gamedev.gigagal.util.Constants;
import com.udacity.gamedev.gigagal.util.Enums;
import com.udacity.gamedev.gigagal.util.Helpers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// immutable
public final class LaunchScreen extends ScreenAdapter {

    // fields
    public static final String TAG = LaunchScreen.class.getName();
    private static final LaunchScreen INSTANCE = new LaunchScreen();
    private static InputControls inputControls;
    private static TouchInterface touchInterface;
    private SpriteBatch batch;
    private ExtendViewport viewport;
    private BitmapFont font;
    private BitmapFont text;
    private BitmapFont title;
    private Backdrop launchBackdrop;
    private List<String> choices;
    private long launchStartTime;
    private boolean launching;
    private boolean continuing;
    private boolean difficultyOptionsVisible;
    private boolean promptVisible;
    private Vector2 gigagalCenter;

    // cannot be subclassed
    private LaunchScreen() {}

    // static factory method
    public static LaunchScreen getInstance() { return INSTANCE; }

    public void create() {
        this.viewport = new ExtendViewport(Constants.WORLD_SIZE, Constants.WORLD_SIZE);
        gigagalCenter = new Vector2(Constants.GIGAGAL_STANCE_WIDTH / 2, Constants.GIGAGAL_HEIGHT / 2);
        text = new BitmapFont(Gdx.files.internal(Constants.FONT_FILE));
        text.getData().setScale(0.5f);
        title = new BitmapFont(Gdx.files.internal(Constants.FONT_FILE));
        title.getData().setScale(1);
        title.setColor(Color.SKY);
        choices = new ArrayList<String>();
        launchStartTime = TimeUtils.nanoTime();
        launching = true;
        continuing = (Energraft.getInstance().getPreferences().getLong("Time", 0) != 0);
        choices.add("NO");
        choices.add("YES");
    }

    @Override
    public void show() {
        // : When you're done testing, use onMobile() turn off the controls when not on a mobile device
        // onMobile();
        difficultyOptionsVisible = false;
        promptVisible = false;
        batch = new SpriteBatch();
        font = new BitmapFont(Gdx.files.internal(Constants.FONT_FILE)); // shared by all overlays instantiated from this class
        font.getData().setScale(.4f); // shared by all overlays instantiated from this class
        viewport = new ExtendViewport(Constants.WORLD_SIZE, Constants.WORLD_SIZE); // shared by all overlays instantiated from this class
        launchBackdrop = new Backdrop();
        inputControls = InputControls.getInstance();
        touchInterface = TouchInterface.getInstance();
        Gdx.input.setInputProcessor(inputControls);
    }

    public static void setResumeMenu() {
        Cursor.getInstance().setRange(35, 20);
        Cursor.getInstance().setOrientation(Enums.Orientation.Y);
        Cursor.getInstance().resetPosition();
        String[] optionStrings = {"START GAME", "ERASE GAME"};
        Menu.getInstance().setOptionStrings(Arrays.asList(optionStrings));
        Menu.getInstance().setAlignment(Align.center);
        Menu.getInstance().setPromptString(" ");
    }

    public static void setEraseMenu() {
        Cursor.getInstance().setRange(50, 150);
        Cursor.getInstance().setOrientation(Enums.Orientation.X);
        Cursor.getInstance().resetPosition();
        String[] optionStrings = {"NO", "YES"};
        Menu.getInstance().setOptionStrings(Arrays.asList(optionStrings));
        Menu.getInstance().setAlignment(Align.center);
        Menu.getInstance().setPromptString("Are you sure you want to start \na new game and erase all saved data?");
    }

    public static void setBeginMenu() {
        Cursor.getInstance().setRange(30, 30);
        Menu.getInstance().isSingleOption(true);
        String[] option = {"PRESS START"};
        Menu.getInstance().setOptionStrings(Arrays.asList(option));
        Menu.getInstance().setAlignment(Align.center);
    }

    public static void setDifficultyMenu() {
        Cursor.getInstance().setRange(75, 35);
        Cursor.getInstance().setOrientation(Enums.Orientation.Y);
        Cursor.getInstance().resetPosition();
        String[] optionStrings = {"NORMAL", "HARD", "VERY HARD"};
        Menu.getInstance().setOptionStrings(Arrays.asList(optionStrings));
        Menu.getInstance().isSingleOption(false);
    }

    private boolean onMobile() {
        return Gdx.app.getType() == Application.ApplicationType.Android || Gdx.app.getType() == Application.ApplicationType.iOS;
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);

//        cursor.getViewport().update(width, height, true);
//        touchInterface.getViewport().update(width, height, true);
//        touchInterface.recalculateButtonPositions();
//        startMenu.getViewport().update(width, height, true);
//        startMenu.getCursor().getViewport().update(width, height, true);
//        difficultyMenu.getViewport().update(width, height, true);
//        difficultyMenu.getCursor().getViewport().update(width, height, true);
//        eraseMenu.getViewport().update(width, height, true);
//        eraseMenu.getCursor().getViewport().update(width, height, true);
//        launchBackdrop.getViewport().update(width, height, true);
    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        if (!difficultyOptionsVisible) {
            if (!launching) {
                if (!promptVisible) {
                    Helpers.drawBitmapFont(batch, viewport, title, "ENERGRAFT", viewport.getWorldWidth() / 2, viewport.getWorldHeight() - Constants.HUD_MARGIN, Align.center);
                    final Vector2 gigagalPosition = new Vector2(viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2);
                    Helpers.drawTextureRegion(batch, viewport, Assets.getInstance().getGigaGalAssets().fallRight, gigagalPosition, gigagalCenter);

                    Menu.getInstance().render(batch, font, viewport, Cursor.getInstance());

                    if (inputControls.shootButtonJustPressed) {
                        if (continuing) {
                            if (Cursor.getInstance().getPosition() == 35) {
                                inputControls.shootButtonJustPressed = false;
                                OverworldScreen overworldScreen = OverworldScreen.getInstance();
                                overworldScreen.create();
                                Energraft.getInstance().setScreen(overworldScreen);
                                this.dispose();
                                return;
                            } else if (Cursor.getInstance().getPosition() == 20) {
                                setEraseMenu();
                                promptVisible = true;
                            }
                        } else {
                            difficultyOptionsVisible = true;
                            setDifficultyMenu();
                        }
                    }
                } else {
                    Menu.getInstance().render(batch, font, viewport, Cursor.getInstance());
                    if (inputControls.shootButtonJustPressed) {
                        if (Cursor.getInstance().getPosition() == (150)) {
                            Energraft.getInstance().getPreferences().clear();
                            Energraft.getInstance().getPreferences().flush();
                            Energraft.getInstance().dispose();
                            Energraft.getInstance().create();
                            setBeginMenu();
                        } else {
                            setResumeMenu();
                        }
                        promptVisible = false;
                    }
                }
            } else {
                launchBackdrop.render(batch, viewport, Assets.getInstance().getOverlayAssets().logo,
                        new Vector2(viewport.getWorldWidth() / 2, viewport.getWorldHeight() * .625f),
                        new Vector2(Constants.LOGO_CENTER.x * .375f, Constants.LOGO_CENTER.y * .375f));
                Helpers.drawBitmapFont(batch, viewport, font, Constants.LAUNCH_MESSAGE, viewport.getWorldWidth() / 2, Constants.HUD_MARGIN, Align.center);
            }
            if (launching) {
                if (Helpers.secondsSince(launchStartTime) > 3) {
                    launching = false;
                    if (continuing) {
                        setResumeMenu();
                    } else {
                        setBeginMenu();
                    }
                }
            }
        } else {
            Menu.getInstance().render(batch, font, viewport, Cursor.getInstance());
            if (inputControls.shootButtonJustPressed) {
                if (Cursor.getInstance().getPosition() == 75) {
                    Energraft.getInstance().getPreferences().putInteger("Difficulty", 0);
                } else if (Cursor.getInstance().getPosition() == 60) {
                    Energraft.getInstance().getPreferences().putInteger("Difficulty", 1);
                } else if (Cursor.getInstance().getPosition() == 45) {
                    Energraft.getInstance().getPreferences().putInteger("Difficulty", 2);
                }

                if (Energraft.getInstance().getPreferences().contains("Difficulty")) {
                    difficultyOptionsVisible = false;
                    OverworldScreen overworldScreen = OverworldScreen.getInstance();
                    overworldScreen.create();
                    Energraft.getInstance().setScreen(overworldScreen);
                    this.dispose();
                    return;
                }
            } else if (inputControls.pauseButtonJustPressed) {
                difficultyOptionsVisible = false;
            }
        }
        inputControls.update();
        touchInterface.render(batch);
    }

    @Override
    public void dispose() {
        choices.clear();
        inputControls.clear();
        text.dispose();
        title.dispose();
        batch.dispose();
        choices = null;
        inputControls = null;
        launchBackdrop = null;
        text = null;
        title = null;
        batch = null;
        this.hide();
        super.dispose();
        System.gc();
    }
}