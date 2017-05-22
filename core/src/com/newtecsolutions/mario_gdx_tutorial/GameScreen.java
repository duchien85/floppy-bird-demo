package com.newtecsolutions.mario_gdx_tutorial;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

/**
 * Created by pedja on 5/22/17 9:35 AM.
 * This class is part of the mario_gdx_tutorial
 * Copyright © 2017 ${OWNER}
 */

public class GameScreen implements Screen, InputProcessor
{
    private SpriteBatch batch;

    private OrthographicCamera camera;

    private Bird bird;

    private Background background;

    private Array<Pipe> pipes = new Array<Pipe>();

    @Override
    public void show()
    {
        batch = new SpriteBatch();

        setupCamera();

        bird = new Bird(3, 5);

        background = new Background(camera.viewportWidth, camera.viewportHeight);

        Gdx.input.setInputProcessor(this);
    }

    private void setupCamera()
    {
        if(camera == null)
            camera = new OrthographicCamera();
        final float cameraWidth = 16;
        final float cameraHeight = cameraWidth / ((float) Gdx.graphics.getWidth() / (float)Gdx.graphics.getHeight());

        camera.setToOrtho(false, cameraWidth, cameraHeight);
        camera.update();
    }

    @Override
    public void render(float delta)
    {
        if(delta > 0.016f)
            delta = 0.016f;
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        background.render(batch);
        background.update(delta);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        bird.render(batch);
        for(Pipe pipe : pipes)
        {
            pipe.render(batch);
        }

        batch.end();

        bird.update(delta);

        bird.position.x += delta;
        camera.position.x += delta;
        camera.update();

        addPipes();
    }

    private void addPipes()
    {
        /*while(pipes.size == 0 || pipes.get(pipes.size - 1).bounds.x < camera.position.x + camera.viewportWidth * 0.5f)
        {
            if(pipes.size == 0)
            {
                Pipe pipe = new Pipe(7)
            }
            Pipe last = pipes.get(pipes.size - 1);

            Pipe pipe = new Pipe()
        }*/
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
        bird.dispose();
    }

    @Override
    public boolean keyDown(int keycode)
    {
        return false;
    }

    @Override
    public boolean keyUp(int keycode)
    {
        return false;
    }

    @Override
    public boolean keyTyped(char character)
    {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button)
    {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button)
    {
        bird.jump();
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer)
    {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY)
    {
        return false;
    }

    @Override
    public boolean scrolled(int amount)
    {
        return false;
    }
}
