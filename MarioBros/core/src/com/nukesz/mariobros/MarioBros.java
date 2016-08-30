package com.nukesz.mariobros;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.nukesz.mariobros.screens.PlayScreen;

public class MarioBros extends Game {

    public static final int V_WIDTH = 400;
    public static final int V_HEIGHT = 208;
	public static final float PPM = 100; // Pixels per meter

    public static final short NOTHING_BIT = 0;
    public static final short GROUND_BIT = 1;
    public static final short MARIO_BIT = 2;
    public static final short BRICK_BIT = 4;
    public static final short COIN_BIT = 8;
    public static final short DESTROYED_BIT = 16;
    public static final short OBJECT_BIT = 32;
    public static final short ENEMY_BIT = 64;
    public static final short ENEMY_HEAD_BIT = 128;
    public static final short ITEM_BIT = 256;
    public static final short MARIO_HEAD_BIT = 512;

    public SpriteBatch batch;

	private AssetManager assetManager;
	private final String[] sounds = { "coin", "bump", "breakblock", "powerup_spawn", "powerup", "powerdown", "stomp",
            "mariodie" };

    @Override
    public void create() {
        batch = new SpriteBatch();
        assetManager = new AssetManager();
        assetManager.load("audio/music/mario_music.ogg", Music.class);
        for (final String sound : sounds) {
            assetManager.load("audio/sounds/" + sound + ".wav", Sound.class);
        }
        assetManager.finishLoading();

        setScreen(new PlayScreen(this));
    }

    @Override
    public void dispose() {
        super.dispose();
        assetManager.dispose();
        batch.dispose();
    }

	public AssetManager getAssetManager() {
		return assetManager;
	}
}
