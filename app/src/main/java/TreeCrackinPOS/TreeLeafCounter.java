package TreeCrackinPOS;

public class TreeLeafCounter {
    // count known leaves
    public static void main(String[] args) {
        char[][] treeLeaves = {
                { 'u', 'u', 'u', 'l', 'u', 'u', 'u', 'n', 'l', 'u', 'n', 'l', },
                { 'n', 'u', 'l', 'u', 'l', 'u', 'n', 'u', 'n', 'u', 'n', 'u', },
                { 'l', 'u', 'u', 'n', 'n', 'u', 'u', 'l', 'n', 'u', 'u', 'l', },
                { 'u', 'u', 'n', 'n', 'n', 'u', 'n', 'n', 'n', 'u', 'n', 'n', },
        };

        for (int tree = 0; tree < treeLeaves.length; tree++) {
            int sureLeaves = 0;
            for (int leaf = 0; leaf < 12; leaf++) {
                if (treeLeaves[tree][leaf] == 'l' || treeLeaves[tree][leaf] == 'n') {
                    sureLeaves += 1;
                }
            }
            System.out.printf("%d,\n", sureLeaves);
        }
    }
}
