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


public class LibGDX extends ApplicationAdapter {
	Texture paddleTx;
	Texture goalTx;

	Stage stage;

	Actor actor;
	Actor goal;

	ShapeRenderer shaper;

	World world;
	
	@Override
	public void create () {
		Viewport viewport = new ScreenViewport();
		stage = new Stage(viewport);
		shaper = new ShapeRenderer();

		paddleTx = new Texture("Paddle.png");
		goalTx = new Texture("Goal.png");

		TextureRegion region = new TextureRegion(paddleTx, 0, 0, 100, 100);
		TextureRegion region1 = new TextureRegion(goalTx, 0, 0, 458, 718);

		Gdx.input.setInputProcessor(stage);

		world = new World(new Vector2(0, -10), true);

		RayHandler.useDiffuseLight(false);

		goal = new Goal(region1, world, stage.getViewport().getScreenWidth() / 2, 0, 458, 718, -90);
		stage.addActor(goal);

		actor = new Paddle(region, world, 0, 0, 100, 0);
		actor.addListener(new DragListener() {
			public void drag(InputEvent event, float x, float y, int pointer) {
				actor.moveBy(x - actor.getWidth() / 2, y - actor.getHeight() / 2);
			}
		});
		stage.addActor(actor);
		stage.setDebugAll(true);

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
