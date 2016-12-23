package com.nukesz.game;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class TiledObjectBodyBuilder {
    private static final float PIXELS_PER_TILE = 32F;
    private static final float HALF = 0.5F;

    public static void buildBuildingBodies(TiledMap tiledMap, World world) {
        MapObjects objects = tiledMap.getLayers().get("Buildings").getObjects();
        for (MapObject object : objects) {
            PolygonShape rectangle = getRectangle((RectangleMapObject) object);
            BodyDef bd = new BodyDef();
            bd.type = BodyDef.BodyType.DynamicBody;
            Body body = world.createBody(bd);
            body.createFixture(rectangle, 1);
            rectangle.dispose();
        }
    }

    public static void buildFloorBodies(TiledMap tiledMap, World world) {
        MapObjects objects = tiledMap.getLayers().get("Floor").getObjects();
        for (MapObject object : objects) {
            PolygonShape rectangle = getRectangle((RectangleMapObject) object);
            BodyDef bd = new BodyDef();
            bd.type = BodyDef.BodyType.StaticBody;
            Body body = world.createBody(bd);
            body.createFixture(rectangle, 1);
            rectangle.dispose();
        }
    }

    private static PolygonShape getRectangle(RectangleMapObject rectangleObject) {
        Rectangle rectangle = rectangleObject.getRectangle();
        PolygonShape polygon = new PolygonShape();
        Vector2 size = new Vector2(
                (rectangle.x + rectangle.width * HALF) / PIXELS_PER_TILE,
                (rectangle.y + rectangle.height * HALF) / PIXELS_PER_TILE
        );
        polygon.setAsBox(
                rectangle.width * HALF / PIXELS_PER_TILE,
                rectangle.height * HALF / PIXELS_PER_TILE,
                size,
                0.0f);
        return polygon;
    }

}
