package gr.james.stats.utils;

import gr.james.stats.binning.DataBin;
import gr.james.stats.binning.DataBinning;

import java.util.*;
import java.util.function.BiFunction;

/**
 * Represents a distribution of double values into decimal frequencies.
 */
public class Distribution {
    private final TreeMap<Double, Double> dist;

    /**
     * Construct a new empty {@link Distribution}.
     */
    public Distribution() {
        this.dist = new TreeMap<>();
    }

    /**
     * Associate a frequency with a value.
     *
     * @param value     the value
     * @param frequency the frequency
     */
    public void put(double value, double frequency) {
        if (!Double.isFinite(value)) {
            throw new IllegalArgumentException("value must be finite");
        }
        if (!Double.isFinite(frequency)) {
            throw new IllegalArgumentException("frequency must be finite");
        }
        this.dist.put(value, frequency);
    }

    /**
     * Returns a read-only view of the underlying frequency map of this distribution.
     *
     * @return a read-only view of the underlying frequency map of this distribution
     */
    public SortedMap<Double, Double> map() {
        return Collections.unmodifiableNavigableMap(dist);
    }

    /**
     * Returns the sum of all frequencies in the distribution
     *
     * @return the sum of all frequencies in the distribution
     */
    public double sum() {
        double sum = 0;
        for (double v : dist.values()) {
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
        double maxFrequency = 0;
        double maxValue = dist.firstKey();
        for (Map.Entry<Double, Double> e : dist.entrySet()) {
            if (e.getValue() > maxFrequency) {
                maxValue = e.getKey();
                maxFrequency = e.getValue();
            }
        }
        return maxValue;
    }

    /**
     * Output the distribution in stdout.
     *
     * @param format the format of the line
     */
    public void print(BiFunction<Double, Double, String> format) {
        for (Map.Entry<Double, Double> e : dist.entrySet()) {
            System.out.print(format.apply(e.getKey(), e.getValue()));
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
     * Return a normalized distribution for which the frequencies sum to 1.
     *
     * @return a normalized distribution for which the frequencies sum to 1
     */
    public Distribution normalize() {
        final double sum = sum();
        final Distribution dd = new Distribution();
        for (Map.Entry<Double, Double> e : dist.entrySet()) {
            dd.put(e.getKey(), e.getValue() / sum);
        }
        assert Math.abs(dd.sum() - 1.0) < 1e-4;
        return dd;
    }

    /**
     * Returns a new distribution by filtering out all elements with zero frequency.
     *
     * @return a new distribution by filtering out all elements with zero frequency
     */
    public Distribution purge() {
        final Distribution dd = new Distribution();
        for (Map.Entry<Double, Double> e : dist.entrySet()) {
            if (e.getValue() != 0.0) {
                dd.dist.put(e.getKey(), e.getValue());
            }
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
    public Distribution tail(double value) {
        final Distribution dd = new Distribution();
        for (Map.Entry<Double, Double> e : dist.entrySet()) {
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
    public Distribution head(double value) {
        final Distribution dd = new Distribution();
        for (Map.Entry<Double, Double> e : dist.entrySet()) {
            if (e.getKey() < value) {
                dd.dist.put(e.getKey(), e.getValue());
            }
        }
        return dd;
    }
}
