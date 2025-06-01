package io.github.some_example_name.buildings;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class Winery {
    private Sprite sprite;
    private Rectangle doorBounds;
    private Rectangle bounds;

    public Winery(Texture texture, float x, float y) {
        sprite = new Sprite(texture);
        sprite.setPosition(x, y);

        //Edit the doorwidth and height smaller numbers for a bigger door box
        float doorWidth = sprite.getWidth() / 2f;
        float doorHeight = sprite.getHeight() / 2f;
        float doorX = x + sprite.getWidth() / 2f - doorWidth / 2f;
        float doorY = y;

        //Bounds for Winery and Winery door
        doorBounds = new Rectangle(doorX, doorY, doorWidth, doorHeight);
        bounds = new Rectangle(x, y, sprite.getWidth(), sprite.getHeight());
    }

    //Draws the winery onto the map
    public void render(SpriteBatch batch) {
        sprite.draw(batch);
    }

    //Collision for Winery door
    public Rectangle getDoorBounds() {
        return doorBounds;
    }

    //Collision for Winery
    public Rectangle getBounds() {
        return bounds;
    }

    public void dispose() {
        sprite.getTexture().dispose();
    }
}
