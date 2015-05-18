package com.padsterprogramming.watches;

import android.content.Context;
import com.padsterprogramming.watches.services.WatchContext;
import com.padsterprogramming.watches.services.WatchMetrics;

/**
 * Abstract simple watchface, contains some useful methods and
 * makes it easier to add things to the interface in the future.
 */
public abstract class BaseSimpleWatchface implements SimpleWatchface {
  // TODO - move elsewhere when there's a better spot.
  protected static final float TWOPI = 2 * (float) Math.PI;

  protected final WatchContext context;

  protected BaseSimpleWatchface(WatchContext context) {
    this.context = context;
  }

  @Override public void createSingletons() {
    // no-op by default.
  }

  @Override public int activeDrawPeriodMs() {
    return 1000; // Draw every one second by default.
  }
}
