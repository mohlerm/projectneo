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
public class JoystickView extends View {

  // =========================================
  // Private Members
  // =========================================

  private final String TAG = "JoystickView";
  private Paint circlePaint;
  private Paint handlePaint;
  private double touchX, touchY;
  private int innerPadding;
  private int handleRadius;
  private int handleInnerBoundaries;
  private JoystickMovedListener listener;
  private int sensitivity;

  // =========================================
  // Constructors
  // =========================================

  public JoystickView(Context context) {
    super(context);
    initJoystickView();
  }

  public JoystickView(Context context, AttributeSet attrs) {
    super(context, attrs);
    initJoystickView();
  }

  public JoystickView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    initJoystickView();
  }

  // =========================================
  // Initialization
  // =========================================

  private void initJoystickView() {
    setFocusable(true);

    circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    circlePaint.setColor(Color.GRAY);
    circlePaint.setStrokeWidth(1);
    circlePaint.setStyle(Paint.Style.STROKE);
    circlePaint.setStrokeWidth(10);
    float[] f = {20,10,10,10};
    circlePaint.setPathEffect(new DashPathEffect(f, 0));

    handlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    handlePaint.setColor(Color.argb(128, 0, 0, 255));
    handlePaint.setMaskFilter(new BlurMaskFilter(15, Blur.NORMAL));
    
    handlePaint.setStrokeWidth(3);
    handlePaint.setStyle(Paint.Style.FILL_AND_STROKE);

    innerPadding = 10;
    sensitivity = 100;
  }

  // =========================================
  // Public Methods 
  // =========================================
 /**
  * Used to set an OnMoved callback method
  * @param listener an callback for the onMoved event
  */
  public void setOnJostickMovedListener(JoystickMovedListener listener) {
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

    handleRadius = (int)(d * 0.25);
    handleInnerBoundaries = handleRadius;
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
    int px = getMeasuredWidth() / 2;
    int py = getMeasuredHeight() / 2;
    float radius = (float) (Math.min(px, py) * 0.7);

    // Draw the background
    canvas.drawCircle(px, py, radius - innerPadding, circlePaint);

    handlePaint.setShader(new RadialGradient((float)touchX + px, (float)touchY+py, handleRadius*2, Color.BLUE, Color.argb(128, 0, 0, 255), Shader.TileMode.CLAMP));
    // Draw the handle
    canvas.drawCircle((int) touchX + px, (int) touchY + py, handleRadius,handlePaint);

    canvas.save();
  }

  @Override
  public boolean onTouchEvent(MotionEvent event) {
    int actionType = event.getAction();
    if (actionType == MotionEvent.ACTION_MOVE) {
      int px = getMeasuredWidth() / 2;
      int py = getMeasuredHeight() / 2;
      int radius = Math.min(px, py) - handleInnerBoundaries; //maximum radius of inner thing
      
      touchX = (event.getX() - px);
      touchY = (event.getY() - py);
      //touchX = Math.max(Math.min(touchX, radius), -radius);
      //touchY = Math.max(Math.min(touchY, radius), -radius);
      
      double rad = Math.sqrt(touchX*touchX+touchY*touchY);
      if(Double.compare(rad, radius)>0){
	      touchX *= radius/rad;
	      touchY *= radius/rad;
	      rad = radius;
      }
      
      // Coordinates
      Log.d(TAG, "X:" + touchX + "|Y:" + touchY);

      // Pressure
      if (listener != null) {
        listener.OnMoved((int) (touchX / radius * sensitivity), (int) (touchY  / radius * sensitivity));
      }

      invalidate();
    } else if (actionType == MotionEvent.ACTION_UP) {
      returnHandleToCenter();
      Log.d(TAG, "X:" + touchX + "|Y:" + touchY);
    }
    return true;
  }

  private void returnHandleToCenter() {

    Handler handler = new Handler();
    int numberOfFrames = 5;
    final double intervalsX = (0 - touchX) / numberOfFrames;
    final double intervalsY = (0 - touchY) / numberOfFrames;

    for (int i = 0; i < numberOfFrames; i++) {
      handler.postDelayed(new Runnable() {
        @Override
        public void run() {
          touchX += intervalsX;
          touchY += intervalsY;
          invalidate();
        }
      }, i * 40);
    }

    if (listener != null) {
      listener.OnReleased();
    }
  }
}