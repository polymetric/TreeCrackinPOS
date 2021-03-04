package TreeCrackinPOS.codegen;

import kaptainwutax.seedutils.lcg.LCG;

public class LCGCheckerCodeGen {
    public long rngCalls;
    public long callOffset;
    public StringBuilder mainSb;
    public StringBuilder moduloSb;
    public String returnValue = null;
    public String comment = "";

    public LCGCheckerCodeGen() {
        rngCalls = 0;
        callOffset = 0;
        mainSb = new StringBuilder();
        moduloSb = new StringBuilder();
    }

    public void addCheck(int bound, Comparison comparison, int target) {
        rngCalls += 1;
        LCG lcg = LCG.JAVA.combine(rngCalls + callOffset);
        String comparisonStr = comparison.getInversion().toString();

        if ((bound & bound - 1) == 0) { // bound is a power of 2
            int rightShift = (int) (48 - Math.log(bound) / Math.log(2));
            int mask = bound - 1;

            String parameterized = String.format(
                    "if (((( seed * %15dLU + %15dLU) >> %2d) & %2d) %2s %2d) return %s; // step %2d %s",
//                        rngCalls + callOffset, 0, // temporary test to make sure we're checking all seeds
                    lcg.multiplier,
                    lcg.addend,
                    rightShift,
                    mask,
                    comparisonStr,
                    target,
                    returnValue,
                    rngCalls,
                    comment
            );
            comment = "";
            mainSb.append(parameterized);
            mainSb.append('\n');
        } else { // non power of 2 bound
            int rightShift = 17;

            String parameterized = String.format(
                    "if (((((seed * %15dLU + %15dLU) & %15dLU) >> %2d) %% %2d) %2s %2d) return %s; // step %2d %s",
//                        rngCalls + callOffset, 0, // temporary test to make sure we're checking all seeds
                    lcg.multiplier,
                    lcg.addend,
                    lcg.modulus-1,
                    rightShift,
                    bound,
                    comparisonStr,
                    target,
                    returnValue,
                    rngCalls,
                    comment
            );
            comment = "";
            moduloSb.append(parameterized);
            moduloSb.append('\n');
        }
    }

    public void addSkip(long steps) {
        rngCalls += steps;
    }

    public void addComment(String comment) {
        this.comment = comment;
    }

    public String toString() {
        // we put the modulos at the end because they tend to be slower
        mainSb.append(moduloSb);
        return mainSb.toString();
    }
}