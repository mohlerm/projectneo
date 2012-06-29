package ch.baws.projectneo.effects;

import java.util.Random;

import android.util.Log;
import ch.baws.projectneo.effects.Effect.DialogOptions;
import ch.baws.projectneo.frameGenerator.*;
import ch.baws.projectneo.minions.*;
import ch.baws.projectneo.R;

public class Matrix extends Effect{
	
	private static final String TAG = "MATRIX";
	private static final boolean D = true;
	
	private class Strip{
		public Strip(double position,double speed,int length){
			this.position = position;
			this.speed = speed;
			this.length = length;
		}
		public void step(){
			if(length>0)
				position += speed;
		}
		double position;
		double speed;
		int length;
	}
	private int COLOR = Frame.NEO_GREEN;
	Strip[] strips= new Strip[8];
	
	public Matrix(){
		super("ThomasR", "Matrix Screen");
		Random rand = new Random();
		for(int i=0;i<8;i++){
			strips[i] = new Strip(0,rand.nextDouble()+0.5,rand.nextInt(5)+2);
		}
		this.icon = R.drawable.ic_eff_matrix2;
		this.hasOnLongClickOptions = true;
	}
	
	@Override
	public DialogOptions getOnLongClickDialogOptions(){
		return new DialogOptions("Choose color", Frame.colorOptions);
	}
	
	@Override
	public void setOnLongClickOption(int pos){
		this.COLOR = pos+1;
	}
	
	@Override
	public int[][] getArray() {
		return array;
	}
	
	@Override
	public void run() {
		Random rand = new Random();
		int[][] tmparray;
		while(!EXIT){
			
			for(int i=0;i<8;i++){
				strips[i].step();
				if((strips[i].position-strips[i].length)>8){
					strips[i] = new Strip(0,rand.nextDouble()+0.7,rand.nextInt(5)+2);
				}
			}
			
			tmparray = Utils.getEmpty8x8();
			for(int i=0;i<8;i++){
				for(int j=0;(strips[i].position)>j;j++){
					if(((strips[i].position-strips[i].length)<j)&&(j<8)){
						tmparray[j][i] = COLOR;
					}
				}
			}
			array = tmparray;
			if(D) Log.d(TAG, "next run...");
			try {
				sleep(100);
			} catch (InterruptedException e) {}
		}
		
	}

}
