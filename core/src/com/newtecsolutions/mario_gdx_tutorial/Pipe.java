package com.newtecsolutions.mario_gdx_tutorial;


import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

/**
 * Created by pedja on 5/22/17 1:13 PM.
 * This class is part of the mario_gdx_tutorial
 * Copyright © 2017 ${OWNER}
 */

public class Pipe
{
    private static final float WIDTH = 1f;

    public Rectangle bounds = new Rectangle();

    private TextureRegion pipe, head;
    private float headHeight, pipeHeight;
    private boolean isTop;

    public Pipe(float x, float y, float height, boolean isTop)
    {
        this.isTop = isTop;
        pipe = FlappyBirdGame.getInstance().getAssets().findRegion("pipe");
        head = FlappyBirdGame.getInstance().getAssets().findRegion(isTop ? "pipe_down" : "pipe_up");
        updateBounds(x, y, height, isTop);
    }

    public void render(SpriteBatch batch)
    {
        if(isTop)
        {
            batch.draw(pipe, bounds.x, bounds.y + headHeight, Utility.getWidth(head, headHeight), pipeHeight);
            Utility.draw(batch, head, bounds.x, bounds.y, headHeight);
        }
        else
        {
            batch.draw(pipe, bounds.x, bounds.y, Utility.getWidth(head, headHeight), pipeHeight);
            Utility.draw(batch, head, bounds.x, bounds.y + pipeHeight, headHeight);
        }
    }

    public void updateBounds(float x, float y, float height, boolean isTop)
    {
        head = FlappyBirdGame.getInstance().getAssets().findRegion(isTop ? "pipe_down" : "pipe_up");
        headHeight = WIDTH / ((float)head.getRegionWidth() / (float)head.getRegionHeight());
        pipeHeight = height - headHeight;
        bounds.set(x, y, WIDTH, height);
        this.isTop = isTop;
    }

    public boolean isTop()
    {
        return isTop;
    }
}
