package TreeCrackinPOS;

import kaptainwutax.seedutils.lcg.LCG;
import kaptainwutax.seedutils.mc.MCVersion;
import mjtb49.hashreversals.ChunkRandomReverser;

public class CRRTreesFinderSanityCheck {
    public static void main(String[] args) {
        // seed 5 tree seed from chunk 0, 2
        long treeSeed02 = 156952868252044L;
        long treeSeed00 = 36469969418861L;
//        System.out.println(checkTreeSeed5(treeSeed02));

        for (Long l : ChunkRandomReverser.reversePopulationSeed(LCG.JAVA.combine(-3787).nextSeed(treeSeed00) ^ LCG.JAVA.multiplier, 0, 0, MCVersion.v1_8)) {
            System.out.println(l);
        }
        System.out.println(WorldToChunk.worldToChunk(5, 0, 2));
    }

    static int checkTreeSeed5(long seed) {
        if ((((seed *     25214903917L +              11L) >> 44) & 15) != 13) return -1;
        if ((((seed * 205749139540585L +    277363943098L) >> 44) & 15) !=  0) return -2;
        if ((((seed *  76790647859193L +  25707281917278L) >> 47) &  1) !=  1) return -3;
        if ((((seed *  61282721086213L +  25979478236433L) >> 47) &  1) !=  0) return -4;
        if ((((seed * 128954768138017L + 137139456763464L) >> 47) &  1) !=  0) return -5;
        if ((((seed * 177269950146317L + 148267022728371L) >> 47) &  1) !=  1) return -6;
        if ((((seed *  19927021227657L + 127911637363266L) >> 47) &  1) !=  0) return -7;
        if ((((seed *  92070806603349L +  65633894156837L) >> 47) &  1) !=  1) return -8;
        if ((((seed *  28158748839985L + 233987836661708L) >> 47) &  1) !=  1) return -9;
        if ((((seed * 118637304785629L + 262259097190887L) >> 47) &  1) !=  1) return -10;
        if ((((seed * 127636996050457L + 159894566279526L) >> 47) &  1) !=  1) return -11;
        if ((((seed *  12659659028133L + 156526639281273L) >> 47) &  1) !=  0) return -12;
        if ((((seed * 120681609298497L +  14307911880080L) >> 47) &  1) !=  0) return -13;
        if ((((seed * 262331189124013L + 215905707320923L) >> 47) &  1) !=  1) return -14;

        System.out.printf("matches seed %15d\n", seed);

        return 1;
    }
}
