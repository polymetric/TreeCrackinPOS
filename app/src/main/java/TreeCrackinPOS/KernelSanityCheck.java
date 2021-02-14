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
        seed = 210112431842404L;

        seed = LCG.JAVA.nextSeed(seed);
        System.out.println((seed >> 44) & 15);
        seed = LCG.JAVA.nextSeed(seed);
        System.out.println((seed >> 44) & 15);

        System.out.println(check_tree_0(seed));

        // fucking what!??!?!?
//        Random rand = new Random(seed ^ LCG.JAVA.multiplier);
//        System.out.println(rand.nextInt(16)); // x = 1
//        System.out.println(rand.nextInt(16)); // y = 1
//        System.out.println(rand.nextInt(5)); // birch = 0;
//        System.out.println(rand.nextInt(10)); // big tree = 0;

//        System.out.println("tree 3");
//        seed = 191943944339408L;
//
//        seed = LCG.JAVA.nextSeed(seed);
//        System.out.println((seed >> 44) & 15);
//        seed = LCG.JAVA.nextSeed(seed);
//        System.out.println((seed >> 44) & 15);
//
//        System.out.println(check_tree_3(seed));


//        System.out.println("tree 1");
//        seed = 26060680171178L;
//        System.out.println(LCG.JAVA.nextSeed(seed));
////
//        seed = LCG.JAVA.nextSeed(seed);
//        System.out.println((seed >> 44) & 15);
//        seed = LCG.JAVA.nextSeed(seed);
//        System.out.println((seed >> 44) & 15);
//
//        System.out.println(check_tree_1(seed));

        System.exit(0);

        seed = 26060680171168L;
        for (long initialSeed = seed; initialSeed < 26060680171188L; initialSeed++) {
            seed = LCG.JAVA.combine(-220).nextSeed(initialSeed);

            // sanity check the tree dfz loop
            int tree_flags = 0;
            int tree_x, tree_z;
            for (int call_offset = 0; call_offset < 220 * 2; call_offset++) {
                tree_x = (int) (((seed = LCG.JAVA.nextSeed(seed)) >> 44) & 15); // nextInt(16)
                tree_z = (int) (((seed = LCG.JAVA.nextSeed(seed)) >> 44) & 15); // nextInt(16)

                if (get_tree_flag(tree_flags, 0) == 0
                        && tree_x == 15
                        && tree_z == 10
                ) {
                    tree_flags |= check_tree_2(seed); // TREE 2
                    if (check_tree_2(seed) == 1) {
                        System.out.println(LCG.JAVA.combine(-2).nextSeed(seed));
                    }
                }
                if (get_tree_flag(tree_flags, 1) == 0
                        && tree_x == 1
                        && tree_z == 1
                ) {
                    tree_flags |= check_tree_0(seed) << 1; // TREE 2
                    if (check_tree_0(seed) == 1) {
                        System.out.println(LCG.JAVA.combine(-2).nextSeed(seed));
                    }
                }
                if (get_tree_flag(tree_flags, 2) == 0
                        && tree_x == 12
                        && tree_z == 2
                ) {
                    tree_flags |= check_tree_3(seed) << 2; // TREE 2
                    if (check_tree_3(seed) == 1) {
                        System.out.println(LCG.JAVA.combine(-2).nextSeed(seed));
                    }
                }
            }
            System.out.println(String.format("%8s", Integer.toBinaryString(tree_flags)).replace(' ', '0'));
        }
    }

    private static int get_tree_flag(int tree_flags, int tree_id) {
        return (tree_flags >> tree_id) & 1;
    }

    public static int check_tree_2(long seed) {
        if ((((seed *  55986898099985L +  49720483695876L) >> 47) &  1) !=  0) return 0;
        if ((((seed * 120950523281469L + 102626409374399L) >> 47) &  1) !=  1) return 0;
        if ((((seed *  76790647859193L +  25707281917278L) >> 47) &  1) !=  1) return 0;
        if ((((seed *  61282721086213L +  25979478236433L) >> 47) &  1) !=  1) return 0;
        if ((((seed * 128954768138017L + 137139456763464L) >> 47) &  1) !=  0) return 0;
        if ((((seed * 177269950146317L + 148267022728371L) >> 47) &  1) !=  0) return 0;
        if ((((seed *  19927021227657L + 127911637363266L) >> 47) &  1) !=  1) return 0;
        if ((((seed *  92070806603349L +  65633894156837L) >> 47) &  1) !=  1) return 0;
        if ((((seed *  28158748839985L + 233987836661708L) >> 47) &  1) !=  1) return 0;
        if ((((seed * 118637304785629L + 262259097190887L) >> 47) &  1) !=  0) return 0;
        if ((((seed * 127636996050457L + 159894566279526L) >> 47) &  1) !=  0) return 0;

        return 1;
    }

    public static int check_tree_0(long seed) {
        if ((((seed *  55986898099985L +  49720483695876L) >> 47) &  1) !=  1) return 0;
        if ((((seed * 120950523281469L + 102626409374399L) >> 47) &  1) !=  0) return 0;
        System.out.println("gamer seed:" + seed);
        if ((((seed *  76790647859193L +  25707281917278L) >> 47) &  1) !=  0) return 0;
        if ((((seed * 128954768138017L + 137139456763464L) >> 47) &  1) !=  0) return 0;
        if ((((seed * 177269950146317L + 148267022728371L) >> 47) &  1) !=  1) return 0;
        if ((((seed *  19927021227657L + 127911637363266L) >> 47) &  1) !=  1) return 0;
        if ((((seed *  28158748839985L + 233987836661708L) >> 47) &  1) !=  1) return 0;
        if ((((seed * 118637304785629L + 262259097190887L) >> 47) &  1) !=  1) return 0;
        if ((((seed * 127636996050457L + 159894566279526L) >> 47) &  1) !=  0) return 0;

        return 1;
    }

    public static int check_tree_3(long seed) {
        if ((((seed * 233752471717045L +  11718085204285L) >> 47) &  1) !=  1) return 0;
        if ((((seed *  55986898099985L +  49720483695876L) >> 47) &  1) !=  0) return 0;
        if ((((seed * 120950523281469L + 102626409374399L) >> 47) &  1) !=  1) return 0;
        if ((((seed *  76790647859193L +  25707281917278L) >> 47) &  1) !=  0) return 0;
        if ((((seed * 128954768138017L + 137139456763464L) >> 47) &  1) !=  1) return 0;
        if ((((seed * 177269950146317L + 148267022728371L) >> 47) &  1) !=  0) return 0;
        if ((((seed *  19927021227657L + 127911637363266L) >> 47) &  1) !=  0) return 0;
        if ((((seed *  92070806603349L +  65633894156837L) >> 47) &  1) !=  1) return 0;
        if ((((seed *  28158748839985L + 233987836661708L) >> 47) &  1) !=  0) return 0;
        if ((((seed * 118637304785629L + 262259097190887L) >> 47) &  1) !=  0) return 0;
        if ((((seed * 127636996050457L + 159894566279526L) >> 47) &  1) !=  1) return 0;

        return 1;
    }

    public static int check_tree_1(long seed) {
//        if ((((seed *     25214903917L +              11L) >> 17) %  5) ==  0) return -1;
//        if ((((seed * 205749139540585L +    277363943098L) >> 17) % 10) ==  0) return -2;
//        if ((((seed * 233752471717045L +  11718085204285L) >> 17) %  3) !=  0) return -3;
        if ((((seed *  55986898099985L +  49720483695876L) >> 47) &  1) !=  0) return 0;
        if ((((seed * 120950523281469L + 102626409374399L) >> 47) &  1) !=  0) return 0;
        if ((((seed *  76790647859193L +  25707281917278L) >> 47) &  1) !=  1) return 0;
        if ((((seed *  61282721086213L +  25979478236433L) >> 47) &  1) !=  1) return 0;
        if ((((seed * 128954768138017L + 137139456763464L) >> 47) &  1) !=  1) return 0;
        if ((((seed * 177269950146317L + 148267022728371L) >> 47) &  1) !=  0) return 0;
        if ((((seed *  19927021227657L + 127911637363266L) >> 47) &  1) !=  0) return 0;
        if ((((seed *  92070806603349L +  65633894156837L) >> 47) &  1) !=  1) return 0;
        if ((((seed *  28158748839985L + 233987836661708L) >> 47) &  1) !=  0) return 0;
        if ((((seed * 118637304785629L + 262259097190887L) >> 47) &  1) !=  0) return 0;
        if ((((seed * 127636996050457L + 159894566279526L) >> 47) &  1) !=  0) return 0;
        if ((((seed *  12659659028133L + 156526639281273L) >> 47) &  1) !=  1) return 0;

        return 1;
    }
}
