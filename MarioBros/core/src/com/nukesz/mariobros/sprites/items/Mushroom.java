package com.nukesz.mariobros.sprites.items;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.nukesz.mariobros.MarioBros;
import com.nukesz.mariobros.screens.PlayScreen;
import com.nukesz.mariobros.sprites.Mario;

public class Mushroom extends Item {

    public Mushroom(final PlayScreen screen, final float x, final float y) {
        super(screen, x, y);
        setRegion(screen.getAtlas().findRegion("mushroom"), 0, 0, 16, 16);
        velocity = new Vector2(0.7f, 0);
    }

    @Override
    public void defineItem() {
        final BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY());
        bdef.type = BodyType.DynamicBody;
        body = world.createBody(bdef);

        final FixtureDef fdef = new FixtureDef();
        final CircleShape shape = new CircleShape();
        shape.setRadius(6 / MarioBros.PPM);
        fdef.filter.categoryBits = MarioBros.ITEM_BIT;
        fdef.filter.maskBits = MarioBros.MARIO_BIT | MarioBros.OBJECT_BIT | MarioBros.GROUND_BIT | MarioBros.COIN_BIT
                | MarioBros.BRICK_BIT;

        fdef.shape = shape;
        body.createFixture(fdef).setUserData(this);

    }

    @Override
    public void use(final Mario mario) {
        mario.grow();
        destroy();
    }

    @Override
    public void update(final float dt) {
        super.update(dt);
        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
        velocity.y = body.getLinearVelocity().y;
        body.setLinearVelocity(velocity);
    }

}
