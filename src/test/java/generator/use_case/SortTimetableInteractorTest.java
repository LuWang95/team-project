package Generator.use_case;

import generator.use_case.sort_timetable.SortTimetableInteractor;
import generator.use_case.sort_timetable.SortTimetableInputData;
import generator.use_case.sort_timetable.SortTimetableOutputData;
import generator.use_case.sort_timetable.DistanceDataAccessInterface;
import generator.use_case.generate_timetable.GenerateTimetableDataAccessInterface;
import generator.use_case.generate_timetable.TimetableDTO;
import course_info.Course;
import course_info.Section;
import course_info.Meeting;

import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

/**
 * Tests for SortTimetableInteractor.
 */
public class SortTimetableInteractorTest {

    /**
     * Normal case: both Fall and Winter have timetables.
     */
    @Test
    public void testSortFallAndWinterByWalkingDistance() throws Exception {
        InMemoryCourseDAO courseDAO = new InMemoryCourseDAO();
        courseDAO.addHalfCourseInBA("CSC207H1F", "LEC0101");
        courseDAO.addHalfCourseInSS("CSC236H1F", "LEC0101");

        RecordingDistanceDAO distanceDAO = new RecordingDistanceDAO(false);

        SortTimetableInteractor interactor =
                new SortTimetableInteractor(courseDAO, distanceDAO);

        // Fall: one timetable with walking, one with no walking
        TimetableDTO fallZero = makeTwoSlotTimetable(
                "CSC207H1FLEC0101",  // BA
                "CSC207H1FLEC0101"   // BA again
        );
        TimetableDTO fallSome = makeTwoSlotTimetable(
                "CSC207H1FLEC0101",  // BA
                "CSC236H1FLEC0101"   // SS
        );

        // Winter
        TimetableDTO winterZero = makeTwoSlotTimetable(
                "CSC207H1FLEC0101",
                "CSC207H1FLEC0101"
        );
        TimetableDTO winterSome = makeTwoSlotTimetable(
                "CSC207H1FLEC0101",
                "CSC236H1FLEC0101"
        );

        List<TimetableDTO> fall = new ArrayList<>();
        fall.add(fallSome);
        fall.add(fallZero);

        List<TimetableDTO> winter = new ArrayList<>();
        winter.add(winterSome);
        winter.add(winterZero);

        SortTimetableInputData inputData =
                new SortTimetableInputData(fall, winter);

        SortTimetableOutputData output = interactor.sort(inputData);

        // Fall sorted
        List<TimetableDTO> sortedFall = output.getFallTimetables();
        assertEquals(2, sortedFall.size());
        assertSame(fallZero, sortedFall.get(0));
        assertSame(fallSome, sortedFall.get(1));

        // Winter sorted
        List<TimetableDTO> sortedWinter = output.getWinterTimetables();
        assertEquals(2, sortedWinter.size());
        assertSame(winterZero, sortedWinter.get(0));
        assertSame(winterSome, sortedWinter.get(1));

        assertTrue(distanceDAO.wasCalledFor("BA", "SS"));
    }

    /**
     * Distance lookup throws -> POSITIVE_INFINITY.
     */
    @Test
    public void testDistanceLookupExceptionPushesTimetableToEnd() throws Exception {
        InMemoryCourseDAO courseDAO = new InMemoryCourseDAO();
        courseDAO.addHalfCourseInBA("CSC207H1F", "LEC0101");
        courseDAO.addHalfCourseInSS("CSC236H1F", "LEC0101");

        RecordingDistanceDAO distanceDAO = new RecordingDistanceDAO(true);

        SortTimetableInteractor interactor =
                new SortTimetableInteractor(courseDAO, distanceDAO);

        TimetableDTO good = makeTwoSlotTimetable(
                "CSC207H1FLEC0101",
                "CSC207H1FLEC0101"
        );
        TimetableDTO bad = makeTwoSlotTimetable(
                "CSC207H1FLEC0101",
                "CSC236H1FLEC0101"
        );

        List<TimetableDTO> fall = new ArrayList<>();
        fall.add(bad);
        fall.add(good);

        SortTimetableInputData input =
                new SortTimetableInputData(fall, Collections.emptyList());

        SortTimetableOutputData output = interactor.sort(input);

        List<TimetableDTO> sortedFall = output.getFallTimetables();
        assertSame(good, sortedFall.get(0));
        assertSame(bad, sortedFall.get(1));
    }

    /**
     * Full-year course parsing branch (CSC110Y1...).
     */
    @Test
    public void testFullYearCourseParsingBranch() throws Exception {
        InMemoryCourseDAO courseDAO = new InMemoryCourseDAO();
        courseDAO.addFullYearCourseInBA("CSC110Y1", "LEC0101");

        RecordingDistanceDAO distanceDAO = new RecordingDistanceDAO(false);

        SortTimetableInteractor interactor =
                new SortTimetableInteractor(courseDAO, distanceDAO);

        TimetableDTO dto = makeTwoSlotTimetable(
                "CSC110Y1LEC0101",
                "CSC110Y1LEC0101"
        );

        List<TimetableDTO> fall = new ArrayList<>();
        fall.add(dto);

        SortTimetableInputData input =
                new SortTimetableInputData(fall, Collections.emptyList());

        SortTimetableOutputData output = interactor.sort(input);
        assertEquals(1, output.getFallTimetables().size());
    }

    /**
     * Course not found -> courseDataAccess returns null.
     */
    @Test
    public void testCourseNotFoundIsHandledGracefully() throws Exception {
        InMemoryCourseDAO courseDAO = new InMemoryCourseDAO(); // empty
        RecordingDistanceDAO distanceDAO = new RecordingDistanceDAO(false);

        SortTimetableInteractor interactor =
                new SortTimetableInteractor(courseDAO, distanceDAO);

        TimetableDTO dto = makeTwoSlotTimetable(
                "CSC999H1FLEC0101",
                "CSC999H1FLEC0101"
        );

        List<TimetableDTO> fall = new ArrayList<>();
        fall.add(dto);

        SortTimetableInputData input =
                new SortTimetableInputData(fall, Collections.emptyList());

        SortTimetableOutputData output = interactor.sort(input);
        assertEquals(1, output.getFallTimetables().size());
    }

    /**
     * Exercise branches where lectureSections == null and
     * tutorialSections exist but have no matching section code.
     */
    @Test
    public void testFindSectionByCodeNullAndNoMatchBranches() throws Exception {
        GenerateTimetableDataAccessInterface specialDAO = new GenerateTimetableDataAccessInterface() {
            @Override
            public ArrayList<Course> getCourses() {
                return new ArrayList<>();
            }

            @Override
            public Course getCoursebyCode(String courseCode) {
                // tutorial section code does NOT match LEC0101
                Meeting meeting = new Meeting("", "", "", 0, 0, "BA", 0);
                ArrayList<Meeting> meetings = new ArrayList<>();
                meetings.add(meeting);
                Section tutorial = new Section("LEC9999", meetings);
                ArrayList<Section> tutorials = new ArrayList<>();
                tutorials.add(tutorial);

                return new Course(
                        courseCode,
                        courseCode + " Name",
                        20249,
                        0.5,
                        null,          // lectureSections
                        tutorials,     // tutorialSections
                        null           // practicalSections
                );
            }
        };

        RecordingDistanceDAO distanceDAO = new RecordingDistanceDAO(false);
        SortTimetableInteractor interactor =
                new SortTimetableInteractor(specialDAO, distanceDAO);

        TimetableDTO dto = makeTwoSlotTimetable(
                "CSC300H1FLEC0101",
                "CSC300H1FLEC0101"
        );

        List<TimetableDTO> fall = Collections.singletonList(dto);
        SortTimetableInputData input =
                new SortTimetableInputData(fall, Collections.emptyList());

        SortTimetableOutputData output = interactor.sort(input);
        assertEquals(1, output.getFallTimetables().size());
    }

    /**
     * Section exists but has an empty meetings list.
     * Hits branch where (section != null && section.getMeetings().isEmpty()).
     */
    @Test
    public void testSectionWithNoMeetingsBranch() throws Exception {
        GenerateTimetableDataAccessInterface dao = new GenerateTimetableDataAccessInterface() {
            @Override
            public ArrayList<Course> getCourses() {
                return new ArrayList<>();
            }

            @Override
            public Course getCoursebyCode(String courseCode) {
                // Section present but meetings list is empty
                Section section = new Section("LEC0101", new ArrayList<>());
                ArrayList<Section> lectures = new ArrayList<>();
                lectures.add(section);

                return new Course(
                        courseCode,
                        courseCode + " Name",
                        20249,
                        0.5,
                        lectures,        // non-null lectures
                        null,
                        null
                );
            }
        };

        RecordingDistanceDAO distanceDAO = new RecordingDistanceDAO(false);
        SortTimetableInteractor interactor =
                new SortTimetableInteractor(dao, distanceDAO);

        TimetableDTO dto = makeTwoSlotTimetable(
                "CSC400H1FLEC0101",
                "CSC400H1FLEC0101"
        );

        List<TimetableDTO> fall = Collections.singletonList(dto);
        SortTimetableInputData input =
                new SortTimetableInputData(fall, Collections.emptyList());

        // Should not throw; building will be cached as null from "empty meetings" path
        SortTimetableOutputData output = interactor.sort(input);
        assertEquals(1, output.getFallTimetables().size());
    }

    /**
     * First class has a building, second class' course has no building (null).
     * Hits branch where havePrev is true and prevBuilding != null but building == null.
     */
    @Test
    public void testPrevBuildingNonNullCurrentNullBranch() throws Exception {
        // DAO with one normal course and one missing course
        InMemoryCourseDAO dao = new InMemoryCourseDAO();
        dao.addHalfCourseInBA("CSC207H1F", "LEC0101");
        // do NOT add CSC999H1F -> courseDataAccess returns null for it

        RecordingDistanceDAO distanceDAO = new RecordingDistanceDAO(false);
        SortTimetableInteractor interactor =
                new SortTimetableInteractor(dao, distanceDAO);

        // First cell: existing course (BA), second cell: missing course (building null)
        TimetableDTO dto = makeTwoSlotTimetable(
                "CSC207H1FLEC0101",
                "CSC999H1FLEC0101"
        );

        List<TimetableDTO> fall = Collections.singletonList(dto);
        SortTimetableInputData input =
                new SortTimetableInputData(fall, Collections.emptyList());

        SortTimetableOutputData output = interactor.sort(input);
        assertEquals(1, output.getFallTimetables().size());

        // There should be no distance call from BA -> null
        assertFalse(distanceDAO.wasCalledFor("BA", "null"));
    }

    // ======================================================
    // Helpers / fake DAOs
    // ======================================================

    private TimetableDTO makeTwoSlotTimetable(String block1, String block2) {
        ArrayList<ArrayList<ArrayList<String>>> table = new ArrayList<>();

        for (int day = 0; day < 5; day++) {
            ArrayList<ArrayList<String>> dayRow = new ArrayList<>();
            for (int slot = 0; slot < 10; slot++) {
                dayRow.add(new ArrayList<>());
            }
            table.add(dayRow);
        }

        table.get(0).get(0).add(block1);
        table.get(0).get(1).add(block2);

        return new TimetableDTO(table);
    }

    private static class InMemoryCourseDAO implements GenerateTimetableDataAccessInterface {
        private final Map<String, Course> courses = new HashMap<>();

        void addHalfCourseInBA(String courseCode, String sectionCode) {
            courses.put(courseCode,
                    TestCourseFactory.makeCourseWithSingleSection(
                            courseCode, sectionCode, "BA"
                    ));
        }

        void addHalfCourseInSS(String courseCode, String sectionCode) {
            courses.put(courseCode,
                    TestCourseFactory.makeCourseWithSingleSection(
                            courseCode, sectionCode, "SS"
                    ));
        }

        void addFullYearCourseInBA(String courseCode, String sectionCode) {
            courses.put(courseCode,
                    TestCourseFactory.makeCourseWithSingleSection(
                            courseCode, sectionCode, "BA"
                    ));
        }

        @Override
        public ArrayList<Course> getCourses() {
            return new ArrayList<>(courses.values());
        }

        @Override
        public Course getCoursebyCode(String courseCode) {
            return courses.get(courseCode);
        }
    }

    private static class RecordingDistanceDAO implements DistanceDataAccessInterface {
        private final boolean alwaysThrow;
        private final Set<String> calledKeys = new HashSet<>();

        RecordingDistanceDAO(boolean alwaysThrow) {
            this.alwaysThrow = alwaysThrow;
        }

        @Override
        public double getWalkingDistance(String fromBuilding, String toBuilding)
                throws DistanceLookupException {
            calledKeys.add(fromBuilding + "->" + toBuilding);
            if (alwaysThrow) {
                throw new DistanceLookupException("fake failure", null);
            }
            return fromBuilding.equals(toBuilding) ? 0.0 : 1000.0;
        }

        boolean wasCalledFor(String from, String to) {
            return calledKeys.contains(from + "->" + to);
        }
    }

    private static class TestCourseFactory {
        static Course makeCourseWithSingleSection(String courseCode,
                                                  String sectionCode,
                                                  String building) {
            Meeting meeting = new Meeting(
                    "", "", "", 0, 0, building, 0
            );
            ArrayList<Meeting> meetings = new ArrayList<>();
            meetings.add(meeting);

            Section section = new Section(sectionCode, meetings);

            ArrayList<Section> lectures = new ArrayList<>();
            lectures.add(section);

            return new Course(
                    courseCode,
                    courseCode + " Name",
                    20249,
                    0.5,
                    lectures,
                    new ArrayList<>(),
                    new ArrayList<>()
            );
        }
    }

    /**
     * Extra branch coverage for the big parsing if:
     *  - block with 'S' at index 8  -> left of || false, right true
     *  - block of length 8 with 'H' at index 6 -> length>=9 branch false
     */
    @Test
    public void testHalfCourseParsingSAndLengthEightBranches() throws Exception {
        // Empty DAO is fine; we only care about the parsing branches.
        InMemoryCourseDAO courseDAO = new InMemoryCourseDAO();
        RecordingDistanceDAO distanceDAO = new RecordingDistanceDAO(false);

        SortTimetableInteractor interactor =
                new SortTimetableInteractor(courseDAO, distanceDAO);

        // First block: half-course with 'S' → hits (charAt(8)=='F' || charAt(8)=='S')
        // where left is false, right is true.
        String sBlock = "CSC207H1SLEC0101";

        // Second block: length 8, charAt(6)=='H' → length>=8 true, charAt(6)=='H' true,
        // but length>=9 false.
        String length8Block = "ABCDEFH1"; // indices: 0..7, charAt(6) == 'H'

        TimetableDTO dto = makeTwoSlotTimetable(sBlock, length8Block);

        List<TimetableDTO> fall = Collections.singletonList(dto);
        SortTimetableInputData input =
                new SortTimetableInputData(fall, Collections.emptyList());

        SortTimetableOutputData output = interactor.sort(input);

        // Just verify it runs without throwing and returns the one timetable.
        assertEquals(1, output.getFallTimetables().size());
    }
}
