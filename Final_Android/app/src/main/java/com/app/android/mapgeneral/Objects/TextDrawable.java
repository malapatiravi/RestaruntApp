package com.app.android.mapgeneral.Objects;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;

/**
 * Created by suzzett on 8/14/16.
 */
public class TextDrawable extends Drawable
{
  private final String text;
  private final Paint paint;
  public TextDrawable(String text)
  {

    this.text = text;

    this.paint = new Paint();
    paint.setColor(Color.WHITE);
    paint.setTextSize(50f);
    paint.setAntiAlias(true);
    paint.setFakeBoldText(true);
    paint.setShadowLayer(6f, 0, 0, Color.BLACK);
    paint.setStyle(Paint.Style.FILL);
    paint.setTextAlign(Paint.Align.LEFT);
  }

  @Override
  public void draw(Canvas canvas)
  {
    paint.setColor(Color.RED);
    canvas.drawCircle(20,0,40, paint);
    paint.setColor(Color.WHITE);
    canvas.drawText(text, 0, 15, paint);
  }

  @Override
  public void setAlpha(int alpha)
  {
    paint.setAlpha(alpha);
  }

  @Override
  public void setColorFilter(ColorFilter cf)
  {
    paint.setColorFilter(cf);
  }

  @Override
  public int getOpacity()
  {
    return PixelFormat.TRANSLUCENT;
  }
}
