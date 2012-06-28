package ch.baws.projectneo.mario;

public class StaticMaps {
	// LEVEL 1_1
	
	public static final long[] level1_1_ground = {	~(BitUtils.BIT20 | BitUtils.BIT26 | BitUtils.BIT46),
													BitUtils.BIT38 | BitUtils.BIT39 | BitUtils.BIT41 | BitUtils.BIT42 | BitUtils.BIT44 | BitUtils.BIT45 | BitUtils.BIT47 | BitUtils.BIT48 | BitUtils.BIT54 | BitUtils.BIT55 | BitUtils.BIT56 | BitUtils.BIT57,
													BitUtils.BIT39 | BitUtils.BIT41 | BitUtils.BIT45 | BitUtils.BIT47 | BitUtils.BIT55 | BitUtils.BIT56 | BitUtils.BIT57,
													BitUtils.BIT06 | BitUtils.BIT22 | BitUtils.BIT28 | BitUtils.BIT35 | BitUtils.BIT51 | BitUtils.BIT56 | BitUtils.BIT57,
													BitUtils.BIT57,
													BitUtils.BIT23 | BitUtils.BIT24| BitUtils.BIT25 | BitUtils.BIT26 | BitUtils.BIT27 | BitUtils.BIT36 | BitUtils.BIT37,
													0L,
													0L};
	public static final long[] level1_1_bricks = { 	0,
													0,
													0,
													BitUtils.BIT03 | BitUtils.BIT05 | BitUtils.BIT07 | BitUtils.BIT19 | BitUtils.BIT23 | BitUtils.BIT31 | BitUtils.BIT32 | BitUtils.BIT33 | BitUtils.BIT52,
													0,
													BitUtils.BIT06 | BitUtils.BIT38,
													BitUtils.BIT32,
													0 };
	public static final long[] level1_1_clouds = {0,0,0,0,0,0,0,0};
	
	public static final Chunk level1_1_tmp = new Chunk(level1_1_ground,level1_1_clouds,level1_1_clouds,null);
	public static final Chunk level1_1 = new Chunk(level1_1_ground,level1_1_bricks,level1_1_clouds,null);
	
	// LEVEL 1_2
	
	
}
