package com.udacity.gamedev.gigagal.app;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.udacity.gamedev.gigagal.entity.GigaGal;
import com.udacity.gamedev.gigagal.overlay.TouchInterface;
import com.udacity.gamedev.gigagal.util.Assets;
import com.udacity.gamedev.gigagal.util.InputControls;
import com.udacity.gamedev.gigagal.overlay.Menu;
import com.udacity.gamedev.gigagal.overlay.Cursor;
import com.udacity.gamedev.gigagal.util.Constants;
import com.udacity.gamedev.gigagal.util.Enums;
import com.udacity.gamedev.gigagal.util.Helpers;
import com.udacity.gamedev.gigagal.util.StaticCam;

import org.json.simple.parser.ParseException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// immutable package-private
final class OverworldScreen extends ScreenAdapter {

    // fields
    public static final String TAG = OverworldScreen.class.getName();
    private static final OverworldScreen INSTANCE = new OverworldScreen();
    private ScreenManager screenManager;
    private static LevelUpdater levelUpdater;
    private SpriteBatch batch;
    private ExtendViewport viewport;
    private Assets assets;
    private BitmapFont font;
    private TouchInterface touchInterface;
    private InputControls inputControls;
    private static GigaGal gigaGal;
    private static Cursor cursor;
    private static Menu menu;
    private static Enums.MenuType menuType;
    private boolean messageVisible;
    private static Enums.Theme selection;

    // cannot be subclassed
    private OverworldScreen() {}

    // static factory method
    protected static OverworldScreen getInstance() { return INSTANCE; }

    @Override
    public void show() {
        // : When you're done testing, use onMobile() turn off the controls when not on a mobile device
        // onMobile();
        screenManager = ScreenManager.getInstance();

        batch = screenManager.getBatch();

        viewport = StaticCam.getInstance().getViewport();

        assets = Assets.getInstance();
        font = assets.getFontAssets().menu;

        inputControls = InputControls.getInstance();
        touchInterface = TouchInterface.getInstance();

        gigaGal = GigaGal.getInstance();
        cursor = Cursor.getInstance();

        menu = Menu.getInstance();

        levelUpdater = LevelUpdater.getInstance();

        Gdx.input.setInputProcessor(InputControls.getInstance());

        messageVisible = false;
        setMainMenu();
    }

    public static void setMainMenu() {
        List<String> selectionStrings = new ArrayList();
        for (Enums.Theme level : Enums.Theme.values()) {
            selectionStrings.add(level.name());
        }
        selectionStrings.add("OPTIONS");
        cursor.setRange(145, 25);
        cursor.setOrientation(Enums.Orientation.Y);
        cursor.resetPosition();
        menu.clearStrings();
        menu.setOptionStrings(selectionStrings);
        menu.TextAlignment(Align.left);
        menuType = Enums.MenuType.MAIN;
    }

    private static void setOptionsMenu() {
        cursor.setRange(106, 76);
        cursor.setOrientation(Enums.Orientation.Y);
        cursor.resetPosition();
        String[] optionStrings = {"BACK", "TOUCH PAD", "QUIT GAME"};
        menu.setOptionStrings(Arrays.asList(optionStrings));
        menu.TextAlignment(Align.center);
        menuType = Enums.MenuType.OPTIONS;
    }

    private boolean onMobile() {
        return Gdx.app.getType() == Application.ApplicationType.Android || Gdx.app.getType() == Application.ApplicationType.iOS;
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
//        cursor.getViewport().update(width, height, true);
//        touchInterface.getViewport().update(width, height, true);
        touchInterface.recalculateButtonPositions();
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

        switch (menuType) {
            case MAIN:
                menu.render(batch, font, viewport, Cursor.getInstance());
                if (inputControls.shootButtonJustPressed) {
                    if (cursor.getPosition() <= 145 && cursor.getPosition() >= 40) {
                        selection = Enums.Theme.valueOf(cursor.getIterator().previous());
                        loadLevel(selection);
                    } else {
                        setOptionsMenu();
                    }
                }
                break;
            case OPTIONS:
                menu.render(batch, font, viewport, Cursor.getInstance());
                if (inputControls.shootButtonJustPressed) {
                    if (cursor.getPosition() == 106) {
                        setMainMenu();
                    } else if (cursor.getPosition() == 91) {
                        SaveData.toggleTouchscreen(!SaveData.hasTouchscreen());
                    } else if (cursor.getPosition() == 76) {
                        screenManager.dispose();
                        screenManager.create();
                    }
                } else if (inputControls.pauseButtonJustPressed) {
                }
                break;
        }
        if (messageVisible) {
            font.getData().setScale(0.25f);
            Helpers.drawBitmapFont(batch, viewport, font, Constants.LEVEL_READ_MESSAGE, viewport.getWorldWidth() / 2, Constants.HUD_MARGIN - 5, Align.center);
            font.getData().setScale(.5f);
        }
        inputControls.update();
        touchInterface.render(batch);
    }

    protected void loadLevel(Enums.Theme level) {
        List<String> allRestores = Arrays.asList(SaveData.getLevelRestores().split(", "));
        List<String> allRemovals = Arrays.asList(SaveData.getLevelRemovals().split(", "));
        List<String> allTimes = Arrays.asList(SaveData.getLevelTimes().split(", "));
        List<String> allScores = Arrays.asList(SaveData.getLevelScores().split(", "));
        int index = Arrays.asList(Enums.Theme.values()).indexOf(level);
        int levelRestores = Integer.parseInt(allRestores.get(index));
        if (levelRestores == 0) {
            allRestores.set(index, "0");
            allRemovals.set(index, "-1");
            allTimes.set(index, "0");
            allScores.set(index, "0");
            SaveData.setLevelRestores(allRestores.toString().replace("[", "").replace("]", ""));
            SaveData.setLevelRemovals(allRemovals.toString().replace("[", "").replace("]", ""));
            SaveData.setLevelTimes(allTimes.toString().replace("[", "").replace("]", ""));
            SaveData.setLevelScores(allScores.toString().replace("[", "").replace("]", ""));
        }
        levelUpdater.setTime(Long.parseLong(allTimes.get(index)));
        levelUpdater.setScore(Integer.parseInt(allScores.get(index)));
        messageVisible = false;
        try {
            LevelLoader.load(level);
            levelUpdater.restoreRemovals(allRemovals.get(index));
            if (levelRestores > 0) {
                gigaGal.setSpawnPosition(levelUpdater.getTransports().get(0).getPosition());
                if (levelRestores > 2) {
                    gigaGal.setSpawnPosition(levelUpdater.getTransports().get(1).getPosition());
                }
            }
            screenManager.setScreen(LevelScreen.getInstance());
            this.dispose();
            return;
        } catch (IOException ex) {
            Gdx.app.log(TAG, Constants.LEVEL_READ_MESSAGE);
            Gdx.app.log(TAG, Constants.LEVEL_READ_MESSAGE, ex);
            cursor.getIterator().next();
            messageVisible = true;
        } catch (ParseException ex) {
            Gdx.app.log(TAG, Constants.LEVEL_READ_MESSAGE);
            Gdx.app.log(TAG, Constants.LEVEL_READ_MESSAGE, ex);
            cursor.getIterator().next();
            messageVisible = true;
        } catch (GdxRuntimeException ex) {
            Gdx.app.log(TAG, Constants.LEVEL_READ_MESSAGE);
            Gdx.app.log(TAG, Constants.LEVEL_READ_MESSAGE, ex);
            cursor.getIterator().next();
            messageVisible = true;
        }
    }

    protected static Enums.Theme getSelection() { return selection; }

    @Override
    public void dispose() {
//        completedLevels.clearStrings();
//        inputControls.clearStrings();
//        errorMessage.dispose();
//        font.dispose();
//        batch.dispose();
//        completedLevels = null;
//        inputControls = null;
//        errorMessage = null;
//        font = null;
//        batch = null;
//        this.hide();
//        super.dispose();
    }
}