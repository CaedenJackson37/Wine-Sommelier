package io.github.some_example_name.tools;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class DayNightCycle {

    private float timeOfDay;
    private float cycleSpeed = 10f;

    public void update(float delta) {
        timeOfDay += delta * cycleSpeed;
        if (timeOfDay >= 24f) {
            timeOfDay -= 24f;
        }
    }

    public float getTimeOfDay() {
        return timeOfDay;
    }

    public Color getTintColor() {
        if (timeOfDay >= 6f && timeOfDay < 18f) {
            return new Color(0, 0, 0, 0f);
        } else if (timeOfDay >= 18f && timeOfDay < 20f) {
            return new Color(0, 0, 0.2f, 0.3f);
            } else if (timeOfDay >= 20f || timeOfDay < 4f) {
            return new Color(0, 0, 0.4f, 0.5f);
        } else {
            return new Color(0, 0, 0.1f, 0.2f);
        }
    }

    public void renderOverlay(ShapeRenderer renderer, float screenWidth, float screenHeight) {
        Color tint = getTintColor();
        if (tint.a == 0.f) return;

        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.setColor(tint);
        renderer.rect(0, 0, screenWidth, screenHeight);
        renderer.end();
    }

    public void setTimeOfDay(float hours) {
        this.timeOfDay = hours % 24f;
    }
}
