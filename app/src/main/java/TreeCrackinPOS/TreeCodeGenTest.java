package TreeCrackinPOS;

import kaptainwutax.seedutils.lcg.LCG;

public class TreeCodeGenTest {
    public static void main(String[] args) throws Exception {
        final int SEEDS_PER_KERNEL = 1;
        final int TREE_COUNT = 4;
        final int TARGET_TREE = 1;

        final String outfile = String.format("tree%d.cl", TARGET_TREE);

        // Ln is the number of the tree on the recreation,
        // the number after that " - 0" is the index in the array
        // which is what you use to set TARGET_TREE to
        int[][] trees = {
                {   6,   2 },
                {   2,   0 },
        };

        char[] treeTypes = {
                'b',
                'o',
        };

        int[] treeHeights = {
                6,
                5,
        };

        char[][] treeLeaves = {
                { 'l', 'u', 'l', 'u', 'n', 'u', 'l', 'n', 'l', 'u', 'l', 'l', },
                { 'u', 'u', 'n', 'u', 'l', 'u', 'l', 'u', 'u', 'u', 'l', 'u', },
        };

        int[] knownLeaves = {
                5, // L4 - 0
                6, // L5 - 1
                7, // L6 - 2
                8, // L7 - 3
        };

        TreeKernelGenerator kernelGen = new TreeKernelGenerator();
        // for every seed this kernel needs to check
        for (int i = 0; i < SEEDS_PER_KERNEL; i++)
        {
            kernelGen.rngCalls = 0;
            kernelGen.callOffset = i;
            kernelGen.addCheck(16, Comparison.EQUAL, trees[TARGET_TREE][0]);
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
        Utils.writeStringToFile(outfile, kernelGen.toString());
        System.out.println("total call checks per seed: " + kernelGen.rngCalls);
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
                        "if ((((seed * %15dL + %15dL) >> %2d) & %2d) %2s %2d) continue;",
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
