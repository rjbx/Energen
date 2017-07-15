package com.udacity.gamedev.gigagal.app;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.udacity.gamedev.gigagal.entity.Boss;
import com.udacity.gamedev.gigagal.entity.Box;
import com.udacity.gamedev.gigagal.entity.Canirol;
import com.udacity.gamedev.gigagal.entity.Cannon;
import com.udacity.gamedev.gigagal.entity.Chamber;
import com.udacity.gamedev.gigagal.entity.Coals;
import com.udacity.gamedev.gigagal.entity.Gate;
import com.udacity.gamedev.gigagal.entity.Ice;
import com.udacity.gamedev.gigagal.entity.Knob;
import com.udacity.gamedev.gigagal.entity.Ladder;
import com.udacity.gamedev.gigagal.entity.Lava;
import com.udacity.gamedev.gigagal.entity.Lift;
import com.udacity.gamedev.gigagal.entity.Orben;
import com.udacity.gamedev.gigagal.entity.Pillar;
import com.udacity.gamedev.gigagal.entity.Pod;
import com.udacity.gamedev.gigagal.entity.Pole;
import com.udacity.gamedev.gigagal.entity.Powerup;
import com.udacity.gamedev.gigagal.entity.Protrusion;
import com.udacity.gamedev.gigagal.entity.Rollen;
import com.udacity.gamedev.gigagal.entity.Rope;
import com.udacity.gamedev.gigagal.entity.Sink;
import com.udacity.gamedev.gigagal.entity.Slick;
import com.udacity.gamedev.gigagal.entity.Spring;
import com.udacity.gamedev.gigagal.entity.Suspension;
import com.udacity.gamedev.gigagal.entity.Teleport;
import com.udacity.gamedev.gigagal.entity.Tripchamber;
import com.udacity.gamedev.gigagal.entity.Tripknob;
import com.udacity.gamedev.gigagal.entity.Swoopa;
import com.udacity.gamedev.gigagal.entity.Treadmill;
import com.udacity.gamedev.gigagal.entity.Triptread;
import com.udacity.gamedev.gigagal.entity.Vines;
import com.udacity.gamedev.gigagal.entity.Portal;
import com.udacity.gamedev.gigagal.entity.GigaGal;
import com.udacity.gamedev.gigagal.entity.Barrier;
import com.udacity.gamedev.gigagal.entity.Waves;
import com.udacity.gamedev.gigagal.entity.Zoomba;
import com.udacity.gamedev.gigagal.util.ChaseCam;
import com.udacity.gamedev.gigagal.util.Constants;
import com.udacity.gamedev.gigagal.util.Enums;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.Comparator;

// immutable non-instantiable static
final class LevelLoader {

    public static final String TAG = LevelLoader.class.toString();
    private static boolean runtimeEx;

    // cannot be subclassed
    private LevelLoader() {}

    protected static final void load(Enums.Theme level) throws ParseException, IOException {

        LevelUpdater.getInstance().setTheme(level);

        final FileHandle file = Gdx.files.internal("levels/" + level + ".dt");

        JSONParser parser = new JSONParser();
        JSONObject rootJsonObject;
        rootJsonObject = (JSONObject) parser.parse(file.reader());

        JSONObject composite = (JSONObject) rootJsonObject.get(Constants.LEVEL_COMPOSITE);

        JSONArray ninePatches = (JSONArray) composite.get(Constants.LEVEL_9PATCHES);
        loadNinePatches(LevelUpdater.getInstance(), ninePatches);

        JSONArray images = (JSONArray) composite.get(Constants.LEVEL_IMAGES);

        runtimeEx = false;

        loadImages(LevelUpdater.getInstance(), images);

        LevelUpdater.getInstance().setLoadEx(runtimeEx);
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

    private static final float extractRotation(JSONObject object) {
        float rotation = 0;
        try {
            if (object.containsKey(Constants.LEVEL_ROTATION_KEY)) {
                rotation = ((Number) object.get(Constants.LEVEL_ROTATION_KEY)).floatValue();
            }
        } catch (NumberFormatException ex) {
            runtimeEx = true;
            Gdx.app.log(TAG, Constants.LEVEL_KEY_MESSAGE
                    + "; object: " + object.get(Constants.LEVEL_IMAGENAME_KEY)
                    + "; id: " + object.get(Constants.LEVEL_UNIQUE_ID_KEY)
                    + "; key: " + Constants.LEVEL_ROTATION_KEY);
        }
        return rotation;
    }

    private static final Enums.Orientation extractOrientation(JSONObject object) {
        Enums.Orientation orientation = Enums.Orientation.X;
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

    private static final Enums.Direction extractDirection(JSONObject object) {
        Enums.Direction direction = Enums.Direction.RIGHT;
        try {
            if (object.containsKey(Constants.LEVEL_CUSTOM_VARS_KEY)) {
                String[] customVars = ((String) object.get(Constants.LEVEL_CUSTOM_VARS_KEY)).split(";");
                for (String customVar : customVars) {
                    if (customVar.contains(Constants.LEVEL_DIRECTION_KEY)) {
                        String[] directionSplit = customVar.split(Constants.LEVEL_DIRECTION_KEY + ":");
                        direction = Enums.Direction.valueOf(directionSplit[1]);
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
        return direction;
    }

    private static final Enums.Material extractType(JSONObject object) {
        Enums.Material type = Enums.Material.NATIVE;
        try {
            if (object.containsKey(Constants.LEVEL_CUSTOM_VARS_KEY)) {
                String[] customVars = ((String) object.get(Constants.LEVEL_CUSTOM_VARS_KEY)).split(";");
                for (String customVar : customVars) {
                    if (customVar.contains(Constants.LEVEL_TYPE_KEY)) {
                        String[] typeSplit = customVar.split(Constants.LEVEL_TYPE_KEY + ":");
                        type = Enums.Material.valueOf(typeSplit[1]);
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

    private static final Enums.ShotIntensity extractIntensity(JSONObject object) {
        Enums.ShotIntensity intensity = Enums.ShotIntensity.NORMAL;
        try {
            if (object.containsKey(Constants.LEVEL_CUSTOM_VARS_KEY)) {
                String[] customVars = ((String) object.get(Constants.LEVEL_CUSTOM_VARS_KEY)).split(";");
                for (String customVar : customVars) {
                    if (customVar.contains(Constants.LEVEL_INTENSITY_KEY)) {
                        String[] intensitySplit = customVar.split(Constants.LEVEL_INTENSITY_KEY + ":");
                        intensity = Enums.ShotIntensity.valueOf(intensitySplit[1]);
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

    private static final Rectangle extractBounds(JSONObject object) {
        Rectangle bounds = new Rectangle(0, 0, 0, 0);
        try {
            if (object.containsKey(Constants.LEVEL_CUSTOM_VARS_KEY)) {
                String[] customVars = ((String) object.get(Constants.LEVEL_CUSTOM_VARS_KEY)).split(";");
                for (String customVar : customVars) {
                    if (customVar.contains(Constants.LEVEL_BOUNDS_KEY)) {
                        String[] boundsSplit = customVar.split(Constants.LEVEL_BOUNDS_KEY + ":");
                        String[] paramSplit = boundsSplit[1].split(",");
                        bounds.set(Float.parseFloat(paramSplit[0]), Float.parseFloat(paramSplit[1]), Float.parseFloat(paramSplit[2]) - Float.parseFloat(paramSplit[0]), Float.parseFloat(paramSplit[3]) - Float.parseFloat(paramSplit[1]));
                    }
                }
            }
        } catch (NumberFormatException ex) {
            runtimeEx = true;
            Gdx.app.log(TAG, Constants.LEVEL_KEY_MESSAGE
                    + "; object: " + object.get(Constants.LEVEL_IMAGENAME_KEY)
                    + "; id: " + object.get(Constants.LEVEL_UNIQUE_ID_KEY)
                    + "; key: " + Constants.LEVEL_BOUNDS_KEY);
        }
        return bounds;
    }

    private static final float extractRange(JSONObject object) {
        float range = Constants.ZOOMBA_RANGE;
        try {
            if (object.containsKey(Constants.LEVEL_CUSTOM_VARS_KEY)) {
                String[] customVars = ((String) object.get(Constants.LEVEL_CUSTOM_VARS_KEY)).split(";");
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

    private static final Vector2 extractDestination(JSONObject object) {
        Vector2 destination = new Vector2(0, 0);
        try {
            if (object.containsKey(Constants.LEVEL_CUSTOM_VARS_KEY)) {
                String[] customVars = ((String) object.get(Constants.LEVEL_CUSTOM_VARS_KEY)).split(";");
                for (String customVar : customVars) {
                    if (customVar.contains(Constants.LEVEL_DESTINATION_KEY)) {
                        String[] destinationSplit = customVar.split(Constants.LEVEL_DESTINATION_KEY + ":");
                        String[] paramSplit = destinationSplit[1].split(",");
                        destination.set(Float.parseFloat(paramSplit[0]), Float.parseFloat(paramSplit[1]));
                    }
                }
            }
        } catch (NumberFormatException ex) {
            runtimeEx = true;
            Gdx.app.log(TAG, Constants.LEVEL_KEY_MESSAGE
                    + "; object: " + object.get(Constants.LEVEL_IMAGENAME_KEY)
                    + "; id: " + object.get(Constants.LEVEL_UNIQUE_ID_KEY)
                    + "; key: " + Constants.LEVEL_DESTINATION_KEY);
        }
        return destination;
    }

    private static final Enums.Upgrade extractUpgrade(JSONObject object) {
       Enums.Upgrade upgrade = Enums.Upgrade.NONE;
        try {
            if (object.containsKey(Constants.LEVEL_CUSTOM_VARS_KEY)) {
                String[] customVars = ((String) object.get(Constants.LEVEL_CUSTOM_VARS_KEY)).split(";");
                for (String customVar : customVars) {
                    if (customVar.contains(Constants.LEVEL_UPGRADE_KEY)) {
                        String[] upgradeSplit = customVar.split(Constants.LEVEL_UPGRADE_KEY + ":");
                        upgrade = Enums.Upgrade.valueOf(upgradeSplit[1]);
                    }
                }
            }
        } catch (NumberFormatException ex) {
            runtimeEx = true;
            Gdx.app.log(TAG, Constants.LEVEL_KEY_MESSAGE
                    + "; object: " + object.get(Constants.LEVEL_IMAGENAME_KEY)
                    + "; id: " + object.get(Constants.LEVEL_UNIQUE_ID_KEY)
                    + "; key: " + Constants.LEVEL_UPGRADE_KEY);
        }
        return upgrade;
    }

    private static final boolean[] extractTags(JSONObject object) {
        boolean[] tagBooleans = {false, false, true};
        try {
            if (object.containsKey(Constants.LEVEL_TAGS_KEY)) {
                JSONArray tags = (JSONArray) object.get(Constants.LEVEL_TAGS_KEY);
                for (Object tag : tags) {
                    String item = (String) tag;
                    if (item.equals(Constants.LEDGE_TAG)) {
                        tagBooleans[Constants.LEDGE_TAG_INDEX] = true;
                    }
                    if (item.equals(Constants.ON_TAG)) {
                        tagBooleans[Constants.ON_TAG_INDEX] = true;
                    }
                    if (item.equals(Constants.OFF_TAG)) {
                        tagBooleans[Constants.OFF_TAG_INDEX] = false;
                    }
                }
            }
        } catch (NumberFormatException ex) {
            runtimeEx = true;
            Gdx.app.log(TAG, Constants.LEVEL_KEY_MESSAGE
                    + "; object: " + object.get(Constants.LEVEL_IMAGENAME_KEY)
                    + "; id: " + object.get(Constants.LEVEL_UNIQUE_ID_KEY)
                    + "; tag: " + Constants.LEVEL_TAGS_KEY);
        }
        return tagBooleans;
    }

    private static final void loadImages(LevelUpdater level, JSONArray images) {
        for (Object o : images) {
            final JSONObject item = (JSONObject) o;

            final Vector2 imagePosition = extractPosition(item);
            final Vector2 scale = extractScale(item);
            final float rotation = extractRotation(item);
            final Vector2 destination = extractDestination(item);
            final Enums.Orientation orientation = extractOrientation(item);
            final Enums.Direction direction = extractDirection(item);
            final Enums.Material type = extractType(item);
            final Enums.ShotIntensity intensity = extractIntensity(item);
            final Enums.Upgrade upgrade = extractUpgrade(item);
            final Rectangle bounds = extractBounds(item);
            final float range = extractRange(item);
            final boolean[] tags = extractTags(item);

            if (item.get(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.AMMO_POWERUP_SPRITE)) {
                final Vector2 powerupPosition = imagePosition.add(Constants.AMMO_POWERUP_CENTER);
                Gdx.app.log(TAG, "Loaded an AmmoPowerup at " + powerupPosition);
                level.addPowerup(new Powerup(powerupPosition, Enums.PowerupType.AMMO));
            } else if (item.get(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.HEALTH_POWERUP_SPRITE)) {
                final Vector2 powerupPosition = imagePosition.add(Constants.HEALTH_POWERUP_CENTER);
                Gdx.app.log(TAG, "Loaded a HealthPowerup at " + powerupPosition);
                level.addPowerup(new Powerup(powerupPosition, Enums.PowerupType.HEALTH));
            } else if (item.get(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.TURBO_POWERUP_SPRITE)) {
                final Vector2 powerupPosition = imagePosition.add(Constants.TURBO_POWERUP_CENTER);
                Gdx.app.log(TAG, "Loaded a TurboPowerup at " + powerupPosition);
                level.addPowerup(new Powerup(powerupPosition, Enums.PowerupType.TURBO));
            } else if (item.get(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.LIFE_POWERUP_SPRITE)) {
                final Vector2 powerupPosition = imagePosition.add(Constants.LIFE_POWERUP_CENTER);
                Gdx.app.log(TAG, "Loaded a LifePowerup at " + powerupPosition);
                level.addPowerup(new Powerup(powerupPosition, Enums.PowerupType.LIFE));
            } else if (item.get(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.CANNON_POWERUP_SPRITE)) {
                final Vector2 powerupPosition = imagePosition.add(Constants.CANNON_POWERUP_CENTER);
                Gdx.app.log(TAG, "Loaded a CannonPowerup at " + powerupPosition);
                level.addPowerup(new Powerup(powerupPosition, Enums.PowerupType.CANNON));
            } else if (item.get(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.STAND_RIGHT)) {
                final Vector2 gigaGalPosition = imagePosition.add(Constants.GIGAGAL_EYE_POSITION);
                Gdx.app.log(TAG, "Loaded GigaGal at " + gigaGalPosition);
                GigaGal.getInstance().setSpawnPosition(gigaGalPosition);
                GigaGal.getInstance().setLevel(level);
            } else if (item.get(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.BOSS_SPRITE)) {
                final Vector2 bossPosition = imagePosition.add(Constants.GIGAGAL_EYE_POSITION);
                Gdx.app.log(TAG, "Loaded Boss at " + bossPosition);
                Boss boss = new Boss.Builder(level, bossPosition).weapon(level.getType()).build();
                level.addHazard(boss);
                level.setBoss(boss);
                ChaseCam.getInstance().setRoomPosition(bossPosition);
            } else if (item.get(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.PORTAL_SPRITE_1)) {
                final Vector2 portalPosition = imagePosition.add(Constants.PORTAL_CENTER);
                Gdx.app.log(TAG, "Loaded the exit portal at " + portalPosition);
                level.getTransports().add(new Portal(portalPosition));
            } else if (item.get(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.TELEPORT_SPRITE_1)) {
                final Vector2 teleportPosition = imagePosition.add(Constants.TELEPORT_CENTER);
                Gdx.app.log(TAG, "Loaded the exit teleport at " + teleportPosition);
                level.getTransports().add(new Teleport(teleportPosition, destination));
            } else if (item.get(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.PROTRUSION_ORE_SPRITE_1)) {
                final Vector2 spikePosition = imagePosition.add(Constants.PROTRUSION_ORE_CENTER);
                Gdx.app.log(TAG, "Loaded the protrusion at " + spikePosition);
                level.addHazard(new Protrusion(spikePosition, Enums.Material.ORE, rotation));
            } else if (item.get(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.PROTRUSION_PLASMA_SPRITE_1)) {
                final Vector2 spikePosition = imagePosition.add(Constants.PROTRUSION_SOLID_CENTER);
                Gdx.app.log(TAG, "Loaded the protrusion at " + spikePosition);
                level.addHazard(new Protrusion(spikePosition, Enums.Material.PLASMA, rotation));
            } else if (item.get(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.PROTRUSION_GAS_SPRITE_1)) {
                final Vector2 spikePosition = imagePosition.add(Constants.PROTRUSION_GAS_CENTER);
                Gdx.app.log(TAG, "Loaded the protrusion at " + spikePosition);
                level.addHazard(new Protrusion(spikePosition, Enums.Material.GAS, rotation));
            } else if (item.get(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.PROTRUSION_LIQUID_SPRITE_1)) {
                final Vector2 spikePosition = imagePosition.add(Constants.PROTRUSION_LIQUID_CENTER);
                Gdx.app.log(TAG, "Loaded the protrusion at " + spikePosition);
                level.addHazard(new Protrusion(spikePosition, Enums.Material.LIQUID, rotation));
            } else if (item.get(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.PROTRUSION_SOLID_SPRITE_1)) {
                final Vector2 spikePosition = imagePosition.add(Constants.PROTRUSION_SOLID_CENTER);
                Gdx.app.log(TAG, "Loaded the protrusion at " + spikePosition);
                level.addHazard(new Protrusion(spikePosition, Enums.Material.SOLID, rotation));
            } else if (item.get(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.SUSPENSION_ORE_SPRITE_1)) {
                final Vector2 suspensionPosition = imagePosition.add(Constants.SUSPENSION_ORE_CENTER);
                Gdx.app.log(TAG, "Loaded the suspension at " + suspensionPosition);
                level.addHazard(new Suspension(suspensionPosition, Enums.Material.ORE));
            } else if (item.get(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.SUSPENSION_PLASMA_SPRITE_1)) {
                final Vector2 suspensionPosition = imagePosition.add(Constants.SUSPENSION_SOLID_CENTER);
                Gdx.app.log(TAG, "Loaded the suspension at " + suspensionPosition);
                level.addHazard(new Suspension(suspensionPosition, Enums.Material.PLASMA));
            } else if (item.get(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.SUSPENSION_GAS_SPRITE_1)) {
                final Vector2 suspensionPosition = imagePosition.add(Constants.SUSPENSION_GAS_CENTER);
                Gdx.app.log(TAG, "Loaded the suspension at " + suspensionPosition);
                level.addHazard(new Suspension(suspensionPosition, Enums.Material.GAS));
            } else if (item.get(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.SUSPENSION_LIQUID_SPRITE_1)) {
                final Vector2 suspensionPosition = imagePosition.add(Constants.SUSPENSION_LIQUID_CENTER);
                Gdx.app.log(TAG, "Loaded the suspension at " + suspensionPosition);
                level.addHazard(new Suspension(suspensionPosition, Enums.Material.LIQUID));
            } else if (item.get(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.SUSPENSION_SOLID_SPRITE_1)) {
                final Vector2 suspensionPosition = imagePosition.add(Constants.SUSPENSION_SOLID_CENTER);
                Gdx.app.log(TAG, "Loaded the suspension at " + suspensionPosition);
                level.addHazard(new Suspension(suspensionPosition, Enums.Material.SOLID));
            } else if (item.get(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.X_CANIROL_SPRITE_1)) {
                final Vector2 canirolPosition = imagePosition.add(Constants.X_CANIROL_CENTER);
                Gdx.app.log(TAG, "Loaded the zoomba at " + canirolPosition);
                level.addGround(new Canirol(canirolPosition, orientation, direction, intensity, range, tags[Constants.OFF_TAG_INDEX]));
            } else if (item.get(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.ZOOMBA_SPRITE)) {
                final Vector2 zoombaPosition = imagePosition.add(Constants.ZOOMBA_CENTER);
                Gdx.app.log(TAG, "Loaded the zoomba at " + zoombaPosition);
                level.addHazard(new Zoomba(zoombaPosition, orientation, type, range));
            } else if (item.get(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.SWOOPA_SPRITE_LEFT)) {
                final Vector2 swoopaPosition = imagePosition.add(Constants.SWOOPA_CENTER);
                Gdx.app.log(TAG, "Loaded the swoopa at " + swoopaPosition);
                level.addHazard(new Swoopa(level, swoopaPosition, Enums.Direction.LEFT, type));
            } else if (item.get(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.SWOOPA_SPRITE_RIGHT)) {
                final Vector2 swoopaPosition = imagePosition.add(Constants.SWOOPA_CENTER);
                Gdx.app.log(TAG, "Loaded the swoopa at " + swoopaPosition);
                level.addHazard(new Swoopa(level, swoopaPosition, Enums.Direction.RIGHT, type));
            } else if (item.get(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.ORBEN_SPRITE)) {
                final Vector2 orbenPosition = imagePosition.add(Constants.ORBEN_CENTER);
                Gdx.app.log(TAG, "Loaded the orben at " + orbenPosition);
                level.addHazard(new Orben(level, orbenPosition, type));
            } else if (item.get(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.ROLLEN_ORE_SPRITE_1)) {
                final Vector2 rollenPosition = imagePosition.add(Constants.ROLLEN_CENTER);
                Gdx.app.log(TAG, "Loaded the rollen at " + rollenPosition);
                level.addHazard(new Rollen(level, rollenPosition, type));
            } else if (item.get(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.X_CANNON_SPRITE)) {
                final Vector2 cannonPosition = imagePosition.add(Constants.X_CANNON_CENTER);
                Gdx.app.log(TAG, "Loaded the cannon at " + cannonPosition);
                level.addGround(new Cannon(cannonPosition, Enums.Orientation.X, intensity, tags[Constants.OFF_TAG_INDEX]));
            } else if (item.get(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.Y_CANNON_SPRITE)) {
                final Vector2 cannonPosition = imagePosition.add(Constants.Y_CANNON_CENTER);
                Gdx.app.log(TAG, "Loaded the cannon at " + cannonPosition);
                level.addGround(new Cannon(cannonPosition, Enums.Orientation.Y, intensity, tags[Constants.OFF_TAG_INDEX]));
            } else if (item.get(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.PILLAR_SPRITE)) {
                final Vector2 pillarPosition = imagePosition.add(Constants.PILLAR_CENTER);
                Gdx.app.log(TAG, "Loaded the pillar at " + pillarPosition);
                level.addGround(new Pillar(pillarPosition));
            } else if (item.get(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.KNOB_SPRITE_1)) {
                final Vector2 knobPosition = imagePosition.add(Constants.KNOB_CENTER);
                Gdx.app.log(TAG, "Loaded the knob at " + knobPosition);
                level.addGround(new Knob(knobPosition));
            } else if (item.get(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.CHAMBER_SPRITE)) {
                final Vector2 chamberPosition = imagePosition.add(Constants.CHAMBER_CENTER);
                Gdx.app.log(TAG, "Loaded the chamber at " + chamberPosition);
                Chamber chamber = new Chamber(chamberPosition);
                chamber.setUpgrade(upgrade);
                level.addGround(chamber);
            } else if (item.get(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.LIFT_SPRITE)) {
                final Vector2 liftPosition = imagePosition.add(Constants.LIFT_CENTER);
                Lift lift = new Lift(liftPosition, orientation, range);
                Gdx.app.log(TAG, "Loaded the lift at " + liftPosition);
                level.addGround(lift);
            } else if (item.get(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.ROPE_SPRITE)) {
                final Vector2 ropePosition = imagePosition.add(Constants.ROPE_CENTER);
                Gdx.app.log(TAG, "Loaded the rope at " + ropePosition);
                level.addGround(new Rope(ropePosition));
            } else if (item.get(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.VINES_SPRITE)) {
                Vector2 adjustedCenter = new Vector2(Constants.VINES_CENTER.x * scale.x, Constants.VINES_CENTER.y * scale.y);
                final Vector2 vinesPosition = imagePosition.add(Constants.VINES_CENTER);
                Gdx.app.log(TAG, "Loaded the vines at " + vinesPosition);
                level.addGround(new Vines(vinesPosition, scale, adjustedCenter));
            } else if (item.get(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.POLE_SPRITE_1)) {
                final Vector2 polePosition = imagePosition.add(Constants.POLE_CENTER);
                Gdx.app.log(TAG, "Loaded the pole at " + polePosition);
                level.addGround(new Pole(polePosition));
            } else if (item.get(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.SINK_SPRITE_1)) {
                final Vector2 sinkPosition = imagePosition.add(Constants.SINK_CENTER);
                Gdx.app.log(TAG, "Loaded the sink at " + sinkPosition);
                level.addGround(new Sink(sinkPosition));
            } else if (item.get(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.SPRING_SPRITE_1)) {
                final Vector2 springPosition = imagePosition.add(Constants.SPRING_CENTER);
                Gdx.app.log(TAG, "Loaded the spring at " + springPosition);
                level.addGround(new Spring(springPosition));
            } else if (item.get(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.TRIPKNOB_SPRITE_1)) {
                final Vector2 tripPosition = imagePosition.add(Constants.TRIPKNOB_CENTER);
                Gdx.app.log(TAG, "Loaded the convert at " + tripPosition);
                Tripknob trip = new Tripknob(level, tripPosition, bounds, rotation, tags[Constants.ON_TAG_INDEX]);
                level.addGround(trip);
            } else if (item.get(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.TRIPTREAD_SPRITE_1_LEFT_OFF)) {
                final Vector2 tripPosition = imagePosition.add(Constants.TRIPTREAD_CENTER);
                Gdx.app.log(TAG, "Loaded the convert at " + tripPosition);
                Triptread trip = new Triptread(level, tripPosition, bounds, tags[Constants.ON_TAG_INDEX], Enums.Direction.LEFT);
                level.addGround(trip);
            } else if (item.get(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.TRIPCHAMBER_SPRITE_1_OFF)) {
                final Vector2 tripPosition = imagePosition.add(Constants.TRIPCHAMBER_CENTER);
                Gdx.app.log(TAG, "Loaded the convert at " + tripPosition);
                Tripchamber trip = new Tripchamber(level, tripPosition, bounds, tags[Constants.ON_TAG_INDEX]);
                level.addGround(trip);
            } else if (item.get(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.POD_SPRITE_1)) {
                final Vector2 podPosition = imagePosition.add(Constants.POD_CENTER);
                Gdx.app.log(TAG, "Loaded the pod at " + podPosition);
                Pod pod = new Pod(podPosition);
                level.addGround(pod);
            } else if (item.get(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.SLICK_SPRITE_1)) {
                Vector2 adjustedCenter = new Vector2(Constants.SLICK_CENTER.x * scale.x, Constants.SLICK_CENTER.y * scale.y);
                final Vector2 slickPosition = imagePosition.add(Constants.SLICK_CENTER);
                final Slick slick = new Slick(slickPosition, scale, adjustedCenter);
                level.addGround(slick);
                Gdx.app.log(TAG, "Loaded the slick at " + slickPosition);
            } else if (item.get(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.ICE_SPRITE_1)) {
                Vector2 adjustedCenter = new Vector2(Constants.ICE_CENTER.x * scale.x, Constants.ICE_CENTER.y * scale.y);
                final Vector2 icePosition = imagePosition.add(Constants.ICE_CENTER);
                final Ice ice = new Ice(icePosition, scale, adjustedCenter);
                level.addGround(ice);
                Gdx.app.log(TAG, "Loaded the ice at " + icePosition);
            } else if (item.get(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.COALS_SPRITE_1)) {
                Vector2 adjustedCenter = new Vector2(Constants.COALS_CENTER.x * scale.x, Constants.COALS_CENTER.y * scale.y);
                final Vector2 coalsPosition = imagePosition.add(Constants.COALS_CENTER);
                final Coals coals = new Coals(coalsPosition, scale, adjustedCenter);
                level.addGround(coals);
                Gdx.app.log(TAG, "Loaded the coals at " + coalsPosition);
            } else if (item.get(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.LAVA_SPRITE_1)) {
                Vector2 adjustedCenter = new Vector2(Constants.LAVA_CENTER.x * scale.x, Constants.LAVA_CENTER.y * scale.y);
                final Vector2 lavaPosition = imagePosition.add(Constants.LAVA_CENTER);
                final Lava lava = new Lava(lavaPosition, scale, adjustedCenter);
                level.addGround(lava);
                Gdx.app.log(TAG, "Loaded the lava at " + lavaPosition);
            } else if (item.get(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.WAVES_SPRITE_1)) {
                Vector2 adjustedCenter = new Vector2(Constants.WAVES_CENTER.x * scale.x, Constants.WAVES_CENTER.y * scale.y);
                final Vector2 wavesPosition = imagePosition.add(Constants.WAVES_CENTER);
                final Waves waves = new Waves(wavesPosition, scale, adjustedCenter);
                level.addGround(waves);
                Gdx.app.log(TAG, "Loaded the waves at " + wavesPosition);
            } else if (item.get(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.TREADMILL_SPRITE_1_LEFT)) {
                Vector2 adjustedCenter = new Vector2(Constants.TREADMILL_CENTER.x * scale.x, Constants.TREADMILL_CENTER.y * scale.y);
                final Vector2 treadmillPosition = imagePosition.add(Constants.TREADMILL_CENTER);
                final Treadmill treadmill = new Treadmill(treadmillPosition, scale, adjustedCenter, Enums.Direction.LEFT);
                level.addGround(treadmill);
                Gdx.app.log(TAG, "Loaded the treadmill at " + treadmillPosition);
            } else if (item.get(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.TREADMILL_SPRITE_1_RIGHT)) {
                Vector2 adjustedCenter = new Vector2(Constants.TREADMILL_CENTER.x * scale.x, Constants.TREADMILL_CENTER.y * scale.y);
                final Vector2 treadmillPosition = imagePosition.add(Constants.TREADMILL_CENTER);
                final Treadmill treadmill = new Treadmill(treadmillPosition, scale, adjustedCenter, Enums.Direction.RIGHT);
                level.addGround(treadmill);
                Gdx.app.log(TAG, "Loaded the treadmill at " + treadmillPosition);
            } else if (item.get(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.GATE_SPRITE_0)) {
                final Vector2 gatePosition = imagePosition.add(Constants.GATE_CENTER);
                final Gate gate = new Gate(gatePosition);
                level.addGround(gate);
                Gdx.app.log(TAG, "Loaded the gate at " + gatePosition);
            }
        }
    }

    private static final void loadNinePatches(LevelUpdater level, JSONArray ninePatches) {

        Array<Barrier> blockArray = new Array<Barrier>();
        Array<Box> boxArray = new Array<Box>();
        Array<Ladder> ladderArray = new Array<Ladder>();

        for (Object o : ninePatches) {
            final JSONObject item = (JSONObject) o;

            final Vector2 imagePosition = extractPosition(item);
            final boolean[] tags = extractTags(item);
            final Enums.Material type = extractType(item);
            float width = ((Number) item.get(Constants.LEVEL_WIDTH_KEY)).floatValue();
            float height = ((Number) item.get(Constants.LEVEL_HEIGHT_KEY)).floatValue();

            if (item.get(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.BLOCK_SPRITE)) {
                final Barrier block;
                block = new Barrier(imagePosition.x, imagePosition.y, width, height, type, !tags[Constants.LEDGE_TAG_INDEX]);
                blockArray.add(block);
                Gdx.app.log(TAG, "Loaded the block at " + imagePosition.add(new Vector2(width / 2, height / 2)));
            } else if (item.get(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.BOX_SPRITE)) {
                final Box box = new Box(imagePosition.x, imagePosition.y, width, height, type, !tags[Constants.LEDGE_TAG_INDEX]);
                boxArray.add(box);
                Gdx.app.log(TAG, "Loaded the box at " + imagePosition.add(new Vector2(width / 2, height / 2)));
            } else if (item.get(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.LADDER_SPRITE)) {
                final Ladder ladder = new Ladder(imagePosition.x, imagePosition.y + height, width, height);
                ladderArray.add(ladder);
                Gdx.app.log(TAG, "Loaded the ladder at " + imagePosition.add(new Vector2(width / 2, height / 2)));
            }

            blockArray.sort(new Comparator<Barrier>() {
                @Override
                public int compare(Barrier o1, Barrier o2) {
                    if (o1.getTop() > o2.getTop()) {
                        return 1;
                    } else if (o1.getTop() < o2.getTop()) {
                        return -1;
                    }
                    return 0;
                }
            });

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
        }

        for (Barrier block : blockArray) {
            level.addGround(block);
        }

        for (Box box : boxArray) {
            level.addGround(box);
        }

        for (Ladder ladder : ladderArray) {
            level.addGround(ladder);
        }
    }
}