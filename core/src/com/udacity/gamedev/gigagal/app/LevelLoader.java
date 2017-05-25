package com.udacity.gamedev.gigagal.app;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.udacity.gamedev.gigagal.entity.Boss;
import com.udacity.gamedev.gigagal.entity.BreakableBox;
import com.udacity.gamedev.gigagal.entity.Cannon;
import com.udacity.gamedev.gigagal.entity.Chamber;
import com.udacity.gamedev.gigagal.entity.Coals;
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
import com.udacity.gamedev.gigagal.entity.Button;
import com.udacity.gamedev.gigagal.entity.Swoopa;
import com.udacity.gamedev.gigagal.entity.Treadmill;
import com.udacity.gamedev.gigagal.entity.Vines;
import com.udacity.gamedev.gigagal.entity.Portal;
import com.udacity.gamedev.gigagal.entity.GigaGal;
import com.udacity.gamedev.gigagal.entity.Box;
import com.udacity.gamedev.gigagal.entity.Zoomba;
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

        LevelUpdater.getInstance().setLevel(level);

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
                        bounds.set(Float.parseFloat(paramSplit[0]), Float.parseFloat(paramSplit[1]), Float.parseFloat(paramSplit[2]), Float.parseFloat(paramSplit[3]));
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
        boolean[] tagBooleans = {false};
        try {
            if (object.containsKey(Constants.LEVEL_TAGS_KEY)) {
                JSONArray tags = (JSONArray) object.get(Constants.LEVEL_TAGS_KEY);
                for (Object tag : tags) {
                    String item = (String) tag;
                    if (item.equals(Constants.LEVEL_LEDGE_TAG)) {
                        tagBooleans[Constants.LEVEL_CLIMBABLE_TAG_INDEX] = true;
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
            final Vector2 destination = extractDestination(item);
            final Enums.Orientation orientation = extractOrientation(item);
            final Enums.Material type = extractType(item);
            final Enums.ShotIntensity intensity = extractIntensity(item);
            final Enums.Upgrade upgrade = extractUpgrade(item);
            final Rectangle bounds = extractBounds(item);
            final float range = extractRange(item);

            if (item.get(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.AMMO_POWERUP_SPRITE)) {
                final Vector2 powerupPosition = imagePosition.add(Constants.AMMO_POWERUP_CENTER);
                Gdx.app.log(TAG, "Loaded an AmmoPowerup at " + powerupPosition);
                level.getPowerups().add(new Powerup(powerupPosition, Enums.PowerupType.AMMO));
            } else if (item.get(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.HEALTH_POWERUP_SPRITE)) {
                final Vector2 powerupPosition = imagePosition.add(Constants.HEALTH_POWERUP_CENTER);
                Gdx.app.log(TAG, "Loaded a HealthPowerup at " + powerupPosition);
                level.getPowerups().add(new Powerup(powerupPosition, Enums.PowerupType.HEALTH));
            } else if (item.get(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.TURBO_POWERUP_SPRITE)) {
                final Vector2 powerupPosition = imagePosition.add(Constants.TURBO_POWERUP_CENTER);
                Gdx.app.log(TAG, "Loaded a TurboPowerup at " + powerupPosition);
                level.getPowerups().add(new Powerup(powerupPosition, Enums.PowerupType.TURBO));
            } else if (item.get(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.LIFE_POWERUP_SPRITE)) {
                final Vector2 powerupPosition = imagePosition.add(Constants.LIFE_POWERUP_CENTER);
                Gdx.app.log(TAG, "Loaded a LifePowerup at " + powerupPosition);
                level.getPowerups().add(new Powerup(powerupPosition, Enums.PowerupType.LIFE));
            } else if (item.get(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.CANNON_POWERUP_SPRITE)) {
                final Vector2 powerupPosition = imagePosition.add(Constants.CANNON_POWERUP_CENTER);
                Gdx.app.log(TAG, "Loaded a CannonPowerup at " + powerupPosition);
                level.getPowerups().add(new Powerup(powerupPosition, Enums.PowerupType.CANNON));
            } else if (item.get(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.STAND_RIGHT)) {
                final Vector2 gigaGalPosition = imagePosition.add(Constants.GIGAGAL_EYE_POSITION);
                Gdx.app.log(TAG, "Loaded GigaGal at " + gigaGalPosition);
                GigaGal.getInstance().setSpawnPosition(gigaGalPosition);
                GigaGal.getInstance().setLevel(level);
            } else if (item.get(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.BOSS_SPRITE)) {
                final Vector2 bossPosition = imagePosition.add(Constants.GIGAGAL_EYE_POSITION);
                Gdx.app.log(TAG, "Loaded Boss at " + bossPosition);
                level.getHazards().add(new Boss.Builder(level, bossPosition).weapon(level.getType()).build());
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
                level.getHazards().add(new Protrusion(spikePosition, Enums.Material.ORE));
            } else if (item.get(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.PROTRUSION_PLASMA_SPRITE_1)) {
                final Vector2 spikePosition = imagePosition.add(Constants.PROTRUSION_SOLID_CENTER);
                Gdx.app.log(TAG, "Loaded the protrusion at " + spikePosition);
                level.getHazards().add(new Protrusion(spikePosition, Enums.Material.PLASMA));
            } else if (item.get(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.PROTRUSION_GAS_SPRITE_1)) {
                final Vector2 spikePosition = imagePosition.add(Constants.PROTRUSION_GAS_CENTER);
                Gdx.app.log(TAG, "Loaded the protrusion at " + spikePosition);
                level.getHazards().add(new Protrusion(spikePosition, Enums.Material.GAS));
            } else if (item.get(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.PROTRUSION_LIQUID_SPRITE_1)) {
                final Vector2 spikePosition = imagePosition.add(Constants.PROTRUSION_LIQUID_CENTER);
                Gdx.app.log(TAG, "Loaded the protrusion at " + spikePosition);
                level.getHazards().add(new Protrusion(spikePosition, Enums.Material.LIQUID));
            } else if (item.get(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.PROTRUSION_SOLID_SPRITE_1)) {
                final Vector2 spikePosition = imagePosition.add(Constants.PROTRUSION_SOLID_CENTER);
                Gdx.app.log(TAG, "Loaded the protrusion at " + spikePosition);
                level.getHazards().add(new Protrusion(spikePosition, Enums.Material.SOLID));
            } else if (item.get(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.SUSPENSION_ORE_SPRITE_1)) {
                final Vector2 spikePosition = imagePosition.add(Constants.SUSPENSION_ORE_CENTER);
                Gdx.app.log(TAG, "Loaded the suspension at " + spikePosition);
                level.getHazards().add(new Suspension(spikePosition, Enums.Material.ORE));
            } else if (item.get(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.SUSPENSION_PLASMA_SPRITE_1)) {
                final Vector2 spikePosition = imagePosition.add(Constants.SUSPENSION_SOLID_CENTER);
                Gdx.app.log(TAG, "Loaded the suspension at " + spikePosition);
                level.getHazards().add(new Suspension(spikePosition, Enums.Material.PLASMA));
            } else if (item.get(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.SUSPENSION_GAS_SPRITE_1)) {
                final Vector2 spikePosition = imagePosition.add(Constants.SUSPENSION_GAS_CENTER);
                Gdx.app.log(TAG, "Loaded the suspension at " + spikePosition);
                level.getHazards().add(new Suspension(spikePosition, Enums.Material.GAS));
            } else if (item.get(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.SUSPENSION_LIQUID_SPRITE_1)) {
                final Vector2 spikePosition = imagePosition.add(Constants.SUSPENSION_LIQUID_CENTER);
                Gdx.app.log(TAG, "Loaded the suspension at " + spikePosition);
                level.getHazards().add(new Suspension(spikePosition, Enums.Material.LIQUID));
            } else if (item.get(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.SUSPENSION_SOLID_SPRITE_1)) {
                final Vector2 spikePosition = imagePosition.add(Constants.SUSPENSION_SOLID_CENTER);
                Gdx.app.log(TAG, "Loaded the suspension at " + spikePosition);
                level.getHazards().add(new Suspension(spikePosition, Enums.Material.SOLID));
            } else if (item.get(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.ZOOMBA_SPRITE)) {
                final Vector2 zoombaPosition = imagePosition.add(Constants.ZOOMBA_CENTER);
                Gdx.app.log(TAG, "Loaded the zoomba at " + zoombaPosition);
                level.getHazards().add(new Zoomba(zoombaPosition, type, range));
            } else if (item.get(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.SWOOPA_SPRITE)) {
                final Vector2 swoopaPosition = imagePosition.add(Constants.SWOOPA_CENTER);
                Gdx.app.log(TAG, "Loaded the swoopa at " + swoopaPosition);
                level.getHazards().add(new Swoopa(level, swoopaPosition, type));
            } else if (item.get(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.ORBEN_SPRITE)) {
                final Vector2 orbenPosition = imagePosition.add(Constants.ORBEN_CENTER);
                Gdx.app.log(TAG, "Loaded the orben at " + orbenPosition);
                level.getHazards().add(new Orben(level, orbenPosition, type));
            } else if (item.get(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.ROLLEN_ORE_SPRITE_1)) {
                final Vector2 rollenPosition = imagePosition.add(Constants.ROLLEN_CENTER);
                Gdx.app.log(TAG, "Loaded the rollen at " + rollenPosition);
                level.getHazards().add(new Rollen(level, rollenPosition, type));
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
            } else if (item.get(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.KNOB_SPRITE_1)) {
                final Vector2 knobPosition = imagePosition.add(Constants.KNOB_CENTER);
                Gdx.app.log(TAG, "Loaded the knob at " + knobPosition);
                level.getGrounds().add(new Knob(knobPosition));
            } else if (item.get(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.CHAMBER_SPRITE)) {
                final Vector2 chamberPosition = imagePosition.add(Constants.CHAMBER_CENTER);
                Gdx.app.log(TAG, "Loaded the chamber at " + chamberPosition);
                Chamber chamber = new Chamber(chamberPosition);
                chamber.setUpgrade(upgrade);
                level.getGrounds().add(chamber);
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
                Vector2 adjustedCenter = new Vector2(Constants.VINES_CENTER.x * scale.x, Constants.VINES_CENTER.y * scale.y);
                final Vector2 vinesPosition = imagePosition.add(Constants.VINES_CENTER);
                Gdx.app.log(TAG, "Loaded the vines at " + vinesPosition);
                level.getGrounds().add(new Vines(vinesPosition, scale, adjustedCenter));
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
            } else if (item.get(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.TRIP_SPRITE_1)) {
                final Vector2 tripPosition = imagePosition.add(Constants.TRIP_CENTER);
                Gdx.app.log(TAG, "Loaded the trip at " + tripPosition);
                Button button = new Button(level, tripPosition, bounds);
                level.getGrounds().add(button);
            } else if (item.get(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.POD_SPRITE_1)) {
                final Vector2 podPosition = imagePosition.add(Constants.POD_CENTER);
                Gdx.app.log(TAG, "Loaded the pod at " + podPosition);
                Pod pod = new Pod(podPosition);
                level.getGrounds().add(pod);
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
            } else if (item.get(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.LAVA_SPRITE_1)) {
                Vector2 adjustedCenter = new Vector2(Constants.LAVA_CENTER.x * scale.x, Constants.LAVA_CENTER.y * scale.y);
                final Vector2 lavaPosition = imagePosition.add(Constants.LAVA_CENTER);
                final Lava lava = new Lava(lavaPosition, scale, adjustedCenter);
                level.getHazards().add(lava);
                Gdx.app.log(TAG, "Loaded the lava at " + lavaPosition);
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

    private static final void loadNinePatches(LevelUpdater level, JSONArray ninePatches) {

        Array<Box> boxArray = new Array<Box>();
        Array<BreakableBox> breakableBoxArray = new Array<BreakableBox>();
        Array<Ladder> ladderArray = new Array<Ladder>();

        for (Object o : ninePatches) {
            final JSONObject item = (JSONObject) o;

            final Vector2 imagePosition = extractPosition(item);
            final boolean[] tags = extractTags(item);
            final Enums.Material type = extractType(item);
            float width = ((Number) item.get(Constants.LEVEL_WIDTH_KEY)).floatValue();
            float height = ((Number) item.get(Constants.LEVEL_HEIGHT_KEY)).floatValue();

            if (item.get(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.BOX_SPRITE)) {
                final Box box;
                box = new Box(imagePosition.x, imagePosition.y, width, height, type);
                box.setDensity(!tags[Constants.LEVEL_CLIMBABLE_TAG_INDEX]);
                boxArray.add(box);
                Gdx.app.log(TAG, "Loaded the box at " + imagePosition.add(new Vector2(width / 2, height / 2)));
            } else if (item.get(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.BREAKABLE_BOX_SPRITE)) {
                final BreakableBox box = new BreakableBox(imagePosition.x, imagePosition.y, width, height, type);
                breakableBoxArray.add(box);
                Gdx.app.log(TAG, "Loaded the breakableBox at " + imagePosition.add(new Vector2(width / 2, height / 2)));
            } else if (item.get(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.LADDER_SPRITE)) {
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

            breakableBoxArray.sort(new Comparator<BreakableBox>() {
                @Override
                public int compare(BreakableBox o1, BreakableBox o2) {
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

        level.getGrounds().addAll(boxArray);
        level.getGrounds().addAll(breakableBoxArray);
        level.getGrounds().addAll(ladderArray);
    }
}