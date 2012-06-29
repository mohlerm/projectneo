package ch.baws.projectneo.minions;

public interface PaintbrushMovedListener {
		/**
		 * Gets called if the joystick moves, the radius of the joystick will
		 * always be between 0 and 100
		 * @param pan the x offset from the joystick center, from -100 to 100
		 * @param tilt the y offset from the joystick center, from -100 to 100
		 */
	  public void OnMoved(int pan, int tilt);
	  public void OnReleased();
	  
}