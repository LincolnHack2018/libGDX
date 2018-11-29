package com.lincolnhack;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.lincolnhack.data.Device;
import com.lincolnhack.data.Direction;
import com.lincolnhack.data.GameState;
import com.lincolnhack.data.Pair;
import com.lincolnhack.data.Response;
import com.lincolnhack.interfaces.InitDevice;
import com.lincolnhack.interfaces.Network;
import com.lincolnhack.interfaces.Socket;
import com.lincolnhack.objects.ClientInputProcessor;
import com.lincolnhack.objects.ClientPaddle;
import com.lincolnhack.objects.HostPaddle;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import lombok.Setter;
import lombok.SneakyThrows;

import static com.lincolnhack.objects.Paddle.PADDLE_RADIUS;


public class LibGDX extends ApplicationAdapter {
    public static final AssetDescriptor<TextureAtlas> SKIN_ATLAS = new AssetDescriptor<TextureAtlas>("skin/terra-mother-ui.atlas", TextureAtlas.class);
    public static final AssetDescriptor<Skin> SKIN_JSON = new AssetDescriptor<Skin>("skin/terra-mother-ui.json", Skin.class, new SkinLoader.SkinParameter("skin/terra-mother-ui.atlas"));
    public static final AssetDescriptor<Texture> PADDLE = new AssetDescriptor<Texture>("Paddle.png", Texture.class);
    public static final AssetDescriptor<Texture> PADDLE_GREEN = new AssetDescriptor<Texture>("PaddleGreen.png", Texture.class);
    public static final AssetDescriptor<Texture> GOAL_BOTTOM = new AssetDescriptor<Texture>("Goal Bottom.png", Texture.class);
    public static final AssetDescriptor<Texture> GOAL_LEFT = new AssetDescriptor<Texture>("Goal Left.png", Texture.class);
    public static final AssetDescriptor<Texture> GOAL_RIGHT = new AssetDescriptor<Texture>("Goal Right.png", Texture.class);
    public static final AssetDescriptor<Texture> GOAL_TOP = new AssetDescriptor<Texture>("Goal Top.png", Texture.class);
    public static final AssetDescriptor<Texture> PUCK = new AssetDescriptor<Texture>("Puck.png", Texture.class);
    public static final AssetDescriptor<Texture> BARRIER = new AssetDescriptor<Texture>("Barrier.png", Texture.class);

    AssetManager assetManager;
    ShapeRenderer shaper;
    Texture puckTx;
    Stage stage;
    Stage ui;

    Field homeField;
    HostPaddle paddle;
    Puck puck;

    World world;
    Box2DDebugRenderer debugRenderer;

    float startX = 0.0f;
    float startY = 0.0f;
    float endX = 0.0f;
    float endY = 0.0f;

    static String id = UUID.randomUUID().toString();

    private InitDevice initDevice;
    private Socket socket;

    @Setter
    private static List<Response> responses;

    public LibGDX(InitDevice initDevice, Network network, Socket socket) {
        this.initDevice = initDevice;
        this.socket = socket;
    }

    public LibGDX(InitDevice initDevice, Network network) {
        this.initDevice = initDevice;
    }

    GameState gameState;

    ClientPaddle clientPaddle;
    ClientInputProcessor clientInputProcessor;

    @Override
    public void create() {
        loadAssets();

        float screenPhysicalWidthInCentimeters = Gdx.graphics.getWidth() / Gdx.graphics.getPpcX();
        float screenPhysicalHeightInCentimeters = Gdx.graphics.getHeight() / Gdx.graphics.getPpcY();
        Viewport viewport = new FillViewport(screenPhysicalWidthInCentimeters, screenPhysicalHeightInCentimeters);
        Gdx.app.log(this.getClass().getSimpleName(), "Screen size is " + screenPhysicalWidthInCentimeters + "cm wide x " +
                screenPhysicalHeightInCentimeters + "cm high");

        stage = new Stage(viewport);
        shaper = new ShapeRenderer();
        debugRenderer = new Box2DDebugRenderer();
        world = new World(new Vector2(0, 0), true);
        shaper.setProjectionMatrix(stage.getCamera().combined);

        puckTx = assetManager.get(PUCK);
        puck = new Puck(puckTx, world, stage.getViewport().getWorldWidth() / 2 - 0.5f, 5, 0.5f, 0);
        homeField = new Field(new HashMap<>(), stage.getViewport().getWorldWidth(), stage.getViewport().getWorldHeight());
        //paddle = new HostPaddle(assetManager.get(PADDLE), stage, world, puck,stage.getViewport().getWorldWidth() / 2 - 0.5f, stage.getViewport().getWorldHeight() - 3f, PADDLE_RADIUS, 0);
        clientPaddle = new ClientPaddle(assetManager.get(PADDLE_GREEN), stage, world, puck,stage.getViewport().getWorldWidth() / 2 - 0.5f, stage.getViewport().getWorldHeight() - 3f, PADDLE_RADIUS, 0);

        //stage.addActor(paddle);
        stage.addActor(clientPaddle);

        ui = new Stage(new ScreenViewport());

        stage.addActor(puck);
        stage.setDebugAll(true);

        gameState = GameState.SETUP;

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

//        clientInputProcessor = new ClientInputProcessor(stage);
//        Gdx.input.setInputProcessor(clientInputProcessor);
    }

    private void loadAssets() {
        assetManager = new AssetManager();
        assetManager.load(SKIN_ATLAS);
        assetManager.load(SKIN_JSON);
        assetManager.load(PADDLE);
        assetManager.load(PADDLE_GREEN);
        assetManager.load(GOAL_BOTTOM);
        assetManager.load(GOAL_LEFT);
        assetManager.load(GOAL_RIGHT);
        assetManager.load(GOAL_TOP);
        assetManager.load(PUCK);
        assetManager.load(BARRIER);
        assetManager.finishLoading();
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        switch (gameState) {
            case SETUP:
                setupUpdate();
                break;
            case RUNNING:
                gameUpdate();
                break;
            default:
                break;
        }

        homeField.draw(shaper, Color.GREEN);
        stage.act(Gdx.graphics.getDeltaTime());
        world.step(Gdx.graphics.getDeltaTime(), 8, 3);
        stage.draw();

        debugRenderer.render(world, stage.getCamera().combined);
    }

    private void setupUpdate() {
        if (responses != null && !responses.isEmpty()) {
            Gdx.input.setInputProcessor(paddle);
            gameState = GameState.RUNNING;
            Map<Direction, List<Pair<Float>>> openings = new HashMap<>();
            for (int i = 0; i < responses.size(); i++) {
                openings.put(responses.get(i).getDirection(), responses.get(i).getIntersectDistances());
            }
            homeField = new Field(openings, stage.getViewport().getWorldWidth(), stage.getViewport().getWorldHeight());
        }
    }

    private void gameUpdate() {
        clientPaddle.update(clientInputProcessor.getTouch());
    }


    @Override
    public void dispose() {
        assetManager.dispose();
        stage.dispose();
    }


}
