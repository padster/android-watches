package com.padsterprogramming.watches.faces;

import com.padsterprogramming.watches.BasePaletteWatchface.Palettes;
import com.padsterprogramming.watches.ImageLoader;
import com.padsterprogramming.watches.R;

/** Per-mode image resource bundles for the Big Ben watch. */
public class BigBenPalettes implements Palettes<BigBenPalettes.BigBenPalette> {
  @Override public BigBenPalette buildActivePalette() {
    return new BigBenPalette(
        R.drawable.bigben_face, R.drawable.bigben_hhand, R.drawable.bigben_mhand);
  }

  @Override public BigBenPalette buildPassivePalette() {
    return new BigBenPalette(
        R.drawable.bigben_dark_face, R.drawable.bigben_dark_hhand, R.drawable.bigben_dark_mhand);
  }

  // Palette struct for drawing a particular mode.
  public static class BigBenPalette {
    public final int faceResource;
    public final int hourHandResource;
    public final int minsHandResource;

    public BigBenPalette(int faceResource, int hourHandResource, int minsHandResource) {
      this.faceResource = faceResource;
      this.hourHandResource = hourHandResource;
      this.minsHandResource = minsHandResource;
    }

    public void preload(ImageLoader imageLoader) {
      imageLoader.preload(faceResource);
      imageLoader.preload(hourHandResource);
      imageLoader.preload(minsHandResource);
    }
  }
}
