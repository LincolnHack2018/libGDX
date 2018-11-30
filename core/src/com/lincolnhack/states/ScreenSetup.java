package com.lincolnhack.states;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.lincolnhack.data.Device;
import com.lincolnhack.interfaces.InitDevice;
import com.lincolnhack.interfaces.Socket;

import java.util.UUID;

import lombok.SneakyThrows;

public class ScreenSetup extends InputAdapter {


    private Socket socket;
    private InitDevice initDevice;
    public static String id = UUID.randomUUID().toString();
    private Stage stage;
    float startX = 0.0f;
    float startY = 0.0f;
    float endX = 0.0f;
    float endY = 0.0f;

    public ScreenSetup(Stage stage, Socket socket, InitDevice initDevice) {
        this.stage = stage;
        this.initDevice = initDevice;
        this.socket = socket;
        socket.subscribe();
    }

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
                .touchDownTime(System.currentTimeMillis())
                .deviceWidth(stage.getViewport().getWorldWidth())
                .deviceHeight(stage.getViewport().getWorldHeight())
                .build();

        initDevice.init(device);

        return true;
    }
}
