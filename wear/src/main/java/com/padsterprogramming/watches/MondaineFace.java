package com.padsterprogramming.watches;

import com.padsterprogramming.watches.Palette.ModePalette;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.format.Time;

/** Demo watchface which looks like the SBB (Swiss rail) Mondaine design. */
public class MondaineFace implements SimpleWatchface {
  private static final float TWOPI = 2 * (float) Math.PI;

  private Palette palette;

  @Override public void createSingletons() {
    palette = new Palette();
  }

  @Override
  public void drawActive(Canvas canvas, Rect bounds) {
    drawMode(canvas, bounds, palette.getActivePalette());
  }

  @Override
  public void drawPassive(Canvas canvas, Rect bounds) {
    drawMode(canvas, bounds, palette.getPassivePalette());
  }

  public void drawMode(Canvas canvas, Rect bounds, ModePalette paints) {
    canvas.drawRect(bounds, paints.background);

    int width = bounds.width();
    int height = bounds.height();
    float cX = width / 2f;
    float cY = height / 2f;
    float rad = Math.min(cX, cY);
    canvas.drawCircle(cX, cY, rad, paints.foreground);

    for (int i = 0; i < 60; i++) {
      float radians = TWOPI * i / 60f;
      boolean isMajor = (i % 5 == 0);
      float lenFrom = (isMajor ? 0.74f : 0.83f) * rad; // major markers be longer.
      Paint paint = isMajor ? paints.markerMajor : paints.markerMinor;
      radialLine(canvas, cX, cY, lenFrom, 0.93f * rad, radians, paint, false);
    }

    Time time = new Time(); time.setToNow();

    // Hour hand is normal, minute and second hands lock to last minute/second.
    float hrAng  = TWOPI * ((time.hour % 12) * 60f + time.minute) / (12f * 60f);
    float minAng = TWOPI * time.minute / 60f;
    float secAng = TWOPI * time.second / 60f;
    radialLine(canvas, cX, cY, -0.15f * rad, 0.60f * rad, hrAng , paints.handHr,  false);
    radialLine(canvas, cX, cY, -0.15f * rad, 0.80f * rad, minAng, paints.handMin, false);
    radialLine(canvas, cX, cY, -0.18f * rad, 0.60f * rad, secAng, paints.handSec,  true);

    // Bonus: if there's a second hand, draw the central joining dot.
    if (paints.handSec != null) {
      canvas.drawCircle(cX, cY, 0.03f * rad, paints.handSec);
    }
  }

  /** From a center (cx, cy), draw a line at angle (ang), from lenFr -> lenTo distance. */
  private void radialLine(Canvas canvas, float cx, float cy,
      float lenFr, float lenTo, float ang, Paint paint, boolean withDot) {
    if (paint == null) { return; } // Special case, null = ignore the line.

    float dX =  (float) Math.sin(ang);
    float dY = -(float) Math.cos(ang);
    float frX = cx + lenFr * dX;
    float frY = cy + lenFr * dY;
    float toX = cx + lenTo * dX;
    float toY = cy + lenTo * dY;
    canvas.drawLine(frX, frY, toX, toY, paint);

    // For the second hand: this optionally draws a circle at the end of the line, width 12.
    if (withDot) {
      canvas.drawCircle(toX, toY, 12f, paint);
    }
  }
}
