package com.nukesz.game.candy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

public class PlayState extends State {

	private final Grid grid;
	private Vector2 lastTouch;
	private final int DRAG_DIST = 50;
	private static TextureAtlas atlas;

	public PlayState(final GameStateManager gsm) {
		super(gsm);
		Gdx.input.setInputProcessor(this);
		loadAtlas("pack/pack.pack");
		grid = new Grid((int) (Game.HEIGHT / (Cell.SIZE + Cell.PADDING)),
				(int) (Game.WIDTH / (Cell.SIZE + Cell.PADDING)));
	}

	private static void loadAtlas(final String path) {
		atlas = new TextureAtlas(path);
		atlas.getTextures().first().setFilter(TextureFilter.Linear, TextureFilter.Linear);
	}

	public static TextureAtlas getAtlas() {
		return atlas;
	}

	@Override
	public void update(final float dt) {
	}

	@Override
	public void render(final SpriteBatch sb) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		sb.begin();
		sb.setProjectionMatrix(cam.combined);
		grid.render(sb);
		sb.end();
	}

	@Override
	public boolean touchDown(final int x, final int y, final int p, final int b) {
		unproject(m, cam);
		lastTouch = new Vector2(m.x, m.y);
		// grid.click(m.x, m.y);
		return true;
	}

	@Override
	public boolean touchUp(final int x, final int y, final int p, final int b) {
		unproject(m, cam);
		final float startx = lastTouch.x;
		final float starty = lastTouch.y;
		final Vector2 newTouch = new Vector2(m.x, m.y);
		grid.swap(lastTouch, newTouch);
		return true;
	}
}
