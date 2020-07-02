package com.github.rjbx.energen.app;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.github.rjbx.energen.entity.Avatar;
import com.github.rjbx.energen.overlay.Backdrop;
import com.github.rjbx.energen.overlay.TouchInterface;
import com.github.rjbx.energen.util.AssetManager;
import com.github.rjbx.energen.util.InputControls;
import com.github.rjbx.energen.overlay.Menu;
import com.github.rjbx.energen.overlay.Cursor;
import com.github.rjbx.energen.util.Constants;
import com.github.rjbx.energen.util.Enums;
import com.github.rjbx.energen.util.Helpers;
import com.github.rjbx.energen.util.StaticCam;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// immutable package-private singleton
final class OverworldScreen extends ScreenAdapter {

    // fields
    public static final String TAG = OverworldScreen.class.getName();
    private static final OverworldScreen INSTANCE = new OverworldScreen();
    private ScreenManager screenManager;
    private static LevelUpdater levelUpdater;
    private SpriteBatch batch;
    private ExtendViewport viewport;
    private AssetManager assetManager;
    private BitmapFont font;
    private TouchInterface touchInterface;
    private InputControls inputControls;
    private static Avatar avatar;
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
        screenManager = ScreenManager.getInstance();

        batch = screenManager.getBatch();

        viewport = StaticCam.getInstance().getViewport();

        assetManager = AssetManager.getInstance();
        font = assetManager.getFontAssets().menu;
        assetManager.getMusicAssets().overworld.play();
        assetManager.getMusicAssets().overworld.setLooping(true);

        touchInterface = TouchInterface.getInstance();

        inputControls = InputControls.getInstance();
        Gdx.input.setInputProcessor(InputControls.getInstance());

        avatar = Avatar.getInstance();
        cursor = Cursor.getInstance();

        menu = Menu.getInstance();

        levelUpdater = LevelUpdater.getInstance();

        messageVisible = false;
        setMainMenu();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        touchInterface.getViewport().update(width, height, true);
        touchInterface.recalculateButtonPositions();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(
                Constants.BACKGROUND_COLOR.r,
                Constants.BACKGROUND_COLOR.g,
                Constants.BACKGROUND_COLOR.b,
                Constants.BACKGROUND_COLOR.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        new Backdrop(assetManager.getBackgroundAssets().gas).render(batch, viewport, new Vector2(viewport.getCamera().position.x, viewport.getCamera().position.y), Constants.BACKGROUND_CENTER, 1);

        switch (menuType) {
            case MAIN:
                menu.render(batch, font, viewport, Cursor.getInstance(), Enums.Theme.GRAVITATIONAL.name(), Enums.Theme.MYSTERIOUS.name(), Enums.Theme.FINAL.name());
                if (inputControls.justSelected()) {
                    assetManager.getMusicAssets().overworld.stop();
                    if (cursor.getPosition() <= viewport.getCamera().position.y + 55 && cursor.getPosition() > viewport.getCamera().position.y - 65) {
                        selection = Enums.Theme.valueOf(cursor.getIterator().previous());
                        if (selection == Enums.Theme.GRAVITATIONAL || selection == Enums.Theme.MYSTERIOUS) {
                            selection = null;
                            cursor.getIterator().next();
                        } else loadLevel(selection);
                    } else {
                        setOptionsMenu();
                    }
                }
                break;
            case OPTIONS:
                menu.render(batch, font, viewport, Cursor.getInstance());
                if (inputControls.justSelected()) {
                    if (cursor.getPosition() == viewport.getCamera().position.y + 30) {
                        setMainMenu();
                    } else if (cursor.getPosition() == viewport.getCamera().position.y + 15) {
                        SaveData.setTouchscreen(!SaveData.hasTouchscreen());
                    } else if (cursor.getPosition() == viewport.getCamera().position.y) {
                        screenManager.dispose();
                        screenManager.create();
                    }
                } else if (inputControls.pauseButtonJustPressed) {
                }
                break;
        }
        if (messageVisible) {
            font.getData().setScale(0.25f);
            Helpers.drawBitmapFont(batch, viewport, font, Constants.LEVEL_READ_MESSAGE, viewport.getCamera().position.x, viewport.getCamera().position.y + 70, Align.center);
            font.getData().setScale(.5f);
        }
        inputControls.update();
        touchInterface.render(batch);
    }

    protected void loadLevel(Enums.Theme level) {
        List<String> allRestores = Arrays.asList(SaveData.getLevelRestore().split(", "));
        List<String> allRemovals = Arrays.asList(SaveData.getLevelRemovals().split(", "));
        List<String> allTimes = Arrays.asList(SaveData.getLevelTimes().split(", "));
        List<String> allScores = Arrays.asList(SaveData.getLevelScores().split(", "));
        int index = Arrays.asList(Enums.Theme.values()).indexOf(level);
        boolean levelRestored = !allRestores.get(index).equals("0:0");
        if (!levelRestored) {
            allRestores.set(index, "0:0");
            allRemovals.set(index, "-1");
            allTimes.set(index, "0");
            allScores.set(index, "0");
            SaveData.setLevelRestore(allRestores.toString().replace("[", "").replace("]", ""));
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
            if (levelRestored) {
                String[] coordinateStr = allRestores.get(index).split(":");
                Vector2 position = new Vector2(Float.valueOf(coordinateStr[0]), Float.valueOf(coordinateStr[1]));
                avatar.setSpawnPosition(position);
            }
            screenManager.setScreen(LevelScreen.getInstance());
            this.dispose();
            return;
        } catch (GdxRuntimeException ex) {
            Gdx.app.log(TAG, Constants.LEVEL_READ_MESSAGE);
            Gdx.app.log(TAG, Constants.LEVEL_READ_MESSAGE, ex);
            cursor.getIterator().next();
            messageVisible = true;
        }
    }

    protected static Enums.Theme getSelection() { return selection; }


    public void setMainMenu() {
        List<String> selectionStrings = new ArrayList();
        for (Enums.Theme level : Enums.Theme.values()) {
            selectionStrings.add(level.name());
        }
        selectionStrings.add("OPTIONS");
        cursor.setRange(viewport.getCamera().position.y + 55, viewport.getCamera().position.y - 65);
        cursor.setOrientation(Enums.Orientation.Y);
        cursor.resetPosition();
        menu.clearStrings();
        menu.setOptionStrings(selectionStrings);
        menu.TextAlignment(Align.left);
        menuType = Enums.MenuType.MAIN;
    }

    private void setOptionsMenu() {
        cursor.setRange(viewport.getCamera().position.y + 30, viewport.getCamera().position.y);
        cursor.setOrientation(Enums.Orientation.Y);
        cursor.resetPosition();
        String[] optionStrings = {"BACK", "TOUCH PAD", "QUIT GAME"};
        menu.setOptionStrings(Arrays.asList(optionStrings));
        menu.TextAlignment(Align.center);
        menuType = Enums.MenuType.OPTIONS;
    }

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