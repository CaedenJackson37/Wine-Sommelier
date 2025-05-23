package io.github.some_example_name;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import io.github.some_example_name.entity.Player;
import io.github.some_example_name.tools.GameCamera;

public class Main extends ApplicationAdapter {
    private SpriteBatch batch;
    private Player player;

    public TiledMap map;
    private OrthogonalTiledMapRenderer mapRenderer;
    private GameCamera gameCamera;

    public float worldWidth;
    private float worldHeight;

    @Override
    public void create() {
        // Load map
        map = new TmxMapLoader().load("map1.tmx");

        // Assume tile size is 16x16 pixels and map is 32x20 tiles
        int tileWidth = 16;
        int tileHeight = 16;
        int mapWidthInTiles = 50;
        int mapHeightInTiles = 32;

        worldWidth = mapWidthInTiles * tileWidth;
        worldHeight = mapHeightInTiles * tileHeight;

        gameCamera = new GameCamera(worldWidth, worldHeight);

        mapRenderer = new OrthogonalTiledMapRenderer(map);

        batch = new SpriteBatch();

        player = new Player("player_walk.png", 400, 256);
    }

    @Override
    public void render() {
        player.handleInput(Gdx.graphics.getDeltaTime());
        player.clampToMapBounds(worldWidth, worldHeight);
        gameCamera.follow(player.getCenterX(), player.getCenterY());

        // Clamp camera position so it stays inside the map
        gameCamera.clampToWorldBounds();

        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);

        mapRenderer.setView(gameCamera.getCamera());
        mapRenderer.render();

        batch.setProjectionMatrix(gameCamera.getCamera().combined);
        batch.begin();

        // Use the new draw method instead of sprite.draw()
        player.draw(batch);

        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        gameCamera.getViewport().update(width, height);
    }

    @Override
    public void dispose() {
        batch.dispose();
        player.dispose();
        if (map != null) map.dispose();
        if (mapRenderer != null) mapRenderer.dispose();
    }
}
