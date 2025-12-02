package generator.data_access;

import course_info.Course;
import course_info.LectureSection;
import course_info.PracticalSection;
import course_info.Section;
import course_info.TutorialSection;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.Assert.*;

public class JsonCourseDataAccessTest {

    /**
     * Integration-style unit test for JsonCourseDataAccess:
     * - writes a small JSON file on the fly
     * - constructs JsonCourseDataAccess with that file
     * - verifies course code normalization and section distribution
     */
    @Test
    public void testJsonCourseDataAccess_buildDomainCourses() throws Exception {
        // 1) Prepare a small JSON with:
        //    - CSC207H1 (F) with LEC + TUT
        //    - CSC209H1 (S) with PRA
        //    - CSC110Y1 (Y course) with LEC only
        String json = "[\n" +
                "  {\n" +
                "    \"course_code\": \"CSC207H1\",\n" +
                "    \"course_title\": \"Software Design\",\n" +
                "    \"campus\": \"STG\",\n" +
                "    \"session\": \"20259\",\n" +
                "    \"department_code\": \"CSC\",\n" +
                "    \"faculty_code\": \"ARTSC\",\n" +
                "    \"credit\": 0.5,\n" +
                "    \"section_code\": \"LEC0101\",\n" +
                "    \"component\": \"LEC\",\n" +
                "    \"instructors\": [\"Prof A\"],\n" +
                "    \"meetings\": [\n" +
                "      {\n" +
                "        \"day_of_week\": 1,\n" +
                "        \"day_abbr\": \"MON\",\n" +
                "        \"start\": \"10:00\",\n" +
                "        \"end\": \"11:00\",\n" +
                "        \"start_min\": 600,\n" +
                "        \"end_min\": 660,\n" +
                "        \"building_code\": \"BA\",\n" +
                "        \"room\": \"123\",\n" +
                "        \"session_code\": \"20259\",\n" +
                "        \"repetition\": \"ONCE_A_WEEK\"\n" +
                "      }\n" +
                "    ]\n" +
                "  },\n" +
                "  {\n" +
                "    \"course_code\": \"CSC207H1\",\n" +
                "    \"course_title\": \"Software Design\",\n" +
                "    \"campus\": \"STG\",\n" +
                "    \"session\": \"20259\",\n" +
                "    \"department_code\": \"CSC\",\n" +
                "    \"faculty_code\": \"ARTSC\",\n" +
                "    \"credit\": 0.5,\n" +
                "    \"section_code\": \"TUT0101\",\n" +
                "    \"component\": \"TUT\",\n" +
                "    \"instructors\": [\"TA 1\"],\n" +
                "    \"meetings\": [\n" +
                "      {\n" +
                "        \"day_of_week\": 2,\n" +
                "        \"day_abbr\": \"TUE\",\n" +
                "        \"start\": \"12:00\",\n" +
                "        \"end\": \"13:00\",\n" +
                "        \"start_min\": 720,\n" +
                "        \"end_min\": 780,\n" +
                "        \"building_code\": \"BA\",\n" +
                "        \"room\": \"124\",\n" +
                "        \"session_code\": \"20259\",\n" +
                "        \"repetition\": \"ONCE_A_WEEK\"\n" +
                "      }\n" +
                "    ]\n" +
                "  },\n" +
                "  {\n" +
                "    \"course_code\": \"CSC209H1\",\n" +
                "    \"course_title\": \"Systems Programming\",\n" +
                "    \"campus\": \"STG\",\n" +
                "    \"session\": \"20261\",\n" +
                "    \"department_code\": \"CSC\",\n" +
                "    \"faculty_code\": \"ARTSC\",\n" +
                "    \"credit\": 0.5,\n" +
                "    \"section_code\": \"PRA0101\",\n" +
                "    \"component\": \"PRA\",\n" +
                "    \"instructors\": [\"Prof B\"],\n" +
                "    \"meetings\": [\n" +
                "      {\n" +
                "        \"day_of_week\": 3,\n" +
                "        \"day_abbr\": \"WED\",\n" +
                "        \"start\": \"14:00\",\n" +
                "        \"end\": \"16:00\",\n" +
                "        \"start_min\": 840,\n" +
                "        \"end_min\": 960,\n" +
                "        \"building_code\": \"BA\",\n" +
                "        \"room\": \"2155\",\n" +
                "        \"session_code\": \"20261\",\n" +
                "        \"repetition\": \"ONCE_A_WEEK\"\n" +
                "      }\n" +
                "    ]\n" +
                "  },\n" +
                "  {\n" +
                "    \"course_code\": \"CSC110Y1\",\n" +
                "    \"course_title\": \"Foundations of Computer Science\",\n" +
                "    \"campus\": \"STG\",\n" +
                "    \"session\": \"20259\",\n" +
                "    \"department_code\": \"CSC\",\n" +
                "    \"faculty_code\": \"ARTSC\",\n" +
                "    \"credit\": 1.0,\n" +
                "    \"section_code\": \"LEC0101\",\n" +
                "    \"component\": \"LEC\",\n" +
                "    \"instructors\": [\"Prof C\"],\n" +
                "    \"meetings\": [\n" +
                "      {\n" +
                "        \"day_of_week\": 4,\n" +
                "        \"day_abbr\": \"THU\",\n" +
                "        \"start\": \"09:00\",\n" +
                "        \"end\": \"11:00\",\n" +
                "        \"start_min\": 540,\n" +
                "        \"end_min\": 660,\n" +
                "        \"building_code\": \"BA\",\n" +
                "        \"room\": \"1160\",\n" +
                "        \"session_code\": \"20259\",\n" +
                "        \"repetition\": \"ONCE_A_WEEK\"\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "]\n";

        // 2) Write JSON to a temporary file
        Path tempFile = Files.createTempFile("courses_test", ".json");
        Files.write(tempFile, json.getBytes(StandardCharsets.UTF_8));

        // 3) Construct DAO with the temp file
        JsonCourseDataAccess dao = new JsonCourseDataAccess(tempFile.toString());

        // 4) Check half-course in FALL: CSC207H1 → CSC207H1F
        assertTrue(dao.courseExists("CSC207H1F"));
        Course csc207 = dao.getCoursebyCode("CSC207H1F");
        assertNotNull(csc207);
        assertEquals("CSC207H1F", csc207.getCourseCode());
        assertEquals(20259, csc207.getSessionCode());
        assertEquals(0.5, csc207.getCredit(), 1e-9);

        // LEC + TUT should both be attached
        List<Section> lecSections = csc207.getLectureSections();
        List<Section> tutSections = csc207.getTutorialSections();
        assertEquals(1, lecSections.size());
        assertEquals(1, tutSections.size());
        assertTrue(lecSections.get(0) instanceof LectureSection);
        assertTrue(tutSections.get(0) instanceof TutorialSection);

        // 5) Check half-course in WINTER: CSC209H1 → CSC209H1S
        assertTrue(dao.courseExists("CSC209H1S"));
        Course csc209 = dao.getCoursebyCode("CSC209H1S");
        assertNotNull(csc209);
        assertEquals("CSC209H1S", csc209.getCourseCode());
        assertEquals(20261, csc209.getSessionCode());
        assertEquals(0.5, csc209.getCredit(), 1e-9);
        assertEquals(0, csc209.getLectureSections().size());
        assertEquals(0, csc209.getTutorialSections().size());
        assertEquals(1, csc209.getPracticalSections().size());
        assertTrue(csc209.getPracticalSections().get(0) instanceof PracticalSection);

        // 6) Check Y-course: CSC110Y1 (no F/S suffix added)
        assertTrue(dao.courseExists("CSC110Y1"));
        Course csc110 = dao.getCoursebyCode("CSC110Y1");
        assertNotNull(csc110);
        assertEquals("CSC110Y1", csc110.getCourseCode());
        assertEquals(20259, csc110.getSessionCode());
        assertEquals(1.0, csc110.getCredit(), 1e-9);
        assertEquals(1, csc110.getLectureSections().size());
    }


    @Test
    public void testLoadRaw_emptyJsonArray_returnsEmptyList() throws Exception {
        // create JSON file containing empty array []
        Path tempFile = Files.createTempFile("empty_courses_test", ".json");
        Files.write(tempFile, "[]".getBytes(StandardCharsets.UTF_8));

        // DAO should not crash
        JsonCourseDataAccess dao = new JsonCourseDataAccess(tempFile.toString());

        // No course should be loaded
        assertFalse(dao.courseExists("ANY123H1"));
        assertNull(dao.getCoursebyCode("ANY123H1"));
    }


    @Test
    public void testLoadRaw_fileDoesNotExist_returnsEmptyList() {
        // This file path definitely does not exist
        String nonExistentPath = "this/path/does/not/exist_12345.json";

        // Should NOT throw → catch block prints stack trace and continues
        JsonCourseDataAccess dao = new JsonCourseDataAccess(nonExistentPath);

        // DAO should behave as empty
        assertFalse(dao.courseExists("CSC207H1"));
        assertNull(dao.getCoursebyCode("CSC207H1"));
    }


    @Test
    public void testLoadRaw_jsonLiteralNull_returnsEmptyList() throws Exception {
        Path tempFile = Files.createTempFile("null_json_test", ".json");
        Files.write(tempFile, "null".getBytes(StandardCharsets.UTF_8));

        JsonCourseDataAccess dao = new JsonCourseDataAccess(tempFile.toString());

        assertFalse(dao.courseExists("CSC207H1F"));
        assertNull(dao.getCoursebyCode("CSC207H1F"));
    }

    @Test
    public void testBuildDomainCourses_invalidSectionCode_isSkipped() throws Exception {
        // A single course record whose section_code has invalid prefix "XXX"
        String json = "[\n" +
                "  {\n" +
                "    \"course_code\": \"ABC123H1\",\n" +
                "    \"course_title\": \"Some Course\",\n" +
                "    \"campus\": \"STG\",\n" +
                "    \"session\": \"20259\",\n" +
                "    \"department_code\": \"TST\",\n" +
                "    \"faculty_code\": \"ARTSC\",\n" +
                "    \"credit\": 0.5,\n" +
                "    \"section_code\": \"XXX0101\",\n" +          // <-- invalid prefix
                "    \"component\": \"LEC\",\n" +
                "    \"instructors\": [\"Prof X\"],\n" +
                "    \"meetings\": [\n" +
                "      {\n" +
                "        \"day_of_week\": 1,\n" +
                "        \"day_abbr\": \"MON\",\n" +
                "        \"start\": \"10:00\",\n" +
                "        \"end\": \"11:00\",\n" +
                "        \"start_min\": 600,\n" +
                "        \"end_min\": 660,\n" +
                "        \"building_code\": \"BA\",\n" +
                "        \"room\": \"100\",\n" +
                "        \"session_code\": \"20259\",\n" +
                "        \"repetition\": \"ONCE_A_WEEK\"\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "]\n";

        // Write JSON to a temp file
        Path tempFile = Files.createTempFile("invalid_section_test", ".json");
        Files.write(tempFile, json.getBytes(StandardCharsets.UTF_8));

        // Construct DAO; this will run buildDomainCourses and hit the catch branch
        JsonCourseDataAccess dao = new JsonCourseDataAccess(tempFile.toString());

        // H-course in 20259 → fullCode = "ABC123H1F"
        assertTrue("Course object should still be created", dao.courseExists("ABC123H1F"));
        Course course = dao.getCoursebyCode("ABC123H1F");
        assertNotNull(course);

        // But the invalid section should have been skipped, so no sections attached
        assertEquals(0, course.getLectureSections().size());
        assertEquals(0, course.getTutorialSections().size());
        assertEquals(0, course.getPracticalSections().size());
    }
}