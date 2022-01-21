package com.mygdx.game;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.TimeUtils;

public class Coin extends Rectangle {
    long spawnTime;
    float startX;
    float startY;
    float controlX;
    float controlY;
    float endX;
    float endY;
    boolean spawned;

    public Coin(float xPos, float yPos, int width, int height) {
        this.x = xPos;
        this.startX = xPos;
        this.endX = MathUtils.random(Math.max(0, startX - 200f), Math.min(800 - 64,startX + 200f));
        this.controlX = (startX + endX) / 2f;

        this.y = yPos;
        this.startY = yPos;
        this.endY = MathUtils.random(Math.max(0, startY - 200f), Math.min(480 - 64,startY - 100f));
        this.controlY = Math.min(480 - 64, ((startY + endY) / 2f) + 200);

        this.width = width;
        this.height = height;
        this.spawnTime = TimeUtils.millis();

        this.spawned = false;
    }

    public float getLife() {
        return (TimeUtils.millis() - spawnTime) / 1000f;
    }

    public void update() {
        if (!spawned) {
            updateX();
            updateY();
            if (getLife() >= 1) {
                spawned = true;
            }
        }
    }

    public void updateX() {
        float life = this.getLife();
        this.x = (float) (Math.pow(1f-life,2f) * startX + 2f * (1f-life) * life * controlX + Math.pow(life,2f) * endX);
    }

    public void updateY() {
        float life = this.getLife();
        this.y = (float) (Math.pow(1-life,2) * startY + 2 * (1-life) * life * controlY + Math.pow(life,2) * endY);
    }

}
