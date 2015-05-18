package com.padsterprogramming.watches;

import android.graphics.Paint;

/** Collection of utilities for Paint instances. */
public final class Paints {
  public static final Paint WHITE = Paints.fromGrey(255);
  public static final Paint BLACK = Paints.fromGrey(0);

  private Paints() { /* Only static access... */ }

  /** Build a paint from r/g/b colours. */
  public static Paint fromRgb(int r, int g, int b) {
    Paint paint = new Paint();
    paint.setARGB(255, r, g, b);
    paint.setAntiAlias(true);
    return paint;
  }

  /** Build a paint of a particular shade of grey. */
  public static Paint fromGrey(int grey) {
    return fromRgb(grey, grey, grey);
  }

  /** Build a paint from r/g/b and line width. */
  public static Paint fromRgbAndWidth(int r, int g, int b, float w) {
    Paint paint = fromRgb(r, g, b);
    paint.setStrokeWidth(w);
    return paint;
  }

  /** Build a greyscale paint with a given line width. */
  public static Paint fromGreyAndWidth(int grey, float w) {
    return fromRgbAndWidth(grey, grey, grey, w);
  }
}
