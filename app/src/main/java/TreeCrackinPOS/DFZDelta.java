package TreeCrackinPOS;

public class DFZDelta {
    public static void main(String[] args) throws Exception {
        long chunkSeed      =     25214903912L;
//                                25214903917L // multiplier
        long treeRegionSeed = 197343751701400L;
        long[] treeSeeds = {
                210579644006633L,
                73835844781823L,
                243555800182311L,
                174706538134093L,
                71665048239413L,
                210755731079403L,
                244081849022867L,
                276860902705753L,
                232022459368687L,
                260783265487573L,
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
