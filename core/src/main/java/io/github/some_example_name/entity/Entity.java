package io.github.some_example_name.entity;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.Texture;

public class Entity {
    protected Texture texture;
    protected Sprite sprite;

    public Entity(String texturePath, float x, float y) {
        this.texture = new Texture(texturePath);
        this.sprite = new Sprite(texture);
        this.sprite.setPosition(x, y);
    }

    public Sprite getSprite() {
        return sprite;
    }

    public void dispose() {
        texture.dispose();
    }
}
