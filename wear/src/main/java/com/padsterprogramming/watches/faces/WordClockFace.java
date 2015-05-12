package com.padsterprogramming.watches.faces;

import android.content.Context;
import com.padsterprogramming.watches.Paints;
import com.padsterprogramming.watches.WatchMetrics;
import com.padsterprogramming.watches.faces.WordClockPalette.ModePalette;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.format.Time;
import android.util.Log;

/**
 * Clockface with the time as illuminated letters, rather than numbers/hands.
 * See e.g. http://www.timeanddate.com/wordclock/
 *
 * IDEA: Different colours?
 * IDEA: Avoid burnin with letters changing in row #1?
 */
public class WordClockFace extends BaseSimpleWatchface {
  private static final String TAG = "WordClockFace";

  private WordClockPalette palette;

  public WordClockFace(Context context, WatchMetrics metrics) {
    super(context, metrics);
  }

  @Override public void createSingletons() {
    palette = new WordClockPalette();
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

    double yDelta = realBounds.height() * 1.0 / WordClockLetters.ROWS;
    double xDelta = realBounds.width() * 1.0 / WordClockLetters.COLS;

    int hour = currentTime.hour;
    int mins = currentTime.minute - currentTime.minute % 5; // Round down - 6:47 = 'quarter to 7'.

    // Draw the entire grid.
    for (int i = 0; i < WordClockLetters.ROWS; i++) {
      for (int j = 0; j < WordClockLetters.COLS; j++) {
        Paint paint =
            WordClockLetters.isActive(hour, mins, i, j) ? paints.onColour : paints.offColour;
        if (paint == null) {
          continue;
        }

        int yAt = (int) (realBounds.top  + i * yDelta + paint.getTextSize());
        int xAt = (int) (realBounds.left + j * xDelta);
        canvas.drawText(WordClockLetters.charAt(i, j), xAt, yAt, paint);
      }
    }
  }
}
