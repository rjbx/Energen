package com.udacity.gamedev.gigagal.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.udacity.gamedev.gigagal.Level;
import com.udacity.gamedev.gigagal.entities.AmmoPowerup;
import com.udacity.gamedev.gigagal.entities.Cannon;
import com.udacity.gamedev.gigagal.entities.ChargedSwoopa;
import com.udacity.gamedev.gigagal.entities.ChargedZoomba;
import com.udacity.gamedev.gigagal.entities.Coals;
import com.udacity.gamedev.gigagal.entities.Coil;
import com.udacity.gamedev.gigagal.entities.FierySwoopa;
import com.udacity.gamedev.gigagal.entities.FieryZoomba;
import com.udacity.gamedev.gigagal.entities.GushingSwoopa;
import com.udacity.gamedev.gigagal.entities.GushingZoomba;
import com.udacity.gamedev.gigagal.entities.Ladder;
import com.udacity.gamedev.gigagal.entities.Pillar;
import com.udacity.gamedev.gigagal.entities.Rope;
import com.udacity.gamedev.gigagal.entities.SharpSwoopa;
import com.udacity.gamedev.gigagal.entities.SharpZoomba;
import com.udacity.gamedev.gigagal.entities.Sink;
import com.udacity.gamedev.gigagal.entities.Slick;
import com.udacity.gamedev.gigagal.entities.Spring;
import com.udacity.gamedev.gigagal.entities.Swoopa;
import com.udacity.gamedev.gigagal.entities.Treadmill;
import com.udacity.gamedev.gigagal.entities.TurboPowerup;
import com.udacity.gamedev.gigagal.entities.Vacuum;
import com.udacity.gamedev.gigagal.entities.Flame;
import com.udacity.gamedev.gigagal.entities.Geiser;
import com.udacity.gamedev.gigagal.entities.HealthPowerup;
import com.udacity.gamedev.gigagal.entities.Spike;
import com.udacity.gamedev.gigagal.entities.Wheel;
import com.udacity.gamedev.gigagal.entities.WhirlingSwoopa;
import com.udacity.gamedev.gigagal.entities.WhirlingZoomba;
import com.udacity.gamedev.gigagal.entities.Zoomba;
import com.udacity.gamedev.gigagal.entities.Portal;
import com.udacity.gamedev.gigagal.entities.GigaGal;
import com.udacity.gamedev.gigagal.entities.Platform;
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

            JSONArray ground = (JSONArray) composite.get(Constants.LEVEL_9PATCHES);
            loadGrounds(ground, level);

            JSONArray nonGrounds = (JSONArray) composite.get(Constants.LEVEL_IMAGES);
            loadNonGrounds(level, nonGrounds);

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


    private static final void loadNonGrounds(Level level, JSONArray nonGrounds) {
        for (Object o : nonGrounds) {
            final JSONObject item = (JSONObject) o;
            final Vector2 imagePosition = extractXY(item);

            if (item.get(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.AMMO_POWERUP_SPRITE)) {
                final Vector2 powerupPosition = imagePosition.add(Constants.POWERUP_CENTER);
                Gdx.app.log(TAG, "Loaded an AmmoPowerup at " + powerupPosition);
                level.getPowerups().add(new AmmoPowerup(powerupPosition));
            } else if (item.get(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.HEALTH_POWERUP_SPRITE)) {
                final Vector2 powerupPosition = imagePosition.add(Constants.POWERUP_CENTER);
                Gdx.app.log(TAG, "Loaded a HealthPowerup at " + powerupPosition);
                level.getPowerups().add(new HealthPowerup(powerupPosition));
            } else if (item.get(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.TURBO_POWERUP_SPRITE)) {
                final Vector2 powerupPosition = imagePosition.add(Constants.POWERUP_CENTER);
                Gdx.app.log(TAG, "Loaded a TurboPowerup at " + powerupPosition);
                level.getPowerups().add(new TurboPowerup(powerupPosition));
            } else if (item.get(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.STAND_RIGHT)) {
                final Vector2 gigaGalPosition = imagePosition.add(Constants.GIGAGAL_EYE_POSITION);
                Gdx.app.log(TAG, "Loaded GigaGal at " + gigaGalPosition);
                level.setGigaGal(new GigaGal(gigaGalPosition, level));
            } else if (item.get(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.PORTAL_SPRITE_1)) {
                final Vector2 portalPosition = imagePosition.add(Constants.PORTAL_CENTER);
                Gdx.app.log(TAG, "Loaded the exit portal at " + portalPosition);
                level.setPortal(new Portal(portalPosition));
            } else if (item.get(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.SPIKE_SPRITE_1)) {
                final Vector2 spikePosition = imagePosition.add(Constants.SPIKE_CENTER);
                Gdx.app.log(TAG, "Loaded the spike at " + spikePosition);
                level.getIndestructibles().add(new Spike(spikePosition));
            } else if (item.get(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.FLAME_SPRITE_1)) {
                final Vector2 flamePosition = imagePosition.add(Constants.FLAME_CENTER);
                Gdx.app.log(TAG, "Loaded the flame at " + flamePosition);
                level.getIndestructibles().add(new Flame(flamePosition));
            } else if (item.get(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.GEISER_SPRITE_1)) {
                final Vector2 geiserPosition = imagePosition.add(Constants.GEISER_CENTER);
                Gdx.app.log(TAG, "Loaded the geiser at " + geiserPosition);
                level.getIndestructibles().add(new Geiser(geiserPosition));
            } else if (item.get(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.WHEEL_SPRITE_1)) {
                final Vector2 wheelPosition = imagePosition.add(Constants.WHEEL_CENTER);
                Gdx.app.log(TAG, "Loaded the wheel at " + wheelPosition);
                level.getIndestructibles().add(new Wheel(wheelPosition));
            } else if (item.get(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.COIL_SPRITE_1)) {
                final Vector2 coilPosition = imagePosition.add(Constants.COIL_CENTER);
                Gdx.app.log(TAG, "Loaded the coil at " + coilPosition);
                level.getIndestructibles().add(new Coil(coilPosition));
            } else if (item.get(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.VACUUM_SPRITE_1)) {
                final Vector2 vacuumPosition = imagePosition.add(Constants.VACUUM_CENTER);
                Gdx.app.log(TAG, "Loaded the vacuum at " + vacuumPosition);
                level.getIndestructibles().add(new Vacuum(vacuumPosition));
            } else if (item.get(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.LATERAL_CANNON_SPRITE)) {
                final Vector2 cannonPosition = imagePosition.add(Constants.LATERAL_CANNON_CENTER);
                Gdx.app.log(TAG, "Loaded the cannon at " + cannonPosition);
                level.getGrounds().add(new Cannon(cannonPosition, Enums.Orientation.LATERAL));
            } else if (item.get(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.VERTICAL_CANNON_SPRITE)) {
                final Vector2 cannonPosition = imagePosition.add(Constants.VERTICAL_CANNON_CENTER);
                Gdx.app.log(TAG, "Loaded the cannon at " + cannonPosition);
                level.getGrounds().add(new Cannon(cannonPosition, Enums.Orientation.VERTICAL));
            } else if (item.get(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.PILLAR_SPRITE)) {
                final Vector2 pillarPosition = imagePosition.add(Constants.PILLAR_CENTER);
                Gdx.app.log(TAG, "Loaded the pillar at " + pillarPosition);
                level.getGrounds().add(new Pillar(pillarPosition));
            } else if (item.get(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.ROPE_SPRITE)) {
                final Vector2 ropePosition = imagePosition.add(Constants.ROPE_CENTER);
                Gdx.app.log(TAG, "Loaded the rope at " + ropePosition);
                level.getGrounds().add(new Rope(ropePosition));
            } else if (item.get(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.TREADMILL_1_RIGHT)) {
                final Vector2 treadmillPosition = imagePosition.add(Constants.TREADMILL_CENTER);
                Gdx.app.log(TAG, "Loaded the treadmillRight at " + treadmillPosition);
                level.getGrounds().add(new Treadmill(treadmillPosition, Enums.Direction.RIGHT));
            } else if (item.get(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.TREADMILL_1_LEFT)) {
                final Vector2 treadmillPosition = imagePosition.add(Constants.TREADMILL_CENTER);
                Gdx.app.log(TAG, "Loaded the treadmillLeft at " + treadmillPosition);
                level.getGrounds().add(new Treadmill(treadmillPosition, Enums.Direction.LEFT));
            } else if (item.get(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.SINK_SPRITE_1)) {
                final Vector2 sinkPosition = imagePosition.add(Constants.SINK_CENTER);
                Gdx.app.log(TAG, "Loaded the sink at " + sinkPosition);
                level.getGrounds().add(new Sink(sinkPosition));
            } else if (item.get(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.SPRING_SPRITE_1)) {
                final Vector2 springPosition = imagePosition.add(Constants.SPRING_CENTER);
                Gdx.app.log(TAG, "Loaded the spring at " + springPosition);
                level.getGrounds().add(new Spring(springPosition));
            } else if (item.get(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.SLICK_SPRITE_1)) {
                final Vector2 slickPosition = imagePosition.add(Constants.SLICK_CENTER);
                Gdx.app.log(TAG, "Loaded the slick at " + slickPosition);
                level.getGrounds().add(new Slick(slickPosition));
            } else if (item.get(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.COALS_SPRITE_1)) {
                final Vector2 coalsPosition = imagePosition.add(Constants.COALS_CENTER);
                Gdx.app.log(TAG, "Loaded the coals at " + coalsPosition);
                level.getGrounds().add(new Coals(coalsPosition));
            }
        }
    }

    private static final void loadGrounds(JSONArray array, Level level) {

        Array<Ladder> ladderArray = new Array<Ladder>();
        Array<Platform> platformArray = new Array<Platform>();

        for (Object o : array) {

            final JSONObject item = (JSONObject) o;
            Vector2 imagePosition = extractXY(item);
            String identifier = (String) item.get(Constants.LEVEL_IDENTIFIER_KEY);

           if (item.get(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.PLATFORM_SPRITE)){
                Vector2 bottomLeft = extractXY(item);
                float width = ((Number) item.get(Constants.LEVEL_WIDTH_KEY)).floatValue();
                float height = ((Number) item.get(Constants.LEVEL_HEIGHT_KEY)).floatValue();
                final Platform platform = new Platform(bottomLeft.x, bottomLeft.y + height, width, height);

                platformArray.add(platform);

                if (identifier != null) {
                    if (identifier.equals(Constants.LEVEL_ZOOMBA_TAG)) {
                        final Zoomba zoomba = new Zoomba(platform);
                        level.getDestructibles().add(zoomba);
                    } else if (identifier.equals(Constants.LEVEL_FIERYZOOMBA_TAG)) {
                        final FieryZoomba fieryZoomba = new FieryZoomba(platform);
                        level.getDestructibles().add(fieryZoomba);
                    } else if (identifier.equals(Constants.LEVEL_GUSHINGZOOMBA_TAG)) {
                        final GushingZoomba gushingZoomba = new GushingZoomba(platform);
                        level.getDestructibles().add(gushingZoomba);
                    } else if (identifier.equals(Constants.LEVEL_CHARGEDZOOMBA_TAG)) {
                        final ChargedZoomba chargedZoomba = new ChargedZoomba(platform);
                        level.getDestructibles().add(chargedZoomba);
                    } else if (identifier.equals(Constants.LEVEL_WHIRLINGZOOMBA_TAG)) {
                        final WhirlingZoomba whirlingZoomba = new WhirlingZoomba(platform);
                        level.getDestructibles().add(whirlingZoomba);
                    } else if (identifier.equals(Constants.LEVEL_SHARPZOOMBA_TAG)) {
                        final SharpZoomba sharpZoomba = new SharpZoomba(platform);
                        level.getDestructibles().add(sharpZoomba);
                    } else if (identifier.equals(Constants.LEVEL_SWOOPA_TAG)) {
                        final Swoopa swoopa = new Swoopa(platform, level);
                        level.getDestructibles().add(swoopa);
                    } else if (identifier.equals(Constants.LEVEL_FIERYSWOOPA_TAG)) {
                        final FierySwoopa fierySwoopa = new FierySwoopa(platform, level);
                        level.getDestructibles().add(fierySwoopa);
                    } else if (identifier.equals(Constants.LEVEL_GUSHINGSWOOPA_TAG)) {
                        final GushingSwoopa gushingSwoopa = new GushingSwoopa(platform, level);
                        level.getDestructibles().add(gushingSwoopa);
                    } else if (identifier.equals(Constants.LEVEL_CHARGEDSWOOPA_TAG)) {
                        final ChargedSwoopa chargedSwoopa = new ChargedSwoopa(platform, level);
                        level.getDestructibles().add(chargedSwoopa);
                    } else if (identifier.equals(Constants.LEVEL_WHIRLINGSWOOPA_TAG)) {
                        final WhirlingSwoopa whirlingSwoopa = new WhirlingSwoopa(platform, level);
                        level.getDestructibles().add(whirlingSwoopa);
                    } else if (identifier.equals(Constants.LEVEL_SHARPSWOOPA_TAG)) {
                        final SharpSwoopa sharpSwoopa = new SharpSwoopa(platform, level);
                        level.getDestructibles().add(sharpSwoopa);
                    }
                }
            } else  if (item.get(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.LADDER_SPRITE)) {
                Vector2 bottomLeft = extractXY(item);
                float width = ((Number) item.get(Constants.LEVEL_WIDTH_KEY)).floatValue();
                float height = ((Number) item.get(Constants.LEVEL_HEIGHT_KEY)).floatValue();
                final Ladder ladder = new Ladder(bottomLeft.x, bottomLeft.y + height, width, height);

                ladderArray.add(ladder);
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

            ladderArray.sort(new Comparator<Ladder>() {
                @Override
                public int compare(Ladder o1, Ladder o2) {
                    if (o1.getTop() < o2.getTop()) {
                        return 1;
                    } else if (o1.getTop() > o2.getTop()) {
                        return -1;
                    }
                    return 0;
                }
            });

            level.getPlatforms().addAll(platformArray);
            level.getGrounds().addAll(platformArray);

            Vector2 ladderPosition = imagePosition.add(Constants.LADDER_CENTER);
            Gdx.app.log(TAG, "Loaded the ladder at " + ladderPosition);
            level.getGrounds().addAll(ladderArray);
        }
    }
}