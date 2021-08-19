package com.stucom.wormy;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

public class WormyGame extends Game {

    // Constants
    public static final int NUM_TILES_IN_SHORTEST_SIDE = 17;
    public static final int TILE_SIZE = 32;
    public static final int SLOW_DOWN = 5;
    public static final int ACCELERATION_THRESHOLD = 2;

    // Screen sizes and related
    private int nCols, nRows;
    private int paddingTop, paddingLeft;
    private float scale = 1.0f;

    // Current map of the game screen
    private char[] map;

    // Bitmap resources
    private final Bitmap wormLeft, wormRight;
    private final Bitmap[] tiles = new Bitmap[32];

    // Worm position
    private int wormX, wormY;
    // Current worm bitmap (looking in the right direction)
    private Bitmap worm;
    // Score and coins left
    private int score = 0, numCoins = 0;
    // Slowdown factor for easy vs hard playing
    private int slowdownLimit;

    public WormyGame(Context context, GameView gameView) {
        super(gameView);

        Resources resources = context.getResources();

        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inScaled = false;
        wormLeft = BitmapFactory.decodeResource(resources, R.raw.worm_left, opts);
        wormRight = BitmapFactory.decodeResource(resources, R.raw.worm_right, opts);
        worm = wormLeft;

        Bitmap tilesBitmap = BitmapFactory.decodeResource(context.getResources(), R.raw.tiles2, opts);
        for(int row = 0; row < 8; row++) {
            for(int col = 0; col < 4; col++) {
                tiles[row * 4 + col] = Bitmap.createBitmap(tilesBitmap, col * TILE_SIZE, row * TILE_SIZE, TILE_SIZE, TILE_SIZE);
            }
        }
    }

    @Override
    public void setViewSize(float viewWidth, float viewHeight) {
        super.setViewSize(viewWidth, viewHeight);
        if (viewWidth * viewHeight == 0) return;
        // Evaluate the scale and number of tiles on screen based on screen size
        scale = Math.min(viewWidth, viewHeight) / NUM_TILES_IN_SHORTEST_SIDE / TILE_SIZE;
        nCols = (int) (viewWidth / scale / TILE_SIZE);
        nRows = (int) (viewHeight / scale / TILE_SIZE);
        paddingLeft = (int) (viewWidth - (int)(nCols * scale * TILE_SIZE)) / 2;
        paddingTop = (int) (viewHeight - (int)(nRows * scale * TILE_SIZE)) / 2;
        // Reset the map and stop the game, because probably sizes have changed
        resetMap(false);
        setPlaying(false);
        pause();
    }

    @Override
    public void newGame() {
        // Reset the slow-down counter
        slowdownLimit = SLOW_DOWN;
        // Reset score & generate new map
        score = 0;
        resetMap(true);
        // Start the game
        setPlaying(true);
        resume();
    }

    public void resetMap(boolean withObjects) {
        if ((nCols <= 0) || (nRows <= 0)) return;
        // Wormy at center of screen
        wormX = nCols / 2;
        wormY = nRows / 2;
        // New empty map
        int size = nCols * nRows;
        map = new char[size];

        // Empty the board
        for (int i = 0; i < size; i++) map[i] = ' ';

        // Borders around
        for (int i = 0; i < nCols; i++) {
            map[i] = 'X';
            map[(nRows - 1) * nCols + i] = 'X';
        }
        for (int i = 0; i < nRows; i++) {
            map[i * nCols] = 'X';
            map[(i + 1) * nCols - 1] = 'X';
        }

        // Asked for a complete map (generate random objects)
        if (withObjects) {
            // Protect worm position by occupying his cell
            map[wormY * nCols + wormX] = 'W';
            // Random plants
            int placed = 0;
            while (placed < size / 20) {
                int i = (int) (Math.random() * nCols);
                int j = (int) (Math.random() * nRows);
                int idx = j * nCols + i;
                if (map[idx] == ' ') {
                    map[idx] = (char) ('P' + (int) (Math.random() * 4));
                    placed++;
                }
            }
            // Random coins
            placed = 0;
            numCoins = size / 50;
            while (placed < numCoins) {
                int i = (int) (Math.random() * nCols);
                int j = (int) (Math.random() * nRows);
                int idx = j * nCols + i;
                if (map[idx] == ' ') {
                    map[idx] = (char) ('C' + (int) (Math.random() * 5));
                    placed++;
                }
            }
            // Remove the worm lock
            map[wormY * nCols + wormX] = ' ';
        }
    }

    private static int slowDownCounter = 0;
    @Override
    public void physics() {
        // Only if playing
        if (!playing || paused) return;

        // Rotate the coins (changing their sprite in sequence C-D-E-F-G)
        for (int row = 0; row < nRows; row++) {
            for (int col = 0; col < nCols; col++) {
                int idx = row * nCols + col;
                char m = map[idx];
                if (m < 'C' || m > 'G') continue;
                m = (char) (((m - 'C' + 1) % 5) + 'C');
                map[idx] = m;
            }
        }

        // Use the counter to slowdown the worm (easy vs hard)
        if (++slowDownCounter == slowdownLimit) {
            slowDownCounter = 0;
            // Evaluate new worm position (and change worm sprite if needed)
            int newX = wormX, newY = wormY;
            if (accelerometerX < -ACCELERATION_THRESHOLD) { newX--; worm = wormLeft; }
            if (accelerometerX > +ACCELERATION_THRESHOLD) { newX++; worm = wormRight; }
            if (accelerometerY < -ACCELERATION_THRESHOLD) newY--;
            if (accelerometerY > +ACCELERATION_THRESHOLD) newY++;
            int idx = newY * nCols + newX;
            // Don't invade the border
            if (map[idx] != 'X') {
                wormX = newX;
                wormY = newY;
                // Check if there is a coin (sprite animation)
                if ((map[idx] >= 'C') && (map[idx] <= 'G')) {
                    // Coin collected!
                    score += 10;
                    map[idx] = ' ';
                    numCoins--;
                    if (numCoins == 0) {
                        // New screen, increase difficulty!
                        slowdownLimit--;
                        if (slowdownLimit < 1) slowdownLimit = 1;
                        resetMap(true);
                    }
                    // Alert external listener
                    if (listener != null) listener.scoreUpdated(score);
                }
                else if ((map[idx] >= 'P') && (map[idx] <= 'S')) {
                    // Plant touched!
                    playing = false;
                    // Alert external listener
                    if (listener != null) listener.gameLost();
                }
            }
        }
    }

    @Override
    public void onDraw(Canvas canvas) {
        // Apply padding and scale
        canvas.translate(paddingLeft, paddingTop);
        canvas.scale(scale, scale);
        // Protect against wrong initialization steps
        if (map == null) return;
        // Draw the tiles in their positions
        for (int row = 0; row < nRows; row++) {
            for (int col = 0; col < nCols; col++) {
                drawTile(canvas, row, col);
            }
        }
        // Put the worm on screen
        drawWorm(canvas);
    }

    // Method to draw a tile from the map
    public void drawTile(Canvas canvas, int row, int col) {
        int s = 3;
        int idx = row * nCols + col;
        char m = map[idx];
        switch (m) {
            case 'X': s = 28; break;    // Block of rock (borders)
            case 'C': s = 16; break;    // Coin, sprite #1
            case 'D': s = 17; break;    // Coin, sprite #2
            case 'E': s = 18; break;    // Coin, sprite #3
            case 'F': s = 19; break;    // Coin, sprite #4
            case 'G': s = 23; break;    // Coin, sprite #5
            case 'P': s = 2; break;     // Plant #1
            case 'Q': s = 21; break;    // Plant #2
            case 'R': s = 25; break;    // Plant #3
            case 'S': s = 29; break;    // Plant #4
        }
        canvas.drawBitmap(tiles[s], col * TILE_SIZE, row * TILE_SIZE, null);
    }

    // Method to draw the worm on screen
    public void drawWorm(Canvas canvas) {
        canvas.drawBitmap(worm, wormX * TILE_SIZE, wormY * TILE_SIZE, null);
    }

    // Interface for listener definition
    public interface WormyListener {
        void scoreUpdated(int score);
        void gameLost();
    }

    // Listener setter
    private WormyListener listener;
    public void setWormyListener(WormyListener listener) {
        this.listener = listener;
    }

}
