package TreeCrackinPOS.tests;

import kaptainwutax.seedutils.lcg.LCG;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;

import static TreeCrackinPOS.tests.ApeBrainNextLongReversal.*;

public class ApeBrainNanotimeFinder {
    public static final long initialUniquifierValue = 8682522807148012L; // this value isn't actually ever used as a uniquifier

    public static long getNthUniquifier(long n) {
        return LCG.JAVA_UNIQUIFIER_OLD.combine(n).nextSeed(initialUniquifierValue);
    }

    // gets the lower 48 bits of the nanotime that would've initialized a java RNG instance
    // when the INITIAL seed of that instance is the parameter `seed`.
    // it also depends on knowing which uniquifier was used.
    public static long getNanoTimeMod48FromSeed48(long seed, long uniquifierSeqNum) {
        return ((seed ^ LCG.JAVA.multiplier) ^ getNthUniquifier(uniquifierSeqNum)) % LCG.JAVA.modulus;
    }

    public static long getNanoTimeMod48FromNextLong(long nextLong, long uniquifierSeqNum) throws Exception {
        return (getNanoTimeMod48FromSeed48(nextLongReversal(nextLong), uniquifierSeqNum));
    }

    public static void main(String[] args) throws Exception {
//        long seed = nextLongReversal(new Random().nextLong());
        long seed = nextLongReversal(0);
        System.out.println(seed);
        ArrayList<Long> nanoTimes = new ArrayList<>();
        for (int i = 0; i < 1; i++) {
            long nanoTime = getNanoTimeMod48FromSeed48(seed, i+1);
            if (nanoTime > 0) {
                nanoTimes.add(nanoTime);
//                System.out.println(nanoTime);
            }
        }
        nanoTimes.sort(Comparator.reverseOrder());
        for (long nanoTime : nanoTimes) {
//            System.out.println(nanoTime/1e9/60/60);
            System.out.println(nanoTime);
        }

        System.out.println();
        System.out.println(System.nanoTime()/1e9/60/60);
    }
}
