package ch.baws.projectneo;

import ch.baws.projectneo.effects.HumanSnakePlayer;
import ch.baws.projectneo.effects.HumanSnakePlayer.Dir;
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

public class JoystickSnakeActivity extends Activity implements JoystickMovedListener, OnClickListener{

	protected static final String TAG = "HSNAKEACTIVITY";
	protected static final boolean D = true;
	
	private Handler mHandler = new Handler();
	private class ScoreUpdater implements Runnable{
		TextView score = (TextView) findViewById(R.id.snake_score_view);;
		@Override
		public void run() {
			score.setText(Integer.toString(effect.getScore()));
			mHandler.postDelayed(this, 100); //recursive call
		}
	}
	
	private ScoreUpdater mScoreUpdater;
	private ProjectMORPHEUS application;
	private HumanSnakePlayer effect;
	private JoystickView jstk;
	private Button btn_newgame;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		if(D) Log.d(TAG,"On create");
		setContentView(R.layout.eff_joysticksnake);
		
		jstk = (JoystickView) findViewById(R.id.jstk_snake);
		jstk.setOnJostickMovedListener(this);
		
		btn_newgame = (Button) findViewById(R.id.btn_newgame);
		btn_newgame.setOnClickListener(this);
		mScoreUpdater = new ScoreUpdater();
		application = (ProjectMORPHEUS) getApplication();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		super.onPause();
		mHandler.removeCallbacks(mScoreUpdater);
	}

	@Override
	protected void onRestart() {
		super.onRestart();
	}

	@Override
	protected void onResume() {
		super.onResume();
		effect = new HumanSnakePlayer();
		application.setEffect(effect);
		mHandler.removeCallbacks(mScoreUpdater);
		mHandler.postDelayed(mScoreUpdater, 100);
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	
	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.btn_newgame){
			effect.newGame();
		}
		
	}

	private static final int RTHRESHOLD = 60;
	@Override
	public void OnMoved(int x, int y) {
		if(D) Log.d(TAG,"Joystick moved!");
		//determine angle (empiric^^)
		
		double radius = Math.sqrt(x*x+y*y);
		//dont use the direction if it isn't very precise
		if(D) Log.d(TAG,"Radius: " + radius);
		if(radius<RTHRESHOLD) return;
		//determine direction
		if(x<y){
			if(y>-x){
				//down
				if(D) Log.d(TAG,"go Up");
				effect.setDir(Dir.DOWN);
			}else{
				//left
				if(D) Log.d(TAG,"go Left");
				effect.setDir(Dir.LEFT);
			}
		}else{
			if(y>-x){
				//right
				if(D) Log.d(TAG,"go right");
				effect.setDir(Dir.RIGHT);
			}else{
				//up
				if(D) Log.d(TAG,"go down");
				effect.setDir(Dir.UP);
			}	
		}

	}

	@Override
	public void OnReleased() {
		if(D) Log.d(TAG,"+OnRelease+");
		//Pause snake?
	}

}
