package com.padsterprogramming.watches.faces;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.text.format.Time;
import com.padsterprogramming.watches.BasePaletteWatchface;
import com.padsterprogramming.watches.faces.BigBenPalettes.BigBenPalette;
import com.padsterprogramming.watches.services.ImageLoader;
import com.padsterprogramming.watches.services.WatchContext;

/** Watchface to emulate the clockface on the tower that holds Big Ben in London. */
public class BigBenFace extends BasePaletteWatchface<BigBenPalette> {
  private static final double MINS_LENGTH = 0.90;
  private static final double HOUR_LENGTH = 0.45;
  private static final double MINS_CENTRE = 170.0 / 210.0; // Centre at pixel 170 of 210.
  private static final double HOUR_CENTRE = 85.0 / 118.0; // Centre at pixel  85 of 118.

  public BigBenFace(WatchContext context) {
    super(context, new BigBenPalettes());
  }

  @Override public void createSingletons() {
    // Preload images on service startup, not on first draw.
    ensureActivePalette().preload(context.imageLoader());
    ensurePassivePalette().preload(context.imageLoader());
  }

  protected void drawMode(Time currentTime, Canvas canvas, Rect bounds, BigBenPalette palette) {
    ImageLoader imageLoader = context.imageLoader();

    Bitmap face = imageLoader.getBitmap(palette.faceResource, bounds.width(), bounds.height());
    canvas.drawBitmap(face, 0, 0, null);

    // Calculate how big the hour hand image should be and resize the image, using:
    // hourCenter * hourHeight = hourLength * bounds_height / 2
    int hourHeight = (int) (bounds.height() * HOUR_LENGTH / (2.0 * HOUR_CENTRE));
    Bitmap hourHand = imageLoader.scaleBitmapToHeight(palette.hourHandResource, hourHeight);

    // Do likewise with minutes:
    int minsHeight = (int) (bounds.height() * MINS_LENGTH / (2.0 * MINS_CENTRE));
    Bitmap minsHand = imageLoader.scaleBitmapToHeight(palette.minsHandResource, minsHeight);

    // Transformation matrix for the hour hand.
    int hourAmount = 60 * (60 * (currentTime.hour % 12) + currentTime.minute) + currentTime.second;
    Matrix hourMatrix = new Matrix();
    hourMatrix.postTranslate(-hourHand.getWidth() / 2.0f, -bounds.centerY() * (float) HOUR_LENGTH);
    hourMatrix.postRotate(hourAmount / 120f);
    hourMatrix.postTranslate(bounds.centerX(), bounds.centerY());

    // Transformation matrix for the minute hand.
    int minsAmount = 60 * currentTime.minute + currentTime.second;
    Matrix minsMatrix = new Matrix();
    minsMatrix.postTranslate(-minsHand.getWidth() / 2.0f, -bounds.centerY() * (float) MINS_LENGTH);
    minsMatrix.postRotate(minsAmount / 10f); // 3600 = 360 degrees
    minsMatrix.postTranslate(bounds.centerX(), bounds.centerY());

    // Finally, draw each:
    canvas.drawBitmap(hourHand, hourMatrix, null);
    canvas.drawBitmap(minsHand, minsMatrix, null);
  }
}
