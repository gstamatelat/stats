package gr.james.stats.utils;

import gr.james.stats.binning.DataBin;
import gr.james.stats.binning.DataBinning;

import java.util.*;

/**
 * Represents a distribution of double values into integer frequencies.
 */
public class Frequency {
    private final TreeMap<Double, Long> dist = new TreeMap<>();

    /**
     * Construct a new empty {@link Frequency}.
     */
    public Frequency() {
    }

    @Deprecated
    public static void main(String[] args) {
        final Frequency d = new Frequency();
        for (int i = 1; i < 100; i++) {
            final long frequency = (long) (10000 * Math.pow(i, -2));
            for (long f = 0; f < frequency; f++) {
                d.add(i);
            }
        }
        Plotting.logLog(d.toDistribution(), "LogLog", "X", "Y");
        Plotting.logLogBins(d.toDistribution(), 25, "LogLogBins", "X", "Y");
        Plotting.linear(d.toDistribution(), "Linear", "X", "Y");
        Plotting.linearBins(d.toDistribution(), 25, "LinearBins", "X", "Y");
    }

    /**
     * Convert this {@link Frequency} into a {@link Distribution}.
     *
     * @return a new {@link Distribution} from this {@link Frequency}
     */
    public Distribution toDistribution() {
        final Distribution d = new Distribution();
        for (Map.Entry<Double, Long> e : dist.entrySet()) {
            d.put(e.getKey(), (double) e.getValue());
        }
        return d;
    }

    /**
     * Add a single observation.
     * <p>
     * If the value exists, its frequency will be increased by 1.
     *
     * @param t the observation value
     */
    public void add(double t) {
        if (!Double.isFinite(t)) {
            throw new IllegalArgumentException("argument must be finite");
        }
        dist.merge(t, 1L, Long::sum);
    }

    /**
     * Returns a read-only view of the underlying frequency map of this distribution.
     *
     * @return a read-only view of the underlying frequency map of this distribution
     */
    public SortedMap<Double, Long> map() {
        return Collections.unmodifiableNavigableMap(dist);
    }

    /**
     * Returns the sum of all frequencies in the distribution
     *
     * @return the sum of all frequencies in the distribution
     */
    public long sum() {
        long sum = 0;
        for (long v : dist.values()) {
            sum += v;
        }
        return sum;
    }

    /**
     * Returns the mode of this distribution, i.e. the most frequent value in the data set.
     * <p>
     * If the first rank is tied with more than 1 element, this method returns the lowest key.
     *
     * @return the mode of this distribution
     * @throws java.util.NoSuchElementException if this distribution is empty
     */
    public double mode() {
        long maxFrequency = 0;
        double maxValue = dist.firstKey();
        for (Map.Entry<Double, Long> e : dist.entrySet()) {
            if (e.getValue() > maxFrequency) {
                maxValue = e.getKey();
                maxFrequency = e.getValue();
            }
        }
        return maxValue;
    }

    /**
     * Output the distribution in stdout as comma separated x,y values.
     */
    public void print() {
        for (Map.Entry<Double, Long> e : dist.entrySet()) {
            System.out.printf("%f,%d%n", e.getKey(), e.getValue());
        }
    }

    /**
     * Returns a new {@link Distribution} by binning this distribution.
     *
     * @param binning the binning method
     * @return a new {@link Distribution} by binning this distribution
     */
    public Distribution bin(DataBinning binning) {
        final List<DataBin<Double, Double>> bb = binning.bin(dist);
        final Distribution dd = new Distribution();
        for (DataBin<Double, Double> b : bb) {
            dd.put(b.center(), b.value);
        }
        return dd;
    }

    /**
     * Returns the tail of the distribution that is formed by values greater than {@code value}.
     * <p>
     * More formally, returns a new distribution with values strictly greater than {@code value}.
     *
     * @param value the value dictating the tail of the distribution
     * @return the tail of the distribution that is formed by values greater than {@code value}
     */
    public Frequency tail(double value) {
        final Frequency dd = new Frequency();
        for (Map.Entry<Double, Long> e : dist.entrySet()) {
            if (e.getKey() > value) {
                dd.dist.put(e.getKey(), e.getValue());
            }
        }
        return dd;
    }

    /**
     * Returns the head of the distribution that is formed by values smaller than {@code value}.
     * <p>
     * More formally, returns a new distribution with values strictly smaller than {@code value}.
     *
     * @param value the value dictating the head of the distribution
     * @return the head of the distribution that is formed by values smaller than {@code value}
     */
    public Frequency head(double value) {
        final Frequency dd = new Frequency();
        for (Map.Entry<Double, Long> e : dist.entrySet()) {
            if (e.getKey() < value) {
                dd.dist.put(e.getKey(), e.getValue());
            }
        }
        return dd;
    }
}