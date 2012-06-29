package ch.baws.projectneo.minions;

import java.util.Random;

import android.util.Log;

public class Utils {
	
	private static final String TAG = "GENERALUTILS";
	private static final boolean D = false;
	private static final int[][] empty8x8 = {{0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0},{0,0,0,0,0,0,0,0}};
	
	public static String randomCharString()
	{
		Random r = new Random();
		char c = (char)(r.nextInt(26) + 'a');
		return String.valueOf(c);
		
	}
	public static int[][] getEmpty8x8(){
		int[][] array = new int[8][8];
		for(int i = 0;i<8;i++){
			System.arraycopy(empty8x8[i], 0, array[i], 0, 8);
		}
		return array;
	}
	/**
	 * static method randomArray
	 * creates an array with random entries
	 * @return
	 */
	public static int[][] randomArray(int m,int n)
	{
		Random rand = new Random();
		int[][] array = new int[m][n];
		for(int i=0;i<m;i++){
			for(int j=0;j<n;j++){
				array[i][j] = rand.nextInt(2);
			}
		}	
		return array;
	}
	
	/**
	 * static method emptyArray
	 * creates an array with all entries = zero
	 * @return
	 */
	public static int[][] emptyArray(int m, int n)
	{
		if(m==8&&n==8) return getEmpty8x8();
		
		int[][] array = new int[m][n]; // default Array, makes sure it contains zeros
    	for (int i=0;i<m;i++){
    		for (int j=0;j<n;j++){
    			array[i][j] = 0;
    		}
    	}
		return array;
	}
	/**
	 * method fillArray - fills the array with a constant value c
	 * @param m
	 * @param n
	 * @param c the colorcode
	 * @return
	 */
	public static int[][] fillArray(int m, int n, int c)
	{
		int[][] array = new int[m][n]; // default Array, makes sure it contains zeros
    	for (int i=0;i<m;i++){
    		for (int j=0;j<n;j++){
    			array[i][j] = c;
    		}
    	}
		return array;
	}
	
	public static void drawArray(int[][] array, int m, int n)
	{
		String s = "";
		for (int i=0;i<m;i++){
    		for (int j=0;j<n;j++){
    			s += (new Integer(array[i][j])).toString();
    	   		
    		}    		
    	}
		if (D) 	Log.e(TAG, s);	
	}
}
