package com.nukesz.game.items;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.nukesz.game.scene.GameScreen;

public abstract class Item {

    private static final Color[] availableColors = new Color[] {Color.GREEN, Color.RED, Color.WHITE, Color.BLUE, Color.PINK};

    protected Rectangle collision = new Rectangle();
    protected boolean moving = true;
    protected Color color;

    public Item(int x, int y, int width, int height) {
        this.color = availableColors[MathUtils.random(availableColors.length - 1)];
        setCollision(x, y, width, height);
    }

    protected void setCollision(float x, float y, float width, float height) {
        collision.set(x, y, width, height);
    }

    private boolean doesCollideWithOtherItem(Array<Item> otherItems, Rectangle nextCollision) {
        for (Item other: otherItems) {
            if ((other.collision.x != collision.x || other.collision.y != collision.y)
                    && Intersector.overlaps(nextCollision, other.collision)) {
                return true;
            }
        }
        return false;
    }

    public void draw(ShapeRenderer shapeRenderer) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(color);
        shapeRenderer.rect(collision.x, collision.y, collision.width, collision.height);
        shapeRenderer.end();
    }

    public boolean isMoving() {
        return moving;
    }

    public void setMoving(boolean moving) {
        this.moving = moving;
    }

    public void moveLeft(Array<Item> otherItems) {
        Rectangle nextCollision = new Rectangle();
        nextCollision.set(collision.x - 16, collision.y, collision.width, collision.height);
        if (moving && collision.x > 0 && !doesCollideWithOtherItem(otherItems, nextCollision)) {
            collision = nextCollision;
        }
    }

    public void moveRight(Array<Item> otherItems) {
        Rectangle nextCollision = new Rectangle();
        nextCollision.set(collision.x + 16, collision.y, collision.width, collision.height);
        if (moving && collision.x + collision.width < GameScreen.WORLD_WIDTH && !doesCollideWithOtherItem(otherItems, nextCollision)) {
            collision = nextCollision;
        }
    }

    public void moveDown(Array<Item> otherItems) {
        Rectangle nextCollision = new Rectangle();
        nextCollision.set(collision.x, collision.y - 16, collision.width, collision.height);
        if (moving && nextCollision.y >= 0 && !doesCollideWithOtherItem(otherItems, nextCollision)) {
            collision = nextCollision;
        } else {
            setMoving(false);
        }
    }

    public abstract void rotate();
}
