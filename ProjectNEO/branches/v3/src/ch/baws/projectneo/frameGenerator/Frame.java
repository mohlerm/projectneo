package ch.baws.projectneo.frameGenerator;

import java.util.Random;

import android.graphics.Color;



public class Frame {
	public static final String[] colorOptions = {"Red", "Green", "Blue","Yellow","Magenta","Cyan","White"};
	public static final int NEO_OFF 		= 0;
	public static final int NEO_RED 		= 1;
	public static final int NEO_GREEN 	= 2;
	public static final int NEO_BLUE 		= 3;
	public static final int NEO_YELLOW 	= 4;
	
	public static final int NEO_TURK 		= 5; //Alias
	public static final int NEO_CYAN 		= 5;

	public static final int NEO_PINK 		= 6; //Alias
	public static final int NEO_MAGENTA		= 6;
	
	public static final int NEO_WHITE 	= 7;
	
	public static final int[] NEO_COLOR_LST = {Color.BLACK,Color.RED,Color.GREEN,Color.BLUE,Color.YELLOW,Color.CYAN,Color.MAGENTA,Color.WHITE};

	public enum CMDB {
		/* Possible command bytes: 
		 * #define CMD_NONE 		 0
		 * #define CMD_ROTATE_START  1
		 * #define CMD_ROTATE 		 2
		 * #define CMD_ROTATE_END 	 3
		 * 
		 */
		CMD_NONE 		 ((byte) 0x00),
		CMD_ROTATE_START ((byte) 0x01),
		CMD_ROTATE 		 ((byte) 0x02),
		CMD_ROTATE_END	 ((byte) 0x03),
		CMD_DEGUG		 ((byte) 0x04);
		
		private byte value;
		
		CMDB(byte cmd){
			this.value = cmd;
		}
		public byte cmd(){
			return this.value;
		}
		
	}

	
	private CMDB cmdb= CMDB.CMD_NONE;
	
	private long blub = 0;
	private long redb = 0;
	private long greb = 0;
	
	public Frame(int[][] arr) {
		set(arr);
	}




	//===== Generators
	/**
	 * Generates a Frame Packet out of a single array
	 * use the constants of the class 'Frame' for the colors
	 * @param arr Array which has integers representing the color of an LED
	 * @return Frame-Packet
	 */
	public void set(int[][] arr){
		redb=0; greb=0; blub=0;
		for(int i=0;i<8;i++){
			for(int j=0;j<8;j++){
				switch(arr[i][j]){
					case Frame.NEO_RED:
						redb |= Bitfields.xyToBit(i, j);
						break;
					case Frame.NEO_GREEN:
						greb |= Bitfields.xyToBit(i, j);
						break;
					case Frame.NEO_BLUE:
						blub |= Bitfields.xyToBit(i, j);
						break;
					case Frame.NEO_YELLOW: 
						redb |= Bitfields.xyToBit(i, j);
						greb |= Bitfields.xyToBit(i, j);
						break;
					case Frame.NEO_TURK:
						blub |= Bitfields.xyToBit(i, j);
						greb |= Bitfields.xyToBit(i, j);
						break;
					case Frame.NEO_PINK:
						redb |= Bitfields.xyToBit(i, j);
						blub |= Bitfields.xyToBit(i, j);
						break;
					case Frame.NEO_WHITE:
						redb |= Bitfields.xyToBit(i, j);
						greb |= Bitfields.xyToBit(i, j);
						blub |= Bitfields.xyToBit(i, j);
						break;
				}
			}
		}
		return;
	}

	

	
	//===== Utils
	
	public void setDebug(){
		redb = ('t' << 56) | ('e' << 48) | ('s' << 40) | ('t' << 32) | ('m' << 24) | ('e' << 16) | ('s' << 8) | ( 's' << 0);
		greb = ('a' << 56) | ('g' << 48) | ('e' << 40) | ('1' << 32) | ('2' << 24) | ('3' << 16) | ('4' << 8) | ('5' << 0);
		
		this.cmdb = CMDB.CMD_DEGUG;
		
		blub = 0;
		Random rand = new Random();
		int chr;
		for(int i=0;i<8;i++){
			chr = (int) 'A' + rand.nextInt(26);
			blub <<= 8;
			blub |= chr;
		}
		return;
	}
	/**
	 * @return a string representation of the packed frame
	 */
	public String toString(){
		StringBuffer str = new StringBuffer();
		
		byte[] packet = PacketGenerator.pack(this);
		str.append('[');
		
		str.append(String.format("%1$#04x", packet[0]));
		str.append(" ");
		str.append(String.format("%1$#04x", packet[1]));
		str.append(" ");
		
		str.append("{ ");
		for(int i=2;i<10;i++){
			str.append(String.format("%1$#04x", packet[i]));
			str.append(" ");
		}
		str.append("}");
		
		str.append("{ ");
		for(int i=10;i<18;i++){
			str.append(String.format("%1$#04x", packet[i]));
			str.append(" ");
		}
		str.append("}");
		
		str.append("{ ");
		for(int i=18;i<26;i++){
			str.append(String.format("%1$#04x", packet[i]));
			str.append(" ");
		}
		str.append("}");
		
		str.append("]");
		
		return str.toString();
	}
	
	//Getter
	public CMDB getCmdb() {
		return cmdb;
	}


	public long getBlub() {
		return blub;
	}


	public long getRedb() {
		return redb;
	}

	public long getGreb() {
		return greb;
	}


}
