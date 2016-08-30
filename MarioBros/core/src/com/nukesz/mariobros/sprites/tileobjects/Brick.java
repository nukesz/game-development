package com.nukesz.mariobros.sprites.tileobjects;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.MapObject;
import com.nukesz.mariobros.MarioBros;
import com.nukesz.mariobros.scenes.Hud;
import com.nukesz.mariobros.screens.PlayScreen;
import com.nukesz.mariobros.sprites.Mario;

public class Brick extends InteractiveTileObject {

	public Brick(final PlayScreen screen, final MapObject object) {
		super(screen, object);
		fixture.setUserData(this);
		setCategoryFilter(MarioBros.BRICK_BIT);
	}

	@Override
	public void onHeadHit(final Mario mario) {
		if (mario.isBig()) {
			setCategoryFilter(MarioBros.DESTROYED_BIT);
			getCell().setTile(null);
			Hud.addScore(200);
			assetManager.get("audio/sounds/breakblock.wav", Sound.class).play();
		} else {
			assetManager.get("audio/sounds/bump.wav", Sound.class).play();
		}
	}

}
