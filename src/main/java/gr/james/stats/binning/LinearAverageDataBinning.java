package gr.james.stats.binning;

import java.util.*;

/**
 * Linear binning of data, where the size of the bin is determined by the average frequency of the individual x values.
 * <p>
 * The operation of this binning method is in contrast with {@link LinearDataBinning} where the size of each bin is
 * determined by the sum of the frequencies of individual x values.
 */
public class LinearAverageDataBinning implements DataBinning {
    private final int bins;

    /**
     * Construct a new instance with the given bin count.
     *
     * @param bins the bin count
     * @throws IllegalArgumentException if {@code bins} is less than 1
     */
    public LinearAverageDataBinning(int bins) {
        if (bins < 1) {
            throw new IllegalArgumentException("the number of bins must be at least 1");
        }
        this.bins = bins;
    }

    /**
     * {@inheritDoc}
     *
     * @param frequency {@inheritDoc}
     * @return {@inheritDoc}
     * @throws NullPointerException     if {@code frequency} is {@code null}
     * @throws IllegalArgumentException if {@code frequency} contains less than two distinct values
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
        final long[] groupsCount = new long[bins];
        final double[] limits = new double[bins + 1];

        double step = (maxKey - minKey) / bins;
        for (int i = 0; i <= bins; i++) {
            limits[i] = minKey + i * step;
        }

        int currentBin = 0;
        for (Map.Entry<? extends Number, ? extends Number> e : frequency.entrySet()) {
            while (e.getKey().doubleValue() > limits[currentBin] && currentBin < bins - 1) {
                currentBin++;
            }
            groups[currentBin] += e.getValue().doubleValue();
            groupsCount[currentBin] += 1;
        }

        for (int i = 0; i < groups.length; i++) {
            binsList.add(new DataBin<>(
                    groups[i] / groupsCount[i],
                    limits[i],
                    limits[i + 1],
                    (limits[i] + limits[i + 1]) / 2
            ));
        }

        assert Math.abs(frequency.values().stream().mapToDouble(Number::doubleValue).sum() - Arrays.stream(groups).sum()) < 1.0e-4;

        return Collections.unmodifiableList(binsList);
    }
}
