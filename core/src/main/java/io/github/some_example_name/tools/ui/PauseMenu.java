package io.github.some_example_name.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class PauseMenu extends Table {

    private boolean paused = false;

    public PauseMenu(Skin skin, Stage uiStage) {
        super();
        setFillParent(true);
        setVisible(false);

        Label title = new Label("Paused", skin, "title");
        TextButton resumeButton = new TextButton("Resume", skin);
        TextButton quitButton = new TextButton("Quit", skin);

        resumeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                resume();
            }
        });

        quitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        add(title).padBottom(20).row();
        add(resumeButton).padBottom(10).row();
        add(quitButton).padBottom(10).row();

        uiStage.addActor(this);
    }

    public boolean isPaused() {
        return paused;
    }

    public void togglePause(Stage gameStage) {
        paused = !paused;
        setVisible(paused);
        Gdx.input.setInputProcessor(paused ? getStage() : gameStage);
    }

    public void resume() {
        paused = false;
        setVisible(false);
        Gdx.input.setInputProcessor(getStage()); // GameStage should reset externally
    }
}
