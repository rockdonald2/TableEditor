package edu.gof.visitor.service.loader;

import edu.gof.visitor.model.Data;
import edu.gof.visitor.service.exception.ServiceException;

public interface Importer {

    Data importData(String fileName) throws ServiceException;

}
