package Generator.InterfaceAdapter.AddCourse;

import CourseInfo.LectureSection;
import CourseInfo.PracticalSection;
import CourseInfo.TutorialSection;
import Generator.InterfaceAdapter.DisplayTimeTableState;
import Generator.InterfaceAdapter.DisplayTimeTableViewModel;
import Generator.UseCases.AddCourseOutputBoundary;
import Generator.UseCases.AddCourseOutputData;
import Generator.View.DisplayTimetableView;
import Generator.View.ViewManagerModel;

import javax.print.attribute.PrintJobAttribute;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class AddCoursePresenter implements AddCourseOutputBoundary {
    private final AddCourseViewModel addCourseViewModel;
    private final ViewManagerModel viewManagerModel;
    private DisplayTimeTableViewModel displayTimetableViewModel;

    public AddCoursePresenter(AddCourseViewModel addCourseViewModel , ViewManagerModel viewManagerModel, DisplayTimeTableViewModel displayTimetableViewModel) {
        this.addCourseViewModel = addCourseViewModel;
        this.viewManagerModel = viewManagerModel;
        this.displayTimetableViewModel = displayTimetableViewModel;
    }

    @Override
    public void prepareSuccessView(AddCourseOutputData addcourseOutputData) {
        // Modify the addCourseState
        final AddCourseState addCourseState = addCourseViewModel.getState();
        final DisplayTimeTableState displayTimeTableState = displayTimetableViewModel.getState();
        String newCourseCode = addcourseOutputData.getCourseCode();
        ArrayList<String> newList = new ArrayList<>(addCourseState.getCurCourses());
        newList.add(newCourseCode);
        addCourseState.setCurCourses(newList);
        addCourseState.setCoursesCode("");
        addCourseViewModel.firePropertyChange();

        //Modify the DisplayTimeTableState
        final String newCourseName = addcourseOutputData.getCourseName();
        final ArrayList<LectureSection> newLectureSections = addcourseOutputData.getLectureSection();
        final ArrayList<TutorialSection> newTutorialSections = addcourseOutputData.getTutorialSection();
        final ArrayList<PracticalSection> newPracticalSections = addcourseOutputData.getPracticalSection();

    }

    @Override
    public void prepareErrorView(String errorMessage) {
        final AddCourseState addCourseState = addCourseViewModel.getState();
        addCourseState.setCourseNotFoundError(errorMessage);
        addCourseViewModel.firePropertyChange();

    }
}
