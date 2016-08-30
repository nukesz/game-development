package com.nukesz.mariobros.tools;

import static com.nukesz.mariobros.MarioBros.PPM;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.nukesz.mariobros.MarioBros;
import com.nukesz.mariobros.screens.PlayScreen;
import com.nukesz.mariobros.sprites.enemies.Enemy;
import com.nukesz.mariobros.sprites.enemies.Goomba;
import com.nukesz.mariobros.sprites.enemies.Turtle;
import com.nukesz.mariobros.sprites.tileobjects.Brick;
import com.nukesz.mariobros.sprites.tileobjects.Coin;

public class B2WorldCreator {

	private static final int TURTLES_INDEX = 7;
	private static final int GOOMBAS_INDEX = 6;
	private static final int BRICK_INDEX = 5;
	private static final int COIN_INDEX = 4;
	private static final int PIPE_INDEX = 3;
	private static final int GROUND_INDEX = 2;
	private final World world;
	private final TiledMap map;
	private final Array<Goomba> goombas = new Array<>();
	private final Array<Turtle> turtles = new Array<>();;

	public B2WorldCreator(final PlayScreen screen) {
		this.world = screen.getWorld();
		this.map = screen.getMap();

		for (final MapObject object : map.getLayers().get(GROUND_INDEX).getObjects()
				.getByType(RectangleMapObject.class)) {
			createBody(object, MarioBros.GROUND_BIT);
		}
		for (final MapObject object : map.getLayers().get(PIPE_INDEX).getObjects()
				.getByType(RectangleMapObject.class)) {
			createBody(object, MarioBros.OBJECT_BIT);
		}
		for (final MapObject object : map.getLayers().get(COIN_INDEX).getObjects()
				.getByType(RectangleMapObject.class)) {
			new Coin(screen, object);
		}
		for (final MapObject object : map.getLayers().get(BRICK_INDEX).getObjects()
				.getByType(RectangleMapObject.class)) {
			new Brick(screen, object);
		}

		for (final MapObject object : map.getLayers().get(GOOMBAS_INDEX).getObjects()
				.getByType(RectangleMapObject.class)) {
			final Rectangle rect = ((RectangleMapObject) object).getRectangle();
			goombas.add(new Goomba(screen, rect.getX() / MarioBros.PPM, rect.getY() / MarioBros.PPM));
		}

		for (final MapObject object : map.getLayers().get(TURTLES_INDEX).getObjects()
				.getByType(RectangleMapObject.class)) {
			final Rectangle rect = ((RectangleMapObject) object).getRectangle();
			turtles.add(new Turtle(screen, rect.getX() / MarioBros.PPM, rect.getY() / MarioBros.PPM));
		}
	}

	private void createBody(final MapObject object, final short categoryBit) {
		final BodyDef bdef = new BodyDef();
		bdef.type = BodyDef.BodyType.StaticBody;
		final Rectangle rect = ((RectangleMapObject) object).getRectangle();
		bdef.position.set((rect.getX() + rect.getWidth() / 2) / PPM, (rect.getY() + rect.getHeight() / 2) / PPM);
		final Body body = world.createBody(bdef);

		final PolygonShape shape = new PolygonShape();
		shape.setAsBox(rect.getWidth() / 2 / PPM, rect.getHeight() / 2 / PPM);
		final FixtureDef fdef = new FixtureDef();
		fdef.shape = shape;
		fdef.filter.categoryBits = categoryBit;
		body.createFixture(fdef);
	}

	public Array<Enemy> getEnemies() {
		final Array<Enemy> enemies = new Array<>();
		enemies.addAll(goombas);
		enemies.addAll(turtles);
		return enemies;
	}
}
