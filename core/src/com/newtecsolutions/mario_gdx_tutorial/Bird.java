package com.newtecsolutions.mario_gdx_tutorial;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by pedja on 5/22/17 9:50 AM.
 * This class is part of the mario_gdx_tutorial
 * Copyright © 2017 ${OWNER}
 */

public class Bird
{
    private static final float BIRD_INIT_X = 3;
    private static final float BIRD_INIT_Y = 5;
    private static final float GRAVITY = -0.7f;
    private static final float MAX_VELOCITY = 0.5f;

    private TextureAtlas atlas;
    private Animation<TextureAtlas.AtlasRegion> animation;
    private float stateTime;

    public Vector2 position;
    private Vector2 acceleration;
    private Vector2 velocity;
    private float rotation;

    private Rectangle bounds = new Rectangle();
    private float height;
    private float width;

    public Bird()
    {
        position = new Vector2(BIRD_INIT_X, BIRD_INIT_Y);
        acceleration = new Vector2();
        velocity = new Vector2();

        atlas = new TextureAtlas(Gdx.files.internal("bird.atlas"));
        animation = new Animation<TextureAtlas.AtlasRegion>(0.075f, atlas.getRegions());
        animation.setPlayMode(Animation.PlayMode.LOOP);
    }

    public void render(SpriteBatch batch)
    {
        TextureRegion region = animation.getKeyFrame(stateTime);
        height = 0.5f;
        width = height * region.getRegionWidth() / region.getRegionHeight();
        batch.draw(region, position.x, position.y, width / 2, height / 2, width, height, 1, 1, rotation);
    }

    public void update(float delta)
    {
        //handle rotation based on velocity
        rotation += velocity.y > 0 ? 180f * delta : -180f * delta;
        if(rotation < -90)
            rotation = -90;
        if(rotation > 35)
            rotation = 35;

        //handle physics
        acceleration.y = GRAVITY;

        acceleration.scl(delta);

        velocity.add(acceleration);

        if(velocity.y > MAX_VELOCITY)
            velocity.y = MAX_VELOCITY;

        position.add(velocity);

        stateTime += delta;
    }

    public void dispose()
    {
        atlas.dispose();
    }

    public void jump()
    {
        velocity.set(0, 0.17f);
    }

    public Rectangle getBounds()
    {
        bounds.set(position.x, position.y, width, height);
        return bounds;
    }

    public void reset()
    {
        position.set(BIRD_INIT_X, BIRD_INIT_Y);
        velocity.set(0, 0);
        rotation = 0;
    }
}
