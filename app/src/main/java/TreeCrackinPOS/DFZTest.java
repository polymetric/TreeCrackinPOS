package TreeCrackinPOS;

import kaptainwutax.seedutils.lcg.LCG;

public class DFZTest {
    public static void main(String[] args) {
        long seed = 0;


        long chunkSeed      =     25214903912L;
//                                25214903917L
        long treeRegionSeed = 197343751701400L;
        long[] treeSeeds = {
                197343751701400L,
                36469969418861L,
        };

        long dfz1 = DiscreteLog.distanceFromZero(chunkSeed);
        System.out.println(DiscreteLog.distanceFromZero(treeRegionSeed) - dfz1);
        for (long s : treeSeeds) {
            System.out.println(DiscreteLog.distanceFromZero(s) - dfz1);
        }
    }
}
