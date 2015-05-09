package com.padsterprogramming.watches.faces;

import android.graphics.Paint;
import android.graphics.Typeface;

import com.padsterprogramming.watches.Paints;

/** Colours for the WordClock in both modes. */
public class WordClockPalette {
  // Lazy-created palettes for each mode.
  private ModePalette activePaints;
  private ModePalette passivePaints;

  public ModePalette getActivePalette() {
    if (activePaints == null) {
      activePaints = new ModePalette(
          fixedWidthText(Paints.fromGrey(180),  true),
          fixedWidthText(Paints.fromGrey( 55), false));
    }
    return activePaints;
  }

  public ModePalette getPassivePalette() {
    if (passivePaints == null) {
      passivePaints = new ModePalette(
          fixedWidthText(Paints.fromGrey(70), true),
          null /* Inactive letters disabled. */);
    }
    return passivePaints;
  }

  // Palette struct for drawing a particular mode.
  public static class ModePalette {
    public final Paint onColour;
    public final Paint offColour;

    public ModePalette(Paint onColour, Paint offColour) {
      this.onColour = onColour;
      this.offColour = offColour;
    }
  }

  private static Paint fixedWidthText(Paint base, boolean bold) {
    base.setTypeface(Typeface.create("monospace", bold ? Typeface.BOLD : Typeface.NORMAL));
    // TODO - resize based on watch size.
    base.setTextSize(24f);
    return base;
  }
}
