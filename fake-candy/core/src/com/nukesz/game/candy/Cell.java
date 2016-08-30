package com.nukesz.game.candy;

import java.util.Random;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Cell {

	public static final float SIZE = 40;
	public static final int PADDING = 10;
	private final TextureRegion bg = PlayState.getAtlas().findRegion("grid_cell");
	private final float x;
	private final float y;
	private Color color;
	private final Color[] colors = { Color.GREEN, Color.BLUE, Color.CYAN };

	public Cell(final float x, final float y) {
		this.x = x;
		this.y = y;
		final Random rand = new Random();
		color = colors[rand.nextInt(3)];
	}

	public void render(final SpriteBatch batch) {
		batch.setColor(color);
		batch.draw(bg, x, y, SIZE, SIZE);
	}

	public boolean contains(final float mx, final float my) {
		return mx > x && mx < x + SIZE && my > y && my < y + SIZE;
	}

	public void changeColor() {
		final Random rand = new Random();
		final float r = rand.nextFloat();
		final float g = rand.nextFloat();
		final float b = rand.nextFloat();
		color = new Color(r, g, b, 1);
	}

	public Color getColor() {
		return color;
	}

	public void setColor(final Color color) {
		this.color = color;
	}
}
