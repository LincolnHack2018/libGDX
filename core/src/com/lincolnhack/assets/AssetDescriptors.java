package com.lincolnhack.assets;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class AssetDescriptors {
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
    public static final AssetDescriptor<BitmapFont> FONT = new AssetDescriptor<>("font.ttf", BitmapFont.class);
}
