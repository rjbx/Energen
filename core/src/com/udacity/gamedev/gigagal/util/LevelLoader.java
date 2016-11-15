package com.udacity.gamedev.gigagal.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.udacity.gamedev.gigagal.Level;
import com.udacity.gamedev.gigagal.entities.Spike;
import com.udacity.gamedev.gigagal.entities.Zoomba;
import com.udacity.gamedev.gigagal.entities.ExitPortal;
import com.udacity.gamedev.gigagal.entities.GigaGal;
import com.udacity.gamedev.gigagal.entities.Platform;
import com.udacity.gamedev.gigagal.entities.Powerup;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.util.Comparator;

// immutable static
public final class LevelLoader {

    public static final String TAG = LevelLoader.class.toString();

    // non-instantiable
    private LevelLoader() {}

    public static final Level load(String path) {

        final FileHandle file = Gdx.files.internal(path);
        Level level = new Level();
        JSONParser parser = new JSONParser();
        JSONObject rootJsonObject;

        try {
            rootJsonObject = (JSONObject) parser.parse(file.reader());

            JSONObject composite = (JSONObject) rootJsonObject.get(Constants.LEVEL_COMPOSITE);

            JSONArray platforms = (JSONArray) composite.get(Constants.LEVEL_9PATCHES);
            loadPlatforms(level, platforms);

            JSONArray nonPlatformObjects = (JSONArray) composite.get(Constants.LEVEL_IMAGES);
            loadNonPlatformEntities(level, nonPlatformObjects);

        } catch (Exception ex) {
            Gdx.app.log(TAG, ex.getMessage());
            Gdx.app.log(TAG, Constants.LEVEL_ERROR_MESSAGE);
        }

        return level;
    }

    private static final Vector2 extractXY(JSONObject object) {

        Number x = (Number) object.get(Constants.LEVEL_X_KEY);
        Number y = (Number) object.get(Constants.LEVEL_Y_KEY);

        return new Vector2(
                (x == null) ? 0 : x.floatValue(),
                (y == null) ? 0 : y.floatValue()
        );
    }

    private static final void loadNonPlatformEntities(Level level, JSONArray nonPlatformObjects) {
        for (Object o : nonPlatformObjects) {
            JSONObject item = (JSONObject) o;
            String identifier = (String) item.get(Constants.LEVEL_IDENTIFIER_KEY);
            Vector2 imageCenter = extractXY(item);

            if (identifier.equals(Constants.POWERUP_SPRITE)) {
                final Vector2 powerupPosition = imageCenter.add(Constants.POWERUP_CENTER);
                Gdx.app.log(TAG, "Loaded a powerup at " + powerupPosition);
                level.getPowerups().add(new Powerup(powerupPosition));
            } else if (identifier.equals(Constants.STAND_RIGHT)) {
                final Vector2 gigaGalPosition = imageCenter.add(Constants.GIGAGAL_EYE_POSITION);
                Gdx.app.log(TAG, "Loaded GigaGal at " + gigaGalPosition);
                level.setGigaGal(new GigaGal(gigaGalPosition, level));
            } else if (identifier.equals(Constants.EXIT_PORTAL_SPRITE_1)) {
                final Vector2 exitPortalPosition = imageCenter.add(Constants.EXIT_PORTAL_CENTER);
                Gdx.app.log(TAG, "Loaded the exit portal at " + exitPortalPosition);
                level.setExitPortal(new ExitPortal(exitPortalPosition));
            }
        }
    }

    private static final void loadPlatforms(Level level, JSONArray array) {

        Array<Platform> platformArray = new Array<Platform>();

        for (Object o : array) {
            final JSONObject platformObject = (JSONObject) o;
            final String identifier = (String) platformObject.get(Constants.LEVEL_IDENTIFIER_KEY);
            Vector2 platformBottomLeft = extractXY(platformObject);
            final float width = ((Number) platformObject.get(Constants.LEVEL_WIDTH_KEY)).floatValue();
            final float height = ((Number) platformObject.get(Constants.LEVEL_HEIGHT_KEY)).floatValue();
            final Platform platform = new Platform(platformBottomLeft.x, platformBottomLeft.y + height, width, height);

            platformArray.add(platform);

            if (identifier.equals(Constants.LEVEL_ZOOMBA_TAG)) {
                final Zoomba zoomba = new Zoomba(platform);
                level.getEnemies().add(zoomba);
            }

            if (identifier.equals(Constants.LEVEL_SPIKE_TAG)) {
                final Vector2 spikePosition = extractXY(platformObject).add(Constants.SPIKE_CENTER);
                final Spike spike = new Spike(platform, spikePosition.x);
                level.getEnemies().add(spike);
            }
        }

        platformArray.sort(new Comparator<Platform>() {
            @Override
            public int compare(Platform o1, Platform o2) {
                if (o1.getTop() < o2.getTop()) {
                    return 1;
                } else if (o1.getTop() > o2.getTop()) {
                    return -1;
                }
                return 0;
            }
        });

        level.getPlatforms().addAll(platformArray);
    }
}