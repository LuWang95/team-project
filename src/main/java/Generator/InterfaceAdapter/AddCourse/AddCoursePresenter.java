package Generator.InterfaceAdapter.AddCourse;

import CourseInfo.LectureSection;
import CourseInfo.PracticalSection;
import CourseInfo.Section;
import CourseInfo.TutorialSection;
import Generator.InterfaceAdapter.DisplayTimeTable.DisplayTimeTableState;
import Generator.InterfaceAdapter.DisplayTimeTable.DisplayTimeTableViewModel;
import Generator.UseCases.AddCourse.AddCourseOutputBoundary;
import Generator.UseCases.AddCourse.AddCourseOutputData;
import Generator.View.ViewManagerModel;

import java.util.ArrayList;

public class AddCoursePresenter implements AddCourseOutputBoundary {
    private final AddCourseViewModel addCourseViewModel;
    private final DisplayTimeTableViewModel displayTimeTableViewModel;

    public AddCoursePresenter(AddCourseViewModel addCourseViewModel , DisplayTimeTableViewModel displayTimetableViewModel) {
        this.addCourseViewModel = addCourseViewModel;
        this.displayTimeTableViewModel = displayTimetableViewModel;
    }

    //Update(add the course that the user inputs) the curCourses in the addCourseState. Clear the courseCode in the addCourseState and set the Error to null.
    @Override
    public void prepareSuccessView(AddCourseOutputData addcourseOutputData) {
        // Modify the addCourseState
        final AddCourseState addCourseState = addCourseViewModel.getState();
        final DisplayTimeTableState displayTimeTableState = displayTimeTableViewModel.getState();
        String newCourseCode = addcourseOutputData.getCourseCode();
        ArrayList<String> newList = new ArrayList<>(addCourseState.getCurCourses());
        newList.add(newCourseCode);
        addCourseState.setCurCourses(newList);
        addCourseState.setCoursesCode("");
        addCourseState.setCourseNotFoundError(null);


        //Modify the DisplayTimeTableState(To implement the basic view function, you can ignore this)
        ArrayList<String>  courseCodes = new ArrayList<>(displayTimeTableState.getCourses());
        courseCodes.add(newCourseCode);
        displayTimeTableState.setCourses(courseCodes);

        final String newCourseName = addcourseOutputData.getCourseName();
        final ArrayList<String> newCourseNames = new ArrayList<>(displayTimeTableState.getCourseNames());
        newCourseNames.add(newCourseName);
        displayTimeTableState.setCourseNames(newCourseNames);

        final ArrayList<Section>     newLectureSection = addcourseOutputData.getLectureSection();
        final ArrayList<ArrayList<Section>> lectureSections = new ArrayList<>(displayTimeTableState.getLectureSections());
        lectureSections.add(newLectureSection);
        displayTimeTableState.setLectureSections(lectureSections);

        final ArrayList<Section> newTutorialSection = addcourseOutputData.getTutorialSection();
        final ArrayList<ArrayList<Section>> tutorialSections = new ArrayList<>(displayTimeTableState.getTutorialSections());
        tutorialSections.add(newTutorialSection);
        displayTimeTableState.setTutorialSections(tutorialSections);

        final ArrayList<Section> newPracticalSections = addcourseOutputData.getPracticalSection();
        final ArrayList<ArrayList<Section>> practicalSections = new ArrayList<>(displayTimeTableState.getPracticalSections());
        practicalSections.add(newPracticalSections);
        displayTimeTableState.setPracticalSections(practicalSections);

        final Double newCredit = addcourseOutputData.getCredit();
        final ArrayList<Double> credits = new ArrayList<>(displayTimeTableState.getCredit());
        credits.add(newCredit);
        displayTimeTableState.setCredit(credits);

        final String sessionCode = addcourseOutputData.getSessionCode();
        final ArrayList<String> sessionCodes = new ArrayList<>(displayTimeTableState.getSessionCode());
        sessionCodes.add(sessionCode);
        displayTimeTableState.setSessionCode(sessionCodes);

        addCourseViewModel.firePropertyChange();
        displayTimeTableViewModel.firePropertyChange();

    }

    // If the addCourseCode textField is empty then send an error message.
    @Override
    public void prepareErrorView(String errorMessage) {
        final AddCourseState addCourseState = addCourseViewModel.getState();
        addCourseState.setCourseNotFoundError(errorMessage);
        addCourseViewModel.firePropertyChange();

    }
}
