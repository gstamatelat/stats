package gr.james.stats.utils;

import org.junit.Assert;
import org.junit.Test;

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
}
