package com.padsterprogramming.watches;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.wearable.watchface.CanvasWatchFaceService;
import android.support.wearable.watchface.WatchFaceStyle;
import android.text.format.Time;
import android.view.Gravity;
import android.view.SurfaceHolder;

import com.padsterprogramming.watches.faces.MondaineFace;

/** Service that renders a simple watchface, managing the life cycle. */
public class SimpleWatchfaceService extends CanvasWatchFaceService {
  private static final String TAG = "Mondaine";

  @Override
  public Engine onCreateEngine() {
    // Put whatever face you want here...
    return new Engine(new MondaineFace());
  }

  // Wraps a simple watchface as an engine.
  private final class Engine extends CanvasWatchFaceService.Engine {
    private final SimpleWatchface face;

    public Engine(SimpleWatchface face) {
      this.face = face;
    }

    @Override
    public void onCreate(SurfaceHolder holder) {
      super.onCreate(holder);
      face.createSingletons();

      setWatchFaceStyle(new WatchFaceStyle.Builder(SimpleWatchfaceService.this)
          .setCardPeekMode(WatchFaceStyle.PEEK_MODE_SHORT)
          .setStatusBarGravity(Gravity.TOP | Gravity.LEFT)
          .setViewProtection(WatchFaceStyle.PROTECT_STATUS_BAR | WatchFaceStyle.PROTECT_HOTWORD_INDICATOR)
          .setHotwordIndicatorGravity(Gravity.TOP | Gravity.RIGHT)
          .setBackgroundVisibility(WatchFaceStyle.BACKGROUND_VISIBILITY_INTERRUPTIVE)
          .setShowSystemUiTime(false)
          .build());
    }

    @Override
    public void onTimeTick() {
      super.onTimeTick();
      if (isVisible()) {
        invalidate();
      }
    }

    @Override
    public void onVisibilityChanged(boolean visible) {
      super.onVisibilityChanged(visible);
      if (visible) {
        invalidate();
      }
    }

    @Override
    public void onDraw(Canvas canvas, Rect bounds) {
      super.onDraw(canvas, bounds);

      Time currentTime = new Time();
      currentTime.setToNow();
      if (!isInAmbientMode()) {
        face.drawActive(currentTime, canvas, bounds);
      } else {
        face.drawPassive(currentTime, canvas, bounds);
      }

      if (isVisible() && !isInAmbientMode()) {
        invalidate();
      }
    }
  }
}
