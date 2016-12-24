package com.nukesz.game;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;

public class BirdContactListener implements ContactListener {

    private Array<Body> toRemove = new Array<Body>();
    private World world;

    public BirdContactListener(World world) {
        this.world = world;
    }

    @Override
    public void beginContact(Contact contact) {
        if (contact.isTouching()) {
            Fixture attacker = contact.getFixtureA();
            Fixture defender = contact.getFixtureB();
            WorldManifold worldManifold = contact.getWorldManifold();
            if ("enemy".equals(defender.getUserData())) {
                Vector2 vel1 = attacker.getBody().getLinearVelocityFromWorldPoint(worldManifold.getPoints()[0]);
                Vector2 vel2 = defender.getBody().getLinearVelocityFromWorldPoint(worldManifold.getPoints()[0]);
                Vector2 impactVelocity = vel1.sub(vel2);
                if (Math.abs(impactVelocity.x) > 1 || Math.abs(impactVelocity.y) > 1) {
                    toRemove.add(defender.getBody());
                }
            }
        }
    }

    public void clearDeadBodies() {
        for (Body body : toRemove) {
            world.destroyBody(body);
        }
        toRemove.clear();
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
