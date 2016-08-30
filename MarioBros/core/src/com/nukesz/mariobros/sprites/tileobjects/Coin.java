package com.nukesz.mariobros.sprites.tileobjects;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.math.Vector2;
import com.nukesz.mariobros.MarioBros;
import com.nukesz.mariobros.scenes.Hud;
import com.nukesz.mariobros.screens.PlayScreen;
import com.nukesz.mariobros.sprites.Mario;
import com.nukesz.mariobros.sprites.items.ItemDef;
import com.nukesz.mariobros.sprites.items.Mushroom;

public class Coin extends InteractiveTileObject {

	private static final int BLACK_COIN = 28;
	private final TiledMapTileSet tileSet;

	public Coin(final PlayScreen screen, final MapObject object) {
		super(screen, object);
		tileSet = map.getTileSets().getTileSet("tileset_gutter");
		fixture.setUserData(this);
		setCategoryFilter(MarioBros.COIN_BIT);
	}

	@Override
	public void onHeadHit(final Mario mario) {
		if (getCell().getTile().getId() == BLACK_COIN) {
			assetManager.get("audio/sounds/bump.wav", Sound.class).play();
		} else {
			if (object.getProperties().containsKey("mushroom")) {
				screen.spawnItem(new ItemDef(
						new Vector2(body.getPosition().x, body.getPosition().y + 16 / MarioBros.PPM), Mushroom.class));
				assetManager.get("audio/sounds/powerup_spawn.wav", Sound.class).play();
			} else {
				assetManager.get("audio/sounds/coin.wav", Sound.class).play();
			}
		}
		getCell().setTile(tileSet.getTile(BLACK_COIN));
		Hud.addScore(100);
	}

}
