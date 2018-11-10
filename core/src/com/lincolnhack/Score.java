package com.lincolnhack;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.rafaskoberg.gdx.typinglabel.TypingLabel;

import lombok.Getter;

import static com.lincolnhack.LibGDX.SKIN_JSON;

public class Score {

    @Getter
    private Table root;

    public Score(AssetManager assetManager, Stage stage) {
        root = new Table();
        root.row().expandX();

        Skin skin = assetManager.get(SKIN_JSON);
        TypingLabel label = new TypingLabel("Hello world!", skin);
        label.setColor(Color.GOLD);
        label.setFontScale(10f, 10f);
        root.addActor(label);

        root.setPosition(0, stage.getViewport().getWorldHeight() / 4);
        root.setFillParent(true);
        stage.addActor(root);
    }
}
