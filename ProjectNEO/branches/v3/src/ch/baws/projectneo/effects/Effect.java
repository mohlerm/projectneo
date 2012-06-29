package ch.baws.projectneo.effects;

import ch.baws.projectneo.minions.*;
import ch.baws.projectneo.R;

/**
 * This is the generic Framework for an ProjectNEO Effect.
 * 
 * some things you have to implement if you want a working effect:
 * <ul>
 * <li>implementing the Constructor<br>
 *   Only implement a Standard Constructor, without any arguments.
 *   Assure that you call super("Author of this effect","Name of Effect");
 *   in your Constructor or you won't get credit for your Effect ;)</li>
 * <li>implementing the getArray() method<br>
 *   Get Array is called every time the Matrix needs a new Frame. Represented
 *   by a 8x8 integer array ( int[][] array = new int[8][8];), whereas array[0][0] is the upper left corner. It is important
 *   that this method is as light as possible. Calculate and fill your array in the run method
 *   whenever there's an update/change for it. Valid values are the constants in defined in 'Frame' (e.g. Frame.NEO_WHITE)</li>
 * <li>implementing the run() method<br>
 *   You can use this to make something time driven, the usual pattern looks like this:
 *    <PRE>
 *    public void run(){
 *    	//init stuff
 *      while(!EXIT){
 *      	//alter the array
 *      	try{
 *      	   sleep(100); //sleep 100ms, so this loop gets executed every 100ms
 *      	} catch (InterruptedException e) {}
 *      }
 *      //end of effect
 *    }</PRE>
 *   You have to assure, that your Effect terminates as soon as EXIT is set true. Or else your
 *   Effect might become a Zombie-Thread. If you don't need anything time driven, just leave run empty.
 *   It's OK if your thread terminates before a new Effect is set.</li>
 *   </ul>
 * Optional Stuff: this is only interesting if your Effect desires some user input
 * <ul>
 * <li>OnClickOptions<br>
 *   If your Effect needs some primitive user input you can use the OnClickOption like this:
 *   <ol>
 *   <li>in your constructor:<br>
 *   	this.hasOnClickOptions = true;</li>
 *   <li>override&implement:<br>
 *   	   public DialogOptions getOnClickDialogOptions(){ return new DialogOptions("title",...);}
 *      whereas ... stands for some String array containing your options (e.g. String[] myOptions = {"red","green"};</li>
 *   <li>override&implement:<br>
 *   	   public void setOnClickOption(int pos){ ... }
 *   	whereas 'pos' is the index of the selected string from the user</li>
 *   </ol></li>
 * <li>OnLongClickOptions<br>
 *   the same as OnClickOptions, but gets called if a user has 'long' clicked on your Effect.
 *   You can implement this analog to OnClickOptions.</li>
 * <li>Activity<br>
 *   Your Effect can even have his own Activity! This allows more user interaction.
 *   An own Activity is very advanced and not very generic anymore. If you really need one, you
 *   have to make a class that extends Activity and set 'this.activity = YourActivity.class;'.
 *   Furthermore you have to make a layout corresponding to your Activity and register your 
 *   Activity in the 'AndroidManifest.xml'.</li>
 *   </ul>
 *   
 *   Hints:
 *   <ul>
 *   <li>getArray() must ALWAYS return a valid 8x8 Array, no matter if your Effect was just instantiated, running or terminated</li>
 *   <li>don't calculate more arrays than needed, only calculate if it changed (light getArray() Method)</li>
 *   <li>use the OnLongClick feature for 'hidden' Options</li>
 *   <li>be aware: if your getArray() Method locks up, the whole Matrix locks up</li>
 *   <li>StarSky is a good example Effect for very simple implementations</li>
 *   <li>Colorfield Effect is a good example for the OnClickOptions user input</li>
 *   <li>BinaryClock Effect is a good example for the OnLongClickOptions user input</li>
 *   <li>Tetris 'Effect' shows an Effect with an Activity</li>
 *   </ul>
 * @author (c) by Thomas Richner
 *
 */
public abstract class Effect extends Thread{
	protected static final String TAG = "GENERIC_EFFECT";
	protected static final boolean D = false;
	
	public class DialogOptions{
		public final String title;
		public final String[] options;
		public DialogOptions(String title,String[] options){
			this.title = title;
			this.options = options;
		}
	}
	/** useful to return with getArray() and compute & fill somewhere else */
	protected volatile int[][] array;
	/** indicates if your Effect should terminate */
	protected volatile boolean EXIT = false;
	/** set this true if your Effect has it's own activity*/
	protected Class activity = null;
	/** set this to your own R.drawable id if you have an icon for your Effect */
	protected int icon = R.drawable.ic_app;
	
	/** set this true if you have setOnClickOption and getOnClickDialogOptions methods overridden*/
	protected boolean hasOnClickOptions = false;
	/** set this true if you have setOnLongClickOption and getOnLongClickDialogOptions methods overridden*/
	protected boolean hasOnLongClickOptions = false;
	
	/** the Effect Authors name */
	public final String AUTHOR;
	/** the Effects title, name or short description */
	public final String TITLE;
	
	/**
	 * Get's never called directly, only
	 * in the constructor of an Effect implementation.
	 * @param author author of the calling Effect
	 * @param title title / short description of the calling Effect (e.g. 'My awesome Effect')
	 */
	public Effect(String author,String title){
		this.array = Utils.emptyArray(8, 8);
		this.AUTHOR = author;
		this.TITLE = title;
	}
	
	public Effect(){
		this.array = Utils.emptyArray(8, 8);
		AUTHOR = "Brownie";
		TITLE = "GENERIC";	
	}
	
	/**
	 * gets called by the BluetoothUtils whenever
	 * it wants to send a new Frame to the Matrix.
	 * This Method MUST always return a valid x8 array.
	 * 
	 * @return an 8x8 int Array, containing values from 0 to 7.
	 * 		    These values represent the possible colors, see Frame.NEO_... for
	 * 			the each corresponding color. 
	 */	
	public abstract int[][] getArray();
	
	/**
	 * stops the Effect
	 * Note: implementation is up to the programmer
	 * 
	 */
	public void exit(){
		EXIT = true;
		this.interrupt();
	}
	
	/**
	 * checks if this Effect has his own Activity
	 * @return true if this Effect has his own Activity
	 */
	public boolean hasAnActivity(){
		return activity!=null;
	}
	
	public Class getActivity(){
		return activity;
	}
	
	public boolean hasOnClickOptions(){
		return hasOnClickOptions;
	}
	
	public boolean hasOnLongClickOptions(){
		return hasOnLongClickOptions;
	}
	
	/*
	 * Override this Method if you need some user input
	 * when your Effect is selected
	 */
	public DialogOptions getOnClickDialogOptions(){
		return null;
	}
	
	/*
	 * Override this Method if you need some user input
	 * when your Effect get's 'long' clicked
	 */
	public DialogOptions getOnLongClickDialogOptions(){
		return null;
	}
	
	/*
	 * Override this Method if you need some user input
	 * when your Effect is selected
	 */
	public void setOnClickOption(int pos){
		//implement in subclass if needed
	}
	
	/*
	 * Override this Method if you need some user input
	 * when your Effect get's 'long' clicked
	 */
	public void setOnLongClickOption(int pos) {
		//implement in subclass if needed
	}	
	
	/**
	 * 
	 * @return R.drawable id for this Effects icon
	 */
	public int getIcon(){
		return this.icon;
	}
		
	public abstract void run();


}
