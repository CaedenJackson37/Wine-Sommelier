package io.github.some_example_name.tools.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

Skin skin = new Skin(Gdx.files.internal("craftacular-ui.json"));


public void PauseMenu() {
    pauseMenu = new Table();
    pauseMenu.setFillParent(true);
    pauseMenu.setVisible(false); // Start hidden

    Label title = new Label("Paused", skin, "title");
    TextButton resumeButton = new TextButton("Resume", skin);
    TextButton quitButton = new TextButton("Quit", skin);

    resumeButton.addListener(new ClickListener() {
        @Override
        public void clicked(InputEvent event, float x, float y) {
            paused = false;
            pauseMenu.setVisible(false);
            Gdx.input.setInputProcessor(gameStage); // back to game input
        }
    });

    quitButton.addListener(new ClickListener() {
        @Override
        public void clicked(InputEvent event, float x, float y) {
            Gdx.app.exit();
        }
    });

    pauseMenu.add(title).padBottom(20).row();
    pauseMenu.add(resumeButton).padBottom(10).row();
    pauseMenu.add(quitButton).padBottom(10).row();

    uiStage.addActor(pauseMenu);
}
