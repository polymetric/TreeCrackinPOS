package TreeCrackinPOS;

import TreeCrackinPOS.utils.Utils;
import kaptainwutax.seedutils.lcg.LCG;

import java.util.ArrayList;
import java.util.Comparator;

import static TreeCrackinPOS.utils.Utils.*;
import static java.lang.Math.*;

public class FindNearestSeedInFile {
    public static void main(String[] args) throws Exception {
        final long targetSeed = 147759439373717L;
        final String seedsInPath = "treeseeds_taigatest_2.txt";
        String[] seedsIn = readFileToString(seedsInPath).split("\n");
        ArrayList<SeedDfzPair> seedDfzPairs = new ArrayList<>();

        for (int i = 0; i < seedsIn.length; i++) {
            long seed = Long.parseLong(seedsIn[i].trim());
            seedDfzPairs.add(new SeedDfzPair(seed, abs(LCG.JAVA.distance(targetSeed, seed))));
        }

        seedDfzPairs.sort(Comparator.comparing(SeedDfzPair::getDfz).reversed());

        for (SeedDfzPair seedDfzPair : seedDfzPairs) {
            System.out.printf("seed %15d has dfz %15d\n", seedDfzPair.seed, seedDfzPair.dfz);
        }
    }

    public static class SeedDfzPair {
        public long seed;
        public long dfz;

        public SeedDfzPair(long seed, long dfz) {
            this.seed = seed;
            this.dfz = dfz;
        }

        public long getDfz() {
            return dfz;
        }
    }
}
