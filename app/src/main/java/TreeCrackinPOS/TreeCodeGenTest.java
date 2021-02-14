package TreeCrackinPOS;

import kaptainwutax.seedutils.lcg.LCG;

import java.io.File;

public class TreeCodeGenTest {
    public static void main(String[] args) throws Exception {
        // SHOT P - CHUNK 7, 13
//        int[][] trees = {
//                {   5,   8 },
//                {   6,  15 },
//                {   0,  11 },
//                {  15,  15 },
//        };
//
//        char[] treeTypes = {
//                'o',
//                'b',
//                'o',
//                'o',
//        };
//
//        int[] treeHeights = {
//                5,
//                5,
//                4,
//                6,
//        };
//
//        char[][] treeLeaves = {
//                { 'u', 'u', 'u', 'l', 'u', 'u', 'u', 'n', 'l', 'u', 'n', 'l', },
//                { 'n', 'u', 'l', 'u', 'l', 'u', 'n', 'u', 'n', 'u', 'n', 'u', },
//                { 'l', 'u', 'u', 'n', 'n', 'u', 'u', 'l', 'n', 'u', 'l', 'l', },
//                { 'u', 'u', 'n', 'n', 'n', 'u', 'n', 'n', 'n', 'u', 'n', 'n', },
//        };
//
//        int[] knownLeaves = {
//
//        };

        // SHOT P - CHUNK 7, 12
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
//        char[][] treeLeaves = {
//                { 'l', 'u', 'l', 'u', 'n', 'u', 'l', 'n', 'l', 'u', 'l', 'l', },
//                { 'u', 'u', 'n', 'u', 'l', 'u', 'l', 'u', 'u', 'u', 'l', 'u', },
//        };
//
//        int[] knownLeaves = {
//                8,
//                4,
//        };

        // SEED 5 - CHUNK 0, 0
//        int[][] trees = {
//                {   1,   1 },
//                {   2,   8 },
//                {  15,  10 },
//                {  12,   2 },
//                {   7,   3 },
//                {   3,   4 },
//                {   0,  12 },
//        };
//
//        char[] treeTypes = {
//                'o',
//                'o',
//                'o',
//                'b',
//                'o',
//                'b',
//                'o',
//        };
//
//        int[] treeHeights = {
//                5,
//                4,
//                5,
//                5,
//                6,
//                6,
//                4,
//        };
//
//        char[][] treeLeaves = {
//                { 'l', 'n', 'n', 'u', 'n', 'l', 'l', 'u', 'l', 'l', 'n', 'u', },
//                { 'n', 'n', 'l', 'l', 'l', 'n', 'n', 'l', 'n', 'n', 'n', 'l', },
//                { 'n', 'l', 'l', 'l', 'n', 'n', 'l', 'l', 'l', 'n', 'n', 'u', },
//                { 'l', 'n', 'l', 'n', 'u', 'l', 'n', 'n', 'l', 'n', 'n', 'l', },
//                { 'l', 'l', 'u', 'u', 'l', 'l', 'u', 'u', 'l', 'u', 'u', 'u', },
//                { 'u', 'l', 'u', 'l', 'u', 'n', 'u', 'n', 'l', 'l', 'u', 'u', },
//                { 'u', 'n', 'u', 'l', 'u', 'n', 'u', 'n', 'l', 'n', 'n', 'l', },
//        };
//
//        int[] knownLeaves = {
//                9,
//                12,
//                12,
//                11,
//                5,
//                6,
//                8,
//        };

        // SEED 5 - CHUNK 0, 2
        int[][] trees = {
                {  13,   0 },
        };

        char[] treeTypes = {
                'o',
        };

        int[] treeHeights = {
                6,
        };

        char[][] treeLeaves = {
                { 'l', 'n', 'n', 'l', 'n', 'n', 'l', 'l', 'l', 'n', 'n', 'l', },
        };

        int[] knownLeaves = {
                12,
        };

        // remove existing files
        for (int i = 0; i < 11; i++) {
            File file = new File(String.format("tree%d.cl", i));
            if (file.exists()) {
                file.delete();
            }
        }
        for (int targetTree = 0; targetTree < trees.length; targetTree++) {
            String outfile = String.format("tree%d.cl", targetTree);
            TreeKernelGenerator kernelGen = new TreeKernelGenerator();
            kernelGen.rngCalls = 0;
//            kernelGen.addCheck(16, Comparison.EQUAL, trees[targetTree][0]);
//            kernelGen.addCheck(16, Comparison.EQUAL, trees[targetTree][1]);
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
            kernelGen.addCheck(3, Comparison.EQUAL, treeHeights[targetTree] - 4);
            // leaves
            for (int j = 0; j < 12; j++) {
                char leaf = treeLeaves[targetTree][j];
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

    static class TreeKernelGenerator {
        public long rngCalls;
        public long callOffset;
        public StringBuilder mainSb;
        public StringBuilder moduloSb;
        public boolean auxTree;

        public TreeKernelGenerator() {
            rngCalls = 0;
            callOffset = 0;
            mainSb = new StringBuilder();
            auxTree = false;
        }

        public void addCheck(int bound, Comparison comparison, int target) {
            rngCalls += 1;
            LCG lcg = LCG.JAVA.combine(rngCalls + callOffset);
            String comparisonStr = comparison.getInversion().toString();

            if ((bound & bound - 1) == 0) { // bound is a power of 2
                int rightShift = (int) (48 - Math.log(bound) / Math.log(2));
                int mask = bound - 1;

                String parameterized = String.format(
                        "if ((((seed * %15dLU + %15dLU) >> %2d) & %2d) %2s %2d) return;",
//                        rngCalls + callOffset, 0, // temporary test to make sure we're checking all seeds
                        lcg.multiplier,
                        lcg.addend,
                        rightShift,
                        mask,
                        comparisonStr,
                        target
                );
                mainSb.append(parameterized);
            } else { // non power of 2 bound
                int rightShift = 17;

                String parameterized = String.format(
                        "if ((((seed * %15dLU + %15dLU) >> %2d) %% %2d) %2s %2d) return;",
//                        rngCalls + callOffset, 0, // temporary test to make sure we're checking all seeds
                        lcg.multiplier,
                        lcg.addend,
                        rightShift,
                        bound,
                        comparisonStr,
                        target
                );
                mainSb.append(parameterized);
            }
            mainSb.append('\n');
        }

        public void addSkip(long steps) {
            rngCalls += steps;
        }

        public String toString() {
            mainSb.append(moduloSb);
            return mainSb.toString();
        }
    }

    enum Comparison {
        EQUAL,
        NOT_EQUAL,
        LESS_THAN,
        GREATER_THAN,
        LESS_THAN_OR_EQUAL,
        GREATER_THAN_OR_EQUAL;

        public Comparison getInversion() {
            switch (this) {
                case EQUAL:
                    return NOT_EQUAL;
                case NOT_EQUAL:
                    return EQUAL;
                case LESS_THAN:
                    return GREATER_THAN_OR_EQUAL;
                case GREATER_THAN:
                    return LESS_THAN_OR_EQUAL;
                case LESS_THAN_OR_EQUAL:
                    return GREATER_THAN;
                case GREATER_THAN_OR_EQUAL:
                    return LESS_THAN;
            }
            return null;
        }

        public String toString() {
            switch (this) {
                case EQUAL:
                    return "==";
                case NOT_EQUAL:
                    return "!=";
                case LESS_THAN:
                    return "<";
                case GREATER_THAN:
                    return ">";
                case LESS_THAN_OR_EQUAL:
                    return "<=";
                case GREATER_THAN_OR_EQUAL:
                    return ">=";
            }
            return null;
        }
    }

    // unused
    enum TreeType {
        OAK,
        BIRCH,
        BIG_OAK,
        SPRUCE
    }
}
