package ch.baws.projectneo.effects;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import android.util.Log;

import ch.baws.projectneo.R;
import ch.baws.projectneo.TetrisActivity;
import ch.baws.projectneo.frameGenerator.Bitfields;
import ch.baws.projectneo.frameGenerator.Frame;
import ch.baws.projectneo.minions.NotImplementedException;

public class Tetris extends Effect{

	protected static final String TAG = "TETRIS";
	protected static final boolean D = true;
	
	private long stack;	
	private long ubrick;
	private long lbrick;
	
	private long curbrick;
	int brick_x,brick_y;
	
		
	Random rand = new Random();
	private int score;
	private List<Integer> highscore = new ArrayList<Integer>();
	
	public Tetris(){
		super("Thomas Richner","Tetris");
		this.icon = R.drawable.ic_eff_tetris;
		this.activity = TetrisActivity.class;
		
		//start a new game
		stack = 0;
		newBrick();
	}
	
	@Override
	public int[][] getArray() {
		return Bitfields.toNEOArr(lbrick, stack, Frame.NEO_BLUE, Frame.NEO_WHITE);
	}

	@Override
	public void run() {
		while(!EXIT){
			
			shiftDown();
			
//			Log.d("Tetris", Bitfields.toString(ubrick));
//			Log.d("Tetris", Bitfields.toString(lbrick | stack));
			
			try {
				sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}
	/**
	 * 
	 * @return true if it was able to shift down, false if it was GameOver or at the top
	 * of the stack
	 */
	private boolean shiftDown(){
		long carry;
		
		carry = ubrick & 0xFF;
		ubrick >>>= 8; // shift down
		lbrick >>>= 8;
		lbrick |= (carry << 56);
		brick_y--;
		
		if((((lbrick >>> 8)&stack )!=0) || ((lbrick&0xFF)!=0)){ //brick on the stack or at the bottom
			if(ubrick!=0){		//part of a brick still at the top? Game Over!
				gameOver();
				return false;
			}
			
			newBrick();			//just make a new brick, set the old
			
			for(int i=0;i<8;i++){	//is a line full?
				if((stack&(0xFF<<i*8))==(0xFF<<i*8)){ 
					stack = (stack & BRICKS.LINEMASK[i]) | ((stack >>> 8) & (~BRICKS.LINEMASK[i]));
					score++;
				}
			}
			return false;
		}
		return true;
	}
	

	/**
	 * this drops a stone to the top of the stack
	 */
	public void dropDown(){
		boolean shift = shiftDown();
		while(shift){
			shift = shiftDown();
		}
	}
	
	public void rotate(){
		
		if(D) throw new NotImplementedException();		
		
		if(D) Log.d(TAG,"try to rotate");
		
		if(ubrick!=0 || brick_y>10) return;  //noch im oberen Bereich?nicht drehbarer Brick?
		
		if(D) Log.d(TAG,"ok, legal brick");
		
		long tmp = lbrick;
		int count = Bitfields.numberOfSet(tmp); //kontrollsumme
		
		//Brick in die Mitte schieben
		int i;
		for(i=brick_y;i>0;i--){
			tmp = Bitfields.shiftS(tmp);
		}
		for(i=brick_y;i<0;i++){
			tmp = Bitfields.shiftN(tmp);
		}
		for(i=brick_x;i>0;i--){
			tmp = Bitfields.shiftO(tmp);
		}
		for(i=brick_x;i<0;i++){
			tmp = Bitfields.shiftW(tmp);
		}
		
		tmp = Bitfields.rotate90(tmp); //rotieren um Mitte
		
		//Zur�ckschieben
		for(i=0;i>brick_y;i--){
			tmp = Bitfields.shiftN(tmp);
		}
		for(i=0;i<brick_y;i++){
			tmp = Bitfields.shiftS(tmp);
		}
		for(i=0;i>brick_x;i--){
			tmp = Bitfields.shiftW(tmp);
		}
		for(i=0;i<brick_x;i++){
			tmp = Bitfields.shiftO(tmp);
		}
		
		if(count!=Bitfields.numberOfSet(tmp)) return; //ausserhalb des Felds?
		if(D) Log.d(TAG,"ok, finally rotate");
		
		lbrick = tmp;
	}
	
	
	public boolean shiftLeft(){
		if((((lbrick >>> 1)&(~Bitfields.BORDER_R)))!=(lbrick >>> 1) || (((lbrick >>> 1)&(stack)))!=0) return false;	//already at the left edge
		brick_x--;
		ubrick >>>= 1;
		lbrick >>>= 1;
		return true;
	}
	
	public boolean shiftRight(){
		if((((lbrick << 1)&(~Bitfields.BORDER_L)))!=(lbrick << 1) || (((lbrick << 1)&(stack)))!=0) return false;	//already at the left edge
		brick_x++;
		ubrick <<= 1;
		lbrick <<= 1;
		return true;
	}
	
	private void newBrick(){
		
		stack |= lbrick;
		ubrick = BRICKS.BRICK[rand.nextInt(BRICKS.BRICK.length)];
		lbrick =0;
		
		//TODO
		curbrick = ubrick;
		if(ubrick==BRICKS.SLAB2 || ubrick==BRICKS.SLAB3 || ubrick==BRICKS.SLAB4){		
			brick_y = 5;
		}else if(ubrick==BRICKS.JBRICK){		
			brick_y = 6;
		}else if(ubrick==BRICKS.LBRICK){		
			brick_y = 6;
		}else{
			brick_y = 20;
		}
		
		brick_x = 0;
	}
	
	private void gameOver(){
		highscore.add(new Integer(score));
		//TODO fancy "Game Over!" Effect
		
		stack = 0;
		lbrick = 0;
		newBrick();
	}
	
	public int getHighestScore(){
		Collections.sort(highscore);
		int ret = 0;
		if(highscore.size()>0)
			ret = highscore.get(highscore.size()-1);
		return ret;
	}
	
	public int getScore(){
		return score;
	}
	
	
	/**
	 * Utils to handle 8x8 BitTetris
	 * <font face="Courier New"><pre>
	 *    A  B  C  D  E  F  G  H  
	 * 1| 56 57 58 59 60 61 62 63 
	 * 2| 48 49 50 51 52 53 54 55  
	 * 3| 40 41 42 43 44 45 46 47  
	 * 4| 32 33 34 35 36 37 38 39  
	 * 5| 24 25 26 27 28 29 30 31  
	 * 6| 16 17 18 19 20 21 22 23  
	 * 7| 08 09 10 11 12 13 14 15  
	 * 8| 00 01 02 03 04 05 06 07  
	 * </pre> </font> 
	 */
	private static class BRICKS{
		public static final long BLOCK1 = Bitfields.E8;
		public static final long BLOCK2 = Bitfields.D8 | Bitfields.E8 | Bitfields.D7 | Bitfields.E7;
		public static final long SLAB2 = Bitfields.D8 | Bitfields.E8;
		public static final long SLAB3 = Bitfields.D8 | Bitfields.E8 | Bitfields.F8;
		public static final long SLAB4 = Bitfields.C8 | Bitfields.D8 | Bitfields.E8 | Bitfields.F8;
		public static final long JBRICK = Bitfields.D8 | Bitfields.E8 | Bitfields.E7 | Bitfields.E6;
		public static final long LBRICK = Bitfields.D8 | Bitfields.E8 | Bitfields.D7 | Bitfields.D6;
		public static final long[] BRICK = {BLOCK1,BLOCK2,SLAB2,SLAB3,SLAB4,JBRICK,LBRICK};
		public static final long[] LINEMASK = {0x00,0xFFL,0xFFFFL,0xFFFFFFL,0xFFFFFFFFL,0xFFFFFFFFFFL,0xFFFFFFFFFFFFL,0xFFFFFFFFFFFFFFL,0xFFFFFFFFFFFFFFL};
	}

}
