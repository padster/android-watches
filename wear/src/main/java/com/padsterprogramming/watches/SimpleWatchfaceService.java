package com.padsterprogramming.watches;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.support.wearable.watchface.CanvasWatchFaceService;
import android.support.wearable.watchface.WatchFaceStyle;
import android.text.format.Time;
import android.view.Gravity;
import android.view.SurfaceHolder;
import android.view.WindowInsets;

import com.padsterprogramming.watches.faces.*;
import com.padsterprogramming.watches.services.WatchContext;
import com.padsterprogramming.watches.services.WatchMetrics;

/** Service that renders a simple watchface, managing the life cycle. */
public class SimpleWatchfaceService extends CanvasWatchFaceService {
  private static final String TAG = "Simple Watchface";

  @Override public Engine onCreateEngine() {
    WatchContext context = new WatchContext(this);
    return new Engine(new HalographFace(context), context);
  }

  // Wraps a simple watchface as an engine.
  private final class Engine extends CanvasWatchFaceService.Engine {
    private final SimpleWatchface face;
    private final WatchContext context;

    public Engine(SimpleWatchface face, WatchContext context) {
      this.face = face;
      this.context = context;
    }

    @Override public void onCreate(SurfaceHolder holder) {
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

    @Override public void onTimeTick() {
      super.onTimeTick();
      if (isVisible()) {
        // Ambient mode, and visible, so force redraw.
        invalidate();
      }
    }

    @Override public void onVisibilityChanged(boolean visible) {
      super.onVisibilityChanged(visible);
      if (visible) {
        // Redraw only when transitioning to visible.
        invalidate();
        handler.sendEmptyMessage(0);
      }
    }

    @Override public void onAmbientModeChanged(boolean inAmbientMode) {
      // Always force redraw when Ambient mode changes.
      super.onAmbientModeChanged(inAmbientMode);
      invalidate();
      if (!inAmbientMode) {
        handler.sendEmptyMessage(0);
      }
    }

    @Override public void onDraw(Canvas canvas, Rect bounds) {
      super.onDraw(canvas, bounds);
      context.watchMetrics().handleBounds(bounds);

      Time currentTime = new Time();
      currentTime.setToNow();

      // NOTE: Every time these are called, you really should draw something.
      // It appears that Android cycles through three buffers, and if you don't draw anything,
      // it cycles to a previous (out-of-date) one anyway...
      if (!isInAmbientMode()) {
        face.drawActive(currentTime, canvas, bounds);
      } else {
        face.drawPassive(currentTime, canvas, bounds);
      }
    }

    @Override public void onApplyWindowInsets(WindowInsets insets) {
      context.watchMetrics().handleWindowInsets(insets);
    }

    /** Forces a redraw to be scheduled with a given period. */
    private final Handler handler = new Handler() {
      @Override public void handleMessage(Message message) {
        if (isVisible() && !isInAmbientMode()) {
          invalidate();
          int period = face.activeDrawPeriodMs();
          long timeMs = System.currentTimeMillis();
          long delayMs = period - timeMs % period; // Delay until next draw period.
          this.sendEmptyMessageDelayed(0, delayMs);
        }
      }
    };
  }
}
