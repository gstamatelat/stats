package gr.james.stats.utils;

import gr.james.stats.binning.DataBin;
import gr.james.stats.binning.LinearDataBinning;
import gr.james.stats.binning.LogarithmicDataBinning;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * Represents a probability distribution based on a frequency map of double values.
 */
public class Distribution {
    private final TreeMap<Double, Long> dist = new TreeMap<>();

    /**
     * Construct a new empty {@link Distribution}.
     */
    public Distribution() {
    }

    /**
     * Add a single observation.
     * <p>
     * If the value exists, its frequency will be increased by 1.
     *
     * @param t the observation value
     */
    public void add(double t) {
        if (!Double.isFinite(t)) {
            throw new IllegalArgumentException("argument must be finite");
        }
        dist.merge(t, 1L, Long::sum);
    }

    /**
     * Show the log-log plot of the distribution.
     * <p>
     * The {@code scaling} parameter determines the sum of the probabilities. Typically a value of {@code 1.0} is used.
     *
     * @param title  the title of the plot
     * @param xLabel the label of the x axis
     * @param yLabel the label of the y axis
     * @param scale  the scaling parameter
     */
    public void showLogLog(String title, String xLabel, String yLabel, double scale) {
        final double sum = frequencySum();
        final List<Double> xData = new ArrayList<>(dist.keySet());
        final List<Double> yData = dist.values().stream().map(y -> scale * y / sum).collect(Collectors.toList());
        final XYChart chart = new XYChartBuilder().title(title).xAxisTitle(xLabel).yAxisTitle(yLabel).build();
        chart.addSeries(title, xData, yData).setXYSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Scatter);
        chart.getStyler().setLegendVisible(false);
        chart.getStyler().setXAxisLogarithmic(true).setYAxisLogarithmic(true);
        new SwingWrapper<>(chart).displayChart();
    }

    /**
     * Show the log-log plot of the distribution.
     * <p>
     * This method is equivalent to
     * <pre><code>
     * showLogLog(title, xLabel, yLabel, 1.0);
     * </code></pre>
     *
     * @param title  the title of the plot
     * @param xLabel the label of the x axis
     * @param yLabel the label of the y axis
     */
    public void showLogLog(String title, String xLabel, String yLabel) {
        final double sum = frequencySum();
        final List<Double> xData = new ArrayList<>(dist.keySet());
        final List<Double> yData = dist.values().stream().map(y -> y / sum).collect(Collectors.toList());
        final XYChart chart = new XYChartBuilder().title(title).xAxisTitle(xLabel).yAxisTitle(yLabel).build();
        chart.addSeries(title, xData, yData).setXYSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Scatter);
        chart.getStyler().setLegendVisible(false);
        chart.getStyler().setXAxisLogarithmic(true).setYAxisLogarithmic(true);
        new SwingWrapper<>(chart).displayChart();
    }

    /**
     * Show the linear plot of the distribution.
     * <p>
     * The {@code scaling} parameter determines the sum of the probabilities. Typically a value of {@code 1.0} is used.
     *
     * @param title  the title of the plot
     * @param xLabel the label of the x axis
     * @param yLabel the label of the y axis
     * @param scale  the scaling parameter
     */
    public void showLinear(String title, String xLabel, String yLabel, double scale) {
        final double sum = frequencySum();
        final List<Double> xData = new ArrayList<>(dist.keySet());
        final List<Double> yData = dist.values().stream().map(y -> scale * y / sum).collect(Collectors.toList());
        final XYChart chart = new XYChartBuilder().title(title).xAxisTitle(xLabel).yAxisTitle(yLabel).build();
        chart.addSeries(title, xData, yData).setXYSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Scatter);
        chart.getStyler().setLegendVisible(false);
        new SwingWrapper<>(chart).displayChart();
    }

    /**
     * Show the linear plot of the distribution.
     * <p>
     * This method is equivalent to
     * <pre><code>
     * showLinear(title, xLabel, yLabel, 1.0);
     * </code></pre>
     *
     * @param title  the title of the plot
     * @param xLabel the label of the x axis
     * @param yLabel the label of the y axis
     */
    public void showLinear(String title, String xLabel, String yLabel) {
        final double sum = frequencySum();
        final List<Double> xData = new ArrayList<>(dist.keySet());
        final List<Double> yData = dist.values().stream().map(y -> y / sum).collect(Collectors.toList());
        final XYChart chart = new XYChartBuilder().title(title).xAxisTitle(xLabel).yAxisTitle(yLabel).build();
        chart.addSeries(title, xData, yData).setXYSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Scatter);
        chart.getStyler().setLegendVisible(false);
        new SwingWrapper<>(chart).displayChart();
    }

    /**
     * Returns the sum of all frequencies in the distribution
     *
     * @return the sum of all frequencies in the distribution
     */
    public long frequencySum() {
        long sum = 0;
        for (long v : dist.values()) {
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
        long maxFrequency = 0;
        double maxValue = dist.firstKey();
        for (Map.Entry<Double, Long> e : dist.entrySet()) {
            if (e.getValue() > maxFrequency) {
                maxValue = e.getKey();
                maxFrequency = e.getValue();
            }
        }
        return maxValue;
    }

    /**
     * Output the distribution in stdout as comma separated x,y values.
     * <p>
     * The {@code scaling} parameter determines the sum of the probabilities. Typically a value of {@code 1.0} is used.
     *
     * @param scaling the scaling parameter
     */
    public void print(double scaling) {
        final double sum = (double) frequencySum();
        for (Map.Entry<Double, Long> e : dist.entrySet()) {
            System.out.printf("%f,%f%n", e.getKey(), scaling * e.getValue() / sum);
        }
    }

    /**
     * Output the distribution in stdout as comma separated x,y values.
     * <p>
     * This method is equivalent to
     * <pre><code>
     * print(1.0);
     * </code></pre>
     */
    public void print() {
        print(1.0);
    }

    /**
     * Returns a new {@link Distribution} by binning this distribution.
     *
     * @param bins  how many bins to create
     * @param space the type of binning
     * @return a new {@link Distribution} by binning this distribution
     */
    public Distribution bin(int bins, Space space) {
        final List<DataBin<Double, Long>> bb;
        if (space == Space.Linear) {
            bb = new LinearDataBinning(bins).bin(dist);
        } else if (space == Space.Log2) {
            bb = new LogarithmicDataBinning(bins, 2).bin(dist);
        } else if (space == Space.Log10) {
            bb = new LogarithmicDataBinning(bins, 10).bin(dist);
        } else if (space == Space.Ln) {
            bb = new LogarithmicDataBinning(bins, Math.exp(1.0)).bin(dist);
        } else {
            throw new AssertionError();
        }

        final Distribution dd = new Distribution();
        for (DataBin<Double, Long> b : bb) {
            dd.dist.put((b.left + b.right) / 2, b.value);
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
        for (Map.Entry<Double, Long> e : dist.entrySet()) {
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
        for (Map.Entry<Double, Long> e : dist.entrySet()) {
            if (e.getKey() < value) {
                dd.dist.put(e.getKey(), e.getValue());
            }
        }
        return dd;
    }

    /**
     * Represents the binning space.
     */
    public enum Space {
        /**
         * Linear space.
         */
        Linear,

        /**
         * Log base 2 space.
         */
        Log2,

        /**
         * Log base 10 space.
         */
        Log10,

        /**
         * Natural log space.
         */
        Ln
    }
}
