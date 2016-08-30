package com.nukesz.mariobros.sprites.tileobjects;

import static com.nukesz.mariobros.MarioBros.PPM;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.nukesz.mariobros.MarioBros;
import com.nukesz.mariobros.screens.PlayScreen;
import com.nukesz.mariobros.sprites.Mario;

public abstract class InteractiveTileObject {
	protected final MapObject object;
	protected final World world;
	protected final TiledMap map;
	protected final AssetManager assetManager;
	protected final PlayScreen screen;
	protected final Fixture fixture;
	protected TiledMapTile tile;
	protected Rectangle bounds;
	protected Body body;

	public InteractiveTileObject(final PlayScreen screen, final MapObject object) {
		this.object = object;
		this.screen = screen;
		this.world = screen.getWorld();
		this.map = screen.getMap();
		this.assetManager = screen.getAssetManager();
		this.bounds = ((RectangleMapObject) object).getRectangle();

		final BodyDef bdef = new BodyDef();
		final FixtureDef fdef = new FixtureDef();
		final PolygonShape shape = new PolygonShape();

		bdef.type = BodyDef.BodyType.StaticBody;
		bdef.position.set((bounds.getX() + bounds.getWidth() / 2) / PPM,
				(bounds.getY() + bounds.getHeight() / 2) / PPM);
		body = world.createBody(bdef);
		shape.setAsBox(bounds.getWidth() / 2 / PPM, bounds.getHeight() / 2 / PPM);
		fdef.shape = shape;
		fixture = body.createFixture(fdef);
	}

	public abstract void onHeadHit(Mario mario);

	public void setCategoryFilter(final short filterBit) {
		final Filter filter = new Filter();
		filter.categoryBits = filterBit;
		fixture.setFilterData(filter);
	}

	public Cell getCell() {
		final TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(1);
		return layer.getCell((int) (body.getPosition().x * MarioBros.PPM / 16),
				(int) (body.getPosition().y * MarioBros.PPM / 16));
	}

}
