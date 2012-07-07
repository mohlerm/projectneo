package ch.baws.projectneo.effects;

public class DefaultEffect extends Effect{
	Effect effect = new StarSky();
	public DefaultEffect(){
		super("Chuck","Default Effect");
	}
	@Override
	public int[][] getArray() {
		return effect.getArray();
	}

	@Override
	public void run() {
		effect.run();
		while(!EXIT){
			try {
				sleep(999999);
			} catch (InterruptedException e) {
			}
		}
		effect.exit();
	}

}
