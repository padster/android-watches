package com.padsterprogramming.watches.faces;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.location.Location;
import android.text.format.Time;
import com.padsterprogramming.watches.BasePaletteWatchface;
import com.padsterprogramming.watches.Paints;
import com.padsterprogramming.watches.faces.LambertPalettes.LambertPalette;
import com.padsterprogramming.watches.services.WatchContext;

/**
 * Face that uses a north-pole lambert projection to draw the globe as a circle,
 * interpreted as a 24hr clock with lines of longitude aligned to their approximate time.
 * http://kartoweb.itc.nl/geometrics/map%20projections/mappro.html
 *
 * NOTE: DO NOT USE - LocationService isn't working yet.
 */
public class LambertFace extends BasePaletteWatchface<LambertPalette> {
  public LambertFace(WatchContext context) {
    super(context, new LambertPalettes());
  }

  @Override public int activeDrawPeriodMs() {
    return 1000; // HACK 60 * 1000; // Every minute is fine.
  }

  @Override public void createSingletons() {
    context.location().start();

    // Preload images on service startup, not on first draw.
    ensureActivePalette().preload(context.imageLoader());
    ensurePassivePalette().preload(context.imageLoader());
  }

  @Override protected void drawMode(
      Time currentTime, Canvas canvas, Rect bounds, LambertPalette lambertPalette) {
    canvas.drawRect(bounds, Paints.BLACK);

    // TODO:
    // 1) Get the location working:
    //  https://developer.android.com/training/articles/wear-location-detection.html
    // 4) Calculations for where the dot goes. Math: http://en.wikipedia.org/wiki/Azimuthal_equidistant_projection
    // 5) Calculation for rotation
    // 6) Time in numbers somewhere / hour markers?
    // 7) Passive dark version.

    float nowHandRatio = (currentTime.hour * 60 + currentTime.minute) / (24 * 60.0f);

    Location location = context.location().getLocation();

    // HACK
    float degrees = currentTime.second * 6.0f;
    Matrix rotateMatrix = new Matrix();
    rotateMatrix.postRotate(degrees, bounds.exactCenterX(), bounds.exactCenterY());
    Bitmap bitmap = context.imageLoader().getBitmap(
        lambertPalette.globeResource, bounds.width(), bounds.height());
    canvas.drawBitmap(bitmap, rotateMatrix, null);

    float nowHandRad = TWOPI * nowHandRatio;
    float dX = (float) Math.sin(nowHandRad);
    float dY = -(float) Math.cos(nowHandRad);
    float r = 0.45f * Math.min(bounds.width(), bounds.height());
    canvas.drawLine(bounds.exactCenterX(), bounds.exactCenterY(),
        bounds.exactCenterX() + dX * r, bounds.exactCenterY() + dY * r,
        lambertPalette.timeArmPaint);
    canvas.drawCircle(bounds.exactCenterX(), bounds.exactCenterY(), r * 0.04f, lambertPalette.timeArmPaint);

    String msg = "null";
    if (location != null) {
      msg = location.getLongitude() + " / " + location.getLatitude() + "!";
    }
    canvas.drawText(msg, bounds.exactCenterX(), bounds.exactCenterY(), Paints.BLACK);
  }
}
