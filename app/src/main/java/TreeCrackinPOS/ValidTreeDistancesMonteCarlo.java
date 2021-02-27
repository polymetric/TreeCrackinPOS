package TreeCrackinPOS;

import java.util.*;

public class ValidTreeDistancesMonteCarlo {
    // monte carlo approach to generating valid distances between multiple trees
    // essentially we just generate sums of random multiples of the possible distances between SINGLE trees
    // and print all the ones that are unique
    public static void main(String[] args) throws Exception {
        Random rand = new Random();
        final String outFile = "validTaigaTreeDistances.txt";
        final int TREE_CALL_RANGE = 90;
        final int ITERS = 1000000;
        final int[] possibleDistances = {
                6,
                8,
        };
        ArrayList<Integer> results = new ArrayList<>();

        for (int i = 0; i < ITERS; i++) {
            int sum = 0;
            for (int j = 0; j < possibleDistances.length; j++) {
                sum += possibleDistances[j] * rand.nextInt(TREE_CALL_RANGE / possibleDistances[j] + 1);
            }
            if (sum > TREE_CALL_RANGE) {
                // if it's outside the call range it's not a valid distance
                continue;
            }
            results.add(sum);
        }

        StringBuilder sb = new StringBuilder();

        Set<Integer> set = new HashSet<>();
        for (int result : results) {
            set.add(result);
        }
        results.clear();
        for (int i : set) {
            results.add(i);
        }
        results.sort(Comparator.naturalOrder());
        for (int i : results) {
//            System.out.println(i);
            sb.append(i + ",\n");
        }

        Utils.writeStringToFile(outFile, sb.toString());
    }
}
