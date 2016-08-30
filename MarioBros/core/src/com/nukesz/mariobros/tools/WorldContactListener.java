package com.nukesz.mariobros.tools;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.nukesz.mariobros.MarioBros;
import com.nukesz.mariobros.sprites.Mario;
import com.nukesz.mariobros.sprites.enemies.Enemy;
import com.nukesz.mariobros.sprites.items.Item;
import com.nukesz.mariobros.sprites.tileobjects.InteractiveTileObject;

public class WorldContactListener implements ContactListener {

	@Override
	public void beginContact(final Contact contact) {
		final Fixture fixA = contact.getFixtureA();
		final Fixture fixB = contact.getFixtureB();

		final int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;
		final Object userDataA = fixA.getUserData();
		final Object userDataB = fixB.getUserData();
		switch (cDef) {
			case MarioBros.MARIO_HEAD_BIT | MarioBros.BRICK_BIT:
			case MarioBros.MARIO_HEAD_BIT | MarioBros.COIN_BIT:
				if (fixA.getFilterData().categoryBits == MarioBros.MARIO_HEAD_BIT) {
					((InteractiveTileObject) userDataB).onHeadHit((Mario) userDataA);
				} else {
					((InteractiveTileObject) userDataA).onHeadHit((Mario) userDataB);
				}
				break;
			case MarioBros.ENEMY_HEAD_BIT | MarioBros.MARIO_BIT:
				if (fixA.getFilterData().categoryBits == MarioBros.ENEMY_HEAD_BIT) {
					((Enemy) userDataA).hitOnHead((Mario) userDataB);
				} else {
					((Enemy) userDataB).hitOnHead((Mario) userDataA);
				}
				break;
			case MarioBros.ENEMY_BIT | MarioBros.OBJECT_BIT:
				if (fixA.getFilterData().categoryBits == MarioBros.ENEMY_BIT) {
					((Enemy) userDataA).reverseVelocity(true, false);
				} else {
					((Enemy) userDataB).reverseVelocity(true, false);
				}
				break;
			case MarioBros.ENEMY_BIT | MarioBros.ENEMY_BIT:
				((Enemy) userDataA).onEnemyHit((Enemy) userDataB);
				((Enemy) userDataB).onEnemyHit((Enemy) userDataA);
				break;
			case MarioBros.MARIO_BIT | MarioBros.ENEMY_BIT:
				if (fixA.getFilterData().categoryBits == MarioBros.MARIO_BIT) {
					((Mario) userDataA).hit((Enemy) userDataB);
				} else {
					((Mario) userDataB).hit((Enemy) userDataA);
				}
				break;
			case MarioBros.ITEM_BIT | MarioBros.OBJECT_BIT:
				if (fixA.getFilterData().categoryBits == MarioBros.ITEM_BIT) {
					((Item) userDataA).reverseVelocity(true, false);
				} else {
					((Item) userDataB).reverseVelocity(true, false);
				}
				break;
			case MarioBros.ITEM_BIT | MarioBros.MARIO_BIT:
				if (fixA.getFilterData().categoryBits == MarioBros.ITEM_BIT) {
					((Item) userDataA).use((Mario) userDataB);
				} else {
					((Item) userDataB).use((Mario) userDataA);
				}
				break;
			default:
				break;
		}
	}

	@Override
	public void endContact(final Contact contact) {
	}

	@Override
	public void preSolve(final Contact contact, final Manifold oldManifold) {
	}

	@Override
	public void postSolve(final Contact contact, final ContactImpulse impulse) {
	}

}
