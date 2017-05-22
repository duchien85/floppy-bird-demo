package com.newtecsolutions.mario_gdx_tutorial;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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

    private Texture pipe, head;
    private float headHeight, pipeHeight;
    private boolean isTop;

    public Pipe(float x, float y, float height, boolean isTop)
    {
        this.isTop = isTop;
        bounds.set(x, y, WIDTH, height);
        pipe = new Texture(Gdx.files.internal("pipe.png"));
        head = new Texture(Gdx.files.internal(isTop ? "pipe_down.png" : "pipe_up.png"));

        headHeight = WIDTH / ((float)head.getWidth() / (float)head.getHeight());
        pipeHeight = height - headHeight;
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
}
