package TreeCrackinPOS;

public class TreePosSubtractOffset {
    public static void main(String[] args) {
        int[][] trees = {
                {134, 210},
                {130, 208},
        };

        // subtract offset
        for (int i = 0; i < trees.length; i++) {
            trees[i][0] -= (trees[i][0] / 16) * 16;
            trees[i][1] -= (trees[i][1] / 16) * 16;
            System.out.printf("{ %3d, %3d },\n", trees[i][0], trees[i][1]);
        }

        // get pop chunk
//        for (int i = 0; i < trees.length; i++) {
//            trees[i][0] = ((trees[i][0] - 8) / 16);
//            trees[i][1] = ((trees[i][1] - 8) / 16);
//            System.out.printf("{ %3d, %3d },\n", trees[i][0], trees[i][1]);
//        }
    }
}
