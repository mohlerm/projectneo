package ch.baws.projectneo;

import ch.baws.projectneo.effects.HumanSnakePlayer;
import ch.baws.projectneo.effects.HumanSnakePlayer.Dir;
import ch.baws.projectneo.mario.MarioEffect;
import ch.baws.projectneo.minions.JoystickMovedListener;
import ch.baws.projectneo.minions.JoystickView;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MarioActivity extends Activity implements JoystickMovedListener, OnClickListener{

	protected static final String TAG = "HSNAKEACTIVITY";
	protected static final boolean D = true;
	
	private Handler mHandler = new Handler();
	private class ScoreUpdater implements Runnable{
		TextView score = (TextView) findViewById(R.id.mario_score_view);
		@Override
		public void run() {
			//score.setText(Integer.toString(effect.getScore()));
			mHandler.postDelayed(this, 100);
		}
	}
	private ScoreUpdater mScoreUpdater;
	
	private ProjectMORPHEUS application;
	
	MarioEffect effect;
	
	JoystickView jstk;
	Button btn_newgame;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		if(D) Log.d(TAG,"On create");
		setContentView(R.layout.eff_mario);
		
		jstk = (JoystickView) findViewById(R.id.jstk_mario);
		jstk.setOnJostickMovedListener(this);
		
		btn_newgame = (Button) findViewById(R.id.btn_newgame);
		btn_newgame.setOnClickListener(this);
		mScoreUpdater = new ScoreUpdater();
		application = (ProjectMORPHEUS) getApplication();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		mHandler.removeCallbacks(mScoreUpdater);
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
	}

	@Override
	protected void onResume() {
		super.onResume();
		effect = new MarioEffect();
		application.setEffect(effect);
		mHandler.removeCallbacks(mScoreUpdater);
		mHandler.postDelayed(mScoreUpdater, 100);
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	
	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.btn_newgame){
			//effect.newGame();
		}
		
	}

	@Override
	public void OnMoved(int x, int y) {
		if(D) Log.d(TAG,"Joystick moved!");
		//determine angle (empiric^^)
		
		double radius = Math.sqrt(x*x+y*y);
		//dont use the direction if it isn't very precise
		if(D) Log.d(TAG,"Radius: " + radius);
		if(radius<50) return;
		//determine direction
		if(x<y){
			if(y>-x){
				//down
				if(D) Log.d(TAG,"go Up");
				//effect.setDir(Dir.DOWN);
			}else{
				//left
				if(D) Log.d(TAG,"go Left");
				effect.moveLeft();
			}
		}else{
			if(y>-x){
				//right
				if(D) Log.d(TAG,"go right");
				effect.moveRight();
			}else{
				//up
				if(D) Log.d(TAG,"go down");
				effect.moveJump();
			}	
		}

	}

	@Override
	public void OnReleased() {
		if(D) Log.d(TAG,"+OnRelease+");
		
	}

}
