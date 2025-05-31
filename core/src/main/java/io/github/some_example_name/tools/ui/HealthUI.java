package io.github.some_example_name.tools.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import io.github.some_example_name.entity.Player;

public class HealthUI {
    private ShapeRenderer shapeRenderer;
    private Label healthLabel;
    private Vector3 mouseCoords;

    public HealthUI(Skin skin) {
        shapeRenderer = new ShapeRenderer();
        healthLabel = new Label("", skin);
        mouseCoords = new Vector3();
    }

    public void render(SpriteBatch batch, Player player, float screenWidth, float screenHeight) {
        // Health bar dimensions and position
        float barWidth = 20f;
        float barHeight = 200f;
        float barX = screenWidth - barWidth - 10f;
        float barY = 10f;

        float healthPercentage = player.getHealthPercentage();
        float filledHeight = barHeight * healthPercentage;

        // Draw health bar background and filled portion
        batch.end();
        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        shapeRenderer.setColor(Color.DARK_GRAY);
        shapeRenderer.rect(barX, barY, barWidth, barHeight);

        Color healthColor = healthPercentage > 0.6f ? Color.GREEN :
            healthPercentage > 0.3f ? Color.YELLOW : Color.RED;
        shapeRenderer.setColor(healthColor);
        shapeRenderer.rect(barX, barY, barWidth, filledHeight);

        shapeRenderer.end();

        // Draw white border
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.rect(barX, barY, barWidth, barHeight);
        shapeRenderer.end();
        batch.begin();

        // Get mouse position (convert Y axis since LibGDX origin is bottom-left)
        mouseCoords.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        Vector3 worldMouse = mouseCoords;

        // Check if mouse is hovering over the health bar
        float mouseX = worldMouse.x;
        float mouseY = screenHeight - worldMouse.y; // Convert from screen to world Y

        boolean isHovering = mouseX >= barX && mouseX <= (barX + barWidth) &&
            mouseY >= barY && mouseY <= (barY + barHeight);

        // Show label only if hovering
        if (isHovering) {
            healthLabel.setText("Health: " + player.getHealth() + " / 100");
            healthLabel.setFontScale(0.75f); // Reduce label size
            healthLabel.setPosition(barX - 110f, barY + barHeight / 2f - healthLabel.getPrefHeight() / 2f);
            healthLabel.draw(batch, 1f);
        }
    }

    public void dispose() {
        shapeRenderer.dispose();
    }
}
