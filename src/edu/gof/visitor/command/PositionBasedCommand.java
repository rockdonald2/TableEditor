package edu.gof.visitor.command;

import edu.gof.visitor.controller.MainController;
import edu.gof.visitor.model.Position;

public abstract class PositionBasedCommand<ExecReturn, UnexecReturn> extends Command<ExecReturn, UnexecReturn> {

    protected final Position editPosition;

    public Position getEditPosition() {
        return editPosition;
    }

    protected PositionBasedCommand(MainController mainController, Position editPosition) {
        super(mainController);
        this.editPosition = editPosition;
    }

    public enum Orientation {
        DECREASE,
        INCREASE
    }

    public enum Which {
        COLUMN,
        ROW
    }

}
