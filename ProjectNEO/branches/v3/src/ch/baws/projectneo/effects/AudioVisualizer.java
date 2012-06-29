package ch.baws.projectneo.effects;

import android.media.audiofx.Visualizer;
import android.util.Log;

import ch.baws.projectneo.minions.*;
import ch.baws.projectneo.R;
import ch.baws.projectneo.frameGenerator.Frame;

public class AudioVisualizer extends Effect {
	
	private static final String TAG = "AudioVisualizer";
	private static final boolean D = false;	
	
	private static final double maxFrequency = 6000*1000;	//in mHz
	private static final double minFrequency = 50*1000;	
	
	private static final double LOGARITHMIC_BASE = 1.5;
	
	//----magic values
	private static final double AVERAGE_MIN = 10;
	private static final double AVERAGE_MAX = 30;
	
	private static final int MAXIMUM_OFFSET = 3;
	private static final int MINIMUM_OFFSET = 0;
	
	private static final double WEIGHT_INV = 20;
	private static final double WEIGHT_TOT = WEIGHT_INV+1;
	
	private static final int CAPTURE_SIZE = 2048;
	
	//----
	private static final int[] empty8 = {0,0,0,0,0,0,0,0};
	
	private Visualizer visualizer;
	private int kmax,kmin;
	
	double step = 1/(Math.pow(LOGARITHMIC_BASE, 0)+Math.pow(LOGARITHMIC_BASE, 1)+Math.pow(LOGARITHMIC_BASE, 2)+Math.pow(LOGARITHMIC_BASE, 3)+Math.pow(LOGARITHMIC_BASE, 4)+Math.pow(LOGARITHMIC_BASE, 5)+Math.pow(LOGARITHMIC_BASE, 6)+Math.pow(LOGARITHMIC_BASE, 7));
	
	public AudioVisualizer(){
		super("Thomas Richner", "Audio Visualizer");
		this.icon = R.drawable.ic_eff_audiovisualizer;
		
		if(D) Log.d(TAG,"New Visualizer!");
		
		array = Utils.getEmpty8x8();
	}
	
	@Override
	public int[][] getArray() {
		this.interrupt();
		return array;
	}

	@Override
	public void run() {
		//---- init
		visualizer = new Visualizer(0);  // use audio output mix
		
		if(D) Log.d(TAG,"Max capture size: " + Integer.toString(Visualizer.getMaxCaptureRate()));
		try{
			visualizer.setCaptureSize(CAPTURE_SIZE);
		}catch(IllegalStateException e){
			Log.e(TAG,"setCaptureSize: Illegal state!");
		}
		kmax = (int) (maxFrequency/((double) visualizer.getSamplingRate())*visualizer.getCaptureSize()*2);
		kmin = (int) (minFrequency/((double) visualizer.getSamplingRate())*visualizer.getCaptureSize()*2);
		
		if(D){
			Log.v(TAG, "kmax: "+kmax);
			Log.v(TAG, "kmin: "+kmin);
			Log.v(TAG, "FS: " + visualizer.getSamplingRate());
			Log.v(TAG,"step:"+step);
		}
		
		int err,MAX,MIN,tMAX,tMIN,real,imag;
		double averageMAX=AVERAGE_MAX, averageMIN=AVERAGE_MIN;
		
		byte[] buffer_fft = new byte[visualizer.getCaptureSize()];
		int[] magnitude = new int[8];
		
		int tband[] = { (int) (step*(kmax-kmin)),
						(int) (step*Math.pow(LOGARITHMIC_BASE, 1)*(kmax-kmin)),
						(int) (step*Math.pow(LOGARITHMIC_BASE, 2)*(kmax-kmin)),
						(int) (step*Math.pow(LOGARITHMIC_BASE, 3)*(kmax-kmin)),
						(int) (step*Math.pow(LOGARITHMIC_BASE, 4)*(kmax-kmin)),
						(int) (step*Math.pow(LOGARITHMIC_BASE, 5)*(kmax-kmin)),
						(int) (step*Math.pow(LOGARITHMIC_BASE, 6)*(kmax-kmin)),
						(int) (step*Math.pow(LOGARITHMIC_BASE, 7)*(kmax-kmin))};
		
		//---- run 
		visualizer.setEnabled(true);
		while(!EXIT){
			err = visualizer.getFft(buffer_fft);
			tMAX=0; tMIN=Integer.MAX_VALUE;
			System.arraycopy(empty8, 0, magnitude, 0, 8);
			if(err!=Visualizer.SUCCESS){
				Log.e(TAG,"FFT wen't wrong, errorcode: " + err);
			}else{
				if(D) Log.d(TAG,"FFT success");
				int offset=0;
				for(int j=0;j<8;j++){	// calculate the magnitude of each bar as a sum of frequencies
					double mag=0;
					for(int i=0; i<tband[j];i+=2){					// add up a whole band
						real = buffer_fft[kmin+offset];		// real part of a frequency
						imag = buffer_fft[kmin+offset+1]; 	// imaginary part of a frequency
						mag += Math.sqrt(real*real+imag*imag);		// only use the absolute value, phase is not interesting
						offset++;
					}

					if(mag!=0)
						magnitude[j] = (int) (10 * Math.log10(mag)); 	// use dB value
					
					tMAX = Math.max(magnitude[j],tMAX);				// get the maximum and minimum of the magnitudes
					tMIN = Math.min(magnitude[j],tMIN);				
					
					if(D) Log.v(TAG, "MAX"+j+": "+tMAX);
					if(D) Log.v(TAG, "MIN"+j+": "+tMIN);
				}
				
				//get an average
				if(tMAX-tMIN>5 && tMAX>10 && tMIN>5 && tMAX<50){ 	// magic values...
					averageMIN = (((WEIGHT_INV*averageMIN+tMIN))/WEIGHT_TOT);		// weight it
					averageMAX = (((WEIGHT_INV*averageMAX+tMAX))/WEIGHT_TOT);
				}
				
				MAX = (int) (averageMAX+MAXIMUM_OFFSET); // floor(avg+3), magic value
				MIN = (int) (averageMIN-MINIMUM_OFFSET);	// floor(avg)
				
				if(D){
					Log.d(TAG, "magnitude="+magnitude[0]+ ","+magnitude[1]+ ","+magnitude[2]+ ","+magnitude[3]+ ","+magnitude[4]+ ","+magnitude[5]+ ","+magnitude[6]+ ","+magnitude[7]);	
					Log.d(TAG, "MIN/MAX: "+MIN+" / "+MAX + "    tMIN/tMAX: "+tMIN+" / "+tMAX +"    kmin/kmax:" + kmin +" / "+ kmax);
				}

				array = Utils.getEmpty8x8();
				for(int i=0;i<8;i++){
					int tempMag = magnitude[i]; 
					if(tempMag>0.125*(MAX-MIN)+MIN){
						array[7][i] = Frame.NEO_GREEN;
					}
					if(tempMag>0.25*(MAX-MIN)+MIN){
						array[6][i] = Frame.NEO_GREEN;
					}
					if(tempMag>0.375*(MAX-MIN)+MIN){
						array[5][i] = Frame.NEO_GREEN;
					}
					if(tempMag>0.5*(MAX-MIN)+MIN){
						array[4][i] = Frame.NEO_YELLOW;
					}
					if(tempMag>0.625*(MAX-MIN)+MIN){
						array[3][i] = Frame.NEO_YELLOW;
					}
					if(tempMag>0.75*(MAX-MIN)+MIN){
						array[2][i] = Frame.NEO_RED;
					}
					if(tempMag>0.875*(MAX-MIN)+MIN){
						array[1][i] = Frame.NEO_RED;
					}
					if(tempMag>=MAX){
						array[0][i] = Frame.NEO_WHITE;
					}	
				}
				
			}
			
			try {
				sleep(1000);
			} catch (InterruptedException e) {}
		}
		visualizer.release();
	}

}
