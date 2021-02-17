package TreeCrackinPOS;

import java.util.Arrays;

import static java.lang.Math.*;

public class TreePosSubtractOffset {
    public static void main(String[] args) {
        final int[][] trees = {
                { 134, 210 },
                { 130, 208 },
        };
        final int[][] popChunks = new int[trees.length][2];
        final int[][] popChunkOriginBlocks = new int[trees.length][2];

        // get chunk coords
        System.out.println("\nchunk coords:");
        for (int i = 0; i < trees.length; i++) {
            popChunks[i][0] = floorDiv((trees[i][0] - 8), 16);
            popChunks[i][1] = floorDiv((trees[i][1] - 8), 16);
            System.out.printf("{ %3d, %3d },\n", popChunks[i][0], popChunks[i][1]);
        }

        // get chunk origin coords (mainly sanity check)
        System.out.println("\nchunk origin block coords:");
        for (int i = 0; i < trees.length; i++) {
            popChunkOriginBlocks[i][0] = popChunks[i][0] * 16;
            popChunkOriginBlocks[i][1] = popChunks[i][1] * 16;
            System.out.printf("{ %3d, %3d },\n", popChunkOriginBlocks[i][0], popChunkOriginBlocks[i][1]);
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
