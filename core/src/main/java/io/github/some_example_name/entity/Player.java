package io.github.some_example_name.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Player {
    private static final float FRAME_DURATION = 0.15f;
    private static final float INVINCIBILITY_TIME = 1.0f; // 1 second of invincibility after taking damage

    private TextureRegion[] walkFrames;
    private Texture walkSheet;
    private Animation<TextureRegion> walkAnimation;
    private float stateTime = 0f;

    private float x, y;
    private float speed = 50f;
    private int frameWidth = 16;
    private int frameHeight = 16;

    // Health system
    private int health = 100;
    private int maxHealth = 100;
    private boolean isInvincible = false;
    private float invincibilityTimer = 0f;
    private float damageFlashTimer = 0f;
    private static final float DAMAGE_FLASH_DURATION = 0.1f;

    // Health regeneration
    private float healthRegenTimer = 0f;
    private static final float HEALTH_REGEN_INTERVAL = 5f; // Regen 1 HP every 5 seconds
    private static final int HEALTH_REGEN_AMOUNT = 1;

    public Player(String walkSheetPath, float startX, float startY) {
        walkSheet = new Texture(walkSheetPath);

        TextureRegion[][] tmpFrames = TextureRegion.split(walkSheet, frameWidth, frameHeight);
        int frameCount = tmpFrames[0].length;
        walkFrames = new TextureRegion[frameCount];

        for (int i = 0; i < frameCount; i++) {
            walkFrames[i] = tmpFrames[0][i];
        }

        walkAnimation = new Animation<>(FRAME_DURATION, walkFrames);
        x = startX;
        y = startY;
    }

    public void update(float delta) {
        handleInput(delta);
        updateHealthSystem(delta);
    }

    private void updateHealthSystem(float delta) {
        // Update invincibility timer
        if (isInvincible) {
            invincibilityTimer -= delta;
            if (invincibilityTimer <= 0) {
                isInvincible = false;
            }
        }

        // Update damage flash timer
        if (damageFlashTimer > 0) {
            damageFlashTimer -= delta;
        }

        // Health regeneration (only when not at max health)
        if (health < maxHealth && health > 0) {
            healthRegenTimer += delta;
            if (healthRegenTimer >= HEALTH_REGEN_INTERVAL) {
                heal(HEALTH_REGEN_AMOUNT);
                healthRegenTimer = 0f;
            }
        }
    }

    public void handleInput(float delta) {
        float dx = 0, dy = 0;

        if (Gdx.input.isKeyPressed(Input.Keys.W)) dy += speed * delta;
        if (Gdx.input.isKeyPressed(Input.Keys.S)) dy -= speed * delta;
        if (Gdx.input.isKeyPressed(Input.Keys.A)) dx -= speed * delta;
        if (Gdx.input.isKeyPressed(Input.Keys.D)) dx += speed * delta;

        // Debug keys for testing health system
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) {
            takeDamage(10); // Test damage
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) {
            heal(10); // Test healing
        }

        boolean isMoving = (dx != 0 || dy != 0);

        x += dx;
        y += dy;

        if(isMoving) {
            stateTime += delta;
        } else {
            stateTime = 0f;
        }
    }

    public void takeDamage(int damage) {
        if (isInvincible || health <= 0) {
            return; // Can't take damage while invincible or dead
        }

        health -= damage;
        health = Math.max(0, health); // Don't go below 0

        // Start invincibility period
        isInvincible = true;
        invincibilityTimer = INVINCIBILITY_TIME;

        // Start damage flash effect
        damageFlashTimer = DAMAGE_FLASH_DURATION;

        // Reset health regen timer (damage interrupts regen)
        healthRegenTimer = 0f;

        Gdx.app.log("Player", "Took " + damage + " damage. Health: " + health + "/" + maxHealth);

        if (health <= 0) {
            onDeath();
        }
    }

    public void heal(int healAmount) {
        if (health <= 0) {
            return; // Can't heal when dead
        }

        health += healAmount;
        health = Math.min(maxHealth, health); // Don't exceed max health

        Gdx.app.log("Player", "Healed " + healAmount + " HP. Health: " + health + "/" + maxHealth);
    }

    public void setMaxHealth(int newMaxHealth) {
        this.maxHealth = newMaxHealth;
        if (health > maxHealth) {
            health = maxHealth; // Adjust current health if it exceeds new max
        }
    }

    public void fullHeal() {
        health = maxHealth;
    }

    private void onDeath() {
        Gdx.app.log("Player", "Player has died!");
        // You can add death logic here, like:
        // - Play death animation
        // - Show game over screen
        // - Respawn at checkpoint
        // - etc.
    }

    public void clampToMapBounds(float worldWidth, float worldHeight) {
        x = Math.max(0, Math.min(x, worldWidth - frameWidth));
        y = Math.max(0, Math.min(y, worldHeight - frameHeight));
    }

    public void draw(SpriteBatch batch) {
        TextureRegion currentFrame = walkAnimation.getKeyFrame(stateTime, true);

        // Apply damage flash effect
        if (damageFlashTimer > 0) {
            batch.setColor(1f, 0.5f, 0.5f, 1f); // Red tint
        } else if (isInvincible) {
            // Flicker effect during invincibility
            float alpha = (float) Math.sin(invincibilityTimer * 20) * 0.5f + 0.5f;
            batch.setColor(1f, 1f, 1f, alpha);
        }

        batch.draw(currentFrame, x, y);
        batch.setColor(Color.WHITE); // Reset color
    }

    public void drawHealthBar(ShapeRenderer shapeRenderer) {
        if (health <= 0) return; // Don't draw health bar when dead

        float barWidth = 50f;
        float barHeight = 6f;
        float barX = x - (barWidth - frameWidth) / 2f;
        float barY = y + frameHeight + 5f;

        // Background (red)
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.rect(barX, barY, barWidth, barHeight);

        // Health bar (green)
        float healthPercentage = (float) health / maxHealth;
        shapeRenderer.setColor(Color.GREEN);
        shapeRenderer.rect(barX, barY, barWidth * healthPercentage, barHeight);

        // Border
        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.rect(barX - 1, barY - 1, barWidth + 2, barHeight + 2);
        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.rect(barX, barY, barWidth, barHeight);
    }

    public void dispose() {
        walkSheet.dispose();
    }

    // Getters
    public float getCenterX() { return x + frameWidth / 2f; }
    public float getCenterY() { return y + frameHeight / 2f; }
    public float getX() { return x; }
    public float getY() { return y; }
    public int getHealth() { return health; }
    public int getMaxHealth() { return maxHealth; }
    public boolean isAlive() { return health > 0; }
    public boolean isInvincible() { return isInvincible; }
    public float getHealthPercentage() { return (float) health / maxHealth; }

    // Setters
    public void setHealth(int health) {
        this.health = Math.max(0, Math.min(health, maxHealth));
    }
    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }
}
