package TreeCrackinPOS;

import TreeCrackinPOS.treecodegen.Comparison;
import TreeCrackinPOS.treecodegen.LCGCheckerCodeGen;

import java.io.File;

public class TreeCodeGenTest {
    public static void main(String[] args) throws Exception {
        // SHOT P - CHUNK 7, 13
        final boolean CHECK_X_AND_Z = false;
        final int PRIMARY_TREE = 3;
        int[][] trees = {
                {  13,   0 },
                {  14,   7 },
                {   8,   3 },
                {   7,   7 }, // primary
                {  11,  12 },
                {   2,  14 },
        };

        char[] treeTypes = {
                'o',
                'b',
                'o',
                'o',
                'o',
                'o',
        };

        int[] treeHeights = {
                6,
                5,
                0,
                0,
                0,
                0,
        };

        String[] treeLeaves = {
                "uuuluuunuunl",
                "nululunununu",
                "uuunnuulnuul",
                "uunununnnunn",
                "uuuuluuuuuuu",
                "uulununununu",
        };

        int[] knownLeaves = {
                4,
                6,
                4,
                7,
                1,
                3,
        };

        // SHOT P - CHUNK 7, 12
//        final boolean CHECK_X_AND_Z = true;
//        final int PRIMARY_TREE = 0;
//        int[][] trees = {
//                {  14,  10 },
//                {  10,   8 },
//        };
//
//        char[] treeTypes = {
//                'b',
//                'o',
//        };
//
//        int[] treeHeights = {
//                6,
//                5,
//        };
//
//        String[] treeLeaves = {
//                "lulunulnlull",
//                "uunululuuulu",
//        };
//
//
//        int[] knownLeaves = {
//                8,
//                4,
//        };

        // TEST SEED
//        final boolean CHECK_X_AND_Z = false;
//        final int PRIMARY_TREE = 5;
//        int[][] trees = {
//                {   9,   2 },
//                {   6,   5 },
//                {  12,   6 },
//                {   4,   8 },
//                {   2,  11 },
//                {   7,  14 },
//        };
//
//        char[] treeTypes = {
//                'o',
//                'o',
//                'o',
//                'b',
//                'o',
//                'o',
//        };
//
//        int[] treeHeights = {
//                6,
//                6,
//                5,
//                5,
//                4,
//                6,
//        };
//
//        String[] treeLeaves = {
//                "nulululnlulu",
//                "nuuuluunllun",
//                "uunuulnunlnu",
//                "luuunuullulu",
//                "nuuuluunnuuu",
//                "nululnlllulu",
//        };
//
//        int[] knownLeaves = {
//                7,
//                6,
//                6,
//                5,
//                5,
//                8,
//        };

        // remove existing files
        for (int i = 0; i < 11; i++) {
            File file = new File(String.format("tree%d.cl", i));
            if (file.exists()) {
                file.delete();
            }
        }
        int treeId = 0;
        for (int targetTree = 0; targetTree < trees.length; targetTree++) {
            if (targetTree == PRIMARY_TREE) {
                continue;
            }
            System.out.printf("check_tree(%d, %2d, %2d);\n", treeId, trees[targetTree][0], trees[targetTree][1]);
            treeId++;
        }
        for (int targetTree = 0; targetTree < trees.length; targetTree++) {
            String outfile = String.format("tree%d.cl", targetTree);
            LCGCheckerCodeGen kernelGen = new LCGCheckerCodeGen();
            kernelGen.rngCalls = 0;
            if (CHECK_X_AND_Z) {
                kernelGen.addCheck(16, Comparison.EQUAL, trees[targetTree][0]); // check X position
                kernelGen.addCheck(16, Comparison.EQUAL, trees[targetTree][1]); // check Z position
            } else if (targetTree == PRIMARY_TREE) {
                // we don't check X position because the kernel already uses that to determine
                // the first 4 bits of the seed
//                kernelGen.addCheck(16, Comparison.EQUAL, trees[targetTree][0]); // check X position
                kernelGen.addCheck(16, Comparison.EQUAL, trees[targetTree][1]); // check Z position
            } else {
                // if this is meant for the GPU kernel, we don't check X or Z because the kernel
                // already does that
//                kernelGen.addCheck(16, Comparison.EQUAL, trees[targetTree][0]); // check X position
//                kernelGen.addCheck(16, Comparison.EQUAL, trees[targetTree][1]); // check Z position
            }
            switch (treeTypes[targetTree]) {
                case 'o': // oak
                    kernelGen.addCheck(5, Comparison.NOT_EQUAL, 0);
                    kernelGen.addCheck(10, Comparison.NOT_EQUAL, 0);
                    break;
                case 'b': // birch
                    kernelGen.addCheck(5, Comparison.EQUAL, 0);
                    break;
                case 'B': // big oak
                    kernelGen.addCheck(5, Comparison.EQUAL, 0);
                    kernelGen.addCheck(10, Comparison.EQUAL, 0);
                    break;
            }
            if (treeHeights[targetTree] != 0) {
                if (treeTypes[targetTree] == 'o') {
                    kernelGen.addCheck(3, Comparison.EQUAL, treeHeights[targetTree] - 4);
                }
                if (treeTypes[targetTree] == 'b') {
                    kernelGen.addCheck(3, Comparison.EQUAL, treeHeights[targetTree] - 5);
                }
            } else {
                kernelGen.addSkip(1);
            }
            // leaves
            for (int j = 0; j < 12; j++) {
                char leaf = treeLeaves[targetTree].charAt(j);
                // if leaf is not unsure
                if (leaf != 'u') {
                    if (leaf == 'l') {
                        kernelGen.addCheck(2, Comparison.EQUAL, 1);
                    } else {
                        kernelGen.addCheck(2, Comparison.EQUAL, 0);
                    }
                } else {
                    kernelGen.addSkip(1);
                }
            }
            kernelGen.addSkip(4);
            Utils.writeStringToFile(outfile, kernelGen.toString());
        }
//        System.out.println("total call checks per seed: " + kernelGen.rngCalls);

    }
}
