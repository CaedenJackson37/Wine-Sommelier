package io.github.some_example_name.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.some_example_name.Main;
import io.github.some_example_name.entity.Player;
import io.github.some_example_name.tools.GameCamera;
import io.github.some_example_name.tools.ui.HealthUI;
import io.github.some_example_name.ui.PauseMenu;

public class MainGameScreen implements Screen {

    private SpriteBatch batch;
    private Player player;

    private Stage gameStage;
    private Stage uiStage;

    private PauseMenu pauseMenu;
    private boolean paused = false;

    private Skin skin;


    private TiledMap map;
    private OrthogonalTiledMapRenderer mapRenderer;
    private GameCamera gameCamera;

    private float worldWidth;
    private float worldHeight;

    private HealthUI healthUI;
    private Main game;


    public MainGameScreen(Main game) {
        this.game = game;

        gameStage = new Stage(new ScreenViewport());
        uiStage = new Stage(new ScreenViewport());

        // Start with game stage as input processor
        Gdx.input.setInputProcessor(gameStage);

        skin = new Skin(Gdx.files.internal("craftacular-ui.json"));
        healthUI = new HealthUI(skin);

        pauseMenu = new PauseMenu(skin, uiStage);
    }


    @Override
    public void show() {
        // Load map
        map = new TmxMapLoader().load("map1.tmx");

        // Assume tile size and map dimensions
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
    public void render(float delta) {
        // Handle pause toggle
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            pauseMenu.togglePause(gameStage);
        }

        // Update game logic only when not paused
        if (!pauseMenu.isPaused()) {
            player.handleInput(delta);
            player.clampToMapBounds(worldWidth, worldHeight);
            gameCamera.follow(player.getCenterX(), player.getCenterY());
            gameCamera.clampToWorldBounds();
            gameStage.act(delta);
        }

        // Always update UI
        uiStage.act(delta);

        // Render everything
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);

        // Render map
        mapRenderer.setView(gameCamera.getCamera());
        mapRenderer.render();

        // Render game objects
        batch.setProjectionMatrix(gameCamera.getCamera().combined);
        batch.begin();
        player.draw(batch);
        batch.end();

        // Render game stage
        gameStage.draw();

        // Render UI with screen coordinates
        batch.setProjectionMatrix(uiStage.getCamera().combined);
        batch.begin();

        // Render health UI
        healthUI.render(batch, player, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        batch.end();

        // Render UI stage (includes pause menu)
        uiStage.draw();
    }

    @Override
    public void resize(int width, int height) {
        gameCamera.getViewport().update(width, height);
        gameStage.getViewport().update(width, height, true);
        uiStage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        batch.dispose();
        player.dispose();
        gameStage.dispose();
        uiStage.dispose();
        skin.dispose();
        healthUI.dispose(); // Fixed typo
        if (map != null) map.dispose();
        if (mapRenderer != null) mapRenderer.dispose();
    }
}
