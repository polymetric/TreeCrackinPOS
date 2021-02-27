package TreeCrackinPOS;

import kaptainwutax.seedutils.lcg.LCG;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

public class DeDodgeTrees {
    // takes the output of the tree kernel (primary tree seeds that matched aux tree seeds as well)
    // and gets the aux tree seeds back out
    public static void main(String[] args) throws Exception {
        final int TREE_COUNT = 5;
//        final String seedsInPath = "treeseeds_shotn_sorted.txt";
//        final String seedsOutPath = "treeseeds_shotn_refound.txt";
        final String seedsInPath = "treeseeds_taigatest.txt";
        final String seedsOutPath = "treeseeds_taigatest_dedodged.txt";

        final int TREE_CALL_RANGE = 90;
        final LCG revTCR = LCG.JAVA.combine(-TREE_CALL_RANGE);

//        String[] seedsIn = Utils.readFileToString(seedsInPath).split("\n");\
        String[] seedsIn = {
                "145958532796539",
        }; // DEBUG

        File seedsOutFile = new File(seedsOutPath);
        seedsOutFile.delete();
        seedsOutFile.createNewFile();
        FileWriter seedsOutStream = new FileWriter(seedsOutPath);

        int seedsPassed = 0;

        mainSeedLoop:
        for (int i = 0; i < seedsIn.length; i++) {
            long seed = Long.parseLong(seedsIn[i].trim());

            ArrayList<Long> treeSeeds = new ArrayList<>();

            // refind tree seeds
            System.out.printf("subseeds for seed %15d\n", seed);
            seed = revTCR.nextSeed(seed);
            for (int callOffset = 0; callOffset < TREE_CALL_RANGE * 2; callOffset++) {
                seed = LCG.JAVA.nextSeed(seed);

                if (checkTree0(seed) == 1) {
                    treeSeeds.add(seed);
                }
                if (checkTree1(seed) == 1) {
                    treeSeeds.add(seed);
                }
                if (checkTree2(seed) == 1) {
                    treeSeeds.add(seed);
                }
                if (checkTree3(seed) == 1) {
                    treeSeeds.add(seed);
                }
                if (checkTree4(seed) == 1) {
                    treeSeeds.add(seed);
                }
//                if (checkTree5(seed) == 1) {
//                    treeSeeds.add(seed);
//                }
            }

            // this is where we actually eliminate seeds

            // first we confirm all the trees actually made it thru
            if (treeSeeds.size() < TREE_COUNT) {
                continue mainSeedLoop;
            }

            System.out.println("passed tree count check");

            // create a list of the deltas between dfzs (i.e. the number of calls each tree made)
            // effectively this list starts at tree 0 and does not include the last tree,
            // because since we don't have a tree after that one we can't compare it to anything
            // but it should be enough anyway, we don't actually need all of them
            ArrayList<Long> dfzs = new ArrayList<>();
            long initialDfz = DiscreteLog.distanceFromZero(treeSeeds.get(0));
            long lastDfz = initialDfz;
            for (long treeSeed : treeSeeds) {
                if (treeSeed == treeSeeds.get(0)) {
                    continue;
                }
                long dfz = DiscreteLog.distanceFromZero(treeSeed);
                dfzs.add(dfz-lastDfz);
                lastDfz = dfz;
            }

            System.out.printf("dfzs for seed %15d:\n", seed);
            for (long dfz : dfzs) {
                System.out.printf("%15d\n", dfz);
            }

            // we checked in a range of -90..+90 around the primary tree,
            // but now we know which tree is first so we can eliminate matches
            // that ended more than 90 calls away from that first one
            if (DiscreteLog.distanceFromZero(treeSeeds.get(treeSeeds.size()-1)) - DiscreteLog.distanceFromZero(treeSeeds.get(0)) > 90) {
                continue mainSeedLoop;
            }

            System.out.println("passed max length test");

            // if any of the seeds overlap too much, it's not a real match
            for (long dfz : dfzs) {
                if (dfz < 6) {
                    // eliminate this seed
                    continue mainSeedLoop;
                }
            }

            System.out.println("passed partial dedodge");

            // full de-dodge
            // each seed must be exactly one of the valid tree distances.
            for (long dfz : dfzs) {
                boolean isValidDfz = false;
                for (int validDfz : validTaigaTreeDistances) {
                    if (dfz == validDfz) {
                        System.out.println("beef");
                        isValidDfz = true;
                    }
                }
                if (!isValidDfz) {
                    // eliminate this seed
                    continue mainSeedLoop;
                }
            }

            System.out.println("passed full dedodge");

            seedsPassed++;
        }

        System.out.printf("seeds passed: %d\n", seedsPassed);
    }

    public static int checkTree0(long seed) {
        if ((((seed *     25214903917L +              11L) >> 44) & 15) !=  5) return 0; // pos X
        if ((((seed * 205749139540585L +    277363943098L) >> 44) & 15) !=  2) return 0; // pos Z
        if ((((seed * 120950523281469L + 102626409374399L) >> 47) &  1) !=  1) return 0; // leaf height
        if (((((seed * 233752471717045L +  11718085204285L) & 281474976710655L) >> 17) %  3) !=  0) return 0; // type
        if (((((seed *  55986898099985L +  49720483695876L) & 281474976710655L) >> 17) %  5) !=  1) return 0; // height
        if (((((seed *  76790647859193L +  25707281917278L) & 281474976710655L) >> 17) %  5)  <  2) return 0; // radius > 1

        return 1;
    }

    public static int checkTree1(long seed) {
        if ((((seed *     25214903917L +              11L) >> 44) & 15) != 11) return 0; // pos X
        if ((((seed * 205749139540585L +    277363943098L) >> 44) & 15) !=  5) return 0; // pos Z
        if ((((seed * 120950523281469L + 102626409374399L) >> 47) &  1) !=  0) return 0; // leaf height
        if ((((seed *  76790647859193L +  25707281917278L) >> 46) &  3)  <  1) return 0; // radius > 1
        if (((((seed * 233752471717045L +  11718085204285L) & 281474976710655L) >> 17) %  3) !=  0) return 0; // type
        if (((((seed *  55986898099985L +  49720483695876L) & 281474976710655L) >> 17) %  5) !=  4) return 0; // height

        return 1;
    }

    public static int checkTree2(long seed) {
        if ((((seed *     25214903917L +              11L) >> 44) & 15) != 11) return 0; // pos X
        if ((((seed * 205749139540585L +    277363943098L) >> 44) & 15) !=  2) return 0; // pos Z
        if ((((seed *  55986898099985L +  49720483695876L) >> 46) &  3) !=  0) return 0; // height
        if ((((seed * 120950523281469L + 102626409374399L) >> 47) &  1) !=  1) return 0; // base height
        if ((((seed *  76790647859193L +  25707281917278L) >> 47) &  1) !=  0) return 0; // radius
        if ((((seed *  61282721086213L +  25979478236433L) >> 47) &  1) !=  0) return 0; // initial radius
        if (((((seed * 233752471717045L +  11718085204285L) & 281474976710655L) >> 17) %  3) ==  0) return 0; // type

        return 1;
    }

    public static int checkTree3(long seed) {
        if ((((seed *     25214903917L +              11L) >> 44) & 15) != 12) return 0; // pos X
        if ((((seed * 205749139540585L +    277363943098L) >> 44) & 15) != 11) return 0; // pos Z
        if ((((seed * 120950523281469L + 102626409374399L) >> 47) &  1) !=  0) return 0; // leaf height
        if ((((seed *  76790647859193L +  25707281917278L) >> 46) &  3)  <  1) return 0; // radius > 1
        if (((((seed * 233752471717045L +  11718085204285L) & 281474976710655L) >> 17) %  3) !=  0) return 0; // type
        if (((((seed *  55986898099985L +  49720483695876L) & 281474976710655L) >> 17) %  5) !=  1) return 0; // height

        return 1;
    }

    public static int checkTree4(long seed) {
        if ((((seed *     25214903917L +              11L) >> 44) & 15) !=  1) return 0; // pos X
        if ((((seed * 205749139540585L +    277363943098L) >> 44) & 15) != 15) return 0; // pos Z
        if ((((seed * 120950523281469L + 102626409374399L) >> 47) &  1) !=  0) return 0; // leaf height
        if ((((seed *  76790647859193L +  25707281917278L) >> 46) &  3) !=  0) return 0; // radius == 1
        if (((((seed * 233752471717045L +  11718085204285L) & 281474976710655L) >> 17) %  3) !=  0) return 0; // type
        if (((((seed *  55986898099985L +  49720483695876L) & 281474976710655L) >> 17) %  5) !=  2) return 0; // height

        return 1;
    }

    public static int checkTree5(long seed) {

        return 1;
    }

    public static final int[] validTaigaTreeDistances = {
            0,
            6,
            8,
            12,
            14,
            16,
            18,
            20,
            22,
            24,
            26,
            28,
            30,
            32,
            34,
            36,
            38,
            40,
            42,
            44,
            46,
            48,
            50,
            52,
            54,
            56,
            58,
            60,
            62,
            64,
            66,
            68,
            70,
            72,
            74,
            76,
            78,
            80,
            82,
            84,
            86,
            88,
            90,
    };
}
