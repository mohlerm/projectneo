package ch.baws.projectneo;

import ch.baws.projectneo.effects.Tetris;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class TetrisActivity extends Activity implements OnClickListener{

	private class ScoreUpdater implements Runnable{

		@Override
		public void run() {
			txt_high.setText(Integer.toString(tetris.getHighestScore()));
			txt_score.setText(Integer.toString(tetris.getScore()));
			mHandler.removeCallbacks(updater);
			mHandler.postDelayed(this, 100);
		}
		
	}
	
	Tetris tetris;
	ProjectMORPHEUS application;
	Handler mHandler;
	ScoreUpdater updater;

	TextView txt_score,txt_high;
	Button left,right,drop,rotate;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.eff_tetris);
		application = (ProjectMORPHEUS) getApplication();
		
		left = (Button) findViewById(R.id.tetris_left);
		right = (Button) findViewById(R.id.tetris_right);
		drop = (Button) findViewById(R.id.tetris_drop);
		//rotate = (Button) findViewById(R.id.tetris_rotate);
		txt_high  = (TextView) findViewById(R.id.tetris_high);
		txt_score = (TextView) findViewById(R.id.tetris_score);
		
		mHandler = new Handler();
		updater = new ScoreUpdater();
		
		//rotate.setOnClickListener(this);
		left.setOnClickListener(this);
		right.setOnClickListener(this);
		drop.setOnClickListener(this);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		mHandler.removeCallbacks(updater);
		super.onPause();
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
	}

	@Override
	protected void onResume() {
		super.onResume();
		tetris = new Tetris();
		application.setEffect(tetris);	
		mHandler.postDelayed(updater, 100);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.tetris_right:
			tetris.shiftRight();
			break;
		case R.id.tetris_left:
			tetris.shiftLeft();
			break;
		case R.id.tetris_drop:
			tetris.dropDown();
			break;
//		case R.id.tetris_rotate:
//			tetris.rotate();
//			break;
		}
		
	}

}
