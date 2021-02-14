package TreeCrackinPOS;

import java.util.Arrays;

import static java.lang.Math.*;

public class TreePosSubtractOffset {
    public static void main(String[] args) {
        final int[][] trees = {
                {  9,  9, }, // 0
                { 10, 16, }, // 1
                { 23, 18, }, // 2
                { 20, 10, }, // 3
                { 15, 11, },
                { 11, 12, },
                { 8 , 20, },
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
