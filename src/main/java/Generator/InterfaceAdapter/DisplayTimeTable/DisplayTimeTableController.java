package Generator.InterfaceAdapter.DisplayTimeTable;

import Generator.UseCases.DisplayTimeTable.DisplayTimeTableInteractor;

public class DisplayTimeTableController {
    private final DisplayTimeTableInteractor displayTimeTableInteractor;
    private final DisplayTimeTableViewModel displayTimeTableViewModel;

    public DisplayTimeTableController(DisplayTimeTableInteractor displayTimeTableInteractor,DisplayTimeTableViewModel displayTimeTableViewModel) {
        this.displayTimeTableInteractor = displayTimeTableInteractor;
        this.displayTimeTableViewModel = displayTimeTableViewModel;
    }

    public void execute() {
        DisplayTimeTableState displayTimeTableState = displayTimeTableViewModel.getState();


    }
}
