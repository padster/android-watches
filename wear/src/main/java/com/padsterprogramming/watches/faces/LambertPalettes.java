package com.padsterprogramming.watches.faces;

import android.graphics.Paint;
import com.padsterprogramming.watches.BasePaletteWatchface.Palettes;
import com.padsterprogramming.watches.services.ImageLoader;
import com.padsterprogramming.watches.Paints;
import com.padsterprogramming.watches.R;

/** Customization for the two Lambert faces. */
public class LambertPalettes implements Palettes<LambertPalettes.LambertPalette> {
  @Override public LambertPalette buildActivePalette() {
    return new LambertPalette(R.drawable.lambert, Paints.fromRgb(255, 0, 0));
  }

  @Override public LambertPalette buildPassivePalette() {
    return new LambertPalette(R.drawable.lambert, Paints.fromRgb(255, 0, 0));
  }

  public static class LambertPalette {
    public final int globeResource;
    public final Paint timeArmPaint;

    public LambertPalette(int globeResource, Paint timeArmPaint) {
      this.globeResource = globeResource;
      this.timeArmPaint = timeArmPaint;
    }

    void preload(ImageLoader imageLoader) {
      imageLoader.preload(globeResource);
    }
  }
}
