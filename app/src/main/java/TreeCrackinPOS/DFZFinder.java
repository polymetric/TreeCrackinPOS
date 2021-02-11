package TreeCrackinPOS;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.LongBuffer;

public class DFZFinder {
    public static void main(String[] args) {
        SeedFileBuffer treeFileA, treeFileB;
        treeFileA = new SeedFileBuffer("I:\\shotPseeds\\tree_l5_seeds.txt");
        treeFileB = new SeedFileBuffer("I:\\shotPseeds\\tree_l6_seeds.txt");

        long timeStart = System.currentTimeMillis();

        for (long treeIndexA = 0; treeIndexA < treeFileA.totalSeeds; treeIndexA++) {
            for (long treeIndexB = 0; treeIndexB < treeFileB.totalSeeds; treeIndexB++) {
                long seedA = treeFileA.getSeed(treeIndexA);
                long seedB = treeFileB.getSeed(treeIndexB);
                long dfzA = DiscreteLog.distanceFromZero(seedA);
                long dfzB = DiscreteLog.distanceFromZero(seedB);
                if (Math.abs(dfzA - dfzB) < 300) {
                    System.out.printf("match found: seedA = %15d seedB = %15d distance = %15d\n", seedA, seedB, dfzA - dfzB);
                }
            }
        }
    }

    static class SeedFileBuffer {
//        final int BUFFER_SIZE = 1 << 24;
        final int BUFFER_SIZE = 8;

        File file;
        InputStream stream;
        long offset;
        long[] seeds;
        long totalSeeds;

        public SeedFileBuffer(String path) {
            file = new File(path);
            try {
                stream = new FileInputStream(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                System.exit(1);
            }
            seeds = new long[BUFFER_SIZE];
            totalSeeds = file.length() / 8;
        }

        public void nextBuffer() {
            byte[] buffer = new byte[Long.BYTES * BUFFER_SIZE];
            try {
                stream.read(buffer, 0, Long.BYTES * BUFFER_SIZE);
            } catch (IOException e) {
                System.out.println("eror");
                e.printStackTrace();
                System.exit(1);
            }
            offset += Long.BYTES * BUFFER_SIZE;
            seeds = convertToLongArray(buffer);
        }

        public long getSeed(long index) {
            if (index % BUFFER_SIZE == 0) {
                nextBuffer();
            }
            return seeds[(int) (index % BUFFER_SIZE)];
        }
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
}
