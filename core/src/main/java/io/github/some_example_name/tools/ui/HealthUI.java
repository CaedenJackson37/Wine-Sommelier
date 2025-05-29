package io.github.some_example_name.tools.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import io.github.some_example_name.entity.Player;

public class HealthUI {
    private ShapeRenderer shapeRenderer;
    private Label healthLabel;

    public HealthUI(Skin skin) {
        shapeRenderer = new ShapeRenderer();
        healthLabel = new Label("", skin);
    }

    public void render(SpriteBatch batch, Player player, float screenWidth, float screenHeight) {
        // Draw health bar in top-left corner
        batch.end(); // End sprite batch to use shape renderer

        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        float barWidth = 200f;
        float barHeight = 20f;
        float barX = 20f;
        float barY = screenHeight - 40f;

        // Background
        shapeRenderer.setColor(Color.DARK_GRAY);
        shapeRenderer.rect(barX, barY, barWidth, barHeight);

        // Health bar
        float healthPercentage = player.getHealthPercentage();
        Color healthColor = healthPercentage > 0.6f ? Color.GREEN :
            healthPercentage > 0.3f ? Color.YELLOW : Color.RED;
        shapeRenderer.setColor(healthColor);
        shapeRenderer.rect(barX, barY, barWidth * healthPercentage, barHeight);

        // Border
        shapeRenderer.end();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.rect(barX, barY, barWidth, barHeight);

        shapeRenderer.end();
        batch.begin(); // Resume sprite batch

        // Health text
        healthLabel.setText("Health: " + player.getHealth() + "/" + player.getMaxHealth());
        healthLabel.setPosition(barX, barY - 25f);
        healthLabel.draw(batch, 1f);
    }

    public void dispose() {
        shapeRenderer.dispose();
    }
}
