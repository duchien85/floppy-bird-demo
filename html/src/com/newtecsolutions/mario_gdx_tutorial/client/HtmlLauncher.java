package com.newtecsolutions.mario_gdx_tutorial.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.newtecsolutions.mario_gdx_tutorial.FlappyBirdGame;

public class HtmlLauncher extends GwtApplication {

        @Override
        public GwtApplicationConfiguration getConfig () {
                return new GwtApplicationConfiguration(960, 540);
        }

        @Override
        public ApplicationListener createApplicationListener () {
                return new FlappyBirdGame();
        }
}