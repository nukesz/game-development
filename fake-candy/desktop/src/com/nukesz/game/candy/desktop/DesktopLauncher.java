package com.nukesz.game.candy.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.nukesz.game.candy.Game;

public class DesktopLauncher {
    public static void main(final String[] arg) {
        final LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = Game.TITLE;
        config.height = Game.HEIGHT;
        config.width = Game.WIDTH;
        new LwjglApplication(new Game(), config);
    }
}
