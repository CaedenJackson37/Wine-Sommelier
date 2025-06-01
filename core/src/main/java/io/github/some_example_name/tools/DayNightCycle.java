package io.github.some_example_name.tools;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class DayNightCycle {

    private float timeOfDay;
    private float cycleSpeed = 10f;
    private GameTime gameTime = new GameTime();

    public void update(float delta) {
        timeOfDay += delta * cycleSpeed;
        if (timeOfDay >= 24f) {
            timeOfDay -= 24f;
        }
    }

    public float getTimeOfDay() {
        return timeOfDay;
    }

    /*public Color getTintColor() {
        double z = Math.cos((gameTime.getHours()-14) * Math.PI / 12);
        float b = (float) (0.3f + 0.7f * (z + 1.0) / 2.0);

        return new Color(b, b, b, 1.0f);
    }

    public void renderOverlay(ShapeRenderer renderer, float screenWidth, float screenHeight) {
        Color tint = getTintColor();
        if (tint.a == 0.f) return;

        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.setColor(tint);
        renderer.rect(0, 0, screenWidth, screenHeight);
        renderer.end();
    }*/

    public void setTimeOfDay(float hours) {
        this.timeOfDay = hours % 24f;
    }
}
