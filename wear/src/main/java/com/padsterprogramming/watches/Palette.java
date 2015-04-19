package com.padsterprogramming.watches;

import android.graphics.Paint;

import static com.padsterprogramming.watches.Paints.fromGrey;
import static com.padsterprogramming.watches.Paints.fromGreyAndWidth;
import static com.padsterprogramming.watches.Paints.fromRgbAndWidth;

/** Singleton palette for faces, lazily created. */
public class Palette {
  // Lazy-created palettes for each mode.
  private ModePalette activePaints;
  private ModePalette passivePaints;

  public ModePalette getActivePalette() {
    if (activePaints == null) {
      int aG = 50; // Active greyscale for markers.
      activePaints = new ModePalette(
          Paints.BLACK, Paints.WHITE,
          fromGreyAndWidth(aG, 10), fromGreyAndWidth(aG,  4),
          fromGreyAndWidth( 0, 18), fromGreyAndWidth( 0, 10),
          fromRgbAndWidth(255, 0, 0, 4)); // red second hand.
    }
    return activePaints;
  }

  public ModePalette getPassivePalette() {
    if (passivePaints == null) {
      int pG1 = 25; // clockface grey.
      int pG2 = 45; // hands grey.
      passivePaints = new ModePalette(
          Paints.BLACK, fromGrey(pG1),
          fromGreyAndWidth(pG2, 10), null, // no minute marker, and no second hand.
          fromGreyAndWidth(pG2, 18), fromGreyAndWidth(pG2, 10), null);
    }
    return passivePaints;
  }

  // Palette struct for drawing a particular mode.
  public static class ModePalette {
    public final Paint background, foreground;
    public final Paint markerMajor, markerMinor;
    public final Paint handHr, handMin, handSec;
    public ModePalette(Paint... paints) {
      background = paints[0]; foreground = paints[1];
      markerMajor = paints[2]; markerMinor = paints[3];
      handHr = paints[4]; handMin = paints[5]; handSec = paints[6];
    }
  }
}
