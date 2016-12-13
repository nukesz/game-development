package com.nukesz.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

import java.awt.*;

public class GameScreen extends ScreenAdapter {

    private static final int RIGHT = 0;
    private static final int LEFT = 1;
    private static final int UP = 2;
    private static final int DOWN = 3;
    private int snakeDirection = UP;

    private static final float MOVE_TIME = 0.2F;
    private static final int SNAKE_MOVEMENT = 32;
    private float timer = MOVE_TIME;
    private int snakeX = 0;
    private int snakeY = 0;

    private SpriteBatch batch;
    private Texture snakeHead;
    private Texture snakeBody;
    private Texture apple;
    private boolean appleAvailable = false;
    private int appleX;
    private int appleY;
    private Array<BodyPart> bodyParts = new Array<BodyPart>();

    @Override
    public void show() {
        batch = new SpriteBatch();
        snakeHead = new Texture(Gdx.files.internal("snakehead_resized.png"));
        snakeBody = new Texture(Gdx.files.internal("snakebody.png"));
        apple = new Texture(Gdx.files.internal("apple.png"));
    }

    @Override
    public void render(float delta) {
        queryInput();
        timer -= delta;
        if (timer <= 0) {
            timer = MOVE_TIME;
            moveSnake();
            checkForOutOfBounds();
        }
        checkAppleCollision();
        checkAndPlaceApple();
        clearScreen();
        batch.begin();
        draw();
    }

    private void draw() {
        batch.draw(snakeHead, snakeX, snakeY);
        if (appleAvailable) {
            batch.draw(apple, appleX, appleY);
        }
        batch.end();
    }

    private void clearScreen() {
        Gdx.gl.glClearColor(Color.BLACK.getRed(), Color.BLACK.getGreen(), Color.BLACK.getBlue(), Color.BLACK.getAlpha());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    private void checkAppleCollision() {
        if (appleAvailable && appleX == snakeX && appleY == snakeY) {
            appleAvailable = false;
        }
    }

    private void checkAndPlaceApple() {
        if (!appleAvailable) {
            do {
                appleX = MathUtils.random(Gdx.graphics.getWidth()
                        / SNAKE_MOVEMENT - 1) * SNAKE_MOVEMENT;
                appleY = MathUtils.random(Gdx.graphics.getHeight()
                        / SNAKE_MOVEMENT - 1) * SNAKE_MOVEMENT;
                appleAvailable = true;
            } while (appleX == snakeX && appleY == snakeY);
        }
    }

    private void checkForOutOfBounds() {
        if (snakeX >= Gdx.graphics.getWidth()) {
            snakeX = 0;
        }

        if (snakeX < 0) {
            snakeX = Gdx.graphics.getWidth() - SNAKE_MOVEMENT;
        }

        if (snakeY >= Gdx.graphics.getHeight()) {
            snakeY = 0;
        }

        if (snakeY < 0) {
            snakeY = Gdx.graphics.getHeight() - SNAKE_MOVEMENT;
        }
    }

    private void moveSnake() {
        switch (snakeDirection) {
            case RIGHT: {
                snakeX += SNAKE_MOVEMENT;
                break;
            }
            case LEFT: {
                snakeX -= SNAKE_MOVEMENT;
                break;
            }
            case UP: {
                snakeY += SNAKE_MOVEMENT;
                break;
            }
            case DOWN: {
                snakeY -= SNAKE_MOVEMENT;
                break;
            }
        }
    }

    private void queryInput() {
        boolean lPressed = Gdx.input.isKeyPressed(Input.Keys.LEFT);
        boolean rPressed = Gdx.input.isKeyPressed(Input.Keys.RIGHT);
        boolean uPressed = Gdx.input.isKeyPressed(Input.Keys.UP);
        boolean dPressed = Gdx.input.isKeyPressed(Input.Keys.DOWN);

        if (lPressed) snakeDirection = LEFT;
        if (rPressed) snakeDirection = RIGHT;
        if (uPressed) snakeDirection = UP;
        if (dPressed) snakeDirection = DOWN;
    }
}
