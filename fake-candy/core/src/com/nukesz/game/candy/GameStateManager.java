package com.nukesz.game.candy;

import java.util.LinkedList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GameStateManager {

    private final LinkedList<State> states;

    public GameStateManager() {
        states = new LinkedList<State>();
    }

    public void update(final float dt) {
        states.peek().update(dt);
    }

    public void render(final SpriteBatch sb) {
        states.peek().render(sb);
    }

    public void pop() {
        states.pop();
    }

    public void push(final State s) {
        states.push(s);
    }

    public void set(final State s) {
        states.pop();
        states.push(s);
    }

}
