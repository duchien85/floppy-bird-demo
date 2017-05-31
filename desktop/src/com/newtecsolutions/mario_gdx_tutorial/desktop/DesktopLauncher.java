package com.newtecsolutions.mario_gdx_tutorial.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.newtecsolutions.mario_gdx_tutorial.FlappyBirdGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 960;
		config.height = 540;
		config.allowSoftwareMode = false;
		new LwjglApplication(new FlappyBirdGame(), config);
	}
}
