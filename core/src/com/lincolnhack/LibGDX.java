package com.lincolnhack;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.lincolnhack.data.Device;
import com.lincolnhack.data.Response;
import com.lincolnhack.interfaces.InitDevice;
import com.lincolnhack.interfaces.Network;
import com.lincolnhack.interfaces.Socket;

import java.util.List;
import java.util.UUID;

import box2dLight.RayHandler;
import lombok.Setter;
import lombok.SneakyThrows;

import static com.badlogic.gdx.graphics.Texture.TextureWrap.Repeat;
import static com.lincolnhack.Orientation.VERTIVAL;
import static com.lincolnhack.util.SwipeUtil.nextToEdge;


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

		paddle = new Paddle(region, world, stage.getViewport().getScreenWidth()/2, 0, 100, 0);
		paddle.addListener(new DragListener() {
			public void drag(InputEvent event, float x, float y, int pointer) {
				paddle.moveBy(x - paddle.getWidth() / 2, y - paddle.getHeight() / 2);
			}
		});
		puck = new Puck(region2, world, 300, 300, 100, 0);

		Field field = new Field(0, 0, VERTIVAL, world, stage, barrierTx);

		stage.addActor(paddle);
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
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

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
		world.step(Gdx.graphics.getDeltaTime(), 6, 2);
		stage.draw();
	}
	
	@Override
	public void dispose () {
		paddleTx.dispose();
		stage.dispose();
	}
}
