package com.github.rjbx.energraft.entity;

import com.badlogic.gdx.utils.Array;

public interface Impermeable {
    void touchAllGrounds(Array<Ground> grounds);
    void touchAllHazards(Array<Hazard> hazards);
}
