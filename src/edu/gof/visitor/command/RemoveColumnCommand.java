package edu.gof.visitor.command;

import edu.gof.visitor.controller.MainController;
import edu.gof.visitor.model.Position;

import java.util.Collections;
import java.util.List;

public class RemoveColumnCommand extends Command<Void, Void> {

    private final Position editPosition;

    private List<String> lostData = Collections.emptyList();
    private String lostCol;

    public RemoveColumnCommand(MainController mainController, Position position) {
        super(mainController);
        this.editPosition = position;
    }

    @Override
    public Void execute() {
        lostCol = mainController.getColumnNameAt(editPosition.getColumn());
        lostData = mainController.doDeleteColumnAt(editPosition.getColumn());

        return null;
    }

    @Override
    public Void unexecute() {
        mainController.doAddColumnAt(editPosition.getColumn(), lostData, lostCol);

        return null;
    }

}
