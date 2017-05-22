package com.newtecsolutions.mario_gdx_tutorial;

import com.badlogic.gdx.Game;

public class SuperMarioTutorial extends Game
{
    private static SuperMarioTutorial instance;

    public static SuperMarioTutorial getInstance()
    {
        return instance;
    }

    @Override
    public void create()
    {
        instance = this;
        setScreen(new GameScreen());
    }

    @Override
    public void dispose()
    {
        instance = null;
        super.dispose();

    }
}
