package gr.james.stats.utils;

import gr.james.stats.binning.LinearDataBinning;
import gr.james.stats.binning.LogarithmicDataBinning;
import org.junit.Assert;
import org.junit.Test;

import java.util.Random;

public class FrequencyTests {
    /**
     * Test the mode.
     */
    @Test
    public void modeTest() {
        final Frequency d = new Frequency();
        d.add(1.0);
        d.add(2.0);
        d.add(3.0);
        d.add(3.0);
        d.add(2.0);
        Assert.assertEquals(2.0, d.mode(), 1e-4);
    }

    /**
     * Test the binning function.
     */
    @Test
    public void binning() {
        final Random r = new Random(12345L);
        final Frequency d = new Frequency();
        for (int i = 0; i < 1000; i++) {
            d.add(r.nextInt(20) + 1);
        }
        d.bin(new LinearDataBinning(r.nextInt(3) + 2));
        d.bin(new LogarithmicDataBinning(r.nextInt(3) + 2, 2));
        d.bin(new LogarithmicDataBinning(r.nextInt(3) + 2, 10));
        d.bin(new LogarithmicDataBinning(r.nextInt(3) + 2, Math.exp(1.0)));
    }

    /**
     * Tets for the normalize method.
     */
    @Test
    public void normalize() {
        final Random r = new Random(53800L);
        final Frequency d = new Frequency();
        for (int i = 0; i < 1000; i++) {
            d.add(r.nextInt(20) + 1);
        }
        Assert.assertEquals(1.0, d.normalize().sum(), 1e-4);
    }
}
