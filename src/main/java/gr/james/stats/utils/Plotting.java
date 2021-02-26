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
import java.util.SortedMap;
import java.util.stream.Collectors;

/**
 * Plotting functions using {@link org.knowm.xchart}.
 */
public final class Plotting {
    /**
     * Display a distribution in linear scale.
     *
     * @param d      the distribution
     * @param title  the window title
     * @param xLabel the label of the x axis
     * @param yLabel the label of the y axis
     */
    public static void linear(Distribution d, String title, String xLabel, String yLabel) {
        final SortedMap<Double, Double> m = d.map();
        final List<Double> xData = new ArrayList<>(m.keySet());
        final List<Double> yData = new ArrayList<>(m.values());
        final XYChart chart = new XYChartBuilder().title(title).xAxisTitle(xLabel).yAxisTitle(yLabel).build();
        chart.addSeries(title, xData, yData).setXYSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Scatter);
        chart.getStyler().setLegendVisible(false);
        new SwingWrapper<>(chart).displayChart();
    }

    /**
     * Display a distribution in linear scale with a certain amount of bins.
     *
     * @param d      the distribution
     * @param bins   the bin count
     * @param title  the window title
     * @param xLabel the label of the x axis
     * @param yLabel the label of the y axis
     */
    public static void linearBins(Distribution d, int bins, String title, String xLabel, String yLabel) {
        final SortedMap<Double, Double> m = d.map();
        final List<DataBin<Double, Double>> bb = new LinearDataBinning(bins).bin(m)
                .stream().filter(b -> b.value > 0).collect(Collectors.toList());
        final List<Double> xData = bb.stream().map(DataBin::center).collect(Collectors.toList());
        final List<Double> yData = bb.stream().map(b -> b.value).collect(Collectors.toList());
        final XYChart chart = new XYChartBuilder().title(title).xAxisTitle(xLabel).yAxisTitle(yLabel).build();
        chart.addSeries(title, xData, yData).setXYSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Scatter);
        chart.getStyler().setLegendVisible(false);
        new SwingWrapper<>(chart).displayChart();
    }

    /**
     * Display a distribution in log-log scale with a certain amount of bins.
     *
     * @param d      the distribution
     * @param title  the window title
     * @param xLabel the label of the x axis
     * @param yLabel the label of the y axis
     */
    public static void logLog(Distribution d, String title, String xLabel, String yLabel) {
        final SortedMap<Double, Double> m = d.map();
        final List<Double> xData = new ArrayList<>(m.keySet());
        final List<Double> yData = new ArrayList<>(m.values());
        final XYChart chart = new XYChartBuilder().title(title).xAxisTitle(xLabel).yAxisTitle(yLabel).build();
        chart.addSeries(title, xData, yData).setXYSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Scatter);
        chart.getStyler().setLegendVisible(false);
        chart.getStyler().setXAxisLogarithmic(true).setYAxisLogarithmic(true);
        new SwingWrapper<>(chart).displayChart();
    }

    /**
     * Display a distribution in log-log scale with a certain amount of bins.
     *
     * @param d      the distribution
     * @param bins   the bin count
     * @param title  the window title
     * @param xLabel the label of the x axis
     * @param yLabel the label of the y axis
     */
    public static void logLogBins(Distribution d, int bins, String title, String xLabel, String yLabel) {
        final SortedMap<Double, Double> m = d.map();
        final List<DataBin<Double, Double>> bb = new LogarithmicDataBinning(bins, 10).bin(m)
                .stream().filter(b -> b.value > 0).collect(Collectors.toList());
        final List<Double> xData = bb.stream().map(DataBin::center).collect(Collectors.toList());
        final List<Double> yData = bb.stream().map(b -> b.value).collect(Collectors.toList());
        final XYChart chart = new XYChartBuilder().title(title).xAxisTitle(xLabel).yAxisTitle(yLabel).build();
        chart.addSeries(title, xData, yData).setXYSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Scatter);
        chart.getStyler().setLegendVisible(false);
        chart.getStyler().setXAxisLogarithmic(true).setYAxisLogarithmic(true);
        new SwingWrapper<>(chart).displayChart();
    }
}
