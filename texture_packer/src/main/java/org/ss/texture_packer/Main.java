package org.ss.texture_packer;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;

public class Main
{
    public static void main(String[] args)
    {
        TexturePacker.Settings settings = new TexturePacker.Settings();
        settings.filterMag = Texture.TextureFilter.Linear;
        settings.filterMin = Texture.TextureFilter.Linear;
        settings.duplicatePadding = true;
        settings.maxHeight = 2048;
        settings.maxWidth = 2048;
        TexturePacker.process(settings, "./android/assets", "./android/assets", "assets.atlas");
    }
}
