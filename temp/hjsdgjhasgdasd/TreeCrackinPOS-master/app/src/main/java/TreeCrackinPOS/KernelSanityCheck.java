package TreeCrackinPOS;

import kaptainwutax.seedutils.lcg.LCG;

import java.util.Random;

public class KernelSanityCheck {
    public static void main(String[] args) {
        long seed = 0L;

        seed = Long.parseLong("225855820187136");

        System.out.println(checkTree0(seed));
    }

    static final long mask = (1L << 48) - 1;


    public static int checkTree0(long seed) {
        if ((((seed *     25214903917L +              11L) >> 44) & 15) !=  9) return 0; // pos X
        if ((((seed * 205749139540585L +    277363943098L) >> 44) & 15) !=  0) return 0; // pos Z
        if ((((seed *  55986898099985L +  49720483695876L) >> 46) &  3) !=  3) return 0; // height
        if ((((seed * 120950523281469L + 102626409374399L) >> 47) &  1) !=  1) return 0; // base height
        if ((((seed *  76790647859193L +  25707281917278L) >> 47) &  1) !=  0) return 0; // radius
        if ((((seed *  61282721086213L +  25979478236433L) >> 47) &  1) !=  0) return 0; // initial radius
        if (((((seed * 233752471717045L +  11718085204285L) & 281474976710655L) >> 17) %  3) ==  0) return 0; // type

        return 1;
    }

    public static int checkTree1(long seed) {
        if ((((seed *     25214903917L +              11L) >> 44) & 15) != 12) return 0; // pos X
        if ((((seed * 205749139540585L +    277363943098L) >> 44) & 15) !=  5) return 0; // pos Z
        if ((((seed *  55986898099985L +  49720483695876L) >> 46) &  3) !=  3) return 0; // height
        if ((((seed * 120950523281469L + 102626409374399L) >> 47) &  1) !=  1) return 0; // base height
        if ((((seed *  76790647859193L +  25707281917278L) >> 47) &  1) !=  0) return 0; // radius
        if ((((seed *  61282721086213L +  25979478236433L) >> 47) &  1) !=  1) return 0; // initial radius
        if (((((seed * 233752471717045L +  11718085204285L) & 281474976710655L) >> 17) %  3) ==  0) return 0; // type

        return 1;
    }

    public static int checkTree2(long seed) {
        if ((((seed *     25214903917L +              11L) >> 44) & 15) !=  4) return 0; // pos X
        if ((((seed * 205749139540585L +    277363943098L) >> 44) & 15) !=  5) return 0; // pos Z
        if ((((seed *  55986898099985L +  49720483695876L) >> 46) &  3) !=  0) return 0; // height
        if ((((seed * 120950523281469L + 102626409374399L) >> 47) &  1) !=  0) return 0; // base height
        if ((((seed *  76790647859193L +  25707281917278L) >> 47) &  1) !=  0) return 0; // radius
        if ((((seed *  61282721086213L +  25979478236433L) >> 47) &  1) !=  1) return 0; // initial radius
        if (((((seed * 233752471717045L +  11718085204285L) & 281474976710655L) >> 17) %  3) ==  0) return 0; // type

        return 1;
    }

    public static int checkTree3(long seed) {
        if ((((seed *     25214903917L +              11L) >> 44) & 15) !=  2) return 2; // pos X
        if ((((seed * 205749139540585L +    277363943098L) >> 44) & 15) != 10) return 3; // pos Z
        if ((((seed *  55986898099985L +  49720483695876L) >> 46) &  3) !=  2) return 4; // height
        if ((((seed * 120950523281469L + 102626409374399L) >> 47) &  1) !=  1) return 5; // base height
//        if ((((seed *  76790647859193L +  25707281917278L) >> 47) &  1) !=  0) return 6; // radius
        if ((((seed *  61282721086213L +  25979478236433L) >> 47) &  1) !=  0) return 7; // initial radius
        if (((((seed * 233752471717045L +  11718085204285L) & 281474976710655L) >> 17) %  3) ==  0) return 8; // type

        return 1;
    }

    public static int checkTree4(long seed) {
        if ((((seed *     25214903917L +              11L) >> 44) & 15) !=  9) return 0; // pos X
        if ((((seed * 205749139540585L +    277363943098L) >> 44) & 15) != 11) return 0; // pos Z
        if ((((seed *  55986898099985L +  49720483695876L) >> 46) &  3) !=  3) return 0; // height
        if ((((seed * 120950523281469L + 102626409374399L) >> 47) &  1) !=  1) return 0; // base height
        if ((((seed *  76790647859193L +  25707281917278L) >> 47) &  1) !=  0) return 0; // radius
        if ((((seed *  61282721086213L +  25979478236433L) >> 47) &  1) !=  0) return 0; // initial radius
        if (((((seed * 233752471717045L +  11718085204285L) & 281474976710655L) >> 17) %  3) ==  0) return 0; // type

        return 1;
    }

    public static int checkTree5(long seed) {

        return 1;
    }
}
