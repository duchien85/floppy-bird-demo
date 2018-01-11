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

public class SplashScreen implements Screen {
    private static final float SPLASH_TIME = 2;
    private SpriteBatch batch;
    private Texture img;

    private OrthographicCamera camera;

    private float logoWidth, logoHeight;

    private float stateTime;

    @Override
    public void show() {
        batch = new SpriteBatch();
        img = new Texture("libgdx.jpg");
        img.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        setupCamera();

        if (Utility.isPortrait()) {
            logoWidth = camera.viewportWidth;
            logoHeight = logoWidth / ((float) img.getWidth() / (float) img.getHeight());
        } else {
            logoHeight = camera.viewportHeight * .5f;
            logoWidth = logoWidth * ((float) img.getWidth() / (float) img.getHeight());
        }
    }

    private void setupCamera() {
        if (camera == null)
            camera = new OrthographicCamera();

        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.update();
    }

    @Override
    public void render(float delta) {
        if (stateTime >= SPLASH_TIME) {
            FlappyBirdGame.getInstance().setScreen(new GameScreen());
        }
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(img, camera.position.x - logoWidth * 0.5f, camera.position.y - logoHeight * 0.5f, logoWidth, logoHeight);
        batch.end();

        stateTime += delta;
    }

    @Override
    public void resize(int width, int height) {
        setupCamera();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        batch.dispose();
        img.dispose();
    }
}
