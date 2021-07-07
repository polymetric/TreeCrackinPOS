package TreeCrackinPOS.utils;

public class ProfilerTest1 {
    public static void main(String[] args) throws Exception {
        Profiler p = new Profiler();
        p.switchSection("a");
        Thread.sleep(1000);
        p.switchSection("b");
        Thread.sleep(250);
        p.switchSection("c");
        Thread.sleep(1);
        p.switchSection("a");
        Thread.sleep(1000);
        p.stop();
        System.out.println(p.summary());
    }
}
