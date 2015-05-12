package com.padsterprogramming.watches.faces;

import android.graphics.Paint;
import com.padsterprogramming.watches.BasePaletteWatchface.Palettes;
import com.padsterprogramming.watches.Paints;

/** Colours for the Fibonacci face in both modes. */
public class FibonacciPalettes implements Palettes<FibonacciPalettes.FibonacciPalette> {
  @Override public FibonacciPalette buildActivePalette() {
    return new FibonacciPalette(
        Paints.WHITE,
        Paints.fromRgb(255, 0, 0),
        Paints.fromRgb(0, 255, 0),
        Paints.fromRgb(0, 0, 255),
        strokeGrey(0, 5));
  }

  @Override public FibonacciPalette buildPassivePalette() {
    return new FibonacciPalette(
        Paints.fromGrey(0),
        Paints.fromRgb(60, 10, 10),
        Paints.fromRgb(10, 60, 10),
        Paints.fromRgb(10, 10, 60),
        strokeGrey(50, 3));
  }

  // Palette struct for drawing a particular mode.
  public static class FibonacciPalette {
    public final Paint nullColour;
    public final Paint hourColour;
    public final Paint minsColour;
    public final Paint bothColour;
    public final Paint stroke;

    public FibonacciPalette(Paint nullColour, Paint hourColour, Paint minsColour, Paint bothColour, Paint stroke) {
      this.nullColour = nullColour;
      this.hourColour = hourColour;
      this.minsColour = minsColour;
      this.bothColour = bothColour;
      this.stroke = stroke;
    }
  }

  private static Paint strokeGrey(int grey, float width) {
    Paint p = Paints.fromGrey(grey);
    p.setStyle(Paint.Style.STROKE);
    p.setStrokeWidth(width);
    return p;
  }
}
