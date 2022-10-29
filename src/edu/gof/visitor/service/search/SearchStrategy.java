package edu.gof.visitor.service.search;

import edu.gof.visitor.model.Data;
import edu.gof.visitor.model.Position;

public interface SearchStrategy {

    Position search(Data data, String searchWord);

}
