package com.padsterprogramming.watches.faces;

import android.graphics.*;
import android.text.format.Time;
import com.padsterprogramming.watches.BasePaletteWatchface;
import com.padsterprogramming.watches.Paints;
import com.padsterprogramming.watches.faces.HalographPalettes.HalographPalette;
import com.padsterprogramming.watches.services.WatchContext;

/**
 * Watchface with the same dual-circle time-indicator as used by Halograph:
 * https://www.kickstarter.com/projects/watchismo/the-halograph-an-unusual-mechanical-automatic-watc
 *
 * Electronic 'skeleton' insides of watch taken from iFixit teardown:
 * https://www.ifixit.com/Teardown/LG+G+Watch+Teardown/27037
 */
public class HalographFace extends BasePaletteWatchface<HalographPalette> {
  private static final float SEMI_DEG = 180.0f; // Path curves use degrees, not radians :(
  private static final float PAD_DEG = 30f;
  private static final float CURVE_WIDTH = 18f;

  public HalographFace(WatchContext context) {
    super(context, new HalographPalettes());
  }

  @Override protected void drawMode(Time currentTime, Canvas canvas, Rect bounds, HalographPalette palette) {
    // Background and optional behind-numbers semicircle.
    canvas.drawRect(bounds, palette.backgroundPaint);
    if (palette.behindNumbersPaint != null) {
      Path behindNumbersPath = new Path();
      behindNumbersPath.moveTo(0, bounds.exactCenterY());
      behindNumbersPath.arcTo(new RectF(bounds), -SEMI_DEG, SEMI_DEG);
      behindNumbersPath.lineTo(0, bounds.exactCenterY());
      canvas.drawPath(behindNumbersPath, palette.behindNumbersPaint);
    }

    float cX = bounds.exactCenterX();
    float cY = bounds.exactCenterY();
    float rad = Math.min(cX, cY);
    RectF semicircleClipRect = new RectF(
        bounds.left, bounds.top, bounds.right, (6 * bounds.top + 7 * bounds.bottom) / 13f);

    // 'Skeleton' view inside watch showing electronics underneath.
    if (palette.skeletonResource != -1) {
      int imageWidth = bounds.width() / 2;
      int imageHeight = bounds.height() / 5;
      float imageLeft = bounds.width() / 4f;
      float imageTop = bounds.height() * 5f / 8f;
      Bitmap image = context.imageLoader().getBitmap(palette.skeletonResource, imageWidth, imageHeight);
      RectF imageBounds = new RectF(imageLeft, imageTop, imageLeft + imageWidth, imageTop + imageHeight);
      canvas.drawBitmap(image, imageBounds.left, imageBounds.top, null);
      Paint p = new Paint(Paints.WHITE);
      p.setStyle(Paint.Style.STROKE);
      canvas.drawRect(imageBounds, p);
    }

    // Hour hand and numbers.
    double hours = (60 * (60 * currentTime.hour + currentTime.minute) + currentTime.second) / 3600.0;
    if (hours >= 12) {
      hours -= 12; // 12hr time.
    }
    float hourAngle = (float) (hours * 2 * Math.PI / 12f);

    float hourHandInner = 0.28f;
    float hourHandOuter = 0.44f;
    Path hourPathInner = topSemicircle(cX, cY, hourHandInner * rad);
    Path hourPathOuter = topSemicircle(cX, cY, hourHandOuter * rad);
    canvas.clipRect(semicircleClipRect, Region.Op.INTERSECT);
    canvas.drawPath(hourPathInner, palette.pathPaint);
    canvas.drawPath(hourPathOuter, palette.pathPaint);
    canvas.clipRect(bounds, Region.Op.UNION);
    drawSpacedNumbers(canvas, 3, 9, 12, hourHandInner * rad, hourPathInner,
        palette.hourMajorTextPaint, palette.hourMinorTextPaint, false /* minutes */);
    drawSpacedNumbers(canvas, 9, 3, 12, hourHandOuter * rad, hourPathOuter,
        palette.hourMajorTextPaint, palette.hourMinorTextPaint, false /* minutes */);
    radialLine(canvas, cX, cY,
        hourHandOuter * rad, -hourHandInner * rad, hourAngle,
        palette.armPaint, palette.hourHandPaint, 21f);

    // Minute hand and numbers.
    double minutes = (60 * currentTime.minute + currentTime.second) / 60.0;
    float minuteAngle = (float) (minutes * 2 * Math.PI / 60f);

    float minuteHandInner = 0.68f;
    float minuteHandOuter = 0.84f;
    Path minutePathInner = topSemicircle(cX, cY, minuteHandInner * rad);
    Path minutePathOuter = topSemicircle(cX, cY, minuteHandOuter * rad);
    canvas.clipRect(semicircleClipRect, Region.Op.INTERSECT);
    canvas.drawPath(minutePathInner, palette.pathPaint);
    canvas.drawPath(minutePathOuter, palette.pathPaint);
    canvas.clipRect(bounds, Region.Op.UNION);
    drawSpacedNumbers(canvas, 15, 45, 60, minuteHandInner * rad, minutePathInner,
        palette.minuteTextMajorPaint, palette.minuteTextMinorPaint, true /* minutes */);
    drawSpacedNumbers(canvas, 45, 15, 60, minuteHandOuter * rad, minutePathOuter,
        palette.minuteTextMajorPaint, palette.minuteTextMinorPaint, true /* minutes */);
    radialLine(canvas, cX, cY,
        minuteHandOuter * rad, -minuteHandInner * rad, minuteAngle,
        palette.armPaint, palette.minuteHandPaint, 18f);
  }

  /** From a center (cx, cy), draw a line at angle (ang), from lenFr -> lenTo distance, circle at end. */
  private void radialLine(
      Canvas canvas, float cx, float cy,
      float lenFr, float lenTo, float ang,
      Paint linePaint, Paint circlePaint, float circleRadius) {
    assert lenFr > 0 && lenTo < 0;

    // First offset, to draw lines that end before the centre of the circle.
    float offset = circleRadius * 0.33f;
    lenFr = lenFr - offset;
    lenTo = lenTo + offset;

    float dX = (float) Math.sin(ang);
    float dY = -(float) Math.cos(ang);
    float frX = cx + lenFr * dX;
    float frY = cy + lenFr * dY;
    float toX = cx + lenTo * dX;
    float toY = cy + lenTo * dY;
    canvas.drawLine(frX, frY, toX, toY, linePaint);

    // Add two triangles to connect the line ends with the circles.
    drawTriangle(canvas, circlePaint, frX, frY, circleRadius - offset, ang + (float) Math.PI);
    drawTriangle(canvas, circlePaint, toX, toY, circleRadius - offset, ang);

    // Draw circles at both ends undoing offsets.
    toX -= offset * dX;
    toY -= offset * dY;
    frX += offset * dX;
    frY += offset * dY;
    canvas.drawCircle(toX, toY, circleRadius, circlePaint);
    canvas.drawCircle(frX, frY, circleRadius, circlePaint);
  }

  /** @return A path following a top-half semi-circle with a given radius. */
  private static Path topSemicircle(float cx, float cy, float radius) {
    Path path = new Path();
    RectF bounds = new RectF();

    // Draw inner curve first, for text baseline
    radius -= CURVE_WIDTH / 2;
    bounds.set(cx - radius, cy - radius, cx + radius, cy + radius);
    path.arcTo(bounds, -SEMI_DEG - PAD_DEG, SEMI_DEG + 2 * PAD_DEG);

    // and connect to outer curve.
    radius += CURVE_WIDTH;
    bounds.set(cx - radius, cy - radius, cx + radius, cy + radius);
    path.arcTo(bounds, PAD_DEG, -SEMI_DEG - 2 * PAD_DEG);
    return path;
  }

  /** Draw a triangle to make a pie-shape, given a position, radius, and wedge arc angle. */
  private static void drawTriangle(Canvas canvas, Paint paint, float atX, float atY, float radius, float angle) {
    float arc = 0.6f;
    float controlDX = (float) Math.sin(angle) * radius * 0.5f;
    float controlDY = - (float) Math.cos(angle) * radius * 0.5f;
    float p1X = atX + (float) Math.sin(angle - arc) * radius;
    float p1Y = atY - (float) Math.cos(angle - arc) * radius;
    float p2X = atX + (float) Math.sin(angle + arc) * radius;
    float p2Y = atY - (float) Math.cos(angle + arc) * radius;
    float startX = atX - controlDX * 0.7f;
    float startY = atY - controlDY * 0.7f;

    // Make the triangle from three curves - two wedge arcs curving inwards, and the connector outwards.
    Paint fillPaint = new Paint(paint);
    fillPaint.setStyle(Paint.Style.FILL);
    Path path = new Path();
    path.moveTo(startX, startY);
    path.quadTo(atX + controlDX, atY + controlDY, p1X, p1Y);
    path.quadTo(atX + 3 * controlDX, atY + 3 * controlDY, p2X, p2Y);
    path.quadTo(atX + controlDX, atY + controlDY, startX, startY);
    canvas.drawPath(path, fillPaint);
  }

  /** Draws consecutive numbers spaced out along an arc'd path. */
  private static void drawSpacedNumbers(Canvas canvas,
      int from, int to, int modBase, float radius,
      Path path, Paint majorPaint, Paint minorPaint, boolean minuteHand) {
    float halfWidth = CURVE_WIDTH / 2;
    radius -= halfWidth;

    // Spacing between each number.
    int count = to > from ? to - from + 1 : to - from + 1 + modBase;
    float arcLength = (float) Math.PI * radius;
    float perItem = arcLength / (count - 1);

    float hOffset = (PAD_DEG * (float) Math.PI / 180f) * radius;
    float vOffsetMajor = -halfWidth - majorPaint.getFontMetrics().ascent / 2;
    float vOffsetMinor = minorPaint == null ? 0 :
        -halfWidth - minorPaint.getFontMetrics().ascent + majorPaint.getFontMetrics().ascent / 2f;

    int number = from;
    while(true) {
      String text = Integer.toString(number);
      if (number < 10 && minuteHand) {
        text = "0" + text;
      }

      boolean isMinor = minuteHand && (number % 5 != 0);
      float vOffset = isMinor ? vOffsetMinor : vOffsetMajor;
      Paint paint = isMinor ? minorPaint : majorPaint;
      if (paint != null) {
        float newOffset = hOffset - paint.measureText(text) / 2.0f;
        canvas.drawTextOnPath(text, path, newOffset, vOffset, paint);
      }

      if (number == to) {
        break;
      }
      hOffset += perItem;
      number = number % modBase + 1;
    }
  }
}
