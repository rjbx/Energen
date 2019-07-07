package com.github.rjbx.energen.app;

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
import com.github.rjbx.energen.entity.Avatar;
import com.github.rjbx.energen.overlay.*;
import com.github.rjbx.energen.util.*;

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
    private Avatar avatar;

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

        avatar = Avatar.getInstance();
        avatar.respawn();

        choices = new ArrayList<String>();
        launchStartTime = TimeUtils.nanoTime();
        launching = true;
        continuing = SaveData.getDifficulty() != -1;
        choices.add("NO");
        choices.add("YES");
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        final Vector2 avatarPosition = new Vector2(viewport.getCamera().position.x, viewport.getCamera().position.y);
        avatar.setPosition(avatarPosition);
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
                    Helpers.drawTextureRegion(batch, viewport, assetManager.getOverlayAssets().beast, viewport.getCamera().position.x - 55, viewport.getCamera().position.y + 25, Constants.BEAST_CENTER.x, Constants.BEAST_CENTER.y, 2f);
                    Helpers.drawTextureRegion(batch, viewport, assetManager.getOverlayAssets().globe, viewport.getCamera().position.x, viewport.getCamera().position.y, Constants.GLOBE_CENTER.x + 12, Constants.GLOBE_CENTER.y + 20, 3f);
                    Helpers.drawBitmapFont(batch, viewport, title, "ENERGEN", viewport.getCamera().position.x, viewport.getCamera().position.y + 70, Align.center);
                    avatar.render(batch, viewport);
                    menu.render(batch, font, viewport, Cursor.getInstance());

                    if (inputControls.justSelected()) {
                        assetManager.getMusicAssets().intro.stop();
                        if (continuing) {
                            if (cursor.getPosition() == viewport.getCamera().position.y - 50) {
                                screenManager.setScreen(overworldScreen);
                                this.dispose();
                                return;
                            } else if (cursor.getPosition() == viewport.getCamera().position.y - 65) {
                                setEraseMenu();
                            }
                        } else {
                            setDifficultyMenu();
                        }
                    }
                    break;
                case ERASE:
                    menu.render(batch, font, viewport, Cursor.getInstance());
                    if (inputControls.justSelected()) {
                        if (cursor.getPosition() == (viewport.getCamera().position.x + 25)) {
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
                    if (inputControls.justSelected()) {
                        if (cursor.getPosition() == viewport.getCamera().position.y) {
                            SaveData.setDifficulty(0);
                        } else if (cursor.getPosition() == viewport.getCamera().position.y - 15) {
                            SaveData.setDifficulty(1);
                        } else if (cursor.getPosition() == viewport.getCamera().position.y - 30) {
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
                    new Vector2(Constants.LOGO_CENTER.x * .375f, Constants.LOGO_CENTER.y * .375f), .23f);
            Helpers.drawBitmapFont(batch, viewport, font, Constants.LAUNCH_MESSAGE, viewport.getCamera().position.x, viewport.getCamera().position.y - 50, Align.center);
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

    private void setResumeMenu() {
        cursor.setRange(viewport.getCamera().position.y - 50, viewport.getCamera().position.y - 65);
        cursor.setOrientation(Enums.Orientation.Y);
        cursor.resetPosition();
        String[] optionStrings = {"START GAME", "ERASE GAME"};
        menu.clearStrings();
        menu.setOptionStrings(Arrays.asList(optionStrings));
        menu.TextAlignment(Align.center);
        menuType = Enums.MenuType.START;
    }

    private void setEraseMenu() {
        cursor.setRange(viewport.getCamera().position.x - 50, viewport.getCamera().position.x + 50);
        cursor.setOrientation(Enums.Orientation.X);
        cursor.resetPosition();
        String[] optionStrings = {"NO", "YES"};
        menu.setOptionStrings(Arrays.asList(optionStrings));
        menu.TextAlignment(Align.center);
        menu.setPromptString(Align.center, "Are you sure you want to start \na new game and erase all saved data?");
        menuType = Enums.MenuType.ERASE;
    }

    private void setBeginMenu() {
        cursor.setRange(viewport.getCamera().position.y - 55, viewport.getCamera().position.y - 55);
        menu.isSingleOption(true);
        String[] option = {"PRESS START"};
        menu.clearStrings();
        menu.setOptionStrings(Arrays.asList(option));
        menu.TextAlignment(Align.center);
        menuType = Enums.MenuType.START;
    }

    private void setDifficultyMenu() {
        cursor.setRange(viewport.getCamera().position.y, viewport.getCamera().position.y - 30);
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