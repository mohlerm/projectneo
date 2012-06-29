package ch.baws.projectneo.minions;

/**
 * Throw this implementation if you have a function defined, but
 * not fully implemented
 * @author Thomas Richner
 *
 */
public class NotImplementedException extends RuntimeException {
	private static final long serialVersionUID = 7386661111693727830L;

	public NotImplementedException(){
		super("This function hasn't been implemented yet");
	}
}
