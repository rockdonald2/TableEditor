package edu.gof.visitor.service.export;

import edu.gof.visitor.service.loader.Data;

public interface Exporter {

    String exportData(Data data);

}
