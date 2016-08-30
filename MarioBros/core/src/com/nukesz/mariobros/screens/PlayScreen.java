package com.nukesz.mariobros.screens;

import static com.nukesz.mariobros.MarioBros.PPM;

import java.util.concurrent.LinkedBlockingDeque;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.nukesz.mariobros.MarioBros;
import com.nukesz.mariobros.scenes.Hud;
import com.nukesz.mariobros.sprites.Mario;
import com.nukesz.mariobros.sprites.enemies.Enemy;
import com.nukesz.mariobros.sprites.items.Item;
import com.nukesz.mariobros.sprites.items.ItemDef;
import com.nukesz.mariobros.sprites.items.Mushroom;
import com.nukesz.mariobros.tools.B2WorldCreator;
import com.nukesz.mariobros.tools.WorldContactListener;

public class PlayScreen implements Screen {

    private final MarioBros game;
	private final AssetManager assetManager;
    private final TextureAtlas atlas;

    private final OrthographicCamera gamecam;
    private final Viewport gamePort;
    private final Hud hud;

    private final TmxMapLoader mapLoader;
    private final TiledMap map;
    private final OrthogonalTiledMapRenderer renderer;

    private final World world;
    private final Box2DDebugRenderer b2dr;
    private final B2WorldCreator creator;

    private final Mario player;

    private final Music music;

    private final Array<Item> items;
    private final LinkedBlockingDeque<ItemDef> itemsToSpawn;

    public PlayScreen(final MarioBros game) {
        this.game = game;
		this.assetManager = game.getAssetManager();
        atlas = new TextureAtlas("Mario_and_Enemies.pack");
        gamecam = new OrthographicCamera();
        gamePort = new FitViewport(MarioBros.V_WIDTH / PPM, MarioBros.V_HEIGHT / PPM, gamecam);
        hud = new Hud(game.batch);
        mapLoader = new TmxMapLoader();
        map = mapLoader.load("level1.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1 / PPM);
        gamecam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);

        world = new World(new Vector2(0, -9.8f), true);
        b2dr = new Box2DDebugRenderer();

        creator = new B2WorldCreator(this);
        player = new Mario(this);

        world.setContactListener(new WorldContactListener());

		music = game.getAssetManager().get("audio/music/mario_music.ogg", Music.class);
        music.setLooping(true);
        music.play();

        items = new Array<>();
        itemsToSpawn = new LinkedBlockingDeque<>();
    }

    public void spawnItem(final ItemDef idef) {
        itemsToSpawn.add(idef);
    }

    public void handleSpawningItems() {
        if (!itemsToSpawn.isEmpty()) {
            final ItemDef idef = itemsToSpawn.pop();
            if (idef.type == Mushroom.class) {
                items.add(new Mushroom(this, idef.position.x, idef.position.y));
            }
        }
    }

    public TextureAtlas getAtlas() {
        return atlas;
    }

    @Override
    public void show() {

    }

    public void update(final float dt) {
        handleInput(dt);
        handleSpawningItems();

        world.step(1 / 60f, 6, 2);

        player.update(dt);
        for (final Enemy enemy : creator.getEnemies()) {
            enemy.update(dt);
            if (enemy.getX() < player.getX() + 224 / MarioBros.PPM) {
                enemy.b2body.setActive(true);
            }
        }

        for (final Item item : items) {
            item.update(dt);
        }

        hud.update(dt);

        if (player.currentState != Mario.State.DEAD) {
            gamecam.position.x = player.b2body.getPosition().x;
        }

        gamecam.update();
        renderer.setView(gamecam);
    }

    private void handleInput(final float dt) {
        if (player.currentState != Mario.State.DEAD) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
                player.b2body.applyLinearImpulse(new Vector2(0, 4f), player.b2body.getWorldCenter(), true);
            }

            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && player.b2body.getLinearVelocity().x <= 2) {
                player.b2body.applyLinearImpulse(new Vector2(0.1f, 0), player.b2body.getWorldCenter(), true);
            }

            if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && player.b2body.getLinearVelocity().x >= -2) {
                player.b2body.applyLinearImpulse(new Vector2(-0.1f, 0), player.b2body.getWorldCenter(), true);
            }
        }
    }

    @Override
    public void render(final float delta) {
        update(delta);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        renderer.render();
        b2dr.render(world, gamecam.combined);

        game.batch.setProjectionMatrix(gamecam.combined);
        game.batch.begin();
        player.draw(game.batch);
        for (final Enemy enemy : creator.getEnemies()) {
            enemy.draw(game.batch);
        }
        for (final Item item : items) {
            item.draw(game.batch);
        }
        game.batch.end();

        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();

        if (gameOver()) {
            game.setScreen(new GameOverScreen(game));
            dispose();
        }
    }

    public boolean gameOver() {
        return (player.currentState == Mario.State.DEAD && player.getStateTimer() > 3);
    }

    @Override
    public void resize(final int width, final int height) {
        gamePort.update(width, height);
    }

    public TiledMap getMap() {
        return map;
    }

    public World getWorld() {
        return world;
    }

	public AssetManager getAssetManager() {
		return assetManager;
	}

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        map.dispose();
        renderer.dispose();
        world.dispose();
        b2dr.dispose();
        hud.dispose();

    }

}
