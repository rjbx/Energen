package com.udacity.gamedev.gigagal.util;

public final class Enums {

    private Enums() {}

    public enum AmmoType {
        REGULAR, CHARGE
    }

    public enum Direction {
        LEFT, RIGHT
    }

    public enum AerialMove {
        JUMPING,
        HOVERING,
        RICOCHETING,
        FALLING,
        GROUNDED,
        RECOILING,
        SLIDING
    }

    public enum GroundMove {
        STANDING,
        LEANING,
        WALKING,
        DASHING
    }
}
