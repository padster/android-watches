package com.padsterprogramming.watches.faces;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.format.Time;
import com.padsterprogramming.watches.Paints;
import com.padsterprogramming.watches.WatchMetrics;
import com.padsterprogramming.watches.faces.FibonacciPalette.ModePalette;

/**
 * Watchface for the fibonacci clock:
 * https://www.kickstarter.com/projects/basbrun/fibonacci-clock-an-open-source-clock-for-nerds-wit
 *
 * TODO - this was written quickly :) Should be cleaned up afterwards...
 */
public class FibonacciFace extends BaseSimpleWatchface {

    private FibonacciPalette palette;

    public FibonacciFace(WatchMetrics metrics) {
        super(metrics);
    }

    @Override public void createSingletons() {
        palette = new FibonacciPalette();
    }

    @Override public int activeDrawPeriodMs() {
        return 5 * 60 * 1000; // Only updates every five minutes.
    }

    @Override public void drawActive(Time currentTime, Canvas canvas, Rect bounds) {
        drawMode(currentTime, canvas, bounds, palette.getActivePalette());
    }

    @Override public void drawPassive(Time currentTime, Canvas canvas, Rect bounds) {
        drawMode(currentTime, canvas, bounds, palette.getPassivePalette());
    }

    public void drawMode(Time currentTime, Canvas canvas, Rect bounds, ModePalette paints) {
        canvas.drawRect(bounds, Paints.BLACK);
        Rect realBounds = bounds;

        // TODO - verify this works on a round watch...
        // TODO - split out into common library to find biggest rectangle (copied from WordClockFace).
        if (metrics.isRound()) {
            // Cull to square inside the circle.
            double oneOnRootTwo = 1.0 / Math.sqrt(2.0);
            double size = Math.min(bounds.height(), bounds.width());
            double newSize = oneOnRootTwo * size;

            realBounds = new Rect();
            realBounds.top = (int) (Math.ceil(bounds.exactCenterY() - newSize));
            realBounds.left = (int) (Math.ceil(bounds.exactCenterX() - newSize));
            realBounds.right = (int) (Math.floor(bounds.exactCenterX() + newSize));
            realBounds.bottom = (int) (Math.floor(bounds.exactCenterY() + newSize));
        }

        // Assume width is limiting factory, and have up 5% padding on left/right.
        float unitSz = 0.9f * realBounds.width() / 8.0f;
        float topLeftX = realBounds.exactCenterX() - unitSz * 4f;
        float topLeftY = realBounds.exactCenterY() - unitSz * 2.5f;

        int hour = currentTime.hour % 12;
        int mins = currentTime.minute / 5;
        // Have midnight as 0, but noon as 12, it feels cleaner that way for some reason...
        if (currentTime.hour == 12) {
            hour = 12;
        }

        // Draw the entire grid.
        Paint strokeColour = paints.stroke;
        for (int i = 0; i < CELL_COUNT; i++) {
            boolean hOn = (USED[hour].charAt(i) == 'X');
            boolean mOn = (USED[mins].charAt(i) == 'X');
            Paint squareColour = hOn ? ( mOn ? paints.bothColour : paints.hourColour) :
                               ( mOn ? paints.minsColour : paints.nullColour);

            float squareSz = FIBONS[i] * unitSz;
            float tlX = topLeftX + CELL_X[i] * unitSz;
            float tlY = topLeftY + CELL_Y[i] * unitSz;
            canvas.drawRect(tlX, tlY, tlX + squareSz, tlY + squareSz, squareColour);
            canvas.drawRect(tlX, tlY, tlX + squareSz, tlY + squareSz, strokeColour);
        }
    }


    // Rectangle is 8 x 5:
    //  _ _ _ _ _ _ _ _
    // | 2 |1|         |
    // |_ _|0|         |
    // |     |    4    |
    // |  3  |         |
    // |_ _ _|_ _ _ _ _|
    private static final int[] FIBONS = {1, 1, 2, 3, 5};
    private static final int[] CELL_X = {2, 2, 0, 0, 3};
    private static final int[] CELL_Y = {1, 0, 0, 2, 0};

    private static final String[] USED = {
        ".....", //  0
        "X....", //  1, although ".X..." also?
        "..X..", //  2, although "XX..." also?
        "...X.", //  3, although ...etc. many options for some of these.
        ".X.X.", //  4
        "....X", //  5
        ".XXX.", //  6, trying to minimize overlap here...
        "..X.X", //  7
        "...XX", //  8
        "X..XX", //  9
        "..XXX", // 10
        ".XXXX", // 11
        "XXXXX", // 12
    };
    private static final int CELL_COUNT = USED[0].length();
}
