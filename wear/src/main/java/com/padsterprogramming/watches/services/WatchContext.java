package com.padsterprogramming.watches.services;

import android.content.Context;

/** Collection of services that are available to watchfaces. */
public class WatchContext {
  private final Context androidContext;
  private final ImageLoader imageLoader;
  private final WatchMetrics watchMetrics;

  public WatchContext(Context androidContext) {
    this.androidContext = androidContext;
    // TODO - pull out and register externally instead.
    this.imageLoader = new ImageLoader(this.androidContext);
    this.watchMetrics = new WatchMetrics();
  }

  public Context androidContext() {
    return this.androidContext;
  }

  public ImageLoader imageLoader() {
    return this.imageLoader;
  }

  public WatchMetrics watchMetrics() {
    return this.watchMetrics;
  }
}
