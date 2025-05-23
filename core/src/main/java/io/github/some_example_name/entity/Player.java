package io.github.some_example_name.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Player {
    private static final float FRAME_DURATION = 0.15f;

    private TextureRegion[] walkFrames;

    private Texture walkSheet;
    private Animation<TextureRegion> walkAnimation;
    private float stateTime = 0f;

    private float x, y;
    private float speed = 50f;
    private int frameWidth = 16;
    private int frameHeight = 16;

    public Player(String walkSheetPath, float startX, float startY) {
        walkSheet = new Texture(walkSheetPath);

        // Split the sheet into frames (assumes 4 frames horizontally)
        TextureRegion[][] tmpFrames = TextureRegion.split(walkSheet, frameWidth, frameHeight);
        int frameCount = tmpFrames[0].length;  // how many frames in first row
        walkFrames = new TextureRegion[frameCount];

        for (int i = 0; i < frameCount; i++) {
            walkFrames[i] = tmpFrames[0][i];
        }

        walkAnimation = new Animation<>(FRAME_DURATION, walkFrames);
        x = startX;
        y = startY;
    }

    public void handleInput(float delta) {
        float dx = 0, dy = 0;

        if (Gdx.input.isKeyPressed(Input.Keys.W)) dy += speed * delta;
        if (Gdx.input.isKeyPressed(Input.Keys.S)) dy -= speed * delta;
        if (Gdx.input.isKeyPressed(Input.Keys.A)) dx -= speed * delta;
        if (Gdx.input.isKeyPressed(Input.Keys.D)) dx += speed * delta;

        boolean isMoving = (dx != 0 || dy != 0);

        x += dx;
        y += dy;

        if(isMoving) {
            stateTime += delta;
        } else {
            stateTime = 0f;
        }
    }

    public void clampToMapBounds(float worldWidth, float worldHeight) {
        x = Math.max(0, Math.min(x, worldWidth - frameWidth));
        y = Math.max(0, Math.min(y, worldHeight - frameHeight));
    }

    public void draw(SpriteBatch batch) {
        TextureRegion currentFrame = walkAnimation.getKeyFrame(stateTime, true);
        batch.draw(currentFrame, x, y);
    }

    public void dispose() {
        walkSheet.dispose();
    }

    public float getCenterX() {
        return x + frameWidth / 2f;
    }

    public float getCenterY() {
        return y + frameHeight / 2f;
    }
}
