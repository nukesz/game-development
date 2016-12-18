package com.nukesz.game.items;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

public class Item {

    private static final Color[] availableColors = new Color[] {Color.GREEN, Color.RED, Color.WHITE, Color.BLUE, Color.PINK};

    private int x;
    private int y;
    private int width;
    private int height;
    private Rectangle collision = new Rectangle();
    private boolean moving = true;
    private Color color;

    public Item(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.color = availableColors[MathUtils.random(availableColors.length - 1)];
        collision.set(x, y, width, height);
    }

    public void move(Array<Item> otherItems) {
        Rectangle nextCollision = new Rectangle();
        nextCollision.set(collision.x, collision.y - 16, collision.width, collision.height);
        if (moving && nextCollision.y >= 0 && !doesCollideWithOtherItem(otherItems, nextCollision)) {
            collision = nextCollision;
            y -= 16;
        } else {
            setMoving(false);
        }
    }

    private boolean doesCollideWithOtherItem(Array<Item> otherItems, Rectangle nextCollision) {
        for (Item other: otherItems) {
            if ((other.x != x || other.y != y) && Intersector.overlaps(nextCollision, other.collision)) {
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
}
