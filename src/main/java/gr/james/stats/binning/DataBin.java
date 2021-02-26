package gr.james.stats.binning;

import java.util.Objects;

/**
 * Represents the result of a data binning.
 *
 * @param <X> the type of elements representing the range of this data bin
 * @param <Y> the type of value
 */
public class DataBin<X extends Number, Y> {
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

    /**
     * Returns the center of this data bin, which is the average of {@link #left} and {@link #right}.
     *
     * @return the center of this data bin, which is the average of {@link #left} and {@link #right}
     */
    public double center() {
        return (left.doubleValue() + right.doubleValue()) / 2;
    }
}
