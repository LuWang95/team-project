package generator.use_case;



import course_info.Course;
import course_info.LectureSection;
import course_info.Meeting;
import course_info.Section;
import generator.use_case.generate_timetable.*;
import generator.use_case.sort_timetable.SortTimetableInputBoundary;
import generator.use_case.sort_timetable.SortTimetableInputData;
import generator.use_case.sort_timetable.SortTimetableOutputData;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Unit tests for GenerateTimetableInteractor, focusing on sortEnabled/sortDisabled cases.
 */
public class GenerateTimetableInteractorTest {

    // -------------------------
    // Fake DAO
    // -------------------------
    private static class FakeDataAccess implements GenerateTimetableDataAccessInterface {
        private final List<Course> courses;

        FakeDataAccess(List<Course> courses) {
            this.courses = courses;
        }

        @Override
        public ArrayList<Course> getCourses() {
            return (ArrayList<Course>) courses;
        }

        @Override
        public Course getCoursebyCode(String courseCode) {
            return null;
        }
    }

    // -------------------------
    // Sort Spy
    // -------------------------
    private static class SortSpy implements SortTimetableInputBoundary {
        boolean called = false;
        SortTimetableInputData lastInput;

        @Override
        public SortTimetableOutputData sort(SortTimetableInputData inputData) {
            called = true;
            lastInput = inputData;
            return new SortTimetableOutputData(
                    inputData.getFallTimetables(),
                    inputData.getWinterTimetables()
            );
        }
    }

    // -------------------------
    // Presenter Spy
    // -------------------------
    private static class PresenterSpy implements GenerateTimetableOutputBoundary {
        boolean failureCalled = false;
        String failureMessage;
        GenerateTimetableOutputData successData;

        @Override
        public void prepareGenerateTimetableFailureView(String errorMessage) {
            failureCalled = true;
            failureMessage = errorMessage;
        }


        @Override
        public void prepareGenerateTimetableSuccessView(GenerateTimetableOutputData outputData) {
            this.successData = outputData;
        }
    }

    // -------------------------
    // Helper: one simple FALL course
    // -------------------------
    private Course createSimpleFallCourse(
            String courseCode,
            int dayOfWeek,   // 1=Mon, 2=Tue, ...
            int startMin,    // 600 = 10:00
            int endMin       // 660 = 11:00
    ) {
        ArrayList<Meeting> meetings = new ArrayList<>();
        meetings.add(new Meeting(
                "ProfX",
                "dummyStart",
                "dummyEnd",
                startMin,
                endMin,
                "BA",
                dayOfWeek
        ));

        ArrayList<Section> lectureSections = new ArrayList<>();
        lectureSections.add(new LectureSection("LEC0101", meetings));

        return new Course(
                courseCode,
                courseCode + " Title",
                20259,              // FALL
                0.5,
                lectureSections,
                new ArrayList<>(),
                new ArrayList<>()
        );
    }

    // -------------------------
    // Helper: two conflicting FALL courses
    // -------------------------
    private List<Course> createConflictingFallCourses() {
        List<Course> courses = new ArrayList<>();
        courses.add(createSimpleFallCourse("CSC207H1", 1, 600, 660));
        courses.add(createSimpleFallCourse("STA257H1", 1, 600, 660));
        return courses;
    }

    // ============================================================
    // sortEnabled = false tests
    // ============================================================

    @Test
    public void testGenerateTimetable_sortDisabled_twoNonConflictingFallCourses() {
        List<Course> courses = new ArrayList<>();
        courses.add(createSimpleFallCourse("CSC207H1", 1, 600, 660));   // Mon 10-11
        courses.add(createSimpleFallCourse("STA257H1", 2, 840, 900));   // Tue 14-15

        FakeDataAccess dao = new FakeDataAccess(courses);
        PresenterSpy presenter = new PresenterSpy();
        SortSpy sortSpy = new SortSpy();

        GenerateTimetableInteractor interactor =
                new GenerateTimetableInteractor(dao, presenter, sortSpy);
        ArrayList<String> timePreference = new ArrayList<>(List.of(""));

        GenerateTimetableInputData inputData = new GenerateTimetableInputData(false, timePreference);

        interactor.execute(inputData);

        assertFalse("Failure view should NOT be called", presenter.failureCalled);
        assertNotNull("Success data must exist", presenter.successData);

        List<TimetableDTO> fall = presenter.successData.getFallTimeTables();
        List<TimetableDTO> winter = presenter.successData.getWinterTimeTables();

        assertNotNull("Fall timetables cannot be null", fall);
        assertTrue("There should be at least 1 valid fall timetable", fall.size() > 0);

        assertNotNull("Winter timetables cannot be null", winter);
        assertEquals("When there are no winter courses, we get exactly 1 empty timetable",
                1, winter.size());

        assertFalse("Sort interactor should NOT be called when sort is disabled", sortSpy.called);
    }

    @Test
    public void testGenerateTimetable_sortDisabled_noCourses_failure() {
        FakeDataAccess dao = new FakeDataAccess(new ArrayList<>());
        PresenterSpy presenter = new PresenterSpy();
        SortSpy sortSpy = new SortSpy();

        GenerateTimetableInteractor interactor =
                new GenerateTimetableInteractor(dao, presenter, sortSpy);
        ArrayList<String> timePreference = new ArrayList<>(List.of(""));

        GenerateTimetableInputData inputData = new GenerateTimetableInputData(false, timePreference);

        interactor.execute(inputData);

        assertTrue("Failure view SHOULD be called when no courses", presenter.failureCalled);
        assertNull("Success data should be null when failure", presenter.successData);

        assertFalse("Sort should not be called on failure", sortSpy.called);
    }

    @Test
    public void testGenerateTimetable_sortDisabled_conflictingFallCourses() {
        List<Course> courses = createConflictingFallCourses();

        FakeDataAccess dao = new FakeDataAccess(courses);
        PresenterSpy presenter = new PresenterSpy();
        SortSpy sortSpy = new SortSpy();

        GenerateTimetableInteractor interactor =
                new GenerateTimetableInteractor(dao, presenter, sortSpy);
        ArrayList<String> timePreference = new ArrayList<>(List.of(""));

        GenerateTimetableInputData inputData = new GenerateTimetableInputData(false, timePreference);

        interactor.execute(inputData);

        assertFalse("Failure view should NOT be called (still a valid request)", presenter.failureCalled);
        assertNotNull("Success data must exist", presenter.successData);

        List<TimetableDTO> fall = presenter.successData.getFallTimeTables();
        List<TimetableDTO> winter = presenter.successData.getWinterTimeTables();

        assertNotNull(fall);
        assertEquals("All fall combinations should be invalid due to conflicts", 0, fall.size());

        assertNotNull(winter);
        assertEquals("When no winter courses, we get exactly 1 empty timetable",
                1, winter.size());

        assertFalse("Sort should NOT be called when sort is disabled", sortSpy.called);
    }

    // ============================================================
    // sortEnabled = true tests
    // ============================================================

    @Test
    public void testGenerateTimetable_sortEnabled_twoNonConflictingFallCourses_callsSort() {
        List<Course> courses = new ArrayList<>();
        courses.add(createSimpleFallCourse("CSC207H1", 1, 600, 660));
        courses.add(createSimpleFallCourse("STA257H1", 2, 840, 900));

        FakeDataAccess dao = new FakeDataAccess(courses);
        PresenterSpy presenter = new PresenterSpy();
        SortSpy sortSpy = new SortSpy();

        GenerateTimetableInteractor interactor =
                new GenerateTimetableInteractor(dao, presenter, sortSpy);
        ArrayList<String> timePreference = new ArrayList<>(List.of(""));

        GenerateTimetableInputData inputData = new GenerateTimetableInputData(true, timePreference);

        interactor.execute(inputData);

        assertFalse("Failure view should NOT be called", presenter.failureCalled);
        assertNotNull("Success data must exist", presenter.successData);

        List<TimetableDTO> fall = presenter.successData.getFallTimeTables();
        List<TimetableDTO> winter = presenter.successData.getWinterTimeTables();

        assertNotNull(fall);
        assertTrue("There should be at least 1 valid fall timetable", fall.size() > 0);

        assertNotNull(winter);
        assertEquals("When there are no winter courses, we get exactly 1 empty timetable",
                1, winter.size());

        assertTrue("Sort interactor SHOULD be called when sort is enabled", sortSpy.called);
        assertNotNull("SortSpy should capture input data", sortSpy.lastInput);
    }

    @Test
    public void testGenerateTimetable_sortEnabled_noCourses_failure_noSort() {
        FakeDataAccess dao = new FakeDataAccess(new ArrayList<>());
        PresenterSpy presenter = new PresenterSpy();
        SortSpy sortSpy = new SortSpy();

        GenerateTimetableInteractor interactor =
                new GenerateTimetableInteractor(dao, presenter, sortSpy);
        ArrayList<String> timePreference = new ArrayList<>(List.of(""));

        GenerateTimetableInputData inputData = new GenerateTimetableInputData(true, timePreference);

        interactor.execute(inputData);

        assertTrue("Failure view SHOULD be called when no courses", presenter.failureCalled);
        assertNull("Success data should be null when failure", presenter.successData);

        assertFalse("Sort should NOT be called on failure", sortSpy.called);
    }

    @Test
    public void testGenerateTimetable_sortEnabled_conflictingFallCourses_callsSort() {
        List<Course> courses = createConflictingFallCourses();

        FakeDataAccess dao = new FakeDataAccess(courses);
        PresenterSpy presenter = new PresenterSpy();
        SortSpy sortSpy = new SortSpy();

        GenerateTimetableInteractor interactor =
                new GenerateTimetableInteractor(dao, presenter, sortSpy);
        ArrayList<String> timePreference = new ArrayList<>(List.of(""));

        GenerateTimetableInputData inputData = new GenerateTimetableInputData(true, timePreference);

        interactor.execute(inputData);

        assertFalse("Failure view should NOT be called", presenter.failureCalled);
        assertNotNull("Success data must exist", presenter.successData);

        List<TimetableDTO> fall = presenter.successData.getFallTimeTables();
        List<TimetableDTO> winter = presenter.successData.getWinterTimeTables();

        assertNotNull(fall);
        assertEquals("All fall combinations should be invalid due to conflicts", 0, fall.size());

        assertNotNull(winter);
        assertEquals("When no winter courses, we get exactly 1 empty timetable",
                1, winter.size());

        assertTrue("Sort interactor SHOULD be called when sort is enabled", sortSpy.called);
    }
}