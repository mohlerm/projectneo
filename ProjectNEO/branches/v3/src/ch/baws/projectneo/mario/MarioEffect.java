package ch.baws.projectneo.mario;

import ch.baws.projectneo.effects.Effect;
import ch.baws.projectneo.minions.Utils;

public class MarioEffect extends Effect{

	
	private MarioGame game;
	
	public MarioEffect(){
		super("Thomas Richner","YAMario Alpha");
		array = Utils.getEmpty8x8();
		game = new MarioGame(StaticMaps.level1_1);
	}
	
	@Override
	public int[][] getArray() {
		int[][] tmp = array;
		this.interrupt();
		return tmp;
	}
	
	// Control, check if game Over!!!
	public void moveLeft(){
		game.moveLeft();
	}
	
	public void moveRight(){
		game.moveRight();
	}
	
	public void moveJump(){
		game.moveJump();
	}
	

	@Override
	public void run() {
		
		
		while(!EXIT){
			
			array = game.getNEOArray();
			
			try {
				sleep(1000);
			} catch (InterruptedException e) {

			}
		}
	}

}
