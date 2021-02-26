package gr.james.stats.utils;

import org.junit.Assert;
import org.junit.Test;

import java.util.Random;

public class DistributionTests {
    /**
     * Test the mode.
     */
    @Test
    public void modeTest() {
        final Distribution d = new Distribution();
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
        final Distribution d = new Distribution();
        for (int i = 0; i < 1000; i++) {
            d.add(r.nextInt(20));
        }
        d.bin(r.nextInt(20) + 5, Distribution.Space.Linear);
    }
}
