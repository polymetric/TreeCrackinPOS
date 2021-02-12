package TreeCrackinPOS;

import kaptainwutax.seedutils.lcg.LCG;

public class WorldToChunk {
    // world seed -> chunk seed
    public static void main(String[] args) {
        long worldSeed = 5;
        int chunkX = -5;
        int chunkZ = 7;

        long chunkSeed = worldToChunk(worldSeed, chunkX, chunkZ);
        System.out.println(chunkSeed);
        System.out.println((chunkSeed ^ LCG.JAVA.multiplier) & LCG.JAVA.modulus-1);
    }

    public static long worldToChunk(long worldSeed, int chunkX, int chunkZ) {
        RandomOverride rand = new RandomOverride();
        rand.setSeed(worldSeed);
        long l1 = (rand.nextLong() / 2L) * 2L + 1L;
        long l2 = (rand.nextLong() / 2L) * 2L + 1L;
        long chunkSeed = (long) chunkX * l1 + (long) chunkZ * l2 ^ worldSeed;
        return chunkSeed;
    }
}
