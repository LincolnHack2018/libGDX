package com.lincolnhack;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
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
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.lincolnhack.States.Player;
import com.lincolnhack.data.Device;
import com.lincolnhack.data.Direction;
import com.lincolnhack.data.GameState;
import com.lincolnhack.data.Pair;
import com.lincolnhack.data.Response;
import com.lincolnhack.interfaces.InitDevice;
import com.lincolnhack.interfaces.Network;
import com.lincolnhack.interfaces.Socket;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import lombok.Setter;
import lombok.SneakyThrows;

import static com.lincolnhack.Orientation.VERTICAL_BOTTOM;
import static com.lincolnhack.Orientation.VERTICAL_TOP;
import static com.lincolnhack.Paddle.resetPaddle;


public class LibGDX extends ApplicationAdapter {
	public static final AssetDescriptor<TextureAtlas> SKIN_ATLAS = new AssetDescriptor<TextureAtlas>("skin/terra-mother-ui.atlas", TextureAtlas.class);
	public static final AssetDescriptor<Skin> SKIN_JSON = new AssetDescriptor<Skin>("skin/terra-mother-ui.json", Skin.class,  new SkinLoader.SkinParameter("skin/terra-mother-ui.atlas"));
	public static final AssetDescriptor<Texture> PADDLE = new AssetDescriptor<Texture>("Paddle.png", Texture.class);
	public static final AssetDescriptor<Texture> GOAL_BOTTOM = new AssetDescriptor<Texture>("Goal Bottom.png", Texture.class);
	public static final AssetDescriptor<Texture> GOAL_LEFT = new AssetDescriptor<Texture>("Goal Left.png", Texture.class);
	public static final AssetDescriptor<Texture> GOAL_RIGHT = new AssetDescriptor<Texture>("Goal Right.png", Texture.class);
	public static final AssetDescriptor<Texture> GOAL_TOP = new AssetDescriptor<Texture>("Goal Top.png", Texture.class);
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
	SpriteBatch batch;
	Texture puckTx;
	Stage stage;
	Stage ui;

	Field homeField;
	Field awayField;
	Paddle paddle;
	Actor puck;

	World world;
	Box2DDebugRenderer debugRenderer;


	boolean setup = false;
	float startX = 0.0f;
	float startY = 0.0f;
	float endX = 0.0f;
	float endY = 0.0f;

	static String id = UUID.randomUUID().toString();

	private InitDevice initDevice;
	private Network network;
	private Socket socket;

	@Setter
	private static List<Response> responses;

	public LibGDX(InitDevice initDevice, Network network, Socket socket) {
		this.initDevice = initDevice;
		this.network = network;
		this.socket = socket;
	}

	public LibGDX(InitDevice initDevice, Network network) {
		this.initDevice = initDevice;
		this.network = network;
	}

	Texture img;
//	Image image1;
//	Image image2;
//	Image image3;

	GameState gameState = GameState.SETUP;
	ShapeRenderer shapeRenderer;
	@Override
	public void create () {
		batch = new SpriteBatch();
		loadAssets();
		player.score = 0;
		player2.score = 0;
		yourScore = new BitmapFont();
		oppenentsScore = new BitmapFont();

		shapeRenderer = new ShapeRenderer();


		float ratio = (float)(Gdx.graphics.getWidth()) / (float)(Gdx.graphics.getHeight());
		Viewport viewport = new FillViewport(10, 10 / ratio);
		img = new Texture("point.png");
//		image1 = new Image(img);
//		image2 = new Image(img);
//		image3 = new Image(img);
//		image1.setSize(1, 1);
//		image2.setSize(1, 1);
//		image3.setSize(1, 1);

		stage = new Stage(viewport);
		shaper = new ShapeRenderer();
		debugRenderer = new Box2DDebugRenderer();
		world = new World(new Vector2(0, 0), true);

		puckTx = assetManager.get(PUCK);
		puck = new Puck(puckTx, world, stage.getViewport().getWorldWidth() / 2 - 0.5f, 5, 1, 0);


		paddle = resetPaddle(VERTICAL_BOTTOM, stage, world, assetManager, (Puck) puck);

		stage.addActor(paddle);

		ui = new Stage(new ScreenViewport());
		Score score = new Score(assetManager, ui);

		stage.addActor(puck);
		stage.setDebugAll(true);

		socket.subscribe();

		Gdx.input.setInputProcessor(new InputAdapter() {

			@Override
			public boolean touchDown(int x, int y, int pointer, int button) {
				Vector3 testPoint = new Vector3();
				testPoint.set(x, y, 0);
				stage.getCamera().unproject(testPoint);
				startX = testPoint.x;
				startY = testPoint.y;
				System.out.println(startX);
				System.out.println(startY);
				return true;
			}

			@Override
			@SneakyThrows
			public boolean touchUp(int screenX, int screenY, int pointer, int button) {
				Vector3 testPoint = new Vector3();
				testPoint.set(screenX, screenY, 0);
				stage.getCamera().unproject(testPoint);
				endX = testPoint.x;
				endY = testPoint.y;

				Device device = Device.builder()
						.id(id)
						.touchDownX(startX)
						.touchDownY(startY)
						.touchUpX(endX)
						.touchUpY(endY)
						.deviceWidth(stage.getViewport().getWorldWidth())
						.deviceHeight(stage.getViewport().getWorldHeight())
						.build();

				initDevice.init(device);

				return true;
			}
		});

//		stage.addActor(image1);
//		stage.addActor(image2);
//		stage.addActor(image3);
		shapeRenderer.setProjectionMatrix(stage.getCamera().combined);
	}

	private void loadAssets() {
		assetManager = new AssetManager();
		assetManager.load(SKIN_ATLAS);
		assetManager.load(SKIN_JSON);
		assetManager.load(PADDLE);
		assetManager.load(GOAL_BOTTOM);
		assetManager.load(GOAL_LEFT);
		assetManager.load(GOAL_RIGHT);
		assetManager.load(GOAL_TOP);
		assetManager.load(PUCK);
		assetManager.load(BARRIER);
		assetManager.finishLoading();
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		switch (gameState) {
			case SETUP:
				//if (responses != null && !responses.isEmpty()) {
					if(gameState == GameState.SETUP){
						Gdx.input.setInputProcessor((InputProcessor) paddle);
						gameState = GameState.RUNNING;
						Map<Direction, List<Pair<Float>>> openings = new HashMap<>();
						openings.put(Direction.RIGHT, Arrays.asList(new Pair<>(3f, 5f)));
						homeField = new Field(openings, stage.getViewport().getWorldWidth(), stage.getViewport().getWorldHeight());
						//awayField = new Field(0, stage.getViewport().getWorldHeight(), VERTICAL_TOP, world, stage, assetManager, (Puck) puck, responses.get(0));
					}
//					switch (responses.get(0).getDirection()){
//						case TOP:
//						case BOTTOM:
////							image1.setPosition(responses.get(0).getIntersectX() - responses.get(0).getIntersectMinus(), responses.get(0).getIntersectY());
////							image2.setPosition(responses.get(0).getIntersectX(), responses.get(0).getIntersectY());
////							image3.setPosition(responses.get(0).getIntersectX() + responses.get(0).getIntersectPlus(), responses.get(0).getIntersectY());
//							break;
//						case RIGHT:
//						case LEFT:
////							image1.setPosition(responses.get(0).getIntersectX(), responses.get(0).getIntersectY() - responses.get(0).getIntersectMinus());
////							image2.setPosition(responses.get(0).getIntersectX(), responses.get(0).getIntersectY());
////							image3.setPosition(responses.get(0).getIntersectX(), responses.get(0).getIntersectY() + responses.get(0).getIntersectPlus());
//							break;
//					}
				//}
				break;

			case RUNNING:
				//homeField.update((Puck) puck, player.score);
				homeField.draw(shapeRenderer, Color.BLACK);
				break;
			default:
				break;
		}


		stage.act(Gdx.graphics.getDeltaTime());
		world.step(Gdx.graphics.getDeltaTime(), 8, 3);
		stage.draw();
		//ui.draw();

		debugRenderer.render(world, stage.getCamera().combined);
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
