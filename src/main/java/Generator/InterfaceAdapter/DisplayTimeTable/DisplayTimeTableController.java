package Generator.InterfaceAdapter.DisplayTimeTable;


import Generator.UseCases.DisplayTimeTable.DisplayTimeTableInputBoundary;

public class DisplayTimeTableController {
    private final DisplayTimeTableInputBoundary displayTimeTableInteractor;

    public DisplayTimeTableController(DisplayTimeTableInputBoundary displayTimeTableInteractor) {
        this.displayTimeTableInteractor = displayTimeTableInteractor;
    }

    public void execute() {
        displayTimeTableInteractor.execute();
    }
}
