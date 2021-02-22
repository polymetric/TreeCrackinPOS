package TreeCrackinPOS.treecodegen;

import TreeCrackinPOS.TreeCodeGenTest;
import kaptainwutax.seedutils.lcg.LCG;

public class TreeCheckerGen {
    public long rngCalls;
    public long callOffset;
    public StringBuilder mainSb;
    public StringBuilder moduloSb;
    public boolean auxTree;

    public TreeCheckerGen() {
        rngCalls = 0;
        callOffset = 0;
        mainSb = new StringBuilder();
        moduloSb = new StringBuilder();
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
                    "if ((((seed * %15dLU + %15dLU) >> %2d) & %2d) %2s %2d) return 0;",
//                        rngCalls + callOffset, 0, // temporary test to make sure we're checking all seeds
                    lcg.multiplier,
                    lcg.addend,
                    rightShift,
                    mask,
                    comparisonStr,
                    target
            );
            mainSb.append(parameterized);
            mainSb.append('\n');
        } else { // non power of 2 bound
            int rightShift = 17;

            String parameterized = String.format(
                    "if (((((seed * %15dLU + %15dLU) & %15dLU) >> %2d) %% %2d) %2s %2d) return 0;",
//                        rngCalls + callOffset, 0, // temporary test to make sure we're checking all seeds
                    lcg.multiplier,
                    lcg.addend,
                    lcg.modulus-1,
                    rightShift,
                    bound,
                    comparisonStr,
                    target
            );
            moduloSb.append(parameterized);
            moduloSb.append('\n');
        }
    }

    public void addSkip(long steps) {
        rngCalls += steps;
    }

    public String toString() {
        // we put the modulos at the end because they tend to be slower
        mainSb.append(moduloSb);
        return mainSb.toString();
    }
}