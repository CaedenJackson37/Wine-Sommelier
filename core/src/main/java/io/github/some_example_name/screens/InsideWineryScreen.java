package io.github.some_example_name.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.ScreenUtils;
import io.github.some_example_name.Main;

public class InsideWineryScreen implements Screen {

    private Main game;

    public InsideWineryScreen(Main game) {
        this.game = game;
    }

    @Override
    public void show() {
        // Called when this screen becomes the current screen.
    }

    @Override
    public void render(float delta) {
        // Clear the screen with a dark color for indoors
        ScreenUtils.clear(0.1f, 0.1f, 0.1f, 1f);
    }

    @Override
    public void resize(int width, int height) {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        // Clean up resources here if needed
    }
}


