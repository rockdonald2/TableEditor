package edu.gof.visitor.command;

import edu.gof.visitor.controller.MainController;
import edu.gof.visitor.model.Position;

import java.util.Collections;
import java.util.List;

public class AddColumnCommand extends Command<Void, Void> {

    private final Position editPosition;
    private final String newColumnName;

    private List<String> lostData = Collections.emptyList();

    public AddColumnCommand(MainController mainController, Position position, String columnName) {
        super(mainController);
        this.editPosition = position;
        this.newColumnName = columnName;
    }

    @Override
    public Void execute() {
        mainController.doAddNewColumn(newColumnName);
        mainController.doDisplayData();

        return null;
    }

    @Override
    public Void unexecute() {
        lostData = mainController.doDeleteColumnAt(editPosition.getColumn());

        return null;
    }

}
