package com.padsterprogramming.watches.faces;

import android.graphics.Paint;
import android.graphics.Typeface;
import com.padsterprogramming.watches.BasePaletteWatchface;
import com.padsterprogramming.watches.Paints;
import com.padsterprogramming.watches.R;

/**
 * Palettes for the active and passive halograph faces.
 */
public class HalographPalettes implements BasePaletteWatchface.Palettes<HalographPalettes.HalographPalette> {
  @Override public HalographPalette buildActivePalette() {
    return new HalographPalette(
        Paints.fromGrey(50), Paints.fromGrey(80),
        Paints.fromGreyAndWidth(127, 3.5f), Paints.fromGreyAndWidth(230, 3.0f),
        0, 0, 255,
        Paints.fromRgbAndWidth(255, 0, 0, 3.0f), fontPaint(10, 0, 0, 0, true), fontPaint(7, 0, 0, 0, false),
        R.drawable.halograph_inside);
  }

  @Override public HalographPalette buildPassivePalette() {
    int g = 80; // default grey for text and hands.
    return new HalographPalette(
        Paints.BLACK, null,
        Paints.fromGreyAndWidth(55, 3.5f), Paints.fromGreyAndWidth(30, 3.0f),
        g, g, g,
        Paints.fromRgbAndWidth(g, g, g, 3.0f), fontPaint(10, g, g, g, true), null,
        -1);
  }

  public static class HalographPalette {
    public final Paint backgroundPaint;
    public final Paint behindNumbersPaint;
    public final Paint armPaint;
    public final Paint pathPaint;
    public final Paint hourHandPaint;
    public final Paint hourMajorTextPaint;
    public final Paint hourMinorTextPaint;
    public final Paint minuteHandPaint;
    public final Paint minuteTextMajorPaint;
    public final Paint minuteTextMinorPaint;
    public final int skeletonResource;

    public HalographPalette(
        Paint backgroundPaint, Paint behindNumbersPaint,
        Paint armPaint, Paint pathPaint,
        int hourR, int hourG, int hourB,
        Paint minuteHandPaint, Paint minuteTextMajorPaint, Paint minuteTextMinorPaint,
        int skeletonResource) {
      this.backgroundPaint = backgroundPaint;
      this.behindNumbersPaint = behindNumbersPaint;
      this.armPaint = armPaint;
      this.pathPaint = pathPaint;
      this.hourHandPaint = Paints.fromRgbAndWidth(hourR, hourG, hourB, 3.0f);
      this.hourMajorTextPaint = fontPaint(16, hourR, hourG, hourB, true);
      this.hourMinorTextPaint = null;
      this.minuteHandPaint = minuteHandPaint;
      this.minuteTextMajorPaint = minuteTextMajorPaint;
      this.minuteTextMinorPaint = minuteTextMinorPaint;
      this.skeletonResource = skeletonResource;
      this.hourHandPaint.setStyle(Paint.Style.STROKE);
      this.minuteHandPaint.setStyle(Paint.Style.STROKE);
    }
  }

  public static Paint fontPaint(int size, int r, int g, int b, boolean bold) {
    Typeface typeface = bold ? Typeface.create(Typeface.MONOSPACE, Typeface.BOLD) : Typeface.MONOSPACE;
    Paint paint = Paints.fromRgbAndWidth(r, g, b, 0.5f);
    paint.setTextSize(size);
    paint.setTypeface(typeface);
    paint.setTextScaleX(bold ? 0.9f : 0.7f);
    paint.setAntiAlias(true);
    return paint;
  }
}
