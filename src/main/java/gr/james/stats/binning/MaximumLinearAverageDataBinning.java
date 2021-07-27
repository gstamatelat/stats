package gr.james.stats.binning;

import java.util.List;
import java.util.SortedMap;

/**
 * Linear average binning of data using the maximum bin count for which no empty bins exist.
 */
public class MaximumLinearAverageDataBinning implements DataBinning {
    /**
     * Construct a new instance.
     */
    public MaximumLinearAverageDataBinning() {
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
        final MaximumLinearDataBinning mldb = new MaximumLinearDataBinning();
        final int size = mldb.bin(frequency).size();
        return new LinearAverageDataBinning(size).bin(frequency);
    }
}
