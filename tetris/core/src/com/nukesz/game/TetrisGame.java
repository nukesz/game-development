package com.nukesz.game;

import com.badlogic.gdx.Game;
import com.nukesz.game.scene.GameScreen;

public class TetrisGame extends Game {

    @Override
    public void create() {
        setScreen(new GameScreen());
    }
}
