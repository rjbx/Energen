package com.udacity.gamedev.gigagal.util;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

// immutable static
public final class Constants {

    // non-instantiable; cannot be subclassed
    private Constants() {}

    // World/Camera
    public static final Color BACKGROUND_COLOR = Color.SLATE;
    public static final float WORLD_SIZE = 160;
    public static final float KILL_PLANE = -200;
    public static final float GRAVITY = 10;
    public static final float CHASE_CAM_MOVE_SPEED = WORLD_SIZE;
    public static final String TEXTURE_ATLAS = "images/gigagal.pack.atlas";

    // GigaGal attributes
    public static final Vector2 GIGAGAL_EYE_POSITION = new Vector2(14f, 24);
    public static final float GIGAGAL_EYE_HEIGHT = 16.0f;
    public static final float GIGAGAL_STANCE_WIDTH = 19.0f;
    public static final Vector2 GIGAGAL_CANNON_OFFSET = new Vector2(7.5f, -5.75f);
    public static final float GIGAGAL_HEIGHT = 21.0f;
    public static final float GIGAGAL_HEAD_RADIUS = 5;
    public static final float GIGAGAL_MAX_SPEED = 200;
    public static final float JUMP_SPEED = 200;
    public static final float CLIMB_SPEED = 40;
    public static final float GIGAGAL_STARTING_SPEED = 0.3f;
    public static final float STRIDING_JUMP_MULTIPLIER = 1.1f;
    public static final float Y_KNOCKBACK = 150;
    public static final float MAX_JUMP_DURATION = 0.025f;
    public static final float MAX_HOVER_DURATION = 1.5f;
    public static final float MIN_GROUND_DISTANCE = 20;
    public static final float MAX_DASH_DURATION = 0.35f;
    public static final float MAX_LOOK_DISTANCE = 60;
    public static final float RECOVERY_TIME = 1;
    public static final float DOUBLE_TAP_SPEED = 0.2f;
    public static final float GROUND_SLIDE_MIN_TURBO = 25;
    public static final float STRIDE_TURBO_INCREMENT = 0.75f;
    public static final float FALL_TURBO_INCREMENT = 1;
    public static final float STAND_TURBO_INCREMENT = 1.25f;
    public static final int BLAST_AMMO_CONSUMPTION = 3;
    public static final int SHOT_AMMO_CONSUMPTION = 1;
    public static final int INITIAL_AMMO = 10;
    public static final int INITIAL_HEALTH = 100;
    public static final int INITIAL_LIVES = 3;
    public static final int MAX_AMMO = 100;
    public static final int MAX_HEALTH = 100;
    public static final float MAX_TURBO = 100;

    // Gigagal assets
    public static final String STAND_RIGHT = "gg-stand-right";
    public static final String STAND_LEFT = "gg-stand-left";
    public static final String RECOIL_RIGHT = "gg-recoil-right";
    public static final String RECOIL_LEFT = "gg-recoil-left";
    public static final String FALL_RIGHT = "gg-fall-right";
    public static final String FALL_LEFT = "gg-fall-left";
    public static final String LOOKUP_STAND_RIGHT = "gg-lookup-stand-right";
    public static final String LOOKUP_STAND_LEFT = "gg-lookup-stand-left";
    public static final String LOOKDOWN_STAND_RIGHT = "gg-lookdown-stand-right";
    public static final String LOOKDOWN_STAND_LEFT = "gg-lookdown-stand-left";
    public static final String LOOKUP_FALL_RIGHT = "gg-lookup-fall-right";
    public static final String LOOKUP_FALL_LEFT = "gg-lookup-fall-left";
    public static final String LOOKDOWN_FALL_RIGHT = "gg-lookdown-fall-right";
    public static final String LOOKDOWN_FALL_LEFT = "gg-lookdown-fall-left";
    public static final String LOOKUP_HOVER_RIGHT_1 = "gg-lookup-hover-right-1";
    public static final String LOOKUP_HOVER_LEFT_1 = "gg-lookup-hover-left-1";
    public static final String LOOKDOWN_HOVER_RIGHT_1 = "gg-lookdown-hover-right-1";
    public static final String LOOKDOWN_HOVER_LEFT_1 = "gg-lookdown-hover-left-1";
    public static final String LOOKUP_HOVER_RIGHT_2 = "gg-lookup-hover-right-2";
    public static final String LOOKUP_HOVER_LEFT_2 = "gg-lookup-hover-left-2";
    public static final String LOOKDOWN_HOVER_RIGHT_2 = "gg-lookdown-hover-right-2";
    public static final String LOOKDOWN_HOVER_LEFT_2 = "gg-lookdown-hover-left-2";
    public static final String CLING_RIGHT = "gg-cling-right";
    public static final String CLING_LEFT = "gg-cling-left";
    public static final float CLING_FRAME_DURATION = 0.05f;
    public static final String HOVER_RIGHT_1 = "gg-hover-1-right";
    public static final String HOVER_LEFT_1 = "gg-hover-1-left";
    public static final String HOVER_RIGHT_2 = "gg-hover-2-right";
    public static final String HOVER_LEFT_2 = "gg-hover-2-left";
    public static final float HOVER_LOOP_DURATION = 0.05f;
    public static final String STRIDE_RIGHT_1 = "gg-stride-1-right";
    public static final String STRIDE_LEFT_1 = "gg-stride-1-left";
    public static final String STRIDE_RIGHT_2 = "gg-stride-2-right";
    public static final String STRIDE_LEFT_2 = "gg-stride-2-left";
    public static final String STRIDE_RIGHT_3 = "gg-stride-3-right";
    public static final String STRIDE_LEFT_3 = "gg-stride-3-left";
    public static final float STRIDE_LOOP_DURATION = 0.1f;
    public static final String CLIMB_1 = "gg-climb-1";
    public static final String CLIMB_2 = "gg-climb-2";
    public static final float CLIMB_LOOP_DURATION = 0.25f;

    // Box
    public static final int BOX_EDGE = 8;
    public static final float MAX_LEDGE_HEIGHT = 8;
    public static final String BOX_SPRITE = "box";
    public static final String BLUE_BOX_SPRITE = "box-blue";
    public static final String YELLOW_BOX_SPRITE = "box-yellow";
    public static final String BLACK_BOX_SPRITE = "box-black";
    public static final String RED_BOX_SPRITE = "box-red";
    public static final String GREY_BOX_SPRITE = "box-grey";
    public static final String CLEAR_BOX_SPRITE = "box-clear";
    public static final String MAGENTA_BOX_SPRITE = "box-magenta";

    // Pillar
    public static final Vector2 PILLAR_CENTER = new Vector2(6, 3);
    public static final String PILLAR_SPRITE = "pillar";

    // Lift
    public static final Vector2 LIFT_CENTER = new Vector2(18, 4);
    public static final String LIFT_SPRITE = "lift";
    public static final float LIFT_RANGE = 80;
    public static final float LIFT_SPEED = 40;

    // Ladder
    public static final Vector2 LADDER_CENTER = new Vector2(8.5f, 25);
    public static final String LADDER_SPRITE = "ladder";
    public static final int LADDER_X_EDGE = 4;
    public static final int LADDER_Y_EDGE = 0;

    // Vines
    public static final Vector2 VINES_CENTER = new Vector2(12.5f, 20);
    public static final String VINES_SPRITE = "vines";

    // Rope
    public static final Vector2 ROPE_CENTER = new Vector2(3, 50);
    public static final String ROPE_SPRITE = "rope";

    // Pole
    public static final Vector2 POLE_CENTER = new Vector2(2.5f, 24);
    public static final String POLE_SPRITE_1 = "pole-1";
    public static final String POLE_SPRITE_2 = "pole-2";
    public static final float POLE_DURATION = 1;

    // Slick
    public static final Vector2 SLICK_CENTER = new Vector2(24, 8);
    public static final String SLICK_SPRITE_1 = "slick-1";
    public static final String SLICK_SPRITE_2 = "slick-2";
    public static final float SLICK_DURATION = 1;

    // Ice
    public static final Vector2 ICE_CENTER = new Vector2(24, 8);
    public static final String ICE_SPRITE_1 = "ice-1";
    public static final String ICE_SPRITE_2 = "ice-2";
    public static final float ICE_DURATION = 1;

    // Treadmill
    public static final Vector2 TREADMILL_CENTER = new Vector2(19, 9);
    public static final String TREADMILL_1_RIGHT = "treadmill-1-right";
    public static final String TREADMILL_2_RIGHT = "treadmill-2-right";
    public static final String TREADMILL_1_LEFT = "treadmill-1-left";
    public static final String TREADMILL_2_LEFT = "treadmill-2-left";
    public static final float TREADMILL_DURATION = .1f;
    public static final float TREADMILL_SPEED = 175;

    // Spring
    public static final Vector2 SPRING_CENTER = new Vector2(11.5f, 4);
    public static final String SPRING_SPRITE_1 = "spring-1";
    public static final String SPRING_SPRITE_2 = "spring-2";
    public static final String SPRING_SPRITE_3 = "spring-3";
    public static final String SPRING_SPRITE_4 = "spring-4";
    public static final float SPRING_LOAD_DURATION = 0.5f;
    public static final float SPRING_UNLOAD_DURATION = 0.5f;

    // Sink
    public static final Vector2 SINK_CENTER = new Vector2(21, 9);
    public static final String SINK_SPRITE_1 = "sink-1";
    public static final String SINK_SPRITE_2 = "sink-2";
    public static final float SINK_DURATION = 1.25f;

    // Coals
    public static final Vector2 COALS_CENTER = new Vector2(21, 9);
    public static final String COALS_SPRITE_1 = "coals-1";
    public static final String COALS_SPRITE_2 = "coals-2";
    public static final float COALS_DURATION = .25f;

    // Cannon
    public static final Vector2 X_CANNON_CENTER = new Vector2(10, 6);
    public static final String X_CANNON_SPRITE = "cannon-x";
    public static final Vector2 Y_CANNON_CENTER = new Vector2(8, 10);
    public static final String Y_CANNON_SPRITE = "cannon-y";

    // Zoomba
    public static final Vector2 ZOOMBA_CENTER = new Vector2(14.5f, 25.5f);
    public static final float ZOOMBA_MOVEMENT_SPEED = 20;
    public static final float ZOOMBA_BOB_AMPLITUDE = 20;
    public static final float ZOOMBA_BOB_PERIOD = 2f;
    public static final int ZOOMBA_MAX_HEALTH = 30;
    public static final int ZOOMBA_STANDARD_DAMAGE = 1;
    public static final Vector2 ZOOMBA_KNOCKBACK = new Vector2(75, Y_KNOCKBACK);
    public static final float ZOOMBA_COLLISION_WIDTH = 30;
    public static final float ZOOMBA_COLLISION_HEIGHT = 51;
    public static final float ZOOMBA_SHOT_RADIUS = 17;
    public static final String ZOOMBA_SPRITE = "zoomba";

    // Fiery-Zoomba
    public static final String FIERYZOOMBA_SPRITE_1 = "zoomba-fire-1";
    public static final String FIERYZOOMBA_SPRITE_2 = "zoomba-fire-2";

    // Gushing-Zoomba
    public static final String GUSHINGZOOMBA_SPRITE_1 = "zoomba-water-1";
    public static final String GUSHINGZOOMBA_SPRITE_2 = "zoomba-water-2";

    // Charged-Zoomba
    public static final String CHARGEDZOOMBA_SPRITE_1 = "zoomba-electric-1";
    public static final String CHARGEDZOOMBA_SPRITE_2 = "zoomba-electric-2";

    // Whirling-Zoomba
    public static final String WHIRLINGZOOMBA_SPRITE_1 = "zoomba-rubber-1";
    public static final String WHIRLINGZOOMBA_SPRITE_2 = "zoomba-rubber-2";

    // Sharp-Zoomba
    public static final String SHARPZOOMBA_SPRITE_1 = "zoomba-metal-1";
    public static final String SHARPZOOMBA_SPRITE_2 = "zoomba-metal-2";

    // Swoopa
    public static final Vector2 SWOOPA_CENTER = new Vector2(25f, 13.5f);
    public static final float SWOOPA_MOVEMENT_SPEED = 75;
    public static final int SWOOPA_MAX_HEALTH = 30;
    public static final int SWOOPA_STANDARD_DAMAGE = 1;
    public static final Vector2 SWOOPA_KNOCKBACK = new Vector2(75, Y_KNOCKBACK);
    public static final float SWOOPA_COLLISION_WIDTH = 60;
    public static final float SWOOPA_COLLISION_HEIGHT = 27;
    public static final float SWOOPA_SHOT_RADIUS = 17;
    public static final String SWOOPA_SPRITE = "swoopa";

    // Fiery-Swoopa
    public static final String FIERYSWOOPA_SPRITE_1 = "swoopa-fire-1";
    public static final String FIERYSWOOPA_SPRITE_2 = "swoopa-fire-2";

    // Gushing-Swoopa
    public static final String GUSHINGSWOOPA_SPRITE_1 = "swoopa-water-1";
    public static final String GUSHINGSWOOPA_SPRITE_2 = "swoopa-water-2";

    // Charged-Swoopa
    public static final String CHARGEDSWOOPA_SPRITE_1 = "swoopa-electric-1";
    public static final String CHARGEDSWOOPA_SPRITE_2 = "swoopa-electric-2";

    // Whirling-Swoopa
    public static final String WHIRLINGSWOOPA_SPRITE_1 = "swoopa-rubber-1";
    public static final String WHIRLINGSWOOPA_SPRITE_2 = "swoopa-rubber-2";

    // Sharp-Swoopa
    public static final String SHARPSWOOPA_SPRITE_1 = "swoopa-metal-1";
    public static final String SHARPSWOOPA_SPRITE_2 = "swoopa-metal-2";

    // Orben
    public static final float ORBEN_TEXTURE_SCALE = 1.5f;
    public static final Vector2 ORBEN_CENTER = new Vector2(5 * ORBEN_TEXTURE_SCALE, 5 * ORBEN_TEXTURE_SCALE);
    public static final float ORBEN_COLLISION_WIDTH = 10 * ORBEN_TEXTURE_SCALE;
    public static final float ORBEN_COLLISION_HEIGHT = 10 * ORBEN_TEXTURE_SCALE;
    public static final float ORBEN_SHOT_RADIUS = 5 * ORBEN_TEXTURE_SCALE;
    public static final float ORBEN_MOVEMENT_SPEED = 25;
    public static final int ORBEN_MAX_HEALTH = 30;
    public static final int ORBEN_STANDARD_DAMAGE = 1;
    public static final Vector2 ORBEN_KNOCKBACK = new Vector2(75, Y_KNOCKBACK);
    public static final float ORBEN_DURATION = 1.5f;
    public static final int ORBEN_REGIONS = 3;
    public static final String DORMANTORBEN_SPRITE = "orben-dormant";

    // Fiery-Orben
    public static final String FIERYORBEN_SPRITE_0 = "orben-fire-active-0";
    public static final String FIERYORBEN_SPRITE_1 = "orben-fire-active-1";
    public static final String FIERYORBEN_SPRITE_2 = "orben-fire-active-2";

    // Gushing-Orben
    public static final String GUSHINGORBEN_SPRITE_0 = "orben-water-active-0";
    public static final String GUSHINGORBEN_SPRITE_1 = "orben-water-active-1";
    public static final String GUSHINGORBEN_SPRITE_2 = "orben-water-active-2";

    // Charged-Orben
    public static final String CHARGEDORBEN_SPRITE_0 = "orben-electric-active-0";
    public static final String CHARGEDORBEN_SPRITE_1 = "orben-electric-active-1";
    public static final String CHARGEDORBEN_SPRITE_2 = "orben-electric-active-2";

    // Whirling-Orben
    public static final String WHIRLINGORBEN_SPRITE_0 = "orben-rubber-active-0";
    public static final String WHIRLINGORBEN_SPRITE_1 = "orben-rubber-active-1";
    public static final String WHIRLINGORBEN_SPRITE_2 = "orben-rubber-active-2";

    // Sharp-Orben
    public static final String SHARPORBEN_SPRITE_0 = "orben-metal-active-0";
    public static final String SHARPORBEN_SPRITE_1 = "orben-metal-active-1";
    public static final String SHARPORBEN_SPRITE_2 = "orben-metal-active-2";

    // Rollen
    public static final float ROLLEN_TEXTURE_SCALE = 1.5f;
    public static final Vector2 ROLLEN_CENTER = new Vector2(12.5f * ROLLEN_TEXTURE_SCALE, 12.5f * ROLLEN_TEXTURE_SCALE);
    public static final float ROLLEN_COLLISION_WIDTH = 25 * ROLLEN_TEXTURE_SCALE;
    public static final float ROLLEN_COLLISION_HEIGHT = 25 * ROLLEN_TEXTURE_SCALE;
    public static final float ROLLEN_SHOT_RADIUS = 4 * ROLLEN_TEXTURE_SCALE;
    public static final float ROLLEN_MOVEMENT_SPEED = 75;
    public static final int ROLLEN_MAX_HEALTH = 60;
    public static final int ROLLEN_STANDARD_DAMAGE = 1;
    public static final Vector2 ROLLEN_KNOCKBACK = new Vector2(75, Y_KNOCKBACK);
    public static final float ROLLEN_DURATION = .5f;
    public static final int ROLLEN_REGIONS = 4;

    // Fiery-Rollen
    public static final String FIERYROLLEN_SPRITE_4 = "rollen-fire-4";
    public static final String FIERYROLLEN_SPRITE_1 = "rollen-fire-1";
    public static final String FIERYROLLEN_SPRITE_2 = "rollen-fire-2";
    public static final String FIERYROLLEN_SPRITE_3 = "rollen-fire-3";

    // Gushing-Rollen
    public static final String GUSHINGROLLEN_SPRITE_4 = "rollen-water-4";
    public static final String GUSHINGROLLEN_SPRITE_1 = "rollen-water-1";
    public static final String GUSHINGROLLEN_SPRITE_2 = "rollen-water-2";
    public static final String GUSHINGROLLEN_SPRITE_3 = "rollen-water-3";

    // Charged-Rollen
    public static final String CHARGEDROLLEN_SPRITE_4 = "rollen-electric-4";
    public static final String CHARGEDROLLEN_SPRITE_1 = "rollen-electric-1";
    public static final String CHARGEDROLLEN_SPRITE_2 = "rollen-electric-2";
    public static final String CHARGEDROLLEN_SPRITE_3 = "rollen-electric-3";

    // Whirling-Rollen
    public static final String WHIRLINGROLLEN_SPRITE_4 = "rollen-rubber-4";
    public static final String WHIRLINGROLLEN_SPRITE_1 = "rollen-rubber-1";
    public static final String WHIRLINGROLLEN_SPRITE_2 = "rollen-rubber-2";
    public static final String WHIRLINGROLLEN_SPRITE_3 = "rollen-rubber-3";

    // Sharp-Rollen
    public static final String SHARPROLLEN_SPRITE_4 = "rollen-metal-4";
    public static final String SHARPROLLEN_SPRITE_1 = "rollen-metal-1";
    public static final String SHARPROLLEN_SPRITE_2 = "rollen-metal-2";
    public static final String SHARPROLLEN_SPRITE_3 = "rollen-metal-3";

    // Spike
    public static final Vector2 SPIKE_CENTER = new Vector2(4, 8);
    public static final float SPIKE_DURATION = 0.5f;
    public static final int SPIKE_DAMAGE = 5;
    public static final Vector2 SPIKE_KNOCKBACK = new Vector2(50, Y_KNOCKBACK);
    public static final float SPIKE_COLLISION_WIDTH = 9;
    public static final float SPIKE_COLLISION_HEIGHT = 17;
    public static final String SPIKE_SPRITE_1 = "spike-1";
    public static final String SPIKE_SPRITE_2 = "spike-2";

    // Flame
    public static final Vector2 FLAME_CENTER = new Vector2(8.5f, 17.5f);
    public static final float FLAME_DURATION = 0.15f;
    public static final int FLAME_DAMAGE = 15;
    public static final Vector2 FLAME_KNOCKBACK = new Vector2(100, Y_KNOCKBACK);
    public static final float FLAME_COLLISION_WIDTH = 15;
    public static final float FLAME_COLLISION_HEIGHT = 25;
    public static final String FLAME_SPRITE_1 = "flame-1";
    public static final String FLAME_SPRITE_2 = "flame-2";

    // Geiser
    public static final Vector2 GEISER_CENTER = new Vector2(8.5f, 17.5f);
    public static final float GEISER_DURATION = 0.3f;
    public static final int GEISER_DAMAGE = 10;
    public static final Vector2 GEISER_KNOCKBACK = new Vector2(25, Y_KNOCKBACK);
    public static final float GEISER_COLLISION_WIDTH = 15;
    public static final float GEISER_COLLISION_HEIGHT = 25;
    public static final String GEISER_SPRITE_1 = "geiser-1";
    public static final String GEISER_SPRITE_2 = "geiser-2";

    // Wheel
    public static final Vector2 WHEEL_CENTER = new Vector2(12.5f, 12.5f);
    public static final float WHEEL_DURATION = 0.1f;
    public static final int WHEEL_DAMAGE = 5;
    public static final Vector2 WHEEL_KNOCKBACK = new Vector2(200, 250);
    public static final float WHEEL_COLLISION_WIDTH = 25;
    public static final float WHEEL_COLLISION_HEIGHT = 25;
    public static final String WHEEL_SPRITE_1 = "wheel-1";
    public static final String WHEEL_SPRITE_2 = "wheel-2";

    // Coil
    public static final Vector2 COIL_CENTER = new Vector2(12.5f, 12.5f);
    public static final float COIL_DURATION = 0.5f;
    public static final int COIL_DAMAGE = 5;
    public static final Vector2 COIL_KNOCKBACK = new Vector2(200, 50);
    public static final float COIL_COLLISION_WIDTH = 25;
    public static final float COIL_COLLISION_HEIGHT = 25;
    public static final String COIL_SPRITE_1 = "coil-1";
    public static final String COIL_SPRITE_2 = "coil-2";

    // Vacuum
    public static final Vector2 VACUUM_CENTER = new Vector2(31, 31);
    public static final float VACUUM_FRAME_DURATION = 0.4f;
    public static final int VACUUM_DAMAGE = 100;
    public static final Vector2 VACUUM_KNOCKBACK = new Vector2(100, 10);
    public static final float VACUUM_COLLISION_WIDTH = 25;
    public static final float VACUUM_COLLISION_HEIGHT = 25;
    public static final String VACUUM_SPRITE_1 = "vacuum-1";
    public static final String VACUUM_SPRITE_2 = "vacuum-2";
    public static final String VACUUM_SPRITE_3 = "vacuum-3";

    // Portal
    public static final Vector2 PORTAL_CENTER = new Vector2(31, 31);
    public static final float PORTAL_RADIUS = 28;
    public static final float PORTAL_FRAME_DURATION = 0.25f;
    public static final String PORTAL_SPRITE_1 = "portal-1";
    public static final String PORTAL_SPRITE_2 = "portal-2";
    public static final String PORTAL_SPRITE_3 = "portal-3";
    public static final String PORTAL_SPRITE_4 = "portal-4";
    public static final String PORTAL_SPRITE_5 = "portal-5";
    public static final String PORTAL_SPRITE_6 = "portal-6";

    // Ammo
    public static final float CHARGE_DURATION = 1;
    public static final float AMMO_MAX_SPEED = 500;
    public static final float AMMO_NORMAL_SPEED = 375;
    public static final int AMMO_STANDARD_DAMAGE = 10;
    public static final int AMMO_SPECIALIZED_DAMAGE = 30;
    public static final int AMMO_WEAK_DAMAGE = 1;
    public static final float SHOT_RADIUS = 4;
    public static final float BLAST_RADIUS = 9;
    public static final Vector2 SHOT_CENTER = new Vector2(SHOT_RADIUS, SHOT_RADIUS);
    public static final Vector2 BLAST_CENTER = new Vector2(BLAST_RADIUS, BLAST_RADIUS);
    public static final String SHOT_NATIVE_SPRITE = "ammo-shot-native";
    public static final String BLAST_NATIVE_SPRITE = "ammo-blast-native";
    public static final String SHOT_FIRE_SPRITE = "ammo-shot-fire";
    public static final String SHOT_FIRE_SPRITE_ALT = "ammo-shot-fire-alt";
    public static final String BLAST_FIRE_SPRITE = "ammo-blast-fire";
    public static final String BLAST_FIRE_SPRITE_ALT = "ammo-blast-fire-alt";
    public static final String SHOT_WATER_SPRITE = "ammo-shot-water";
    public static final String SHOT_WATER_SPRITE_ALT = "ammo-shot-water-alt";
    public static final String BLAST_WATER_SPRITE = "ammo-blast-water";
    public static final String BLAST_WATER_SPRITE_ALT = "ammo-blast-water-alt";
    public static final String SHOT_ELECTRIC_SPRITE = "ammo-shot-electric";
    public static final String SHOT_ELECTRIC_SPRITE_ALT = "ammo-shot-electric-alt";
    public static final String BLAST_ELECTRIC_SPRITE = "ammo-blast-electric";
    public static final String SHOT_RUBBER_SPRITE = "ammo-shot-rubber";
    public static final String SHOT_RUBBER_SPRITE_ALT = "ammo-shot-rubber-alt";
    public static final String BLAST_RUBBER_SPRITE = "ammo-blast-rubber";
    public static final String SHOT_METAL_SPRITE = "ammo-shot-metal";
    public static final String SHOT_METAL_SPRITE_ALT = "ammo-shot-metal-alt";
    public static final String BLAST_METAL_SPRITE = "ammo-blast-metal";
    public static final String BLAST_METAL_SPRITE_ALT = "ammo-blast-metal-alt";
    public static final String SHOT_PSYCHIC_SPRITE = "ammo-shot-psychic";
    public static final String BLAST_PSYCHIC_SPRITE = "ammo-blast-psychic";
    public static final Enums.WeaponType[] weapons = {
                                    Enums.WeaponType.FIRE,
                                    Enums.WeaponType.WATER,
                                    Enums.WeaponType.ELECTRIC,
                                    Enums.WeaponType.RUBBER,
                                    Enums.WeaponType.METAL,
                                    Enums.WeaponType.PSYCHIC,
                                    Enums.WeaponType.NATIVE
                                    };

    // Explosion
    public static final Vector2 EXPLOSION_CENTER = new Vector2(8, 8);
    public static final float EXPLOSION_DURATION = 0.5f;
    public static final String EXPLOSION_LARGE = "explosion-large";
    public static final String EXPLOSION_MEDIUM = "explosion-medium";
    public static final String EXPLOSION_SMALL = "explosion-small";

    // Powerup
    public static final Vector2 POWERUP_CENTER = new Vector2(7, 5);
    public static final int POWERUP_AMMO = 10;
    public static final int POWERUP_HEALTH = 50;
    public static final int POWERUP_TURBO = 100;
    public static final String AMMO_POWERUP_SPRITE = "powerup-ammo";
    public static final String HEALTH_POWERUP_SPRITE = "powerup-health";
    public static final String TURBO_POWERUP_SPRITE = "powerup-turbo";

    // Level Loading
    public static final String LEVEL_COMPOSITE = "composite";
    public static final String LEVEL_9PATCHES = "sImage9patchs";
    public static final String LEVEL_IMAGES = "sImages";
    public static final String LEVEL_ERROR_MESSAGE = "There was a problem loading the level.";
    public static final String LEVEL_IMAGENAME_KEY = "imageName";
    public static final String LEVEL_X_KEY = "x";
    public static final String LEVEL_Y_KEY = "y";
    public static final String LEVEL_WIDTH_KEY = "width";
    public static final String LEVEL_HEIGHT_KEY = "height";
    public static final String LEVEL_X_SCALE_KEY = "scaleX";
    public static final String LEVEL_Y_SCALE_KEY = "scaleY";
    public static final String LEVEL_IDENTIFIER_KEY = "itemIdentifier";
    public static final String LEVEL_ZOOMBA_TAG = "Zoomba";
    public static final String LEVEL_FIERYZOOMBA_TAG = "FieryZoomba";
    public static final String LEVEL_GUSHINGZOOMBA_TAG = "GushingZoomba";
    public static final String LEVEL_CHARGEDZOOMBA_TAG = "ChargedZoomba";
    public static final String LEVEL_WHIRLINGZOOMBA_TAG = "WhirlingZoomba";
    public static final String LEVEL_SHARPZOOMBA_TAG = "SharpZoomba";
    public static final String LEVEL_SWOOPA_TAG = "Swoopa";
    public static final String LEVEL_FIERYSWOOPA_TAG = "FierySwoopa";
    public static final String LEVEL_GUSHINGSWOOPA_TAG = "GushingSwoopa";
    public static final String LEVEL_CHARGEDSWOOPA_TAG = "ChargedSwoopa";
    public static final String LEVEL_WHIRLINGSWOOPA_TAG = "WhirlingSwoopa";
    public static final String LEVEL_SHARPSWOOPA_TAG = "SharpSwoopa";
    public static final String LEVEL_Y_LIFT_TAG = "YLift";
    public static final String LEVEL_X_LIFT_TAG = "XLift";
    public static final String[] LEVELS =  {"levels/METAL.dt", "levels/RUBBER.dt", "levels/ELECTRIC.dt", "levels/WATER.dt", "levels/FIRE.dt", "levels/PSYCHIC.dt", "levels/FINAL.dt"};

    // HUD
    public static final float HUD_VIEWPORT_SIZE = 480;
    public static final float HUD_MARGIN = 20;
    public static final float AMMO_ICON_SCALE = 1.5f;
    public static final String SHOOT_ICON = "icon-shoot";
    public static final String BLAST_ICON = "icon-blast";
    public static final String JUMP_ICON = "icon-jump";
    public static final String HOVER_ICON = "icon-hover";
    public static final String CLING_ICON = "icon-cling";
    public static final String CLIMB_ICON = "icon-climb";
    public static final String DASH_ICON = "icon-dash";
    public static final String LIFE_ICON = "icon-life";
    public static final String HUD_AMMO_LABEL = "Ammo: ";
    public static final String HUD_SCORE_LABEL = "Score: ";
    public static final String HUD_HEALTH_LABEL = "Health: ";
    public static final String HUD_WEAPON_LABEL = "WeaponType: ";
    public static final Vector2 ICON_CENTER = new Vector2(20, 8.5f);
    public static final Color HEALTH_CRITICAL_COLOR = Color.RED;
    public static final Color HEALTH_LOW_COLOR = Color.CORAL;
    public static final Color HEALTH_NORMAL_COLOR = new Color(0x0077eeff);
    public static final Color HEALTH_MAX_COLOR = new Color(0x1e90ffff);
    public static final Color TURBO_NORMAL_COLOR = Color.FOREST;
    public static final Color TURBO_MAX_COLOR = new Color(0x006400FF);
    public static final Color AMMO_NORMAL_COLOR = new Color(0xB8860BFF);
    public static final Color AMMO_CHARGED_COLOR = Color.GOLDENROD;

    // Onscreen Controls
    public static final float CONTROLS_OVERLAY_VIEWPORT_SIZE = 200;
    public static final Vector2 BUTTON_CENTER = new Vector2(15, 15);
    public static final float POSITION_DIAMETER = 32;
    public static final float TOUCH_RADIUS = 20;
    public static final String LEFT_BUTTON = "button-left";
    public static final String RIGHT_BUTTON = "button-right";
    public static final String UP_BUTTON = "button-up";
    public static final String DOWN_BUTTON = "button-down";
    public static final String CENTER_BUTTON = "button-center";
    public static final String SHOOT_BUTTON = "button-a";
    public static final String BLAST_BUTTON = "button-a";
    public static final String JUMP_BUTTON = "button-b";
    public static final String HOVER_BUTTON = "button-b";
    public static final String CLING_BUTTON = "button-b";
    public static final String PAUSE_BUTTON = "button-pause";
    public static final String SELECTION_CURSOR = "selection-cursor";

    // Victory/Game Over screens
    public static final float LEVEL_END_DURATION = 5;
    public static final int EXPLOSION_COUNT = 500;
  //  public static final int ZOOMBA_COUNT = 200;
    public static final String VICTORY_MESSAGE = "Boo Ya.";
    public static final String GAME_OVER_MESSAGE = "Game Over, Gal";
    public static final String FONT_FILE = "font/header.fnt";
    public static final String TIME_PATTERN = "HH:mm:ss";
    public static final DateTimeFormatter TIME_FORMATTER = DateTimeFormat.forPattern(TIME_PATTERN);

    // Scoring
    public static final int ZOOMBA_KILL_SCORE = 100;
    public static final int ZOOMBA_HIT_SCORE = 25;
    public static final int SWOOPA_KILL_SCORE = 100;
    public static final int SWOOPA_HIT_SCORE = 25;
    public static final int ORBEN_KILL_SCORE = 100;
    public static final int ORBEN_HIT_SCORE = 25;
    public static final int ROLLEN_KILL_SCORE = 100;
    public static final int ROLLEN_HIT_SCORE = 25;
    public static final int POWERUP_SCORE = 50;
}
