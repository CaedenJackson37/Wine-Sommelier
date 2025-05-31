package io.github.some_example_name.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.some_example_name.Main;
import io.github.some_example_name.entity.Player;
import io.github.some_example_name.tools.DayNightCycle;
import io.github.some_example_name.tools.GameCamera;
import io.github.some_example_name.tools.GameTime;
import io.github.some_example_name.tools.ui.HealthUI;
import io.github.some_example_name.tools.ui.GameTimeUI;
import io.github.some_example_name.tools.ui.PauseMenuUI;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class MainGameScreen implements Screen {

    private SpriteBatch batch;
    private Player player;

    private Stage gameStage;
    private Stage uiStage;

    private DayNightCycle dayNightCycle;
    private ShapeRenderer shapeRenderer;

    private PauseMenuUI pauseMenuUI;


    private Skin skin;

    private GameTime gameTime = new GameTime();
    private GameTimeUI gameTimeUI; // NEW

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

        dayNightCycle = new DayNightCycle();
        shapeRenderer = new ShapeRenderer();

        // Start with game stage as input processor
        Gdx.input.setInputProcessor(gameStage);

        skin = new Skin(Gdx.files.internal("craftacular-ui.json"));

        healthUI = new HealthUI(skin);
        gameTimeUI = new GameTimeUI(skin); // NEW

        pauseMenuUI = new PauseMenuUI(skin, uiStage);
    }

    @Override
    public void show() {
        // Load map
        map = new TmxMapLoader().load("map1.tmx");

        int tileWidth = 16;
        int tileHeight = 16;
        int mapWidthInTiles = 50;
        int mapHeightInTiles = 32;

        worldWidth = mapWidthInTiles * tileWidth;
        worldHeight = mapHeightInTiles * tileHeight;

        gameCamera = new GameCamera(worldWidth, worldHeight);
        mapRenderer = new OrthogonalTiledMapRenderer(map);

        batch = new SpriteBatch();
        player = new Player(game, "walk_sheet.png", 400, 256);
    }

    @Override
    public void render(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            pauseMenuUI.togglePause();
        }

        if (!pauseMenuUI.isPaused()) {
            player.update(delta);
            player.clampToMapBounds(worldWidth, worldHeight);
            gameCamera.follow(player.getCenterX(), player.getCenterY());
            gameCamera.clampToWorldBounds();
            gameStage.act(delta);
        }

        uiStage.act(delta);
        gameTime.update(delta); // Update game time

        dayNightCycle.setTimeOfDay(gameTime.getHours());

        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);

        mapRenderer.setView(gameCamera.getCamera());
        mapRenderer.render();

        batch.setProjectionMatrix(gameCamera.getCamera().combined);
        batch.begin();
        player.draw(batch);
        batch.end();

        gameStage.draw();

        dayNightCycle.update(delta);
        shapeRenderer.setProjectionMatrix(uiStage.getCamera().combined);
        dayNightCycle.renderOverlay(shapeRenderer, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());


        batch.setProjectionMatrix(uiStage.getCamera().combined);
        batch.begin();

        healthUI.render(batch, player, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        gameTimeUI.render(batch, gameTime, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()); // NEW

        batch.end();

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
        healthUI.dispose();
        if (map != null) map.dispose();
        if (mapRenderer != null) mapRenderer.dispose();
    }
}
