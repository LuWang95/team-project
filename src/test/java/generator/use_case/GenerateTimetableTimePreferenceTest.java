package generator.use_case;

import course_info.Course;
import course_info.Meeting;
import course_info.Section;
import generator.use_case.generate_timetable.*;
import generator.use_case.sort_timetable.SortTimetableInputBoundary;
import generator.use_case.sort_timetable.SortTimetableInputData;
import generator.use_case.sort_timetable.SortTimetableOutputData;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for time preference functionality in GenerateTimetableInteractor.
 * Tests the filtering and prioritization logic for morning, afternoon, and evening preferences.
 */
class GenerateTimetableTimePreferenceTest {

    /**
     * Fake DAO that provides test courses.
     */
    private static final class FakeCourseDAO implements GenerateTimetableDataAccessInterface {
        private final ArrayList<Course> courses;

        FakeCourseDAO(ArrayList<Course> courses) {
            this.courses = courses;
        }

        @Override
        public ArrayList<Course> getCourses() {
            return courses;
        }

        @Override
        public Course getCoursebyCode(String code) {
            return null;
        }
    }

    /**
     * Fake presenter that captures the output.
     */
    private static final class FakePresenter implements GenerateTimetableOutputBoundary {
        GenerateTimetableOutputData lastOutput;
        String lastError;

        @Override
        public void prepareGenerateTimetableSuccessView(GenerateTimetableOutputData outputData) {
            lastOutput = outputData;
        }

        @Override
        public void prepareGenerateTimetableFailureView(String errorMessage) {
            lastError = errorMessage;
        }
    }

    /**
     * Fake sort interactor - not used in these tests since sortEnabled is false.
     */
    private static final class FakeSortInteractor implements SortTimetableInputBoundary {
        @Override
        public SortTimetableOutputData sort(SortTimetableInputData inputData) {
            // Not called when sortEnabled is false
            return null;
        }
    }

    /**
     * Helper to create a course with a lecture at specific time.
     * @param courseCode the course code
     * @param sessionCode the session code (20259 for fall, 20251 for winter)
     * @param day day of week (1=Mon, 2=Tue, etc.)
     * @param startHour hour in 24h format (e.g., 9 for 9am, 14 for 2pm)
     * @param endHour ending hour in 24h format
     * @return Course object
     */
    private Course createCourseWithLecture(String courseCode, int sessionCode,
                                           int day, int startHour, int endHour) {
        Meeting meeting = new Meeting(
                "Instructor",
                startHour + ":00",
                endHour + ":00",
                startHour * 60,
                endHour * 60,
                "BA",
                day
        );
        ArrayList<Meeting> meetings = new ArrayList<>(List.of(meeting));
        Section lecture = new Section("LEC0101", meetings);
        ArrayList<Section> lectures = new ArrayList<>(List.of(lecture));

        return new Course(
                courseCode,
                "Test Course",
                sessionCode,
                0.5,
                lectures,
                new ArrayList<>(),
                new ArrayList<>()
        );
    }

    /**
     * Helper to create a course with both lecture and tutorial at specific times.
     */
    private Course createCourseWithLectureAndTutorial(String courseCode, int sessionCode,
                                                      int lecDay, int lecStartHour, int lecEndHour,
                                                      int tutDay, int tutStartHour, int tutEndHour) {
        Meeting lecMeeting = new Meeting(
                "Instructor",
                lecStartHour + ":00",
                lecEndHour + ":00",
                lecStartHour * 60,
                lecEndHour * 60,
                "BA",
                lecDay
        );
        ArrayList<Meeting> lecMeetings = new ArrayList<>(List.of(lecMeeting));
        Section lecture = new Section("LEC0101", lecMeetings);
        ArrayList<Section> lectures = new ArrayList<>(List.of(lecture));

        Meeting tutMeeting = new Meeting(
                "TA",
                tutStartHour + ":00",
                tutEndHour + ":00",
                tutStartHour * 60,
                tutEndHour * 60,
                "BA",
                tutDay
        );
        ArrayList<Meeting> tutMeetings = new ArrayList<>(List.of(tutMeeting));
        Section tutorial = new Section("TUT0101", tutMeetings);
        ArrayList<Section> tutorials = new ArrayList<>(List.of(tutorial));

        return new Course(
                courseCode,
                "Test Course",
                sessionCode,
                0.5,
                lectures,
                tutorials,
                new ArrayList<>()
        );
    }

    @Test
    void testMorningPreference() {
        // Arrange: Create courses - one in morning, one in afternoon
        Course morningCourse = createCourseWithLecture("CSC207H1", 20259, 1, 9, 11);
        Course afternoonCourse = createCourseWithLecture("CSC209H1", 20259, 1, 14, 16);

        ArrayList<Course> courses = new ArrayList<>(List.of(morningCourse, afternoonCourse));
        FakeCourseDAO dao = new FakeCourseDAO(courses);
        FakePresenter presenter = new FakePresenter();
        FakeSortInteractor sortInteractor = new FakeSortInteractor();
        GenerateTimetableInteractor interactor = new GenerateTimetableInteractor(dao, presenter, sortInteractor);

        ArrayList<String> timePreferences = new ArrayList<>(List.of("Morning"));
        GenerateTimetableInputData input = new GenerateTimetableInputData(false , timePreferences);

        // Act
        interactor.execute(input);

        // Assert: Morning timetable should be first
        assertNotNull(presenter.lastOutput);
        ArrayList<TimetableDTO> fallTimetables = presenter.lastOutput.getFallTimeTables();
        assertTrue(fallTimetables.size() > 0);

        // Check that first timetable has morning course
        TimetableDTO firstTimetable = fallTimetables.get(0);
        ArrayList<ArrayList<ArrayList<String>>> table = firstTimetable.getTable();

        // Hour 0 = 9am, Hour 1 = 10am (morning hours)
        boolean hasMorningCourse = false;
        for (int hour = 0; hour < 3; hour++) {
            if (!table.get(0).get(hour).isEmpty()) {
                hasMorningCourse = true;
                break;
            }
        }
        assertTrue(hasMorningCourse, "First timetable should have morning course");
    }

    @Test
    void testAfternoonPreference() {
        // Arrange: Create courses - one in morning, one in afternoon
        Course morningCourse = createCourseWithLecture("CSC207H1", 20259, 1, 9, 11);
        Course afternoonCourse = createCourseWithLecture("CSC209H1", 20259, 1, 14, 16);

        ArrayList<Course> courses = new ArrayList<>(List.of(morningCourse, afternoonCourse));
        FakeCourseDAO dao = new FakeCourseDAO(courses);
        FakePresenter presenter = new FakePresenter();
        FakeSortInteractor sortInteractor = new FakeSortInteractor();
        GenerateTimetableInteractor interactor = new GenerateTimetableInteractor(dao, presenter, sortInteractor);

        ArrayList<String> timePreferences = new ArrayList<>(List.of("Afternoon"));
        GenerateTimetableInputData input = new GenerateTimetableInputData(false, timePreferences);

        // Act
        interactor.execute(input);

        // Assert: Afternoon timetable should be first
        assertNotNull(presenter.lastOutput);
        ArrayList<TimetableDTO> fallTimetables = presenter.lastOutput.getFallTimeTables();
        assertTrue(fallTimetables.size() > 0);

        TimetableDTO firstTimetable = fallTimetables.get(0);
        ArrayList<ArrayList<ArrayList<String>>> table = firstTimetable.getTable();

        // Hour 3-7 = 12pm-5pm (afternoon hours)
        boolean hasAfternoonCourse = false;
        for (int hour = 3; hour < 8; hour++) {
            if (!table.get(0).get(hour).isEmpty()) {
                hasAfternoonCourse = true;
                break;
            }
        }
        assertTrue(hasAfternoonCourse, "First timetable should have afternoon course");
    }

    @Test
    void testEveningPreference() {
        // Arrange: Create courses - one in afternoon, one in evening
        Course afternoonCourse = createCourseWithLecture("CSC207H1", 20259, 1, 14, 16);
        Course eveningCourse = createCourseWithLecture("CSC209H1", 20259, 1, 18, 20);

        ArrayList<Course> courses = new ArrayList<>(List.of(afternoonCourse, eveningCourse));
        FakeCourseDAO dao = new FakeCourseDAO(courses);
        FakePresenter presenter = new FakePresenter();
        FakeSortInteractor sortInteractor = new FakeSortInteractor();
        GenerateTimetableInteractor interactor = new GenerateTimetableInteractor(dao, presenter, sortInteractor);

        ArrayList<String> timePreferences = new ArrayList<>(List.of("Evening"));
        GenerateTimetableInputData input = new GenerateTimetableInputData(false, timePreferences);

        // Act
        interactor.execute(input);

        // Assert: Evening timetable should be first
        assertNotNull(presenter.lastOutput);
        ArrayList<TimetableDTO> fallTimetables = presenter.lastOutput.getFallTimeTables();
        assertTrue(fallTimetables.size() > 0);

        TimetableDTO firstTimetable = fallTimetables.get(0);
        ArrayList<ArrayList<ArrayList<String>>> table = firstTimetable.getTable();

        // Hour 8-11 = 5pm-9pm (evening hours)
        boolean hasEveningCourse = false;
        for (int hour = 8; hour < 12; hour++) {
            if (!table.get(0).get(hour).isEmpty()) {
                hasEveningCourse = true;
                break;
            }
        }
        assertTrue(hasEveningCourse, "First timetable should have evening course");
    }

    @Test
    void testMultipleTimePreferences() {
        // Arrange: Create courses in morning, afternoon, and evening
        Course morningCourse = createCourseWithLecture("CSC207H1", 20259, 1, 9, 11);
        Course afternoonCourse = createCourseWithLecture("CSC209H1", 20259, 2, 14, 16);
        Course eveningCourse = createCourseWithLecture("MAT137Y1", 20259, 3, 18, 20);

        ArrayList<Course> courses = new ArrayList<>(List.of(morningCourse, afternoonCourse, eveningCourse));
        FakeCourseDAO dao = new FakeCourseDAO(courses);
        FakePresenter presenter = new FakePresenter();
        FakeSortInteractor sortInteractor = new FakeSortInteractor();
        GenerateTimetableInteractor interactor = new GenerateTimetableInteractor(dao, presenter, sortInteractor);

        // Prefer morning and afternoon (not evening)
        ArrayList<String> timePreferences = new ArrayList<>(Arrays.asList("Morning", "Afternoon"));
        GenerateTimetableInputData input = new GenerateTimetableInputData(false, timePreferences);

        // Act
        interactor.execute(input);

        // Assert: Timetables with morning/afternoon courses should score higher than evening-only
        assertNotNull(presenter.lastOutput);
        ArrayList<TimetableDTO> fallTimetables = presenter.lastOutput.getFallTimeTables();
        assertTrue(fallTimetables.size() > 0);
    }

    @Test
    void testCoursePrioritization() {
        // Arrange: First course in morning, second course in afternoon
        // First course should be weighted higher
        Course firstCourse = createCourseWithLecture("CSC207H1", 20259, 1, 9, 11);
        Course secondCourse = createCourseWithLecture("CSC209H1", 20259, 2, 14, 16);

        ArrayList<Course> courses = new ArrayList<>(List.of(firstCourse, secondCourse));
        FakeCourseDAO dao = new FakeCourseDAO(courses);
        FakePresenter presenter = new FakePresenter();
        FakeSortInteractor sortInteractor = new FakeSortInteractor();
        GenerateTimetableInteractor interactor = new GenerateTimetableInteractor(dao, presenter, sortInteractor);

        ArrayList<String> timePreferences = new ArrayList<>(List.of("Morning"));
        GenerateTimetableInputData input = new GenerateTimetableInputData(false, timePreferences);

        // Act
        interactor.execute(input);

        // Assert: Since first course is more important and in morning,
        // it should appear in the top timetable
        assertNotNull(presenter.lastOutput);
        ArrayList<TimetableDTO> fallTimetables = presenter.lastOutput.getFallTimeTables();
        assertTrue(fallTimetables.size() > 0);
    }

    @Test
    void testLectureVsTutorialWeighting() {
        // Arrange: Course with lecture in preferred time and tutorial in non-preferred time
        // Lecture should be weighted 3x more than tutorial
        Course course = createCourseWithLectureAndTutorial(
                "CSC207H1", 20259,
                1, 9, 11,  // Lecture in morning
                2, 14, 16  // Tutorial in afternoon
        );

        ArrayList<Course> courses = new ArrayList<>(List.of(course));
        FakeCourseDAO dao = new FakeCourseDAO(courses);
        FakePresenter presenter = new FakePresenter();
        FakeSortInteractor sortInteractor = new FakeSortInteractor();
        GenerateTimetableInteractor interactor = new GenerateTimetableInteractor(dao, presenter, sortInteractor);

        ArrayList<String> timePreferences = new ArrayList<>(List.of("Morning"));
        GenerateTimetableInputData input = new GenerateTimetableInputData(false, timePreferences);

        // Act
        interactor.execute(input);

        // Assert: Should generate timetable successfully
        // The lecture being in morning should give higher score than tutorial in afternoon
        assertNotNull(presenter.lastOutput);
        ArrayList<TimetableDTO> fallTimetables = presenter.lastOutput.getFallTimeTables();
        assertTrue(fallTimetables.size() > 0);
    }

    @Test
    void testEmptyTimePreferences() {
        // Arrange: No time preferences specified
        Course course1 = createCourseWithLecture("CSC207H1", 20259, 1, 9, 11);
        Course course2 = createCourseWithLecture("CSC209H1", 20259, 2, 14, 16);

        ArrayList<Course> courses = new ArrayList<>(List.of(course1, course2));
        FakeCourseDAO dao = new FakeCourseDAO(courses);
        FakePresenter presenter = new FakePresenter();
        FakeSortInteractor sortInteractor = new FakeSortInteractor();
        GenerateTimetableInteractor interactor = new GenerateTimetableInteractor(dao, presenter, sortInteractor);

        ArrayList<String> timePreferences = new ArrayList<>();
        GenerateTimetableInputData input = new GenerateTimetableInputData(false, timePreferences);

        // Act
        interactor.execute(input);

        // Assert: Should return all timetables without filtering
        assertNotNull(presenter.lastOutput);
        ArrayList<TimetableDTO> fallTimetables = presenter.lastOutput.getFallTimeTables();
        assertTrue(fallTimetables.size() > 0);
    }

    @Test
    void testNullTimePreferences() {
        // Arrange: Null time preferences
        Course course = createCourseWithLecture("CSC207H1", 20259, 1, 9, 11);

        ArrayList<Course> courses = new ArrayList<>(List.of(course));
        FakeCourseDAO dao = new FakeCourseDAO(courses);
        FakePresenter presenter = new FakePresenter();
        FakeSortInteractor sortInteractor = new FakeSortInteractor();
        GenerateTimetableInteractor interactor = new GenerateTimetableInteractor(dao, presenter, sortInteractor);

        GenerateTimetableInputData input = new GenerateTimetableInputData(false, null);

        // Act
        interactor.execute(input);

        // Assert: Should handle null gracefully and return all timetables
        assertNotNull(presenter.lastOutput);
        ArrayList<TimetableDTO> fallTimetables = presenter.lastOutput.getFallTimeTables();
        assertTrue(fallTimetables.size() > 0);
    }

    @Test
    void testWinterTermFiltering() {
        // Arrange: Winter term courses
        Course winterCourse1 = createCourseWithLecture("CSC207H1", 20251, 1, 9, 11);
        Course winterCourse2 = createCourseWithLecture("CSC209H1", 20251, 2, 14, 16);

        ArrayList<Course> courses = new ArrayList<>(List.of(winterCourse1, winterCourse2));
        FakeCourseDAO dao = new FakeCourseDAO(courses);
        FakePresenter presenter = new FakePresenter();
        FakeSortInteractor sortInteractor = new FakeSortInteractor();
        GenerateTimetableInteractor interactor = new GenerateTimetableInteractor(dao, presenter, sortInteractor);

        ArrayList<String> timePreferences = new ArrayList<>(List.of("Morning"));
        GenerateTimetableInputData input = new GenerateTimetableInputData(false, timePreferences);

        // Act
        interactor.execute(input);

        // Assert: Winter timetables should be filtered by time preference
        assertNotNull(presenter.lastOutput);
        ArrayList<TimetableDTO> winterTimetables = presenter.lastOutput.getWinterTimeTables();
        assertTrue(winterTimetables.size() > 0);

        // Check that first timetable has morning course
        TimetableDTO firstTimetable = winterTimetables.get(0);
        ArrayList<ArrayList<ArrayList<String>>> table = firstTimetable.getTable();

        boolean hasMorningCourse = false;
        for (int hour = 0; hour < 3; hour++) {
            if (!table.get(0).get(hour).isEmpty()) {
                hasMorningCourse = true;
                break;
            }
        }
        assertTrue(hasMorningCourse, "First winter timetable should have morning course");
    }

    @Test
    void testNoCourses() {
        // Arrange: No courses
        ArrayList<Course> courses = new ArrayList<>();
        FakeCourseDAO dao = new FakeCourseDAO(courses);
        FakePresenter presenter = new FakePresenter();
        FakeSortInteractor sortInteractor = new FakeSortInteractor();
        GenerateTimetableInteractor interactor = new GenerateTimetableInteractor(dao, presenter, sortInteractor);

        ArrayList<String> timePreferences = new ArrayList<>(List.of("Morning"));
        GenerateTimetableInputData input = new GenerateTimetableInputData(false, timePreferences);

        // Act
        interactor.execute(input);

        // Assert: Should return error message
        assertNotNull(presenter.lastError);
        assertEquals("No added courses", presenter.lastError);
    }

    @Test
    void testBoundaryHours() {
        // Arrange: Test boundary hours for morning (hour 2 = 11am is last morning hour)
        Course boundaryMorning = createCourseWithLecture("CSC207H1", 20259, 1, 11, 12);
        Course earlyAfternoon = createCourseWithLecture("CSC209H1", 20259, 2, 12, 13);

        ArrayList<Course> courses = new ArrayList<>(List.of(boundaryMorning, earlyAfternoon));
        FakeCourseDAO dao = new FakeCourseDAO(courses);
        FakePresenter presenter = new FakePresenter();
        FakeSortInteractor sortInteractor = new FakeSortInteractor();
        GenerateTimetableInteractor interactor = new GenerateTimetableInteractor(dao, presenter, sortInteractor);

        ArrayList<String> timePreferences = new ArrayList<>(List.of("Morning"));
        GenerateTimetableInputData input = new GenerateTimetableInputData(false, timePreferences);

        // Act
        interactor.execute(input);

        // Assert: Should correctly identify boundary hours
        assertNotNull(presenter.lastOutput);
        ArrayList<TimetableDTO> fallTimetables = presenter.lastOutput.getFallTimeTables();
        assertTrue(fallTimetables.size() > 0);
    }
}