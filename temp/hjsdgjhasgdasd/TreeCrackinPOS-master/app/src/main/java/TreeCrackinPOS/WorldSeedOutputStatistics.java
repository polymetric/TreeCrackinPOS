package TreeCrackinPOS;

import TreeCrackinPOS.utils.Utils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

public class WorldSeedOutputStatistics {
    public static void main(String[] args) throws Exception {
        String[] seedsIn = Utils.readFileToString("worldseeds_shotp_try2.txt").split("\\r?\\n");
        HashMap<Long, Seed> occurrences = new HashMap<>();
        ArrayList<Seed> seeds = new ArrayList<>();
        long total = 0;

        for (String s : seedsIn) {
            long seed = 0L;
            try {
                seed = Long.parseLong(s);
            } catch (NumberFormatException e) {
                continue;
            }
            if (occurrences.containsKey(seed)) {
                occurrences.get(seed).occurrences++;
            } else {
                Seed seedObj = new Seed(seed);
                occurrences.put(seed, seedObj);
                seeds.add(seedObj);
            }
            total++;
        }

        seeds.sort(Comparator.comparing(Seed::getOccurrences));

        for (Seed seed : seeds) {
            System.out.printf("seed %15d has %4d occurrences, %6.3f%%\n", seed.seed, seed.occurrences, ((float) seed.occurrences / total) * 100F);
        }
    }

    static class Seed {
        long seed;
        int occurrences;

        public Seed(long seed) {
            this.seed = seed;
            this.occurrences = 1;
        }

        public int getOccurrences() {
            return occurrences;
        }
    }
}
