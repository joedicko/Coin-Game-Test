package com.mygdx.game;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;

public class GameScreen implements Screen {
    final SpiceTraders game;

    Texture playerImage;
    Texture coinImage;
    Sound coinPickup;
    OrthographicCamera camera;
    Rectangle player;
    Array<Coin> coins;
    long lastSpawnTime;
    int coinsGathered;

    public GameScreen(final SpiceTraders game) {
        this.game = game;

        // load the images for the droplet and the bucket, 64x64 pixels each
        playerImage = new Texture(Gdx.files.internal("greedy-man.png"));
        coinImage = new Texture(Gdx.files.internal("coin.png"));

        // load the drop sound effect and the rain background "music"
        coinPickup = Gdx.audio.newSound(Gdx.files.internal("coin-pickup.mp3"));

        // create the camera and the SpriteBatch
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);

        // create a Rectangle to logically represent the bucket
        player = new Rectangle();
        player.x = 400 - 32; // center the bucket horizontally
        player.y = 20; // bottom left corner of the bucket is 20 pixels above
        // the bottom screen edge
        player.width = 64;
        player.height = 64;

        // create the raindrops array and spawn the first raindrop
        coins = new Array<>();
        spawnCoin();
    }
    private void spawnCoin() {
        Coin coin= new Coin(MathUtils.random(0, 800 - 64), MathUtils.random(0, 480 - 64), 64, 64);
        coins.add(coin);
        lastSpawnTime = TimeUtils.millis();
    }
    @Override
    public void render(float delta) {
        ScreenUtils.clear(1, 1, 1, 1);

        camera.update();

        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        game.font.draw(game.batch, "Coins Collected: " + coinsGathered, 0, 480);
        game.batch.draw(playerImage, player.x, player.y, player.width, player.height);
        for (Coin coin : coins) {
            game.batch.draw(coinImage, coin.x, coin.y, coin.width, coin.height);
        }
        game.batch.end();

        if (Gdx.input.isKeyPressed(Keys.A))
            player.x -= 200 * Gdx.graphics.getDeltaTime();
        if (Gdx.input.isKeyPressed(Keys.D))
            player.x += 200 * Gdx.graphics.getDeltaTime();
        if (Gdx.input.isKeyPressed(Keys.W))
            player.y += 200 * Gdx.graphics.getDeltaTime();
        if (Gdx.input.isKeyPressed(Keys.S))
            player.y -= 200 * Gdx.graphics.getDeltaTime();

        if (player.x < 0)
            player.x = 0;
        if (player.x > 800 - 64)
            player.x = 800 - 64;
        if (player.y < 0)
            player.y = 0;
        if (player.y > 480 - 64)
            player.y = 480 - 64;

        if (TimeUtils.millis() - lastSpawnTime > 1000)
            spawnCoin();

        Iterator<Coin> iter = coins.iterator();
        while (iter.hasNext()) {
            Coin coin = iter.next();
            if (coin.overlaps(player)) {
                coinsGathered++;
                coinPickup.play();
                iter.remove();
            }
            coin.update();
        }
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void show() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
        playerImage.dispose();
        coinImage.dispose();
        coinPickup.dispose();
    }

}

