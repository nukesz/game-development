package com.nukesz.game;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class Catapult {

    private static final float UNITS_PER_METER = 32F;
    private static final float MAX_STRENGTH = 15;
    private static final float MAX_DISTANCE = 100;
    private static final float UPPER_ANGLE = 3 * MathUtils.PI / 2f;
    private static final float LOWER_ANGLE = MathUtils.PI / 2f;
    private final Vector2 anchor = new Vector2(convertMetresToUnits(3), convertMetresToUnits(6));
    private final Vector2 firingPosition = anchor.cpy();
    private float distance;
    private float angle;
    private FitViewport viewport;
    private World world;

    public Catapult(FitViewport viewport, World world) {
        this.viewport = viewport;
        this.world = world;
    }

    public void calculateAngleAndDistanceForBullet(int screenX, int screenY) {
        firingPosition.set(screenX, screenY);
        viewport.unproject(firingPosition);
        distance = distanceBetweenTwoPoints();
        angle = angleBetweenTwoPoints();
        if (distance > MAX_DISTANCE) {
            distance = MAX_DISTANCE;
        }
        if (angle > LOWER_ANGLE) {
            if (angle > UPPER_ANGLE) {
                angle = 0;
            } else {
                angle = LOWER_ANGLE;
            }
        }
        firingPosition.set(anchor.x + (distance * -
                MathUtils.cos(angle)), anchor.y + (distance * -
                MathUtils.sin(angle)));
    }

    private float convertUnitsToMetres(float pixels) {
        return pixels / UNITS_PER_METER;
    }
    private float convertMetresToUnits(float metres) {
        return metres * UNITS_PER_METER;
    }

    private float angleBetweenTwoPoints() {
        float angle = MathUtils.atan2(anchor.y - firingPosition.y, anchor.x - firingPosition.x);
        angle %= 2 * MathUtils.PI;
        if (angle < 0) {
            angle += 2 * MathUtils.PI2;
        }
        return angle;
    }

    private float distanceBetweenTwoPoints() {
        return (float) Math.sqrt(((anchor.x - firingPosition.x) * (anchor.x - firingPosition.x))
                + ((anchor.y - firingPosition.y) * (anchor.y - firingPosition.y)));
    }

    public void setFiringPosition() {
        firingPosition.set(anchor.cpy());
    }

    public void createBullet() {
        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(0.5f);
        circleShape.setPosition(new
                Vector2(convertUnitsToMetres(firingPosition.x),
                convertUnitsToMetres(firingPosition.y)));
        BodyDef bd = new BodyDef();
        bd.type = BodyDef.BodyType.DynamicBody;
        Body bullet = world.createBody(bd);
        bullet.createFixture(circleShape, 1);
        circleShape.dispose();
        float velX = Math.abs( (MAX_STRENGTH * -MathUtils.cos(angle) *
                (distance / 100f)));
        float velY = Math.abs( (MAX_STRENGTH * -MathUtils.sin(angle) *
                (distance / 100f)));
        bullet.setLinearVelocity(velX, velY);
    }

    public void drawDebug(ShapeRenderer shapeRenderer) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.rect(anchor.x - 5, anchor.y - 5, 10, 10);
        shapeRenderer.rect(firingPosition.x - 5, firingPosition.y - 5, 10, 10);
        shapeRenderer.line(anchor.x, anchor.y, firingPosition.x, firingPosition.y);
        shapeRenderer.end();
    }
}
