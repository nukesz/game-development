package com.nukesz.game.candy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Grid {

	private final Cell[][] grid;
	private final int numRows;
	private final int numCols;

	public Grid(final int numRows, final int numCols) {
		this.numRows = numRows;
		this.numCols = numCols;
		Gdx.app.log("", numRows + ", " + numCols);
		grid = new Cell[numRows][numCols];
		for (int i = 0; i < numRows; i++) {
			grid[i] = new Cell[numCols];
			for (int j = 0; j < numCols; j++) {
				grid[i][j] = new Cell(j * (Cell.SIZE + Cell.PADDING), i * (Cell.SIZE + Cell.PADDING));
			}
		}
	}

	public void render(final SpriteBatch batch) {
		for (final Cell[] cellss : grid) {
			for (final Cell cell : cellss) {
				cell.render(batch);
			}
		}
	}

	public void click(final float x, final float y) {
		Gdx.app.log("T", String.format("[%f,%f]", x, y));
		for (int row = 0; row < numRows; row++) {
			for (int col = 0; col < numCols; col++) {
				final Cell cell = grid[row][col];
				if (cell.contains(x, y)) {
					cell.changeColor();
					break;
				}
			}
		}
	}

	public void swap(final Vector2 lastTouch, final Vector2 newTouch) {
		final Cell cell1 = findCell(lastTouch);
		final Cell cell2 = findCell(newTouch);
		final Color colorOfCell2 = cell2.getColor();
		cell2.setColor(cell1.getColor());
		cell1.setColor(colorOfCell2);
	}

	private Cell findCell(final Vector2 position) {
		Cell cell = null;
		for (int row = 0; row < numRows; row++) {
			for (int col = 0; col < numCols; col++) {
				cell = grid[row][col];
				if (cell.contains(position.x, position.y)) {
					return cell;
				}
			}
		}
		return cell;
	}

}
