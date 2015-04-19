package com.padsterprogramming.watches;

import android.graphics.Paint;

/** Collection of utilities for Paint instances. */
public final class Paints {
  public static final Paint WHITE = Paints.fromGrey(255);
  public static final Paint BLACK = Paints.fromGrey(  0);

  private Paints() {}

  public static Paint fromRgb(int r, int g, int b) {
    Paint paint = new Paint();
    paint.setARGB(255, r, g, b);
    paint.setAntiAlias(true);
    return paint;
  }

  public static Paint fromGrey(int grey) {
    return fromRgb(grey, grey, grey);
  }

  public static Paint fromRgbAndWidth(int r, int g, int b, float w) {
    Paint paint = fromRgb(r, g, b);
    paint.setStrokeWidth(w);
    return paint;
  }

  public static Paint fromGreyAndWidth(int grey, float w) {
    return fromRgbAndWidth(grey, grey, grey, w);
  }
}
