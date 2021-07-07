package TreeCrackinPOS.utils;

public class ProfilerTest2 {
    public static void main(String[] args) throws Exception {
        Profiler pa = new Profiler();
        pa.switchSection("a");
        Thread.sleep(1000);
        pa.stop();
        Profiler pb = new Profiler();
        pb.switchSection("a");
        Thread.sleep(500);
        pb.switchSection("b");
        Thread.sleep(250);
        pb.stop();
        System.out.println(Profiler.combine(pa, pb).summary());
    }
}
