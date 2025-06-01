package io.github.some_example_name.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import io.github.some_example_name.Main;
import io.github.some_example_name.screens.DeathScreen;

public class Player {
    private static final float FRAME_DURATION = 0.15f;

    private enum Direction { DOWN, LEFT, RIGHT, UP }

    private Direction currentDirection = Direction.DOWN;

    //Player animation textures
    private Animation<TextureRegion> walkDown;
    private Animation<TextureRegion> walkUp;
    private Animation<TextureRegion> walkLeft;
    private Animation<TextureRegion> walkRight;

    //Player texture
    private Texture walkSheet;
    private float stateTime = 0f;

    //Player speed and size
    private float x, y;
    private float speed = 50f;
    private int frameWidth = 16;
    private int frameHeight = 16;
    private float lastX, lastY;

    //Health System
    private int health = 100;
    private int maxHealth = 100;
    private boolean isInvincible = false;
    private float invincibilityTimer = 0f;
    private float damageFlashTimer = 0f;
    private static final float DAMAGE_FLASH_DURATION = 0.1f;
    private static final float INVINCIBILITY_TIME = 1.0f;

    //Box2D
    private Rectangle bounds;

    //Health regeneration
    private float healthRegenTimer = 0f;
    private static final float HEALTH_REGEN_INTERVAL = 5f;
    private static final int HEALTH_REGEN_AMOUNT = 1;

    private Main game;

    //Remove before posting
    private boolean debugMode = true;

    //Main
    public Player(Main game, String walkSheetPath, float startX, float startY) {
        this.game = game;
        this.walkSheet = new Texture(walkSheetPath);

        TextureRegion[][] tmp = TextureRegion.split(walkSheet, frameWidth, frameHeight);

        this.x = startX;
        this.y = startY;

        bounds = new Rectangle(x, y, frameWidth, frameHeight);

        walkDown = new Animation<>(FRAME_DURATION, tmp[0]);
        walkLeft = new Animation<>(FRAME_DURATION, tmp[1]);
        walkRight = new Animation<>(FRAME_DURATION, tmp[2]);
        walkUp = new Animation<>(FRAME_DURATION, tmp[3]);

        x = startX;
        y = startY;
    }


    //Updates health system and player input and position
    public void update(float delta) {
        lastX = getX();
        lastY = getY();
        handleInput(delta);
        updateHealthSystem(delta);
    }

    //Sets player position
    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
        bounds.setPosition(x, y);
    }

    //Reverts to last player position
    public void revertPosition() {
        setPosition(lastX, lastY);
    }

    //Updates player health system
    private void updateHealthSystem(float delta) {
        if (isInvincible) {
            invincibilityTimer -= delta;
            if (invincibilityTimer <= 0) {
                isInvincible = false;
                invincibilityTimer = 0f;
            }
        }

        if (damageFlashTimer > 0) {
            damageFlashTimer -= delta;
        }

        if (health < maxHealth && health > 0) {
            healthRegenTimer += delta;
            if (healthRegenTimer >= HEALTH_REGEN_INTERVAL) {
                heal(HEALTH_REGEN_AMOUNT);
                healthRegenTimer = 0f;
            }
        }
    }

    //Allows player to move with keyboard input
    public void handleInput(float delta) {
        float dx = 0, dy = 0;

        boolean up = Gdx.input.isKeyPressed(Input.Keys.W);
        boolean down = Gdx.input.isKeyPressed(Input.Keys.S);
        boolean left = Gdx.input.isKeyPressed(Input.Keys.A);
        boolean right = Gdx.input.isKeyPressed(Input.Keys.D);

        if (up) {
            dy += speed * delta;
            currentDirection = Direction.UP;
        } else if (down) {
            dy -= speed * delta;
            currentDirection = Direction.DOWN;
        }

        if (left) {
            dx -= speed * delta;
            currentDirection = Direction.LEFT;
        } else if (right) {
            dx += speed * delta;
            currentDirection = Direction.RIGHT;
        }

        handleDebugInput(); //For debugging

        boolean isMoving = (dx != 0 || dy != 0);

        x += dx;
        y += dy;

        if (isMoving) {
            stateTime += delta;
        }
        bounds.setPosition(x, y);
    }

    //Remove before posting
    private void handleDebugInput() {
        if (!debugMode) return;

        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) takeDamage(10);
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) heal(10);
    }

    //Player taking damage
    public void takeDamage(int damage) {
        if (isInvincible || health <= 0) return;

        health -= damage;
        health = Math.max(0, health);
        isInvincible = true;
        invincibilityTimer = INVINCIBILITY_TIME;
        damageFlashTimer = DAMAGE_FLASH_DURATION;
        healthRegenTimer = 0f;

        Gdx.app.log("Player", "Took " + damage + " damage. Health: " + health + "/" + maxHealth);

        if (health <= 0) onDeath();
    }

    //Player healing
    public void heal(int healAmount) {
        if (health <= 0) return;

        health = Math.min(health + healAmount, maxHealth);
        Gdx.app.log("Player", "Healed " + healAmount + " HP. Health: " + health + "/" + maxHealth);
    }

    //Death screen
    private void onDeath() {
        Gdx.app.log("Player", "Player has died!");
        game.setScreen(new DeathScreen(game));
    }

    //Clamps player inside map
    public void clampToMapBounds(float worldWidth, float worldHeight) {
        x = MathUtils.clamp(x, 0, worldWidth - frameWidth);
        y = MathUtils.clamp(y, 0, worldHeight - frameHeight);
    }

    //Draws player and animation on screen
    public void draw(SpriteBatch batch) {
        TextureRegion frame;
        switch (currentDirection) {
            case LEFT:
                frame = walkLeft.getKeyFrame(stateTime, true);
                break;
            case RIGHT:
                frame = walkRight.getKeyFrame(stateTime, true);
                break;
            case UP:
                frame = walkUp.getKeyFrame(stateTime, true);
                break;
            case DOWN:
            default:
                frame = walkDown.getKeyFrame(stateTime, true);
                break;
        }

        if (damageFlashTimer > 0) {
            batch.setColor(1f, 0.5f, 0.5f, 1f);
        } else if (isInvincible) {
            float alpha = 0.5f + 0.5f * MathUtils.sin(invincibilityTimer * 20f);
            batch.setColor(1f, 1f, 1f, alpha);
        } else {
            batch.setColor(Color.WHITE);
        }

        batch.draw(frame, x, y);
        batch.setColor(Color.WHITE);
    }

    public float getHealthPercentage() {
        return (float) health / maxHealth;
    }

    public void drawHealthBar(ShapeRenderer shapeRenderer) {
        if (health <= 0) return;

        float barWidth = 50f;
        float barHeight = 6f;
        float barX = x - (barWidth - frameWidth) / 2f;
        float barY = y + frameHeight + 5f;

        shapeRenderer.setColor(Color.RED);
        shapeRenderer.rect(barX, barY, barWidth, barHeight);

        shapeRenderer.setColor(Color.GREEN);
        shapeRenderer.rect(barX, barY, barWidth * ((float) health / maxHealth), barHeight);

        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.rect(barX - 1, barY - 1, barWidth + 2, barHeight + 2);
    }

    public void dispose() {
        if (walkSheet != null) {
            walkSheet.dispose();
            walkSheet = null;
        }
    }

    // Getters
    public float getX() { return x; }
    public float getY() { return y; }
    public float getCenterX() { return x + frameWidth / 2f; }
    public float getCenterY() { return y + frameHeight / 2f; }
    public int getHealth() { return health; }
    public int getMaxHealth() { return maxHealth; }
    public boolean isAlive() { return health > 0; }
    public boolean isInvincible() { return isInvincible; }
    public Rectangle getBounds() { return bounds; }


    //Setters
    public void setMaxHealth(int newMaxHealth) {
        this.maxHealth = newMaxHealth;
        if (health > maxHealth) health = maxHealth;
    }
}

