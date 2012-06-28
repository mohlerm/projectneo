package ch.baws.projectneo.mario;
/**
 * Represents one Chunk of a CMarioMap
 * @author thomas
 *
 */
public class Chunk{
	long[] ground = new long[8];
	long[] bricks = new long[8];
	long[] clouds = new long[8];
	Chunk next=null;
	/**
	 * 
	 * @param ground the BitMap for the ground pieces
	 * @param bricks the BitMap for the brick pieces (the ones that you can destroy)
	 * @param clouds the BitMap for the clouds (just fancy)
	 * @param next
	 */
	public Chunk(long[] ground, long[] bricks, long[] clouds,Chunk next){
		this.ground = ground;
		this.bricks = bricks;
		this.clouds = clouds;
		this.next = next;
	}
}