package TreeCrackinPOS;

public class TreeLeafCounter {
    // count known leaves
    public static void main(String[] args) {
        char[][] treeLeaves = {
                {'l', 'n', 'n', 'l', 'n', 'n', 'l', 'l', 'l', 'n', 'n', 'l',},
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
