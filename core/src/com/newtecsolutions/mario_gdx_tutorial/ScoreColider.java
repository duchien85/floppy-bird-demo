package com.newtecsolutions.mario_gdx_tutorial;

import com.badlogic.gdx.math.Rectangle;


/**
 * Created by pedja on 5/24/17 12:07 PM.
 * This class is part of the mario_gdx_tutorial
 * Copyright © 2017 ${OWNER}
 */

public class ScoreColider
{
    private static final float WIDTH = 0.2f;

    public Rectangle bounds = new Rectangle();
    public boolean alreadyChecked;

    public ScoreColider(float x, float y, float height)
    {
        bounds.set(x, y, WIDTH, height);
    }

    public void updateBounds(float x, float y, float height)
    {
        bounds.set(x, y, WIDTH, height);
    }
}
