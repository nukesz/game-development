package com.nukesz.mariobros.sprites.enemies;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.nukesz.mariobros.screens.PlayScreen;
import com.nukesz.mariobros.sprites.Mario;

public abstract class Enemy extends Sprite {

    protected final PlayScreen screen;
    protected final World world;
	protected final AssetManager assetManager;
    public Body b2body;
    public Vector2 velocity;

    public Enemy(final PlayScreen screen, final float x, final float y) {
        this.screen = screen;
        this.world = screen.getWorld();
		this.assetManager = screen.getAssetManager();
        setPosition(x, y);
        defineEnemy();
        velocity = new Vector2(-1, -2);
        b2body.setActive(false);
    }

    protected abstract void defineEnemy();

    public abstract void hitOnHead(Mario mario);

    public abstract void update(final float dt);

    public abstract void onEnemyHit(final Enemy enemy);

    public void reverseVelocity(final boolean x, final boolean y) {
        if (x) {
            velocity.x = -velocity.x;
        }
        if (y) {
            velocity.y = -velocity.y;
        }
    }

}
