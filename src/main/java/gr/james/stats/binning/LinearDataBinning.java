package gr.james.stats.binning;

import java.util.*;

/**
 * Linear binning of data.
 */
public class LinearDataBinning implements DataBinning {
    private final int bins;

    /**
     * Construct a new instance with the given bin count.
     *
     * @param bins the bin count
     * @throws IllegalArgumentException if {@code bins} is less than 1
     */
    public LinearDataBinning(int bins) {
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
    public List<DataBin<Double, Long>> bin(SortedMap<? extends Number, Long> frequency) {
        if (frequency.size() < 2) {
            throw new IllegalArgumentException("data must contain at least two distinct values");
        }

        final List<DataBin<Double, Long>> binsList = new ArrayList<>();

        final double minKey = frequency.firstKey().doubleValue();
        final double maxKey = frequency.lastKey().doubleValue();

        final long[] groups = new long[bins];
        final double[] limits = new double[bins + 1];

        double step = (maxKey - minKey) / bins;
        for (int i = 0; i <= bins; i++) {
            limits[i] = minKey + i * step;
        }

        int currentBin = 0;
        for (Map.Entry<? extends Number, Long> e : frequency.entrySet()) {
            while (e.getKey().doubleValue() > limits[currentBin] && currentBin < bins - 1) {
                currentBin++;
            }
            groups[currentBin] += e.getValue();
        }

        for (int i = 0; i < groups.length; i++) {
            binsList.add(new DataBin<>(groups[i], limits[i], limits[i + 1]));
        }

        assert frequency.values().stream().mapToLong(i -> i).sum() == Arrays.stream(groups).sum();

        return Collections.unmodifiableList(binsList);
    }
}
