package com.padsterprogramming.watches.faces;

import android.text.format.Time;

import com.padsterprogramming.watches.SimpleWatchface;

/**
 * Abstract simple watchface, contains some useful methods and
 * makes it easier to add things to the interface in the future.
 */
public abstract class BaseSimpleWatchface implements SimpleWatchface {
  // TODO - move elsewhere when there's a better spot.
  protected static final float TWOPI = 2 * (float) Math.PI;
}
