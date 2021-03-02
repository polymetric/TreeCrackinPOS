package TreeCrackinPOS;

import TreeCrackinPOS.utils.Utils;

public class DFZDelta2 {
    public static void main(String[] args) throws Exception {
        long chunkSeed      =     25214903912L;
//                                25214903917L // multiplier
        long treeRegionSeed = 197343751701400L;
        final int TREE_PATCH_SIZE = 4;

        String[] treeSeeds = Utils.readFileToString("treeseeds_shotp_3_sorted.txt").split("\n");

        long lastDfz = 0;
//        for (long s : treeSeeds) {
        for (int i = 0; i < treeSeeds.length; i += TREE_PATCH_SIZE) {
            long initialSeed = 0;
            long initialDfz = 0;
            for (int j = 0; j < TREE_PATCH_SIZE && i+j < treeSeeds.length; j++) {
                long s = Long.parseLong(treeSeeds[i+j]);
                if (j == 0) {
                    initialSeed = s;
                    System.out.println(initialSeed);
                    initialDfz = DiscreteLog.distanceFromZero(s);
                    continue;
                }
                long curDfz = DiscreteLog.distanceFromZero(s);
                System.out.printf("%4d\n", curDfz - initialDfz);
            }
        }
    }
}
