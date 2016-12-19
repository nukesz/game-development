package com.nukesz.game.items;

import com.badlogic.gdx.Game;
import com.nukesz.game.scene.GameScreen;

public class Square extends Item {

    public Square(int x, int y) {
        super(x, y, GameScreen.GRID_CELL * 2, GameScreen.GRID_CELL * 2);
    }

    @Override
    public void rotate() {
        // Nothing to do
    }
}
