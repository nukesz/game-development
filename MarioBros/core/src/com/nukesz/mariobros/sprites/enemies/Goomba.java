package com.nukesz.mariobros.sprites.enemies;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.nukesz.mariobros.MarioBros;
import com.nukesz.mariobros.screens.PlayScreen;
import com.nukesz.mariobros.sprites.Mario;

public class Goomba extends Enemy {

	private static final int GOOMBA_SIZE = 16;
	private float stateTime = 0;
    private final Animation walkAnimation;
	private final Array<TextureRegion> frames = new Array<>();
    private boolean setToDestroy = false;
    private boolean destroyed = false;
	private final AtlasRegion region;

    public Goomba(final PlayScreen screen, final float x, final float y) {
        super(screen, x, y);
		region = screen.getAtlas().findRegion("goomba");
        for (int i = 0; i < 2; i++) {
			frames.add(new TextureRegion(region, i * GOOMBA_SIZE, 0, GOOMBA_SIZE, GOOMBA_SIZE));
        }
        walkAnimation = new Animation(0.4f, frames);
        setBounds(getX(), getY(), 16 / MarioBros.PPM, 16 / MarioBros.PPM);
    }

    @Override
    public void update(final float dt) {
        stateTime += dt;
        if (setToDestroy && !destroyed) {
            world.destroyBody(b2body);
            destroyed = true;
			setRegion(new TextureRegion(region, 2 * GOOMBA_SIZE, 0, GOOMBA_SIZE, GOOMBA_SIZE));
            stateTime = 0;
        } else if (!destroyed) {
            b2body.setLinearVelocity(velocity);
            setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
            setRegion(walkAnimation.getKeyFrame(stateTime, true));
        }
    }

    @Override
    protected void defineEnemy() {
        final BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY());
        bdef.type = BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        final FixtureDef fdef = new FixtureDef();
        final CircleShape shape = new CircleShape();
        shape.setRadius(6 / MarioBros.PPM);
        fdef.filter.categoryBits = MarioBros.ENEMY_BIT;
        fdef.filter.maskBits = MarioBros.GROUND_BIT | MarioBros.COIN_BIT | MarioBros.BRICK_BIT | MarioBros.ENEMY_BIT
                | MarioBros.OBJECT_BIT | MarioBros.MARIO_BIT;
        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);

        // Create the Head here:
        final PolygonShape head = new PolygonShape();
        final Vector2[] vertice = new Vector2[4];
        final float scalar = 1 / MarioBros.PPM;
		vertice[0] = new Vector2(-5, 8).scl(scalar);
        vertice[1] = new Vector2(5, 8).scl(scalar);
        vertice[2] = new Vector2(-3, 3).scl(scalar);
        vertice[3] = new Vector2(3, 3).scl(scalar);
        head.set(vertice);

        fdef.shape = head;
        fdef.restitution = 0.5f;
        fdef.filter.categoryBits = MarioBros.ENEMY_HEAD_BIT;
        b2body.createFixture(fdef).setUserData(this);
    }

    @Override
    public void draw(final Batch batch) {
        if (!destroyed || stateTime < 1) {
            super.draw(batch);
        }
    }

    @Override
    public void hitOnHead(final Mario mario) {
        setToDestroy = true;
		assetManager.get("audio/sounds/stomp.wav", Sound.class).play();
    }

    @Override
    public void onEnemyHit(final Enemy enemy) {
        if (enemy instanceof Turtle && ((Turtle) enemy).currentState == Turtle.State.MOVING_SHELL) {
            setToDestroy = true;
        } else {
            reverseVelocity(true, false);
        }
    }

}
