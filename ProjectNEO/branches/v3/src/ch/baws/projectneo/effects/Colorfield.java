package ch.baws.projectneo.effects;

import ch.baws.projectneo.R;
import ch.baws.projectneo.effects.Effect.DialogOptions;
import ch.baws.projectneo.frameGenerator.Frame;
import ch.baws.projectneo.minions.Utils;




public class Colorfield extends Effect{
	public Colorfield(){
		super("MarcelM", "Colorfield");
		array = Utils.fillArray(8,8, Frame.NEO_WHITE);
		//this.activity = ColorfieldActivity.class;
		icon = R.drawable.ic_eff_colorfield;
		this.hasOnClickOptions = true;
	}

	@Override
	public int[][] getArray() {
		return this.array;
	}
	
	public void setColor(int color)
	{	
		array = Utils.fillArray(8,8, color);
	}

	@Override
	public void run() {
		// do nothing
	}
	
	@Override
	public void setOnClickOption(int pos){
		array = Utils.fillArray(8,8, pos+1);
	}
	
	@Override
	public DialogOptions getOnClickDialogOptions(){
		return new DialogOptions("Choose color", Frame.colorOptions);
	}


}
