package TreeCrackinPOS.tests;

import kaptainwutax.seedutils.lcg.LCG;

import static TreeCrackinPOS.utils.Utils.printRuler;

public class whyisitadding1 {
    public static void main(String[] args) {
        printRuler();

        seed = 1;
//        System.out.println(String.format("%64s", Long.toBinaryString(nextLong())).replace(' ', '0'));

        seed = 1;
//        System.out.println(String.format("%64s", Long.toBinaryString(nextLong3())).replace(' ', '0'));

//        int a = 0b00000000000000000000000000000000;
        int b = 0b10000000000000000000000000000000;
        int a = -1;
//        int b = -1;

        long c = (((long) a) << 32) + b;
//        System.out.println(String.format("%32s%32s", Integer.toBinaryString(a), ""));
        System.out.println(String.format("%64s", Long.toBinaryString((long) a << 32)).replace(' ', '0'));
//        System.out.println(String.format("%32s%32s", "", Integer.toBinaryString(b)));
        System.out.println(String.format("%64s", Long.toBinaryString(b)).replace(' ', '0'));
        System.out.println(String.format("%64s", Long.toBinaryString(c)).replace(' ', '0'));
    }

    public static long seed = 1;

    public static int next(int bits) {
        seed = LCG.JAVA.nextSeed(seed);
        return (int) (seed >>> (48 - bits));
    }

    public static long nextLong() {
        return ((long) (next(32)) << 32) + next(32);
    }

    public static long nextLong3() {
        long upper = ((long) next(32)) << 32;
        long lower = next(32);
        return upper + lower;
    }
}
