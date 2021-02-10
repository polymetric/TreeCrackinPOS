package TreeCrackinPOS;

public class NextIntDemo {
    public static void main(String[] args) {
        for (int i = 0; i < 3; i++) {
            printRuler();
            int bound = 256;
            int n = nextInt(bound);
            String binString = Integer.toBinaryString(n);
            int intendedLength = (int) Math.ceil(Math.log(bound) / Math.log(2));
            int leadingZeroes = intendedLength - binString.length();
            for (int j = 0; j < 16; j++) {
                System.out.print(' ');
            }
            for (int j = 0; j < leadingZeroes; j++) {
                System.out.print('0');
            }
            System.out.println(binString + " < final result");
            System.out.println();
        }
    }

    static final long multiplier = 0x5DEECE66DL;
    static final long addend = 0xBL;
    static final long mask = (1L << 48) - 1;

    static long seed = 1;

    static void printRuler() {
        for (int i = 0; i < 8; i++) {
            int x = (8 - i) * 8;
            System.out.printf("%d", x);
            if (i != 7) {
                for (int j = 0; j < 8 - Integer.toString(x).length(); j++) {
                    System.out.print(' ');
                }
            }
        }
        for (int j = 0; j < 6; j++) {
            System.out.print(' ');
        }
        System.out.print('1');
        System.out.println(" < bit ruler");
        for (int i = 0; i < 64; i++) {
            if (i % 8 == 0 || i == 63) {
                System.out.print('v');
            } else {
                System.out.print(' ');
            }
        }
        System.out.print('\n');
    }

    static int next(int bits) {
        seed = (seed * multiplier + addend) & mask;
        System.out.print(String.format("%64s", Long.toBinaryString(seed)).replace(' ', '0'));
        System.out.println(" < result seed");
        return (int)(seed >>> (48 - bits));
    }

    static int nextInt(int bound) {
        int result = next(31);
        System.out.printf("%47s%17s < 31 bits from next(31)\n", String.format("%31s", Long.toBinaryString(result)).replace(' ', '0'), "");
        int m = bound - 1;
        if ((bound & m) == 0) {  // i.e., bound is a power of 2
//            result = (int) ((bound * (long) result) >> 31);
            int boundBits = (int) (Math.log(bound) / Math.log(2));
            int rightShiftAmount = (int) (31 - boundBits);
            result >>>= rightShiftAmount;
        } else { // bound is not a power of 2
            for (int result2 = result; result2 - (result = result2 % bound) + m < 0; result2 = next(31))
                ;
        }
        return result;
    }

    static int nextInt2(int bound) {
        int bits, value;
        do {
		    seed = (seed * 0x5DEECE66DL + 0xBL) & ((1L << 48) - 1);
            bits = (int) (seed >> 17);
            value = bits % bound;
        } while(bits - value + (bound - 1) < 0);
        return value;
    }
}
