package com.nukesz.game.candy;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Game extends ApplicationAdapter {

    public static final String TITLE = "Fake Candy";
    public static int WIDTH = 450;
    public static int HEIGHT = 800;

    private SpriteBatch sb;
    private GameStateManager gsm;

    @Override
    public void create() {
        Gdx.gl.glClearColor(1f, 0.99f, 0.98f, 1);
        sb = new SpriteBatch();
        gsm = new GameStateManager();
        gsm.push(new PlayState(gsm));

    }

    @Override
    public void render() {
        gsm.update(Gdx.graphics.getDeltaTime());
        gsm.render(sb);
    }

}