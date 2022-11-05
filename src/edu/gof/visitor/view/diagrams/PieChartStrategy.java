package edu.gof.visitor.view.diagrams;

import edu.gof.visitor.model.Data;
import edu.gof.visitor.service.exception.ServiceException;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PieChartStrategy implements DiagramStrategy {

    @Override
    public JFreeChart createChart(Data data, int positionIdx) throws ServiceException {
        try {
            List<String> rowData = data.getData().get(positionIdx);
            List<String> headers = data.getHeaders();

            Map<String, Double> valsMap = IntStream.range(0, headers.size()).boxed().collect(Collectors.toMap(headers::get, rowIdx -> Double.parseDouble(rowData.get(rowIdx))));

            DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
            valsMap.keySet().forEach(key -> dataset.setValue(key, valsMap.get(key)));

            return ChartFactory.createPieChart("Pie Chart", dataset, true, true, false);
        } catch (Exception e) {
            throw new ServiceException("Failed to create pie chart", e);
        }
    }

}
