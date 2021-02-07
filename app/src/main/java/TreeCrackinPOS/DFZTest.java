package TreeCrackinPOS;

import kaptainwutax.seedutils.lcg.LCG;

public class DFZTest {
    public static void main(String[] args) {
        long seed = 0;
        for (int i = 0; i < 10; i++) {
            seed = LCG.JAVA.nextSeed(seed);
//            System.out.println(DiscreteLog.distanceFromZero(seed));
        }

        LCG advance24087125031275 = LCG.JAVA.combine(24087125031275L);

        System.out.println(advance24087125031275.nextSeed(0));

        System.out.println(DiscreteLog.distanceFromZero(5));
    }
}
