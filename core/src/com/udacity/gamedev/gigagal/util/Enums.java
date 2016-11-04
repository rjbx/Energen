package com.udacity.gamedev.gigagal.util;

public final class Enums {

    private Enums() {}

    public enum AmmoType {
        REGULAR, CHARGE
    }

    public enum Direction {
        LEFT, RIGHT
    }

    public enum AerialState {
        JUMPING,
        HOVERING,
        RICOCHETING,
        FALLING,
        GROUNDED,
        RECOILING,
        SLIDING
    }

    public enum GroundState {
        STANDING,
        LEANING,
        STRIDING,
        DASHING,
        RECOILING,
        AIRBORNE
    }
}
