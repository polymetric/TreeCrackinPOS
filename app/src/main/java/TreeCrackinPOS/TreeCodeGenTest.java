package TreeCrackinPOS;

import kaptainwutax.seedutils.lcg.LCG;

public class TreeCodeGenTest {
    public static void main(String[] args) throws Exception {
        final int SEEDS_PER_KERNEL = 1;
        final int TREE_COUNT = 7;
        final int TARGET_TREE = 1;

        final String outfile = String.format("tree%d.cl", TARGET_TREE);

        int[][] trees = {
                {1, 1}, // 0
                {2, 8}, // 1
                {15, 10}, // 2
                {12, 2}, // 3
                {7, 3}, // 4
                {3, 4}, // 5
                {0, 12}, // 6
        };

        char[] treeTypes = {
                'o', // 0
                'o', // 1
                'o', // 2
                'b', // 3
                'o', // 4
                'b', // 5
                'o', // 6
        };

        int[] treeHeights = {
                5, // 0
                4, // 1
                5, // 2
                5, // 3
                6, // 4
                6, // 5
                4, // 6
        };

        char[][] treeLeaves = {
                {'l', 'n', 'n', 'u', 'n', 'l', 'l', 'u', 'l', 'l', 'n', 'u',}, // 0
                {'n', 'n', 'l', 'l', 'l', 'n', 'n', 'l', 'n', 'n', 'n', 'l',}, // 1
                {'n', 'l', 'l', 'l', 'n', 'n', 'l', 'l', 'l', 'n', 'n', 'l',}, // 2
                {'l', 'n', 'l', 'n', 'u', 'l', 'n', 'n', 'l', 'n', 'n', 'l',}, // 3
                {'l', 'l', 'u', 'u', 'l', 'l', 'u', 'u', 'l', 'u', 'u', 'u',}, // 4
                {'u', 'l', 'u', 'l', 'u', 'n', 'u', 'n', 'l', 'l', 'u', 'u',}, // 5
                {'u', 'n', 'u', 'l', 'u', 'n', 'u', 'n', 'l', 'n', 'n', 'l',}, // 6
        };

        int[] sureLeaves = {
                9,  // 0
                12, // 1
                12, // 2
                11, // 3
                5,  // 4
                6,  // 5
                8,  // 6
        };

        TreeKernelGenerator kernelGen = new TreeKernelGenerator();
        // for every seed this kernel needs to check
        for (int i = 0; i < SEEDS_PER_KERNEL; i++)
        {
            kernelGen.rngCalls = 0;
            kernelGen.callOffset = i;
            kernelGen.addCheck(16, Comparison.EQUAL, trees[TARGET_TREE][1]);
            switch(treeTypes[TARGET_TREE]) {
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
            }
            kernelGen.addCheck(3, Comparison.EQUAL, treeHeights[TARGET_TREE] - 4);
            // leaves
            for (int j = 0; j < 12; j++) {
                char leaf = treeLeaves[TARGET_TREE][j];
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

            kernelGen.callOffset += 1;
        }
//        System.out.println(kernelGen.sb.toString());
        Utils.writeStringToFile(outfile, kernelGen.sb.toString());
        System.out.println("total call checks per seed: " + kernelGen.rngCalls);
    }

    static class TreeKernelGenerator {
        public long rngCalls;
        public long callOffset;
        public StringBuilder sb;

        public TreeKernelGenerator() {
            rngCalls = 0;
            callOffset = 0;
            sb = new StringBuilder();
        }

        public void addCheck(int bound, Comparison comparison, int target) {
            rngCalls += 1;
            LCG lcg = LCG.JAVA.combine(rngCalls + callOffset);
            String comparisonStr = comparison.getInversion().toString();

            if ((bound & bound - 1) == 0) { // bound is a power of 2
                int rightShift = (int) (48 - Math.log(bound) / Math.log(2));
                int mask = bound - 1;

                String parameterized = String.format(
                        "if ((((baseSeed * %15dLU + %15dLU) >> %2d) & %2d) %2s %2d) continue;",
//                        rngCalls + callOffset, 0, // temporary test to make sure we're checking all seeds
                        lcg.multiplier,
                        lcg.addend,
                        rightShift,
                        mask,
                        comparisonStr,
                        target
                );
                sb.append(parameterized);
            } else { // non power of 2 bound
                int rightShift = 17;

                String parameterized = String.format(
                        "if ((((baseSeed * %15dLU + %15dLU) >> %2d) %% %2d) %2s %2d) continue;",
//                        rngCalls + callOffset, 0, // temporary test to make sure we're checking all seeds
                        lcg.multiplier,
                        lcg.addend,
                        rightShift,
                        bound,
                        comparisonStr,
                        target
                );
                sb.append(parameterized);
            }
            sb.append('\n');
        }

        public void addSkip(long steps) {
            rngCalls += steps;
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
