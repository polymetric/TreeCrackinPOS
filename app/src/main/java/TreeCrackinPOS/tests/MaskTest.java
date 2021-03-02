package TreeCrackinPOS.tests;

import kaptainwutax.seedutils.lcg.LCG;

public class MaskTest {
    public static void main(String[] args) {
        final int RANGE = 20;
        long seed = 210479984533430L;
        seed = LCG.JAVA.combine(-RANGE).nextSeed(seed);

        for (int i = 0; i < RANGE*2; i++) {
            System.out.printf("seed %15d gets coord %2d\n", seed, (seed >> 44) & 15);
            seed = LCG.JAVA.nextSeed(seed);
        }
    }
}
