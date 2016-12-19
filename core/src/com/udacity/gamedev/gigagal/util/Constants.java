package com.udacity.gamedev.gigagal.util;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

// immutable static
public final class Constants {

    // non-instantiable
    private Constants() {}

    // World/Camera
    public static final Color BACKGROUND_COLOR = Color.SLATE;
    public static final float WORLD_SIZE = 160;
    public static final float KILL_PLANE = -200;
    public static final float GRAVITY = 10;
    public static final float CHASE_CAM_MOVE_SPEED = WORLD_SIZE;
    public static final String TEXTURE_ATLAS = "images/gigagal.pack.atlas";

    // GigaGal attributes
    public static final Vector2 GIGAGAL_EYE_POSITION = new Vector2(16, 24);
    public static final float GIGAGAL_EYE_HEIGHT = 16.0f;
    public static final float GIGAGAL_STANCE_WIDTH = 19.0f;
    public static final Vector2 GIGAGAL_CANNON_OFFSET = new Vector2(4, -7);
    public static final float GIGAGAL_HEIGHT = 21.0f;
    public static final float GIGAGAL_HEAD_RADIUS = 5;
    public static final float GIGAGAL_MAX_SPEED = 200;
    public static final float JUMP_SPEED = 200;
    public static final float GIGAGAL_STARTING_SPEED = 0.3f;
    public static final float STRIDING_JUMP_MULTIPLIER = 1.1f;

    public static final float VERTICAL_KNOCKBACK = 150;
    public static final float MAX_JUMP_DURATION = 0.025f;
    public static final float MAX_HOVER_DURATION = 1;
    public static final float MIN_GROUND_DISTANCE = 20;
    public static final float MAX_DASH_DURATION = 0.35f;
    public static final float RECOVERY_TIME = 1;
    public static final float DOUBLE_TAP_SPEED = 0.2f;
    public static final int INITIAL_AMMO = 10;
    public static final int INITIAL_HEALTH = 100;
    public static final int INITIAL_LIVES = 3;

    // Gigagal assets
    public static final String STAND_RIGHT = "stand-right";
    public static final String STAND_LEFT = "stand-left";
    public static final String RECOIL_RIGHT = "recoil-right";
    public static final String RECOIL_LEFT = "recoil-left";
    public static final String FALL_RIGHT = "fall-right";
    public static final String FALL_LEFT = "fall-left";
    public static final String LOOKUP_RIGHT = "lookup-right";
    public static final String LOOKUP_LEFT = "lookup-left";
    public static final String LOOKDOWN_RIGHT = "lookdown-right";
    public static final String LOOKDOWN_LEFT = "lookdown-left";
    public static final String RICOCHET_RIGHT = "ricochet-right";
    public static final String RICOCHET_LEFT = "ricochet-left";
    public static final float RICOCHET_FRAME_DURATION = 0.05f;
    public static final String HOVER_RIGHT_1 = "hover-1-right";
    public static final String HOVER_LEFT_1 = "hover-1-left";
    public static final String HOVER_RIGHT_2 = "hover-2-right";
    public static final String HOVER_LEFT_2 = "hover-2-left";
    public static final float HOVER_LOOP_DURATION = 0.05f;
    public static final String STRIDE_RIGHT_1 = "stride-1-right";
    public static final String STRIDE_LEFT_1 = "stride-1-left";
    public static final String STRIDE_RIGHT_2 = "stride-2-right";
    public static final String STRIDE_LEFT_2 = "stride-2-left";
    public static final String STRIDE_RIGHT_3 = "stride-3-right";
    public static final String STRIDE_LEFT_3 = "stride-3-left";
    public static final float STRIDE_LOOP_DURATION = 0.1f;

    // Platform
    public static final int PLATFORM_EDGE = 8;
    public static final float MAX_LEDGE_HEIGHT = 5;
    public static final String PLATFORM_SPRITE = "platform";
    public static final String BLUE_PLATFORM_SPRITE = "platform-blue";
    public static final String YELLOW_PLATFORM_SPRITE = "platform-yellow";
    public static final String BLACK_PLATFORM_SPRITE = "platform-black";
    public static final String RED_PLATFORM_SPRITE = "platform-red";
    public static final String GREY_PLATFORM_SPRITE = "platform-grey";
    public static final String CLEAR_PLATFORM_SPRITE = "platform-clear";
    public static final String MAGENTA_PLATFORM_SPRITE = "platform-magenta";

    // Pillar
    public static final Vector2 PILLAR_CENTER = new Vector2(6, 3);
    public static final String PILLAR_SPRITE = "pillar";

    // Cannon
    public static final Vector2 LATERAL_CANNON_CENTER = new Vector2(10, 6);
    public static final String LATERAL_CANNON_SPRITE = "cannon-lateral";
    public static final Vector2 VERTICAL_CANNON_CENTER = new Vector2(8, 10);
    public static final String VERTICAL_CANNON_SPRITE = "cannon-vertical";
    
    // Zoomba
    public static final Vector2 ZOOMBA_CENTER = new Vector2(14.5f, 25.5f);
    public static final float ZOOMBA_MOVEMENT_SPEED = 20;
    public static final float ZOOMBA_BOB_AMPLITUDE = 20;
    public static final float ZOOMBA_BOB_PERIOD = 2f;
    public static final int ZOOMBA_MAX_HEALTH = 30;
    public static final int ZOOMBA_STANDARD_DAMAGE = 1;
    public static final Vector2 ZOOMBA_KNOCKBACK = new Vector2(75, VERTICAL_KNOCKBACK);
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
    public static final Vector2 SWOOPA_KNOCKBACK = new Vector2(75, VERTICAL_KNOCKBACK);
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

    // Spike
    public static final Vector2 SPIKE_CENTER = new Vector2(4, 8);
    public static final float SPIKE_DURATION = 0.5f;
    public static final int SPIKE_DAMAGE = 5;
    public static final Vector2 SPIKE_KNOCKBACK = new Vector2(50, VERTICAL_KNOCKBACK);
    public static final float SPIKE_COLLISION_WIDTH = 9;
    public static final float SPIKE_COLLISION_HEIGHT = 17;
    public static final String SPIKE_SPRITE_1 = "spike-1";
    public static final String SPIKE_SPRITE_2 = "spike-2";

    // Flame
    public static final Vector2 FLAME_CENTER = new Vector2(8.5f, 17.5f);
    public static final float FLAME_DURATION = 0.15f;
    public static final int FLAME_DAMAGE = 15;
    public static final Vector2 FLAME_KNOCKBACK = new Vector2(100, VERTICAL_KNOCKBACK);
    public static final float FLAME_COLLISION_WIDTH = 15;
    public static final float FLAME_COLLISION_HEIGHT = 25;
    public static final String FLAME_SPRITE_1 = "flame-1";
    public static final String FLAME_SPRITE_2 = "flame-2";

    // Geiser
    public static final Vector2 GEISER_CENTER = new Vector2(8.5f, 17.5f);
    public static final float GEISER_DURATION = 0.3f;
    public static final int GEISER_DAMAGE = 10;
    public static final Vector2 GEISER_KNOCKBACK = new Vector2(25, VERTICAL_KNOCKBACK);
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
    public static final float CHARGE_DURATION = 0.8f;
    public static final float AMMO_MOVE_SPEED = 500;
    public static final int AMMO_STANDARD_DAMAGE = 10;
    public static final int AMMO_SPECIALIZED_DAMAGE = 30;
    public static final Vector2 SHOT_CENTER = new Vector2(3, 2);
    public static final Vector2 BLAST_CENTER = new Vector2(8, 4);
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
    public static final String[] LEVELS =  {"levels/METAL.dt", "levels/RUBBER.dt", "levels/ELECTRIC.dt", "levels/WATER.dt", "levels/FIRE.dt", "levels/PSYCHIC.dt", "levels/FINAL.dt"};

    // HUD
    public static final float HUD_VIEWPORT_SIZE = 480;
    public static final float HUD_MARGIN = 20;
    public static final String HUD_AMMO_LABEL = "Ammo: ";
    public static final String HUD_SCORE_LABEL = "Score: ";
    public static final String HUD_HEALTH_LABEL = "Health: ";
    public static final String HUD_WEAPON_LABEL = "WeaponType: ";
    public static final String SHOOT_ICON = "icon-shoot";
    public static final String BLAST_ICON = "icon-blast";
    public static final String JUMP_ICON = "icon-jump";
    public static final String HOVER_ICON = "icon-hover";
    public static final String RICOCHET_ICON = "icon-ricochet";
    public static final String DASH_ICON = "icon-dash";

    // Onscreen Controls
    public static final float ONSCREEN_CONTROLS_VIEWPORT_SIZE = 200;
    public static final Vector2 BUTTON_CENTER = new Vector2(15, 15);
    public static final float BUTTON_RADIUS = 32;
    public static final String LEFT_BUTTON = "button-left";
    public static final String RIGHT_BUTTON = "button-right";
    public static final String UP_BUTTON = "button-up";
    public static final String DOWN_BUTTON = "button-down";
    public static final String CENTER_BUTTON = "button-center";
    public static final String SHOOT_BUTTON = "button-a";
    public static final String BLAST_BUTTON = "button-a";
    public static final String JUMP_BUTTON = "button-b";
    public static final String HOVER_BUTTON = "button-b";
    public static final String RICOCHET_BUTTON = "button-b";
    public static final String PAUSE_BUTTON = "button-pause";
    public static final String SELECTION_CURSOR = "selection-cursor";

    // Victory/Game Over screens
    public static final float LEVEL_END_DURATION = 2;
    public static final int EXPLOSION_COUNT = 500;
  //  public static final int ZOOMBA_COUNT = 200;
    public static final String VICTORY_MESSAGE = "Boo Ya.";
    public static final String GAME_OVER_MESSAGE = "Game Over, Gal";
    public static final String FONT_FILE = "font/header.fnt";

    // Scoring
    public static final int ZOOMBA_KILL_SCORE = 100;
    public static final int ZOOMBA_HIT_SCORE = 25;
    public static final int SWOOPA_KILL_SCORE = 100;
    public static final int SWOOPA_HIT_SCORE = 25;
    public static final int POWERUP_SCORE = 50;
}
