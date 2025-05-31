package io.github.some_example_name.tools.ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import io.github.some_example_name.tools.GameTime;

public class GameTimeUI {
    private Label timeLabel;

    public GameTimeUI(Skin skin) {
        timeLabel = new Label("", skin);
        timeLabel.setFontScale(0.75f); // Adjust for smaller font
    }

    public void render(SpriteBatch batch, GameTime gameTime, float screenWidth, float screenHeight) {
        // Update label text
        timeLabel.setText(gameTime.getFormattedTime());

        // Top-left corner (adjust as needed)
        float padding = 10f;
        timeLabel.setPosition(padding, screenHeight - padding - timeLabel.getPrefHeight());

        // Draw the label
        timeLabel.draw(batch, 1f);
    }
}
