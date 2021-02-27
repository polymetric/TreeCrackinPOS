package TreeCrackinPOS;

import TreeCrackinPOS.codegen.Comparison;
import TreeCrackinPOS.codegen.LCGCheckerCodeGen;

import java.io.File;

public class TaigaCodeGen {
    public static void main(String[] args) throws Exception {
        // SHOT N - CHUNK -7, 22
//        final boolean CHECK_X_AND_Z = false;
//        final int PRIMARY_TREE = 0;
//        int[][] trees = {
//                {   9,  13 },
//                {   8,  10 },
//                {   9,   5 },
//                {   4,   3 },
//                {   6,  13 },
//                {  13,   8 },
//        };
//
//        char[] treeTypes = {
//                '1',
//                '2',
//                '2',
//                '1',
//                '2',
//                '1',
//        };
//
//        int[] treeTotalHeights = {
//                9,
//                7,
//                8,
//                10,
//                0,
//                0,
//        };
//
//        int[] treeBaseHeights = {
//                6,
//                1,
//                2,
//                6,
//                1,
//                -1,
//        };
//
//        int[] treeLeafHeights = {
//                3,
//                -1,
//                -1,
//                4,
//                -1,
//                -1,
//        };
//
//        int[] treeRadiuses = {
//                2,
//                2,
//                2,
//                1,
//                2,
//                -1,
//        };
//
//        int[] treeInitialRadiuses = {
//                -1,
//                0,
//                0,
//                -1,
//                -1,
//                -1,
//        };
//
//        int[] treeTopLeaves = {
//                -1,
//                -1,
//                -1,
//                -1,
//                -1,
//                -1,
//        };

        // TEST SEED CHUNK
        final boolean CHECK_X_AND_Z = true;
        final int PRIMARY_TREE = 0;
        int[][] trees = {
                {   5,   2 },
                {  11,   5 },
                {  11,   2 },
                {  12,  11 },
                {   1,  15 },
        };

        char[] treeTypes = {
                '1',
                '1',
                '2',
                '1',
                '1',
        };

        int[] treeTotalHeights = {
                8,
                11,
                6,
                8,
                9,
        };

        int[] treeBaseHeights = {
                -1,
                -1,
                2,
                -1,
                -1,
        };

        int[] treeLeafHeights = {
                4,
                3,
                -1,
                3,
                3,
        };

        int[] treeRadiuses = {
                3,
                2,
                2,
                2,
                1,
        };

        int[] treeInitialRadiuses = {
                -1,
                -1,
                0,
                -1,
                -1,
        };

        int[] treeTopLeaves = {
                -1,
                -1,
                -1,
                -1,
                -1,
        };






        // remove existing files
        for (int i = 0; i < 11; i++) {
            File file = new File(String.format("tree%d.cl", i));
            if (file.exists()) {
                file.delete();
            }
        }


        int treeId = 0;
        for (int targetTree = 0; targetTree < trees.length; targetTree++) {
//            System.out.printf("DEBUG GENERATING TREE %d\n", targetTree);
            if (targetTree != PRIMARY_TREE) {
                System.out.printf("check_tree(%d, %2d, %2d);\n", treeId, trees[targetTree][0], trees[targetTree][1]);
                treeId++;
            }

            String outfile = String.format("tree%d.cl", targetTree);
            LCGCheckerCodeGen kernelGen = new LCGCheckerCodeGen();
            if (targetTree == PRIMARY_TREE) {
                kernelGen.returnValue = "";
            } else {
                kernelGen.returnValue = "0";
            }
            kernelGen.rngCalls = 0;



            // position checks
            if (CHECK_X_AND_Z) { // mainly meant for debugging
                kernelGen.addComment("pos X");
                kernelGen.addCheck(16, Comparison.EQUAL, trees[targetTree][0]); // check X position
                kernelGen.addComment("pos Z");
                kernelGen.addCheck(16, Comparison.EQUAL, trees[targetTree][1]); // check Z position
            } else if (targetTree == PRIMARY_TREE) {
                // we don't check X position because the kernel already uses that to determine
                // the first 4 bits of the seed
//                kernelGen.addCheck(16, Comparison.EQUAL, trees[targetTree][0]); // check X position
                kernelGen.addComment("pos Z");
                kernelGen.addCheck(16, Comparison.EQUAL, trees[targetTree][1]); // check Z position
            } else {
                // if this is meant for the GPU kernel, we don't check X or Z because the kernel
                // already does that
//                kernelGen.addCheck(16, Comparison.EQUAL, trees[targetTree][0]); // check X position
//                kernelGen.addCheck(16, Comparison.EQUAL, trees[targetTree][1]); // check Z position
            }

            // type check
            if (treeTypes[targetTree] != '1' && treeTypes[targetTree] != '2') {
                throw new Exception(String.format("invalid tree type %c (tree %d)", treeTypes[targetTree], targetTree));
            }
            kernelGen.addComment("type");
            switch (treeTypes[targetTree]) {
                case '1': // taiga type 1
                    kernelGen.addCheck(3, Comparison.EQUAL, 0);
                    break;
                case '2': // taiga type 2
                    kernelGen.addCheck(3, Comparison.NOT_EQUAL, 0);
                    break;
            }

            // height check
            // sometimes height is unknown
            if (treeTotalHeights[targetTree] != 0) {
                switch (treeTypes[targetTree]) {
                    case '1': // taiga type 1
                        kernelGen.addComment("height");
//                        System.out.printf("height %d\n", treeTotalHeights[targetTree] - 7);
                        kernelGen.addCheck(5, Comparison.EQUAL, treeTotalHeights[targetTree] - 7);
                        break;
                    case '2': // taiga type 2
                        kernelGen.addComment("height");
//                        System.out.printf("height %d\n", treeTotalHeights[targetTree] - 6);
                        kernelGen.addCheck(4, Comparison.EQUAL, treeTotalHeights[targetTree] - 6);
                        break;
                }
            } else {
                kernelGen.addSkip(1);
            }

            // leaf checks
            switch (treeTypes[targetTree]) {
                case '1': // taiga type 1
                    // leaf height
                    if (treeLeafHeights[targetTree] != -1) {
//                        System.out.printf("leaf height %d\n", treeLeafHeights[targetTree] - 3);
                        kernelGen.addComment("leaf height");
                        kernelGen.addCheck(2, Comparison.EQUAL, treeLeafHeights[targetTree] - 3);
                    } else {
                        kernelGen.addSkip(1);
                    }
                    // radius limit
                    if (treeRadiuses[targetTree] != -1) {
//                        System.out.printf("radius %d\n", treeRadiuses[targetTree]);
                        if (treeRadiuses[targetTree] == 1) {
                            kernelGen.addComment("radius == 1");
                            kernelGen.addCheck(treeLeafHeights[targetTree] + 1, Comparison.EQUAL, treeRadiuses[targetTree] - 1);
                        } else {
                            kernelGen.addComment("radius > 1");
                            kernelGen.addCheck(treeLeafHeights[targetTree] + 1, Comparison.GREATER_THAN_OR_EQUAL, treeRadiuses[targetTree] - 1);
                        }
                    } else {
                        kernelGen.addSkip(1);
                    }
                    break;
                case '2': // taiga type 2
                    // base height
                    if (treeBaseHeights[targetTree] != -1) {
                        kernelGen.addComment("base height");
//                        System.out.printf("base height %d\n", treeBaseHeights[targetTree] - 1);
                        kernelGen.addCheck(2, Comparison.EQUAL, treeBaseHeights[targetTree] - 1);
                    } else {
                        kernelGen.addSkip(1);
                    }
                    // max radius limit
                    if (treeRadiuses[targetTree] != -1) {
                        kernelGen.addComment("radius");
//                        System.out.printf("radius %d\n", treeRadiuses[targetTree] - 2);
                        kernelGen.addCheck(2, Comparison.EQUAL, treeRadiuses[targetTree] - 2);
                    } else {
                        kernelGen.addSkip(1);
                    }
                    // initial radius
                    if (treeInitialRadiuses[targetTree] != -1) {
                        kernelGen.addComment("initial radius");
//                        System.out.printf("initial radius %d\n", treeInitialRadiuses[targetTree]);
                        kernelGen.addCheck(2, Comparison.EQUAL, treeInitialRadiuses[targetTree]);
                    } else {
                        kernelGen.addSkip(1);
                    }
                    // top leaves
                    if (treeTopLeaves[targetTree] != -1) {
                        kernelGen.addComment("top leaves");
//                        System.out.printf("top leaves %d\n", treeTopLeaves[targetTree]);
                        kernelGen.addCheck(2, Comparison.EQUAL, treeTopLeaves[targetTree]);
                    } else {
                        kernelGen.addSkip(1);
                    }
                    break;
            }

            Utils.writeStringToFile(outfile, kernelGen.toString());
//            System.out.println("total call checks: " + kernelGen.rngCalls);
        }
    }
}
