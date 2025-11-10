package Generator.InterfaceAdapter;

import Generator.UseCases.AddCourseInputData;
import Generator.UseCases.AddCourseOutputBoundary;
import Generator.UseCases.AddCourseOutputData;
import Generator.View.DisplayTimetableView;
import Generator.View.ViewManagerModel;

public class AddCoursePresenter implements AddCourseOutputBoundary {
    private AddCourseViewModel addCourseViewModel;
    private ViewManagerModel viewManagerModel;
    private DisplayTimetableView displayTimetableView;

    public AddCoursePresenter(AddCourseViewModel addCourseViewModel, ViewManagerModel viewManagerModel,DisplayTimetableView displayTimetableView) {
        this.addCourseViewModel = addCourseViewModel;
        this.viewManagerModel = viewManagerModel;
        this.displayTimetableView = displayTimetableView;
    }


    @Override
    public void prepareSuccessView(AddCourseOutputData addcourseOutputData) {

    }

    @Override
    public void prepareErrorView(String errorMessage) {

    }
}
