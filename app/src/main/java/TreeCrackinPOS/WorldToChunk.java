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
        long chunkSeedMultiplierX = (rand.nextLong() / 2L) * 2L + 1L;
        long chunkSeedMultiplierZ = (rand.nextLong() / 2L) * 2L + 1L;
        long chunkSeed = (long) chunkX * chunkSeedMultiplierX + (long) chunkZ * chunkSeedMultiplierZ ^ worldSeed;
        return (chunkSeed & ((1L << 48) - 1)) ^ LCG.JAVA.multiplier;
    }
}
