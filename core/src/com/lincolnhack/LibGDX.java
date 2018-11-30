package com.lincolnhack;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.lincolnhack.data.Direction;
import com.lincolnhack.data.GameState;
import com.lincolnhack.data.Pair;
import com.lincolnhack.data.Response;
import com.lincolnhack.interfaces.InitDevice;
import com.lincolnhack.interfaces.Socket;
import com.lincolnhack.objects.ClientInputProcessor;
import com.lincolnhack.objects.ClientPaddle;
import com.lincolnhack.objects.HostPaddle;
import com.lincolnhack.states.ScreenSetup;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Setter;

import static com.lincolnhack.assets.AssetDescriptors.BARRIER;
import static com.lincolnhack.assets.AssetDescriptors.FONT;
import static com.lincolnhack.assets.AssetDescriptors.GOAL_BOTTOM;
import static com.lincolnhack.assets.AssetDescriptors.GOAL_LEFT;
import static com.lincolnhack.assets.AssetDescriptors.GOAL_RIGHT;
import static com.lincolnhack.assets.AssetDescriptors.GOAL_TOP;
import static com.lincolnhack.assets.AssetDescriptors.PADDLE;
import static com.lincolnhack.assets.AssetDescriptors.PADDLE_GREEN;
import static com.lincolnhack.assets.AssetDescriptors.PUCK;
import static com.lincolnhack.assets.AssetDescriptors.SKIN_ATLAS;
import static com.lincolnhack.assets.AssetDescriptors.SKIN_JSON;
import static com.lincolnhack.objects.Paddle.PADDLE_RADIUS;


public class LibGDX extends ApplicationAdapter {

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

    GameState gameState;

    ClientPaddle clientPaddle;
    ClientInputProcessor clientInputProcessor;

    ScreenSetup screenSetup;

    Text text;

    @Setter
    private static List<Response> responses;

    Socket socket;
    InitDevice initDevice;

    public LibGDX(InitDevice initDevice, Socket socket) {
        this.socket = socket;
        this.initDevice = initDevice;
    }

    public LibGDX() {}

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
        clientPaddle = new ClientPaddle(assetManager.get(PADDLE_GREEN), stage, world, puck,stage.getViewport().getWorldWidth() / 2 - 0.5f, stage.getViewport().getWorldHeight() - 3f, PADDLE_RADIUS, 0);

        stage.addActor(clientPaddle);

        ui = new Stage(new ScreenViewport());

        stage.addActor(puck);
        stage.setDebugAll(true);

        gameState = GameState.SETUP;

        screenSetup = new ScreenSetup(stage, socket, initDevice);
        Gdx.input.setInputProcessor(screenSetup);
        text = new Text(ui, assetManager);

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

        ui.act();
        ui.draw();

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
            text.queueText(responses.get(0).getOrderNumber() + "", 5f);
        }
    }

    private void gameUpdate() {
        //clientPaddle.update(clientInputProcessor.getTouch());
    }


    @Override
    public void dispose() {
        assetManager.dispose();
        stage.dispose();
    }


}
