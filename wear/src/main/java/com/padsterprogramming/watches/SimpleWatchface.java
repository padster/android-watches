package com.padsterprogramming.watches;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.text.format.Time;

/**
 * Remaining:
 *
 * 1) Have more implementations (Big Ben, Harrison's H4, ...)
 * 2) Add simpler performant resource loading.
 * 3) Documentation for deployment/debugging.
 * 4) Settings activity API.
 */
public interface SimpleWatchface {
  /** Called once at the start, before any other method. */
  void createSingletons();

  /** How often active mode is repainted. */
  int activeDrawPeriodMs();

  /** Draw the watchface when the watch is active. */
  void drawActive(Time currentTime, Canvas canvas, Rect bounds);

  /** Draw the watchface when the watch is passive. */
  void drawPassive(Time currentTime, Canvas canvas, Rect bounds);
}
