package ch.baws.projectneo.minions;
import ch.baws.projectneo.effects.Effect;
import ch.baws.projectneo.frameGenerator.Bitfields;

public class FancyCurtain extends Effect{
	private int[][] array;
	private int color;
	private int speed;
	private long bf;
	
	public FancyCurtain(int color,int duration){
		array = Utils.getEmpty8x8();
		speed = duration/9;
		bf=0;
		throw new NotImplementedException(); //TODO
	}
	@Override
	public void run(){
		for(int i=0;i<9 && !EXIT;i++){
			array = Bitfields.toNEOArr(bf, 0, color, 0);
			try {
				sleep(speed);
			} catch (InterruptedException e) {}
			bf = (bf >>> 8) | Bitfields.BORDER_U; //we could also exploit the way signed shift works...
		}
	}
	@Override
	public int[][] getArray() {
		return array;
	}
	
	public long getBitMap(){
		return bf;
	}
}	