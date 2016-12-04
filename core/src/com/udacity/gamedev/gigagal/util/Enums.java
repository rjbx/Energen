package com.udacity.gamedev.gigagal.util;

public final class Enums {

    private Enums() {}

    public enum ShotIntensity {
        NORMAL, CHARGED,
    }

    public enum AmmoType {
        ELECTRIC, FIRE, WATER, RUBBER, METAL, PSYCHIC
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
        RECOILING
    }

    public enum GroundState {
        STANDING,
        STRIDING,
        DASHING,
        AIRBORNE
    }
}
