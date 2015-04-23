package com.padsterprogramming.watches.faces;

import com.padsterprogramming.watches.WatchMetrics;
import com.padsterprogramming.watches.faces.MondainePalette.ModePalette;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.format.Time;

/** Demo watchface which looks like the SBB (Swiss rail) Mondaine design. */
public class MondaineFace extends BaseSimpleWatchface {
  private MondainePalette palette;

  public MondaineFace(WatchMetrics metrics) {
    super(metrics);
  }

  @Override public void createSingletons() {
    palette = new MondainePalette();
  }

  @Override public void drawActive(Time currentTime, Canvas canvas, Rect bounds) {
    drawMode(currentTime, canvas, bounds, palette.getActivePalette());
  }

  @Override public void drawPassive(Time currentTime, Canvas canvas, Rect bounds) {
    drawMode(currentTime, canvas, bounds, palette.getPassivePalette());
  }

  public void drawMode(Time currentTime, Canvas canvas, Rect bounds, ModePalette paints) {
    canvas.drawRect(bounds, paints.background);

    float cX = bounds.exactCenterX();
    float cY = bounds.exactCenterY();
    float rad = Math.min(cX, cY);
    canvas.drawCircle(cX, cY, rad, paints.foreground);

    for (int i = 0; i < 60; i++) {
      float radians = TWOPI * i / 60f;
      boolean isMajor = (i % 5 == 0);
      float lenFrom = (isMajor ? 0.74f : 0.83f) * rad; // major markers be longer.
      Paint paint = isMajor ? paints.markerMajor : paints.markerMinor;
      radialLine(canvas, cX, cY, lenFrom, 0.93f * rad, radians, paint, false);
    }

    // Hour hand is normal, minute and second hands lock to last minute/second.
    float hrAng  = TWOPI * ((currentTime.hour % 12) * 60f + currentTime.minute) / (12f * 60f);
    float minAng = TWOPI * currentTime.minute / 60f;
    float secAng = TWOPI * currentTime.second / 60f;
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
