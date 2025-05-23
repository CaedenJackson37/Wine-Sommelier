package io.github.some_example_name.tools;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class GameCamera {
    private OrthographicCamera camera;
    private FitViewport viewport;
    private float worldWidth, worldHeight;

    public GameCamera(float worldWidth, float worldHeight) {
        this.worldWidth = worldWidth;
        this.worldHeight = worldHeight;
        camera = new OrthographicCamera();
        viewport = new FitViewport(worldWidth, worldHeight, camera);
        viewport.apply();
        camera.zoom = 0.5f;

        centerCamera();
    }

    public void centerCamera() {
        camera.position.set(worldWidth / 2f, worldHeight / 2f, 0);
        camera.update();
    }

    public void follow(float x, float y) {
        camera.position.set(x, y, 0);
        clampToWorldBounds();
    }

    public void clampToWorldBounds() {
        float camHalfWidth = (camera.viewportWidth * camera.zoom) / 2f;
        float camHalfHeight = (camera.viewportHeight * camera.zoom) / 2f;

        float camX = Math.min(Math.max(camera.position.x, camHalfWidth), worldWidth - camHalfWidth);
        float camY = Math.min(Math.max(camera.position.y, camHalfHeight), worldHeight - camHalfHeight);

        camera.position.set(camX, camY, 0);
        camera.update();
    }

    public OrthographicCamera getCamera() {
        return camera;
    }

    public FitViewport getViewport() {
        return viewport;
    }
}
