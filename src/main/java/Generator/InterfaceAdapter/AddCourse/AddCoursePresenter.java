package Generator.InterfaceAdapter.AddCourse;

import CourseInfo.LectureSection;
import CourseInfo.PracticalSection;
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


        //Modify the DisplayTimeTableState
        ArrayList<String>  courseCodes = new ArrayList<>(displayTimeTableState.getCourses());
        courseCodes.add(newCourseCode);
        displayTimeTableState.setCourses(courseCodes);

        final String newCourseName = addcourseOutputData.getCourseName();
        final ArrayList<String> newCourseNames = new ArrayList<>(displayTimeTableState.getCourseNames());
        newCourseNames.add(newCourseName);
        displayTimeTableState.setCourseNames(newCourseNames);

        final ArrayList<LectureSection>     newLectureSection = addcourseOutputData.getLectureSection();
        final ArrayList<ArrayList<LectureSection>> lectureSections = new ArrayList<>(displayTimeTableState.getLectureSections());
        lectureSections.add(newLectureSection);
        displayTimeTableState.setLectureSections(lectureSections);

        final ArrayList<TutorialSection> newTutorialSection = addcourseOutputData.getTutorialSection();
        final ArrayList<ArrayList<TutorialSection>> tutorialSections = new ArrayList<>(displayTimeTableState.getTutorialSections());
        tutorialSections.add(newTutorialSection);
        displayTimeTableState.setTutorialSections(tutorialSections);

        final ArrayList<PracticalSection> newPracticalSections = addcourseOutputData.getPracticalSection();
        final ArrayList<ArrayList<PracticalSection>> practicalSections = new ArrayList<>(displayTimeTableState.getPracticalSections());
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

    @Override
    public void prepareErrorView(String errorMessage) {
        final AddCourseState addCourseState = addCourseViewModel.getState();
        addCourseState.setCourseNotFoundError(errorMessage);
        addCourseViewModel.firePropertyChange();

    }
}
