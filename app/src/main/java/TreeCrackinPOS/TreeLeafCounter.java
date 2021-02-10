package TreeCrackinPOS;

public class TreeLeafCounter {
    // count known leaves
    public static void main(String[] args) {
        final int TREE_COUNT = 4;

        char[][] treeLeaves = {
                { 'u', 'u', 'u', 'l', 'u', 'u', 'u', 'n', 'l', 'u', 'n', 'l', },
                { 'n', 'u', 'l', 'u', 'l', 'u', 'n', 'u', 'n', 'u', 'n', 'u', },
                { 'u', 'u', 'n', 'n', 'n', 'u', 'n', 'n', 'n', 'u', 'n', 'n', },
                { 'l', 'u', 'u', 'n', 'n', 'u', 'u', 'l', 'n', 'u', 'l', 'l', },
        };

        for (int tree = 0; tree < TREE_COUNT; tree++) {
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
