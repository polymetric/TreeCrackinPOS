package TreeCrackinPOS;

import kaptainwutax.seedutils.lcg.LCG;

public class NextIntWhackCaseDetector {
    public static void main(String[] args) {
        long seed = 9943773019369L;

        for (int i = 0; i < 1000; i++) {
            if(nextIntWhackCase(seed, 3)) {
                System.out.printf("hit whack case in %4d iters\n", i);
            }
        }
    }

    static int next(long seed, int bits) {
        return (int)(LCG.JAVA.nextSeed(seed) >>> (48 - bits));
    }

    // nextInt where bound is not a power of 2
    static boolean nextIntWhackCase(long seed, int bound) {
        int mask = bound - 1;
        int bits = next(seed, 31);
        int result = bits % bound;
        return (bits - result + mask < 0);
    }
}
