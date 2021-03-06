package gr.james.stats.binning;

import java.util.*;
import java.util.function.ToDoubleFunction;

/**
 * Logarithmic binning of data.
 */
public class LogarithmicDataBinning implements DataBinning {
    private final int bins;
    private final double base;

    /**
     * Construct a new instance with the given bin count and base.
     *
     * @param bins the bin count
     * @param base the base of the logarithm
     * @throws IllegalArgumentException if {@code bins} is less than 1
     * @throws IllegalArgumentException if {@code base} is less than 2
     */
    public LogarithmicDataBinning(int bins, double base) {
        if (bins < 1) {
            throw new IllegalArgumentException("the number of bins must be at least 1");
        }
        if (base < 2) {
            throw new IllegalArgumentException("the base of the logarithm must be at least 2");
        }
        this.bins = bins;
        this.base = base;
    }

    /**
     * {@inheritDoc}
     *
     * @param frequency {@inheritDoc}
     * @return {@inheritDoc}
     * @throws NullPointerException     if {@code frequency} is {@code null}
     * @throws IllegalArgumentException if {@code frequency} contains less than two distinct values
     * @throws IllegalArgumentException if {@code frequency} contains non-positive values
     */
    @Override
    public List<DataBin<Double, Double>> bin(SortedMap<? extends Number, ? extends Number> frequency) {
        if (frequency.size() < 2) {
            throw new IllegalArgumentException("data must contain at least two distinct values");
        }

        final List<DataBin<Double, Double>> binsList = new ArrayList<>();

        final double minKey = frequency.firstKey().doubleValue();
        final double maxKey = frequency.lastKey().doubleValue();

        final double[] groups = new double[bins];
        final double[] limits = new double[bins + 1];

        if (minKey <= 0) {
            throw new IllegalArgumentException("data contains non positive values");
        }

        double step = (Math.log(maxKey) / Math.log(base) - Math.log(minKey) / Math.log(base)) / bins;
        for (int i = 0; i <= bins; i++) {
            limits[i] = minKey * Math.pow(base, i * step);
        }

        int currentBin = 0;
        for (Map.Entry<? extends Number, ? extends Number> e : frequency.entrySet()) {
            while (e.getKey().doubleValue() > limits[currentBin] && currentBin < bins - 1) {
                currentBin++;
            }
            groups[currentBin] += e.getValue().doubleValue();
        }

        final double originalSum = frequency.values().stream().mapToDouble((ToDoubleFunction<Number>) Number::doubleValue).sum();
        double sumOfGroups = 0;
        for (int i = 0; i < groups.length; i++) {
            final double newValue = groups[i] / (limits[i + 1] - limits[i]);
            sumOfGroups += newValue;
        }
        final double ratio = originalSum / sumOfGroups;

        for (int i = 0; i < groups.length; i++) {
            binsList.add(new DataBin<>(
                    ratio * groups[i] / (limits[i + 1] - limits[i]),
                    limits[i],
                    limits[i + 1],
                    Math.pow(base, 0.5 * Math.log(limits[i] * limits[i + 1]) / Math.log(base))
            ));
        }

        return Collections.unmodifiableList(binsList);
    }
}
