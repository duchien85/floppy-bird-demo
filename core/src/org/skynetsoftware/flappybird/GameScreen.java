package org.skynetsoftware.flappybird;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.profiling.GLProfiler;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

import java.util.Iterator;

/**
 * Created by pedja on 5/22/17 9:35 AM.
 * This class is part of the mario_gdx_tutorial
 * Copyright © 2017 ${OWNER}
 */

public class GameScreen implements Screen, InputProcessor
{
    private static final float UPDATE_DEBUG_TEXT_INTERVAL = 1;

    private SpriteBatch batch;

    private OrthographicCamera camera, hudCamera;

    private Bird bird;

    private Background background;

    private Array<Pipe> pipes = new Array<Pipe>();
    private Array<ScoreColider> scoreColiders = new Array<ScoreColider>();

    private enum GameState
    {
        Idle, Running, Paused, GameOver
    }

    private GameState gameState = GameState.Idle;

    private TextureRegion gameOver;
    private float gameOverWidth, gameOverHeight;

    private TextureRegion replay;
    private float replayWidth, replayHeight;
    private Rectangle replayBounds = new Rectangle();

    private int score = 0;

    private Pool<Pipe> pipePool = new Pool<Pipe>()
    {
        @Override
        protected Pipe newObject()
        {
            return new Pipe(0, 0, 0, true);
        }
    };

    private Pool<ScoreColider> colliderPool = new Pool<ScoreColider>()
    {
        @Override
        protected ScoreColider newObject()
        {
            return new ScoreColider(0, 0, 0);
        }
    };

    private BitmapFont debugFont;

    private TextureRegion[] fontRegions = new TextureRegion[10];

    private float stateTime;
    private float lastDebugDrawTime;
    private String debugMessage;

    @Override
    public void show()
    {
        GLProfiler.enable();
        batch = new SpriteBatch();

        setupCamera();

        hudCamera = new OrthographicCamera();
        hudCamera.setToOrtho(false, camera.viewportWidth, camera.viewportHeight);
        hudCamera.update();

        bird = new Bird();

        background = new Background(camera.viewportWidth, camera.viewportHeight);

        gameOver = FlappyBirdGame.getInstance().getAssets().findRegion("game_over");

        if (isPortrait()) {
            gameOverWidth = camera.viewportWidth * 0.75f;
            gameOverHeight = gameOverWidth / ((float)gameOver.getRegionWidth() / (float)gameOver.getRegionHeight());
        }
        else {
            gameOverHeight = camera.viewportHeight * 0.25f;
            gameOverWidth = gameOverHeight * ((float)gameOver.getRegionWidth() / (float)gameOver.getRegionHeight());
        }

        replay = FlappyBirdGame.getInstance().getAssets().findRegion("replay");

        replayHeight = camera.viewportHeight * 0.1f;
        replayWidth = replayHeight * ((float)replay.getRegionWidth() / (float)replay.getRegionHeight());

        for(int i = 0; i < 10; i++)
        {
            fontRegions[i] = FlappyBirdGame.getInstance().getAssets().findRegion("font_big", i);
        }

        Gdx.input.setInputProcessor(this);

        debugFont = new BitmapFont();
        debugFont.setColor(Color.RED);
        debugFont.setUseIntegerPositions(false);
        debugFont.getData().setScale(0.02f);
    }

    private void setupCamera()
    {
        if(camera == null)
            camera = new OrthographicCamera();
        float cameraHeight;
        float cameraWidth;

        if(isPortrait())
        {
            cameraWidth = 8;
        }
        else
        {
            cameraWidth = 16;
        }
        cameraHeight = cameraWidth / ((float) Gdx.graphics.getWidth() / (float)Gdx.graphics.getHeight());

        camera.setToOrtho(false, cameraWidth, cameraHeight);
        camera.update();
    }

    private boolean isPortrait() {
        return Utility.isPortrait();
    }

    @Override
    public void render(float delta)
    {
        if(delta > 0.016f)
            delta = 0.016f;
        stateTime += delta;
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

        batch.setProjectionMatrix(hudCamera.combined);
        batch.begin();

        //draw score
        drawScore(score);

        batch.end();

        update(delta);

        batch.setProjectionMatrix(hudCamera.combined);
        batch.begin();

        drawDebugText();

        batch.end();

        autoPilot();
        GLProfiler.reset();
    }

    private void drawDebugText()
    {
        if(!Debug.DEBUG)
            return;
        if(debugMessage == null || stateTime - lastDebugDrawTime > UPDATE_DEBUG_TEXT_INTERVAL)
        {
            debugMessage = generateDebugMessage();
            lastDebugDrawTime = stateTime;
        }
        debugFont.draw(batch, debugMessage, 0.5f, hudCamera.viewportHeight - 0.5f);
    }

    private String generateDebugMessage()
    {
        return "Camera: width=" + camera.viewportWidth + ", height=" + camera.viewportHeight
                + "\n" + "Camera: x=" + camera.position.x + ", y=" + camera.position.y
                + "\n" + "Bird: x=" + bird.position.x + ", y=" + bird.position.y
                + "\n" + "Bird Vel: x=" + bird.velocity.x + ", y=" + bird.velocity.y
                + "\n" + "Pipes: " + pipes.size
                + "\n" + "Colliders: " + scoreColiders.size
                + "\n" + "JavaHeap: " + Gdx.app.getJavaHeap() / 1000000 + "MB"
                + "\n" + "NativeHeap: " + Gdx.app.getNativeHeap() / 1000000 + "MB"
                + "\n" + "OGL Draw Calls: " + GLProfiler.drawCalls
                + "\n" + "OGL TextureBindings: " + GLProfiler.textureBindings
                + "\n" + "Screen w=" + Gdx.graphics.getWidth() + "h=" + Gdx.graphics.getHeight()
                + "\n" + "FPS: " + Gdx.graphics.getFramesPerSecond();
    }


    private void autoPilot()
    {
        if(gameState != GameState.Running || !Debug.DEBUG)
            return;
        Pipe closestBottomPipe = getClosestBottomPipe();
        if(closestBottomPipe == null)
            return;

        if(bird.position.y <= 0.2f + closestBottomPipe.bounds.y + closestBottomPipe.bounds.height)
            bird.jump();
    }

    private Pipe getClosestBottomPipe()
    {
        float minDistance = Float.MAX_VALUE;
        Pipe closestPipe = null;
        for(int i = pipes.size - 1; i >= 0; i--)
        {
            Pipe pipe = pipes.get(i);
            if(pipe.bounds.x + pipe.bounds.width < bird.position.x)
                break;
            if(!pipe.isTop())
            {
                float distance = (pipe.bounds.x + pipe.bounds.width) - bird.position.x;
                if(distance < minDistance)
                {
                    minDistance = distance;
                    closestPipe = pipe;
                }
            }
        }
        return closestPipe;
    }

    private void drawScore(int score)
    {
        String strScore = String.valueOf(score);
        char[] chars = strScore.toCharArray();

        float charHeight = 1;
        float charWidth = charHeight * fontRegions[0].getRegionWidth()/fontRegions[0].getRegionHeight();

        float spaceWidth = 0.6f;

        float width = charWidth * chars.length + spaceWidth * (chars.length - 1);

        float offset = hudCamera.viewportWidth / 2 - width / 2;

        for(int i = 0; i < chars.length; i++)
        {
            char scoreChar = chars[i];
            int index = ('0' <= scoreChar && scoreChar <= '9') ? scoreChar - '0' : scoreChar - 'A' + 10;
            batch.draw(fontRegions[index], offset, hudCamera.viewportHeight * 0.7f, charWidth, charHeight);
            offset += spaceWidth;
        }
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
        for(Iterator<Pipe> iterator = pipes.iterator(); iterator.hasNext();)
        {
            Pipe pipe = iterator.next();
            if(pipe.bounds.x + pipe.bounds.width < camera.position.x - camera.viewportWidth / 2)
            {
                iterator.remove();
                pipePool.free(pipe);
                continue;
            }
            if(pipe.bounds.overlaps(bird.getBounds()))
                die();
        }
        for(Iterator<ScoreColider> iterator = scoreColiders.iterator(); iterator.hasNext();)
        {
            ScoreColider scoreColider = iterator.next();
            if(scoreColider.bounds.x < bird.position.x)
            {
                iterator.remove();
                scoreColider.alreadyChecked = false;
                colliderPool.free(scoreColider);
                continue;
            }
            if(!scoreColider.alreadyChecked && scoreColider.bounds.overlaps(bird.getBounds()))
            {
                scoreColider.alreadyChecked = true;
                score++;
            }
        }
    }

    private void die()
    {
        gameState = GameState.GameOver;
    }

    private void restart()
    {
        pipes.clear();
        scoreColiders.clear();
        bird.reset();
        camera.position.x = camera.viewportWidth * 0.5f;
        camera.update();
        gameState = GameState.Idle;
        score = 0;
    }

    private void addPipes()
    {
        while(pipes.size == 0 || pipes.get(pipes.size - 1).bounds.x < camera.position.x + camera.viewportWidth * 0.5f)
        {
            float gapHeight = MathUtils.random(2.5f, 3);
            float gapStart = isPortrait() ? MathUtils.random(6, 9) : MathUtils.random(3, 6);
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
            Pipe pipeBottom = pipePool.obtain();
            pipeBottom.updateBounds(x, 0, gapStart, false);
            Pipe pipeTop = pipePool.obtain();
            pipeTop.updateBounds(x, gapStart + gapHeight, totalHeight - (gapStart + gapHeight), true);
            ScoreColider scoreColider = colliderPool.obtain();
            scoreColider.updateBounds(x, gapStart, gapHeight);
            scoreColiders.add(scoreColider);
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
        GLProfiler.disable();
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
