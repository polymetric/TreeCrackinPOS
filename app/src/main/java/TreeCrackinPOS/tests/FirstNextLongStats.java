package TreeCrackinPOS.tests;

import kaptainwutax.seedutils.lcg.LCG;

import java.util.Random;

import static TreeCrackinPOS.tests.ApeBrainNanotimeFinder.*;
import static TreeCrackinPOS.tests.ApeBrainNextLongReversal.*;

public class FirstNextLongStats {
    public static void main(String[] args) throws Exception {
//        System.out.println(LCG.JAVA.combine(-1).nextSeed(0) ^ LCG.JAVA.multiplier);
        System.out.println(new Random(107038380838084L).nextLong());

        System.exit(0);

        for (int i = 0; i < 100; i++) {
            long originalNanoTime = System.nanoTime();
            Random rand = new Random(getNthUniquifier(1) ^ originalNanoTime);
            long l = rand.nextLong();
            long ntGuess = getNanoTimeMod48FromNextLong(l, 1);
            System.out.printf("original nanotime: %15d derived nanotime: %15d\n", originalNanoTime % LCG.JAVA.modulus, ntGuess % LCG.JAVA.modulus);
        }
    }
}
