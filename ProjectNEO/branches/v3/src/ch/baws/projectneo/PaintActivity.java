package ch.baws.projectneo;

import ch.baws.projectneo.effects.PaintEffect;
import ch.baws.projectneo.frameGenerator.Frame;
import ch.baws.projectneo.minions.PaintbrushMovedListener;
import ch.baws.projectneo.minions.PaintbrushView;
import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class PaintActivity extends Activity implements OnClickListener,PaintbrushMovedListener{

	private Button btn_red,btn_green,btn_blue,btn_cyan,btn_magenta,btn_yellow,btn_black,btn_white;
	private PaintbrushView brush;
	
	private PaintEffect effect;
	private ProjectMORPHEUS application;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.eff_paint);
			
		application = (ProjectMORPHEUS) getApplication();

		btn_red 	= (Button) findViewById(R.id.btn_red);
		btn_green 	= (Button) findViewById(R.id.btn_green);
		btn_blue 	= (Button) findViewById(R.id.btn_blue);
		btn_cyan 	= (Button) findViewById(R.id.btn_cyan);
		btn_magenta = (Button) findViewById(R.id.btn_magenta);
		btn_yellow 	= (Button) findViewById(R.id.btn_yellow);
		btn_black 	= (Button) findViewById(R.id.btn_black);
		btn_white 	= (Button) findViewById(R.id.btn_white);
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
			break;
		case R.id.btn_green:
			effect.setColor(Frame.NEO_GREEN);
			break;
		case R.id.btn_blue:
			effect.setColor(Frame.NEO_BLUE);
			break;
		case R.id.btn_cyan:
			effect.setColor(Frame.NEO_CYAN);
			break;
		case R.id.btn_magenta:
			effect.setColor(Frame.NEO_MAGENTA);
			break;
		case R.id.btn_yellow:
			effect.setColor(Frame.NEO_YELLOW);
			break;
		case R.id.btn_black:
			effect.setColor(Frame.NEO_OFF);
			break;
		case R.id.btn_white:
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
