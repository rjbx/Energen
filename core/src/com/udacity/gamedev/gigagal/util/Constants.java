package com.udacity.gamedev.gigagal.util;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

// immutable non-instantiable static
public final class Constants {

    // cannot be subclassed
    private Constants() {}

    // World/Camera
    public static final Color BACKGROUND_COLOR = Color.SLATE;
    public static final float WORLD_SIZE = 160;
    public static final float KILL_PLANE = -800;
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
    public static final float CLIMB_SPEED = 80;
    public static final float GIGAGAL_STARTING_SPEED = 0.3f;
    public static final float STRIDING_JUMP_MULTIPLIER = 1.1f;
    public static final float Y_KNOCKBACK = 150;
    public static final float CLING_GRAVITY_OFFSET = 5;
    public static final float MAX_CLING_DURATION = .75f;
    public static final float MAX_JUMP_DURATION = 0.025f;
    public static final float MAX_HOVER_DURATION = 1.5f;
    public static final float MIN_GROUND_DISTANCE = 20;
    public static final float MAX_DASH_DURATION = 0.35f;
    public static final float MAX_LOOK_DISTANCE = 60;
    public static final float RECOVERY_TIME = 1;
    public static final float DOUBLE_TAP_SPEED = 0.2f;
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

    // Boss
    public static final String BOSS_SPRITE = "beast";

    // Box
    public static final int BOX_EDGE = 8;
    public static final float MAX_LEDGE_HEIGHT = 8;
    public static final String BOX_SPRITE = "box";
    public static final String BREAKABLE_BOX_SPRITE = "box-breakable";
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

    // Knob
    public static final Vector2 KNOB_CENTER = new Vector2(5.5f, 4.5f);
    public static final String KNOB_SPRITE_1 = "knob-1";
    public static final String KNOB_SPRITE_2 = "knob-2";

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
    
    // Trip
    public static final Vector2 TRIP_CENTER = new Vector2(6, 6);
    public static final String TRIP_SPRITE_1 = "trip-1";
    public static final String TRIP_SPRITE_2 = "trip-2";
    public static final String TRIP_SPRITE_3 = "trip-3";
    public static final String TRIP_SPRITE_4 = "trip-4";
    public static final float TRIP_LOAD_DURATION = 0.5f;
    public static final float TRIP_UNLOAD_DURATION = 0.5f;
    public static final float TRIP_SHOT_RADIUS = 6;

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

    // Lava
    public static final Vector2 LAVA_CENTER = new Vector2(16, 16);
    public static final String LAVA_SPRITE_1 = "lava-1";
    public static final String LAVA_SPRITE_2 = "lava-2";
    public static final float LAVA_DURATION = .25f;
    public static final int LAVA_DAMAGE = 25;
    public static final Vector2 LAVA_KNOCKBACK = new Vector2(150, 400);

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
    public static final float ZOOMBA_RANGE = 50;
    public static final String ZOOMBA_SPRITE = "zoomba";

    public static final String FIERYZOOMBA_SPRITE_1 = "zoomba-gas-1";
    public static final String FIERYZOOMBA_SPRITE_2 = "zoomba-gas-2";

    public static final String GUSHINGZOOMBA_SPRITE_1 = "zoomba-liquid-1";
    public static final String GUSHINGZOOMBA_SPRITE_2 = "zoomba-liquid-2";

    public static final String CHARGEDZOOMBA_SPRITE_1 = "zoomba-plasma-1";
    public static final String CHARGEDZOOMBA_SPRITE_2 = "zoomba-plasma-2";

    public static final String WHIRLINGZOOMBA_SPRITE_1 = "zoomba-polymer-1";
    public static final String WHIRLINGZOOMBA_SPRITE_2 = "zoomba-polymer-2";

    public static final String SHARPZOOMBA_SPRITE_1 = "zoomba-solid-1";
    public static final String SHARPZOOMBA_SPRITE_2 = "zoomba-solid-2";

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

    public static final String FIERYSWOOPA_SPRITE_1 = "swoopa-gas-1";
    public static final String FIERYSWOOPA_SPRITE_2 = "swoopa-gas-2";

    public static final String GUSHINGSWOOPA_SPRITE_1 = "swoopa-liquid-1";
    public static final String GUSHINGSWOOPA_SPRITE_2 = "swoopa-liquid-2";

    public static final String CHARGEDSWOOPA_SPRITE_1 = "swoopa-plasma-1";
    public static final String CHARGEDSWOOPA_SPRITE_2 = "swoopa-plasma-2";

    public static final String WHIRLINGSWOOPA_SPRITE_1 = "swoopa-polymer-1";
    public static final String WHIRLINGSWOOPA_SPRITE_2 = "swoopa-polymer-2";

    public static final String SHARPSWOOPA_SPRITE_1 = "swoopa-solid-1";
    public static final String SHARPSWOOPA_SPRITE_2 = "swoopa-solid-2";

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

    public static final String FIERYORBEN_SPRITE_0 = "orben-gas-active-0";
    public static final String FIERYORBEN_SPRITE_1 = "orben-gas-active-1";
    public static final String FIERYORBEN_SPRITE_2 = "orben-gas-active-2";

    public static final String GUSHINGORBEN_SPRITE_0 = "orben-liquid-active-0";
    public static final String GUSHINGORBEN_SPRITE_1 = "orben-liquid-active-1";
    public static final String GUSHINGORBEN_SPRITE_2 = "orben-liquid-active-2";

    public static final String CHARGEDORBEN_SPRITE_0 = "orben-plasma-active-0";
    public static final String CHARGEDORBEN_SPRITE_1 = "orben-plasma-active-1";
    public static final String CHARGEDORBEN_SPRITE_2 = "orben-plasma-active-2";

    public static final String WHIRLINGORBEN_SPRITE_0 = "orben-polymer-active-0";
    public static final String WHIRLINGORBEN_SPRITE_1 = "orben-polymer-active-1";
    public static final String WHIRLINGORBEN_SPRITE_2 = "orben-polymer-active-2";

    public static final String SHARPORBEN_SPRITE_0 = "orben-solid-active-0";
    public static final String SHARPORBEN_SPRITE_1 = "orben-solid-active-1";
    public static final String SHARPORBEN_SPRITE_2 = "orben-solid-active-2";

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

    public static final String FIERYROLLEN_SPRITE_4 = "rollen-gas-4";
    public static final String FIERYROLLEN_SPRITE_1 = "rollen-gas-1";
    public static final String FIERYROLLEN_SPRITE_2 = "rollen-gas-2";
    public static final String FIERYROLLEN_SPRITE_3 = "rollen-gas-3";

    public static final String GUSHINGROLLEN_SPRITE_4 = "rollen-liquid-4";
    public static final String GUSHINGROLLEN_SPRITE_1 = "rollen-liquid-1";
    public static final String GUSHINGROLLEN_SPRITE_2 = "rollen-liquid-2";
    public static final String GUSHINGROLLEN_SPRITE_3 = "rollen-liquid-3";

    public static final String CHARGEDROLLEN_SPRITE_4 = "rollen-plasma-4";
    public static final String CHARGEDROLLEN_SPRITE_1 = "rollen-plasma-1";
    public static final String CHARGEDROLLEN_SPRITE_2 = "rollen-plasma-2";
    public static final String CHARGEDROLLEN_SPRITE_3 = "rollen-plasma-3";

    public static final String WHIRLINGROLLEN_SPRITE_4 = "rollen-polymer-4";
    public static final String WHIRLINGROLLEN_SPRITE_1 = "rollen-polymer-1";
    public static final String WHIRLINGROLLEN_SPRITE_2 = "rollen-polymer-2";
    public static final String WHIRLINGROLLEN_SPRITE_3 = "rollen-polymer-3";

    public static final String SHARPROLLEN_SPRITE_4 = "rollen-solid-4";
    public static final String SHARPROLLEN_SPRITE_1 = "rollen-solid-1";
    public static final String SHARPROLLEN_SPRITE_2 = "rollen-solid-2";
    public static final String SHARPROLLEN_SPRITE_3 = "rollen-solid-3";

    // Protrusions
    public static final Vector2 SPIKE_CENTER = new Vector2(4, 8);
    public static final float SPIKE_DURATION = 0.5f;
    public static final int SPIKE_DAMAGE = 5;
    public static final Vector2 SPIKE_KNOCKBACK = new Vector2(50, Y_KNOCKBACK);
    public static final float SPIKE_COLLISION_WIDTH = 9;
    public static final float SPIKE_COLLISION_HEIGHT = 17;
    public static final String SPIKE_SPRITE_1 = "protrusion-solid-1";
    public static final String SPIKE_SPRITE_2 = "protrusion-solid-2";

    public static final Vector2 FLAME_CENTER = new Vector2(8.5f, 17.5f);
    public static final float FLAME_DURATION = 0.15f;
    public static final int FLAME_DAMAGE = 15;
    public static final Vector2 FLAME_KNOCKBACK = new Vector2(100, Y_KNOCKBACK);
    public static final float FLAME_COLLISION_WIDTH = 15;
    public static final float FLAME_COLLISION_HEIGHT = 25;
    public static final String FLAME_SPRITE_1 = "protrusion-gas-1";
    public static final String FLAME_SPRITE_2 = "protrusion-gas-2";

    public static final Vector2 GEISER_CENTER = new Vector2(8.5f, 17.5f);
    public static final float GEISER_DURATION = 0.3f;
    public static final int GEISER_DAMAGE = 10;
    public static final Vector2 GEISER_KNOCKBACK = new Vector2(25, Y_KNOCKBACK);
    public static final float GEISER_COLLISION_WIDTH = 15;
    public static final float GEISER_COLLISION_HEIGHT = 25;
    public static final String GEISER_SPRITE_1 = "protrusion-liquid-1";
    public static final String GEISER_SPRITE_2 = "protrusion-liquid-2";

    public static final Vector2 ROD_CENTER = new Vector2(4, 8);
    public static final float ROD_DURATION = 0.3f;
    public static final int ROD_DAMAGE = 10;
    public static final Vector2 ROD_KNOCKBACK = new Vector2(25, Y_KNOCKBACK);
    public static final float ROD_COLLISION_WIDTH = 8;
    public static final float ROD_COLLISION_HEIGHT = 16;
    public static final String ROD_SPRITE_1 = "protrusion-plasma-1";
    public static final String ROD_SPRITE_2 = "protrusion-plasma-2";

    public static final Vector2 WHIRL_CENTER = new Vector2(8.5f, 17.5f);
    public static final float WHIRL_DURATION = 0.3f;
    public static final int WHIRL_DAMAGE = 10;
    public static final Vector2 WHIRL_KNOCKBACK = new Vector2(25, Y_KNOCKBACK);
    public static final float WHIRL_COLLISION_WIDTH = 15;
    public static final float WHIRL_COLLISION_HEIGHT = 25;
    public static final String WHIRL_SPRITE_1 = "protrusion-polymer-1";
    public static final String WHIRL_SPRITE_2 = "protrusion-polymer-2";

    // Suspensions
    public static final Vector2 BURNER_CENTER = new Vector2(12.5f, 12.5f);
    public static final float BURNER_DURATION = 0.1f;
    public static final int BURNER_DAMAGE = 5;
    public static final Vector2 BURNER_KNOCKBACK = new Vector2(200, 250);
    public static final float BURNER_COLLISION_WIDTH = 25;
    public static final float BURNER_COLLISION_HEIGHT = 25;
    public static final String BURNER_SPRITE_1 = "suspension-gas-1";
    public static final String BURNER_SPRITE_2 = "suspension-gas-2";
    
    public static final Vector2 COIL_CENTER = new Vector2(12.5f, 12.5f);
    public static final float COIL_DURATION = 0.5f;
    public static final int COIL_DAMAGE = 5;
    public static final Vector2 COIL_KNOCKBACK = new Vector2(200, 50);
    public static final float COIL_COLLISION_WIDTH = 25;
    public static final float COIL_COLLISION_HEIGHT = 25;
    public static final String COIL_SPRITE_1 = "suspension-plasma-1";
    public static final String COIL_SPRITE_2 = "suspension-plasma-2";
    
    public static final Vector2 LUMP_CENTER = new Vector2(12.5f, 12.5f);
    public static final float LUMP_DURATION = 0.5f;
    public static final int LUMP_DAMAGE = 5;
    public static final Vector2 LUMP_KNOCKBACK = new Vector2(200, 50);
    public static final float LUMP_COLLISION_WIDTH = 25;
    public static final float LUMP_COLLISION_HEIGHT = 25;
    public static final String LUMP_SPRITE_1 = "suspension-liquid-1";
    public static final String LUMP_SPRITE_2 = "suspension-liquid-2";

    public static final Vector2 SHARP_CENTER = new Vector2(12.5f, 12.5f);
    public static final float SHARP_DURATION = 0.5f;
    public static final int SHARP_DAMAGE = 5;
    public static final Vector2 SHARP_KNOCKBACK = new Vector2(200, 50);
    public static final float SHARP_COLLISION_WIDTH = 25;
    public static final float SHARP_COLLISION_HEIGHT = 25;
    public static final String SHARP_SPRITE_1 = "suspension-solid-1";
    public static final String SHARP_SPRITE_2 = "suspension-solid-2";

    public static final Vector2 WHEEL_CENTER = new Vector2(12.5f, 12.5f);
    public static final float WHEEL_DURATION = 0.1f;
    public static final int WHEEL_DAMAGE = 5;
    public static final Vector2 WHEEL_KNOCKBACK = new Vector2(200, 250);
    public static final float WHEEL_COLLISION_WIDTH = 25;
    public static final float WHEEL_COLLISION_HEIGHT = 25;
    public static final String WHEEL_SPRITE_1 = "suspension-polymer-1";
    public static final String WHEEL_SPRITE_2 = "suspension-polymer-2";

    public static final Vector2 VACUUM_CENTER = new Vector2(31, 31);
    public static final float VACUUM_FRAME_DURATION = 0.4f;
    public static final int VACUUM_DAMAGE = 100;
    public static final Vector2 VACUUM_KNOCKBACK = new Vector2(100, 10);
    public static final float VACUUM_COLLISION_WIDTH = 25;
    public static final float VACUUM_COLLISION_HEIGHT = 25;
    public static final String VACUUM_SPRITE_1 = "suspension-psychic-1";
    public static final String VACUUM_SPRITE_2 = "suspension-psychic-2";
    public static final String VACUUM_SPRITE_3 = "suspension-psychic-3";

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
    public static final String SHOT_GAS_SPRITE = "ammo-shot-gas";
    public static final String BLAST_GAS_SPRITE = "ammo-blast-gas";
    public static final String SHOT_LIQUID_SPRITE = "ammo-shot-liquid";
    public static final String BLAST_LIQUID_SPRITE = "ammo-blast-liquid";
    public static final String SHOT_PLASMA_SPRITE = "ammo-shot-plasma";
    public static final String BLAST_PLASMA_SPRITE = "ammo-blast-plasma";
    public static final String SHOT_POLYMER_SPRITE = "ammo-shot-polymer";
    public static final String BLAST_POLYMER_SPRITE = "ammo-blast-polymer";
    public static final String SHOT_SOLID_SPRITE = "ammo-shot-solid";
    public static final String BLAST_SOLID_SPRITE = "ammo-blast-solid";
    public static final String SHOT_PSYCHIC_SPRITE = "ammo-shot-psychic";
    public static final String BLAST_PSYCHIC_SPRITE = "ammo-blast-psychic";
    public static final String SHOT_HYBRID_SPRITE = "ammo-shot-hybrid";
    public static final String BLAST_HYBRID_SPRITE = "ammo-blast-hybrid";

    // Impact
    public static final Vector2 EXPLOSION_CENTER = new Vector2(8, 8);
    public static final float IMPACT_DURATION = 0.5f;
    public static final String IMPACT_PLASMA_LARGE = "impact-plasma-large";
    public static final String IMPACT_PLASMA_MEDIUM = "impact-plasma-medium";
    public static final String IMPACT_PLASMA_SMALL = "impact-plasma-small";
    public static final String IMPACT_GAS_LARGE = "impact-gas-large";
    public static final String IMPACT_GAS_MEDIUM = "impact-gas-medium";
    public static final String IMPACT_GAS_SMALL = "impact-gas-small";
    public static final String IMPACT_LIQUID_LARGE = "impact-liquid-large";
    public static final String IMPACT_LIQUID_MEDIUM = "impact-liquid-medium";
    public static final String IMPACT_LIQUID_SMALL = "impact-liquid-small";
    public static final String IMPACT_SOLID_LARGE = "impact-solid-large";
    public static final String IMPACT_SOLID_MEDIUM = "impact-solid-medium";
    public static final String IMPACT_SOLID_SMALL = "impact-solid-small";
    public static final String IMPACT_HYBRID_LARGE = "impact-hybrid-large";
    public static final String IMPACT_HYBRID_MEDIUM = "impact-hybrid-medium";
    public static final String IMPACT_HYBRID_SMALL = "impact-hybrid-small";
    public static final String IMPACT_PSYCHIC_LARGE = "impact-psychic-large";
    public static final String IMPACT_PSYCHIC_MEDIUM = "impact-psychic-medium";
    public static final String IMPACT_PSYCHIC_SMALL = "impact-psychic-small";
    public static final String IMPACT_NATIVE_LARGE = "impact-native-large";
    public static final String IMPACT_NATIVE_MEDIUM = "impact-native-medium";
    public static final String IMPACT_NATIVE_SMALL = "impact-native-small";

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
    public static final String LEVEL_READ_MESSAGE = "Could not read from the specified level JSON file name";
    public static final String LEVEL_KEY_MESSAGE = "Could not load an invalid key value assigned to a JSON object parameter";
    public static final String LEVEL_IMAGENAME_KEY = "imageName";
    public static final String LEVEL_UNIQUE_ID_KEY = "uniqueId";
    public static final String LEVEL_X_POSITION_KEY = "x";
    public static final String LEVEL_Y_POSITION_KEY = "y";
    public static final String LEVEL_WIDTH_KEY = "width";
    public static final String LEVEL_HEIGHT_KEY = "height";
    public static final String LEVEL_X_SCALE_KEY = "scaleX";
    public static final String LEVEL_Y_SCALE_KEY = "scaleY";
    public static final String LEVEL_IDENTIFIER_KEY = "itemIdentifier";
    public static final String LEVEL_RANGE_KEY = "range";
    public static final String LEVEL_TYPE_KEY = "type";
    public static final String LEVEL_INTENSITY_KEY = "intensity";
    public static final String LEVEL_BOUNDS_KEY = "bounds";

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
    public static final String LEFT_BUTTON = "button-directional-left";
    public static final String RIGHT_BUTTON = "button-directional-right";
    public static final String UP_BUTTON = "button-directional-up";
    public static final String DOWN_BUTTON = "button-directional-down";
    public static final String CENTER_BUTTON = "button-directional-center";
    public static final String SHOOT_BUTTON = "button-bash";
    public static final String JUMP_BUTTON = "button-bash";
    public static final String PAUSE_BUTTON = "button-pause";
    public static final String SELECTION_CURSOR = "selection-cursor";

    // Victory/Game Over screens
    public static final float LEVEL_END_DURATION = 5;
    public static final int EXPLOSION_COUNT = 500;
    public static final int ZOOMBA_COUNT = 200;
    public static final String VICTORY_MESSAGE = "Boo Ya.";
    public static final String DEFEAT_MESSAGE = "Game Over, Gal";
    public static final String LAUNCH_MESSAGE = "Energraft v0.0.1 (c) Qualiv 2017";
    public static final String FONT_FILE = "font/header.fnt";
    public static final String TIME_PATTERN = "HH:mm:ss";
    public static final DateTimeFormatter TIME_FORMATTER = DateTimeFormat.forPattern(TIME_PATTERN);

    // Start screen
    public static final String LOGO = "qualiv";
    public static final Vector2 LOGO_CENTER = new Vector2(117.5f, 117.5f);

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

    // Preferences
    public static final int[] DIFFICULTY_MULTIPLIER = {1, 2, 3};

    // Overlays
    public static final String DEBUG_MODE_MESSAGE = "DEBUG MODE\n\nPRESS SHOOT TO EXIT";
}
