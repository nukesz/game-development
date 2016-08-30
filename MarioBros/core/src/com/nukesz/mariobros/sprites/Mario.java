package com.nukesz.mariobros.sprites;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.nukesz.mariobros.MarioBros;
import com.nukesz.mariobros.screens.PlayScreen;
import com.nukesz.mariobros.sprites.enemies.Enemy;
import com.nukesz.mariobros.sprites.enemies.Turtle;

public class Mario extends Sprite {
	public enum State {
		FALLING, JUMPING, STANDING, RUNNING, GROWING, DEAD
	}

	public State currentState = State.STANDING;
	public State previousState = State.STANDING;

	private final AssetManager assetManager;
	public World world;
	public Body b2body;
	public TextureRegion marioStand;
	private Animation marioRun;
	private final TextureRegion marioJump;
	private final TextureRegion marioDead;
	public TextureRegion bigMarioStand;
	public TextureRegion bigMarioJump;
	private Animation bigMarioRun;
	private Animation growMario;

	private float stateTimer = 0f;
	private boolean runningRight = true;
	private boolean marioIsBig;
	private boolean runGrowAnimation;
	private boolean timeToDefineMario;
	private boolean timeToRedefineMario;
	private boolean marioIsDead;
	private final AtlasRegion bigMarioRegion;
	private final AtlasRegion marioRegion;

	public Mario(final PlayScreen screen) {
		this.world = screen.getWorld();
		this.assetManager = screen.getAssetManager();
		this.bigMarioRegion = screen.getAtlas().findRegion("big_mario");
		this.marioRegion = screen.getAtlas().findRegion("little_mario");

		marioJump = new TextureRegion(marioRegion, 5 * 16, 0, 16, 16);
		marioStand = new TextureRegion(marioRegion, 0, 0, 16, 16);
		marioDead = new TextureRegion(marioRegion, 6 * 16, 0, 16, 16);
		bigMarioJump = new TextureRegion(bigMarioRegion, 5 * 16, 0, 16, 32);
		bigMarioStand = new TextureRegion(bigMarioRegion, 0, 0, 16, 32);
		animateMarioRun();
		animateMarioGrow();
		animateBigMarioRun();
		defineMario();
		setBounds(0, 0, 16 / MarioBros.PPM, 16 / MarioBros.PPM);
		setRegion(marioStand);
	}

	private void animateMarioRun() {
		final Array<TextureRegion> frames = new Array<>();
		for (int i = 1; i < 4; i++) {
			frames.add(new TextureRegion(marioRegion, i * 16, 0, 16, 16));
		}
		marioRun = new Animation(0.1f, frames);
	}

	private void animateBigMarioRun() {
		final Array<TextureRegion> frames = new Array<>();
		for (int i = 1; i < 4; i++) {
			frames.add(new TextureRegion(bigMarioRegion, i * 16, 0, 16, 32));
		}
		bigMarioRun = new Animation(0.1f, frames);
	}

	private void animateMarioGrow() {
		final Array<TextureRegion> frames = new Array<>();
		frames.add(new TextureRegion(bigMarioRegion, 240, 0, 16, 32));
		frames.add(new TextureRegion(bigMarioRegion, 0, 0, 16, 32));
		frames.add(new TextureRegion(bigMarioRegion, 240, 0, 16, 32));
		frames.add(new TextureRegion(bigMarioRegion, 0, 0, 16, 32));
		growMario = new Animation(0.2f, frames);
	}

	public void update(final float dt) {
		if (marioIsBig) {
			setPosition(b2body.getPosition().x - getWidth() / 2,
					b2body.getPosition().y - getHeight() / 2 - 6 / MarioBros.PPM);
		} else {
			setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
		}
		setRegion(getFrame(dt));
		if (timeToDefineMario) {
			defineBigMario();
		}
		if (timeToRedefineMario) {
			redefineMario();
		}
	}

	private TextureRegion getFrame(final float dt) {
		currentState = getState();
		TextureRegion region;
		switch (currentState) {
			case DEAD:
				region = marioDead;
				break;
			case GROWING:
				region = growMario.getKeyFrame(stateTimer, false);
				if (growMario.isAnimationFinished(stateTimer)) {
					runGrowAnimation = false;
				}
				break;
			case JUMPING:
				region = marioIsBig ? bigMarioJump : marioJump;
				break;
			case RUNNING:
				region = marioIsBig ? bigMarioRun.getKeyFrame(stateTimer, true)
						: marioRun.getKeyFrame(stateTimer, true);
				break;
			case FALLING:
			case STANDING:
			default:
				region = marioIsBig ? bigMarioStand : marioStand;
				break;
		}

		if ((b2body.getLinearVelocity().x < 0 || !runningRight) && !region.isFlipX()) {
			region.flip(true, false);
			runningRight = false;
		} else if ((b2body.getLinearVelocity().x > 0 || runningRight) && region.isFlipX()) {
			region.flip(true, false);
			runningRight = true;
		}
		stateTimer = currentState == previousState ? stateTimer + dt : 0;
		previousState = currentState;
		return region;
	}

	private State getState() {
		if (marioIsDead) {
			return State.DEAD;
		} else if (runGrowAnimation) {
			return State.GROWING;
		} else if (b2body.getLinearVelocity().y > 0
				|| b2body.getLinearVelocity().y < 0 && previousState == State.JUMPING) {
			return State.JUMPING;
		} else if (b2body.getLinearVelocity().y < 0) {
			return State.FALLING;
		} else if (b2body.getLinearVelocity().x != 0) {
			return State.RUNNING;
		} else {
			return State.STANDING;
		}
	}

	public void grow() {
		runGrowAnimation = true;
		marioIsBig = true;
		timeToDefineMario = true;
		setBounds(getX(), getY(), getWidth(), getHeight() * 2);
		assetManager.get("audio/sounds/powerup.wav", Sound.class).play();
	}

	public boolean isDead() {
		return marioIsDead;
	}

	public float getStateTimer() {
		return stateTimer;
	}

	public boolean isBig() {
		return marioIsBig;
	}

	public void hit(final Enemy enemy) {
		if (enemy instanceof Turtle && ((Turtle) enemy).getCurrentState() == Turtle.State.STANDING_SHELL) {
			((Turtle) enemy).kick(this.getX() <= enemy.getX() ? Turtle.KICK_RIGHT_SPEED : Turtle.KICK_LEFT_SPEED);
		} else if (marioIsBig) {
			marioIsBig = false;
			timeToRedefineMario = true;
			setBounds(getX(), getY(), getWidth(), getHeight() / 2);
			assetManager.get("audio/sounds/powerdown.wav", Sound.class).play();
		} else {
			assetManager.get("audio/music/mario_music.ogg", Music.class).stop();
			assetManager.get("audio/sounds/mariodie.wav", Sound.class).play();
			marioIsDead = true;
			final Filter filter = new Filter();
			filter.maskBits = MarioBros.NOTHING_BIT;
			for (final Fixture fixture : b2body.getFixtureList()) {
				fixture.setFilterData(filter);
			}
			b2body.applyLinearImpulse(new Vector2(0, 4f), b2body.getWorldCenter(), true);
		}

	}

	private void redefineMario() {
		final Vector2 position = b2body.getPosition();
		world.destroyBody(b2body);
		final BodyDef bdef = new BodyDef();
		bdef.position.set(position);
		setBodyDef(bdef);
		timeToRedefineMario = false;

	}

	private void defineMario() {
		final BodyDef bdef = new BodyDef();
		bdef.position.set(200 / MarioBros.PPM, 32 / MarioBros.PPM);
		setBodyDef(bdef);
	}

	private void setBodyDef(final BodyDef bdef) {
		bdef.type = BodyType.DynamicBody;
		b2body = world.createBody(bdef);
		final FixtureDef fdef = createFixtureDef();
		final CircleShape shape = new CircleShape();
		shape.setRadius(6 / MarioBros.PPM);
		fdef.shape = shape;
		b2body.createFixture(fdef).setUserData(this);
		final EdgeShape head = new EdgeShape();
		head.set(new Vector2(-2 / MarioBros.PPM, 6 / MarioBros.PPM), new Vector2(2 / MarioBros.PPM, 6 / MarioBros.PPM));
		fdef.filter.categoryBits = MarioBros.MARIO_HEAD_BIT;
		fdef.shape = head;
		fdef.isSensor = true;
		b2body.createFixture(fdef).setUserData(this);
	}

	private void defineBigMario() {
		final Vector2 currentPosition = b2body.getPosition();
		world.destroyBody(b2body);

		final BodyDef bdef = new BodyDef();
		bdef.position.set(currentPosition.add(0, 10 / MarioBros.PPM));
		bdef.type = BodyType.DynamicBody;
		b2body = world.createBody(bdef);
		final FixtureDef fdef = createFixtureDef();
		final CircleShape shape = new CircleShape();
		shape.setRadius(6 / MarioBros.PPM);
		fdef.shape = shape;
		shape.setPosition(new Vector2(0, -14 / MarioBros.PPM));
		b2body.createFixture(fdef).setUserData(this);

		final EdgeShape head = new EdgeShape();
		head.set(new Vector2(-2 / MarioBros.PPM, 6 / MarioBros.PPM), new Vector2(2 / MarioBros.PPM, 6 / MarioBros.PPM));
		fdef.filter.categoryBits = MarioBros.MARIO_HEAD_BIT;
		fdef.shape = head;
		fdef.isSensor = true;
		b2body.createFixture(fdef).setUserData(this);
		timeToDefineMario = false;

	}

	private FixtureDef createFixtureDef() {
		final FixtureDef fdef = new FixtureDef();
		fdef.filter.categoryBits = MarioBros.MARIO_BIT;
		fdef.filter.maskBits = MarioBros.GROUND_BIT | MarioBros.COIN_BIT | MarioBros.BRICK_BIT | MarioBros.ENEMY_BIT
				| MarioBros.OBJECT_BIT | MarioBros.ENEMY_HEAD_BIT | MarioBros.ITEM_BIT;
		return fdef;
	}
}
