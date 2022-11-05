package edu.gof.visitor.view.diagrams;

import edu.gof.visitor.model.Data;
import edu.gof.visitor.service.exception.ServiceException;
import org.jfree.chart.JFreeChart;

public interface DiagramStrategy {

    JFreeChart createChart(Data data, int positionIdx) throws ServiceException;

}
