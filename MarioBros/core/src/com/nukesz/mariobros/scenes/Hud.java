package com.nukesz.mariobros.scenes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.nukesz.mariobros.MarioBros;

public class Hud implements Disposable {
	private static Integer score;
	private static Label scoreLabel;

	private final Viewport viewport;

	private Integer worldTimer;
    private float timeCount;

	private final Label countDownLabel;
	private final Label timeLabel;
	private final Label levelLabel;
	private final Label worldLabel;
	private final Label marioLabel;

	public Stage stage;

	public Hud(final SpriteBatch sb) {
        worldTimer = 300;
        timeCount = 0;
        score = 0;

        viewport = new FitViewport(MarioBros.V_WIDTH, MarioBros.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, sb);

        final Table table = new Table();
        table.top();
        table.setFillParent(true);

		final LabelStyle labelStyle = new LabelStyle(new BitmapFont(), Color.WHITE);
		countDownLabel = new Label(String.format("%03d", worldTimer), labelStyle);
		scoreLabel = new Label(String.format("%06d", score), labelStyle);
		timeLabel = new Label("TIME", labelStyle);
		levelLabel = new Label("1-1", labelStyle);
		worldLabel = new Label("WORLD", labelStyle);
		marioLabel = new Label("MARIO", labelStyle);

        table.add(marioLabel).expandX().padTop(10);
        table.add(worldLabel).expandX().padTop(10);
        table.add(timeLabel).expandX().padTop(10);
        table.row();
        table.add(scoreLabel).expandX();
        table.add(levelLabel).expandX();
        table.add(countDownLabel).expandX();

        stage.addActor(table);
    }

    public void update(final float dt) {
        timeCount += dt;
        if (timeCount >= 1) {
            worldTimer--;
            countDownLabel.setText(String.format("%03d", worldTimer));
            timeCount = 0;
        }
    }

    public static void addScore(final int value) {
        score += value;
        scoreLabel.setText(String.format("%06d", score));
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
