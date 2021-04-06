package gr.james.stats.utils;

import org.junit.Assert;
import org.junit.Test;

public class WelfordVarianceTests {
    /**
     * Test the average.
     */
    @Test
    public void average() {
        final WelfordVariance wv = new WelfordVariance();
        wv.add(1.0);
        wv.add(3.0);
        wv.add(5.0);
        Assert.assertEquals(3.0, wv.mean(), 1.0e-8);
    }

    /**
     * Test the stdev.
     */
    @Test
    public void stdev() {
        final WelfordVariance wv = new WelfordVariance();
        wv.add(2.0);
        wv.add(4.0);
        wv.add(4.0);
        wv.add(4.0);
        wv.add(5.0);
        wv.add(5.0);
        wv.add(7.0);
        wv.add(9.0);
        Assert.assertEquals(4.0, wv.populationVariance(), 1.0e-8);
    }
}
