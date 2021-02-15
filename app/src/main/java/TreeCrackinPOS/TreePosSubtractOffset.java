package TreeCrackinPOS;

import java.util.Arrays;

import static java.lang.Math.*;

public class TreePosSubtractOffset {
    public static void main(String[] args) {
        final int[][] trees = {
                { 21, 40 },
        };
        final int[][] popChunks = new int[trees.length][2];

        // get chunk
        System.out.println("\nchunk coords:");
        for (int i = 0; i < trees.length; i++) {
            popChunks[i][0] = (trees[i][0] - 8) / 16;
            popChunks[i][1] = (trees[i][1] - 8) / 16;
            System.out.printf("{ %3d, %3d },\n", popChunks[i][0], popChunks[i][1]);
        }

        // subtract offset
        System.out.println("tree locations in pop region:");
        for (int i = 0; i < trees.length; i++) {
            int x = trees[i][0] - 8 - (popChunks[i][0] * 16);
            int z = trees[i][1] - 8 - (popChunks[i][1] * 16);
            System.out.printf("{ %3d, %3d },\n", x, z);
        }
    }
}
