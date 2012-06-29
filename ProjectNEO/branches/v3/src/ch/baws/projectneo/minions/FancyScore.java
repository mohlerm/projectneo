package ch.baws.projectneo.minions;

import ch.baws.projectneo.frameGenerator.Bitfields;
import ch.baws.projectneo.frameGenerator.Frame;

public class FancyScore {
	public FancyScore(){
		throw new NotImplementedException();
	}
	// these ciphers are alinged to the left of a 8x8 Bitfield
	// they are at maximum 6 pixel high and 4 wide
	// ciphers[0] represents a 0 and ciphers[9] a 9 and so on
	private static final long[] ciphers = { 0x3c42423c00000000L, // 0
											0x08047e0000000000L, // 1
											0x64524a4400000000L, // 2
											0x24424a3400000000L, // 3
											0x18147a1000000000L, // 4
											0x2e4a4a3200000000L, // 5
											0x3c4a4a3200000000L, // 6
											0x02720a0600000000L, // 7
											0x344a4a3400000000L, // 8
											0x244A4A3C00000000L};// 9	
	
	private static final long ERR = 0x7c54007010007010L;

	/**
	 * Generates the Array to display a
	 * @param number
	 * @return
	 */
	public static int[][] getArray(int number){ //TODO alignment isn't correct
		if(number <0 || number > 99) return Bitfields.toNEOArr(ERR, 0, Frame.NEO_RED, 0); // Score OutOfBounds? TODO better solution for score over 99
		
		long bf=0;
		
		if(number<10){
			bf = ciphers[number];
			bf >>>= 16; // align in the center
		}else{
			long tmp;
			tmp = ciphers[number%10];
			tmp >>>= 32; // aling right
			bf = ciphers[number/10];
			bf |= tmp;
		}
		
		return Bitfields.toNEOArr(bf, 0, Frame.NEO_RED, 0); // one might want to change the color here...
	}
	
	public long getBitMap(int number){
		if(number <0 || number > 99) return ERR; // Score OutOfBounds? TODO better solution for score over 99
		
		long bf=0;
		
		if(number<10){
			bf = ciphers[number];
			bf <<= 2; // align in the center
		}else{
			long tmp;
			tmp = ciphers[number%10];
			tmp <<= 4; // aling right
			bf = ciphers[number/10];
			bf |= tmp;
		}
		
		return bf;
	}
}
