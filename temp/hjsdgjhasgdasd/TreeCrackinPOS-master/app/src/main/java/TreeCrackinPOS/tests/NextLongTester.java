package TreeCrackinPOS.tests;

import java.util.Random;

import static TreeCrackinPOS.tests.ApeBrainNextLongReversal.nextLongReversal;

public class NextLongTester {
    public static void main(String[] args) {
        int fails = 0;
        int successes = 0;
        for (int i = 0; i < 100000; i++) {
            long test = new Random().nextLong();
            long reversed;
            try {
                reversed = nextLongReversal(test);
                successes++;
            } catch (Exception e) {
                fails++;
            }
        }
        System.out.printf("fails: %d successes: %d", fails, successes);
        System.exit(0);
    }
}
