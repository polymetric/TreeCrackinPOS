package TreeCrackinPOS.post_processing;

import TreeCrackinPOS.WorldToChunk;
import TreeCrackinPOS.utils.Utils;
import kaptainwutax.seedutils.lcg.LCG;
import kaptainwutax.mcutils.version.MCVersion;
import mjtb49.hashreversals.ChunkRandomReverser;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class CRRTreeFinder {
    public static void main(String[] args) throws Exception {
        final int THREADS = 8;
//        final int[] dfzs = new int[] { 3840, 4097, 3841, 4098, 4356, 4101, 4102, 4617, 4110, 4369, 4114, 3863, 4375, 4120, 3865, 3866, 4122, 3868, 4126, 3875, 3878, 3879, 3880, 3883, 4396, 3886, 4143, 3889, 4659, 4409, 3900, 3904, 3906, 3907, 3909, 3910, 4169, 4173, 4436, 3925, 3926, 3928, 3935, 4192, 3936, 4194, 3939, 4195, 3940, 4197, 4200, 3944, 4457, 3946, 3950, 3951, 3953, 3954, 3955, 4467, 3963, 3964, 3968, 3969, 3972, 3973, 3975, 3976, 3978, 4237, 3984, 4244, 3989, 4758, 3991, 3994, 3996, 4255, 4005, 4006, 4263, 4007, 4010, 4011, 4015, 4018, 4276, 4025, 4029, 4805, 4039, 4297, 4298, 4811, 4303, 4560, 5345, 4577, 4837, 3815, 4328, 4072, 4077, 4079, 4081, 4082, 3829, 4857, 4092, 3844, 3852, 3853, 3858, 3867, 3870, 3901, 3931, 3956, 3960, 3986, 3990, 3997, 3999, 4001, 4012, 4021, 4022, 4026, 4033, 4040, 4068, 3817, 3827, 3839, 3850, 3851, 3913, 4000, 4008, 3814, 3821, 3877, 3816, 3838, 3849, 3872, 3837, 3825, 3764, 3813, 3762, 3761, 3763, 3765, 3836, 3824, 3848, 3812, 3760, };
//        final int[] dfzs = new int[] { 3760 };
        final int[] dfzs = new int[] { 3836, 3824, 3848, 3812, 3760 };
        final LCG rev1 = LCG.JAVA.combine(-1);
//         SHOT P
        final int CHUNK_A_X = 7;
        final int CHUNK_A_Z = 13;
        final int CHUNK_B_X = 7;
        final int CHUNK_B_Z = 12;
        // SEED 5
//        final int CHUNK_A_X = 0;
//        final int CHUNK_A_Z = 0;
//        final int CHUNK_B_X = 0;
//        final int CHUNK_B_Z = 2;
        final int TREE_CALL_RANGE = 220*2;
        final String SEEDS_IN = "treeseeds_shotp_12.txt";
        final String SEEDS_OUT = "worldseeds_shotp_try5.txt";
//        final String SEEDS_IN = "treeseeds.txt";
//        final String SEEDS_OUT = "worldseeds_seed5_try1.txt";
        String[] seedsIn = Utils.readFileToString(SEEDS_IN).split("\n");
        FileWriter seedsOutStream = new FileWriter(SEEDS_OUT);
        long totalInputSeeds = seedsIn.length;
        System.out.printf("opened file with %d seeds\n", totalInputSeeds);
//        long approxTotalWorldSeedsToCheck = totalInputSeeds * dfzs.length * dfzs.length + 3;
        long approxTotalWorldSeedsToCheck = totalInputSeeds * (4500 - (3760 - TREE_CALL_RANGE)) * dfzs.length + 3;
//        long approxTotalWorldSeedsToCheck = totalInputSeeds * (4500 - (3760 - TREE_CALL_RANGE)) * (4500 - (3760 - TREE_CALL_RANGE));
        AtomicLong worldSeedsDone = new AtomicLong();
        ArrayList<Thread> threads = new ArrayList<>();
        long timeStart = System.currentTimeMillis();
        AtomicInteger resultsCount = new AtomicInteger();

        // basically what this code does is go through all the possible
        // dfzs between pop and tree gen, then gets a world seed from each of those, then
        // filters those by trying to generate trees in another chunk seed
        for (int i = 0; i < seedsIn.length; i++) {
            long treeRegionSeedA;
            try {
                treeRegionSeedA = Long.parseLong(seedsIn[i].trim());
            } catch (NumberFormatException e) {
                continue;
            }

            final int threadId = i;
            Thread thread = new Thread(() -> {
//                System.out.printf("started thread %4d with seed %15d\n", threadId, treeRegionSeedA);
                for (int dfzA = 3760 - TREE_CALL_RANGE; dfzA < 5500; dfzA++) {
//                for (int dfzIndexA = 0; dfzIndexA < dfzs.length; dfzIndexA++) {
                    long popSeedA = LCG.JAVA.combine((-dfzA) - 1).nextSeed(treeRegionSeedA);
//                    long popSeedA = LCG.JAVA.combine((-dfzs[dfzIndexA]) - 1).nextSeed(treeRegionSeedA);
//                    for (int dfzB = 3760; dfzB < 5500; dfzB++) {
                    for (int dfzIndexB = 0; dfzIndexB < dfzs.length; dfzIndexB++) {
                        for (long worldSeed : ChunkRandomReverser.reversePopulationSeed(popSeedA ^ LCG.JAVA.multiplier, CHUNK_A_X, CHUNK_A_Z, MCVersion.v1_8)) {
                            long popSeedB = WorldToChunk.worldToChunk(worldSeed, CHUNK_B_X, CHUNK_B_Z);
                            long treeRegionSeedB = LCG.JAVA.combine(dfzs[dfzIndexB] - 1).nextSeed(popSeedB);
//                            long treeRegionSeedB = LCG.JAVA.combine(dfzB).nextSeed(popSeedB);

                            boolean treesFound[] = new boolean[2];
                            for (int callOffset = 0; callOffset < TREE_CALL_RANGE * 2; callOffset++) {
                                treeRegionSeedB = LCG.JAVA.nextSeed(treeRegionSeedB);

//                                if (!treesFound[0]) {
//                                    treesFound[0] = checkTreeSeed5(treeRegionSeedB);
//                                }
                                if (!treesFound[0]) {
                                    treesFound[0] = checkTreeL2(treeRegionSeedB);
                                }
                                if (!treesFound[1]) {
                                    treesFound[1] = checkTreeL3(treeRegionSeedB);
                                }

                                if (treesFound[0] && treesFound[1]) {
//                                if (treesFound[0]) {
                                    try {
                                        seedsOutStream.write(Long.toString(worldSeed) + "\n");
                                        seedsOutStream.flush();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    resultsCount.getAndIncrement();
                                    break;
                                }
                            }
                            worldSeedsDone.getAndIncrement();
                        }
                    }
                }
//                System.out.printf("finished thread %4d\n", threadId, treeRegionSeedA);
            });
            threads.add(thread);
        }

        long now = System.currentTimeMillis();
        long lastTime = now;
        int threadsAlive = 0;
        boolean[] threadsStarted = new boolean[threads.size()];
        boolean[] threadsDone = new boolean[threads.size()];
        boolean done = false;

        while (!done) {
            now = System.currentTimeMillis();
            if (now - lastTime > 250) {
                for (int i = 0; i < threads.size(); i++) {
                    Thread t = threads.get(i);
                    if (threadsAlive < THREADS && !threadsStarted[i]) {
                        t.start();
                        threadsAlive++;
                        threadsStarted[i] = true;
                    }
                    if (!t.isAlive() && threadsStarted[i] && !threadsDone[i]) {
                        threadsAlive--;
                        threadsDone[i] = true;
                        if (threadsAlive <= 0) {
                            done = true;
                        }
                    }
                }

                lastTime = now;
                System.out.printf("progress: %6d / %6d, %6.2f%% results: %d\n", worldSeedsDone.get(), approxTotalWorldSeedsToCheck, (double) (worldSeedsDone.get()) / approxTotalWorldSeedsToCheck * 100, resultsCount.get());
            }
        }

        System.out.printf("done, found %d results in %.2fs\n", resultsCount.get(), (System.currentTimeMillis() - timeStart) / 1000.0D);
    }

    static long getNextSeedFromFile(InputStream stream) {
        byte[] buffer = new byte[Long.BYTES];
        try {
            stream.read(buffer, 0, Long.BYTES);
        } catch (IOException e) {
            System.out.println("eror");
            e.printStackTrace();
            System.exit(1);
        }
        ByteBuffer byteBuffer = ByteBuffer.wrap(buffer);
        long l = byteBuffer.getLong();
        return Long.reverseBytes(l);
    }

    static boolean checkTreeSeed5(long seed) {
        if (seed == 156952868252044L) {
            System.out.println("found correct tree seed");
        }
        if ((((seed *     25214903917L +              11L) >> 44) & 15) != 13) return false;
        if ((((seed * 205749139540585L +    277363943098L) >> 44) & 15) !=  0) return false;
        if ((((seed *  76790647859193L +  25707281917278L) >> 47) &  1) !=  1) return false;
        if ((((seed *  61282721086213L +  25979478236433L) >> 47) &  1) !=  0) return false;
        if ((((seed * 128954768138017L + 137139456763464L) >> 47) &  1) !=  0) return false;
        if ((((seed * 177269950146317L + 148267022728371L) >> 47) &  1) !=  1) return false;
        if ((((seed *  19927021227657L + 127911637363266L) >> 47) &  1) !=  0) return false;
        if ((((seed *  92070806603349L +  65633894156837L) >> 47) &  1) !=  1) return false;
        if ((((seed *  28158748839985L + 233987836661708L) >> 47) &  1) !=  1) return false;
        if ((((seed * 118637304785629L + 262259097190887L) >> 47) &  1) !=  1) return false;
        if ((((seed * 127636996050457L + 159894566279526L) >> 47) &  1) !=  1) return false;
        if ((((seed *  12659659028133L + 156526639281273L) >> 47) &  1) !=  0) return false;
        if ((((seed * 120681609298497L +  14307911880080L) >> 47) &  1) !=  0) return false;
        if ((((seed * 262331189124013L + 215905707320923L) >> 47) &  1) !=  1) return false;

        return true;
    }

    static int checkTreeSeed5e(long seed) {
        if (seed == 156952868252044L) {
            System.out.println("found correct tree seed");
        }
        if ((((seed *     25214903917L +              11L) >> 44) & 15) != 13) return -1;
        if ((((seed * 205749139540585L +    277363943098L) >> 44) & 15) !=  0) return -2;
        if ((((seed *  76790647859193L +  25707281917278L) >> 47) &  1) !=  1) return -3;
        if ((((seed *  61282721086213L +  25979478236433L) >> 47) &  1) !=  0) return -4;
        if ((((seed * 128954768138017L + 137139456763464L) >> 47) &  1) !=  0) return -5;
        if ((((seed * 177269950146317L + 148267022728371L) >> 47) &  1) !=  1) return -6;
        if ((((seed *  19927021227657L + 127911637363266L) >> 47) &  1) !=  0) return -7;
        if ((((seed *  92070806603349L +  65633894156837L) >> 47) &  1) !=  1) return -8;
        if ((((seed *  28158748839985L + 233987836661708L) >> 47) &  1) !=  1) return -9;
        if ((((seed * 118637304785629L + 262259097190887L) >> 47) &  1) !=  1) return -10;
        if ((((seed * 127636996050457L + 159894566279526L) >> 47) &  1) !=  1) return -11;
        if ((((seed *  12659659028133L + 156526639281273L) >> 47) &  1) !=  0) return -12;
        if ((((seed * 120681609298497L +  14307911880080L) >> 47) &  1) !=  0) return -13;
        if ((((seed * 262331189124013L + 215905707320923L) >> 47) &  1) !=  1) return -14;

        return 1;
    }

    static boolean checkTreeL2(long seed) {
        if ((((seed *     25214903917L +              11L) >> 44) & 15) != 14) return false;
        if ((((seed * 205749139540585L +    277363943098L) >> 44) & 15) != 10) return false;
        if ((((seed * 120950523281469L + 102626409374399L) >> 47) &  1) !=  1) return false;
        if ((((seed *  61282721086213L +  25979478236433L) >> 47) &  1) !=  1) return false;
        if ((((seed * 177269950146317L + 148267022728371L) >> 47) &  1) !=  0) return false;
        if ((((seed *  92070806603349L +  65633894156837L) >> 47) &  1) !=  1) return false;
        if ((((seed *  28158748839985L + 233987836661708L) >> 47) &  1) !=  0) return false;
        if ((((seed * 118637304785629L + 262259097190887L) >> 47) &  1) !=  1) return false;
        if ((((seed *  12659659028133L + 156526639281273L) >> 47) &  1) !=  1) return false;
        if ((((seed * 120681609298497L +  14307911880080L) >> 47) &  1) !=  1) return false;
        if ((((seed * 233752471717045L +  11718085204285L) >> 17) %  5) !=  0) return false;
        if ((((seed *  55986898099985L +  49720483695876L) >> 17) %  3) !=  1) return false;

        return true;
    }

    static boolean checkTreeL3(long seed) {
        if ((((seed *     25214903917L +              11L) >> 44) & 15) != 10) return false;
        if ((((seed * 205749139540585L +    277363943098L) >> 44) & 15) !=  8) return false;
        if ((((seed * 128954768138017L + 137139456763464L) >> 47) &  1) !=  0) return false;
        if ((((seed *  19927021227657L + 127911637363266L) >> 47) &  1) !=  1) return false;
        if ((((seed *  28158748839985L + 233987836661708L) >> 47) &  1) !=  1) return false;
        if ((((seed * 120681609298497L +  14307911880080L) >> 47) &  1) !=  1) return false;
        if ((((seed * 233752471717045L +  11718085204285L) >> 17) %  5) ==  0) return false;
        if ((((seed *  55986898099985L +  49720483695876L) >> 17) % 10) ==  0) return false;
        if ((((seed * 120950523281469L + 102626409374399L) >> 17) %  3) !=  1) return false;

        return true;
    }
}