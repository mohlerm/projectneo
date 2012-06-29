package ch.baws.projectneo.effects;

import android.util.Log;
import ch.baws.projectneo.PaintActivity;
import ch.baws.projectneo.R;
import ch.baws.projectneo.frameGenerator.Frame;
import ch.baws.projectneo.minions.Utils;

public class PaintEffect extends Effect {
	private static final String TAG = "PaintEffect";
	private int color = Frame.NEO_RED;
	
	public PaintEffect(){
		super("Thomas Richner","Paint!");
		this.array = Utils.getEmpty8x8();
		this.activity = PaintActivity.class;
		this.icon = R.drawable.ic_eff_paint;
	}
	
	public void setColor(int color){
		this.color = color;
	}
	
	public void setField(int x, int y){
		Log.d(TAG,"Set field: " + x + " / " + y);
		//set bounds 0-7
		x = Math.max(Math.min(x,7), 0);
		y = Math.max(Math.min(y,7), 0);
		array[y][x] = color;
	}
	
	@Override
	public int[][] getArray() {
		return array;
	}

	@Override
	public void run() {
		//do nothing...
	}

}
