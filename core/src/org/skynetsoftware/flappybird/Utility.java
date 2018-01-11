package org.skynetsoftware.flappybird;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by pedja on 5/22/17 10:23 AM.
 * This class is part of the mario_gdx_tutorial
 * Copyright © 2017 ${OWNER}
 */

public class Utility
{
    public static void draw(SpriteBatch batch, Texture texture, float x, float y, float height)
    {
        batch.draw(texture, x, y, height * texture.getWidth()/texture.getHeight(), height);
    }

    public static void draw(SpriteBatch batch, TextureRegion region, float x, float y, float height)
    {
        batch.draw(region, x, y, height * region.getRegionWidth() / region.getRegionHeight(), height);
    }

    public static float getWidth(TextureRegion region, float height)
    {
        return height * region.getRegionWidth() / region.getRegionHeight();
    }

    public static float getWidth(Texture texture, float height)
    {
        return height * texture.getWidth() / texture.getHeight();
    }

    public static boolean isPortrait() {
        return Gdx.graphics.getWidth() < Gdx.graphics.getHeight();
    }
}
