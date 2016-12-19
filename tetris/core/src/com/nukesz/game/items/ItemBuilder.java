package com.nukesz.game.items;

import com.badlogic.gdx.math.MathUtils;
import com.nukesz.game.scene.GameScreen;

public class ItemBuilder {

    public static Item create(int x, int y) {
        if (MathUtils.random(1) == 0) {
            return new Line(x, y);
        } else {
            return new Square(x, y);
        }
    }
}
