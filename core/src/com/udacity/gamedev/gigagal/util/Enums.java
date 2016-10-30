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
        RICOCHETING,
        FALLING,
        GROUNDED,
        RECOILING,
        SLIDING
    }

    public enum WalkState {
        NOT_WALKING,
        LEANING,
        WALKING,
        DASHING
    }
}
