package testtask;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;

import javax.swing.JFrame;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.DefaultHighLowDataset;

public class Chart {
	CategoryPlot plot;
	String title;
	JFreeChart lineChart;
	private DefaultCategoryDataset chartDatasets = new DefaultCategoryDataset();
	
	public void fillDataset(double value, String columnY, String columnX){
		chartDatasets.addValue(value, columnY, columnX);
	}
	public void configurePlot(String title, String xLabel, String yLabel, LinkedHashSet <String> xKeys, String [] yKey){
		this.title = title;
		lineChart=ChartFactory.createLineChart(title, xLabel, yLabel, chartDatasets, PlotOrientation.VERTICAL,true,true,false);
		plot = lineChart.getCategoryPlot();
		plot.getRangeAxis().setLowerBound(minValue(xKeys, yKey));
		plot.configureRangeAxes();
	}
	public void showChart(){
        JFrame frame = new JFrame(title);
 	    frame.getContentPane().add(new ChartPanel(lineChart));
 	    frame.setSize(800,600);
 	    frame.setLocationRelativeTo(null);
 	    frame.setVisible(true);
	}
	public double minValue(LinkedHashSet <String> xKeys, String [] yKey){
		ArrayList <Double> values = new ArrayList<Double>();
		for(int i =0;i<yKey.length;i++){
			Iterator <String> xKey= xKeys.iterator();
			while(xKey.hasNext()){
				values.add(chartDatasets.getValue(yKey[i], xKey.next()).doubleValue());
			}
		}
		Collections.sort(values);
		return (values.get(0)-2.0);
	}
}
