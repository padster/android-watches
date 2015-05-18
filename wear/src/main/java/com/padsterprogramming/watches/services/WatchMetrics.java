package com.padsterprogramming.watches.services;

import android.graphics.Rect;
import android.view.WindowInsets;

/** Details about the face shape/size. */
public class WatchMetrics {
  // Oh Android APIs, why do you only update these through weird callbacks.
  private Rect bounds;
  private boolean isRound;

  // TODO: Use it or lose it.
  public Rect getBounds() {
    return bounds;
  }

  public boolean isRound() {
    return isRound;
  }

  /* Update handlers */
  public void handleBounds(Rect bounds) {
    this.bounds = bounds;
  }

  public void handleWindowInsets(WindowInsets insets) {
    isRound = insets.isRound();
    // Ignore chin for now...
  }
}
