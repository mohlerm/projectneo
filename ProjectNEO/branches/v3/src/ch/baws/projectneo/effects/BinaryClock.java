package ch.baws.projectneo.effects;

import java.util.Calendar;
import java.util.Date;
import android.util.Log;

import ch.baws.projectneo.frameGenerator.*;
import ch.baws.projectneo.minions.*;
import ch.baws.projectneo.R;
//doesn't work^^
public class BinaryClock extends Effect{
	private int color = Frame.NEO_WHITE;
	private static final String TAG = "Binary Clock";
	private static final boolean D = false;	
	public BinaryClock(){
		super("Thomas Richner", "Binary Clock");
		this.icon = R.drawable.ic_eff_binaryclock;
		if(D) Log.d(TAG,"New Binary Clock!");
		this.hasOnLongClickOptions = true;
		array = Utils.getEmpty8x8();
	}
	
	public void setColor(int color){
		this.color = color;
	}
	
	@Override
	public DialogOptions getOnLongClickDialogOptions(){
		return new DialogOptions("Choose color", Frame.colorOptions);
	}
	
	@Override
	public void setOnLongClickOption(int pos){
		this.color = pos+1;
	}
	
	@Override
	public int[][] getArray() {
		this.interrupt();       
		return array;
	}

	@Override
	public void run() {

		while(!EXIT){
			array = Utils.getEmpty8x8();
			Date date = Calendar.getInstance().getTime();
			int hours = date.getHours();
			int minutes = date.getMinutes();
			int seconds = date.getSeconds();
			if(D) Log.d(TAG,"Hours: " + hours +"  Minutes: "+minutes);
			
			if(((hours/10)&0x01)==0x01){
				array[7][1] = color;
			}
			if(((hours/10)&0x02)==0x02){
				array[6][1] = color;
			}
			hours = hours%10;
			if((hours&0x01)==0x01){
				array[7][2] = color;
			}
			if((hours&0x02)==0x02){
				array[6][2] = color;
			}
			if((hours&0x04)==0x04){
				array[5][2] = color;
			}
			if((hours&0x08)==0x08){
				array[4][2] = color;
			}
			
			// Display 1 cipher of minutes
			if(((minutes/10)&0x01)==0x01){
				array[7][5] = color;
			}
			if(((minutes/10)&0x02)==0x02){
				array[6][5] = color;
			}
			if(((minutes/10)&0x04)==0x04){
				array[5][5] = color;
			}
			
			// Display 2 cipher of minutes
			minutes %= 10;
			if(((minutes)&0x01)==0x01){
				array[7][6] = color;
			}
			if(((minutes)&0x02)==0x02){
				array[6][6] = color;
			}
			if(((minutes)&0x04)==0x04){
				array[5][6] = color;
			}
			if(((minutes)&0x08)==0x08){
				array[4][6] = color;
			}
			
			if(seconds%2==0){
				array[7][7] = Frame.NEO_GREEN;
			}
			
			try {
				sleep(1000);
			} catch (InterruptedException e) {	}
			
			
		}
		
	}

	
}
