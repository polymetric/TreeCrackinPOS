package TreeCrackinPOS;

import kaptainwutax.seedutils.lcg.LCG;

import java.util.Random;

public class KernelSanityCheck {
    public static void main(String[] args) {
        long seed = 0L;

//        System.out.println("tree 2");
//        seed = 142639957996320L;
//
//        seed = LCG.JAVA.nextSeed(seed);
//        System.out.println((seed >> 44) & 15);
//        seed = LCG.JAVA.nextSeed(seed);
//        System.out.println((seed >> 44) & 15);
//
//        System.out.println(check_tree_2(seed));

        System.out.println("tree 0");
        seed = 41315078552651L;

        for (int i = 0; i < 5; i++) {
            System.out.println(LCG.JAVA.combine(i).nextSeed(seed) >> 17);
        }

        System.out.println(check_tree_0(seed));
    }

    static final long mask = (1L << 48) - 1;

    public static int check_tree_0(long seed) {
        if ((((seed *  55986898099985L +  49720483695876L) >> 47) &  1) !=  0) return -1;
        if ((((seed *  76790647859193L +  25707281917278L) >> 47) &  1) !=  1) return -2;
        if ((((seed * 128954768138017L + 137139456763464L) >> 47) &  1) !=  1) return -3;
        if ((((seed *  19927021227657L + 127911637363266L) >> 47) &  1) !=  1) return -4;
        if ((((seed *  92070806603349L +  65633894156837L) >> 47) &  1) !=  0) return -5;
        if ((((seed *  28158748839985L + 233987836661708L) >> 47) &  1) !=  1) return -6;
        if ((((seed * 127636996050457L + 159894566279526L) >> 47) &  1) !=  1) return -7;
        if (((((seed *     25214903917L +              11L) & 281474976710655L) >> 17) %  5) ==  0) return -8;
        if (((((seed * 205749139540585L +    277363943098L) & 281474976710655L) >> 17) % 10) ==  0) return -9;
        if (((((seed * 233752471717045L +  11718085204285L) & 281474976710655L) >> 17) %  3) !=  2) return -10;


        return 1;
    }

    public static int check_tree_1(long seed) {


        return 1;
    }

    public static int check_tree_2(long seed) {

        return 1;
    }

    public static int check_tree_3(long seed) {

        return 1;
    }
}
