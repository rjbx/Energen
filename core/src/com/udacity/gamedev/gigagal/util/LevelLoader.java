package com.udacity.gamedev.gigagal.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.udacity.gamedev.gigagal.app.Level;
import com.udacity.gamedev.gigagal.entities.AmmoPowerup;
import com.udacity.gamedev.gigagal.entities.Boss;
import com.udacity.gamedev.gigagal.entities.Cannon;
import com.udacity.gamedev.gigagal.entities.Coals;
import com.udacity.gamedev.gigagal.entities.Ice;
import com.udacity.gamedev.gigagal.entities.Ladder;
import com.udacity.gamedev.gigagal.entities.Lift;
import com.udacity.gamedev.gigagal.entities.Orben;
import com.udacity.gamedev.gigagal.entities.Pillar;
import com.udacity.gamedev.gigagal.entities.Pole;
import com.udacity.gamedev.gigagal.entities.Protrusion;
import com.udacity.gamedev.gigagal.entities.Rollen;
import com.udacity.gamedev.gigagal.entities.Rope;
import com.udacity.gamedev.gigagal.entities.Sink;
import com.udacity.gamedev.gigagal.entities.Slick;
import com.udacity.gamedev.gigagal.entities.Spring;
import com.udacity.gamedev.gigagal.entities.Suspension;
import com.udacity.gamedev.gigagal.entities.Swoopa;
import com.udacity.gamedev.gigagal.entities.Treadmill;
import com.udacity.gamedev.gigagal.entities.TurboPowerup;
import com.udacity.gamedev.gigagal.entities.HealthPowerup;
import com.udacity.gamedev.gigagal.entities.Vines;
import com.udacity.gamedev.gigagal.entities.Portal;
import com.udacity.gamedev.gigagal.entities.GigaGal;
import com.udacity.gamedev.gigagal.entities.Box;
import com.udacity.gamedev.gigagal.entities.Zoomba;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.Comparator;

// immutable static
public final class LevelLoader {

    public static final String TAG = LevelLoader.class.toString();
    private static boolean runtimeEx;

    // non-instantiable; cannot be subclassed
    private LevelLoader() {}

    public static final Level load(String path) throws ParseException, IOException {

        final FileHandle file = Gdx.files.internal(path);
        Level level = new Level();

        JSONParser parser = new JSONParser();
        JSONObject rootJsonObject;
        rootJsonObject = (JSONObject) parser.parse(file.reader());


        JSONObject composite = (JSONObject) rootJsonObject.get(Constants.LEVEL_COMPOSITE);

        JSONArray ninePatches = (JSONArray) composite.get(Constants.LEVEL_9PATCHES);
        loadNinePatches(level, ninePatches);

        JSONArray images = (JSONArray) composite.get(Constants.LEVEL_IMAGES);

        runtimeEx = false;

        loadImages(level, images);

        level.setLoadEx(runtimeEx);

        return level;
    }

    private static final Vector2 extractPosition(JSONObject object) {
        Vector2 position = new Vector2(0, 0);

        try {
            Number x = (Number) object.get(Constants.LEVEL_X_POSITION_KEY);
            Number y = (Number) object.get(Constants.LEVEL_Y_POSITION_KEY);

            position.set(
                    (x == null) ? 0 : x.floatValue(),
                    (y == null) ? 0 : y.floatValue()
            );
        } catch (NumberFormatException ex) {
            runtimeEx = true;
            Gdx.app.log(TAG, Constants.LEVEL_KEY_MESSAGE
                    + "; object: " + object.get(Constants.LEVEL_IMAGENAME_KEY)
                    + "; id: " + object.get(Constants.LEVEL_UNIQUE_ID_KEY)
                    + "; key: " + Constants.LEVEL_X_POSITION_KEY + Constants.LEVEL_Y_POSITION_KEY);
        }

        return position;
    }

    private static final Vector2 extractScale(JSONObject object) {
        Vector2 scale = new Vector2(1, 1);
        try {
            if (object.containsKey(Constants.LEVEL_X_SCALE_KEY)) {
                scale.x = ((Number) object.get(Constants.LEVEL_X_SCALE_KEY)).floatValue();
            }
            if (object.containsKey(Constants.LEVEL_Y_SCALE_KEY)) {
                scale.y = ((Number) object.get(Constants.LEVEL_Y_SCALE_KEY)).floatValue();
            }
        } catch (NumberFormatException ex) {
            runtimeEx = true;
            Gdx.app.log(TAG, Constants.LEVEL_KEY_MESSAGE
                    + "; object: " + object.get(Constants.LEVEL_IMAGENAME_KEY)
                    + "; id: " + object.get(Constants.LEVEL_UNIQUE_ID_KEY)
                    + "; key: " + Constants.LEVEL_X_SCALE_KEY + Constants.LEVEL_Y_SCALE_KEY);
        }
        return scale;
    }

    private static final Enums.Orientation extractOrientation(JSONObject object) {
        Enums.Orientation orientation = Enums.Orientation.Z;
        try {
            if (object.containsKey(Constants.LEVEL_IDENTIFIER_KEY)) {
                String identifierVar = (String) object.get(Constants.LEVEL_IDENTIFIER_KEY);
                orientation = Enums.Orientation.valueOf(identifierVar);
            }
        } catch (IllegalArgumentException ex) {
            runtimeEx = true;
            Gdx.app.log(TAG, Constants.LEVEL_KEY_MESSAGE
                    + "; object: " + object.get(Constants.LEVEL_IMAGENAME_KEY)
                    + "; id: " + object.get(Constants.LEVEL_UNIQUE_ID_KEY)
                    + "; key: " + Constants.LEVEL_IDENTIFIER_KEY);
        }
        return orientation;
    }

    private static final Enums.WeaponType extractType(JSONObject object) {
        Enums.WeaponType type = Enums.WeaponType.NATIVE;
        try {
            if (object.containsKey("customVars")) {
                String[] customVars = ((String) object.get("customVars")).split(";");
                for (String customVar : customVars) {
                    if (customVar.contains(Constants.LEVEL_TYPE_KEY)) {
                        String[] typeSplit = customVar.split(Constants.LEVEL_TYPE_KEY + ":");
                        type = Enums.WeaponType.valueOf(typeSplit[1]);
                    }
                }
            }
        } catch (IllegalArgumentException ex) {
            runtimeEx = true;
            Gdx.app.log(TAG, Constants.LEVEL_KEY_MESSAGE
                    + "; object: " + object.get(Constants.LEVEL_IMAGENAME_KEY)
                    + "; id: " + object.get(Constants.LEVEL_UNIQUE_ID_KEY)
                    + "; key: " + Constants.LEVEL_TYPE_KEY);
        }
        return type;
    }

    private static final Enums.AmmoIntensity extractIntensity(JSONObject object) {
        Enums.AmmoIntensity intensity = Enums.AmmoIntensity.SHOT;
        try {
            if (object.containsKey("customVars")) {
                String[] customVars = ((String) object.get("customVars")).split(";");
                for (String customVar : customVars) {
                    if (customVar.contains(Constants.LEVEL_INTENSITY_KEY)) {
                        String[] intensitySplit = customVar.split(Constants.LEVEL_INTENSITY_KEY + ":");
                        intensity = Enums.AmmoIntensity.valueOf(intensitySplit[1]);
                    }
                }
            }
        } catch (IllegalArgumentException ex) {
            runtimeEx = true;
            Gdx.app.log(TAG, Constants.LEVEL_KEY_MESSAGE
                    + "; object: " + object.get(Constants.LEVEL_IMAGENAME_KEY)
                    + "; id: " + object.get(Constants.LEVEL_UNIQUE_ID_KEY)
                    + "; key: " + Constants.LEVEL_INTENSITY_KEY);
        }
        return intensity;
    }

    private static final float extractRange(JSONObject object) {
        float range = Constants.ZOOMBA_RANGE;
        try {
            if (object.containsKey("customVars")) {
                String[] customVars = ((String) object.get("customVars")).split(";");
                for (String customVar : customVars) {
                    if (customVar.contains(Constants.LEVEL_RANGE_KEY)) {
                        String[] rangeSplit = customVar.split(Constants.LEVEL_RANGE_KEY + ":");
                        range = Float.parseFloat(rangeSplit[1]);
                    }
                }
            }
        } catch (NumberFormatException ex) {
            runtimeEx = true;
            Gdx.app.log(TAG, Constants.LEVEL_KEY_MESSAGE
                    + "; object: " + object.get(Constants.LEVEL_IMAGENAME_KEY)
                    + "; id: " + object.get(Constants.LEVEL_UNIQUE_ID_KEY)
                    + "; key: " + Constants.LEVEL_RANGE_KEY);
        }
        return range;
    }

    private static final void loadImages(Level level, JSONArray images) {
        for (Object o : images) {
            final JSONObject item = (JSONObject) o;

            final Vector2 imagePosition = extractPosition(item);
            final Vector2 scale = extractScale(item);
            final Enums.Orientation orientation = extractOrientation(item);
            final Enums.WeaponType type = extractType(item);
            final Enums.AmmoIntensity intensity = extractIntensity(item);
            final float range = extractRange(item);

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
                level.setGigaGal(new GigaGal(level, gigaGalPosition));
            } else if (item.get(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.BOSS_SPRITE)) {
                final Vector2 gigaGalPosition = imagePosition.add(Constants.GIGAGAL_EYE_POSITION);
                Gdx.app.log(TAG, "Loaded GigaGal at " + gigaGalPosition);
                level.setBoss(new Boss(level, gigaGalPosition));
            } else if (item.get(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.PORTAL_SPRITE_1)) {
                final Vector2 portalPosition = imagePosition.add(Constants.PORTAL_CENTER);
                Gdx.app.log(TAG, "Loaded the exit portal at " + portalPosition);
                level.setPortal(new Portal(portalPosition));
            } else if (item.get(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.SPIKE_SPRITE_1)) {
                final Vector2 spikePosition = imagePosition.add(Constants.SPIKE_CENTER);
                Gdx.app.log(TAG, "Loaded the spike at " + spikePosition);
                level.getIndestructibles().add(new Protrusion(spikePosition, Enums.WeaponType.SOLID));
            } else if (item.get(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.FLAME_SPRITE_1)) {
                final Vector2 flamePosition = imagePosition.add(Constants.FLAME_CENTER);
                Gdx.app.log(TAG, "Loaded the flame at " + flamePosition);
                level.getIndestructibles().add(new Protrusion(flamePosition, Enums.WeaponType.GAS));
            } else if (item.get(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.GEISER_SPRITE_1)) {
                final Vector2 geiserPosition = imagePosition.add(Constants.GEISER_CENTER);
                Gdx.app.log(TAG, "Loaded the geiser at " + geiserPosition);
                level.getIndestructibles().add(new Protrusion(geiserPosition, Enums.WeaponType.LIQUID));
            } else if (item.get(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.WHEEL_SPRITE_1)) {
                final Vector2 wheelPosition = imagePosition.add(Constants.WHEEL_CENTER);
                Gdx.app.log(TAG, "Loaded the wheel at " + wheelPosition);
                level.getIndestructibles().add(new Suspension(wheelPosition, Enums.WeaponType.ORE));
            } else if (item.get(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.COIL_SPRITE_1)) {
                final Vector2 coilPosition = imagePosition.add(Constants.COIL_CENTER);
                Gdx.app.log(TAG, "Loaded the coil at " + coilPosition);
                level.getIndestructibles().add(new Suspension(coilPosition, Enums.WeaponType.PLASMA));
            } else if (item.get(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.VACUUM_SPRITE_1)) {
                final Vector2 vacuumPosition = imagePosition.add(Constants.VACUUM_CENTER);
                Gdx.app.log(TAG, "Loaded the vacuum at " + vacuumPosition);
                level.getIndestructibles().add(new Suspension(vacuumPosition, Enums.WeaponType.ANTIMATTER));
            } else if (item.get(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.ZOOMBA_SPRITE)) {
                final Vector2 zoombaPosition = imagePosition.add(Constants.ZOOMBA_CENTER);
                Gdx.app.log(TAG, "Loaded the zoomba at " + zoombaPosition);
                Zoomba zoomba = new Zoomba(zoombaPosition, type);
                zoomba.setRange(range);
                level.getDestructibles().add(zoomba);
            } else if (item.get(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.SWOOPA_SPRITE)) {
                final Vector2 swoopaPosition = imagePosition.add(Constants.SWOOPA_CENTER);
                Gdx.app.log(TAG, "Loaded the swoopa at " + swoopaPosition);
                Swoopa swoopa = new Swoopa(level, swoopaPosition, type);
                level.getDestructibles().add(swoopa);
            } else if (item.get(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.DORMANTORBEN_SPRITE)) {
                final Vector2 orbenPosition = imagePosition.add(Constants.ORBEN_CENTER);
                Gdx.app.log(TAG, "Loaded the orben at " + orbenPosition);
                level.getDestructibles().add(new Orben(level, orbenPosition, type));
            } else if (item.get(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.WHIRLINGROLLEN_SPRITE_1)) {
                final Vector2 rollenPosition = imagePosition.add(Constants.ROLLEN_CENTER);
                Gdx.app.log(TAG, "Loaded the rollen at " + rollenPosition);
                level.getDestructibles().add(new Rollen(level, rollenPosition, type));
            } else if (item.get(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.X_CANNON_SPRITE)) {
                final Vector2 cannonPosition = imagePosition.add(Constants.X_CANNON_CENTER);
                Gdx.app.log(TAG, "Loaded the cannon at " + cannonPosition);
                level.getGrounds().add(new Cannon(cannonPosition, Enums.Orientation.X, intensity));
            } else if (item.get(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.Y_CANNON_SPRITE)) {
                final Vector2 cannonPosition = imagePosition.add(Constants.Y_CANNON_CENTER);
                Gdx.app.log(TAG, "Loaded the cannon at " + cannonPosition);
                level.getGrounds().add(new Cannon(cannonPosition, Enums.Orientation.Y, intensity));
            } else if (item.get(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.PILLAR_SPRITE)) {
                final Vector2 pillarPosition = imagePosition.add(Constants.PILLAR_CENTER);
                Gdx.app.log(TAG, "Loaded the pillar at " + pillarPosition);
                level.getGrounds().add(new Pillar(pillarPosition));
            } else if (item.get(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.LIFT_SPRITE)) {
                final Vector2 liftPosition = imagePosition.add(Constants.LIFT_CENTER);
                Lift lift = new Lift(liftPosition, orientation);
                lift.setRange(range);
                Gdx.app.log(TAG, "Loaded the lift at " + liftPosition);
                level.getGrounds().add(lift);
            } else if (item.get(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.ROPE_SPRITE)) {
                final Vector2 ropePosition = imagePosition.add(Constants.ROPE_CENTER);
                Gdx.app.log(TAG, "Loaded the rope at " + ropePosition);
                level.getGrounds().add(new Rope(ropePosition));
            } else if (item.get(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.VINES_SPRITE)) {
                final Vector2 vinesPosition = imagePosition.add(Constants.VINES_CENTER);
                Gdx.app.log(TAG, "Loaded the vines at " + vinesPosition);
                level.getGrounds().add(new Vines(vinesPosition));
            } else if (item.get(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.POLE_SPRITE_1)) {
                final Vector2 polePosition = imagePosition.add(Constants.POLE_CENTER);
                Gdx.app.log(TAG, "Loaded the pole at " + polePosition);
                level.getGrounds().add(new Pole(polePosition));
            } else if (item.get(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.SINK_SPRITE_1)) {
                final Vector2 sinkPosition = imagePosition.add(Constants.SINK_CENTER);
                Gdx.app.log(TAG, "Loaded the sink at " + sinkPosition);
                level.getGrounds().add(new Sink(sinkPosition));
            } else if (item.get(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.SPRING_SPRITE_1)) {
                final Vector2 springPosition = imagePosition.add(Constants.SPRING_CENTER);
                Gdx.app.log(TAG, "Loaded the spring at " + springPosition);
                level.getGrounds().add(new Spring(springPosition));
            } else if (item.get(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.SLICK_SPRITE_1)) {
                Vector2 adjustedCenter = new Vector2(Constants.SLICK_CENTER.x * scale.x, Constants.SLICK_CENTER.y * scale.y);
                final Vector2 slickPosition = imagePosition.add(Constants.SLICK_CENTER);
                final Slick slick = new Slick(slickPosition, scale, adjustedCenter);
                level.getGrounds().add(slick);
                Gdx.app.log(TAG, "Loaded the slick at " + slickPosition);
            } else if (item.get(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.ICE_SPRITE_1)) {
                Vector2 adjustedCenter = new Vector2(Constants.ICE_CENTER.x * scale.x, Constants.ICE_CENTER.y * scale.y);
                final Vector2 icePosition = imagePosition.add(Constants.ICE_CENTER);
                final Ice ice = new Ice(icePosition, scale, adjustedCenter);
                level.getGrounds().add(ice);
                Gdx.app.log(TAG, "Loaded the ice at " + icePosition);
            } else if (item.get(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.COALS_SPRITE_1)) {
                Vector2 adjustedCenter = new Vector2(Constants.COALS_CENTER.x * scale.x, Constants.COALS_CENTER.y * scale.y);
                final Vector2 coalsPosition = imagePosition.add(Constants.COALS_CENTER);
                final Coals coals = new Coals(coalsPosition, scale, adjustedCenter);
                level.getGrounds().add(coals);
                Gdx.app.log(TAG, "Loaded the coals at " + coalsPosition);
            } else if (item.get(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.TREADMILL_1_LEFT)) {
                Vector2 adjustedCenter = new Vector2(Constants.TREADMILL_CENTER.x * scale.x, Constants.TREADMILL_CENTER.y * scale.y);
                final Vector2 treadmillPosition = imagePosition.add(Constants.TREADMILL_CENTER);
                final Treadmill treadmill = new Treadmill(treadmillPosition, scale, adjustedCenter, Enums.Direction.LEFT);
                level.getGrounds().add(treadmill);
                Gdx.app.log(TAG, "Loaded the treadmill at " + treadmillPosition);
            } else if (item.get(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.TREADMILL_1_RIGHT)) {
                Vector2 adjustedCenter = new Vector2(Constants.TREADMILL_CENTER.x * scale.x, Constants.TREADMILL_CENTER.y * scale.y);
                final Vector2 treadmillPosition = imagePosition.add(Constants.TREADMILL_CENTER);
                final Treadmill treadmill = new Treadmill(treadmillPosition, scale, adjustedCenter, Enums.Direction.RIGHT);
                level.getGrounds().add(treadmill);
                Gdx.app.log(TAG, "Loaded the treadmill at " + treadmillPosition);
            }
        }
    }

    private static final void loadNinePatches(Level level, JSONArray ninePatches) {

        Array<Box> boxArray = new Array<Box>();
        Array<Ladder> ladderArray = new Array<Ladder>();
        
        for (Object o : ninePatches) {
            final JSONObject item = (JSONObject) o;

            final Vector2 imagePosition = extractPosition(item);
            final Enums.WeaponType type = extractType(item);

            if (item.get(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.BOX_SPRITE)) {
                float width = ((Number) item.get(Constants.LEVEL_WIDTH_KEY)).floatValue();
                float height = ((Number) item.get(Constants.LEVEL_HEIGHT_KEY)).floatValue();
                final Rectangle shape = new Rectangle(imagePosition.x, imagePosition.y, width, height);
                final Box box = new Box(shape, type.levelName());
                boxArray.add(box);
                Gdx.app.log(TAG, "Loaded the box at " + imagePosition.add(new Vector2(width / 2, height / 2)));
            } else if (item.get(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.LADDER_SPRITE)) {
                float width = ((Number) item.get(Constants.LEVEL_WIDTH_KEY)).floatValue();
                float height = ((Number) item.get(Constants.LEVEL_HEIGHT_KEY)).floatValue();
                final Ladder ladder = new Ladder(imagePosition.x, imagePosition.y + height, width, height);
                ladderArray.add(ladder);
                Gdx.app.log(TAG, "Loaded the ladder at " + imagePosition.add(new Vector2(width / 2, height / 2)));
            }

            boxArray.sort(new Comparator<Box>() {
                @Override
                public int compare(Box o1, Box o2) {
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

            level.getBoxes().addAll(boxArray);
            level.getGrounds().addAll(boxArray);
            level.getGrounds().addAll(ladderArray);
        }
    }
}