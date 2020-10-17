package com.github.rjbx.energen.util;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

// immutable non-instantiable static
public final class Constants {

    // cannot be subclassed
    private Constants() {}

    // World Camera
    public static final Color BACKGROUND_COLOR = Color.SLATE;
    public static final float WORLD_SIZE = 160;
    public static final float FALL_LIMIT = 6000;
    public static final float GRAVITY = 10;
    public static final float MAX_WEIGHT = 1.2f;
    public static final float DRAG = 1;
    public static final float CHASE_CAM_MOVE_SPEED = WORLD_SIZE;

    // Asset files
    public static final AssetDescriptor<TextureAtlas> TEXTURE_ATLAS = new AssetDescriptor<TextureAtlas>("images/energen.pack.atlas", TextureAtlas.class);
    public static final AssetDescriptor<Sound> HEALTH_SOUND = new AssetDescriptor<Sound>("audio/sound/health.mp3", Sound.class);
    public static final AssetDescriptor<Sound> AMMO_SOUND = new AssetDescriptor<Sound>("audio/sound/ammo.mp3", Sound.class);
    public static final AssetDescriptor<Sound> TURBO_SOUND = new AssetDescriptor<Sound>("audio/sound/turbo.mp3", Sound.class);
    public static final AssetDescriptor<Sound> CANNON_SOUND = new AssetDescriptor<Sound>("audio/sound/cannon.mp3", Sound.class);
    public static final AssetDescriptor<Sound> LIFE_SOUND = new AssetDescriptor<Sound>("audio/sound/life.mp3", Sound.class);
    public static final AssetDescriptor<Sound> UPGRADE_SOUND = new AssetDescriptor<Sound>("audio/sound/upgrade.mp3", Sound.class);
    public static final AssetDescriptor<Sound> BLAST_NATIVE_SOUND = new AssetDescriptor<Sound>("audio/sound/blast-native.mp3", Sound.class);
    public static final AssetDescriptor<Sound> BLAST_PLASMA_SOUND = new AssetDescriptor<Sound>("audio/sound/blast-plasma.mp3", Sound.class);
    public static final AssetDescriptor<Sound> BLAST_GAS_SOUND = new AssetDescriptor<Sound>("audio/sound/blast-gas.mp3", Sound.class);
    public static final AssetDescriptor<Sound> BLAST_LIQUID_SOUND = new AssetDescriptor<Sound>("audio/sound/blast-liquid.mp3", Sound.class);
    public static final AssetDescriptor<Sound> BLAST_ORE_SOUND = new AssetDescriptor<Sound>("audio/sound/blast-ore.mp3", Sound.class);
    public static final AssetDescriptor<Sound> BLAST_SOLID_SOUND = new AssetDescriptor<Sound>("audio/sound/blast-solid.mp3", Sound.class);
    public static final AssetDescriptor<Sound> BLAST_ANTIMATTER_SOUND = new AssetDescriptor<Sound>("audio/sound/blast-antimatter.mp3", Sound.class);
    public static final AssetDescriptor<Sound> BLAST_HYBRID_SOUND = new AssetDescriptor<Sound>("audio/sound/blast-hybrid.mp3", Sound.class);
    public static final AssetDescriptor<Sound> SHOT_NATIVE_SOUND = new AssetDescriptor<Sound>("audio/sound/shot-native.mp3", Sound.class);
    public static final AssetDescriptor<Sound> SHOT_PLASMA_SOUND = new AssetDescriptor<Sound>("audio/sound/shot-plasma.mp3", Sound.class);
    public static final AssetDescriptor<Sound> SHOT_GAS_SOUND = new AssetDescriptor<Sound>("audio/sound/shot-gas.mp3", Sound.class);
    public static final AssetDescriptor<Sound> SHOT_LIQUID_SOUND = new AssetDescriptor<Sound>("audio/sound/shot-liquid.mp3", Sound.class);
    public static final AssetDescriptor<Sound> SHOT_ORE_SOUND = new AssetDescriptor<Sound>("audio/sound/shot-ore.mp3", Sound.class);
    public static final AssetDescriptor<Sound> SHOT_SOLID_SOUND = new AssetDescriptor<Sound>("audio/sound/shot-solid.mp3", Sound.class);
    public static final AssetDescriptor<Sound> SHOT_ANTIMATTER_SOUND = new AssetDescriptor<Sound>("audio/sound/shot-antimatter.mp3", Sound.class);
    public static final AssetDescriptor<Sound> SHOT_HYBRID_SOUND = new AssetDescriptor<Sound>("audio/sound/shot-hybrid.mp3", Sound.class);
    public static final AssetDescriptor<Sound> WARP_SOUND = new AssetDescriptor<Sound>("audio/sound/warp.mp3", Sound.class);
    public static final AssetDescriptor<Sound> HIT_SOUND = new AssetDescriptor<Sound>("audio/sound/hit_effective.mp3", Sound.class);
    public static final AssetDescriptor<Sound> HIT_GROUND_SOUND = new AssetDescriptor<Sound>("audio/sound/hit_ground.mp3", Sound.class);
    public static final AssetDescriptor<Sound> FLIGHT_SOUND = new AssetDescriptor<Sound>("audio/sound/flight.mp3", Sound.class);
    public static final AssetDescriptor<Sound> BREAK_GROUND_SOUND = new AssetDescriptor<Sound>("audio/sound/break_ground.mp3", Sound.class);
    public static final AssetDescriptor<Sound> DAMAGE_SOUND = new AssetDescriptor<Sound>("audio/sound/damage.mp3", Sound.class);
    public static final AssetDescriptor<BitmapFont> MESSAGE_FONT = new AssetDescriptor<BitmapFont>("font/message.fnt", BitmapFont.class);
    public static final AssetDescriptor<BitmapFont> MENU_FONT = new AssetDescriptor<BitmapFont>("font/menu.fnt", BitmapFont.class);
    public static final AssetDescriptor<BitmapFont> INACTIVE_FONT = new AssetDescriptor<BitmapFont>("font/inactive.fnt", BitmapFont.class);
    public static final AssetDescriptor<BitmapFont> TITLE_FONT = new AssetDescriptor<BitmapFont>("font/title.fnt", BitmapFont.class);

    public static final AssetDescriptor<Music> SKY_MUSIC = new AssetDescriptor<Music>("audio/music/ambient-sky.mp3", Music.class);
    public static final AssetDescriptor<Music> PUZZLE_MUSIC = new AssetDescriptor<Music>("audio/music/ambient-puzzle.mp3", Music.class);
    public static final AssetDescriptor<Music> TUNNEL_MUSIC = new AssetDescriptor<Music>("audio/music/ambient-tunnel.mp3", Music.class);
    public static final AssetDescriptor<Music> STROLL_MUSIC = new AssetDescriptor<Music>("audio/music/ambient-stroll.mp3", Music.class);
    public static final AssetDescriptor<Music> TRIAL_MUSIC = new AssetDescriptor<Music>("audio/music/ambient-trial.mp3", Music.class);
    public static final AssetDescriptor<Music> OUTSET_MUSIC = new AssetDescriptor<Music>("audio/music/guitar-outset.mp3", Music.class);
    public static final AssetDescriptor<Music> OFFENCE_MUSIC = new AssetDescriptor<Music>("audio/music/guitar-offence.mp3", Music.class);
    public static final AssetDescriptor<Music> SELECTION_MUSIC = new AssetDescriptor<Music>("audio/music/guitar-selection.mp3", Music.class);
    public static final AssetDescriptor<Music> ENCOUNTER_MUSIC = new AssetDescriptor<Music>("audio/music/guitar-encounter.mp3", Music.class);
    public static final AssetDescriptor<Music> ESCALATION_MUSIC = new AssetDescriptor<Music>("audio/music/guitar-escalation.mp3", Music.class);
    public static final AssetDescriptor<Music> CONTEMPLATION_MUSIC = new AssetDescriptor<Music>("audio/music/guitar-contemplation.mp3", Music.class);
    public static final AssetDescriptor<Music> RECKONING_MUSIC = new AssetDescriptor<Music>("audio/music/guitar-reckoning.mp3", Music.class);
    public static final AssetDescriptor<Music> REELING_MUSIC = new AssetDescriptor<Music>("audio/music/guitar-reeling.mp3", Music.class);
    public static final AssetDescriptor<Music> CONTINUATION_MUSIC = new AssetDescriptor<Music>("audio/music/guitar-continuation.mp3", Music.class);
    public static final AssetDescriptor<Music> SENDOFF_MUSIC = new AssetDescriptor<Music>("audio/music/guitar-sendoff.mp3", Music.class);
    public static final AssetDescriptor<Music> COMBUSTION_MUSIC = new AssetDescriptor<Music>("audio/music/guitar-combustion.mp3", Music.class);
    public static final AssetDescriptor<Music> VILLAGE_MUSIC = new AssetDescriptor<Music>("audio/music/guitar-village.mp3", Music.class);
    public static final AssetDescriptor<Music> RESTART_MUSIC = new AssetDescriptor<Music>("audio/music/guitar-restart.mp3", Music.class);
    public static final AssetDescriptor<Music> UPSTART_MUSIC = new AssetDescriptor<Music>("audio/music/guitar-upstart.mp3", Music.class);
    public static final AssetDescriptor<Music> BUZZARD_MUSIC= new AssetDescriptor<Music>("audio/music/chip-buzzard.mp3", Music.class);
    public static final AssetDescriptor<Music> CHIRPY_MUSIC = new AssetDescriptor<Music>("audio/music/chip-chirpy.mp3", Music.class);
    // Avatar attributes
    public static final Vector2 AVATAR_EYE_POSITION = new Vector2(12, 24);
    public static final float AVATAR_EYE_HEIGHT = 16.0f;
    public static final float AVATAR_STANCE_WIDTH = 20.25f;
    public static final Vector2 AVATAR_X_CANNON_OFFSET = new Vector2(17.5f, -5.75f);
    public static final Vector2 AVATAR_Y_CANNON_OFFSET = new Vector2(6.625f, 9.5f);
    public static final float AVATAR_WEIGHT = 2;
    public static final float AVATAR_HEIGHT = 21.0f;
    public static final float AVATAR_HEAD_RADIUS = 5;
    public static final float AVATAR_STARTING_SPEED = 0.3f;
    public static final float AVATAR_MAX_SPEED = 200;
    public static final float CLIMB_SPEED = 80;
    public static final float JUMP_SPEED = 200;
    public static final float MAX_JUMP_DURATION = 0.025f;
    public static final float Y_KNOCKBACK = 150;
    public static final float RAPPEL_GRAVITY_OFFSET = 5;
    public static final float MIN_GROUND_DISTANCE = 20;
    public static final float STRIDING_JUMP_MULTIPLIER = 1.1f;
    public static final float MAX_LOOK_DISTANCE = 60;
    public static final float RECOVERY_TIME = .25f;
    public static final float DOUBLE_TAP_SPEED = 0.2f;
    public static final float DASH_TURBO_DECREMENT = 0.75f;
    public static final float RAPPEL_TURBO_DECREMENT = 0.5f;
    public static final float HOVER_TURBO_DECREMENT = 1;
    public static final float LEAP_TURBO_DECREMENT = 1.25f;
    public static final float STRIDE_TURBO_INCREMENT = 0.375f;
    public static final float CLIMB_TURBO_INCREMENT = 0.25f;
    public static final float FALL_TURBO_INCREMENT = 0.875f;
    public static final float STAND_TURBO_INCREMENT = 1.125f;
    public static final int BLAST_AMMO_CONSUMPTION = 3;
    public static final int SHOT_AMMO_CONSUMPTION = 1;
    public static final int INITIAL_AMMO = 30;
    public static final int INITIAL_HEALTH = 100;
    public static final int INITIAL_LIVES = 3;
    public static final int MAX_AMMO = 100;
    public static final int MAX_HEALTH = 100;
    public static final float MAX_TURBO = 100;
    public static final int MAX_LIVES = 4;

    // Avatar assets
    public static final String TORSO_0 = "avatar-torso-0";
    public static final String TORSO_CLIMB_0 = "avatar-torso-climb-0";
    public static final String HEAD = "avatar-head";
    public static final String HEAD_CLIMB = "avatar-head-climb";
    public static final String HAIR_CLIMB = "avatar-hair-climb";
    public static final String HAIR_1 = "avatar-hair-1";
    public static final String HAIR_2 = "avatar-hair-2";
    public static final String HAIR_3 = "avatar-hair-3";
    public static final String WAIST_1 = "avatar-waist-1";
    public static final String WAIST_2 = "avatar-waist-2";
    public static final String WAIST_3 = "avatar-waist-3";
    public static final String WAIST_4 = "avatar-waist-4";
    public static final String MOUTH_OPEN = "avatar-mouth-open";
    public static final String MOUTH_CLOSED = "avatar-mouth-closed";
    public static final String ARM_REACH = "avatar-arm-reach";
    public static final String ARM_RELEASE = "avatar-arm-release";
    public static final String ARM_RELAX = "avatar-arm-relax";
    public static final String ARM_CLIMB = "avatar-arm-climb";
    public static final String ARM_CLENCH = "avatar-arm-clench";
    public static final String ARM_POINT = "avatar-arm-point";
    public static final String ARM_RAISE = "avatar-arm-raise";
    public static final String ARM_RAISE_0 = "avatar-arm-raise-0";
    public static final String ARM_LOWER = "avatar-arm-lower";
    public static final String ARM_LOWER_0 = "avatar-arm-lower-0";
    public static final String HAND_REACH = "avatar-hand-reach";
    public static final String HAND_RELEASE = "avatar-hand-release";
    public static final String HAND_RELAX = "avatar-hand-relax";
    public static final String HAND_CLENCH = "avatar-hand-clench";
    public static final String HAND_CURL = "avatar-hand-curl";
    public static final String HAND_OUTWARD = "avatar-hand-outward";
    public static final String HAND_SHOOT = "avatar-hand-shoot";
    public static final String HAND_RAISE = "avatar-hand-raise";
    public static final String HAND_RAISE_0 = "avatar-hand-raise-0";
    public static final String HAND_RAISE_1 = "avatar-hand-raise-1";
    public static final String HAND_RAISE_2 = "avatar-hand-raise-2";
    public static final String HAND_LOWER = "avatar-hand-lower";
    public static final String HAND_LOWER_0 = "avatar-hand-lower-0";
    public static final String HAND_LOWER_1 = "avatar-hand-lower-1";
    public static final String HAND_LOWER_2 = "avatar-hand-lower-2";
    public static final String HAND_BLAST_1 = "avatar-hand-blast-1";
    public static final String HAND_BLAST_2 = "avatar-hand-blast-2";
    public static final String HAND_RAPPEL_1 = "avatar-hand-rappel-1";
    public static final String HAND_RAPPEL_2 = "avatar-hand-rappel-2";
    public static final String HAND_MOVE_1 = "avatar-hand-move-1";
    public static final String HAND_MOVE_2 = "avatar-hand-move-2";
    public static final String FEET_STAND = "avatar-feet-stand";
    public static final String FEET_FALL = "avatar-feet-fall";
    public static final String FEET_RAPPEL = "avatar-feet-rappel";
    public static final String FEET_RECOIL = "avatar-feet-recoil";
    public static final String FEET_CLIMB = "avatar-feet-climb";
    public static final String FEET_DASH_1 = "avatar-feet-dash-1";
    public static final String FEET_DASH_2 = "avatar-feet-dash-2";
    public static final String FEET_STRIDE_1 = "avatar-feet-stride-1";
    public static final String FEET_STRIDE_2 = "avatar-feet-stride-2";
    public static final String FEET_STRIDE_3 = "avatar-feet-stride-3";
    public static final String FEET_HOVER_1 = "avatar-feet-hover-1";
    public static final String FEET_HOVER_2 = "avatar-feet-hover-2";
    public static final String LEGS_STAND = "avatar-legs-stand";
    public static final String LEGS_FALL = "avatar-legs-fall";
    public static final String LEGS_RAPPEL = "avatar-legs-rappel";
    public static final String LEGS_RECOIL = "avatar-legs-recoil";
    public static final String LEGS_CLIMB = "avatar-legs-climb";
    public static final String LEGS_STRIDE_1 = "avatar-legs-stride-1";
    public static final String LEGS_STRIDE_2 = "avatar-legs-stride-2";
    public static final String LEGS_STRIDE_3 = "avatar-legs-stride-3";
    public static final String LEGS_STRIDE_4 = "avatar-legs-stride-4";
    public static final String LEGS_STRIDE_5 = "avatar-legs-stride-5";
    public static final String EYES_BLINK = "avatar-eyes-blink";
    public static final String EYES_OPEN_1 = "avatar-eyes-open-1";
    public static final String EYES_OPEN_2 = "avatar-eyes-open-2";
    public static final String EYES_OPEN_3 = "avatar-eyes-open-3";
    public static final String EYES_OPEN_4 = "avatar-eyes-open-4";
    public static final String OBFUSCATED = "avatar-obfuscated";

    public static final String STAND = "avatar-stand";
    public static final String FALL = "avatar-fall";
    public static final String FLIPSWIPE_1 = "avatar-swipe-flip-1";
    public static final String FLIPSWIPE_2 = "avatar-swipe-flip-2";
    public static final String FLIPSWIPE_3 = "avatar-swipe-flip-3";
    public static final String FLIPSWIPE_4 = "avatar-swipe-flip-4";
    public static final String FLIPSWIPE_5 = "avatar-swipe-flip-5";
    public static final String SIDESWIPE_1 = "avatar-swipe-side-1";
    public static final String SIDESWIPE_2 = "avatar-swipe-side-2";
    public static final String SIDESWIPE_3 = "avatar-swipe-side-3";

    public static final float EYE_ROLL_FRAME_DURATION = 1;
    public static final float FLICKER_FRAME_DURATION = 0.125f;
    public static final float RAPPEL_FRAME_DURATION = 0.05f;
    public static final float STRIDE_FRAME_DURATION = 0.08f;
    public static final float FLIPSWIPE_FRAME_DURATION = 0.1f;
    public static final float SIDESWIPE_FRAME_DURATION = 0.08f;

    // Boss
    public static final String BOSS_LIQUID_BLOCK_LEFT = "boss-liquid-block-left";
    public static final String BOSS_LIQUID_BLOCK_RIGHT = "boss-liquid-block-right";
    public static final String BOSS_LIQUID_LOOKUP_BLOCK_LEFT = "boss-liquid-lookup-block-left";
    public static final String BOSS_LIQUID_LOOKUP_BLOCK_RIGHT = "boss-liquid-lookup-block-right";
    public static final String BOSS_LIQUID_STAND_LEFT = "boss-liquid-stand-left";
    public static final String BOSS_LIQUID_STAND_RIGHT = "boss-liquid-stand-right";
    public static final String BOSS_LIQUID_FALL_LEFT = "boss-liquid-fall-left";
    public static final String BOSS_LIQUID_FALL_RIGHT = "boss-liquid-fall-right";
    public static final String BOSS_LIQUID_DASH_LEFT = "boss-liquid-dash-left";
    public static final String BOSS_LIQUID_DASH_RIGHT = "boss-liquid-dash-right";
    public static final String BOSS_LIQUID_RECOIL_LEFT = "boss-liquid-recoil-left";
    public static final String BOSS_LIQUID_RECOIL_RIGHT = "boss-liquid-recoil-right";
    public static final String BOSS_LIQUID_LOOKUP_STAND_LEFT = "boss-liquid-lookup-stand-left";
    public static final String BOSS_LIQUID_LOOKUP_STAND_RIGHT = "boss-liquid-lookup-stand-right";
    public static final String BOSS_LIQUID_LOOKUP_FALL_LEFT = "boss-liquid-lookup-fall-left";
    public static final String BOSS_LIQUID_LOOKUP_FALL_RIGHT = "boss-liquid-lookup-fall-right";
    public static final String BOSS_LIQUID_LOOKDOWN_FALL_LEFT = "boss-liquid-lookdown-fall-left";
    public static final String BOSS_LIQUID_LOOKDOWN_FALL_RIGHT = "boss-liquid-lookdown-fall-right";

    public static final String BOSS_GAS_BLOCK_LEFT = "boss-gas-block-left";
    public static final String BOSS_GAS_BLOCK_RIGHT = "boss-gas-block-right";
    public static final String BOSS_GAS_LOOKUP_BLOCK_LEFT = "boss-gas-lookup-block-left";
    public static final String BOSS_GAS_LOOKUP_BLOCK_RIGHT = "boss-gas-lookup-block-right";
    public static final String BOSS_GAS_STAND_LEFT = "boss-gas-stand-left";
    public static final String BOSS_GAS_STAND_RIGHT = "boss-gas-stand-right";
    public static final String BOSS_GAS_FALL_LEFT = "boss-gas-fall-left";
    public static final String BOSS_GAS_FALL_RIGHT = "boss-gas-fall-right";
    public static final String BOSS_GAS_DASH_LEFT = "boss-gas-dash-left";
    public static final String BOSS_GAS_DASH_RIGHT = "boss-gas-dash-right";
    public static final String BOSS_GAS_RECOIL_LEFT = "boss-gas-recoil-left";
    public static final String BOSS_GAS_RECOIL_RIGHT = "boss-gas-recoil-right";
    public static final String BOSS_GAS_LOOKUP_STAND_LEFT = "boss-gas-lookup-stand-left";
    public static final String BOSS_GAS_LOOKUP_STAND_RIGHT = "boss-gas-lookup-stand-right";
    public static final String BOSS_GAS_LOOKUP_FALL_LEFT = "boss-gas-lookup-fall-left";
    public static final String BOSS_GAS_LOOKUP_FALL_RIGHT = "boss-gas-lookup-fall-right";
    public static final String BOSS_GAS_LOOKDOWN_FALL_LEFT = "boss-gas-lookdown-fall-left";
    public static final String BOSS_GAS_LOOKDOWN_FALL_RIGHT = "boss-gas-lookdown-fall-right";

    public static final String BOSS_PLASMA_BLOCK_LEFT = "boss-plasma-block-left";
    public static final String BOSS_PLASMA_BLOCK_RIGHT = "boss-plasma-block-right";
    public static final String BOSS_PLASMA_LOOKUP_BLOCK_LEFT = "boss-plasma-lookup-block-left";
    public static final String BOSS_PLASMA_LOOKUP_BLOCK_RIGHT = "boss-plasma-lookup-block-right";
    public static final String BOSS_PLASMA_STAND_LEFT = "boss-plasma-stand-left";
    public static final String BOSS_PLASMA_STAND_RIGHT = "boss-plasma-stand-right";
    public static final String BOSS_PLASMA_FALL_LEFT = "boss-plasma-fall-left";
    public static final String BOSS_PLASMA_FALL_RIGHT = "boss-plasma-fall-right";
    public static final String BOSS_PLASMA_DASH_LEFT = "boss-plasma-dash-left";
    public static final String BOSS_PLASMA_DASH_RIGHT = "boss-plasma-dash-right";
    public static final String BOSS_PLASMA_RECOIL_LEFT = "boss-plasma-recoil-left";
    public static final String BOSS_PLASMA_RECOIL_RIGHT = "boss-plasma-recoil-right";
    public static final String BOSS_PLASMA_LOOKUP_STAND_LEFT = "boss-plasma-lookup-stand-left";
    public static final String BOSS_PLASMA_LOOKUP_STAND_RIGHT = "boss-plasma-lookup-stand-right";
    public static final String BOSS_PLASMA_LOOKUP_FALL_LEFT = "boss-plasma-lookup-fall-left";
    public static final String BOSS_PLASMA_LOOKUP_FALL_RIGHT = "boss-plasma-lookup-fall-right";
    public static final String BOSS_PLASMA_LOOKDOWN_FALL_LEFT = "boss-plasma-lookdown-fall-left";
    public static final String BOSS_PLASMA_LOOKDOWN_FALL_RIGHT = "boss-plasma-lookdown-fall-right";

    public static final String BOSS_ORE_BLOCK_LEFT = "boss-ore-block-left";
    public static final String BOSS_ORE_BLOCK_RIGHT = "boss-ore-block-right";
    public static final String BOSS_ORE_LOOKUP_BLOCK_LEFT = "boss-ore-lookup-block-left";
    public static final String BOSS_ORE_LOOKUP_BLOCK_RIGHT = "boss-ore-lookup-block-right";
    public static final String BOSS_ORE_STAND_LEFT = "boss-ore-stand-left";
    public static final String BOSS_ORE_STAND_RIGHT = "boss-ore-stand-right";
    public static final String BOSS_ORE_FALL_LEFT = "boss-ore-fall-left";
    public static final String BOSS_ORE_FALL_RIGHT = "boss-ore-fall-right";
    public static final String BOSS_ORE_DASH_LEFT = "boss-ore-dash-left";
    public static final String BOSS_ORE_DASH_RIGHT = "boss-ore-dash-right";
    public static final String BOSS_ORE_RECOIL_LEFT = "boss-ore-recoil-left";
    public static final String BOSS_ORE_RECOIL_RIGHT = "boss-ore-recoil-right";
    public static final String BOSS_ORE_LOOKUP_STAND_LEFT = "boss-ore-lookup-stand-left";
    public static final String BOSS_ORE_LOOKUP_STAND_RIGHT = "boss-ore-lookup-stand-right";
    public static final String BOSS_ORE_LOOKUP_FALL_LEFT = "boss-ore-lookup-fall-left";
    public static final String BOSS_ORE_LOOKUP_FALL_RIGHT = "boss-ore-lookup-fall-right";
    public static final String BOSS_ORE_LOOKDOWN_FALL_LEFT = "boss-ore-lookdown-fall-left";
    public static final String BOSS_ORE_LOOKDOWN_FALL_RIGHT = "boss-ore-lookdown-fall-right";

    // Background
    public static final String BACKGROUND_HOME_SPRITE = "background-home";
    public static final String BACKGROUND_ORE_SPRITE = "background-ore";
    public static final String BACKGROUND_PLASMA_SPRITE = "background-plasma";
    public static final String BACKGROUND_GAS_SPRITE = "background-gas-1";
    public static final String BACKGROUND_LIQUID_SPRITE = "background-gas-2";
    public static final String BACKGROUND_SOLID_SPRITE = "background-solid";
    public static final String BACKGROUND_HYBRID_SPRITE = "background-hybrid";
    public static final Vector2 BACKGROUND_CENTER = new Vector2(270, 180);

    // All grounds
    public static final int PRIORITY_OVERRIDE = -1;
    public static final int PRIORITY_MAX = 0;
    public static final int PRIORITY_HIGH = 1;
    public static final int PRIORITY_MEDIUM = 2;
    public static final int PRIORITY_LOW = 3;

    // Box
    public static final int BLOCK_EDGE = 4;
    public static final float MAX_LEDGE_HEIGHT = 8;
    public static final String BARRIER_SPRITE = "box";
    public static final String BOX_SPRITE = "box-breakable";
    public static final String BLOCK_SPRITE = "box-draggable";
    public static final String BOX_DEPOSIT_SPRITE = "box-breakable-deposit";
    public static final Vector2 BOX_DEPOSIT_CENTER = new Vector2(16, 16);

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
    public static final Vector2 LADDER_CENTER = new Vector2(9.5f, 25);
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
    public static final String TREADMILL_SPRITE_1_RIGHT = "treadmill-1-right";
    public static final String TREADMILL_SPRITE_2_RIGHT = "treadmill-2-right";
    public static final String TREADMILL_SPRITE_1_LEFT = "treadmill-1-left";
    public static final String TREADMILL_SPRITE_2_LEFT = "treadmill-2-left";
    public static final float TREADMILL_DURATION = .1f;
    public static final float TREADMILL_SPEED = 175;

    // Tripknob
    public static final Vector2 TRIPKNOB_CENTER = new Vector2(6, 6);
    public static final String TRIPKNOB_SPRITE_1 = "trip-1";
    public static final String TRIPKNOB_SPRITE_2 = "trip-2";
    public static final String TRIPKNOB_SPRITE_3 = "trip-3";
    public static final String TRIPKNOB_SPRITE_4 = "trip-4";
    public static final float TRIPKNOB_LOAD_DURATION = 0.25f;
    public static final float TRIPKNOB_UNLOAD_DURATION = 0.5f;
    public static final float TRIPKNOB_SHOT_RADIUS = 6;

    // Triptread
    public static final Vector2 TRIPTREAD_CENTER = new Vector2(19, 9);
    public static final String TRIPTREAD_SPRITE_1_LEFT_OFF = "treadmill-trip-1-left-off";
    public static final String TRIPTREAD_SPRITE_2_LEFT_OFF = "treadmill-trip-2-left-off";
    public static final String TRIPTREAD_SPRITE_1_LEFT_ON = "treadmill-trip-1-left-on";
    public static final String TRIPTREAD_SPRITE_2_LEFT_ON = "treadmill-trip-2-left-on";
    public static final String TRIPTREAD_SPRITE_1_RIGHT_OFF = "treadmill-trip-1-right-off";
    public static final String TRIPTREAD_SPRITE_2_RIGHT_OFF = "treadmill-trip-2-right-off";
    public static final String TRIPTREAD_SPRITE_1_RIGHT_ON = "treadmill-trip-1-right-on";
    public static final String TRIPTREAD_SPRITE_2_RIGHT_ON = "treadmill-trip-2-right-on";
    public static final float TRIPTREAD_DURATION = .1f;
    public static final float TRIPTREAD_SPEED = 175;

    // Tripchamber
    public static final Vector2 TRIPCHAMBER_CENTER = new Vector2(4.5f, 6);
    public static final String TRIPCHAMBER_SPRITE_1_OFF = "chamber-trip-1-off";
    public static final String TRIPCHAMBER_SPRITE_1_ON = "chamber-trip-1-on";
    public static final String TRIPCHAMBER_SPRITE_2_OFF = "chamber-trip-2-off";
    public static final String TRIPCHAMBER_SPRITE_2_ON = "chamber-trip-2-on";
    public static final float TRIPCHAMBER_LOAD_DURATION = 0.1f;

    // Tripspring
    public static final Vector2 LEVER_CENTER = new Vector2(14.5f, 3);
    public static final String LEVER_SPRITE_1 = "lever-1";
    public static final String LEVER_SPRITE_2 = "lever-2";
    public static final String LEVER_SPRITE_3 = "lever-3";
    public static final String LEVER_SPRITE_4 = "lever-4";
    public static final float LEVER_LOAD_DURATION = 0.2f;
    public static final float LEVER_UNLOAD_DURATION = 0.5f;
    public static final float LEVER_JUMP_MULTIPLIER = 1.2f;

    // Spring
    public static final Vector2 SPRING_CENTER = new Vector2(11.5f, 4);
    public static final String SPRING_SPRITE_1 = "spring-1";
    public static final String SPRING_SPRITE_2 = "spring-2";
    public static final String SPRING_SPRITE_3 = "spring-3";
    public static final String SPRING_SPRITE_4 = "spring-4";
    public static final float SPRING_LOAD_DURATION = 0.1f;
    public static final float SPRING_UNLOAD_DURATION = 0.5f;
    public static final float SPRING_JUMP_MULTIPLIER = 2;

    // Sand
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

    // Waves
    public static final Vector2 WAVES_CENTER = new Vector2(16, 16);
    public static final String WAVES_SPRITE_1 = "waves-1";
    public static final String WAVES_SPRITE_2 = "waves-2";
    public static final float WAVES_DURATION = .25f;
    public static final int WAVES_DAMAGE = 5;
    public static final Vector2 WAVES_KNOCKBACK = new Vector2(15, 40);

    // Pod
    public static final Vector2 POD_CENTER = new Vector2(17.5f, 4);
    public static final String POD_SPRITE_1 = "pod-1";
    public static final String POD_SPRITE_2 = "pod-2";
    public static final String POD_SPRITE_3 = "pod-3";
    public static final float POD_LOAD_DURATION = 0.2f;
    public static final float POD_JUMP_MULTIPLIER = 1.6f;

    // Chamber
    public static final Vector2 CHAMBER_CENTER = new Vector2(4.5f, 10);
    public static final String CHAMBER_SPRITE = "chamber";
    public static final String CHAMBER_SPRITE_1 = "chamber-1";
    public static final String CHAMBER_SPRITE_2 = "chamber-2";
    public static final String CHAMBER_SPRITE_3 = "chamber-3";
    public static final float CHAMBER_LOAD_DURATION = 0.1f;

    // Cannon
    public static final Vector2 X_CANNON_CENTER = new Vector2(10, 6);
    public static final String X_CANNON_SPRITE = "cannon-x";
    public static final Vector2 Y_CANNON_CENTER = new Vector2(8, 10);
    public static final String Y_CANNON_SPRITE = "cannon-y";
    public static final float CANNON_DISPATCH_RATE = 1.5f;

    // Canirol
    public static final Vector2 X_CANIROL_CENTER = new Vector2(10, 7.5f);
    public static final String X_CANIROL_SPRITE_1 = "canirol-x-1";
    public static final String X_CANIROL_SPRITE_2 = "canirol-x-2";
    public static final String X_CANIROL_SPRITE_3 = "canirol-x-3";
    public static final Vector2 Y_CANIROL_CENTER = new Vector2(8, 10);
    public static final String Y_CANIROL_SPRITE_1 = "canirol-y-1";
    public static final String Y_CANIROL_SPRITE_2 = "canirol-y-2";
    public static final String Y_CANIROL_SPRITE_3 = "canirol-y-3";
    public static final float CANIROL_FRAME_DURATION = 0.1f;

    // Zoomba
    public static final Vector2 ZOOMBA_CENTER = new Vector2(28.5f, 26.5f);
    public static final float ZOOMBA_MOVEMENT_SPEED = 20;
    public static final float ZOOMBA_BOB_AMPLITUDE = 20;
    public static final float ZOOMBA_BOB_PERIOD = 2f;
    public static final int ZOOMBA_MAX_HEALTH = 300;
    public static final int ZOOMBA_STANDARD_DAMAGE = 1;
    public static final Vector2 ZOOMBA_KNOCKBACK = new Vector2(75, Y_KNOCKBACK);
    public static final float ZOOMBA_COLLISION_WIDTH = 29;
    public static final float ZOOMBA_COLLISION_HEIGHT = 25;
    public static final float ZOOMBA_SHOT_RADIUS = 14.5f;
    public static final float ZOOMBA_RANGE = 50;
    public static final String ZOOMBA_SPRITE = "zoomba";

    public static final String FIERYZOOMBA_SPRITE_1_LEFT = "zoomba-gas-1-left";
    public static final String FIERYZOOMBA_SPRITE_2_LEFT = "zoomba-gas-2-left";

    public static final String FIERYZOOMBA_SPRITE_1_RIGHT = "zoomba-gas-1-right";
    public static final String FIERYZOOMBA_SPRITE_2_RIGHT = "zoomba-gas-2-right";

    public static final String FIERYZOOMBA_SPRITE_1_UP = "zoomba-gas-1-up";
    public static final String FIERYZOOMBA_SPRITE_2_UP = "zoomba-gas-2-up";

    public static final String FIERYZOOMBA_SPRITE_1_DOWN = "zoomba-gas-1-down";
    public static final String FIERYZOOMBA_SPRITE_2_DOWN = "zoomba-gas-2-down";

    public static final String GUSHINGZOOMBA_SPRITE_1 = "zoomba-liquid-1";
    public static final String GUSHINGZOOMBA_SPRITE_2 = "zoomba-liquid-2";

    public static final String CHARGEDZOOMBA_SPRITE_1 = "zoomba-plasma-1";
    public static final String CHARGEDZOOMBA_SPRITE_2 = "zoomba-plasma-2";

    public static final String OREZOOMBA_SPRITE_1 = "zoomba-polymer-1";
    public static final String OREZOOMBA_SPRITE_2 = "zoomba-polymer-2";

    public static final String SOLIDZOOMBA_SPRITE_1 = "zoomba-solid-1";
    public static final String SOLIDZOOMBA_SPRITE_2 = "zoomba-solid-2";

    // Swoopa
    public static final Vector2 SWOOPA_CENTER = new Vector2(25f, 13.5f);
    public static final float SWOOPA_MOVEMENT_SPEED = 150;
    public static final int SWOOPA_MAX_HEALTH = 300;
    public static final int SWOOPA_STANDARD_DAMAGE = 1;
    public static final Vector2 SWOOPA_KNOCKBACK = new Vector2(75, Y_KNOCKBACK);
    public static final float SWOOPA_COLLISION_WIDTH = 50;
    public static final float SWOOPA_COLLISION_HEIGHT = 27;
    public static final float SWOOPA_SHOT_RADIUS = 13.5f;
    public static final String SWOOPA_SPRITE_LEFT = "swoopa-left";
    public static final String SWOOPA_SPRITE_RIGHT = "swoopa-right";

    public static final String FIERYSWOOPA_SPRITE_1_LEFT = "swoopa-gas-1-left";
    public static final String FIERYSWOOPA_SPRITE_2_LEFT = "swoopa-gas-2-left";

    public static final String FIERYSWOOPA_SPRITE_1_RIGHT = "swoopa-gas-1-right";
    public static final String FIERYSWOOPA_SPRITE_2_RIGHT = "swoopa-gas-2-right";

    public static final String GUSHINGSWOOPA_SPRITE_1 = "swoopa-liquid-1";
    public static final String GUSHINGSWOOPA_SPRITE_2 = "swoopa-liquid-2";

    public static final String CHARGEDSWOOPA_SPRITE_1 = "swoopa-plasma-1";
    public static final String CHARGEDSWOOPA_SPRITE_2 = "swoopa-plasma-2";

    public static final String ORESWOOPA_SPRITE_1 = "swoopa-polymer-1";
    public static final String ORESWOOPA_SPRITE_2 = "swoopa-polymer-2";

    public static final String SOLIDSWOOPA_SPRITE_1 = "swoopa-solid-1";
    public static final String SOLIDSWOOPA_SPRITE_2 = "swoopa-solid-2";

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
    public static final String ORBEN_SPRITE = "orben-dormant";

    public static final String ORBEN_GAS_SPRITE_0 = "orben-gas-active-0";
    public static final String ORBEN_GAS_SPRITE_1 = "orben-gas-active-1";
    public static final String ORBEN_GAS_SPRITE_2 = "orben-gas-active-2";

    public static final String ORBEN_LIQUID_SPRITE_0 = "orben-liquid-active-0";
    public static final String ORBEN_LIQUID_SPRITE_1 = "orben-liquid-active-1";
    public static final String ORBEN_LIQUID_SPRITE_2 = "orben-liquid-active-2";

    public static final String ORBEN_PLASMA_SPRITE_0 = "orben-plasma-active-0";
    public static final String ORBEN_PLASMA_SPRITE_1 = "orben-plasma-active-1";
    public static final String ORBEN_PLASMA_SPRITE_2 = "orben-plasma-active-2";

    public static final String ORBEN_ORE_SPRITE_0 = "orben-polymer-active-0";
    public static final String ORBEN_ORE_SPRITE_1 = "orben-polymer-active-1";
    public static final String ORBEN_ORE_SPRITE_2 = "orben-polymer-active-2";

    public static final String ORBEN_SOLID_SPRITE_0 = "orben-solid-active-0";
    public static final String ORBEN_SOLID_SPRITE_1 = "orben-solid-active-1";
    public static final String ORBEN_SOLID_SPRITE_2 = "orben-solid-active-2";

    // Rollen
    public static final float ROLLEN_TEXTURE_SCALE = 1.5f;
    public static final Vector2 ROLLEN_CENTER = new Vector2(12.5f * ROLLEN_TEXTURE_SCALE, 12.5f * ROLLEN_TEXTURE_SCALE);
    public static final float ROLLEN_SHOT_RADIUS = 4 * ROLLEN_TEXTURE_SCALE;
    public static final float ROLLEN_MOVEMENT_SPEED = 75;
    public static final int ROLLEN_MAX_HEALTH = 60;
    public static final int ROLLEN_STANDARD_DAMAGE = 1;
    public static final Vector2 ROLLEN_KNOCKBACK = new Vector2(75, Y_KNOCKBACK);
    public static final float ROLLEN_DURATION = .5f;
    public static final int ROLLEN_REGIONS = 4;

    public static final String ROLLEN_GAS_SPRITE_4 = "rollen-gas-4";
    public static final String ROLLEN_GAS_SPRITE_1 = "rollen-gas-1";
    public static final String ROLLEN_GAS_SPRITE_2 = "rollen-gas-2";
    public static final String ROLLEN_GAS_SPRITE_3 = "rollen-gas-3";

    public static final String ROLLEN_LIQUID_SPRITE_4 = "rollen-liquid-4";
    public static final String ROLLEN_LIQUID_SPRITE_1 = "rollen-liquid-1";
    public static final String ROLLEN_LIQUID_SPRITE_2 = "rollen-liquid-2";
    public static final String ROLLEN_LIQUID_SPRITE_3 = "rollen-liquid-3";

    public static final String ROLLEN_PLASMA_SPRITE_4 = "rollen-plasma-4";
    public static final String ROLLEN_PLASMA_SPRITE_1 = "rollen-plasma-1";
    public static final String ROLLEN_PLASMA_SPRITE_2 = "rollen-plasma-2";
    public static final String ROLLEN_PLASMA_SPRITE_3 = "rollen-plasma-3";

    public static final String ROLLEN_ORE_SPRITE_4 = "rollen-polymer-4";
    public static final String ROLLEN_ORE_SPRITE_1 = "rollen-polymer-1";
    public static final String ROLLEN_ORE_SPRITE_2 = "rollen-polymer-2";
    public static final String ROLLEN_ORE_SPRITE_3 = "rollen-polymer-3";

    public static final String ROLLEN_SOLID_SPRITE_4 = "rollen-solid-4";
    public static final String ROLLEN_SOLID_SPRITE_1 = "rollen-solid-1";
    public static final String ROLLEN_SOLID_SPRITE_2 = "rollen-solid-2";
    public static final String ROLLEN_SOLID_SPRITE_3 = "rollen-solid-3";

    // Armorollen
    public static final String ARMOROLL_LIQUID_SPRITE_0 = "armorollo-liquid-0";
    public static final String ARMOROLL_LIQUID_SPRITE_1 = "armorollo-liquid-1";
    public static final String ARMOROLLO_LIQUID_SPRITE_2 = "armorollo-liquid-2";
    public static final String ARMOROLLO_LIQUID_SPRITE_3 = "armorollo-liquid-3";
    public static final String ARMOROLLO_LIQUID_SPRITE_4 = "armorollo-liquid-4";

    // Protrusions
    public static final Vector2 PROTRUSION_SOLID_CENTER = new Vector2(4, 8);
    public static final float PROTRUSION_SOLID_DURATION = 0.5f;
    public static final int PROTRUSION_SOLID_DAMAGE = 5;
    public static final Vector2 PROTRUSION_SOLID_KNOCKBACK = new Vector2(50, Y_KNOCKBACK);
    public static final float PROTRUSION_SOLID_COLLISION_WIDTH = 9;
    public static final float PROTRUSION_SOLID_COLLISION_HEIGHT = 17;
    public static final String PROTRUSION_SOLID_SPRITE_1 = "protrusion-solid-1";
    public static final String PROTRUSION_SOLID_SPRITE_2 = "protrusion-solid-2";

    public static final Vector2 PROTRUSION_GAS_CENTER = new Vector2(8.5f, 17.5f);
    public static final float PROTRUSION_GAS_DURATION = 0.15f;
    public static final int PROTRUSION_GAS_DAMAGE = 15;
    public static final Vector2 PROTRUSION_GAS_KNOCKBACK = new Vector2(100, Y_KNOCKBACK);
    public static final float PROTRUSION_GAS_COLLISION_WIDTH = 15;
    public static final float PROTRUSION_GAS_COLLISION_HEIGHT = 25;
    public static final String PROTRUSION_GAS_SPRITE_1 = "protrusion-gas-1";
    public static final String PROTRUSION_GAS_SPRITE_2 = "protrusion-gas-2";

    public static final Vector2 PROTRUSION_LIQUID_CENTER = new Vector2(8.5f, 18.5f);
    public static final float PROTRUSION_LIQUID_DURATION = 0.3f;
    public static final int PROTRUSION_LIQUID_DAMAGE = 10;
    public static final Vector2 PROTRUSION_LIQUID_KNOCKBACK = new Vector2(25, Y_KNOCKBACK);
    public static final float PROTRUSION_LIQUID_COLLISION_WIDTH = 15;
    public static final float PROTRUSION_LIQUID_COLLISION_HEIGHT = 25;
    public static final String PROTRUSION_LIQUID_SPRITE_1 = "protrusion-liquid-1";
    public static final String PROTRUSION_LIQUID_SPRITE_2 = "protrusion-liquid-2";

    public static final Vector2 PROTRUSION_PLASMA_CENTER = new Vector2(4, 8);
    public static final float PROTRUSION_PLASMA_DURATION = 0.3f;
    public static final int PROTRUSION_PLASMA_DAMAGE = 10;
    public static final Vector2 PROTRUSION_PLASMA_KNOCKBACK = new Vector2(25, Y_KNOCKBACK);
    public static final float PROTRUSION_PLASMA_COLLISION_WIDTH = 8;
    public static final float PROTRUSION_PLASMA_COLLISION_HEIGHT = 16;
    public static final String PROTRUSION_PLASMA_SPRITE_1 = "protrusion-plasma-1";
    public static final String PROTRUSION_PLASMA_SPRITE_2 = "protrusion-plasma-2";

    public static final Vector2 PROTRUSION_ORE_CENTER = new Vector2(8.5f, 17.5f);
    public static final float PROTRUSION_ORE_DURATION = 0.3f;
    public static final int PROTRUSION_ORE_DAMAGE = 10;
    public static final Vector2 PROTRUSION_ORE_KNOCKBACK = new Vector2(25, Y_KNOCKBACK);
    public static final float PROTRUSION_ORE_COLLISION_WIDTH = 15;
    public static final float PROTRUSION_ORE_COLLISION_HEIGHT = 25;
    public static final String PROTRUSION_ORE_SPRITE_1 = "protrusion-polymer-1";
    public static final String PROTRUSION_ORE_SPRITE_2 = "protrusion-polymer-2";

    public static final Vector2 PROTRUSION_INACTIVE_CENTER = new Vector2(3.5f, 1);
    public static final String PROTRUSION_INACTIVE_SPRITE = "protrusion-inactive";

    // Suspensions
    public static final Vector2 SUSPENSION_GAS_CENTER = new Vector2(12.5f, 12.5f);
    public static final float SUSPENSION_GAS_DURATION = 0.1f;
    public static final int SUSPENSION_GAS_DAMAGE = 5;
    public static final Vector2 SUSPENSION_GAS_KNOCKBACK = new Vector2(200, Y_KNOCKBACK + 100);
    public static final float SUSPENSION_GAS_COLLISION_WIDTH = 25;
    public static final float SUSPENSION_GAS_COLLISION_HEIGHT = 25;
    public static final String SUSPENSION_GAS_SPRITE_1 = "suspension-gas-1";
    public static final String SUSPENSION_GAS_SPRITE_2 = "suspension-gas-2";

    public static final Vector2 SUSPENSION_PLASMA_CENTER = new Vector2(12.5f, 12.5f);
    public static final float SUSPENSION_PLASMA_DURATION = 0.5f;
    public static final int SUSPENSION_PLASMA_DAMAGE = 5;
    public static final Vector2 SUSPENSION_PLASMA_KNOCKBACK = new Vector2(200, Y_KNOCKBACK);
    public static final float SUSPENSION_PLASMA_COLLISION_WIDTH = 25;
    public static final float SUSPENSION_PLASMA_COLLISION_HEIGHT = 25;
    public static final String SUSPENSION_PLASMA_SPRITE_1 = "suspension-plasma-1";
    public static final String SUSPENSION_PLASMA_SPRITE_2 = "suspension-plasma-2";

    public static final Vector2 SUSPENSION_LIQUID_CENTER = new Vector2(12.5f, 12.5f);
    public static final float SUSPENSION_LIQUID_DURATION = 0.5f;
    public static final int SUSPENSION_LIQUID_DAMAGE = 5;
    public static final Vector2 SUSPENSION_LIQUID_KNOCKBACK = new Vector2(200, Y_KNOCKBACK);
    public static final float SUSPENSION_LIQUID_COLLISION_WIDTH = 25;
    public static final float SUSPENSION_LIQUID_COLLISION_HEIGHT = 25;
    public static final String SUSPENSION_LIQUID_SPRITE_1 = "suspension-liquid-1";
    public static final String SUSPENSION_LIQUID_SPRITE_2 = "suspension-liquid-2";

    public static final Vector2 SUSPENSION_SOLID_CENTER = new Vector2(12.5f, 12.5f);
    public static final float SUSPENSION_SOLID_DURATION = 0.5f;
    public static final int SUSPENSION_SOLID_DAMAGE = 5;
    public static final Vector2 SUSPENSION_SOLID_KNOCKBACK = new Vector2(200, Y_KNOCKBACK);
    public static final float SUSPENSION_SOLID_COLLISION_WIDTH = 25;
    public static final float SUSPENSION_SOLID_COLLISION_HEIGHT = 25;
    public static final String SUSPENSION_SOLID_SPRITE_1 = "suspension-solid-1";
    public static final String SUSPENSION_SOLID_SPRITE_2 = "suspension-solid-2";

    public static final Vector2 SUSPENSION_ORE_CENTER = new Vector2(12.5f, 12.5f);
    public static final float SUSPENSION_ORE_DURATION = 0.1f;
    public static final int SUSPENSION_ORE_DAMAGE = 5;
    public static final Vector2 SUSPENSION_ORE_KNOCKBACK = new Vector2(200, Y_KNOCKBACK + 100);
    public static final float SUSPENSION_ORE_COLLISION_WIDTH = 25;
    public static final float SUSPENSION_ORE_COLLISION_HEIGHT = 25;
    public static final String SUSPENSION_ORE_SPRITE_1 = "suspension-polymer-1";
    public static final String SUSPENSION_ORE_SPRITE_2 = "suspension-polymer-2";

    public static final Vector2 SUSPENSION_ANTIMATTER_CENTER = new Vector2(31, 31);
    public static final float SUSPENSION_ANTIMATTER_FRAME_DURATION = 0.4f;
    public static final int SUSPENSION_ANTIMATTER_DAMAGE = 75;
    public static final Vector2 SUSPENSION_ANTIMATTER_KNOCKBACK = new Vector2(100, Y_KNOCKBACK);
    public static final float SUSPENSION_ANTIMATTER_COLLISION_WIDTH = 25;
    public static final float SUSPENSION_ANTIMATTER_COLLISION_HEIGHT = 25;
    public static final String SUSPENSION_ANTIMATTER_SPRITE_1 = "suspension-antimatter-1";
    public static final String SUSPENSION_ANTIMATTER_SPRITE_2 = "suspension-antimatter-2";
    public static final String SUSPENSION_ANTIMATTER_SPRITE_3 = "suspension-antimatter-3";

    public static final Vector2 SUSPENSION_INACTIVE_CENTER = new Vector2(12.5f, 12.5f);
    public static final String SUSPENSION_INACTIVE_SPRITE = "suspension-inactive";

    // Portal
    public static final Vector2 PORTAL_CENTER = new Vector2(31, 31);
    public static final float PORTAL_RADIUS = 28;
    public static final float PORTAL_FRAME_DURATION = 0.15f;
    public static final String PORTAL_SPRITE_1 = "portal-1";
    public static final String PORTAL_SPRITE_2 = "portal-2";
    public static final String PORTAL_SPRITE_3 = "portal-3";
    public static final String PORTAL_SPRITE_4 = "portal-4";
    public static final String PORTAL_SPRITE_5 = "portal-5";
    public static final String PORTAL_SPRITE_6 = "portal-6";

    public static final Vector2 TELEPORT_CENTER = new Vector2(14, 16);
    public static final float TELEPORT_FRAME_DURATION = 0.125f;
    public static final String TELEPORT_SPRITE_1 = "teleport-1";
    public static final String TELEPORT_SPRITE_2 = "teleport-2";
    public static final String TELEPORT_SPRITE_3 = "teleport-3";

    // Gate
    public static final String GATE_SPRITE_0 = "gate-0";
    public static final String GATE_SPRITE_1 = "gate-1";
    public static final String GATE_SPRITE_2 = "gate-2";
    public static final String GATE_SPRITE_3 = "gate-3";
    public static final String GATE_SPRITE_4 = "gate-4";
    public static final String GATE_SPRITE_5 = "gate-5";
    public static final float GATE_FRAME_DURATION = 0.1f;
    public static final Vector2 GATE_CENTER = new Vector2(4, 17.5f);

    // Projectile
    public static final float BLAST_CHARGE_DURATION = 1;
    public static final float AMMO_MAX_SPEED = 300;
    public static final float AMMO_NORMAL_SPEED = 180;
    public static final int AMMO_STANDARD_DAMAGE = 10;
    public static final int AMMO_SPECIALIZED_DAMAGE = 30;
    public static final int AMMO_WEAK_DAMAGE = 1;
    public static final float SHOT_FRAME_DURATION = 0.1f;
    public static final float SHOT_RADIUS = 4.5f;
    public static final float BLAST_RADIUS = 8;
    public static final Vector2 SHOT_CENTER = new Vector2(SHOT_RADIUS, SHOT_RADIUS);
    public static final Vector2 BLAST_CENTER = new Vector2(BLAST_RADIUS, BLAST_RADIUS);
    public static final String SHOT_NATIVE_SPRITE_1 = "ammo-shot-native-1";
    public static final String SHOT_NATIVE_SPRITE_2 = "ammo-shot-native-2";
    public static final String BLAST_NATIVE_SPRITE_1 = "ammo-blast-native-1";
    public static final String BLAST_NATIVE_SPRITE_2 = "ammo-blast-native-2";
    public static final String BLAST_NATIVE_SPRITE_3 = "ammo-blast-native-3";
    public static final String SHOT_GAS_SPRITE_1 = "ammo-shot-gas-1";
    public static final String SHOT_GAS_SPRITE_2 = "ammo-shot-gas-2";
    public static final String BLAST_GAS_SPRITE_1 = "ammo-blast-gas-1";
    public static final String BLAST_GAS_SPRITE_2 = "ammo-blast-gas-2";
    public static final String BLAST_GAS_SPRITE_3 = "ammo-blast-gas-3";
    public static final String SHOT_LIQUID_SPRITE_1 = "ammo-shot-liquid-1";
    public static final String SHOT_LIQUID_SPRITE_2 = "ammo-shot-liquid-2";
    public static final String BLAST_LIQUID_SPRITE_1 = "ammo-blast-liquid-1";
    public static final String BLAST_LIQUID_SPRITE_2 = "ammo-blast-liquid-2";
    public static final String BLAST_LIQUID_SPRITE_3 = "ammo-blast-liquid-3";
    public static final String SHOT_PLASMA_SPRITE_1 = "ammo-shot-plasma-1";
    public static final String SHOT_PLASMA_SPRITE_2 = "ammo-shot-plasma-2";
    public static final String BLAST_PLASMA_SPRITE_1 = "ammo-blast-plasma-1";
    public static final String BLAST_PLASMA_SPRITE_2 = "ammo-blast-plasma-2";
    public static final String BLAST_PLASMA_SPRITE_3 = "ammo-blast-plasma-3";
    public static final String SHOT_ORE_SPRITE_1 = "ammo-shot-polymer-1";
    public static final String SHOT_ORE_SPRITE_2 = "ammo-shot-polymer-2";
    public static final String BLAST_ORE_SPRITE_1 = "ammo-blast-polymer-1";
    public static final String BLAST_ORE_SPRITE_2 = "ammo-blast-polymer-2";
    public static final String BLAST_ORE_SPRITE_3 = "ammo-blast-polymer-3";
    public static final String SHOT_SOLID_SPRITE_1 = "ammo-shot-solid-1";
    public static final String SHOT_SOLID_SPRITE_2 = "ammo-shot-solid-2";
    public static final String BLAST_SOLID_SPRITE_1 = "ammo-blast-solid-1";
    public static final String BLAST_SOLID_SPRITE_2 = "ammo-blast-solid-2";
    public static final String BLAST_SOLID_SPRITE_3 = "ammo-blast-solid-3";
    public static final String SHOT_ANTIMATTER_SPRITE_1 = "ammo-shot-psychic-1";
    public static final String SHOT_ANTIMATTER_SPRITE_2 = "ammo-shot-psychic-2";
    public static final String BLAST_ANTIMATTER_SPRITE_1 = "ammo-blast-psychic-1";
    public static final String BLAST_ANTIMATTER_SPRITE_2 = "ammo-blast-psychic-2";
    public static final String BLAST_ANTIMATTER_SPRITE_3 = "ammo-blast-psychic-3";
    public static final String SHOT_HYBRID_SPRITE = "ammo-shot-hybrid";
    public static final String SHOT_HYBRID_SPRITE_2 = "ammo-shot-hybrid-2";
    public static final String BLAST_HYBRID_SPRITE_1 = "ammo-blast-hybrid-1";
    public static final String BLAST_HYBRID_SPRITE_2 = "ammo-blast-hybrid-2";
    public static final String BLAST_HYBRID_SPRITE_3 = "ammo-blast-hybrid-3";

    // Blade
    public static final Vector2 BLADE_CENTER = new Vector2(40, 35);
    public static final int BLADE_DAMAGE_FACTOR = 10;

    public static final String FLIPSWIPE_NATIVE_SPRITE_1 = "blade-flip-native-1";
    public static final String FLIPSWIPE_NATIVE_SPRITE_2 = "blade-flip-native-2";
    public static final String FLIPSWIPE_NATIVE_SPRITE_3 = "blade-flip-native-3";
    public static final String FLIPSWIPE_NATIVE_SPRITE_4 = "blade-flip-native-4";
    public static final String FLIPSWIPE_NATIVE_SPRITE_5 = "blade-flip-native-5";
    public static final String FLIPSWIPE_NATIVE_SPRITE_6 = "blade-flip-native-6";
    public static final String SIDESWIPE_NATIVE_SPRITE_1 = "blade-side-native-1";
    public static final String SIDESWIPE_NATIVE_SPRITE_2 = "blade-side-native-2";
    public static final String SIDESWIPE_NATIVE_SPRITE_3 = "blade-side-native-3";

    public static final String FLIPSWIPE_ORE_SPRITE_1 = "blade-flip-polymer-1";
    public static final String FLIPSWIPE_ORE_SPRITE_2 = "blade-flip-polymer-2";
    public static final String FLIPSWIPE_ORE_SPRITE_3 = "blade-flip-polymer-3";
    public static final String FLIPSWIPE_ORE_SPRITE_4 = "blade-flip-polymer-4";
    public static final String FLIPSWIPE_ORE_SPRITE_5 = "blade-flip-polymer-5";
    public static final String FLIPSWIPE_ORE_SPRITE_6 = "blade-flip-polymer-6";
    public static final String SIDESWIPE_ORE_SPRITE_1 = "blade-side-polymer-1";
    public static final String SIDESWIPE_ORE_SPRITE_2 = "blade-side-polymer-2";
    public static final String SIDESWIPE_ORE_SPRITE_3 = "blade-side-polymer-3";

    public static final String FLIPSWIPE_PLASMA_SPRITE_1 = "blade-flip-plasma-1";
    public static final String FLIPSWIPE_PLASMA_SPRITE_2 = "blade-flip-plasma-2";
    public static final String FLIPSWIPE_PLASMA_SPRITE_3 = "blade-flip-plasma-3";
    public static final String FLIPSWIPE_PLASMA_SPRITE_4 = "blade-flip-plasma-4";
    public static final String FLIPSWIPE_PLASMA_SPRITE_5 = "blade-flip-plasma-5";
    public static final String FLIPSWIPE_PLASMA_SPRITE_6 = "blade-flip-plasma-6";
    public static final String SIDESWIPE_PLASMA_SPRITE_1 = "blade-side-plasma-1";
    public static final String SIDESWIPE_PLASMA_SPRITE_2 = "blade-side-plasma-2";
    public static final String SIDESWIPE_PLASMA_SPRITE_3 = "blade-side-plasma-3";

    public static final String FLIPSWIPE_GAS_SPRITE_1 = "blade-flip-gas-1";
    public static final String FLIPSWIPE_GAS_SPRITE_2 = "blade-flip-gas-2";
    public static final String FLIPSWIPE_GAS_SPRITE_3 = "blade-flip-gas-3";
    public static final String FLIPSWIPE_GAS_SPRITE_4 = "blade-flip-gas-4";
    public static final String FLIPSWIPE_GAS_SPRITE_5 = "blade-flip-gas-5";
    public static final String FLIPSWIPE_GAS_SPRITE_6 = "blade-flip-gas-6";
    public static final String SIDESWIPE_GAS_SPRITE_1 = "blade-side-gas-1";
    public static final String SIDESWIPE_GAS_SPRITE_2 = "blade-side-gas-2";
    public static final String SIDESWIPE_GAS_SPRITE_3 = "blade-side-gas-3";

    public static final String FLIPSWIPE_LIQUID_SPRITE_1 = "blade-flip-liquid-1";
    public static final String FLIPSWIPE_LIQUID_SPRITE_2 = "blade-flip-liquid-2";
    public static final String FLIPSWIPE_LIQUID_SPRITE_3 = "blade-flip-liquid-3";
    public static final String FLIPSWIPE_LIQUID_SPRITE_4 = "blade-flip-liquid-4";
    public static final String FLIPSWIPE_LIQUID_SPRITE_5 = "blade-flip-liquid-5";
    public static final String FLIPSWIPE_LIQUID_SPRITE_6 = "blade-flip-liquid-6";
    public static final String SIDESWIPE_LIQUID_SPRITE_1 = "blade-side-liquid-1";
    public static final String SIDESWIPE_LIQUID_SPRITE_2 = "blade-side-liquid-2";
    public static final String SIDESWIPE_LIQUID_SPRITE_3 = "blade-side-liquid-3";

    public static final String FLIPSWIPE_SOLID_SPRITE_1 = "blade-flip-solid-1";
    public static final String FLIPSWIPE_SOLID_SPRITE_2 = "blade-flip-solid-2";
    public static final String FLIPSWIPE_SOLID_SPRITE_3 = "blade-flip-solid-3";
    public static final String FLIPSWIPE_SOLID_SPRITE_4 = "blade-flip-solid-4";
    public static final String FLIPSWIPE_SOLID_SPRITE_5 = "blade-flip-solid-5";
    public static final String FLIPSWIPE_SOLID_SPRITE_6 = "blade-flip-solid-6";
    public static final String SIDESWIPE_SOLID_SPRITE_1 = "blade-side-solid-1";
    public static final String SIDESWIPE_SOLID_SPRITE_2 = "blade-side-solid-2";
    public static final String SIDESWIPE_SOLID_SPRITE_3 = "blade-side-solid-3";

    public static final String FLIPSWIPE_ANTIMATTER_SPRITE_1 = "blade-flip-psychic-1";
    public static final String FLIPSWIPE_ANTIMATTER_SPRITE_2 = "blade-flip-psychic-2";
    public static final String FLIPSWIPE_ANTIMATTER_SPRITE_3 = "blade-flip-psychic-3";
    public static final String FLIPSWIPE_ANTIMATTER_SPRITE_4 = "blade-flip-psychic-4";
    public static final String FLIPSWIPE_ANTIMATTER_SPRITE_5 = "blade-flip-psychic-5";
    public static final String FLIPSWIPE_ANTIMATTER_SPRITE_6 = "blade-flip-psychic-6";
    public static final String SIDESWIPE_ANTIMATTER_SPRITE_1 = "blade-side-psychic-1";
    public static final String SIDESWIPE_ANTIMATTER_SPRITE_2 = "blade-side-psychic-2";
    public static final String SIDESWIPE_ANTIMATTER_SPRITE_3 = "blade-side-psychic-3";

    public static final String FLIPSWIPE_HYBRID_SPRITE_1 = "blade-flip-hybrid-1";
    public static final String FLIPSWIPE_HYBRID_SPRITE_2 = "blade-flip-hybrid-2";
    public static final String FLIPSWIPE_HYBRID_SPRITE_3 = "blade-flip-hybrid-3";
    public static final String FLIPSWIPE_HYBRID_SPRITE_4 = "blade-flip-hybrid-4";
    public static final String FLIPSWIPE_HYBRID_SPRITE_5 = "blade-flip-hybrid-5";
    public static final String FLIPSWIPE_HYBRID_SPRITE_6 = "blade-flip-hybrid-6";
    public static final String SIDESWIPE_HYBRID_SPRITE_1 = "blade-side-hybrid-1";
    public static final String SIDESWIPE_HYBRID_SPRITE_2 = "blade-side-hybrid-2";
    public static final String SIDESWIPE_HYBRID_SPRITE_3 = "blade-side-hybrid-3";

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
    public static final int POWERUP_AMMO = 20;
    public static final int POWERUP_HEALTH = 50;
    public static final int POWERUP_TURBO = 100;

    public static final float POWERUP_DURATION = 0.5f;

    public static final String AMMO_POWERUP_SPRITE_1 = "powerup-ammo-1";
    public static final String AMMO_POWERUP_SPRITE_2 = "powerup-ammo-2";
    public static final String AMMO_POWERUP_SPRITE = "powerup-ammo";
    public static final Vector2 AMMO_POWERUP_CENTER = new Vector2(6, 4);

    public static final String HEALTH_POWERUP_SPRITE_1 = "powerup-health-1";
    public static final String HEALTH_POWERUP_SPRITE_2 = "powerup-health-2";
    public static final String HEALTH_POWERUP_SPRITE = "powerup-health";
    public static final Vector2 HEALTH_POWERUP_CENTER = new Vector2(7.5f, 8.5f);

    public static final String TURBO_POWERUP_SPRITE_1 = "powerup-turbo-1";
    public static final String TURBO_POWERUP_SPRITE_2 = "powerup-turbo-2";
    public static final String TURBO_POWERUP_SPRITE = "powerup-turbo";
    public static final Vector2 TURBO_POWERUP_CENTER = new Vector2(7.5f, 8.5f);

    public static final String LIFE_POWERUP_SPRITE_1 = "icon-life-1";
    public static final String LIFE_POWERUP_SPRITE_2 = "icon-life-2";
    public static final String LIFE_POWERUP_SPRITE = "icon-life";
    public static final Vector2 LIFE_POWERUP_CENTER = new Vector2(7, 5);

    public static final String SUPER_POWERUP_SPRITE_1 = "powerup-super-1";
    public static final String SUPER_POWERUP_SPRITE_2 = "powerup-super-2";
    public static final String SUPER_POWERUP_SPRITE = "powerup-super";
    public static final Vector2 SUPER_POWERUP_CENTER = new Vector2(10, 6);

    public static final String CANNON_POWERUP_SPRITE_1 = "powerup-cannon-1";
    public static final String CANNON_POWERUP_SPRITE_2 = "powerup-cannon-2";
    public static final String CANNON_POWERUP_SPRITE_3 = "powerup-cannon-3";
    public static final String CANNON_POWERUP_SPRITE = "powerup-cannon";
    public static final Vector2 CANNON_POWERUP_CENTER = new Vector2(8, 5);

    public static final float GEM_DURATION = 0.2f;

    public static final String RUBY_SPRITE_1 = "gem-ruby-1";
    public static final String RUBY_SPRITE_2 = "gem-ruby-2";

    public static final String SAPPHIRE_SPRITE_1 = "gem-sapphire-1";
    public static final String SAPPHIRE_SPRITE_2 = "gem-sapphire-2";

    public static final String EMERALD_SPRITE_1 = "gem-emerald-1";
    public static final String EMERALD_SPRITE_2 = "gem-emerald-2";

    public static final Vector2 GEM_CENTER = new Vector2(4, 4);

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
    public static final String LEVEL_ROTATION_KEY = "rotation";
    public static final String LEVEL_IDENTIFIER_KEY = "itemIdentifier";
    public static final String LEVEL_CUSTOM_VARS_KEY = "customVars";
    public static final String LEVEL_TAGS_KEY = "tags";
    public static final String LEVEL_RANGE_KEY = "range";
    public static final String LEVEL_SPEED_KEY = "speed";
    public static final String LEVEL_DIRECTION_KEY = "direction";
    public static final String LEVEL_TYPE_KEY = "type";
    public static final String LEVEL_INTENSITY_KEY = "intensity";
    public static final String LEVEL_BOUNDS_KEY = "bounds";
    public static final String LEVEL_DESTINATION_KEY = "destination";
    public static final String LEVEL_UPGRADE_KEY = "upgrade";
    public static final String LEDGE_TAG = "ledge";
    public static final int LEDGE_TAG_INDEX = 0;
    public static final String ON_TAG = "on";
    public static final int ON_TAG_INDEX = 1;
    public static final String OFF_TAG = "off";
    public static final int OFF_TAG_INDEX = 2;
    public static final String CONVERTIBLE_TAG = "convertible";
    public static final int CONVERTIBLE_TAG_INDEX = 3;
    public static final String GOAL_TAG = "goal";
    public static final int GOAL_TAG_INDEX = 4;


    // HUD
    public static final float HUD_VIEWPORT_SIZE = 480;
    public static final float HUD_MARGIN = 20;
    public static final float AMMO_ICON_SCALE = 1.25f;
    public static final float LIFE_ICON_SCALE = 1;
    public static final float ACTION_ICON_SCALE = .75f;
    public static final String SHOOT_ICON = "icon-blast";
    public static final String BLAST_ICON = "icon-blast";
    public static final String JUMP_ICON = "icon-jump";
    public static final String HOVER_ICON = "icon-hover";
    public static final String RAPPEL_ICON = "icon-rappel";
    public static final String CLIMB_ICON = "icon-climb";
    public static final String DASH_ICON = "icon-dash";
    public static final String MOVE_ICON = "icon-move";
    public static final String LIFE_ICON = "icon-life";
    public static final String HUD_FUEL_LABEL = "Fuel: ";
    public static final String HUD_STAMINA_LABEL = "Stamina: ";
    public static final String HUD_SCORE_LABEL = "Score: ";
    public static final String HUD_HEALTH_LABEL = "Health: ";
    public static final String HUD_ENERGY_LABEL = "Energy: ";
    public static final Vector2 ABILITY_ICON_CENTER = new Vector2(20, 8.5f);
    public static final Vector2 LIFE_ICON_CENTER = new Vector2(7, 4.5f);
    public static final Color HEALTH_CRITICAL_COLOR = Color.RED;
    public static final Color HEALTH_LOW_COLOR = Color.CORAL;
    public static final Color HEALTH_MEDIUM_COLOR = new Color(0x005fddff);
    public static final Color HEALTH_HIGH_COLOR = new Color(0x0077eeff);
    public static final Color HEALTH_MAX_COLOR = new Color(0x1e90ffff);
    public static final Color TURBO_NORMAL_COLOR = Color.FOREST;
    public static final Color TURBO_MAX_COLOR = new Color(0x006400FF);
    public static final Color AMMO_NORMAL_COLOR = new Color(0xB8860BFF);
    public static final Color AMMO_CHARGED_COLOR = Color.GOLDENROD;
    public static final Color AMMO_BLAST_COLOR = new Color(0xFAC94EFF);

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
    public static final float LEVEL_END_DURATION = 20;
    public static final int EXPLOSION_COUNT = 500;
    public static final int ZOOMBA_COUNT = 200;
    public static final String VICTORY_MESSAGE = "COURSE CLEARED";
    public static final String FAIL_MESSAGE = "GAME OVER";
    public static final String LAUNCH_MESSAGE = "Energen v0.0.5";
    public static final String TIME_PATTERN = "HH:mm:ss";

    // Start screen
    public static final String LOGO_SPRITE = "logo-eg";
    public static final Vector2 LOGO_CENTER = new Vector2(117.5f, 117.5f);
    public static final String BEAST_SPRITE = "beast";
    public static final Vector2 BEAST_CENTER = new Vector2(16, 16);
    public static final String GLOBE_SPRITE = "globe";
    public static final Vector2 GLOBE_CENTER = new Vector2(50, 50);

    // Scoring
    public static final int ZOOMBA_KILL_SCORE = 100;
    public static final int ZOOMBA_HIT_SCORE = 25;
    public static final int SWOOPA_KILL_SCORE = 100;
    public static final int SWOOPA_HIT_SCORE = 25;
    public static final int ORBEN_KILL_SCORE = 100;
    public static final int ORBEN_HIT_SCORE = 25;
    public static final int ROLLEN_KILL_SCORE = 100;
    public static final int ROLLEN_HIT_SCORE = 25;
    public static final int POWERUP_SCORE = 200;

    // Preferences
    public static final int[] DIFFICULTY_MULTIPLIER = {1, 2, 3};

    // Overlays
    public static final String DEBUG_MODE_MESSAGE = "DEBUG MODE\n\nPRESS SHOOT TO EXIT";
}
