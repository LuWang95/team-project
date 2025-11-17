import Generator.UseCases.DisplayTimeTable.*;
import CourseInfo.*;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class DisplayTimeTableInteractorTest {

    /**
     * A simple fake DAO that returns fixed courses.
     */
    private static class FakeDAO implements DisplayTimeTableDataAccessInterface {

        private final ArrayList<Course> added = new ArrayList<>();

        public FakeDAO() {
            // Create 1 course: CSC207 with one LEC and one TUT
            ArrayList<Section> lecs = new ArrayList<>();
            ArrayList<Section> tuts = new ArrayList<>();

            // LEC0101: Monday 10–11
            ArrayList<Meeting> lecMeetings = new ArrayList<>();
            lecMeetings.add(new Meeting("Prof A", "10:00", "11:00",
                    600, 660, "BA", 1));
            lecs.add(new LectureSection("LEC0101", lecMeetings));

            // TUT0201: Tuesday 14–15
            ArrayList<Meeting> tutMeetings = new ArrayList<>();
            tutMeetings.add(new Meeting("TA B", "14:00", "15:00",
                    840, 900, "BA", 2));
            tuts.add(new TutorialSection("TUT0201", tutMeetings));

            Course csc207 = new Course("CSC207", "Software Design", "20251",
                    0.5, lecs, tuts, new ArrayList<>());

            added.add(csc207);
        }

        @Override
        public ArrayList<Course> getAddedCourses() {
            return added;
        }
    }

    /**
     * A presenter spy to verify Interactor results.
     */
    private static class PresenterSpy implements DisplayTimeTableOutputBoundary {

        public DisplayTimeTableOutputData received;
        public boolean errorShown = false;

        @Override
        public void prepareErrorView(String errorMessage) {
            errorShown = true;
        }

        @Override
        public void prepareSuccessView(DisplayTimeTableOutputData data) {
            this.received = data;
        }
    }

    @Test
    public void testGenerateTimetable() {
        // Arrange
        FakeDAO dao = new FakeDAO();
        PresenterSpy presenter = new PresenterSpy();
        DisplayTimeTableInteractor interactor =
                new DisplayTimeTableInteractor(presenter, dao);

        // Act
        interactor.execute();

        // Assert
        assertFalse("No error should occur", presenter.errorShown);
        assertNotNull("Presenter should receive data", presenter.received);

        ArrayList<TimeTableDTO> timetables = presenter.received.getAllTimetables();
        assertEquals("Exactly 1 valid timetable expected", 1, timetables.size());

        TimeTableDTO dto = timetables.get(0);

        // Monday 10–11 → index = (10 - 9) = 1
        assertTrue(dto.getTable().get(0).get(1).contains("CSC207LEC0101"));

        // Tuesday 14–15 → index = (14 - 9) = 5
        assertTrue(dto.getTable().get(1).get(5).contains("CSC207TUT0201"));
    }
}
