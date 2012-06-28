package ch.baws.projectneo.mario;

public class MarioMap {
	public static final int EMPTY  =0;
	public static final int GROUND =1;
	public static final int BRICKS =2;
	public static final int CLOUDS =3;
	
	private long[] ground = new long[8];
	private long[] bricks = new long[8];
	private long[] clouds = new long[8];
	
	public MarioMap(long[] ground,long[] bricks, long[] clouds){
		this.ground = ground;
		this.bricks = bricks;
		this.clouds = clouds;
	}
	
	public int getOccupation(XY pos){
		if((ground[pos.y]&BitUtils.BIT[pos.x])!=0){
			return GROUND;
		}else if((bricks[pos.y]&BitUtils.BIT[pos.x])!=0){
			return BRICKS;
		}else if((clouds[pos.y]&BitUtils.BIT[pos.x])!=0){
			return CLOUDS;
		}
		return EMPTY;
	}
	
	public boolean isSolid(XY pos){
		return (bricks[pos.y]==BitUtils.BIT[pos.x]) || (ground[pos.y]==BitUtils.BIT[pos.x]);
	}
	
	public boolean isOnGroud(XY pos){
		if((pos.x<64) && (pos.x>=0) && (pos.y<8) && (pos.y>=1)) return ground[pos.y-1]==BitUtils.BIT[pos.x];
		return false;
	}
	
	public boolean isBelowBrick(XY pos){
		if((pos.x<64) && (pos.x>=0) && (pos.y<=6) && (pos.y>=0)) return bricks[pos.y+1]==BitUtils.BIT[pos.x];
		return false;
	}
	
	public int[][] toNEOArray(int x){
		long GND = 0;
		long CLD = 0;
		long BRK = 0;
		for(int i=7;i>=0;i--){ // converting to a 8x8 Bitfield
			GND <<= 8;
			GND |= ((ground[i] >>> x) & 0xFF);
			BRK <<= 8;
			BRK |= ((bricks[i] >>> x) & 0xFF);
			CLD <<= 8;
			CLD |= ((clouds[i] >>> x) & 0xFF);
		}	
		
		return Bitfields.toNEOArr(GND, BRK, 1, 2); //TODO use all bitboards
	}
	
	public String toString(){
		StringBuffer str = new StringBuffer();
		
		for(int y=7;y>=0;y--){
			for(int i=0;i<64;i++){
				int occ = getOccupation(new XY(i,y));
				switch(occ){
				case GROUND:
					str.append('#');
					break;
				case BRICKS:
					str.append('@');
					break;
				case CLOUDS:
					str.append("C");
					break;
				case EMPTY:
					str.append("O");
					break;
				}
				str.append(' ');
			}
			str.append('\n');
		}
		
		return str.toString();
	}
	
}
