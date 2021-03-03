package gr.james.stats.binning;

import java.util.*;

/**
 * Linear binning of data using the maximum bin count for which no empty bins exist.
 */
public class MaximumLinearDataBinning implements DataBinning {
    /**
     * Construct a new instance.
     */
    public MaximumLinearDataBinning() {
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

        List<DataBin<Double, Double>> previousBinList = null;

        for (int bins = 2; ; bins++) {
            final List<DataBin<Double, Double>> binsList = new ArrayList<>();

            final double minKey = frequency.firstKey().doubleValue();
            final double maxKey = frequency.lastKey().doubleValue();

            final double[] groups = new double[bins];
            final double[] limits = new double[bins + 1];

            double step = (maxKey - minKey) / bins;
            for (int i = 0; i <= bins; i++) {
                limits[i] = minKey + i * step;
            }

            int currentBin = 0;
            for (Map.Entry<? extends Number, ? extends Number> e : frequency.entrySet()) {
                while (e.getKey().doubleValue() > limits[currentBin] && currentBin < bins - 1) {
                    if (groups[currentBin] == 0) {
                        assert previousBinList != null;
                        return Collections.unmodifiableList(previousBinList);
                    }
                    currentBin++;
                }
                groups[currentBin] += e.getValue().doubleValue();
            }

            for (int i = 0; i < groups.length; i++) {
                binsList.add(new DataBin<>(
                        groups[i],
                        limits[i],
                        limits[i + 1],
                        (limits[i] + limits[i + 1]) / 2
                ));
            }

            assert Math.abs(frequency.values().stream().mapToDouble(Number::doubleValue).sum() - Arrays.stream(groups).sum()) < 1.0e-4;

            previousBinList = binsList;
        }
    }
}
