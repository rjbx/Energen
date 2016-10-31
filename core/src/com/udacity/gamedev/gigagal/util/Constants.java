package com.udacity.gamedev.gigagal.util;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

public class Constants {

    // World/Camera
    public static final Color BACKGROUND_COLOR = Color.SKY;
    public static final float WORLD_SIZE = 160;
    public static final float KILL_PLANE = -200;
    public static final float GRAVITY = 10;
    public static final float CHASE_CAM_MOVE_SPEED = WORLD_SIZE;
    public static final String TEXTURE_ATLAS = "images/gigagal.pack.atlas";

    // GigaGal
    public static final Vector2 GIGAGAL_EYE_POSITION = new Vector2(16, 24);
    public static final float GIGAGAL_EYE_HEIGHT = 16.0f;
    public static final float GIGAGAL_STANCE_WIDTH = 19.0f;
    public static final Vector2 GIGAGAL_CANNON_OFFSET = new Vector2(4, -7);
    public static final float GIGAGAL_HEIGHT = 23.0f;
    public static final float GIGAGAL_HEAD_RADIUS = 3;
    public static final float GIGAGAL_MAX_SPEED = 200;

    public static final float JUMP_SPEED = 200;
    public static final float RUNNING_JUMP_MULTIPLIER = 1.1f;
    public static final Vector2 KNOCKBACK_VELOCITY = new Vector2(200, 200);
    public static final float MAX_JUMP_DURATION = .1f;
    public static final float MAX_HOVER_DURATION = 1f;
    public static final float MAX_DASH_DURATION = .5f;
    public static final float DOUBLE_TAP_SPEED = 0.3f;
    public static final int INITIAL_AMMO = 10;
    public static final int INITIAL_LIVES = 3;

    public static final String STANDING_RIGHT = "standing-right";
    public static final String STANDING_LEFT = "standing-left";
    public static final String JUMPING_RIGHT = "jumping-right";
    public static final String JUMPING_LEFT = "jumping-left";
    public static final String RICOCHETING_RIGHT = "ricochet-right";
    public static final String RICOCHETING_LEFT = "ricochet-left";
    public static final float RICOCHET_DURATION = 0.05f;
    public static final String HOVER_RIGHT_1 = "hover-1-right";
    public static final String HOVER_LEFT_1 = "hover-1-left";
    public static final String HOVER_RIGHT_2 = "hover-2-right";
    public static final String HOVER_LEFT_2 = "hover-2-left";
    public static final float HOVER_LOOP_DURATION = 0.05f;
    public static final String WALKING_RIGHT_1 = "walk-1-right";
    public static final String WALKING_LEFT_1 = "walk-1-left";
    public static final String WALKING_RIGHT_2 = "walk-2-right";
    public static final String WALKING_LEFT_2 = "walk-2-left";
    public static final String WALKING_RIGHT_3 = "walk-3-right";
    public static final String WALKING_LEFT_3 = "walk-3-left";
    public static final float WALK_LOOP_DURATION = 0.1f;

    // Platform
    public static final String PLATFORM_SPRITE = "platform";
    public static final float PLATFORM_EDGE = 8;
    public static final float MAX_LEDGE_HEIGHT = 5;

    // Zoomba
    public static final String ZOOMBA_SPRITE = "zoomba";
    public static final Vector2 ZOOMBA_CENTER = new Vector2(14, 22);
    public static final float ZOOMBA_MOVEMENT_SPEED = 10;
    public static final float ZOOMBA_BOB_AMPLITUDE = 2;
    public static final float ZOOMBA_BOB_PERIOD = 3.0f;
    public static final int ZOOMBA_HEALTH = 5;
    public static final float ZOOMBA_COLLISION_RADIUS = 15;
    public static final float ZOOMBA_SHOT_RADIUS = 17;

    // Bullet
    public static final String BULLET_SPRITE = "bullet";
    public static final String CHARGE_BULLET_SPRITE = "charge-bullet";
    public static final float CHARGE_DURATION = .8f;
    public static final float BULLET_MOVE_SPEED = 500;
    public static final Vector2 BULLET_CENTER = new Vector2(3, 2);
    public static final Vector2 CHARGE_BULLET_CENTER = new Vector2(8, 4);

    // Explosion
    public static final String EXPLOSION_LARGE = "explosion-large";
    public static final String EXPLOSION_MEDIUM = "explosion-medium";
    public static final String EXPLOSION_SMALL = "explosion-small";
    public static final Vector2 EXPLOSION_CENTER = new Vector2(8, 8);
    public static final float EXPLOSION_DURATION = 0.5f;

    // Powerup
    public static final String POWERUP_SPRITE = "powerup";
    public static final Vector2 POWERUP_CENTER = new Vector2(7, 5);
    public static final int POWERUP_AMMO = 10;

    // Exit Portal
    public static final String EXIT_PORTAL_SPRITE_1 = "exit-portal-1";
    public static final String EXIT_PORTAL_SPRITE_2 = "exit-portal-2";
    public static final String EXIT_PORTAL_SPRITE_3 = "exit-portal-3";
    public static final String EXIT_PORTAL_SPRITE_4 = "exit-portal-4";
    public static final String EXIT_PORTAL_SPRITE_5 = "exit-portal-5";
    public static final String EXIT_PORTAL_SPRITE_6 = "exit-portal-6";
    public static final Vector2 EXIT_PORTAL_CENTER = new Vector2(31, 31);
    public static final float EXIT_PORTAL_RADIUS = 28;
    public static final float EXIT_PORTAL_FRAME_DURATION = 0.05f;

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
    public static final String[] LEVELS = {"levels/Level1.dt", "levels/Level2.dt", "levels/Level3.dt", };

    // HUD
    public static final float HUD_VIEWPORT_SIZE = 480;
    public static final float HUD_MARGIN = 20;
    public static final String HUD_AMMO_LABEL = "Ammo: ";
    public static final String HUD_SCORE_LABEL = "Score: ";

    // Onscreen Controls
    public static final float ONSCREEN_CONTROLS_VIEWPORT_SIZE = 200;
    public static final String MOVE_LEFT_BUTTON = "button-move-left";
    public static final String MOVE_RIGHT_BUTTON = "button-move-right";
    public static final String SHOOT_BUTTON = "button-shoot";
    public static final String JUMP_BUTTON = "button-jump";
    public static final Vector2 BUTTON_CENTER = new Vector2(15, 15);
    public static final float BUTTON_RADIUS = 32;

    // Victory/Game Over screens
    public static final float LEVEL_END_DURATION = 5;
    public static final String VICTORY_MESSAGE = "Boo Ya.";
    public static final String GAME_OVER_MESSAGE = "Game Over, Gal";
    public static final int EXPLOSION_COUNT = 500;
    public static final int ZOOMBA_COUNT = 200;
    public static final String FONT_FILE = "font/header.fnt";

    // Scoring
    public static final int ZOOMBA_KILL_SCORE = 100;
    public static final int ZOOMBA_HIT_SCORE = 25;
    public static final int POWERUP_SCORE = 50;

}
