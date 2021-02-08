package TreeCrackinPOS;

import kaptainwutax.seedutils.lcg.LCG;

public class DFZTest {
    public static void main(String[] args) {
        long seed = 0;
        for (int i = 0; i < 10; i++) {
            seed = LCG.JAVA.nextSeed(seed);
//            System.out.println(DiscreteLog.distanceFromZero(seed));
        }

//        System.out.println(DiscreteLog.distanceFromZero(156526639281274L));

        long chunkSeed      =     25214903912L;
        long treeRegionSeed = 197343751701400L;
        long treeSeed1      =  14810236876615L;
        long treeSeed2      =  40892402490042L;

        long dfz1 = DiscreteLog.distanceFromZero(chunkSeed);
        System.out.println(DiscreteLog.distanceFromZero(treeRegionSeed) - dfz1);
        System.out.println(DiscreteLog.distanceFromZero(treeSeed1) - dfz1);
        System.out.println(DiscreteLog.distanceFromZero(treeSeed2) - dfz1);
    }
}
