package TreeCrackinPOS;

import kaptainwutax.seedutils.lcg.LCG;

public class MultiplierFinder {
    public static void main(String[] args) {
        long[] mults = {
                120950523281469L
        };

        for (long mult : mults) {
            for (int i = 0; i < 30; i++) {
                if (LCG.JAVA.combine(i).multiplier == mult) {
                    System.out.printf("mult %15d is %4d steps from step 1\n", mult, i);
                }
            }
        }
    }
}
