package ch.baws.projectneo.mario;

public class BitUtils {

	final static long BIT00 = (1L);
	final static long BIT01 = (1L << 1);
	final static long BIT02 = (1L << 2);
	final static long BIT03 = (1L << 3);
	final static long BIT04 = (1L << 4);
	final static long BIT05 = (1L << 5);
	final static long BIT06 = (1L << 6);
	final static long BIT07 = (1L << 7);
	
	final static long BIT08 = (1L << 8);
	final static long BIT09 = (1L << 9);
	final static long BIT10 = (1L << 10);
	final static long BIT11 = (1L << 11);
	final static long BIT12 = (1L << 12);
	final static long BIT13 = (1L << 13);
	final static long BIT14 = (1L << 14);
	final static long BIT15 = (1L << 15);
	
	final static long BIT16 = (1L << 16);
	final static long BIT17 = (1L << 17);
	final static long BIT18 = (1L << 18);
	final static long BIT19 = (1L << 19);
	final static long BIT20 = (1L << 20);
	final static long BIT21 = (1L << 21);
	final static long BIT22 = (1L << 22);
	final static long BIT23 = (1L << 23);
	
	final static long BIT24 = (1L << 24);
	final static long BIT25 = (1L << 25);
	final static long BIT26 = (1L << 26);
	final static long BIT27 = (1L << 27);
	final static long BIT28 = (1L << 28);
	final static long BIT29 = (1L << 29);
	final static long BIT30 = (1L << 30);
	final static long BIT31 = (1L << 31);	
	
	final static long BIT32 = (1L << 32);
	final static long BIT33 = (1L << 33);
	final static long BIT34 = (1L << 34);
	final static long BIT35 = (1L << 35);
	final static long BIT36 = (1L << 36);
	final static long BIT37 = (1L << 37);
	final static long BIT38 = (1L << 38);
	final static long BIT39 = (1L << 39);	
	
	final static long BIT40 = (1L << 40);
	final static long BIT41 = (1L << 41);
	final static long BIT42 = (1L << 42);
	final static long BIT43 = (1L << 43);
	final static long BIT44 = (1L << 44);
	final static long BIT45 = (1L << 45);
	final static long BIT46 = (1L << 46);
	final static long BIT47 = (1L << 47);	
	
	final static long BIT48 = (1L << 48);
	final static long BIT49 = (1L << 49);
	final static long BIT50 = (1L << 50);
	final static long BIT51 = (1L << 51);
	final static long BIT52 = (1L << 52);
	final static long BIT53 = (1L << 53);
	final static long BIT54 = (1L << 54);
	final static long BIT55 = (1L << 55);
	
	final static long BIT56 = (1L << 56);
	final static long BIT57 = (1L << 57);
	final static long BIT58 = (1L << 58);
	final static long BIT59 = (1L << 59);
	final static long BIT60 = (1L << 60);
	final static long BIT61 = (1L << 61);
	final static long BIT62 = (1L << 62);
	final static long BIT63 = (1L << 63);		
	
	/**
	 * BIT[nbr] represents a 8x8 Bitfield where the nbr bit is set.
	 */
	final public static long[] BIT = 	  { BIT00,BIT01,BIT02,BIT03,BIT04,BIT05,BIT06,BIT07,
											BIT08,BIT09,BIT10,BIT11,BIT12,BIT13,BIT14,BIT15,
											BIT16,BIT17,BIT18,BIT19,BIT20,BIT21,BIT22,BIT23,
											BIT24,BIT25,BIT26,BIT27,BIT28,BIT29,BIT30,BIT31,
											BIT32,BIT33,BIT34,BIT35,BIT36,BIT37,BIT38,BIT39,
											BIT40,BIT41,BIT42,BIT43,BIT44,BIT45,BIT46,BIT47,
											BIT48,BIT49,BIT50,BIT51,BIT52,BIT53,BIT54,BIT55,
											BIT56,BIT57,BIT58,BIT59,BIT60,BIT61,BIT62,BIT63 };
	
	public static boolean isSet64x8(int x,int y,long[] bf){
		return ((bf[y]&BitUtils.BIT[x])!=0);
	}
	
	public static String toString64x8(long[] map){
		StringBuffer str = new StringBuffer();
		
		for(int y=7;y>=0;y--){
			str.append("["+y+"] ");
			for(int i=0;i<64;i++){
				if(isSet64x8(i, y, map))
					str.append('X');
				else
					str.append('O');
				str.append(' ');
			}
			str.append('\n');
		}
		
		return str.toString();
	}
	public static String toHexString64x8(long[] map){
		StringBuffer str = new StringBuffer();
		for(int y=7;y>=0;y--){
			str.append("0x");
			str.append(Long.toHexString(map[y]));
			str.append("L\n");
		}
		return str.toString();
	}
	
}
