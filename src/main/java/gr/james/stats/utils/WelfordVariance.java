package gr.james.stats.utils;

/**
 * Implementation of Welford's online variance algorithm.
 * <p>
 * The method computes the variance of a sample in one pass using constant memory and allows querying the variance at
 * any point.
 */
public class WelfordVariance {
    private double m;
    private double sum;
    private long observations;

    /**
     * Construct a new instance of {@link WelfordVariance} with no observations.
     */
    public WelfordVariance() {
        this.m = 0;
        this.sum = 0;
        this.observations = 0;
    }

    /**
     * Add an observation.
     * <p>
     * This method updates the state of the instance to reflect the new observation added.
     *
     * @param observation the observation
     */
    public void add(double observation) {
        final double previousMean = this.observations != 0 ? this.sum / this.observations : 0;

        this.observations = Math.addExact(this.observations, 1);
        this.sum += observation;

        final double nextMean = this.sum / this.observations;
        m += (observation - nextMean) * (observation - previousMean);
    }

    /**
     * Returns the total number of observations that have been added in this instance.
     *
     * @return the total number of observations that have been added in this instance
     */
    public long observations() {
        return this.observations;
    }

    /**
     * Returns the sum of all observations that have been added in this instance.
     *
     * @return the sum of all observations that have been added in this instance
     */
    public double sum() {
        return this.sum;
    }

    /**
     * Returns the population variance of the observations that have been added to this instance.
     *
     * @return the population variance of the observations that have been added to this instance
     */
    public double populationVariance() {
        return this.m / this.observations;
    }

    /**
     * Returns the sample variance of the observations that have been added to this instance.
     *
     * @return the sample variance of the observations that have been added to this instance
     */
    public double sampleVariance() {
        return this.m / (this.observations - 1);
    }

    /**
     * Returns the population standard error.
     * <p>
     * The population standard error is the square root of the population variance over the number of observations.
     *
     * @return the population standard error
     */
    public double populationStandardError() {
        return Math.sqrt(this.populationVariance() / this.observations());
    }

    /**
     * Returns the sample standard error.
     * <p>
     * The sample standard error is the square root of the sample variance over the number of observations.
     *
     * @return the population standard error
     */
    public double sampleStandardError() {
        return Math.sqrt(this.sampleVariance() / this.observations());
    }

    /**
     * Returns the mean value of the observations that have been added to this instance.
     *
     * @return the mean value of the observations that have been added to this instance
     */
    public double mean() {
        return this.sum / this.observations;
    }
}
