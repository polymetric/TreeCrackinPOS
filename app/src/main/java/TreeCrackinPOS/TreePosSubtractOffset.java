package TreeCrackinPOS;

public class TreePosSubtractOffset {
    public static void main(String[] args) {
        int[][] trees = {
                { 133, 216 }, // L4 - 0
                { 134, 223 }, // L5 - 1
                { 127, 223 }, // L6 - 2
                { 128, 219 }, // L7 - 3
        };

        for (int i = 0; i < trees.length; i++) {
            trees[i][0] -= (trees[i][0] / 16) * 16;
            trees[i][1] -= (trees[i][1] / 16) * 16;
            System.out.printf("{ %3d, %3d },\n", trees[i][0], trees[i][1]);
        }
    }
}
