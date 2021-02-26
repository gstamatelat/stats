package gr.james.stats.binning;

import java.util.List;
import java.util.SortedMap;

/**
 * Represents a data binning algorithm.
 */
public interface DataBinning {
    /**
     * Perform binning on the input data
     *
     * @param frequency the input data as a frequency map
     * @return a list of {@link DataBin data bins}
     */
    List<DataBin<Double, Long>> bin(SortedMap<? extends Number, Long> frequency);
}
