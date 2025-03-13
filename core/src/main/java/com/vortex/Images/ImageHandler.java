package com.vortex.Images;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class ImageHandler {
    private Texture texture;

    public ImageHandler(String imagePath) {
        texture = new Texture(Gdx.files.internal(imagePath));
    }

    public void render(SpriteBatch batch, float x, float y, float width, float height) {
        batch.draw(texture, x, y, 200, 340);
    }

    public void dispose() {
        texture.dispose();
    }
}
