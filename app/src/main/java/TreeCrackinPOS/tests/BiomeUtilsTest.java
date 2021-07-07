package TreeCrackinPOS.tests;

import kaptainwutax.biomeutils.source.OverworldBiomeSource;
import kaptainwutax.seedutils.mc.MCVersion;

import javax.swing.*;
import java.awt.*;

public class BiomeUtilsTest {
    public static void main(String[] args) {
        OverworldBiomeSource biomeSource = new OverworldBiomeSource(MCVersion.v1_16_4, Long.parseLong("4915687630941437374"));
        System.out.println(biomeSource.getBiome(278, 0, 327).getName());
    }
}
