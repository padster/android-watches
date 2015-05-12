package com.padsterprogramming.watches;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.text.format.Time;

/**
 * Optional Watchface utility that uses the same logic to draw both active and passive faces,
 * just with a different palette of drawing information.
 *
 * To use one of these, provide a custom 'palettes' definition, then implement drawMode to draw given a palette.
 * The other base watchface options (e.g. singletons, draw MS, context etc.) are also available.
 */
public abstract class BasePaletteWatchface<Palette> extends BaseSimpleWatchface {
    /** Builds the palettes, lazily invoked once when first needed. */
    public interface Palettes<P> {
        P buildActivePalette();
        P buildPassivePalette();
    }

    private final Palettes<Palette> palettes;
    private Palette activePalette;
    private Palette passivePalette;

    /** Build the face given the normal details, plus details how to build the palettes. */
    protected BasePaletteWatchface(Context context, WatchMetrics metrics, Palettes<Palette> palettes) {
        super(context, metrics);
        this.palettes = palettes;
    }

    /** Draw the face, given the palette for the current mode. */
    protected abstract void drawMode(Time currentTime, Canvas canvas, Rect bounds, Palette palette);

    @Override public void drawActive(Time currentTime, Canvas canvas, Rect bounds) {
        drawMode(currentTime, canvas, bounds, ensureActivePalette());
    }

    @Override public void drawPassive(Time currentTime, Canvas canvas, Rect bounds) {
        drawMode(currentTime, canvas, bounds, ensurePassivePalette());
    }

    protected Palette ensureActivePalette() {
        if (activePalette == null) {
            activePalette = palettes.buildActivePalette();
        }
        return activePalette;
    }

    protected Palette ensurePassivePalette() {
        if (passivePalette == null) {
            passivePalette = palettes.buildPassivePalette();
        }
        return passivePalette;
    }
}
