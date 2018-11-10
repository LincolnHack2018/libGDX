package com.lincolnhack;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.lincolnhack.States.Player;

import static com.lincolnhack.Orientation.HORIZONTAL;
import static com.lincolnhack.Orientation.VERTICAL;


public class LibGDX extends ApplicationAdapter {
	public static final AssetDescriptor<TextureAtlas> SKIN_ATLAS = new AssetDescriptor<TextureAtlas>("skin/terra-mother-ui.atlas", TextureAtlas.class);
	public static final AssetDescriptor<Skin> SKIN_JSON = new AssetDescriptor<Skin>("skin/terra-mother-ui.json", Skin.class,  new SkinLoader.SkinParameter("skin/terra-mother-ui.atlas"));
	public static final AssetDescriptor<Texture> PADDLE = new AssetDescriptor<Texture>("Paddle.png", Texture.class);
	public static final AssetDescriptor<Texture> GOAL = new AssetDescriptor<Texture>("Goal.png", Texture.class);
	public static final AssetDescriptor<Texture> PUCK = new AssetDescriptor<Texture>("Puck.png", Texture.class);
	public static final AssetDescriptor<Texture> BARRIER = new AssetDescriptor<Texture>("Barrier.png", Texture.class);

	Player player = new Player();
	Player player2 = new Player();

    private String yourScoreName;
    private String oppenentsScoreName;

    BitmapFont yourScore;
    BitmapFont oppenentsScore;

	AssetManager assetManager;
	ShapeRenderer shaper;
	Texture puckTx;
	Stage stage;
	Stage ui;

	Field homeField;
	Field awayField;
	Actor puck;

	World world;
	Box2DDebugRenderer debugRenderer;

	@Override
	public void create () {
		loadAssets();
		player.score = 0;
		player2.score = 0;
        yourScore = new BitmapFont();
        oppenentsScore = new BitmapFont();


		float ratio = (float)(Gdx.graphics.getWidth()) / (float)(Gdx.graphics.getHeight());
		Viewport viewport = new FillViewport(10, 10 / ratio);
		stage = new Stage(viewport);
		shaper = new ShapeRenderer();
		debugRenderer = new Box2DDebugRenderer();
		world = new World(new Vector2(0, 0), true);

		puckTx = assetManager.get(PUCK);
		puck = new Puck(puckTx, world, stage.getViewport().getWorldWidth() / 2 - 0.5f, 5, 1, 0);

		homeField = new Field(VERTICAL, world, stage, assetManager, (Puck) puck);

		ui = new Stage(new ScreenViewport());
		Score score = new Score(assetManager, ui);

		stage.addActor(puck);
		stage.setDebugAll(true);

		Gdx.input.setInputProcessor((InputProcessor) homeField.getPaddle());

	}

	private void loadAssets() {
		assetManager = new AssetManager();
		assetManager.load(SKIN_ATLAS);
		assetManager.load(SKIN_JSON);
		assetManager.load(PADDLE);
		assetManager.load(GOAL);
		assetManager.load(PUCK);
		assetManager.load(BARRIER);
		assetManager.finishLoading();
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		player.score = homeField.update((Puck) puck, player.score);

        SpriteBatch batch = new SpriteBatch();

		yourScoreName = String.valueOf(player.score);
		oppenentsScoreName = String.valueOf(player2.score);

		stage.act(Gdx.graphics.getDeltaTime());
		world.step(Gdx.graphics.getDeltaTime(), 8, 3);
		stage.draw();
		//ui.draw();

		debugRenderer.render(world, stage.getCamera().combined);
        batch.begin();
        LoadScore(batch);
        LoadOpponentScore(batch);
        batch.end();

    }

    private void LoadOpponentScore(SpriteBatch batch) {
        oppenentsScore.setColor(Color.RED);
        oppenentsScore.draw(batch, oppenentsScoreName, 50, 1100);
        oppenentsScore.getData().setScale(5);
    }

    private void LoadScore(SpriteBatch batch) {
        yourScore.setColor(Color.BLUE);
        yourScore.draw(batch, yourScoreName, 50, 1000);
        yourScore.getData().setScale(5);
    }

    @Override
	public void dispose () {
		assetManager.dispose();
		stage.dispose();
	}


}
