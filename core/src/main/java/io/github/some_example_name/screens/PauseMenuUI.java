package io.github.some_example_name.tools.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class PauseMenuUI {
    private final Table pauseTable;
    private final Stage uiStage;
    private boolean paused = false;

    public PauseMenuUI(Skin skin, Stage uiStage) {
        this.uiStage = uiStage;
        pauseTable = new Table();
        pauseTable.setFillParent(true);
        pauseTable.center();

        Label title = new Label("Paused", skin);
        title.setFontScale(1.5f);

        TextButton resumeButton = new TextButton("Resume", skin);
        resumeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                togglePause();
            }
        });

        TextButton quitButton = new TextButton("Quit", skin);
        quitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        pauseTable.add(title).padBottom(20f).row();
        pauseTable.add(resumeButton).padBottom(10f).row();
        pauseTable.add(quitButton);

        pauseTable.setVisible(false);
        uiStage.addActor(pauseTable);
    }

    public void togglePause() {
        paused = !paused;
        pauseTable.setVisible(paused);
        Gdx.input.setInputProcessor(paused ? uiStage : null); // You can reassign gameStage here if needed
    }

    public boolean isPaused() {
        return paused;
    }
}
