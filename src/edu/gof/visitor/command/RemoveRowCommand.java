package edu.gof.visitor.command;

import edu.gof.visitor.controller.MainController;
import edu.gof.visitor.model.Position;

import java.util.Collections;
import java.util.List;

public class RemoveRowCommand extends PositionBasedCommand<Void, Void> {

    private List<String> lostData = Collections.emptyList();

    public RemoveRowCommand(MainController mainController, Position position) {
        super(mainController, position);
    }

    @Override
    public Void execute() {
        lostData = mainController.doDeleteRowAt(editPosition.getRow());

        return null;
    }

    @Override
    public Void unexecute() {
        mainController.doAddRowAt(editPosition.getRow(), lostData);

        return null;
    }

}
