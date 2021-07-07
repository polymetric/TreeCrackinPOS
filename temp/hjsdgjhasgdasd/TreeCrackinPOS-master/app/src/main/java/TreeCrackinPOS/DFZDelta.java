package TreeCrackinPOS;

public class DFZDelta {
    public static void main(String[] args) throws Exception {
        long chunkSeed      =     25214903912L;
//                                25214903917L // multiplier
        long treeRegionSeed = 197343751701400L;
        long[] treeSeeds = {
                148077625820188L,
                63876454369314L,
                220967722438826L,
                225855820187136L,
                263377406591570L,
        };

//        String[] treeSeeds = Utils.readFileToString("treeseeds_shotp_3_sorted.txt").split("\n");

        long initialDfz = DiscreteLog.distanceFromZero(treeSeeds[0]);
        long lastDfz = initialDfz;
        for (long s : treeSeeds) {
//        for (int i = 0; i < treeSeeds.length; i++) {
//            long s = Long.parseLong(treeSeeds[i].trim());
            long curDfz = DiscreteLog.distanceFromZero(s);
            long delta = curDfz - lastDfz;
            System.out.printf("%4d\n", delta);
            lastDfz = curDfz;
        }
    }
}
