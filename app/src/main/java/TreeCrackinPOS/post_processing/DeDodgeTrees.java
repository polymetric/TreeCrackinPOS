package TreeCrackinPOS.post_processing;

import TreeCrackinPOS.utils.Profiler;
import TreeCrackinPOS.utils.Utils;
import kaptainwutax.seedutils.lcg.LCG;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class DeDodgeTrees {
    // takes the output of the tree kernel (primary tree seeds that matched aux tree seeds as well)
    // and gets the aux tree seeds back out
    public static void main(String[] args) throws Exception {
        final int THREADS = 8000;
        final int TARGET_TREE_COUNT = 4;
        final String seedsInPath = "treeseeds_funny_1.txt";
        final String seedsOutPath = "treeseeds_funny_2_dedodged.txt";

        final int TREE_CALL_RANGE = 90;
        final LCG revTCR = LCG.JAVA.combine(-TREE_CALL_RANGE);
        final LCG rev1 = LCG.JAVA.combine(-1);
        final LCG fwd1 = LCG.JAVA.combine(1);

        Profiler mainThreadProfiler = new Profiler();
        mainThreadProfiler.switchSection("init");

        String[] seedsIn = Utils.readFileToString(seedsInPath).split("\n");
        final int totalSeeds = seedsIn.length;
//        String[] seedsIn = {
//                "161113400866202",
//        }; // DEBUG

        File seedsOutFile = new File(seedsOutPath);
        seedsOutFile.delete();
        seedsOutFile.createNewFile();
        FileWriter seedsOutStream = new FileWriter(seedsOutPath);

        long timeStart = System.currentTimeMillis();
        ArrayList<Thread> threads = new ArrayList<>();
        ArrayList<Profiler> threadProfilers = new ArrayList<>();

        AtomicInteger seedsPassed = new AtomicInteger();
        AtomicInteger seedsChecked = new AtomicInteger();

        // this is a pretty cursed multithreading approach when we have a lot of seeds -
        // basically it spawns all the threads (one for each seed) then runs them in groups at a time.
        // the thread poll loop below takes up a majority of the runtime of this program (see the profiler results)
        // because it has to loop through the entire list of threads every time it wants to check them.
        // TODO make this less cursed by not spawning threads until they're needed
        mainThreadProfiler.switchSection("spawnThreads");
        for (int i = 0; i < seedsIn.length; i++) {
            final long initialSeed = Long.parseLong(seedsIn[i].trim());
            final Profiler threadProfiler = new Profiler();
            final int threadId = i;
            Thread thread = new Thread(() -> {
//                System.out.printf("spawned thread id %d\n", threadId);
                seedsChecked.getAndIncrement();
                ArrayList<Long> treeSeeds = new ArrayList<>();

                // refind tree seeds
                long seed = revTCR.nextSeed(initialSeed);
                for (int callOffset = 0; callOffset < TREE_CALL_RANGE * 2; callOffset++) {
                    threadProfiler.switchSection("checkTrees");
                    if (checkTree0(seed) == 1) {
                        treeSeeds.add(seed);
                    }
                    if (checkTree1(seed) == 1) {
                        treeSeeds.add(seed);
                    }
                    if (checkTree2(seed) == 1) {
                        treeSeeds.add(seed);
                    }
                    if (checkTree3(seed) == 1) {
                        treeSeeds.add(seed);
                    }
//                    if (checkTree4(seed) == 1) {
//                        treeSeeds.add(seed);
//                    }
//                    if (checkTree5(seed) == 1) {
//                        treeSeeds.add(seed);
//                    }

                    seed = LCG.JAVA.nextSeed(seed);
                }

                if (treeSeeds.size() < TARGET_TREE_COUNT) {
                    return;
                }

                // full de-dodge
                // each seed must be exactly one of the valid tree distances.
                // this may end up leaving a few duplicates in there but the goal is
                // obviously not to eliminate any valid seeds.
                threadProfiler.switchSection("full dedodge");
                ArrayList<Long> treeSeedsToRemove = new ArrayList<>();
                long initialDfz = LCG.JAVA.distance(0, treeSeeds.get(0));
                long lastDfz = initialDfz;
                dedodgeLoop:
                for (int j = 0; j < treeSeeds.size(); j++) {
                    long treeSeed = treeSeeds.get(j);
                    long dfz = LCG.JAVA.distance(0, treeSeed);
                    // we loop thru all the valid tree distances
                    // if we hit a valid dfz, then this might be a real tree
                    // so we keep it
                    for (int validDfz : validTaigaTreeDistances) {
                        if (dfz - lastDfz == validDfz) {
                            lastDfz = dfz;
                            continue dedodgeLoop;
                        }
                    }
                    // if our current dfz is not a valid one, it's probably not a real tree
                    // so we remove it
                    treeSeedsToRemove.add(treeSeed);
                }
                // remove any seeds we eliminated from the list
                for (long l : treeSeedsToRemove) {
                    treeSeeds.remove(l);
                }
                // check that we still have enough trees to be considered
                // a match
                if (treeSeeds.size() < TARGET_TREE_COUNT) {
                    return;
                }
//            System.out.println("passed full dedodge");

                threadProfiler.switchSection("write");
                try {
                    seedsOutStream.write(initialSeed + "\n");
                    seedsOutStream.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                seedsPassed.getAndIncrement();
                threadProfiler.stop();
            });

            threads.add(thread);
            threadProfilers.add(threadProfiler);
        }



        // thread spawn and poll loop
        AtomicBoolean done = new AtomicBoolean(false);
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            int threadsAlive = 0;
            boolean[] threadsStarted = new boolean[threads.size()];
            boolean[] threadsDone = new boolean[threads.size()];

            @Override
            public void run() {
                mainThreadProfiler.switchSection("pollLoop");
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
                    }
                }

                if (seedsChecked.get() >= seedsIn.length) {
                    done.set(true);
                    timer.cancel();

                    mainThreadProfiler.stop();
                    System.out.println(Profiler.combine(Profiler.combine(threadProfilers), mainThreadProfiler).summary());

                    System.out.printf("seeds passed: %d\n", seedsPassed.get());
                    System.out.printf("checked %d seeds in %.3fs\n", seedsIn.length, (System.currentTimeMillis() - timeStart) / 1000F);

                    System.exit(0);
                }

                System.out.printf("progress: %6d / %6d, %6.2f%% results %6d, running for %.3fs\n", seedsChecked.get(), seedsIn.length, ((double) seedsChecked.get() / seedsIn.length) * 100D, seedsPassed.get(), (System.currentTimeMillis() - timeStart) / 1000F);
                mainThreadProfiler.stop();
            }
        };
        timer.schedule(timerTask, 0, 250);
    }

    public static int checkTree0(long seed) {
// tree 0
        if (((( seed *     25214903917L +              11L) >> 44) & 15) !=  8) return 0; // step  1 pos X
        if (((( seed * 205749139540585L +    277363943098L) >> 44) & 15) !=  9) return 0; // step  2 pos Z
        if (((( seed * 120950523281469L + 102626409374399L) >> 47) &  1) !=  0) return 0; // step  5 base height
        if (((( seed *  76790647859193L +  25707281917278L) >> 46) &  3)  <  1) return 0; // step  6 radius > 1
        if (((((seed * 233752471717045L +  11718085204285L) & 281474976710655L) >> 17) %  3) !=  0) return 0; // step  3 type
        if (((((seed *  55986898099985L +  49720483695876L) & 281474976710655L) >> 17) %  5) !=  0) return 0; // step  4 height

        return 1;
    }

    public static int checkTree1(long seed) {
// tree 1
        if (((( seed *     25214903917L +              11L) >> 44) & 15) !=  6) return 0; // step  1 pos X
        if (((( seed * 205749139540585L +    277363943098L) >> 44) & 15) != 14) return 0; // step  2 pos Z
        if (((( seed * 120950523281469L + 102626409374399L) >> 47) &  1) !=  1) return 0; // step  5 base height
        if (((((seed * 233752471717045L +  11718085204285L) & 281474976710655L) >> 17) %  3) !=  0) return 0; // step  3 type
        if (((((seed *  55986898099985L +  49720483695876L) & 281474976710655L) >> 17) %  5) !=  4) return 0; // step  4 height
        if (((((seed *  76790647859193L +  25707281917278L) & 281474976710655L) >> 17) %  5) !=  0) return 0; // step  6 radius == 1

        return 1;
    }

    public static int checkTree2(long seed) {
// tree 2
        if (((( seed *     25214903917L +              11L) >> 44) & 15) !=  1) return 0; // step  1 pos X
        if (((( seed * 205749139540585L +    277363943098L) >> 44) & 15) != 15) return 0; // step  2 pos Z
        if (((( seed * 120950523281469L + 102626409374399L) >> 47) &  1) !=  1) return 0; // step  5 base height
        if (((((seed * 233752471717045L +  11718085204285L) & 281474976710655L) >> 17) %  3) !=  0) return 0; // step  3 type
        if (((((seed *  55986898099985L +  49720483695876L) & 281474976710655L) >> 17) %  5) !=  0) return 0; // step  4 height
        if (((((seed *  76790647859193L +  25707281917278L) & 281474976710655L) >> 17) %  5)  <  1) return 0; // step  6 radius > 1

        return 1;
    }

    public static int checkTree3(long seed) {
// tree 3
        if (((( seed *     25214903917L +              11L) >> 44) & 15) !=  1) return 0; // step  1 pos X
        if (((( seed * 205749139540585L +    277363943098L) >> 44) & 15) !=  7) return 0; // step  2 pos Z
        if (((( seed * 120950523281469L + 102626409374399L) >> 47) &  1) !=  1) return 0; // step  5 base height
        if (((((seed * 233752471717045L +  11718085204285L) & 281474976710655L) >> 17) %  3) !=  0) return 0; // step  3 type
        if (((((seed *  55986898099985L +  49720483695876L) & 281474976710655L) >> 17) %  5) !=  4) return 0; // step  4 height
        if (((((seed *  76790647859193L +  25707281917278L) & 281474976710655L) >> 17) %  5)  <  2) return 0; // step  6 radius > 1

        return 1;
    }

    public static int checkTree4(long seed) {

        return 1;
    }

    public static int checkTree5(long seed) {

        return 1;
    }

    public static final int[] validTaigaTreeDistances = {
            0,
            6,
            8,
            12,
            14,
            16,
            18,
            20,
            22,
            24,
            26,
            28,
            30,
            32,
            34,
            36,
            38,
            40,
            42,
            44,
            46,
            48,
            50,
            52,
            54,
            56,
            58,
            60,
            62,
            64,
            66,
            68,
            70,
            72,
            74,
            76,
            78,
            80,
            82,
            84,
            86,
            88,
            90,
    };
}
