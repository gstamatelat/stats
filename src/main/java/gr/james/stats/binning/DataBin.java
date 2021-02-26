package gr.james.stats.binning;

import java.util.Objects;

/**
 * Represents the result of a data binning.
 */
public class DataBin<X, Y> {
    /**
     * The value of this data bin, typically frequency or probability.
     */
    public final Y value;

    /**
     * The left end of this data bin.
     */
    public final X left;

    /**
     * The right end of this data bin.
     */
    public final X right;

    /**
     * Construct a new data bin from the given parameters.
     *
     * @param value the value of the data bin
     * @param left  the left end of the data bin
     * @param right the right end of the data bin
     */
    public DataBin(Y value, X left, X right) {
        this.value = Objects.requireNonNull(value);
        this.left = Objects.requireNonNull(left);
        this.right = Objects.requireNonNull(right);
    }
}
