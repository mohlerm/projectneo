package ch.baws.projectneo.effects;

import ch.baws.projectneo.frameGenerator.Frame;
import ch.baws.projectneo.minions.Utils;

public class PaintEffect extends Effect {

	private int color = Frame.NEO_RED;
	
	public PaintEffect(){
		super("Thomas Richner","Paint!");
		this.array = Utils.getEmpty8x8();
	}
	
	public void setColor(int color){
		this.color = color;
	}
	
	public void setField(int x, int y){
		array[x][y] = color;
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
