package Generator.InterfaceAdapter.DisplayTimeTable;

import Generator.UseCases.DisplayTimeTable.DisplayTimeTableInputData;

public class DisplayTimeTableController {
    private final DisplayTimeTableViewModel displayTimeTableViewModel;
    private final DisplayTimeTableViewModel displayTimeTableInteractor;

    public DisplayTimeTableController(DisplayTimeTableViewModel displayTimeTableViewModel,DisplayTimeTableViewModel displayTimeTableInteractor) {
        this.displayTimeTableViewModel = displayTimeTableViewModel;
        this.displayTimeTableInteractor = displayTimeTableInteractor;
    }

    public void execute() {
        DisplayTimeTableState displayTimeTableState = new DisplayTimeTableState();
        DisplayTimeTableInputData inputData = new DisplayTimeTableInputData(displayTimeTableState.getCourses(),displayTimeTableState.getCourseNames(),displayTimeTableState.getLectureSections(),displayTimeTableState.getTutorialSections(),displayTimeTableState.getPracticalSections(),displayTimeTableState.getCredit());

    }



}
