package TreeCrackinPOS;

public class TreeLeafCounter {
    // count known leaves
    public static void main(String[] args) {
        String[] treeLeaves = {
                "uuuluuunlunl",
                "nululunununu",
                "uuunnuulnuul",
                "uunununnnunn",
                "uuuuluuuuuuu",
                "uulununununl",
        };

        for (int tree = 0; tree < treeLeaves.length; tree++) {
            int sureLeaves = 0;
            for (int leaf = 0; leaf < 12; leaf++) {
                if (treeLeaves[tree].charAt(leaf) == 'l' || treeLeaves[tree].charAt(leaf) == 'n') {
                    sureLeaves += 1;
                }
            }
            System.out.printf("%d,\n", sureLeaves);
        }
    }
}
