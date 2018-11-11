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
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.lincolnhack.States.Player;
import com.lincolnhack.data.Device;
import com.lincolnhack.data.Response;
import com.lincolnhack.interfaces.InitDevice;
import com.lincolnhack.interfaces.Network;
import com.lincolnhack.interfaces.Socket;

import java.util.List;
import java.util.UUID;

import static com.lincolnhack.Orientation.VERTICAL_BOTTOM;
import static com.lincolnhack.Orientation.VERTICAL_TOP;
import static com.lincolnhack.Paddle.resetPaddle;
import box2dLight.RayHandler;
import lombok.Setter;
import lombok.SneakyThrows;

import static com.badlogic.gdx.graphics.Texture.TextureWrap.Repeat;
import static com.lincolnhack.Orientation.VERTIVAL;
import static com.lincolnhack.util.SwipeUtil.nextToEdge;


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
	Image image1;
	Image image2;
	Image image3;

	@Override
	public void create () {
		loadAssets();
		player.score = 0;
		player2.score = 0;
        yourScore = new BitmapFont();
        oppenentsScore = new BitmapFont();


		float ratio = (float)(Gdx.graphics.getWidth()) / (float)(Gdx.graphics.getHeight());
		Viewport viewport = new FillViewport(10, 10 / ratio);
		img = new Texture("point.png");
		image1 = new Image(img);
		image1.setScale(2);
		image2 = new Image(img);
		image2.setScale(2);
		image3 = new Image(img);
		image3.setScale(2);

		Viewport viewport = new ScreenViewport();
		stage = new Stage(viewport);
		shaper = new ShapeRenderer();
		debugRenderer = new Box2DDebugRenderer();
		world = new World(new Vector2(0, 0), true);

		puckTx = assetManager.get(PUCK);
		puck = new Puck(puckTx, world, stage.getViewport().getWorldWidth() / 2 - 0.5f, 5, 1, 0);


		paddle = resetPaddle(VERTICAL_BOTTOM, stage, world, assetManager, (Puck) puck);

		stage.addActor(paddle);

		homeField = new Field(0,0, VERTICAL_BOTTOM, world, stage, assetManager, (Puck) puck);
		awayField = new Field(0, stage.getViewport().getWorldHeight(), VERTICAL_TOP, world, stage, assetManager, (Puck) puck);

		ui = new Stage(new ScreenViewport());
		Score score = new Score(assetManager, ui);

		stage.addActor(puck);
		stage.setDebugAll(true);

		socket.subscribe();

		Gdx.input.setInputProcessor(new InputAdapter(){

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
			public boolean touchUp(int screenX, int screenY, int pointer, int button){
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
						.deviceWidth(Gdx.graphics.getWidth())
						.deviceHeight(Gdx.graphics.getHeight())
						.build();

				initDevice.init(device);

				return true;
			}
		});
		stage.addActor(image1);
		stage.addActor(image2);
		stage.addActor(image3);
		Gdx.input.setInputProcessor((InputProcessor) paddle);

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
		player.score = homeField.update((Puck) puck, player.score);

        SpriteBatch batch = new SpriteBatch();

		yourScoreName = String.valueOf(player.score);
		oppenentsScoreName = String.valueOf(player2.score);
		if (responses != null && !responses.isEmpty()) {
			switch (responses.get(0).getDirection()){
				case TOP:
				case BOTTOM:
					image1.setPosition(responses.get(0).getIntersectX() - responses.get(0).getIntersectMinus(), responses.get(0).getIntersectY());
					image2.setPosition(responses.get(0).getIntersectX(), responses.get(0).getIntersectY());
					image3.setPosition(responses.get(0).getIntersectX() + responses.get(0).getIntersectPlus(), responses.get(0).getIntersectY());

					System.out.println(responses.get(0).getIntersectX() - responses.get(0).getIntersectMinus() + ", " + responses.get(0).getIntersectY());
					System.out.println(responses.get(0).getIntersectX() + ", " + responses.get(0).getIntersectY());
					System.out.println(responses.get(0).getIntersectX() + responses.get(0).getIntersectPlus() + ", " + responses.get(0).getIntersectY());
					break;
				case RIGHT:
				case LEFT:
					image1.setPosition(responses.get(0).getIntersectX(), responses.get(0).getIntersectY() - responses.get(0).getIntersectMinus());
					image2.setPosition(responses.get(0).getIntersectX(), responses.get(0).getIntersectY());
					image3.setPosition(responses.get(0).getIntersectX(), responses.get(0).getIntersectY() + responses.get(0).getIntersectPlus());
					break;
			}
//			responses.clear();


		}

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
