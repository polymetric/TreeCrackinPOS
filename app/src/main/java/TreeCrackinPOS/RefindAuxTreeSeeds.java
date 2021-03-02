package TreeCrackinPOS;

import TreeCrackinPOS.utils.Utils;
import kaptainwutax.seedutils.lcg.LCG;

import java.io.File;
import java.io.FileWriter;

public class RefindAuxTreeSeeds {
    // takes the output of the tree kernel (primary tree seeds that matched aux tree seeds as well)
    // and gets the aux tree seeds back out
    public static void main(String[] args) throws Exception {
        final int TREE_COUNT = 6;
        final String seedsInPath = "treeseeds_shotn_sorted.txt";
        final String seedsOutPath = "treeseeds_shotn_refound.txt";

        final int TREE_CALL_RANGE = 90;
        final LCG revTCR = LCG.JAVA.combine(-TREE_CALL_RANGE);

        String[] seedsIn = Utils.readFileToString(seedsInPath).split("\n");

        File seedsOutFile = new File(seedsOutPath);
        seedsOutFile.delete();
        seedsOutFile.createNewFile();
        FileWriter seedsOutStream = new FileWriter(seedsOutPath);

        for (int i = 0; i < seedsIn.length; i++) {
            long seed = Long.parseLong(seedsIn[i].trim());

            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(String.format("subseeds_for_seed %15d\n", seed));

            seed = revTCR.nextSeed(seed);
            int treesFound = 0;
            for (int callOffset = 0; callOffset < TREE_CALL_RANGE * 2; callOffset++) {
                seed = LCG.JAVA.nextSeed(seed);

                if (checkTree0(seed) == 1) {
                    stringBuilder.append(String.format("%15d tree 0\n", seed));
                    treesFound++;
                }
                if (checkTree1(seed) == 1) {
                    stringBuilder.append(String.format("%15d tree 1\n", seed));
                    treesFound++;
                }
                if (checkTree2(seed) == 1) {
                    stringBuilder.append(String.format("%15d tree 2\n", seed));
                    treesFound++;
                }
                if (checkTree3(seed) == 1) {
                    stringBuilder.append(String.format("%15d tree 3\n", seed));
                    treesFound++;
                }
                if (checkTree4(seed) == 1) {
                    stringBuilder.append(String.format("%15d tree 4\n", seed));
                    treesFound++;
                }
                if (checkTree5(seed) == 1) {
                    stringBuilder.append(String.format("%15d tree 5\n", seed));
                    treesFound++;
                }
            }

            if (treesFound == TREE_COUNT) {
                seedsOutStream.write(stringBuilder.toString());
                seedsOutStream.flush();
            }
        }
    }

    public static int checkTree0(long seed) {
        if ((((seed *     25214903917L +              11L) >> 44) & 15) !=  9) return 0; // pos X
        if ((((seed * 205749139540585L +    277363943098L) >> 44) & 15) != 13) return 0; // pos Z
        if ((((seed * 120950523281469L + 102626409374399L) >> 47) &  1) !=  0) return 0; // leaf height
        if ((((seed *  76790647859193L +  25707281917278L) >> 46) &  3)  <  1) return 0; // radius > 1
        if (((((seed * 233752471717045L +  11718085204285L) & 281474976710655L) >> 17) %  3) !=  0) return 0; // type
        if (((((seed *  55986898099985L +  49720483695876L) & 281474976710655L) >> 17) %  5) !=  2) return 0; // height

        return 1;
    }

    public static int checkTree1(long seed) {
        if ((((seed *     25214903917L +              11L) >> 44) & 15) !=  8) return 0; // pos X
        if ((((seed * 205749139540585L +    277363943098L) >> 44) & 15) != 10) return 0; // pos Z
        if ((((seed *  55986898099985L +  49720483695876L) >> 46) &  3) !=  1) return 0; // height
        if ((((seed * 120950523281469L + 102626409374399L) >> 47) &  1) !=  0) return 0; // base height
        if ((((seed *  76790647859193L +  25707281917278L) >> 47) &  1) !=  0) return 0; // radius
        if ((((seed *  61282721086213L +  25979478236433L) >> 47) &  1) !=  0) return 0; // initial radius
        if (((((seed * 233752471717045L +  11718085204285L) & 281474976710655L) >> 17) %  3) ==  0) return 0; // type

        return 1;
    }

    public static int checkTree2(long seed) {
        if ((((seed *     25214903917L +              11L) >> 44) & 15) !=  9) return 0; // pos X
        if ((((seed * 205749139540585L +    277363943098L) >> 44) & 15) !=  5) return 0; // pos Z
        if ((((seed *  55986898099985L +  49720483695876L) >> 46) &  3) !=  2) return 0; // height
        if ((((seed * 120950523281469L + 102626409374399L) >> 47) &  1) !=  1) return 0; // base height
        if ((((seed *  76790647859193L +  25707281917278L) >> 47) &  1) !=  0) return 0; // radius
        if ((((seed *  61282721086213L +  25979478236433L) >> 47) &  1) !=  0) return 0; // initial radius
        if (((((seed * 233752471717045L +  11718085204285L) & 281474976710655L) >> 17) %  3) ==  0) return 0; // type

        return 1;
    }

    public static int checkTree3(long seed) {
        if ((((seed *     25214903917L +              11L) >> 44) & 15) !=  4) return 0; // pos X
        if ((((seed * 205749139540585L +    277363943098L) >> 44) & 15) !=  3) return 0; // pos Z
        if ((((seed * 120950523281469L + 102626409374399L) >> 47) &  1) !=  1) return 0; // leaf height
        if (((((seed * 233752471717045L +  11718085204285L) & 281474976710655L) >> 17) %  3) !=  0) return 0; // type
        if (((((seed *  55986898099985L +  49720483695876L) & 281474976710655L) >> 17) %  5) !=  3) return 0; // height
        if (((((seed *  76790647859193L +  25707281917278L) & 281474976710655L) >> 17) %  5) !=  0) return 0; // radius == 1

        return 1;
    }

    public static int checkTree4(long seed) {
        if ((((seed *     25214903917L +              11L) >> 44) & 15) !=  6) return 0; // pos X
        if ((((seed * 205749139540585L +    277363943098L) >> 44) & 15) != 13) return 0; // pos Z
        if ((((seed * 120950523281469L + 102626409374399L) >> 47) &  1) !=  0) return 0; // base height
        if ((((seed *  76790647859193L +  25707281917278L) >> 47) &  1) !=  0) return 0; // radius
        if (((((seed * 233752471717045L +  11718085204285L) & 281474976710655L) >> 17) %  3) ==  0) return 0; // type

        return 1;
    }

    public static int checkTree5(long seed) {
        if ((((seed *     25214903917L +              11L) >> 44) & 15) != 13) return 0; // pos X
        if ((((seed * 205749139540585L +    277363943098L) >> 44) & 15) !=  8) return 0; // pos Z
        if (((((seed * 233752471717045L +  11718085204285L) & 281474976710655L) >> 17) %  3) !=  0) return 0; // type

        return 1;
    }
}
