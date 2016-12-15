package com.udacity.gamedev.gigagal.util;

public final class Enums {

    private Enums() {}

    public enum ShotIntensity {
        NORMAL, CHARGED,
    }

    public enum Weapon {
        ELECTRIC,
        FIRE,
        WATER,
        RUBBER,
        METAL,
        PSYCHIC,
        NATIVE
    }

    public enum Direction {
        LEFT,
        RIGHT,
        DOWN,
        UP
    }

    public enum Orientation {
        VERTICAL,
        LATERAL
    }

    public enum AerialState {
        JUMPING,
        LOOKING,
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
        LOOKING,
        AIRBORNE
    }
}
