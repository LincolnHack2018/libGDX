package com.lincolnhack;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import box2dLight.RayHandler;

import static com.badlogic.gdx.graphics.Texture.TextureWrap.Repeat;


public class LibGDX extends ApplicationAdapter {
	Texture paddleTx;
	Texture goalTx;
	Texture puckTx;
	Texture barrierTx;

	Stage stage;

	Actor paddle;
	Actor goal;
	Actor puck;
	Actor barrier1;

	ShapeRenderer shaper;

	World world;
	
	@Override
	public void create () {
		Viewport viewport = new ScreenViewport();
		stage = new Stage(viewport);
		shaper = new ShapeRenderer();

		paddleTx = new Texture("Paddle.png");
		goalTx = new Texture("Goal.png");
		puckTx = new Texture("Puck.png");
		barrierTx = new Texture("Barrier.png");
		barrierTx.setWrap(Repeat, Repeat);

		TextureRegion region = new TextureRegion(paddleTx, 0, 0, 100, 100);
		TextureRegion region1 = new TextureRegion(goalTx, 0, 0,  718, 458);
		TextureRegion region2 = new TextureRegion(puckTx, 0, 0, 100, 100);

		Gdx.input.setInputProcessor(stage);

		world = new World(new Vector2(0, -10), true);

		RayHandler.useDiffuseLight(false);

		goal = new Goal(region1, world, stage.getViewport().getScreenWidth() / 4, 0,  718,458, 0);
		stage.addActor(goal);

		paddle = new Paddle(region, world, 0, 0, 100, 0);
		paddle.addListener(new DragListener() {
			public void drag(InputEvent event, float x, float y, int pointer) {
				paddle.moveBy(x - paddle.getWidth() / 2, y - paddle.getHeight() / 2);
			}
		});
		puck = new Puck(region2, world, 300, 300, 100, 0);
		barrier1 = new Barrier(barrierTx, world, -16, 0, 32, stage.getViewport().getScreenHeight(),0);

		stage.addActor(paddle);
		stage.addActor(puck);
		stage.setDebugAll(true);
		stage.addActor(barrier1);

	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


		stage.act(Gdx.graphics.getDeltaTime());
		world.step(Gdx.graphics.getDeltaTime(), 6, 2);
		stage.draw();
	}
	
	@Override
	public void dispose () {
		paddleTx.dispose();
		stage.dispose();
	}
}
