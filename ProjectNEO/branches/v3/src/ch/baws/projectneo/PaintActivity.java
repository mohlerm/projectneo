package ch.baws.projectneo;

import ch.baws.projectneo.effects.PaintEffect;
import ch.baws.projectneo.frameGenerator.Frame;
import ch.baws.projectneo.minions.PaintbrushMovedListener;
import ch.baws.projectneo.minions.PaintbrushView;
import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;

public class PaintActivity extends Activity implements OnClickListener,PaintbrushMovedListener{

	private ImageButton btn_red,btn_green,btn_blue,btn_cyan,btn_magenta,btn_yellow,btn_black,btn_white;
	private PaintbrushView brush;
	
	private PaintEffect effect;
	private ProjectMORPHEUS application;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.eff_paint);
			
		application = (ProjectMORPHEUS) getApplication();

		btn_red 	= (ImageButton) findViewById(R.id.btn_red);
		btn_green 	= (ImageButton) findViewById(R.id.btn_green);
		btn_blue 	= (ImageButton) findViewById(R.id.btn_blue);
		btn_cyan 	= (ImageButton) findViewById(R.id.btn_cyan);
		btn_magenta = (ImageButton) findViewById(R.id.btn_magenta);
		btn_yellow 	= (ImageButton) findViewById(R.id.btn_yellow);
		btn_black 	= (ImageButton) findViewById(R.id.btn_black);
		btn_white 	= (ImageButton) findViewById(R.id.btn_white);
		brush 		= (PaintbrushView) findViewById(R.id.paintbrushView1);
		
		btn_black.setOnClickListener(this);
		btn_blue.setOnClickListener(this);
		btn_cyan.setOnClickListener(this);
		btn_green.setOnClickListener(this);
		btn_magenta.setOnClickListener(this);
		btn_red.setOnClickListener(this);
		btn_white.setOnClickListener(this);
		btn_yellow.setOnClickListener(this);
		
		brush.setOnPaintbrushMovedListener(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		effect = new PaintEffect();
		application.setEffect(effect);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.btn_red:
			effect.setColor(Frame.NEO_RED);
			brush.setBrushColor(Color.argb(128,255, 0, 0));
			break;
		case R.id.btn_green:
			effect.setColor(Frame.NEO_GREEN);
			brush.setBrushColor(Color.argb(128,0, 255, 0));
			break;
		case R.id.btn_blue:
			effect.setColor(Frame.NEO_BLUE);
			brush.setBrushColor(Color.argb(128,0, 0, 255));
			break;
		case R.id.btn_cyan:
			effect.setColor(Frame.NEO_CYAN);
			brush.setBrushColor(Color.argb(128,0, 255, 255));
			break;
		case R.id.btn_magenta:
			brush.setBrushColor(Color.argb(128,255, 0, 255));
			effect.setColor(Frame.NEO_MAGENTA);
			break;
		case R.id.btn_yellow:
			brush.setBrushColor(Color.argb(128,255, 255, 0));
			effect.setColor(Frame.NEO_YELLOW);
			break;
		case R.id.btn_black:
			brush.setBrushColor(Color.argb(128,0, 0, 0));
			effect.setColor(Frame.NEO_OFF);
			break;
		case R.id.btn_white:
			brush.setBrushColor(Color.argb(128,255, 255, 255));
			effect.setColor(Frame.NEO_WHITE);
			break;
		}
	}

	@Override
	public void OnMoved(int pan, int tilt) {
		effect.setField(pan, tilt);
	}

	@Override
	public void OnReleased() {
		// TODO Auto-generated method stub
		
	}

}
