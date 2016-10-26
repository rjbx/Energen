package com.udacity.gamedev.gigagal.util;


public class Enums {

    public enum BulletType {
        REGULAR, CHARGE
    }


    public enum Direction {
        LEFT, RIGHT
    }

    public enum JumpState {
        JUMPING,
        HOVERING,
        FALLING,
        GROUNDED,
        RECOILING
    }

    public enum WalkState {
        NOT_WALKING,
        WALKING,
        DASHING
    }
}
