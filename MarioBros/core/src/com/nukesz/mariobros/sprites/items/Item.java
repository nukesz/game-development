package com.nukesz.mariobros.sprites.items;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.nukesz.mariobros.MarioBros;
import com.nukesz.mariobros.screens.PlayScreen;
import com.nukesz.mariobros.sprites.Mario;

public abstract class Item extends Sprite {
	protected PlayScreen screen;
	protected World world;
	protected Vector2 velocity;
	protected boolean toDestroy;
	protected boolean destroyed;
	protected Body body;

	public Item(final PlayScreen screen, final float x, final float y) {
		this.screen = screen;
		this.world = screen.getWorld();
		setPosition(x, y);
		setBounds(getX(), getY(), 16 / MarioBros.PPM, 16 / MarioBros.PPM);
		defineItem();
		toDestroy = false;
		destroyed = false;
	}

	public abstract void defineItem();

	public abstract void use(Mario mario);

	public void update(final float dt) {
		if (toDestroy && !destroyed) {
			world.destroyBody(body);
			destroyed = true;
		}
	}

	@Override
	public void draw(final Batch batch) {
		if (!destroyed) {
			super.draw(batch);
		}
	}

	public void destroy() {
		toDestroy = true;
	}

	public void reverseVelocity(final boolean x, final boolean y) {
		if (x) {
			velocity.x = -velocity.x;
		}
		if (y) {
			velocity.y = -velocity.y;
		}
	}
}
