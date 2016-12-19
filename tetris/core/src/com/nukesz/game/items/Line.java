package com.nukesz.game.items;

import com.nukesz.game.scene.GameScreen;

public class Line extends Item {

    public Line(int x, int y) {
        super(x, y, GameScreen.GRID_CELL * 4, GameScreen.GRID_CELL);
    }

    @Override
    public void rotate() {
        if (!isMoving()) {
            return;
        }

        if (collision.width > collision.height) {
            setCollision(collision.x + GameScreen.GRID_CELL * 2, collision.y + GameScreen.GRID_CELL, collision.height, collision.width);
        } else {
            setCollision(Math.min(GameScreen.WORLD_WIDTH - GameScreen.GRID_CELL * 4, Math.max(0, collision.x - GameScreen.GRID_CELL * 2)), collision.y, collision.height, collision.width);
        }
    }
}
