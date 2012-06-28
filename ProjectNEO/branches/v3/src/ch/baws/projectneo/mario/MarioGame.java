package ch.baws.projectneo.mario;

public class MarioGame extends Thread {

	private boolean EXIT = false;
	
	private CMarioMap map;
	private Mario mario;
	
	private static class Control{
		public static boolean Left;
		public static boolean Right;
		public static boolean Jump;
		public static void reset(){
			Left = false;
			Right = false;
			Jump = false;
		}
	}
	
	
	public MarioGame(Chunk c){
		map = new CMarioMap(c);
		mario = new Mario();
	}
	
	public void stopMario(){
		EXIT = true;
	}
	
	public int[][] getNEOArray(){
		return map.toNEOArray(((int) mario.x)-2,(int) mario.x,(int) mario.y);
	}
	
	public void moveLeft(){
		Control.Right = false;
		Control.Left = true;
	}
	
	public void moveRight(){
		Control.Left = false;
		Control.Right = true;
	}
	
	public void moveJump(){
		Control.Jump = true;
	}
	
	public void run(){
		Control.reset();
		while(!EXIT){
			
			//control bounds
			
			//apply Physics/move mario
			
			//move right and left, without acc
			
			if(Control.Left){
				mario.x--;
			}else if(Control.Right){
				mario.x++;
			}
			
			Control.reset();
			try {
				sleep(50); // 1 game tick
			} catch (InterruptedException e) {
			}
		}
		
		
		//some Game Over effect, display score?
		
	}
}
