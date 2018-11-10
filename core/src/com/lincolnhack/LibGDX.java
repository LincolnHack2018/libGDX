package com.lincolnhack;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.rafaskoberg.gdx.typinglabel.TypingLabel;

import box2dLight.RayHandler;

import static com.badlogic.gdx.graphics.Texture.TextureWrap.Repeat;
import static com.lincolnhack.Orientation.VERTIVAL;


public class LibGDX extends ApplicationAdapter {
	public static final AssetDescriptor<TextureAtlas> SKIN_ATLAS = new AssetDescriptor<TextureAtlas>("skin/terra-mother-ui.atlas", TextureAtlas.class);
	public static final AssetDescriptor<Skin> SKIN_JSON = new AssetDescriptor<Skin>("skin/terra-mother-ui.json", Skin.class,  new SkinLoader.SkinParameter("skin/terra-mother-ui.atlas"));

	Texture paddleTx;
	Texture goalTx;
	Texture puckTx;
	Texture barrierTx;

	Stage stage;
	Stage ui;

	Actor paddle;
	Actor goal;
	Actor puck;
	Actor barrier1;

	ShapeRenderer shaper;

	World world;
	private Box2DDebugRenderer debugRenderer;

	AssetManager assetManager;

	@Override
	public void create () {


		float ratio = (float)(Gdx.graphics.getWidth()) / (float)(Gdx.graphics.getHeight());
		Viewport viewport = new FillViewport(10, 10 / ratio);
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


		debugRenderer = new Box2DDebugRenderer();
		world = new World(new Vector2(0, 0), true);

		RayHandler.useDiffuseLight(false);

		goal = new Goal(region1, world, stage.getViewport().getWorldWidth() / 2 - 1.5f, 0,  3,1.5f, 0);
		stage.addActor(goal);

		puck = new Puck(region2, world, stage.getViewport().getWorldWidth() / 2 - 0.5f, 5, 1, 0);

		paddle = new Paddle(region, stage, world, (Puck) puck,stage.getViewport().getWorldWidth() / 2 - 0.5f, 3f, 1, 0);
		paddle.addListener(new DragListener() {
			public void drag(InputEvent event, float x, float y, int pointer) {
				paddle.moveBy(x - paddle.getWidth() / 2, y - paddle.getHeight() / 2);
			}
		});

		AssetManager assetManager = new AssetManager();
		assetManager.load(SKIN_ATLAS);
		assetManager.load(SKIN_JSON);
		assetManager.finishLoading();

		Field field = new Field(VERTIVAL, world, stage, barrierTx);

		ui = new Stage(new ScreenViewport());
		//Score score = new Score(assetManager, ui);


		stage.addActor(paddle);
		stage.addActor(puck);
		stage.setDebugAll(true);

		Gdx.input.setInputProcessor((InputProcessor) paddle);

	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		ui.act(Gdx.graphics.getDeltaTime());

		stage.act(Gdx.graphics.getDeltaTime());
		world.step(Gdx.graphics.getDeltaTime(), 8, 3);
		stage.draw();
		ui.draw();

		debugRenderer.render(world, stage.getCamera().combined);
	}
	
	@Override
	public void dispose () {
		paddleTx.dispose();
		stage.dispose();
	}
}
