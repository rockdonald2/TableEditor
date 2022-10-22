package edu.gof.visitor.command;

import edu.gof.visitor.controller.MainController;

public abstract class Command<ExecReturn, UnexecReturn> {

    protected final MainController mainController;

    public Command(MainController mainController) {
        this.mainController = mainController;
    }

    public abstract ExecReturn execute();

    public abstract UnexecReturn unexecute();

}
