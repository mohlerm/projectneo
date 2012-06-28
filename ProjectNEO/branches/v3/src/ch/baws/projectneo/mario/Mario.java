package ch.baws.projectneo.mario;

public class Mario{
	private static final int xoff = 2;
	private static final int yoff = 1;
	
	double xspeed = 1;
	double yspeed = 5;
	double xacc = 0;
	double yacc = 0;
	
	double x;
	double y;
	
	public Mario(){
		this.x=xoff;
		this.y=yoff;
	}

}