package TreeCrackinPOS;

import java.util.ArrayList;
import java.util.Comparator;

public class SortByDFZ {
    public static void main(String[] args) throws Exception {
        final String seedsInPath = "treeseeds_shotn.txt";
        final String seedsOutPath = "treeseeds_shotn_sorted.txt";

//        long[] treeSeeds = {
//                197343751701400L,
//                238584318639607L,
//                26060680171178L,
//                99121708499217L,
//                249520863891189L,
//                142639957996320L,
//                246967174247599L,
//                235216095870594L,
//                191943944339408L,
//                210112431842404L,
//                91358059325155L,
//        };
        String[] seedsIn = Utils.readFileToString(seedsInPath).split("\n");
        StringBuilder seedsOut = new StringBuilder();
        ArrayList<Seed> seedsList = new ArrayList<Seed>();

//        for (Long seed : treeSeeds) {
        for (int i = 0; i < seedsIn.length; i++) {
            long seed;
            try {
                seed = Long.parseLong(seedsIn[i]);
            } catch (NumberFormatException e) {
                continue;
            }
            seedsList.add(new Seed(seed, DiscreteLog.distanceFromZero(seed)));
        }

        seedsList.sort(Comparator.comparing(Seed::getDfz));

        for (int i = 0; i < seedsList.size(); i++) {
            Seed seed = seedsList.get(i);
//            System.out.printf("%15d\n", seed.seed, seed.dfz);
            seedsOut.append(String.format("%15d\n", seed.seed));
        }

        Utils.writeStringToFile(seedsOutPath, seedsOut.toString());
    }

    public static class Seed {
        long seed;
        long dfz;

        public Seed(long seed, long dfz) {
            this.seed = seed;
            this.dfz = dfz;
        }

        public long getDfz() {
            return dfz;
        }
    }
}
