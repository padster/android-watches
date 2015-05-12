package com.padsterprogramming.watches;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * Interface over the bitmap image loading in Android.
 * Allows images to be preloaded and scaled.
 */
public class ImageLoader {
    private final Context context;
    private final Map<Integer, Bitmap> resourceCache = new WeakHashMap<>();
    private final Map<List<Integer>, Bitmap> scaledResourceCache = new WeakHashMap<>();

    public ImageLoader(Context context) {
        this.context = context;
    }

    /** Force an image to be loaded now, to avoid a future load delay when timing is important. */
    public void preload(int resourceId) {
        ensureLoaded(resourceId);
    }

    /** @return A bitmap resource, scaled to a fixed width and height. */
    public Bitmap getBitmap(int resourceId, int width, int height) {
        List<Integer> key = new ArrayList<>();
        key.add(resourceId);
        key.add(width);
        key.add(height);
        if (scaledResourceCache.containsKey(key)) {
            return scaledResourceCache.get(key);
        }

        Bitmap original = ensureLoaded(resourceId);
        if (original.getWidth() == width && original.getHeight() == height) {
            scaledResourceCache.put(key, original);
        } else {
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(original, width, height, true /* filter */);
            scaledResourceCache.put(key, scaledBitmap);
        }
        return scaledResourceCache.get(key);
    }

    /** @return A bitmap resource, scaled to a given height, with the original aspect ratio. */
    public Bitmap scaleBitmapToHeight(int resourceId, int newHeight) {
        Bitmap original = ensureLoaded(resourceId);
        // newWidth / newHeight = oldWidth / oldHeight, so newWidh = oldWidth * (newHeight / oldHeight);
        int newWidth = (int) (original.getWidth() * newHeight / (1. * original.getHeight()));
        return getBitmap(resourceId, newWidth, newHeight);
    }

    /** @return A bitmap resource, scaled to a given width, with the original aspect ratio. */
    public Bitmap scaleBitmapToWidth(int resourceId, int newWidth) {
        Bitmap original = ensureLoaded(resourceId);
        int newHeight = (int) (original.getHeight() * newWidth / (1. * original.getWidth())); // as above.
        return getBitmap(resourceId, newWidth, newHeight);
    }

    private Bitmap ensureLoaded(int resourceId) {
        if (!resourceCache.containsKey(resourceId)) {
            // NOTE: It would also be possible to assert fail here if it wasn't preloaded.
            Drawable drawable = context.getResources().getDrawable(resourceId);
            resourceCache.put(resourceId, ((BitmapDrawable) drawable).getBitmap());
        }
        return resourceCache.get(resourceId);
    }
}
