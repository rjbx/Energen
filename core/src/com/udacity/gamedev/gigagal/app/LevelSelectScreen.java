package com.udacity.gamedev.gigagal.app;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.udacity.gamedev.gigagal.overlays.Menu;
import com.udacity.gamedev.gigagal.overlays.OnscreenControls;
import com.udacity.gamedev.gigagal.overlays.Cursor;
import com.udacity.gamedev.gigagal.overlays.Message;
import com.udacity.gamedev.gigagal.util.Constants;
import com.udacity.gamedev.gigagal.util.Enums;
import com.udacity.gamedev.gigagal.util.Helpers;

import org.json.simple.parser.ParseException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// immutable
public final class LevelSelectScreen extends ScreenAdapter {

    // fields
    public static final String TAG = LevelSelectScreen.class.getName();
    private static final LevelSelectScreen INSTANCE = new LevelSelectScreen();
    private static InputControls inputControls;
    private static OnscreenControls onscreenControls;
    private com.udacity.gamedev.gigagal.app.GigaGalGame game;
    private Preferences prefs;
    private ExtendViewport viewport;
    private SpriteBatch batch;
    private BitmapFont font;
    private Message errorMessage;
    private List<Enums.LevelName> completedLevels;
    private Enums.LevelName levelName;
    private Enums.LevelName selectedLevel;
    private GameplayScreen gameplayScreen;
    private boolean viewingOptions;
    private boolean messageVisible;

    // cannot be subclassed
    private LevelSelectScreen() {}

    // static factory method
    public static LevelSelectScreen getInstance() { return INSTANCE; }

    public void create() {
        this.game = GigaGalGame.getInstance();
        prefs = game.getPreferences();
        this.viewport = new ExtendViewport(Constants.WORLD_SIZE, Constants.WORLD_SIZE);
        font = new BitmapFont(Gdx.files.internal(Constants.FONT_FILE));
        font.getData().setScale(0.5f);
    }

    @Override
    public void show() {
        // : When you're done testing, use onMobile() turn off the controls when not on a mobile device
        // onMobile();
        viewingOptions = false;
        messageVisible = false;
        batch = new SpriteBatch();
        completedLevels = new ArrayList<Enums.LevelName>();
        errorMessage = new Message();
        inputControls = InputControls.getInstance();
        onscreenControls = OnscreenControls.getInstance();
        Gdx.input.setInputProcessor(inputControls);
    }

    private static void setMainMenu() {
        Cursor.getInstance().init();
        Cursor.getInstance().setRange(145, 25);
        Cursor.getInstance().setOrientation(Enums.Orientation.Y);
        Cursor.getInstance().resetPosition();
        List<String> selectionStrings = new ArrayList();
        for (Enums.LevelName level : Enums.LevelName.values()) {
            selectionStrings.add(level.name());
        }
        selectionStrings.add("OPTIONS");
        Cursor.getInstance().setIterator(selectionStrings);
        Menu.getInstance().setOptionStrings(selectionStrings);
        Menu.getInstance().setAlignment(Align.left);
    }

    private static void setOptionsMenu() {
        String[] optionStrings = {"BACK", "TOUCH PAD", "QUIT GAME"};
        Menu.getInstance().setOptionStrings(Arrays.asList(optionStrings));
        Cursor.getInstance().setIterator(null);
        Cursor.getInstance().setRange(106, 76);
        Cursor.getInstance().resetPosition();
        Cursor.getInstance().update();
    }

    private boolean onMobile() {
        return Gdx.app.getType() == Application.ApplicationType.Android || Gdx.app.getType() == Application.ApplicationType.iOS;
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
//        cursor.getViewport().update(width, height, true);
//        onscreenControls.getViewport().update(width, height, true);
//        onscreenControls.recalculateButtonPositions();
//        optionsOverlay.getViewport().update(width, height, true);
//        optionsOverlay.getCursor().getViewport().update(width, height, true);
//        errorMessage.getViewport().update(width, height, true);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(
                Constants.BACKGROUND_COLOR.r,
                Constants.BACKGROUND_COLOR.g,
                Constants.BACKGROUND_COLOR.b,
                Constants.BACKGROUND_COLOR.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (!viewingOptions) {
            viewport.apply();

            Menu.getInstance().render(batch, font, viewport, Cursor.getInstance());

            if (inputControls.shootButtonJustPressed) {
                if (Cursor.getInstance().getPosition() <= 145 && Cursor.getInstance().getPosition() >= 40) {
                    selectedLevel = Enums.LevelName.valueOf(Cursor.getInstance().getIterator().previous());
                    gameplayScreen = GameplayScreen.getInstance();
                    gameplayScreen.create(selectedLevel);
                    messageVisible = false;
                    try {
                        gameplayScreen.readLevelFile();
                        game.setScreen(gameplayScreen);
                        this.dispose();
                        return;
                    } catch (IOException ex) {
                        Gdx.app.log(TAG, Constants.LEVEL_READ_MESSAGE);
                        errorMessage.setMessage(Constants.LEVEL_READ_MESSAGE);
                        Cursor.getInstance().getIterator().next();
                        messageVisible = true;
                    } catch (ParseException ex) {
                        Gdx.app.log(TAG, Constants.LEVEL_READ_MESSAGE);
                        errorMessage.setMessage(Constants.LEVEL_READ_MESSAGE);
                        Cursor.getInstance().getIterator().next();
                        messageVisible = true;
                    } catch (GdxRuntimeException ex) {
                        Gdx.app.log(TAG, Constants.LEVEL_READ_MESSAGE);
                        errorMessage.setMessage(Constants.LEVEL_READ_MESSAGE);
                        Cursor.getInstance().getIterator().next();
                        messageVisible = true;
                    }
                } else {
                    viewingOptions = true;
                    setOptionsMenu();
                }
            }
        } else {
            Menu.getInstance().render(batch, font, viewport, Cursor.getInstance());
            if (inputControls.shootButtonJustPressed) {
                if (Cursor.getInstance().getPosition() == 106) {
                    setMainMenu();
                    viewingOptions = false;
                } else if (Cursor.getInstance().getPosition() == 91) {
                    onscreenControls.onMobile = Helpers.toggleBoolean(onscreenControls.onMobile);
                    prefs.putBoolean("Mobile", onscreenControls.onMobile);
                } else if (Cursor.getInstance().getPosition() == 76) {
                    game.dispose();
                    game.create();
                }
            } else if (inputControls.pauseButtonJustPressed) {
                viewingOptions = false;
            }
        }
        if (messageVisible) {
            font.getData().setScale(0.25f);
            errorMessage.render(batch, font, viewport, new Vector2(viewport.getWorldWidth() / 2, Constants.HUD_MARGIN - 5));
            font.getData().setScale(.5f);
        }
        inputControls.update();
        onscreenControls.render(batch, viewport);
    }

    @Override
    public void dispose() {
        completedLevels.clear();
        inputControls.clear();
        errorMessage.dispose();
        font.dispose();
        batch.dispose();
        completedLevels = null;
        inputControls = null;
        errorMessage = null;
        font = null;
        batch = null;
        this.hide();
        super.dispose();
        System.gc();
    }
}