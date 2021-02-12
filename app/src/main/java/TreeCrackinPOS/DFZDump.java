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
        final int THREAD_COUNT = 8;
        final String SEEDS_IN = "I:\\shotPSeeds2\\tree_l5_seeds.txt";
        final String DFZS_OUT = "I:\\shotPSeeds2\\tree_l5_dfzs.txt";

        // sanity checks because i accidentally overwrote an 8G file that took hours to generate lol
        if (SEEDS_IN == DFZS_OUT) {
            System.out.println("SEEDS_IN is identical to DFZS_OUT. this would result in overwriting the input file.");
            System.out.println("stopping.");
            System.exit(-1);
        }
        if (DFZS_OUT.contains("seeds")) {
            System.out.println("DFZS_OUT might have accidentally been set to write to an input file this would result in overwriting!");
            System.out.println("stopping.");
            System.exit(-1);
        }
        if (new File(DFZS_OUT).exists()) {
            System.out.println("DFZS_OUT already exists. this might result in overwriting data.");
            System.out.println("stopping.");
            System.exit(-1);
        }

        long timeStart = System.currentTimeMillis();
        long totalSeeds = new File(SEEDS_IN).length() / 8;
//        long totalSeeds = 33554432L;
        InputStream seedsInStream = new BufferedInputStream(new FileInputStream(SEEDS_IN));
        OutputStream dfzOutStream = new BufferedOutputStream(new FileOutputStream(DFZS_OUT));

        long seedsDone = 0;

        System.out.printf("opened file with %d seeds\n", totalSeeds);

        ArrayList<Thread> threads = new ArrayList<Thread>();
        for (int t = 0; t < THREAD_COUNT; t++) {
            long tasks_per_thread = (totalSeeds / THREAD_COUNT);
            long start = tasks_per_thread * t;
            long end = tasks_per_thread * (t + 1);

            int tid = t;
            Thread thread = new Thread(() -> {
                long seedsDone1 = 0;
                ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
                for (long seedIndex = start; seedIndex < end; seedIndex++) {
                    buffer.clear();
                    buffer.putLong(DiscreteLog.distanceFromZero(getNextSeedFromFile(seedsInStream)));
                    buffer.flip();
                    try {
                        dfzOutStream.write(buffer.array());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (seedsDone1 % 100000 == 0) {
                        long now = System.currentTimeMillis();
                        if (tid == 0) {
                            System.out.printf("thread %2d: running at %15.3f sps\n", tid, seedsDone1 / ((now - timeStart) / 1e3));
                            System.out.printf("thread %2d: progress: %15d/%15d, %6.2f\n", tid, seedsDone1, (end-start), (double) seedsDone1 / (end-start) * 100);
                        }
                    }
                    seedsDone1++;
                }
            });
            thread.start();
            threads.add(thread);
        }
        for (Thread t : threads) {
            t.join();
        }
        System.out.printf("done in %.3fs\n", (System.currentTimeMillis() - timeStart) / 1000.0D);
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
