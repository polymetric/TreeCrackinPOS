package TreeCrackinPOS.tests;

import kaptainwutax.seedutils.lcg.LCG;

import static TreeCrackinPOS.tests.ApeBrainNanotimeFinder.*;
import static TreeCrackinPOS.tests.ApeBrainNextLongReversal.*;

public class CanWeGetSeed0 {
    public static void main(String[] args) throws Exception {
        System.out.println(getNanoTimeMod48FromNextLong(0, 1));
        System.out.println(107048004364969L / 1e9 / 60 / 60);

        long timeStart = System.nanoTime();
        int iters = 0;
        while (true) {
            iters++;
            if (System.nanoTime() % LCG.JAVA.modulus == 107048004364969L) {
                System.out.printf("found %19d in %19d iters\n", System.nanoTime(), iters);
                break;
            }
        }
        System.out.printf("tried %19d nanotimes in %12.6fs\n", iters, (System.nanoTime() - timeStart) / 1e9);
    }
}
