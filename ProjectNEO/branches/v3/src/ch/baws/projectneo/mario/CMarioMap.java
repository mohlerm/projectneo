package ch.baws.projectneo.mario;

import ch.baws.projectneo.frameGenerator.Frame;

/**
 * Represents a Mario Map as a Bitfield.
 * Where as the length of the Map is not limited
 * @author Thomas Richner
 * @version 0.2
 *
 */
public class CMarioMap {
	public static final int YSIZE= 64;
	public static final int EMPTY  =0;
	public static final int GROUND =1;
	public static final int BRICKS =2;
	public static final int CLOUDS =3;
	public static final int OUTOFBOUNDS = -1;
	
	private Chunk chunks;
	
	public CMarioMap(Chunk staticmap){
		this.chunks = staticmap;
	}
	
	private int checkChunkSize(){
		int size=0;
		Chunk tmp = chunks;
		while(tmp!=null){
			size++;
			tmp = tmp.next;
		}
		return size;
	}
	
	public int getOccupation(XY pos){
		if(!pos.isInBounds()) return OUTOFBOUNDS;
		int chunknr  = pos.x/YSIZE;
		int chunkoff = pos.x%YSIZE;

		Chunk chunk = chunks;
		while((chunknr!=0) && (chunk!=null)){
			chunk = chunk.next;
			chunknr--;
		}
		if(chunk==null) return OUTOFBOUNDS;
		
		if((chunk.ground[pos.y]&BitUtils.BIT[chunkoff])!=0){
			return GROUND;
		}else if((chunk.bricks[pos.y]&BitUtils.BIT[chunkoff])!=0){
			return BRICKS;
		}else if((chunk.clouds[pos.y]&BitUtils.BIT[chunkoff])!=0){
			return CLOUDS;
		}
		return EMPTY;
	}

	public boolean isSolid(XY pos){
		if(!pos.isInBounds()) return true;
		int chunknr  = pos.x/YSIZE;
		int chunkoff = pos.x%YSIZE;
		Chunk chunk = chunks;
		while((chunknr!=0) && (chunk.next!=null)){
			chunk = chunk.next;
			chunknr--;
		}
		
		return (chunk.bricks[pos.y]==BitUtils.BIT[chunkoff]) || (chunk.ground[pos.y]==BitUtils.BIT[chunkoff]);
	}
	
	public boolean isOnGroud(XY pos){
		if(!pos.isInBounds()) return false;
		int chunknr  = pos.x/YSIZE;
		int chunkoff = pos.x%YSIZE;
		Chunk chunk = chunks;
		while((chunknr!=0) && (chunk.next!=null)){
			chunk = chunk.next;
			chunknr--;
		}
		if(pos.y>=1) return chunk.ground[pos.y-1]==BitUtils.BIT[chunkoff];
		return false;
	}
	
	public boolean isBelowBrick(XY pos){
		if(!pos.isInBounds()) return false;
		int chunknr  = pos.x/YSIZE;
		int chunkoff = pos.x%YSIZE;
		Chunk chunk = chunks;
		while((chunknr!=0) && (chunk.next!=null)){
			chunk = chunk.next;
			chunknr--;
		}
		if(pos.y<7) return chunk.ground[pos.y+1]==BitUtils.BIT[chunkoff];
		return false;
	}
	
	public int[][] toNEOArray(int offset,int x, int y){	
		if(offset<0) offset=0;
		if(chunks==null); //TODO return empty array
		
		long GND = 0;
		long CLD = 0;
		long BRK = 0;
		int chunknr  = offset/YSIZE;
		int chunkoff = offset%YSIZE;

		Chunk chunk = chunks; 
		while((chunknr!=0) && (chunk.next!=null)){	// select the right chunk
			chunk = chunk.next;
			chunknr--;
		}
		
		//usual case
		if(chunkoff<=(YSIZE-8)){
			for(int i=7;i>=0;i--){ // converting to a 8x8 Bitfield
				GND <<= 8;
				GND |= ((chunk.ground[i] >>> chunkoff) & 0xFF);
				BRK <<= 8;
				BRK |= ((chunk.bricks[i] >>> chunkoff) & 0xFF);
				CLD <<= 8;
				CLD |= ((chunk.clouds[i] >>> chunkoff) & 0xFF);
			}	
		}else{//on an edge
			for(int i=7;i>=0;i--){ // converting to a 8x8 Bitfield
				GND <<= 8;
				GND |= ((chunk.ground[i] >>> chunkoff) & 0xFF);
				BRK <<= 8;
				BRK |= ((chunk.bricks[i] >>> chunkoff) & 0xFF);
				CLD <<= 8;
				CLD |= ((chunk.clouds[i] >>> chunkoff) & 0xFF);
			}
			long GND2=0,BRK2=0,CLD2=0;
			chunkoff = YSIZE-chunkoff;
			chunk = chunk.next;
			if(chunk!=null){
				for(int i=7;i>=0;i--){ // converting to a 8x8 Bitfield
					GND2 <<= 8;
					GND2 |= ((chunk.ground[i] << chunkoff) & 0xFF);
					BRK2 <<= 8;
					BRK2 |= ((chunk.bricks[i] << chunkoff) & 0xFF);
					CLD2 <<= 8;
					CLD2 |= ((chunk.clouds[i] << chunkoff) & 0xFF);
				}
			}
			GND |= GND2;
			CLD |= CLD2;
			BRK |= BRK2;
		}
		int[][] arr = Bitfields.toNEOArr(GND, BRK, 1, 2); //TODO use all bitboards
		arr[x][8-y] = Frame.NEO_RED; // paint mario
		return arr;
	}
	
	public String toString(){
		StringBuffer str = new StringBuffer();
		
		for(int y=7;y>=0;y--){
			int occ=0;
			for(int i=0;occ!=OUTOFBOUNDS;i++){
				occ = getOccupation(new XY(i,y));
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
