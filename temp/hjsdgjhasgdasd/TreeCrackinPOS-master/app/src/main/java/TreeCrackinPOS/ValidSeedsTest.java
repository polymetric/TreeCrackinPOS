package TreeCrackinPOS;

public class ValidSeedsTest {
    public static void main(String[] args) {
        long[] seeds = {
                3769850118077413172L & ((1L << 48) - 1),
        };
        for (long seed : seeds) {
            for (long upper16 = 0; upper16 < 1 << 16; upper16++) {
                long testSeed = seed | (upper16 << 48);
                if (validSeed(testSeed)) {
                    System.out.println(testSeed);
                }
            }
        }
    }

    public static boolean validSeed(long a) {
        long b = 18218081;
        long c = 1L << 48;
        long d = 7847617;
        long e = ((((d * ((24667315 * (a >>> 32) + b * (int) a + 67552711) >> 32) - b * ((-4824621 * (a >>> 32) + d * (int) a + d) >> 32)) - 11) *
                0xdfe05bcb1365L) % c);
        return ((((0x5deece66dL * e + 11) % c) >> 16) << 32) + (int) (((0xbb20b4600a69L * e + 0x40942de6baL) % c) >> 16) == a;
    }
}
