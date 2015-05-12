package com.padsterprogramming.watches.faces;

import com.padsterprogramming.watches.ImageLoader;
import com.padsterprogramming.watches.R;

/** Per-mode image resource bundles for the Big Ben watch. */
public class BigBenPalette {
    // Lazy-created palettes for each mode.
    private ModePalette activePaints;
    private ModePalette passivePaints;

    public ModePalette getActivePalette() {
        if (activePaints == null) {
            activePaints =  new ModePalette(
                    R.drawable.bigben_face, R.drawable.bigben_hhand, R.drawable.bigben_mhand);
        }
        return activePaints;
    }

    public ModePalette getPassivePalette() {
        if (passivePaints == null) {
            passivePaints = new ModePalette(
                    R.drawable.bigben_dark_face, R.drawable.bigben_dark_hhand, R.drawable.bigben_dark_mhand);
        }
        return passivePaints;
    }

    // Palette struct for drawing a particular mode.
    public static class ModePalette {
        public final int faceResource;
        public final int hourHandResource;
        public final int minsHandResource;

        public ModePalette(int faceResource, int hourHandResource, int minsHandResource) {
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
