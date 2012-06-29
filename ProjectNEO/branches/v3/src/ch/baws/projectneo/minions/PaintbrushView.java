package ch.baws.projectneo.minions;

import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.graphics.BlurMaskFilter.Blur;
import android.location.Criteria;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Implements a primitive JoyStick
 * @author thomas
 *
 */
public class PaintbrushView extends View {

  // =========================================
  // Private Members
  // =========================================

  private final String TAG = "PBView";
  
  private Paint deskPaint;
  
  private Paint brushPaint;
  
  private double touchX, touchY;
  private int innerPadding;
  private int brushRadius;
  private int brushInnerBoundaries;
  private PaintbrushMovedListener listener;
  private int sensitivity;
  
  private boolean pressed;

  // =========================================
  // Constructors
  // =========================================

  public PaintbrushView(Context context) {
    super(context);
    initJoystickView();
  }

  public PaintbrushView(Context context, AttributeSet attrs) {
    super(context, attrs);
    initJoystickView();
  }

  public PaintbrushView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    initJoystickView();
  }

  // =========================================
  // Initialization
  // =========================================

  private void initJoystickView() {
    setFocusable(true);
    int px = getMeasuredWidth() / 2;
    int py = getMeasuredHeight() / 2;
    deskPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    deskPaint.setColor(Color.TRANSPARENT);
    deskPaint.setStrokeWidth(10);
    deskPaint.setStyle(Paint.Style.STROKE);
    deskPaint.setStrokeWidth(10);
    float[] f = {20,10,10,10};
    deskPaint.setPathEffect(new DashPathEffect(f, 0));

    brushPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    brushPaint.setColor(Color.argb(128, 0, 0, 255));
    brushPaint.setMaskFilter(new BlurMaskFilter(15, Blur.NORMAL));
    brushPaint.setShader(new RadialGradient((float)touchX + px, (float)touchY+py, brushRadius*2, Color.BLUE, Color.argb(128, 0, 0, 255), Shader.TileMode.CLAMP));
    brushPaint.setStrokeWidth(3);
    brushPaint.setStyle(Paint.Style.FILL_AND_STROKE);

    innerPadding = 10;
    sensitivity = 100;
    pressed = false;
  }

  // =========================================
  // Public Methods 
  // =========================================
 /**
  * Used to set an OnMoved callback method
  * @param listener an callback for the onMoved event
  */
  public void setOnPaintbrushMovedListener(PaintbrushMovedListener listener) {
    this.listener = listener;
  }
  
  // =========================================
  // Drawing Functionality 
  // =========================================

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    // Here we make sure that we have a perfect circle
    int measuredWidth = measure(widthMeasureSpec);
    int measuredHeight = measure(heightMeasureSpec);
    int d = Math.min(measuredWidth, measuredHeight);

    brushRadius = (int)(d * 0.15);
    brushInnerBoundaries = brushRadius;
    setMeasuredDimension(d, d);
  }

  private int measure(int measureSpec) {
    int result = 0;
    // Decode the measurement specifications.
    int specMode = MeasureSpec.getMode(measureSpec);
    int specSize = MeasureSpec.getSize(measureSpec);
    if (specMode == MeasureSpec.UNSPECIFIED) {
      // Return a default size of 200 if no bounds are specified.
      result = 200;
    } else {
      // As you want to fill the available space
      // always return the full available bounds.
      result = specSize;
    }
    return result;
  }

  @Override
  protected void onDraw(Canvas canvas) {
    int sx = getMeasuredWidth();
    int sy = getMeasuredHeight();
    
    int s = Math.min(sx, sy);
    
    //float radius = (float) (Math.min(px, py) * 0.7);

    // Draw the background
    //canvas.drawCircle(px, py, radius - innerPadding, circlePaint);
    
    canvas.drawRect(0, s, s, 0, deskPaint);

    
    // Draw the brush
    canvas.drawCircle((int) touchX + sx/2, (int) touchY + sy/2, brushRadius,brushPaint);

    canvas.save();
  }

  @Override
  public boolean onTouchEvent(MotionEvent event) {
    int actionType = event.getAction();
    if (actionType == MotionEvent.ACTION_MOVE) {
      int sx = getMeasuredWidth();
      int sy = getMeasuredHeight();
      
      touchX = (event.getX());
      touchY = (event.getY());
      
      touchX = Math.max(Math.min(touchX, sx), 0); //stay in bounds
      touchY = Math.max(Math.min(touchY, sy), 0);
      
      // Coordinates
      Log.d(TAG, "X:" + touchX + "|Y:" + touchY);

      // Pressure
      if (listener != null) {
        listener.OnMoved((int) (touchX/sx * 8), (int) (touchY/sy * 8));
      }
      pressed = true;
      invalidate();
    } else if (actionType == MotionEvent.ACTION_UP) {
      pressed = false;
      Log.d(TAG, "X:" + touchX + "|Y:" + touchY);
    }
    return true;
  }
}