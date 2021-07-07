package TreeCrackinPOS;

import kaptainwutax.seedutils.lcg.LCG;

public class KernelSanityCheck2 {
    public static void main(String[] args) {
        final long SEEDSPACE_MAX = (1L << 44);
        final long THREAD_BATCH_SIZE = (1L << 31);
        final long BLOCK_SIZE = (1L << 10);

        long timeStart = System.currentTimeMillis();

        long kernelOffset = 0;
        for (; kernelOffset < SEEDSPACE_MAX; kernelOffset += THREAD_BATCH_SIZE) {
            System.out.println(String.format("%64s", (Long.toBinaryString(kernelOffset))).replace(' ', '0'));
        }


        System.out.printf("finished in %6.3fs\n", (System.currentTimeMillis() - timeStart) / 1000.0);
    }
}
