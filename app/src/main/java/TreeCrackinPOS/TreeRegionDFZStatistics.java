package TreeCrackinPOS;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class TreeRegionDFZStatistics {
    public static void main(String[] args) throws Exception {
        String[] dfzsIn = Utils.readFileToString("tree_region_dfzs.txt").split("\\r?\\n");
        HashMap<Long, DFZ> dfzsMap = new HashMap<>();
        ArrayList<DFZ> sortedDfzs = new ArrayList<>();
        long totalDFZs = 0;

        for (String line : dfzsIn) {
//            System.out.println(line);
            long dfz = Long.parseLong(line);
            if (dfzsMap.containsKey(dfz)) {
                dfzsMap.get(dfz).occurrences += 1;
            } else {
                dfzsMap.put(dfz, new DFZ(dfz, 1));
            }
            totalDFZs++;
        }

        sortedDfzs = new ArrayList<>(dfzsMap.values());
        Collections.sort(sortedDfzs, Comparator.comparing(DFZ::getOccurrences));

        for (DFZ dfz : sortedDfzs) {
//            System.out.printf("%4d occurred %3d times, with a %6.2f%% chance\n", dfz.dfz, dfz.occurrences, ((double) dfz.occurrences / totalDFZs) * 100);
            if (dfz.occurrences > 1) {
                System.out.printf("%4d, ", dfz.dfz);
            }
        }
        System.out.println("total unique dfz count:" + sortedDfzs.size());
    }

    static class DFZ {
        long dfz;
        int occurrences;

        public DFZ(long dfz, int occurrences) {
            this.dfz = dfz;
            this.occurrences = occurrences;
        }

        public long getOccurrences() {
            return occurrences;
        }
    }
}
