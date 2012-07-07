package ch.baws.projectneo.minions;

import ch.baws.projectneo.frameGenerator.Frame;
import android.content.Context;
import android.graphics.*;
import android.graphics.BlurMaskFilter.Blur;
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
  private final boolean D = true;
  
  private Paint deskPaint;
  
  private Paint brushPaint;
  
  private double touchX, touchY;
  private int brushRadius = 50;
  private PaintbrushMovedListener listener;
  private int brushColor;
  
  private int[][] painting;
  
  private Paint[] colorPoint;
  
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
    
    painting = Utils.getEmpty8x8();
    
    deskPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    deskPaint.setColor(Color.BLACK);
    deskPaint.setStrokeWidth(10);
    deskPaint.setStyle(Paint.Style.STROKE);
    deskPaint.setStrokeWidth(10);
    float[] f = {20,10,10,10};
    deskPaint.setPathEffect(new DashPathEffect(f, 0));
   
    for(int i=0;i<7;i++){
    	colorPoint[i] = new Paint(Paint.ANTI_ALIAS_FLAG);
    	colorPoint[i].setColor(Frame.NEO_COLOR_LST[i]);
    	colorPoint[i].setStyle(Paint.Style.FILL);
    	colorPoint[i].setMaskFilter(new BlurMaskFilter(5,Blur.NORMAL));
    }

    brushPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    brushPaint.setColor(Color.argb(128,255,0,0));
    brushPaint.setMaskFilter(new BlurMaskFilter(15, Blur.NORMAL));
    //brushPaint.setShader(new RadialGradient((float)touchX + px, (float)touchY+py, brushRadius*2, Color.BLUE, Color.argb(128, 0, 0, 255), Shader.TileMode.CLAMP));
    brushPaint.setStrokeWidth(5);
    brushPaint.setStyle(Paint.Style.FILL_AND_STROKE);
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
  
  public void setBrushColor(int color){
	  this.brushColor = color;
  }
  
  public void setPainting(int[][] painting){
	  this.painting = painting;
	  invalidate();
  }
  
  public void setPoint(int x,int y,int color){
	  throw new NotImplementedException();
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

    brushRadius = (int)(d * 0.05);
    brushRadius = Math.max(brushRadius, 10);
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
    
    int boarder = 10;
    
    int pradius = (int) (sx/16.0);
    
    //float radius = (float) (Math.min(px, py) * 0.7);

    // Draw the background
    //canvas.drawCircle(px, py, radius - innerPadding, circlePaint);
    try{
    	canvas.drawRect(boarder, s-boarder, s-boarder, boarder, deskPaint);
    	for(int i=0;i<7;i++){
        	for(int j=0;j<7;j++){
        		int c = painting[i][j];
        		if(c>7 || c<0) c = 0;
        		if(D) Log.d(TAG, "Drawing the picture");
        		canvas.drawCircle(boarder+pradius*i, boarder+pradius*j, pradius, colorPoint[c]);
        	}
    	}
    }catch(Exception e){
    	Log.e(TAG,"can't draw rectangle");
    }
    // Draw the brush
    brushPaint.setColor(brushColor);
    canvas.drawCircle((int) touchX, (int) touchY, brushRadius,brushPaint);
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
      if(D) Log.d(TAG, "X:" + touchX + "|Y:" + touchY);

      // Pressure
      if (listener != null) {
        listener.OnMoved((int) ((touchX)/sx * 8), (int) ((touchY)/sy * 8));
      }
      invalidate();
    } else if (actionType == MotionEvent.ACTION_UP) {
      if(D) Log.d(TAG, "X:" + touchX + "|Y:" + touchY);
    }
    return true;
  }
}