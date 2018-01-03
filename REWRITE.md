trying to remember as much as i can from 9th grade physical science class in the context of the game world to figure out what formulas are required

entity has mass, volume, motion (in terms of entity standing position), internal motion (movement) force, internal attack (action) force, with motion impacted by vertical (e.g. gravity) horizontal (e.g. drag) resistance and other entities

entity
static mass 
static [variable] volume for humanoid [mechanical] type
variable force dependent on ability
motion depends on force of ability exerted on density (mass volume) of entity in relation to force of gravity on vertical and drag on horizontal motion as well as other entities on either

example, hypothetical numbers

stride force 5 units
drag force 1 unit

effect of drag determined by momentum (density motion) of entity

at motion = 0, density = marginal, net force = 4 force units, motion = 4 motion units (change in distance over time)

at motion = 0, density = substantial, net force = 4 force units, motion = 0

more force needed to move denser entity, but once in motion, more force required to oppose motion (inertia)

at motion = 10, density = marginal, net force = 4, motion = 14

at motion = 10, density = substantial, net force = 4, motion = >20

entity rewrite:

physical
-attributes (constant mass, constant/variable volume, vector x y mForce, vector x y aForce, vector x y velocity, vector x y momentum, float abilityMultiplier, enum type, enum movement, enum action)
-movement (e.g. stride, dash, jump, hover, rappel, climb)
-action (e.g. shoot, swipe, carry, talk)

void update()
switch(movement)
switch(action)
enableMovement()

vector move()
return force

levelupdater.spawn(physical p)
switch(p.getAction)
physicals.add(physical p)

 
physics rewrite

private void applyPhysics(Array<Physical> physicals) {

    for (Physical p : physicals) {

        // motion
        float gravity = Constants.GRAVITY;
        float drag = Constants.DRAG;

        for (Rectangle v : vacuums) {
            v.overlaps(p) ? gravity *= gravityManipulator : 0;
        }

        for (Rectangle v :  vortexes) {
            v.overlaps(p) ? drag *= dragManipulator : 0;
        }

        p.getMass();
		p.getVolume();
        p.getMovementForce();
		// determine p1 motion
        p.queueChanges(acceleration, velocity, momentum);

        // collision
        boolean topCollision, bottomCollision, sideCollision;
        object topPhysical, bottomPhysical, sidePhysical;
        for (Physical p2 : physicals) {
            if (p != p2) {
                if (p.overlaps(p2)) {
                    applyTopCollision(p, p2);
                    applyBottomCollision(p, p2);
                    applySideCollision(p, p2);
                }
            }
        }
    }

    for (Physical p : physicals) {
        p.propagateChanges(); // prevents changing before determining other object collision
    }
}

boolean applyTopCollision(Physical p1, Physical p2) {
    switch (p1.getType()) {
        switch (p1.getMovement()) {
            switch (p2.getType()) {
                switch (p2.getMovement()) {
                    p2.getMass();
                    p2.getVolume();
					p2.getMovementForce();
					p2.getActionForce();
					// determine impact of collision on p1 motion
                    p1.queueChanges(motion);
                }
            }
        }
    }
}

boolean applyBottomCollision(Physical p1, Physical p2) {
    switch (p1.getType()) {
        switch (p1.getMovement()) {
            switch (p2.getType()) {
                switch (p2.getMovement()) {
                    p2.getMass();
                    p2.getVolume();
					p2.getMovementForce();
					p2.getActionForce();
					// determine impact of collision on p1 motion
                    p1.queueChanges(acceleration, velocity, momentum);
                }
            }
        }
    }
}

boolean applySideCollision(Physical p1, Physical p2) {
    switch (p1.getType()) {
        switch (p1.getMovement()) {
            switch (p2.getType()) {
                switch (p2.getMovement()) {
                    p2.getMass();
                    p2.getVolume();
					p2.getMovementForce();
					p2.getActionForce();
					// determine impact of collision on p1 motion
                    p1.queueChanges(acceleration, velocity, momentum);
                }
            }
        }
    }
}
