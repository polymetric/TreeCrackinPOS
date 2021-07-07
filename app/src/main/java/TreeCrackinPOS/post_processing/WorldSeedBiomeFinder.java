package TreeCrackinPOS.post_processing;

import TreeCrackinPOS.utils.Profiler;
import kaptainwutax.biomeutils.Biome;
import kaptainwutax.biomeutils.source.OverworldBiomeSource;
import kaptainwutax.seedutils.mc.MCVersion;
import kaptainwutax.seedutils.mc.pos.BPos;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static TreeCrackinPOS.utils.Utils.*;

public class WorldSeedBiomeFinder {
    public static final MCVersion version = MCVersion.v1_7_10;

    public static void main(String[] args) throws Exception {
        final int THREADS = 8000;
        final String seedsInPath = "worldseeds_funny_1.txt";
        final String seedsOutPath = "worldseeds_funny_filtered_1.txt";

        Profiler mainThreadProfiler = new Profiler();
        mainThreadProfiler.switchSection("init");

        final String[] seedsIn = readFileToString(seedsInPath).split("\n");
//        final String[] seedsIn = { "8442691381203171672" };
        File seedsOutFile = new File(seedsOutPath);
        seedsOutFile.delete();
        seedsOutFile.createNewFile();
        FileWriter seedsOutStream = new FileWriter(seedsOutPath);

        final ArrayList<BiomeSample> biomeSamples = new ArrayList<>();
        // funny seed
        biomeSamples.add(new BiomeSample(4, 73, 256, Biome.PLAINS));
        biomeSamples.add(new BiomeSample(28, 79, 264, Biome.PLAINS));

        biomeSamples.add(new BiomeSample(58, 77, 289, Biome.TAIGA));
        biomeSamples.add(new BiomeSample(10, 79, 310, Biome.TAIGA));
        biomeSamples.add(new BiomeSample(-34, 70, 302, Biome.PLAINS));
        biomeSamples.add(new BiomeSample(-86, 81, 351, Biome.TAIGA));
        biomeSamples.add(new BiomeSample(-86, 81, 351, Biome.TAIGA_HILLS));

//        biomeSamples.add(new BiomeSample(-52, 79, 355, Biome.RIVER));

        biomeSamples.add(new BiomeSample(-18, 71, 246, Biome.PLAINS));
        biomeSamples.add(new BiomeSample(-18, 81, 202, Biome.TAIGA));
        final int biomesNeededForMatch = 8;
        // test seed
//        biomeSamples.add(new BiomeSample(128, 97, 702, Biome.TAIGA));
//        biomeSamples.add(new BiomeSample(128, 97, 702, Biome.TAIGA_HILLS));
//        biomeSamples.add(new BiomeSample(408, 84, 899, Biome.PLAINS));
//        biomeSamples.add(new BiomeSample(12, 120, 782, Biome.TAIGA));
//        biomeSamples.add(new BiomeSample(12, 120, 782, Biome.TAIGA_HILLS));
//        biomeSamples.add(new BiomeSample(95, 119, 883, Biome.PLAINS));
//        final int biomesNeededForMatch = 4;

        ArrayList<Thread> threads = new ArrayList<>();
        ArrayList<Profiler> threadProfilers = new ArrayList<>();
        threadProfilers.add(mainThreadProfiler);
        AtomicInteger seedsChecked = new AtomicInteger();
        AtomicInteger seedsPassed = new AtomicInteger();
        long timeStart = System.currentTimeMillis();
        int curThreadId = 0;

        mainThreadProfiler.switchSection("threadSpawning");
        for (String worldSeedStr : seedsIn) {
            final long worldSeed = Long.parseLong(worldSeedStr);
            final Profiler threadProfiler = new Profiler();
            final int threadId = curThreadId;
            curThreadId++;

            Thread thread = new Thread(() -> {
                threadProfiler.switchSection("biomeSource");
                OverworldBiomeSource biomeSource = new OverworldBiomeSource(version, worldSeed);
                threadProfiler.switchSection("biomeTesting");
                int matches = 0;
                for (BiomeSample bs : biomeSamples) {
                    Biome testBiome = biomeSource.getBiome(bs.pos);
                    if (bs.biome.getId() == testBiome.getId()) {
                        matches++;
                    }
                }
                threadProfiler.switchSection("");
                if (matches >= biomesNeededForMatch) {
                    System.out.println(worldSeed);
                    try {
                        seedsOutStream.write(worldSeed + "\n");
                        seedsOutStream.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    seedsPassed.getAndIncrement();
                }
                seedsChecked.getAndIncrement();
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

                    try {
                        seedsOutStream.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    mainThreadProfiler.stop();
//                        System.out.println(Profiler.combine(Profiler.combine(threadProfilers), mainThreadProfiler).summary());
                }

                System.out.printf("progress: %6d / %6d, %6.2f%% results %6d, running for %.3fs\n", seedsChecked.get(), seedsIn.length, ((double) seedsChecked.get() / seedsIn.length) * 100D, seedsPassed.get(), (System.currentTimeMillis() - timeStart) / 1000F);
                mainThreadProfiler.stop();
            }
        };
        timer.schedule(timerTask, 0, 250);
    }

    public static class BiomeSample {
        BPos pos;
        Biome biome;

        public BiomeSample(int x, int y, int z, Biome biome) {
            this.pos = new BPos(x, y, z);
            this.biome = biome;
        }
    }
}
