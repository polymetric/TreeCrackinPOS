package TreeCrackinPOS;

public class NextIntDemo2 {
    public static void main(String[] args) {
        long seedsWhereWhackConditionWasTrue = 0;
        long seedsTried = 0;
        for (; seedsTried < (1L << 34); seedsTried++) {
            int bound = 3;
            long initialSeed = seed;

            int tries = 0;
            int bits, result;
            while (true) {
                bits = next(31);
                result = bits % bound;
                tries++;
                if (bits - result + bound - 1 >= 0) {
                    break;
                }
//                System.out.println("seeds tried: " + seedsTried);
//                System.out.println("seeds where whack condition was true: " + seedsWhereWhackConditionWasTrue);
//                System.out.println("chance of whack condition: " + (double) seedsWhereWhackConditionWasTrue / seedsTried);
                System.out.println("whack condition was true");
                System.out.println("bits = " + bits);
                System.out.println("bits % bound = " + result);
                System.out.println("result + bound - 1 = " + (result + bound - 1));
                System.out.println("bits - result + bound - 1 = " + (bits - result + bound - 1));
                System.out.println("initialSeed = " + initialSeed);
                System.out.println("tries = " + (tries+1));
//                System.out.println();
                seedsWhereWhackConditionWasTrue++;
            }
        }
        System.out.println("seeds tried: " + seedsTried);
        System.out.println("seeds where whack condition was true: " + seedsWhereWhackConditionWasTrue);
        System.out.println("chance of whack condition: " + (double) seedsWhereWhackConditionWasTrue / seedsTried);
    }

    static final long multiplier = 0x5DEECE66DL;
    static final long addend = 0xBL;
    static final long mask = (1L << 48) - 1;

    static long seed = 1;

    static int next(int bits) {
        seed = (seed * multiplier + addend) & mask;
        return (int)(seed >>> (48 - bits));
    }

    // nextInt where bound is not a power of 2
    static int nextInt(int bound) {
        int bits, result;
        int mask = bound - 1;
        do {
            bits = next(31);
            result = bits % bound;
        } while(bits - result + mask < 0);
        return result;
    }
}
