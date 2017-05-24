package com.newtecsolutions.mario_gdx_tutorial;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
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

    private enum GameState
    {
        Idle, Running, Paused, GameOver
    }

    private GameState gameState = GameState.Idle;

    private Texture gameOver;
    private float gameOverWidth, gameOverHeight;

    private Texture replay;
    private float replayWidth, replayHeight;
    private Rectangle replayBounds = new Rectangle();

    @Override
    public void show()
    {
        batch = new SpriteBatch();

        setupCamera();

        bird = new Bird();

        background = new Background(camera.viewportWidth, camera.viewportHeight);

        gameOver = new Texture(Gdx.files.internal("game_over.png"));

        gameOverHeight = camera.viewportHeight * 0.25f;
        gameOverWidth = gameOverHeight * ((float)gameOver.getWidth() / (float)gameOver.getHeight());

        replay = new Texture(Gdx.files.internal("replay.png"));

        replayHeight = camera.viewportHeight * 0.1f;
        replayWidth = replayHeight * ((float)replay.getWidth() / (float)replay.getHeight());

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
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        background.render(batch);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        for(Pipe pipe : pipes)
        {
            pipe.render(batch);
        }
        bird.render(batch);

        if(gameState == GameState.GameOver)
        {
            float gameOverY = camera.position.y - gameOverHeight * 0.5f;
            batch.draw(gameOver, camera.position.x - gameOverWidth * 0.5f, gameOverY, gameOverWidth, gameOverHeight);
            replayBounds.set(camera.position.x - replayWidth * 0.5f, gameOverY - replayHeight, replayWidth, replayHeight);
            batch.draw(replay, replayBounds.x, replayBounds.y, replayBounds.width, replayBounds.height);
        }

        batch.end();

        update(delta);
    }

    private void update(float delta)
    {
        if(gameState == GameState.Running)
        {
            bird.update(delta);
            background.update(delta);
            bird.position.x += delta;
            camera.position.x += delta;
            camera.update();

            addPipes();

            detectCollision();
        }
    }

    private void detectCollision()
    {
        if(bird.position.y + 0.5f < 0 || bird.position.y > camera.viewportHeight)
            die();
        for(Pipe pipe : pipes)
        {
            if(pipe.bounds.overlaps(bird.getBounds()))
                die();
        }
    }

    private void die()
    {
        gameState = GameState.GameOver;
    }

    private void restart()
    {
        pipes.clear();
        bird.reset();
        camera.position.x = camera.viewportWidth * 0.5f;
        camera.update();
        gameState = GameState.Idle;
    }

    private void addPipes()
    {
        while(pipes.size == 0 || pipes.get(pipes.size - 1).bounds.x < camera.position.x + camera.viewportWidth * 0.5f)
        {
            float gapHeight = MathUtils.random(2.5f, 3);
            float gapStart = MathUtils.random(3, 6);
            float x;
            if(pipes.size == 0)
            {
                x = 7;
            }
            else
            {
                Pipe last = pipes.get(pipes.size - 1);

                x = last.bounds.x + last.bounds.width + MathUtils.random(1.5f, 3f);

            }
            float totalHeight = camera.viewportHeight;
            Pipe pipeBottom = new Pipe(x, 0, gapStart, false);
            Pipe pipeTop = new Pipe(x, gapStart + gapHeight, totalHeight - (gapStart + gapHeight), true);
            pipes.add(pipeBottom);
            pipes.add(pipeTop);
        }
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
        screenY = Gdx.graphics.getHeight() - screenY;//invert y

        float x = (camera.position.x - camera.viewportWidth * 0.5f) + ((float)screenX / ((float)Gdx.graphics.getWidth() / camera.viewportWidth));
        float y = ((float)screenY / ((float)Gdx.graphics.getHeight() / camera.viewportHeight));

        if(gameState == GameState.Idle)
        {
            gameState = GameState.Running;
        }
        else if(gameState == GameState.GameOver && replayBounds.contains(x, y))
        {
            restart();
        }
        else if(gameState == GameState.Running)
        {
            bird.jump();
        }
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
