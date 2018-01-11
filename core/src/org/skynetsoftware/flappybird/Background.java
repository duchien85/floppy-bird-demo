package org.skynetsoftware.flappybird;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by pedja on 5/22/17 9:40 AM.
 * This class is part of the mario_gdx_tutorial
 * Copyright © 2017 ${OWNER}
 */

public class Background
{
    private static final float BACKGROUND_SPEED = 0.5f;
    private static final Color BACKGROUND_COLOR = new Color(0.305882353f, 0.752941176f, 0.792156863f, 1);
    private OrthographicCamera camera;
    private ShapeRenderer shapeRenderer;
    private Texture texture;

    private float skyWidth, skyHeight;

    private Vector2 position;

    public Background(float cameraWidth, float cameraHeight)
    {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, cameraWidth, cameraHeight);

        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setColor(BACKGROUND_COLOR);

        texture = new Texture(Gdx.files.internal("sky.png"));
        texture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.ClampToEdge);

        skyHeight = cameraHeight * 0.25f;
        skyWidth = skyHeight * ((float)texture.getWidth() / (float)texture.getHeight());

        position = new Vector2();
    }

    public void render(SpriteBatch batch)
    {
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.rect(camera.position.x - camera.viewportWidth * 0.5f, camera.position.y - camera.viewportHeight * 0.5f, camera.viewportWidth, camera.viewportHeight);
        shapeRenderer.end();

        float mul = (float) Math.ceil((camera.viewportWidth + Math.abs(position.x)) / skyWidth);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(texture, position.x, position.y, skyWidth * mul, skyHeight, 0, 0, (int) (texture.getWidth() * mul), texture.getHeight(), false, false);
        batch.end();


    }

    public void update(float delta)
    {
        position.x -= BACKGROUND_SPEED * delta;
    }

    public void dispose()
    {
        texture.dispose();
        shapeRenderer.dispose();
    }
}
