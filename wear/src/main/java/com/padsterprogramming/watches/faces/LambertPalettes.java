package com.padsterprogramming.watches.faces;

import android.graphics.Paint;
import com.padsterprogramming.watches.BasePaletteWatchface.Palettes;
import com.padsterprogramming.watches.services.ImageLoader;
import com.padsterprogramming.watches.Paints;
import com.padsterprogramming.watches.R;

/** Customization for the two Lambert faces. */
public class LambertPalettes implements Palettes<LambertPalettes.LambertPalette> {

  @Override public LambertPalettes.LambertPalette buildActivePalette() {
    return null;
  }

  @Override public LambertPalettes.LambertPalette buildPassivePalette() {
    return null;
  }

  public class LambertPalette {
  }
}
