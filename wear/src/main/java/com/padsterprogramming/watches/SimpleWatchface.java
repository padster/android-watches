package com.padsterprogramming.watches;

import android.graphics.Canvas;
import android.graphics.Rect;

/**
 * Remaining:
 *
 * 1) Have more implementations (Mondaine, Big ben, Harrison's H4, ...)
 * 2) Add simpler performant resource loading.
 * 3) Documentation for deployment/debugging.
 * 4) Settings activity API.
 */
public interface SimpleWatchface {
  /** Called once at the start, before any other method. */
  void createSingletons();

  /** Draw the watchface when the watch is active. */
  void drawActive(Canvas canvas, Rect bounds);

  /** Draw the watchface when the watch is passive. */
  void drawPassive(Canvas canvas, Rect bounds);
}
