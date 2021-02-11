package TreeCrackinPOS;

import sun.security.ec.ECDSAOperations;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.LongBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class DFZDump {
    public static void main(String[] args) throws Exception {
        final int THREAD_COUNT = 12;
        final String SEEDS_IN = "I:\\shotPSeeds2\\tree_l7_seeds.txt";
        final String DFZS_OUT = "I:\\shotPSeeds2\\tree_l7_seeds.txt";

        long timeStart = System.currentTimeMillis();
        long totalSeeds = new File(SEEDS_IN).length() / 8;
//        long totalSeeds = 33554432L;
        InputStream seedsInStream = Files.newInputStream(Paths.get(SEEDS_IN));
        long seedsDone = 0;

        System.out.printf("opened file with %d seeds\n", totalSeeds);

        ArrayList<Thread> threads = new ArrayList<Thread>();
        for (int t = 0; t < THREAD_COUNT; t++) {
            long tasks_per_thread = (totalSeeds / THREAD_COUNT);
            long start = tasks_per_thread * t;
            long end = tasks_per_thread * (t + 1);

            int tid = t;
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    long seedsDone = 0;
                    for (long seedIndex = start; seedIndex < end; seedIndex++) {
                        long seed = getNextSeedFromFile(seedsInStream);
                        long dfz = DiscreteLog.distanceFromZero(seed);
                        if (seedsDone % 100000 == 0) {
                            long now = System.currentTimeMillis();
                            if (tid == 0) {
                                System.out.printf("thread %2d: running at %15.3f sps\n", tid, seedsDone / ((now - timeStart) / 1e3));
                                System.out.printf("thread %2d: progress: %15d/%15d, %6.2f\n", tid, seedsDone, (end-start), (double) seedsDone / (end-start) * 100);
                            }
                        }
                        seedsDone++;
                    }
                }
            });
            thread.start();
            threads.add(thread);
        }
        for (Thread t : threads) {
            t.join();
        }
        System.out.printf("done in %.3fs\n", (System.currentTimeMillis() - timeStart) / 1000);
    }

    static long[] convertToLongArray(byte[] bArray) {
        ByteBuffer byteBuffer = ByteBuffer.wrap(bArray);
        LongBuffer longBuffer = byteBuffer.asLongBuffer();
        long l[] = new long[longBuffer.capacity()];
        longBuffer.get(l);
        for (int i = 0; i < l.length; i++) {
            l[i] = Long.reverseBytes(l[i]);
        }
        return l;
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
}
