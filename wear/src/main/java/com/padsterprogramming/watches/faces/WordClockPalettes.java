package com.padsterprogramming.watches.faces;

import android.graphics.Paint;
import android.graphics.Typeface;

import com.padsterprogramming.watches.BasePaletteWatchface.Palettes;
import com.padsterprogramming.watches.Paints;

/** Colours for the WordClock in both modes. */
public class WordClockPalettes implements Palettes<WordClockPalettes.WorldClockPalette> {
    @Override public WorldClockPalette buildActivePalette() {
        return new WorldClockPalette(
                fixedWidthText(Paints.fromGrey(180),  true),
                fixedWidthText(Paints.fromGrey( 55), false));
    }

    @Override public WorldClockPalette buildPassivePalette() {
        return new WorldClockPalette(
                fixedWidthText(Paints.fromGrey(70), true),
                null /* Inactive letters disabled. */);
    }

    // Palette struct for drawing a particular mode.
    public static class WorldClockPalette {
        public final Paint onColour;
        public final Paint offColour;

        public WorldClockPalette(Paint onColour, Paint offColour) {
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
