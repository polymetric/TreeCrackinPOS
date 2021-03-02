package TreeCrackinPOS.post_processing;

import TreeCrackinPOS.utils.Utils;
import kaptainwutax.seedutils.lcg.LCG;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

public class DeDodgeTrees {
    // takes the output of the tree kernel (primary tree seeds that matched aux tree seeds as well)
    // and gets the aux tree seeds back out
    public static void main(String[] args) throws Exception {
        final int TARGET_TREE_COUNT = 5;
        final String seedsInPath = "treeseeds_taigatest_2.txt";
        final String seedsOutPath = "treeseeds_taigatest_2_dedodged.txt";

        final int TREE_CALL_RANGE = 90;
        final LCG revTCR = LCG.JAVA.combine(-TREE_CALL_RANGE);

        String[] seedsIn = Utils.readFileToString(seedsInPath).split("\n");
//        String[] seedsIn = {
//                "262035261123315",
//        }; // DEBUG

        File seedsOutFile = new File(seedsOutPath);
        seedsOutFile.delete();
        seedsOutFile.createNewFile();
        FileWriter seedsOutStream = new FileWriter(seedsOutPath);

        long timeStart = System.currentTimeMillis();

        int seedsPassed = 0;
        mainSeedLoop:
        for (int i = 0; i < seedsIn.length; i++) {
            long seed = Long.parseLong(seedsIn[i].trim());
            long initialSeed = seed;

            ArrayList<Long> treeSeeds = new ArrayList<>();

            // refind tree seeds
            seed = revTCR.nextSeed(seed);
            for (int callOffset = 0; callOffset < TREE_CALL_RANGE * 2; callOffset++) {
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
                if (checkTree5(seed) == 1) {
                    treeSeeds.add(seed);
                }

                seed = LCG.JAVA.nextSeed(seed);
            }

            if (treeSeeds.size() < TARGET_TREE_COUNT) {
                continue mainSeedLoop;
            }

            // full de-dodge
            // each seed must be exactly one of the valid tree distances.
            // this may end up leaving a few duplicates in there but the goal is
            // obviously not to eliminate any valid seeds.
            ArrayList<Long> treeSeedsToRemove = new ArrayList<>();
            long initialDfz = LCG.JAVA.distance(0, treeSeeds.get(0));
            long lastDfz = initialDfz;
            dedodgeLoop:
            for (int j = 0; j < treeSeeds.size(); j++) {
                long treeSeed = treeSeeds.get(j);
                long dfz = LCG.JAVA.distance(0, treeSeed);
                // we loop thru all the valid tree distances
                // if we hit a valid dfz, then this might be a real tree
                // so we keep it
                for (int validDfz : validTaigaTreeDistances) {
                    if (dfz - lastDfz == validDfz) {
                        lastDfz = dfz;
                        continue dedodgeLoop;
                    }
                }
                // if our current dfz is not a valid one, it's probably not a real tree
                // so we remove it
                treeSeedsToRemove.add(treeSeed);
            }
            // remove any seeds we eliminated from the list
            for (long l : treeSeedsToRemove) {
                treeSeeds.remove(l);
            }
            // check that we still have enough trees to be considered
            // a match
            if (treeSeeds.size() < TARGET_TREE_COUNT) {
                continue mainSeedLoop;
            }
//            System.out.println("passed full dedodge");

            // we checked in a range of -90..+90 around the primary tree,
            // but now we know which tree is first so we can eliminate matches
            // that ended more than 90 calls away from that first one
            if (LCG.JAVA.distance(treeSeeds.get(0), treeSeeds.get(treeSeeds.size()-1)) > TREE_CALL_RANGE) {
                continue mainSeedLoop;
            }
//            System.out.println("passed max distance check");

            seedsOutStream.write(initialSeed + "\n");

            // DEBUG
            System.out.printf("DFZs for seed %15d\n", initialSeed);
            lastDfz = initialDfz;
            for (int j = 0; j < treeSeeds.size(); j++) {
                long treeSeed = treeSeeds.get(j);
                long dfz = LCG.JAVA.distance(0, treeSeed);
                System.out.printf("%4d\n", dfz-lastDfz);
                lastDfz = dfz;
            }

            seedsPassed++;
        }
        seedsOutStream.flush();

        System.out.printf("seeds passed: %d\n", seedsPassed);
        System.out.printf("checked %d seeds in %.3fs\n", seedsIn.length, (System.currentTimeMillis() - timeStart) / 1000F);
    }

    public static int checkTree0(long seed) {
// tree 0
        if ((((seed *     25214903917L +              11L) >> 44) & 15) != 10) return 0; // pos X
        if ((((seed * 205749139540585L +    277363943098L) >> 44) & 15) !=  5) return 0; // pos Z
        if ((((seed * 120950523281469L + 102626409374399L) >> 47) &  1) !=  1) return 0; // base height
        if (((((seed * 233752471717045L +  11718085204285L) & 281474976710655L) >> 17) %  3) !=  0) return 0; // type
        if (((((seed *  55986898099985L +  49720483695876L) & 281474976710655L) >> 17) %  5) !=  4) return 0; // height
        if (((((seed *  76790647859193L +  25707281917278L) & 281474976710655L) >> 17) %  5)  <  2) return 0; // radius > 1

        return 1;
    }

    public static int checkTree1(long seed) {
// tree 1
        if ((((seed *     25214903917L +              11L) >> 44) & 15) != 15) return 0; // pos X
        if ((((seed * 205749139540585L +    277363943098L) >> 44) & 15) !=  1) return 0; // pos Z
        if ((((seed *  55986898099985L +  49720483695876L) >> 46) &  3) !=  0) return 0; // height
        if ((((seed * 120950523281469L + 102626409374399L) >> 47) &  1) !=  1) return 0; // base height
        if ((((seed *  61282721086213L +  25979478236433L) >> 47) &  1) !=  1) return 0; // initial radius
        if (((((seed * 233752471717045L +  11718085204285L) & 281474976710655L) >> 17) %  3) ==  0) return 0; // type

        return 1;
    }

    public static int checkTree2(long seed) {
// tree 2
        if ((((seed *     25214903917L +              11L) >> 44) & 15) != 15) return 0; // pos X
        if ((((seed * 205749139540585L +    277363943098L) >> 44) & 15) !=  5) return 0; // pos Z
        if ((((seed *  55986898099985L +  49720483695876L) >> 46) &  3) !=  1) return 0; // height
        if ((((seed * 120950523281469L + 102626409374399L) >> 47) &  1) !=  1) return 0; // base height
        if ((((seed *  61282721086213L +  25979478236433L) >> 47) &  1) !=  0) return 0; // initial radius
        if (((((seed * 233752471717045L +  11718085204285L) & 281474976710655L) >> 17) %  3) ==  0) return 0; // type

        return 1;
    }

    public static int checkTree3(long seed) {
// tree 3
        if ((((seed *     25214903917L +              11L) >> 44) & 15) !=  6) return 0; // pos X
        if ((((seed * 205749139540585L +    277363943098L) >> 44) & 15) != 10) return 0; // pos Z
        if ((((seed *  55986898099985L +  49720483695876L) >> 46) &  3) !=  2) return 0; // height
        if ((((seed * 120950523281469L + 102626409374399L) >> 47) &  1) !=  1) return 0; // base height
        if ((((seed *  61282721086213L +  25979478236433L) >> 47) &  1) !=  0) return 0; // initial radius
        if (((((seed * 233752471717045L +  11718085204285L) & 281474976710655L) >> 17) %  3) ==  0) return 0; // type

        return 1;
    }

    public static int checkTree4(long seed) {
// tree 4
        if ((((seed *     25214903917L +              11L) >> 44) & 15) !=  0) return 0; // pos X
        if ((((seed * 205749139540585L +    277363943098L) >> 44) & 15) != 11) return 0; // pos Z
        if ((((seed * 120950523281469L + 102626409374399L) >> 47) &  1) !=  1) return 0; // base height
        if (((((seed * 233752471717045L +  11718085204285L) & 281474976710655L) >> 17) %  3) !=  0) return 0; // type
        if (((((seed *  55986898099985L +  49720483695876L) & 281474976710655L) >> 17) %  5) !=  1) return 0; // height
        if (((((seed *  76790647859193L +  25707281917278L) & 281474976710655L) >> 17) %  5)  <  2) return 0; // radius > 1

        return 1;
    }

    public static int checkTree5(long seed) {
// tree 5
        if ((((seed *     25214903917L +              11L) >> 44) & 15) != 11) return 0; // pos X
        if ((((seed * 205749139540585L +    277363943098L) >> 44) & 15) != 15) return 0; // pos Z
        if ((((seed * 120950523281469L + 102626409374399L) >> 47) &  1) !=  0) return 0; // base height
        if ((((seed *  76790647859193L +  25707281917278L) >> 46) &  3)  <  1) return 0; // radius > 1
        if (((((seed * 233752471717045L +  11718085204285L) & 281474976710655L) >> 17) %  3) !=  0) return 0; // type
        if (((((seed *  55986898099985L +  49720483695876L) & 281474976710655L) >> 17) %  5) !=  1) return 0; // height

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
