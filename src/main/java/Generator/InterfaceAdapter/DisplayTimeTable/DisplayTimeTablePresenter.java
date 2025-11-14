package Generator.InterfaceAdapter.DisplayTimeTable;

import Generator.UseCases.DisplayTimeTable.DisplayTimeTableOutputBoundary;

public class DisplayTimeTablePresenter implements DisplayTimeTableOutputBoundary {
    private final DisplayTimeTableViewModel displayTimeTableViewModel;

    public DisplayTimeTablePresenter(DisplayTimeTableViewModel displayTimeTableViewModel) {
        this.displayTimeTableViewModel = displayTimeTableViewModel;
    }


    @Override
    public void prepareErrorView(String errorMessage) {

    }

    @Override
    public void prepareSuccessView() {

    }


}
