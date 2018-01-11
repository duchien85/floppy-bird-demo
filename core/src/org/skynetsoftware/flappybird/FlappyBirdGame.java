package org.skynetsoftware.flappybird;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class FlappyBirdGame extends Game
{
    private static FlappyBirdGame instance;

    private TextureAtlas assets;

    public static FlappyBirdGame getInstance()
    {
        return instance;
    }

    @Override
    public void create()
    {
        instance = this;
        setScreen(new SplashScreen());

        assets = new TextureAtlas("assets.atlas");
    }

    @Override
    public void dispose()
    {
        instance = null;
        assets.dispose();
        super.dispose();
    }

    public TextureAtlas getAssets()
    {
        return assets;
    }
}
