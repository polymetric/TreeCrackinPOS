package TreeCrackinPOS;

import kaptainwutax.seedutils.lcg.LCG;
import kaptainwutax.seedutils.mc.MCVersion;
import mjtb49.hashreversals.ChunkRandomReverser;

public class CRRTest {
    public static void main(String[] args) {
        for (long l : ChunkRandomReverser.reversePopulationSeed(25214903912L, 0, 0, MCVersion.v1_8)) {
            System.out.println(l ^ LCG.JAVA.multiplier);
        }
    }
}
