package TreeCrackinPOS;

public class DFZDelta {
    public static void main(String[] args) throws Exception {
        long chunkSeed      =     25214903912L;
//                                25214903917L // multiplier
        long treeRegionSeed = 197343751701400L;
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
//                99121708499217L,
//                142639957996320L,
//                191943944339408L,
//                210112431842404L,
//        };

        String[] treeSeeds = Utils.readFileToString("treeseeds_shotp_3_sorted.txt").split("\n");

        long lastDfz = 0;
//        for (long s : treeSeeds) {
        for (int i = 0; i < treeSeeds.length; i++) {
            long s = Long.parseLong(treeSeeds[i].trim());
            long curDfz = DiscreteLog.distanceFromZero(s);
            long delta = curDfz - lastDfz;
            System.out.printf("%4d\n", delta);
            lastDfz = curDfz;
        }
    }
}
