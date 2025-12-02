/**
 * Tests the AddCourseDataAccessInterface implementation through FileUserDataAccessObject and, by extension,
 * tests the Course class as well.
 */
package generator.data_access;

import course_info.Course;
import generator.use_case.add_course.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AddCourseDataAccessInterfaceTest {

    /**
     * Tests if getCourseByCode returns the correct course
     */
    @Test
    public void getCourseByCodeTest() {

        AddCourseDataAccessInterface dataAccessInterface = new FileUserDataAccessObject("selectedPreferences.csv",
                "artsci_timetable.json",
                "Programs.json"
        );

        Course course = dataAccessInterface.getCoursebyCode("CSC111H1S");
        assertEquals("CSC111H1S", course.getCourseCode());
        assertEquals("Foundations of Computer Science II", course.getCourseTitle());
    }

    /**
     * Tests if getCourseByCode returns null if an invalid course code is put in
     */
    @Test
    public void getCourseByCodeFailureTest() {
        AddCourseDataAccessInterface dataAccessInterface = new FileUserDataAccessObject("selectedPreferences.csv",
                "artsci_timetable.json",
                "Programs.json"
        );

        Course course = dataAccessInterface.getCoursebyCode("CSC999H1F");
        assertNull(course);
    }

    /**
     * Tests courseExists on a valid and invalid course.
     */
    @Test
    public void courseExistsTest() {
        AddCourseDataAccessInterface dataAccessInterface = new FileUserDataAccessObject("selectedPreferences.csv",
                "artsci_timetable.json",
                "Programs.json"
        );

        assertTrue(dataAccessInterface.courseExists("CSC111H1S"));
        assertFalse(dataAccessInterface.courseExists("CSC999H1F"));
    }

    /**
     * Tests the add and courseAlreadyAdded methods by successively adding courses and checking if they are in there.
     */
    @Test
    public void addCourseTest() {
        AddCourseDataAccessInterface dataAccessInterface = new FileUserDataAccessObject("selectedPreferences.csv",
                "artsci_timetable.json",
                "Programs.json"
        );

        assertFalse(dataAccessInterface.courseAlreadyAdded("CSC111H1S"));
        assertFalse(dataAccessInterface.courseAlreadyAdded("MAT137Y1"));

        dataAccessInterface.add(dataAccessInterface.getCoursebyCode("CSC111H1S"));
        assertTrue(dataAccessInterface.courseAlreadyAdded("CSC111H1S"));
        assertFalse(dataAccessInterface.courseAlreadyAdded("MAT137Y1"));

        dataAccessInterface.add(dataAccessInterface.getCoursebyCode("MAT137Y1"));
        assertTrue(dataAccessInterface.courseAlreadyAdded("MAT137Y1"));
        assertTrue(dataAccessInterface.courseAlreadyAdded("CSC111H1S"));

    }
}
