/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package TreeCrackinPOS;

import com.seedfinding.latticg.reversal.DynamicProgram;
import com.seedfinding.latticg.reversal.calltype.java.JavaCalls;
import com.seedfinding.latticg.util.LCG;

import java.util.Random;
import java.util.stream.LongStream;

public class App {
    public static void main(String[] args) {
        final int TREE_COUNT = 7;
        final int TREE_GEN_AMT = 11;

        int[][] trees = {
                {   1,   1 },
                {   2,   8 },
                {  15,  10 },
                {  12,   2 },
                {   7,   3 },
                {   3,   4 },
                {   0,  12 },
        };

        int[] treeHeights = {
                5,
                4,
                5,
                5,
                6,
                6,
                4,
        };

        char[][] treeLeaves = {
                { 'l', 'n', 'n', 'u', 'n', 'l', 'l', 'u', 'l', 'l', 'n', 'u', },
                { 'n', 'n', 'l', 'l', 'l', 'n', 'n', 'l', 'n', 'n', 'n', 'l', },
                { 'n', 'l', 'l', 'l', 'n', 'n', 'l', 'l', 'l', 'n', 'n', 'l', },
                { 'l', 'n', 'l', 'n', 'u', 'l', 'n', 'n', 'l', 'n', 'n', 'l', },
                { 'l', 'l', 'u', 'u', 'l', 'l', 'u', 'u', 'l', 'u', 'u', 'u', },
                { 'u', 'l', 'u', 'l', 'u', 'n', 'u', 'n', 'l', 'l', 'u', 'u', },
                { 'u', 'n', 'u', 'l', 'u', 'n', 'u', 'n', 'l', 'n', 'n', 'l', },
        };

        char[] gennedLeaves = new char[12];

        RandomOverride rand = new RandomOverride();
        long treeRegionSeedStart = 25214903912L;
        long treeRegionSeedEnd = (1L << 48) - 1;
//        long treeRegionSeedEnd = 10;

        System.out.println("started at " + System.currentTimeMillis());
        for (long treeRegionSeed = treeRegionSeedStart; treeRegionSeed < treeRegionSeedEnd; treeRegionSeed++) {
            rand.setSeedDirect(treeRegionSeed);
            int matches = 0;
            for (int gennedTree = 0; gennedTree < TREE_GEN_AMT; gennedTree++) {
                int baseX = rand.nextInt(16);
                int baseZ = rand.nextInt(16);
                int trunkHeight = rand.nextInt(3) + 4;
                for (int leaf = 0; leaf < 12; leaf++) {
                    gennedLeaves[leaf] = rand.nextInt(2) != 0 ? 'l' : 'n';
                }
                for (int targetTree = 0; targetTree < TREE_COUNT; targetTree++) {
                    if (baseX == trees[targetTree][0]
                            && baseZ == trees[targetTree][1]
                            && trunkHeight == treeHeights[targetTree]
                    ) {
                        int leafMatches = 0;
                        for (int leaf = 0; leaf < 12; leaf++) {
                            if (treeLeaves[targetTree][leaf] == gennedLeaves[leaf]) {
                                leafMatches++;
                            }
                        }
                        if (leafMatches == 12) {
                            matches++;
                        }
                    }
                }
            }
            if (matches >= 2) {
                System.out.printf("found matches: %d seed: %d\n", matches, treeRegionSeed);
            }
            // prints seed every time the progress increases by 1/10000 of the seedspace
//          if (treeRegionSeed % 28147497671L == 0) {
            if (treeRegionSeed % 281474976L == 0) {
                System.out.printf("current seed: %d\n", treeRegionSeed);
            }
        }
    }

    public static void main2(String[] args) {
        RandomOverride rand = new RandomOverride();

        long worldSeed = 5;
        int chunkX = 0;
        int chunkZ = 0;

        rand.setSeed(worldSeed);
        long l1 = (rand.nextLong() / 2L) * 2L + 1L;
        long l2 = (rand.nextLong() / 2L) * 2L + 1L;
        rand.setSeed((long) chunkX * l1 + (long) chunkZ * l2 ^ worldSeed);
        System.out.println(rand.getSeed());
    }
}
