package gr.james.stats.utils;

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
     */
    public void print() {
        final double sum = (double) frequencySum();
        for (Map.Entry<Double, Long> e : dist.entrySet()) {
            System.out.printf("%f,%f%n", e.getKey(), e.getValue() / sum);
        }
    }

    /**
     * Returns a new {@link Distribution} by binning this distribution.
     *
     * @param bins  how many bins to create
     * @param space the type of binning
     * @return a new {@link Distribution} by binning this distribution
     */
    public Distribution bin(int bins, Space space) {
        final double minKey = dist.firstKey();
        final double maxKey = dist.lastKey();

        final long[] groups = new long[bins];
        final double[] limits = new double[bins + 1];

        if (space == Space.Linear) {
            double step = (maxKey - minKey) / bins;
            for (int i = 0; i <= bins; i++) {
                limits[i] = minKey + i * step;
            }
        } else if (space == Space.Log2) {
            if (minKey <= 0) {
                throw new RuntimeException("distribution contains non positive values");
            }
            double step = (Math.log(maxKey) / Math.log(2) - Math.log(minKey) / Math.log(2)) / bins;
            for (int i = 0; i <= bins; i++) {
                limits[i] = minKey * Math.pow(2, i * step);
            }
        } else if (space == Space.Log10) {
            if (minKey <= 0) {
                throw new RuntimeException("distribution contains non positive values");
            }
            double step = (Math.log10(maxKey) - Math.log10(minKey)) / bins;
            for (int i = 0; i <= bins; i++) {
                limits[i] = minKey * Math.pow(10, i * step);
            }
        } else if (space == Space.Ln) {
            if (minKey <= 0) {
                throw new RuntimeException("distribution contains non positive values");
            }
            double step = (Math.log(maxKey) - Math.log(minKey)) / bins;
            for (int i = 0; i <= bins; i++) {
                limits[i] = minKey * Math.exp(i * step);
            }
        } else {
            throw new AssertionError();
        }

        int currentBin = 0;
        for (Map.Entry<Double, Long> e : dist.entrySet()) {
            while (e.getKey() > limits[currentBin] && currentBin < bins - 1) {
                currentBin++;
            }
            groups[currentBin] += e.getValue();
        }

        final Distribution dd = new Distribution();
        for (int i = 0; i < groups.length; i++) {
            if (groups[i] > 0) {
                dd.dist.put((limits[i] + limits[i + 1]) / 2, groups[i]);
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
