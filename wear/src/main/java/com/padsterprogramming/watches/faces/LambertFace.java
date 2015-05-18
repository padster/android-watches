package com.padsterprogramming.watches.faces;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.text.format.Time;
import com.padsterprogramming.watches.BasePaletteWatchface;
import com.padsterprogramming.watches.faces.LambertPalettes.LambertPalette;
import com.padsterprogramming.watches.services.WatchContext;

public class LambertFace extends BasePaletteWatchface<LambertPalette> {

  /**
   * Build the face given the normal details, plus details how to build the palettes.
   *
   * @param context
   * @param palettes
   */
  protected LambertFace(
      WatchContext context,
      Palettes<LambertPalette> palettes) {
    super(context, palettes);
  }

  @Override protected void drawMode(
      Time currentTime, Canvas canvas, Rect bounds, LambertPalette lambertPalette) {

  }
}
