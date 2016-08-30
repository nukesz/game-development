package com.nukesz.game.candy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

public abstract class State implements InputProcessor {

    protected GameStateManager gsm;
    protected OrthographicCamera cam;
    protected Vector3 m;

    protected State(final GameStateManager gsm) {
        this.gsm = gsm;
        cam = new OrthographicCamera();
        cam.setToOrtho(false, Game.WIDTH, Game.HEIGHT);
        m = new Vector3();
    }

    public abstract void update(float dt);

    public abstract void render(SpriteBatch sb);

    protected void unproject(final Vector3 m, final OrthographicCamera cam) {
        m.x = Gdx.input.getX();
        m.y = Gdx.input.getY();
        cam.unproject(m);
    }

    @Override
    public boolean keyDown(final int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(final char character) {
        return false;
    }

    @Override
    public boolean keyUp(final int keycode) {
        return false;
    }

    @Override
    public boolean mouseMoved(final int screenX, final int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(final int amount) {
        return false;
    }

    @Override
    public boolean touchDown(final int screenX, final int screenY, final int pointer, final int button) {
        return false;
    }

    @Override
    public boolean touchDragged(final int screenX, final int screenY, final int pointer) {
        return false;
    }

    @Override
    public boolean touchUp(final int screenX, final int screenY, final int pointer, final int button) {
        return false;
    }

}
