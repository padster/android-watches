package com.padsterprogramming.watches.faces;

import android.graphics.Paint;
import android.graphics.Typeface;
import com.padsterprogramming.watches.Paints;

/**
 * Colours for the Fibonacci face in both modes.
 */
public class FibonacciPalette {
    // Lazy-created palettes for each mode.
    private ModePalette activePaints;
    private ModePalette passivePaints;

    public ModePalette getActivePalette() {
        if (activePaints == null) {
            activePaints = new ModePalette(
                    Paints.WHITE,
                    Paints.fromRgb(255, 0, 0),
                    Paints.fromRgb(0, 255, 0),
                    Paints.fromRgb(0, 0, 255),
                    strokeGrey(0, 5));
        }
        return activePaints;
    }

    public ModePalette getPassivePalette() {
        if (passivePaints == null) {
            passivePaints = new ModePalette(
                    Paints.fromGrey(0),
                    Paints.fromRgb(60, 10, 10),
                    Paints.fromRgb(10, 60, 10),
                    Paints.fromRgb(10, 10, 60),
                    strokeGrey(50, 3));
        }
        return passivePaints;
    }

    // Palette struct for drawing a particular mode.
    public static class ModePalette {
        public final Paint nullColour;
        public final Paint hourColour;
        public final Paint minsColour;
        public final Paint bothColour;
        public final Paint stroke;

        public ModePalette(Paint nullColour, Paint hourColour, Paint minsColour, Paint bothColour, Paint stroke) {
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
