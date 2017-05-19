package com.newtecsolutions.mario_gdx_tutorial;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by pedja on 5/19/17 10:40 AM.
 * This class is part of the mario_gdx_tutorial
 * Copyright © 2017 ${OWNER}
 */

public class SplashScreen implements Screen
{
    private SpriteBatch batch;
    private Texture img;

    private OrthographicCamera camera;

    private float logoWidth, logoHeight;

    @Override
    public void show()
    {
        batch = new SpriteBatch();
        img = new Texture("libgdx.jpg");
        img.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        setupCamera();

        logoWidth = 10;
        logoHeight = logoWidth / ((float)img.getWidth() / (float)img.getHeight());
    }

    private void setupCamera()
    {
        if(camera == null)
            camera = new OrthographicCamera();
        final float cameraWidth = 16;
        final float cameraHeight = cameraWidth / ((float)Gdx.graphics.getWidth() / (float)Gdx.graphics.getHeight());

        camera.setToOrtho(false, cameraWidth, cameraHeight);
        camera.update();
    }

    @Override
    public void render(float delta)
    {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(img, camera.position.x - logoWidth * 0.5f, camera.position.y - logoHeight * 0.5f, logoWidth, logoHeight);
        batch.end();
    }

    @Override
    public void resize(int width, int height)
    {
        setupCamera();
    }

    @Override
    public void pause()
    {

    }

    @Override
    public void resume()
    {

    }

    @Override
    public void hide()
    {

    }

    @Override
    public void dispose()
    {
        batch.dispose();
        img.dispose();
    }
}
