package edu.gof.visitor.service.search;

import edu.gof.visitor.model.Data;
import edu.gof.visitor.model.Position;

import java.util.List;

public class MatchCaseSearchStrategy implements SearchStrategy {

    @Override
    public Position search(Data data, String searchWord) {
        Position position = null;

        final List<List<String>> tmpData = data.getData();
        for (int i = 0; i < tmpData.size(); ++i) {
            for (int j = 0; j < data.getHeaders().size(); ++j) {
                if (tmpData.get(i).get(j).contains(searchWord)) {
                    position = new Position(i, j);
                    break;
                }
            }

            if (position != null) {
                break;
            }
        }

        return position;
    }

}
