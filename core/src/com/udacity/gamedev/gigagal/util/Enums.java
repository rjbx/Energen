package com.udacity.gamedev.gigagal.util;

public final class Enums {

    public enum AmmoType {
        REGULAR, CHARGE
    }

    public enum DirectionalState {
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

    public enum TerrestrialState {
        STANDING,
        LEANING,
        WALKING,
        DASHING
    }
}
