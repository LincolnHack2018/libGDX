package com.lincolnhack;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;
import static com.lincolnhack.assets.AssetDescriptors.FONT;

public class Text {

    private Stage stage;
    private Label label;
    private Label.LabelStyle style = new Label.LabelStyle();
    private AssetManager assetManager;

    public Text(Stage stage, AssetManager assetManager) {
        this.stage = stage;
        this.assetManager = assetManager;

        FileHandleResolver resolver = new InternalFileHandleResolver();
        assetManager.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));
        assetManager.setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(resolver));

        FreetypeFontLoader.FreeTypeFontLoaderParameter letterFont = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
        letterFont.fontFileName = FONT.fileName;
        letterFont.fontParameters.size = 200;
        assetManager.load(FONT.fileName, BitmapFont.class, letterFont);
        assetManager.finishLoading();

        BitmapFont font = assetManager.get(FONT);

        style.fontColor = Color.PINK;
        style.font = font;
        label = new Label("", style);
    }

    public void queueText(String text, float duration) {
        label = new Label(text, style);
        label.addAction(sequence(fadeOut(duration)));
        label.setX(50);
        label.setY(50);
        stage.addActor(label);
    }
}
