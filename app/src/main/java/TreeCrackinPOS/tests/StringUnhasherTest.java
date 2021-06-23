package TreeCrackinPOS.tests;

import kaptainwutax.mcutils.rand.seed.WorldSeed;
import kaptainwutax.mcutils.util.data.StringUnhasher;

import java.util.concurrent.atomic.AtomicInteger;

public class StringUnhasherTest {
    public static void main(String[] args) {
//        System.out.println("gamers".hashCode());

        AtomicInteger totalResults = new AtomicInteger();

        StringUnhasher.Config config = StringUnhasher.newConfig();
        WorldSeed.toString(-1253235277, config, s -> {
            totalResults.getAndIncrement();
            if (s == "gamers") {
                System.out.println(s);
                return false;
            }
            return true;
        });
    }
}
