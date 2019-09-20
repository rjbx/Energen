package com.github.rjbx.energen.app;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.github.rjbx.energen.entity.Armoroll;
import com.github.rjbx.energen.entity.Avatar;
import com.github.rjbx.energen.entity.Bladeroll;
import com.github.rjbx.energen.entity.Boss;
import com.github.rjbx.energen.entity.Box;
import com.github.rjbx.energen.entity.Brick;
import com.github.rjbx.energen.entity.Cannoroll;
import com.github.rjbx.energen.entity.Cannon;
import com.github.rjbx.energen.entity.Chamber;
import com.github.rjbx.energen.entity.Coals;
import com.github.rjbx.energen.entity.Deposit;
import com.github.rjbx.energen.entity.Gate;
import com.github.rjbx.energen.entity.Ice;
import com.github.rjbx.energen.entity.Knob;
import com.github.rjbx.energen.entity.Ladder;
import com.github.rjbx.energen.entity.Lava;
import com.github.rjbx.energen.entity.Lift;
import com.github.rjbx.energen.entity.Orben;
import com.github.rjbx.energen.entity.Pillar;
import com.github.rjbx.energen.entity.Pod;
import com.github.rjbx.energen.entity.Pole;
import com.github.rjbx.energen.entity.Powerup;
import com.github.rjbx.energen.entity.Protrusion;
import com.github.rjbx.energen.entity.Rollen;
import com.github.rjbx.energen.entity.Rope;
import com.github.rjbx.energen.entity.Sand;
import com.github.rjbx.energen.entity.Slick;
import com.github.rjbx.energen.entity.Spring;
import com.github.rjbx.energen.entity.Suspension;
import com.github.rjbx.energen.entity.Teleport;
import com.github.rjbx.energen.entity.Tripchamber;
import com.github.rjbx.energen.entity.Tripknob;
import com.github.rjbx.energen.entity.Swoopa;
import com.github.rjbx.energen.entity.Treadmill;
import com.github.rjbx.energen.entity.Tripspring;
import com.github.rjbx.energen.entity.Triptread;
import com.github.rjbx.energen.entity.Vines;
import com.github.rjbx.energen.entity.Portal;
import com.github.rjbx.energen.entity.Barrier;
import com.github.rjbx.energen.entity.Waves;
import com.github.rjbx.energen.entity.Zoomba;
import com.github.rjbx.energen.util.ChaseCam;
import com.github.rjbx.energen.util.Constants;
import com.github.rjbx.energen.util.Enums;

// immutable non-instantiable static
final class LevelLoader {

    public static final String TAG = LevelLoader.class.toString();
    private static boolean runtimeEx;

    // cannot be subclassed
    private LevelLoader() {}

    protected static final void load(Enums.Theme level) {

        runtimeEx = false;

        LevelUpdater.getInstance().setTheme(level);

        final FileHandle file = Gdx.files.internal("levels/scenes/" + level + ".dt");

        JsonReader reader = new JsonReader();
        JsonValue rootJsonValue = reader.parse(file.reader());
        
        JsonValue compositeJsonValue = rootJsonValue.get(Constants.LEVEL_COMPOSITE);
        
        JsonValue.JsonIterator ninePatches = compositeJsonValue.get(Constants.LEVEL_9PATCHES).iterator();
        loadNinePatches(LevelUpdater.getInstance(), ninePatches);

        JsonValue.JsonIterator images = compositeJsonValue.get(Constants.LEVEL_IMAGES).iterator();
        loadImages(LevelUpdater.getInstance(), images);

        LevelUpdater.getInstance().setLoadEx(runtimeEx);
    }

    private static final int extractId(JsonValue object) {
        int id = 0;

        try {
            id = object.getInt(Constants.LEVEL_UNIQUE_ID_KEY);
        } catch (NumberFormatException ex) {
            runtimeEx = true;
            Gdx.app.log(TAG, Constants.LEVEL_KEY_MESSAGE
                    + "; object: " + object.get(Constants.LEVEL_IMAGENAME_KEY)
                    + "; id: " + id
                    + "; key: " + Constants.LEVEL_X_POSITION_KEY + Constants.LEVEL_Y_POSITION_KEY);
        } catch (IllegalArgumentException ex) {
            Gdx.app.log(TAG, Constants.LEVEL_KEY_MESSAGE
                    + ": object: " + object.asString()
                    + ": id " + id
                    + ": key " + Constants.LEVEL_X_SCALE_KEY + Constants.LEVEL_Y_SCALE_KEY);
        }

        return id;
    }

    private static final Vector2 extractPosition(JsonValue object) {
        Vector2 position = new Vector2(0, 0);

        try {
            Float x = object.getFloat(Constants.LEVEL_X_POSITION_KEY);
            Float y = object.getFloat(Constants.LEVEL_Y_POSITION_KEY);

            position.set(x, y);
        } catch (NumberFormatException ex) {
            runtimeEx = true;
            Gdx.app.log(TAG, Constants.LEVEL_KEY_MESSAGE
                    + "; object: " + object.get(Constants.LEVEL_IMAGENAME_KEY)
                    + "; id: " + object.get(Constants.LEVEL_UNIQUE_ID_KEY)
                    + "; key: " + Constants.LEVEL_X_POSITION_KEY + Constants.LEVEL_Y_POSITION_KEY);
        } catch (IllegalArgumentException ex) {
            Gdx.app.log(TAG, Constants.LEVEL_KEY_MESSAGE
                    + ": object: " + object.asString()
                    + ": id " + object.get(Constants.LEVEL_UNIQUE_ID_KEY)
                    + ": key " + Constants.LEVEL_X_SCALE_KEY + Constants.LEVEL_Y_SCALE_KEY);
        }

        return position;
    }

    private static final Vector2 extractScale(JsonValue object) {
        Vector2 scale = new Vector2(1, 1);
        try {
            if (object.has(Constants.LEVEL_X_SCALE_KEY)) {
                scale.x = object.getFloat(Constants.LEVEL_X_SCALE_KEY);
            }
            if (object.has(Constants.LEVEL_Y_SCALE_KEY)) {
                scale.y = object.getFloat(Constants.LEVEL_Y_SCALE_KEY);
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

    private static final float extractRotation(JsonValue object) {
        float rotation = 0;
        try {
            if (object.has(Constants.LEVEL_ROTATION_KEY)) {
                rotation = object.getFloat(Constants.LEVEL_ROTATION_KEY);
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

    private static final Enums.Orientation extractOrientation(JsonValue object) {
        Enums.Orientation orientation = Enums.Orientation.X;
        try {
            if (object.has(Constants.LEVEL_IDENTIFIER_KEY)) {
                String identifierVar = object.getString(Constants.LEVEL_IDENTIFIER_KEY);
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

    private static final Enums.Direction extractDirection(JsonValue object) {
        Enums.Direction direction = Enums.Direction.RIGHT;
        try {
            if (object.has(Constants.LEVEL_CUSTOM_VARS_KEY)) {
                String[] customVars = object.getString(Constants.LEVEL_CUSTOM_VARS_KEY).split(";");
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
                    + "; key: " + Constants.LEVEL_DIRECTION_KEY);
        }
        return direction;
    }

    private static final Enums.Energy extractType(JsonValue object) {
        Enums.Energy type = Enums.Energy.NATIVE;
        try {
            if (object.has(Constants.LEVEL_CUSTOM_VARS_KEY)) {
                String[] customVars = (object.getString(Constants.LEVEL_CUSTOM_VARS_KEY)).split(";");
                for (String customVar : customVars) {
                    if (customVar.contains(Constants.LEVEL_TYPE_KEY)) {
                        String[] typeSplit = customVar.split(Constants.LEVEL_TYPE_KEY + ":");
                        type = Enums.Energy.valueOf(typeSplit[1]);
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

    private static final Enums.ShotIntensity extractIntensity(JsonValue object) {
        Enums.ShotIntensity intensity = Enums.ShotIntensity.NORMAL;
        try {
            if (object.has(Constants.LEVEL_CUSTOM_VARS_KEY)) {
                String[] customVars = object.getString(Constants.LEVEL_CUSTOM_VARS_KEY).split(";");
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

    private static final Rectangle extractBounds(JsonValue object) {
        Rectangle bounds = new Rectangle(0, 0, 0, 0);
        try {
            if (object.has(Constants.LEVEL_CUSTOM_VARS_KEY)) {
                String[] customVars = object.getString(Constants.LEVEL_CUSTOM_VARS_KEY).split(";");
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

    private static final float extractRange(JsonValue object) {
        float range = Constants.ZOOMBA_RANGE;
        try {
            if (object.has(Constants.LEVEL_CUSTOM_VARS_KEY)) {
                String[] customVars = object.getString(Constants.LEVEL_CUSTOM_VARS_KEY).split(";");
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

    private static final float extractSpeed(JsonValue object) {
        float speed = Constants.LIFT_SPEED;
        try {
            if (object.has(Constants.LEVEL_CUSTOM_VARS_KEY)) {
                String[] customVars = object.getString(Constants.LEVEL_CUSTOM_VARS_KEY).split(";");
                for (String customVar : customVars) {
                    if (customVar.contains(Constants.LEVEL_SPEED_KEY)) {
                        String[] speedSplit = customVar.split(Constants.LEVEL_SPEED_KEY + ":");
                        speed = Float.parseFloat(speedSplit[1]);
                    }
                }
            }
        } catch (NumberFormatException ex) {
            runtimeEx = true;
            Gdx.app.log(TAG, Constants.LEVEL_KEY_MESSAGE
                    + "; object: " + object.get(Constants.LEVEL_IMAGENAME_KEY)
                    + "; id: " + object.get(Constants.LEVEL_UNIQUE_ID_KEY)
                    + "; key: " + Constants.LEVEL_SPEED_KEY);
        }
        return speed;
    }

    private static final Vector2 extractDestination(JsonValue object) {
        Vector2 destination = new Vector2(0, 0);
        try {
            if (object.has(Constants.LEVEL_CUSTOM_VARS_KEY)) {
                String[] customVars = object.getString(Constants.LEVEL_CUSTOM_VARS_KEY).split(";");
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

    private static final Enums.Upgrade extractUpgrade(JsonValue object) {
        Enums.Upgrade upgrade = Enums.Upgrade.NONE;
        try {
            if (object.has(Constants.LEVEL_CUSTOM_VARS_KEY)) {
                String[] customVars = object.getString(Constants.LEVEL_CUSTOM_VARS_KEY).split(";");
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

    private static final boolean[] extractTags(JsonValue object) {
        boolean[] tagBooleans = {false, false, true, false, false};
        try {
            if (object.has(Constants.LEVEL_TAGS_KEY)) {
                JsonValue.JsonIterator tags = object.get(Constants.LEVEL_TAGS_KEY).iterator();
                for (JsonValue tag : tags) {
                    String item = tag.asString();
                    if (item.equals(Constants.LEDGE_TAG)) {
                        tagBooleans[Constants.LEDGE_TAG_INDEX] = true;
                    }
                    if (item.equals(Constants.ON_TAG)) {
                        tagBooleans[Constants.ON_TAG_INDEX] = true;
                    }
                    if (item.equals(Constants.OFF_TAG)) {
                        tagBooleans[Constants.OFF_TAG_INDEX] = false;
                    }
                    if (item.equals(Constants.CONVERTIBLE_TAG)) {
                        tagBooleans[Constants.CONVERTIBLE_TAG_INDEX] = true;
                    }
                    if (item.equals(Constants.GOAL_TAG)) {
                        tagBooleans[Constants.GOAL_TAG_INDEX] = true;
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

    private static final void loadImages(LevelUpdater level, JsonValue.JsonIterator images) {
        for (JsonValue item : images) {

            final int id = extractId(item);
            final Vector2 imagePosition = extractPosition(item);
            final Vector2 scale = extractScale(item);
            final float rotation = extractRotation(item);
            final Vector2 destination = extractDestination(item);
            final Enums.Orientation orientation = extractOrientation(item);
            final Enums.Direction direction = extractDirection(item);
            final Enums.Energy type = extractType(item);
            final Enums.ShotIntensity intensity = extractIntensity(item);
            final Enums.Upgrade upgrade = extractUpgrade(item);
            final Rectangle bounds = extractBounds(item);
            final float range = extractRange(item);
            final float speed = extractSpeed(item);
            final boolean[] tags = extractTags(item);

            if (item.getString(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.AMMO_POWERUP_SPRITE)) {
                final Vector2 powerupPosition = imagePosition.add(Constants.AMMO_POWERUP_CENTER);
                Gdx.app.log(TAG, "Loaded an AmmoPowerup at " + powerupPosition);
                Powerup e = new Powerup(powerupPosition, Enums.PowerupType.AMMO);
                e.setId(id);
                level.addPowerup(e);
            } else if (item.getString(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.HEALTH_POWERUP_SPRITE)) {
                final Vector2 powerupPosition = imagePosition.add(Constants.HEALTH_POWERUP_CENTER);
                Gdx.app.log(TAG, "Loaded a HealthPowerup at " + powerupPosition);
                Powerup e = new Powerup(powerupPosition, Enums.PowerupType.HEALTH);
                e.setId(id);
                level.addPowerup(e);
            } else if (item.getString(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.TURBO_POWERUP_SPRITE)) {
                final Vector2 powerupPosition = imagePosition.add(Constants.TURBO_POWERUP_CENTER);
                Gdx.app.log(TAG, "Loaded a TurboPowerup at " + powerupPosition);
                Powerup e = new Powerup(powerupPosition, Enums.PowerupType.TURBO);
                e.setId(id);
                level.addPowerup(e);
            } else if (item.getString(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.LIFE_POWERUP_SPRITE)) {
                final Vector2 powerupPosition = imagePosition.add(Constants.LIFE_POWERUP_CENTER);
                Gdx.app.log(TAG, "Loaded a LifePowerup at " + powerupPosition);
                Powerup e = new Powerup(powerupPosition, Enums.PowerupType.LIFE);
                e.setId(id);
                level.addPowerup(e);
            } else if (item.getString(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.SUPER_POWERUP_SPRITE)) {
                final Vector2 powerupPosition = imagePosition.add(Constants.SUPER_POWERUP_CENTER);
                Gdx.app.log(TAG, "Loaded a SuperPowerup at " + powerupPosition);
                Powerup e = new Powerup(powerupPosition, Enums.PowerupType.SUPER);
                e.setId(id);
                level.addPowerup(e);
            } else if (item.getString(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.CANNON_POWERUP_SPRITE)) {
                final Vector2 powerupPosition = imagePosition.add(Constants.CANNON_POWERUP_CENTER);
                Gdx.app.log(TAG, "Loaded a CannonPowerup at " + powerupPosition);
                Powerup e = new Powerup(powerupPosition, Enums.PowerupType.CANNON);
                e.setId(id);
                level.addPowerup(e);
            } else if (item.getString(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.RUBY_SPRITE_1)) {
                final Vector2 powerupPosition = imagePosition.add(Constants.GEM_CENTER);
                Gdx.app.log(TAG, "Loaded a Ruby at " + powerupPosition);
                Powerup e = new Powerup(powerupPosition, Enums.GemType.RUBY);
                e.setId(id);
                level.addPowerup(e);
            } else if (item.getString(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.SAPPHIRE_SPRITE_1)) {
                final Vector2 powerupPosition = imagePosition.add(Constants.GEM_CENTER);
                Gdx.app.log(TAG, "Loaded a Sapphire at " + powerupPosition);
                Powerup e = new Powerup(powerupPosition, Enums.GemType.SAPPHIRE);
                e.setId(id);
                level.addPowerup(e);
            } else if (item.getString(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.EMERALD_SPRITE_1)) {
                final Vector2 powerupPosition = imagePosition.add(Constants.GEM_CENTER);
                Gdx.app.log(TAG, "Loaded a Emerald at " + powerupPosition);
                Powerup e = new Powerup(powerupPosition, Enums.GemType.EMERALD);
                e.setId(id);
                level.addPowerup(e);
            } else if (item.getString(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.BOX_DEPOSIT_SPRITE)) {
                final Vector2 depositPosition = imagePosition.add(Constants.BOX_DEPOSIT_CENTER);
                Gdx.app.log(TAG, "Loaded a Deposit at " + depositPosition);
                Deposit e = new Deposit(depositPosition);
                e.setId(id);
                level.addGround(e);
            } else if (item.getString(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.STAND)) {
                final Vector2 energenPosition = imagePosition.add(Constants.AVATAR_EYE_POSITION);
                Gdx.app.log(TAG, "Loaded avatar at " + energenPosition);
                Avatar avatar = Avatar.getInstance();
                avatar.setSpawnPosition(energenPosition);
                avatar.setId(id);
            } else if (item.getString(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.BEAST_SPRITE)) {
                final Vector2 bossPosition = imagePosition.add(Constants.AVATAR_EYE_POSITION);
                Gdx.app.log(TAG, "Loaded Boss at " + bossPosition);
                Boss boss = new Boss.Builder(bossPosition).energy(type).build();
                boss.setId(id);
                level.addHazard(boss);
                if (tags[Constants.GOAL_TAG_INDEX]) {
                    level.setBoss(boss);
                    ChaseCam.getInstance().setRoomPosition(bossPosition);
                }
                else {
                    boss.setMiniBoss(true);
                    boss.setConvertBounds(bounds);
                }
            } else if (item.getString(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.PORTAL_SPRITE_1)) {
                final Vector2 portalPosition = imagePosition.add(Constants.PORTAL_CENTER);
                Gdx.app.log(TAG, "Loaded the exit portal at " + portalPosition);
                Portal e = new Portal(portalPosition, tags[Constants.GOAL_TAG_INDEX]);
                e.setId(id);
                level.getTransports().add(e);
            } else if (item.getString(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.TELEPORT_SPRITE_1)) {
                final Vector2 teleportPosition = imagePosition.add(Constants.TELEPORT_CENTER);
                Gdx.app.log(TAG, "Loaded the exit teleport at " + teleportPosition);
                Teleport e = new Teleport(teleportPosition, destination, tags[Constants.GOAL_TAG_INDEX]);
                e.setId(id);
                level.getTransports().add(e);
            } else if (item.getString(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.PROTRUSION_ORE_SPRITE_1)) {
                final Vector2 spikePosition = imagePosition.add(Constants.PROTRUSION_ORE_CENTER);
                Gdx.app.log(TAG, "Loaded the protrusion at " + spikePosition);
                Protrusion e = new Protrusion(spikePosition, Enums.Energy.ORE, rotation, !tags[Constants.ON_TAG_INDEX]);
                e.setId(id);
                level.addHazard(e);
            } else if (item.getString(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.PROTRUSION_PLASMA_SPRITE_1)) {
                final Vector2 spikePosition = imagePosition.add(Constants.PROTRUSION_SOLID_CENTER);
                Gdx.app.log(TAG, "Loaded the protrusion at " + spikePosition);
                Protrusion e = new Protrusion(spikePosition, Enums.Energy.PLASMA, rotation, !tags[Constants.ON_TAG_INDEX]);
                e.setId(id);
                level.addHazard(e);
            } else if (item.getString(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.PROTRUSION_GAS_SPRITE_1)) {
                final Vector2 spikePosition = imagePosition.add(Constants.PROTRUSION_GAS_CENTER);
                Gdx.app.log(TAG, "Loaded the protrusion at " + spikePosition);
                Protrusion e = new Protrusion(spikePosition, Enums.Energy.GAS, rotation, !tags[Constants.ON_TAG_INDEX]);
                e.setId(id);
                level.addHazard(e);
            } else if (item.getString(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.PROTRUSION_LIQUID_SPRITE_1)) {
                final Vector2 spikePosition = imagePosition.add(Constants.PROTRUSION_LIQUID_CENTER);
                Gdx.app.log(TAG, "Loaded the protrusion at " + spikePosition);
                Protrusion e = new Protrusion(spikePosition, Enums.Energy.LIQUID, rotation, !tags[Constants.ON_TAG_INDEX]);
                e.setId(id);
                level.addHazard(e);
            } else if (item.getString(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.PROTRUSION_SOLID_SPRITE_1)) {
                final Vector2 spikePosition = imagePosition.add(Constants.PROTRUSION_SOLID_CENTER);
                Gdx.app.log(TAG, "Loaded the protrusion at " + spikePosition);
                Protrusion e = new Protrusion(spikePosition, Enums.Energy.SOLID, rotation, !tags[Constants.ON_TAG_INDEX]);
                e.setId(id);
                level.addHazard(e);
            } else if (item.getString(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.SUSPENSION_ORE_SPRITE_1)) {
                final Vector2 suspensionPosition = imagePosition.add(Constants.SUSPENSION_ORE_CENTER);
                Gdx.app.log(TAG, "Loaded the suspension at " + suspensionPosition);
                Suspension e = new Suspension(suspensionPosition, Enums.Energy.ORE);
                e.setId(id);
                level.addHazard(e);
            } else if (item.getString(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.SUSPENSION_PLASMA_SPRITE_1)) {
                final Vector2 suspensionPosition = imagePosition.add(Constants.SUSPENSION_SOLID_CENTER);
                Gdx.app.log(TAG, "Loaded the suspension at " + suspensionPosition);
                Suspension e = new Suspension(suspensionPosition, Enums.Energy.PLASMA);
                e.setId(id);
                level.addHazard(e);
            } else if (item.getString(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.SUSPENSION_GAS_SPRITE_1)) {
                final Vector2 suspensionPosition = imagePosition.add(Constants.SUSPENSION_GAS_CENTER);
                Gdx.app.log(TAG, "Loaded the suspension at " + suspensionPosition);
                Suspension e = new Suspension(suspensionPosition, Enums.Energy.GAS);
                e.setId(id);
                level.addHazard(e);
            } else if (item.getString(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.SUSPENSION_LIQUID_SPRITE_1)) {
                final Vector2 suspensionPosition = imagePosition.add(Constants.SUSPENSION_LIQUID_CENTER);
                Gdx.app.log(TAG, "Loaded the suspension at " + suspensionPosition);
                Suspension e = new Suspension(suspensionPosition, Enums.Energy.LIQUID);
                e.setId(id);
                level.addHazard(e);
            } else if (item.getString(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.SUSPENSION_SOLID_SPRITE_1)) {
                final Vector2 suspensionPosition = imagePosition.add(Constants.SUSPENSION_SOLID_CENTER);
                Gdx.app.log(TAG, "Loaded the suspension at " + suspensionPosition);
                Suspension e = new Suspension(suspensionPosition, Enums.Energy.SOLID);
                e.setId(id);
                level.addHazard(e);
            } else if (item.getString(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.SUSPENSION_ANTIMATTER_SPRITE_1)) {
                final Vector2 suspensionPosition = imagePosition.add(Constants.SUSPENSION_ANTIMATTER_CENTER);
                Gdx.app.log(TAG, "Loaded the suspension at " + suspensionPosition);
                Suspension e = new Suspension(suspensionPosition, Enums.Energy.ANTIMATTER);
                e.setId(id);
                level.addHazard(e);
            } else if (item.getString(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.X_CANIROL_SPRITE_1)) {
                final Vector2 canirolPosition = imagePosition.add(Constants.X_CANIROL_CENTER);
                Gdx.app.log(TAG, "Loaded the zoomba at " + canirolPosition);
                Cannoroll e = new Cannoroll(canirolPosition, orientation, intensity, range, tags[Constants.OFF_TAG_INDEX]);
                e.setId(id);
                level.addGround(e);
            } else if (item.getString(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.ZOOMBA_SPRITE)) {
                final Vector2 zoombaPosition = imagePosition.add(Constants.ZOOMBA_CENTER);
                Gdx.app.log(TAG, "Loaded the zoomba at " + zoombaPosition);
                Zoomba e = new Zoomba(zoombaPosition, orientation, type, range);
                e.setId(id);
                level.addHazard(e);
            } else if (item.getString(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.SWOOPA_SPRITE_LEFT)) {
                final Vector2 swoopaPosition = imagePosition.add(Constants.SWOOPA_CENTER);
                Gdx.app.log(TAG, "Loaded the swoopa at " + swoopaPosition);
                Swoopa e = new Swoopa(swoopaPosition, Enums.Direction.LEFT, type);
                e.setId(id);
                level.addHazard(e);
            } else if (item.getString(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.SWOOPA_SPRITE_RIGHT)) {
                final Vector2 swoopaPosition = imagePosition.add(Constants.SWOOPA_CENTER);
                Gdx.app.log(TAG, "Loaded the swoopa at " + swoopaPosition);
                Swoopa e = new Swoopa(swoopaPosition, Enums.Direction.RIGHT, type);
                e.setId(id);
                level.addHazard(e);
            } else if (item.getString(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.ORBEN_SPRITE)) {
                final Vector2 orbenPosition = imagePosition.add(Constants.ORBEN_CENTER);
                Gdx.app.log(TAG, "Loaded the orben at " + orbenPosition);
                Orben e = new Orben(orbenPosition, type);
                e.setId(id);
                level.addHazard(e);
            } else if (item.getString(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.ROLLEN_ORE_SPRITE_1)) {
                final Vector2 rollenPosition = imagePosition.add(Constants.ROLLEN_CENTER);
                Gdx.app.log(TAG, "Loaded the rollen at " + rollenPosition);
                Rollen e = new Rollen(rollenPosition, type);
                e.setId(id);
                level.addHazard(e);
            } else if (item.getString(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.ARMOROLL_LIQUID_SPRITE_1)) {
                final Vector2 armorollPosition = imagePosition.add(Constants.ROLLEN_CENTER);
                Gdx.app.log(TAG, "Loaded the armorollo at " + armorollPosition);
                Armoroll e = new Armoroll(armorollPosition, bounds, type);
                e.setId(id);
                level.addHazard(e);
            } else if (item.getString(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.ARMOROLL_LIQUID_SPRITE_0)) {
                final Vector2 bladerollPosition = imagePosition.add(Constants.ROLLEN_CENTER);
                Gdx.app.log(TAG, "Loaded the bladeroll at " + bladerollPosition);
                Bladeroll e = new Bladeroll(bladerollPosition, bounds, type, speed);
                e.setId(id);
                level.addHazard(e);
            } else if (item.getString(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.X_CANNON_SPRITE)) {
                final Vector2 cannonPosition = imagePosition.add(Constants.X_CANNON_CENTER);
                Gdx.app.log(TAG, "Loaded the cannon at " + cannonPosition);
                Cannon e = new Cannon(cannonPosition, Enums.Orientation.X, intensity, tags[Constants.OFF_TAG_INDEX]);
                e.setId(id);
                level.addGround(e);
            } else if (item.getString(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.Y_CANNON_SPRITE)) {
                final Vector2 cannonPosition = imagePosition.add(Constants.Y_CANNON_CENTER);
                Gdx.app.log(TAG, "Loaded the cannon at " + cannonPosition);
                Cannon e = new Cannon(cannonPosition, Enums.Orientation.Y, intensity, tags[Constants.OFF_TAG_INDEX]);
                e.setId(id);
                level.addGround(e);
            } else if (item.getString(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.PILLAR_SPRITE)) {
                final Vector2 pillarPosition = imagePosition.add(Constants.PILLAR_CENTER);
                Gdx.app.log(TAG, "Loaded the pillar at " + pillarPosition);
                Pillar e = new Pillar(pillarPosition);
                e.setId(id);
                level.addGround(e);
            } else if (item.getString(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.KNOB_SPRITE_1)) {
                final Vector2 knobPosition = imagePosition.add(Constants.KNOB_CENTER);
                Gdx.app.log(TAG, "Loaded the knob at " + knobPosition);
                Knob e = new Knob(knobPosition);
                e.setId(id);
                level.addGround(e);
            } else if (item.getString(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.CHAMBER_SPRITE)) {
                final Vector2 chamberPosition = imagePosition.add(Constants.CHAMBER_CENTER);
                Gdx.app.log(TAG, "Loaded the chamber at " + chamberPosition);
                Chamber chamber = new Chamber(chamberPosition);
                chamber.setUpgrade(upgrade);
                chamber.setId(id);
                level.addGround(chamber);
            } else if (item.getString(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.LIFT_SPRITE)) {
                final Vector2 liftPosition = imagePosition.add(Constants.LIFT_CENTER);
                Gdx.app.log(TAG, "Loaded the lift at " + liftPosition);
                Lift lift = new Lift(liftPosition, orientation, range, speed);
                lift.setId(id);
                level.addGround(lift);
            } else if (item.getString(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.ROPE_SPRITE)) {
                final Vector2 ropePosition = imagePosition.add(Constants.ROPE_CENTER);
                Gdx.app.log(TAG, "Loaded the rope at " + ropePosition);
                Rope e = new Rope(ropePosition);
                e.setId(id);
                level.addGround(e);
            } else if (item.getString(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.VINES_SPRITE)) {
                Vector2 adjustedCenter = new Vector2(Constants.VINES_CENTER.x * scale.x, Constants.VINES_CENTER.y * scale.y);
                final Vector2 vinesPosition = imagePosition.add(Constants.VINES_CENTER);
                Gdx.app.log(TAG, "Loaded the vines at " + vinesPosition);
                Vines e = new Vines(vinesPosition, scale, adjustedCenter);
                e.setId(id);
                level.addGround(e);
            } else if (item.getString(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.POLE_SPRITE_1)) {
                final Vector2 polePosition = imagePosition.add(Constants.POLE_CENTER);
                Gdx.app.log(TAG, "Loaded the pole at " + polePosition);
                Pole e = new Pole(polePosition);
                e.setId(id);
                level.addGround(e);
            } else if (item.getString(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.SINK_SPRITE_1)) {
                final Vector2 sinkPosition = imagePosition.add(Constants.SINK_CENTER);
                Gdx.app.log(TAG, "Loaded the sink at " + sinkPosition);
                Sand e = new Sand(sinkPosition);
                e.setId(id);
                level.addGround(e);
            } else if (item.getString(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.SPRING_SPRITE_1)) {
                final Vector2 springPosition = imagePosition.add(Constants.SPRING_CENTER);
                Gdx.app.log(TAG, "Loaded the spring at " + springPosition);
                Spring e = new Spring(springPosition);
                e.setId(id);
                level.addGround(e);
            } else if (item.getString(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.LEVER_SPRITE_1)) {
                final Vector2 leverPosition = imagePosition.add(Constants.LEVER_CENTER);
                Gdx.app.log(TAG, "Loaded the tripspring at " + leverPosition);
                Tripspring e = new Tripspring(leverPosition, bounds, tags[Constants.ON_TAG_INDEX]);
                e.setId(id);
                level.addGround(e);
            } else if (item.getString(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.TRIPKNOB_SPRITE_1)) {
                final Vector2 tripPosition = imagePosition.add(Constants.TRIPKNOB_CENTER);
                Gdx.app.log(TAG, "Loaded the tripknob at " + tripPosition);
                Tripknob trip = new Tripknob(tripPosition, bounds, rotation, tags[Constants.ON_TAG_INDEX]);
                trip.setId(id);
                level.addGround(trip);
            } else if (item.getString(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.TRIPTREAD_SPRITE_1_LEFT_OFF)) {
                final Vector2 tripPosition = imagePosition.add(Constants.TRIPTREAD_CENTER);
                Gdx.app.log(TAG, "Loaded the triptread at " + tripPosition);
                Triptread trip = new Triptread(tripPosition, bounds, tags[Constants.ON_TAG_INDEX], Enums.Direction.LEFT);
                trip.setId(id);
                level.addGround(trip);
            } else if (item.getString(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.TRIPCHAMBER_SPRITE_1_OFF)) {
                final Vector2 tripPosition = imagePosition.add(Constants.TRIPCHAMBER_CENTER);
                Gdx.app.log(TAG, "Loaded the tripchamber at " + tripPosition);
                Tripchamber trip = new Tripchamber(tripPosition, bounds, tags[Constants.ON_TAG_INDEX]);
                trip.setId(id);
                level.addGround(trip);
            } else if (item.getString(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.POD_SPRITE_1)) {
                final Vector2 podPosition = imagePosition.add(Constants.POD_CENTER);
                Gdx.app.log(TAG, "Loaded the pod at " + podPosition);
                Pod pod = new Pod(podPosition);
                pod.setId(id);
                level.addGround(pod);
            } else if (item.getString(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.SLICK_SPRITE_1)) {
                Vector2 adjustedCenter = new Vector2(Constants.SLICK_CENTER.x * scale.x, Constants.SLICK_CENTER.y * scale.y);
                final Vector2 slickPosition = imagePosition.add(Constants.SLICK_CENTER);
                final Slick slick = new Slick(slickPosition, scale, adjustedCenter);
                slick.setId(id);
                level.addGround(slick);
                Gdx.app.log(TAG, "Loaded the slick at " + slickPosition);
            } else if (item.getString(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.ICE_SPRITE_1)) {
                Vector2 adjustedCenter = new Vector2(Constants.ICE_CENTER.x * scale.x, Constants.ICE_CENTER.y * scale.y);
                final Vector2 icePosition = imagePosition.add(Constants.ICE_CENTER);
                final Ice ice = new Ice(icePosition, scale, adjustedCenter);
                ice.setId(id);
                level.addGround(ice);
                Gdx.app.log(TAG, "Loaded the ice at " + icePosition);
            } else if (item.getString(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.COALS_SPRITE_1)) {
                Vector2 adjustedCenter = new Vector2(Constants.COALS_CENTER.x * scale.x, Constants.COALS_CENTER.y * scale.y);
                final Vector2 coalsPosition = imagePosition.add(Constants.COALS_CENTER);
                final Coals coals = new Coals(coalsPosition, scale, adjustedCenter);
                coals.setId(id);
                level.addGround(coals);
                Gdx.app.log(TAG, "Loaded the coals at " + coalsPosition);
            } else if (item.getString(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.LAVA_SPRITE_1)) {
                Vector2 adjustedCenter = new Vector2(Constants.LAVA_CENTER.x * scale.x, Constants.LAVA_CENTER.y * scale.y);
                final Vector2 lavaPosition = imagePosition.add(Constants.LAVA_CENTER);
                final Lava lava = new Lava(lavaPosition, scale, adjustedCenter);
                lava.setId(id);
                level.addGround(lava);
                Gdx.app.log(TAG, "Loaded the lava at " + lavaPosition);
            } else if (item.getString(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.WAVES_SPRITE_1)) {
                Vector2 adjustedCenter = new Vector2(Constants.WAVES_CENTER.x * scale.x, Constants.WAVES_CENTER.y * scale.y);
                final Vector2 wavesPosition = imagePosition.add(Constants.WAVES_CENTER);
                final Waves waves = new Waves(wavesPosition, scale, adjustedCenter);
                waves.setId(id);
                level.addGround(waves);
                Gdx.app.log(TAG, "Loaded the waves at " + wavesPosition);
            } else if (item.getString(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.TREADMILL_SPRITE_1_LEFT)) {
                Vector2 adjustedCenter = new Vector2(Constants.TREADMILL_CENTER.x * scale.x, Constants.TREADMILL_CENTER.y * scale.y);
                final Vector2 treadmillPosition = imagePosition.add(Constants.TREADMILL_CENTER);
                final Treadmill treadmill = new Treadmill(treadmillPosition, scale, adjustedCenter, Enums.Direction.LEFT);
                treadmill.setId(id);
                level.addGround(treadmill);
                Gdx.app.log(TAG, "Loaded the treadmill at " + treadmillPosition);
            } else if (item.getString(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.TREADMILL_SPRITE_1_RIGHT)) {
                Vector2 adjustedCenter = new Vector2(Constants.TREADMILL_CENTER.x * scale.x, Constants.TREADMILL_CENTER.y * scale.y);
                final Vector2 treadmillPosition = imagePosition.add(Constants.TREADMILL_CENTER);
                final Treadmill treadmill = new Treadmill(treadmillPosition, scale, adjustedCenter, Enums.Direction.RIGHT);
                treadmill.setId(id);
                level.addGround(treadmill);
                Gdx.app.log(TAG, "Loaded the treadmill at " + treadmillPosition);
            } else if (item.getString(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.GATE_SPRITE_0)) {
                final Vector2 gatePosition = imagePosition.add(Constants.GATE_CENTER);
                final Gate gate = new Gate(gatePosition);
                gate.setId(id);
                level.addGround(gate);
                Gdx.app.log(TAG, "Loaded the gate at " + gatePosition);
            }
        }
    }

    private static final void loadNinePatches(LevelUpdater level, JsonValue.JsonIterator ninePatches) {

        for (Object o : ninePatches) {
            final JsonValue item = (JsonValue) o;

            final int id = extractId(item);
            final Vector2 imagePosition = extractPosition(item);
            final boolean[] tags = extractTags(item);
            final Enums.Energy type = extractType(item);
            float width = item.getFloat(Constants.LEVEL_WIDTH_KEY);
            float height = item.getFloat(Constants.LEVEL_HEIGHT_KEY);

            if (item.getString(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.BARRIER_SPRITE)) {
                final Barrier barrier;
                barrier = new Barrier(imagePosition.x, imagePosition.y, width, height, type, !tags[Constants.LEDGE_TAG_INDEX], tags[Constants.CONVERTIBLE_TAG_INDEX]);
                barrier.setId(id);
                level.addGround(barrier);
                Gdx.app.log(TAG, "Loaded the barrier at " + imagePosition.add(new Vector2(width / 2, height / 2)));
            } else if (item.getString(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.BOX_SPRITE)) {
                final Box box = new Box(imagePosition.x, imagePosition.y, width, height, type);
                box.setId(id);
                level.addGround(box);
                Gdx.app.log(TAG, "Loaded the box at " + imagePosition.add(new Vector2(width / 2, height / 2)));
            } else if (item.getString(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.BLOCK_SPRITE)) {
                final Brick brick = new Brick(imagePosition.x, imagePosition.y, width, height, type);
                brick.setId(id);
                level.addGround(brick);
                Gdx.app.log(TAG, "Loaded the brick at " + imagePosition.add(new Vector2(width / 2, height / 2)));
            } else if (item.getString(Constants.LEVEL_IMAGENAME_KEY).equals(Constants.LADDER_SPRITE)) {
                final Ladder ladder = new Ladder(imagePosition.x, imagePosition.y + height, width, height);
                ladder.setId(id);
                level.addGround(ladder);
                Gdx.app.log(TAG, "Loaded the ladder at " + imagePosition.add(new Vector2(width / 2, height / 2)));
            }
        }
    }
}