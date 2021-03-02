package TreeCrackinPOS;

import TreeCrackinPOS.codegen.Comparison;
import TreeCrackinPOS.codegen.LCGCheckerCodeGen;
import TreeCrackinPOS.utils.Utils;

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
//                {  13,   2 },
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
        final boolean CHECK_X_AND_Z = false;
        final int PRIMARY_TREE = 0;
        int[][] trees = {
                {  10,   5 },
                {  15,   1 },
                {  15,   5 },
                {   6,  10 },
                {   0,  11 },
                {  11,  15 },
        };

        char[] treeTypes = {
                '1',
                '2',
                '2',
                '2',
                '1',
                '1',
        };

        int[] treeTotalHeights = {
                11,
                6,
                7,
                8,
                8,
                8,
        };

        int[] treeBaseHeights = {
                7,
                2,
                2,
                2,
                4,
                5,
        };

        int[] treeRadiuses = {
                3,
                2,
                2,
                2,
                3,
                2,
        };

        int[] treeInitialRadiuses = {
                -1,
                1,
                0,
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
            if (targetTree == PRIMARY_TREE && !CHECK_X_AND_Z) {
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
                    int leafHeight = treeTotalHeights[targetTree] - treeBaseHeights[targetTree];
                    if (leafHeight != -1) {
//                        System.out.printf("base height %d\n", treeLeafHeights[targetTree] - 3);
                        kernelGen.addComment("base height");
                        kernelGen.addCheck(2, Comparison.EQUAL, leafHeight - 3);
                    } else {
                        kernelGen.addSkip(1);
                    }
                    // radius limit
                    if (treeRadiuses[targetTree] != -1) {
//                        System.out.printf("radius %d\n", treeRadiuses[targetTree]);
                        if (treeRadiuses[targetTree] == 1) {
                            kernelGen.addComment("radius == 1");
                            kernelGen.addCheck(leafHeight + 1, Comparison.EQUAL, treeRadiuses[targetTree] - 1);
                        } else {
                            kernelGen.addComment("radius > 1");
                            kernelGen.addCheck(leafHeight + 1, Comparison.GREATER_THAN_OR_EQUAL, treeRadiuses[targetTree] - 1);
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
                        if (treeRadiuses[targetTree] < 3) {
                            // if the above is true, then the physical radius of the tree never
                            // got a chance to hit the maximum radius, so we don't actually know
                            // what the RNG value is
                            kernelGen.addSkip(1);
                        } else {
                            kernelGen.addCheck(2, Comparison.EQUAL, treeRadiuses[targetTree] - 2);
                        }
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
                        kernelGen.addCheck(3, Comparison.EQUAL, treeTopLeaves[targetTree]);
                    } else {
                        kernelGen.addSkip(1);
                    }
                    break;
            }

            Utils.writeStringToFile(outfile, "// tree " + targetTree + "\n" + kernelGen.toString());
//            System.out.println("total call checks: " + kernelGen.rngCalls);
        }

        System.out.println();
        System.out.printf("total trees: %4d\n", trees.length);
        System.out.printf("aux trees:   %4d\n", trees.length-1);
    }
}
