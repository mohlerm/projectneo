package ch.baws.projectneo.mario;

public class XY {
	public int x;
	public int y;
	public XY(int x,int y){
		this.x = x;
		this.y = y;
	}
	public boolean isInBounds(){
		return (x>=0) && (y<8) && (y>=0);
	}
}
