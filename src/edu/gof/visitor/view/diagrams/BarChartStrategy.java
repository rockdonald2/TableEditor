package edu.gof.visitor.view.diagrams;

import edu.gof.visitor.model.Data;
import edu.gof.visitor.service.exception.ServiceException;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class BarChartStrategy implements DiagramStrategy {

    @Override
    public JFreeChart createChart(Data data, int positionIdx) throws ServiceException {
        try {
            final String header = data.getHeaders().get(positionIdx);
            final List<Double> rowData = new ArrayList<>();
            data.getData().forEach(row -> rowData.add(Double.valueOf(row.get(positionIdx))));

            DefaultCategoryDataset dataset = new DefaultCategoryDataset();
            AtomicInteger idx = new AtomicInteger(1);
            rowData.forEach(d -> dataset.addValue(d, header, String.format("%s #%s", header, idx.getAndIncrement())));

            return ChartFactory.createBarChart("Bar Chart", "Column", "Value", dataset, PlotOrientation.VERTICAL, true, true, false);
        } catch (Exception e) {
            throw new ServiceException("Failed to create bar chart", e);
        }
    }

}
