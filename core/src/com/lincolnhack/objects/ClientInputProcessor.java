package com.lincolnhack.objects;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;

import lombok.Data;

@Data
public class ClientInputProcessor implements InputProcessor {

    private Touch touch;
    private Vector3 testPoint;
    private Stage stage;

    public ClientInputProcessor(Stage stage) {
        this.stage = stage;
        touch = new Touch();
        testPoint = new Vector3();
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        testPoint.set(screenX, screenY, 0);
        stage.getCamera().unproject(testPoint);
        touch.setDown(true);
        touch.setUp(false);
        touch.setDragged(false);
        touch.setX(testPoint.x);
        touch.setY(testPoint.y);
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        testPoint.set(screenX, screenY, 0);
        stage.getCamera().unproject(testPoint);
        touch.setDown(false);
        touch.setUp(true);
        touch.setDragged(false);
        touch.setX(testPoint.x);
        touch.setY(testPoint.y);
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        testPoint.set(screenX, screenY, 0);
        stage.getCamera().unproject(testPoint);
        touch.setDown(false);
        touch.setUp(false);
        touch.setDragged(true);
        touch.setX(testPoint.x);
        touch.setY(testPoint.y);
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
