package com.nukesz.mariobros.sprites.enemies;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.nukesz.mariobros.MarioBros;
import com.nukesz.mariobros.screens.PlayScreen;
import com.nukesz.mariobros.sprites.Mario;

public class Turtle extends Enemy {

	public static final int KICK_LEFT_SPEED = -2;
	public static final int KICK_RIGHT_SPEED = 2;

	public enum State {
		WALKING, STANDING_SHELL, MOVING_SHELL, DEAD
	}

	public State currentState = State.WALKING;
	public State previousState = State.WALKING;

	private float stateTime;
	private final Animation walkAnimation;
	private final Array<TextureRegion> frames = new Array<>();;
	private final TextureRegion shell;
	private float deadRotationDegress = 0;
	private boolean destroyed = false;
	private final AtlasRegion region;

	public Turtle(final PlayScreen screen, final float x, final float y) {
		super(screen, x, y);
		region = screen.getAtlas().findRegion("turtle");
		frames.add(new TextureRegion(region, 0, 0, 16, 24));
		frames.add(new TextureRegion(region, 16, 0, 16, 24));
		shell = new TextureRegion(region, 64, 0, 16, 24);
		walkAnimation = new Animation(0.2f, frames);
		setBounds(getX(), getY(), 16 / MarioBros.PPM, 24 / MarioBros.PPM);
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

		final PolygonShape head = new PolygonShape();
		final Vector2[] vertice = new Vector2[4];
		vertice[0] = new Vector2(-5, 8).scl(1 / MarioBros.PPM);
		vertice[1] = new Vector2(5, 8).scl(1 / MarioBros.PPM);
		vertice[2] = new Vector2(-3, 3).scl(1 / MarioBros.PPM);
		vertice[3] = new Vector2(3, 3).scl(1 / MarioBros.PPM);
		head.set(vertice);

		fdef.shape = head;
		fdef.restitution = 1.5f;
		fdef.filter.categoryBits = MarioBros.ENEMY_HEAD_BIT;
		b2body.createFixture(fdef).setUserData(this);
	}

	@Override
	public void update(final float dt) {
		setRegion(getFrame(dt));
		if (currentState == State.STANDING_SHELL && stateTime > 5) {
			currentState = State.WALKING;
			velocity.x = 1;
		}

		setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - 8 / MarioBros.PPM);

		if (currentState == State.DEAD) {
			deadRotationDegress += 3;
			rotate(deadRotationDegress);
			if (stateTime > 5 && !destroyed) {
				world.destroyBody(b2body);
				destroyed = true;
			}
		} else {
			b2body.setLinearVelocity(velocity);
		}

	}

	private TextureRegion getFrame(final float dt) {
		TextureRegion region;
		switch (currentState) {
			case STANDING_SHELL:
			case MOVING_SHELL:
				region = shell;
				break;
			case WALKING:
			default:
				region = walkAnimation.getKeyFrame(stateTime, true);
				break;
		}

		if (velocity.x > 0 && !region.isFlipX()) {
			region.flip(true, false);
		}
		if (velocity.x < 0 && region.isFlipX()) {
			region.flip(true, false);
		}

		stateTime = currentState == previousState ? stateTime + dt : 0;
		previousState = currentState;
		return region;
	}

	@Override
	public void hitOnHead(final Mario mario) {
		if (currentState != State.STANDING_SHELL) {
			currentState = State.STANDING_SHELL;
			velocity.x = 0;
		} else {
			kick(mario.getX() <= this.getX() ? KICK_RIGHT_SPEED : KICK_LEFT_SPEED);
		}

	}

	public void kick(final int speed) {
		velocity.x = speed;
		currentState = State.MOVING_SHELL;
	}

	public State getCurrentState() {
		return currentState;
	}

	public void killed() {
		currentState = State.DEAD;
		final Filter filter = new Filter();
		filter.maskBits = MarioBros.NOTHING_BIT;

		for (final Fixture fixture : b2body.getFixtureList()) {
			fixture.setFilterData(filter);
		}

		b2body.applyLinearImpulse(new Vector2(0, 5f), b2body.getWorldCenter(), true);
	}

	@Override
	public void onEnemyHit(final Enemy enemy) {
		if (enemy instanceof Turtle) {
			final Turtle turtle = (Turtle) enemy;
			if (turtle.currentState == State.MOVING_SHELL && currentState != State.MOVING_SHELL) {
				killed();
			} else if (currentState == State.MOVING_SHELL && turtle.currentState == State.WALKING) {
				return;
			} else {
				reverseVelocity(true, false);
			}
		} else if (currentState != State.MOVING_SHELL) {
			reverseVelocity(true, false);
		}
	}

	@Override
	public void draw(final Batch batch) {
		if (!destroyed) {
			super.draw(batch);
		}
	}
}
