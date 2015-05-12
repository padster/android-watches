package com.padsterprogramming.watches.faces;

import android.content.Context;
import com.padsterprogramming.watches.BasePaletteWatchface;
import com.padsterprogramming.watches.BaseSimpleWatchface;
import com.padsterprogramming.watches.WatchMetrics;
import com.padsterprogramming.watches.faces.MondainePalettes.MondainePalette;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.format.Time;

/** Demo watchface which looks like the SBB (Swiss rail) Mondaine design. */
public class MondaineFace extends BasePaletteWatchface<MondainePalette> {
  // SBB clocks pause a bit at 60s, so each visual 'second' is actually a bit shorter.
  // Keeep a multiple of 4, to allow 4 ticks per second.
  private static final int MS_PER_SEC = 980;
  private static final int TICKS_PER_SEC = 4;
  private static final int MS_PER_TICK = MS_PER_SEC / TICKS_PER_SEC;

  // HACK: See comments at usage.
  // TODO: Move into the service?
  private int lastSecond = -1;
  private int lastMillis = -1;

  public MondaineFace(Context context, WatchMetrics metrics) {
    super(context, metrics, new MondainePalettes());
  }

  @Override public int activeDrawPeriodMs() {
    return MS_PER_TICK; // Redraw each tick (roughly).
  }

  public void drawMode(Time currentTime, Canvas canvas, Rect bounds, MondainePalette paints) {
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

    int minute = currentTime.minute;
    int second = currentTime.second;
    float smearedSecond = second;

    // Active mode, so smear the seconds to emulate the SBB tick.
    if (paints.handSec != null) {
      // HACK: Time doesn't include millis :(
      // We measure millis here, but it's possible the millis have wrapped around 0
      // while currentTime.second is still on the previous second,
      // which causes the time to jump backwards. Fix by incrementing the second manually.
      int millis = (int) (System.currentTimeMillis() % 1000);
      if (second == lastSecond && millis < lastMillis) {
        // Backwards jump! Force it forwards again.
        second++;
      }
      lastMillis = millis;
      lastSecond = second;

      // Smearing: Find which # tick it is in the minute.
      int tickNumber = (1000 * second + millis) / MS_PER_TICK;
      smearedSecond = tickNumber * 1f / TICKS_PER_SEC;

      if (tickNumber == 0) {
        minute--; // Only progress the minute hand on tick #1, not tick #0.
      } else if (tickNumber >= TICKS_PER_SEC * 60) {
        smearedSecond = 0f; // For the extra ticks, snap the second hand to the top.
      }
    }

    // Hour hand is normal, minute and second hands lock to last minute/second.
    float hrAng = TWOPI * ((currentTime.hour % 12) * 60f + minute) / (12f * 60f);
    float minAng = TWOPI * minute / 60f;
    float secAng = TWOPI * smearedSecond / 60f;
    radialLine(canvas, cX, cY, -0.15f * rad, 0.60f * rad, hrAng, paints.handHr, false);
    radialLine(canvas, cX, cY, -0.15f * rad, 0.80f * rad, minAng, paints.handMin, false);
    radialLine(canvas, cX, cY, -0.18f * rad, 0.60f * rad, secAng, paints.handSec, true);

    // Bonus: if there's a second hand, draw the central joining dot.
    if (paints.handSec != null) {
      canvas.drawCircle(cX, cY, 0.03f * rad, paints.handSec);
    }
  }

  /** From a center (cx, cy), draw a line at angle (ang), from lenFr -> lenTo distance. */
  private void radialLine(
      Canvas canvas, float cx, float cy,
      float lenFr, float lenTo, float ang, Paint paint, boolean withDot) {
    if (paint == null) { return; } // Special case, null = ignore the line.

    float dX = (float) Math.sin(ang);
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
