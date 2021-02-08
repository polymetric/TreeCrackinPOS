package TreeCrackinPOS;

public class WorldToChunk {
    // world seed -> chunk seed
    public static void main(String[] args) {
        RandomOverride rand = new RandomOverride();

        long worldSeed = 5;
        int chunkX = 0;
        int chunkZ = 0;

        rand.setSeed(worldSeed);
        long l1 = (rand.nextLong() / 2L) * 2L + 1L;
        long l2 = (rand.nextLong() / 2L) * 2L + 1L;
        rand.setSeed((long) chunkX * l1 + (long) chunkZ * l2 ^ worldSeed);
        System.out.println(rand.getSeed());
    }
}
