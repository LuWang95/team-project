import CourseInfo.Requirement;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestRequirement {

    @Test
    public void testSingleCourse() {
        Requirement requirement = new Requirement("CSC110Y1");
        ArrayList<String> courses =  requirement.getCourses();
        assertEquals(new ArrayList<>(List.of("CSC110Y1")), courses);
        assertTrue(requirement.fulfillsRequirement(new ArrayList<>(List.of("CSC110Y1"))));
        assertFalse(requirement.fulfillsRequirement(new ArrayList<>(List.of("CSC111H1"))));
        assertTrue(requirement.fulfillsRequirement(new ArrayList<>(List.of("CSC110Y1", "CSC111H1", "MAT137Y1"))));
    }

    @Test
    public void testTwoAndCourses() {
        Requirement requirement = new Requirement("CSC110Y1, CSC111H1");
        ArrayList<String> courses =  requirement.getCourses();
        assertTrue(courses.contains("CSC110Y1"));
        assertTrue(courses.contains("CSC111H1"));
        assertTrue(requirement.fulfillsRequirement(new ArrayList<>(List.of("CSC110Y1", "CSC111H1"))));
        assertFalse(requirement.fulfillsRequirement(new ArrayList<>(List.of("CSC111H1"))));
    }

    @Test
    public void testTwoOrCourses() {
        Requirement requirement = new Requirement("MAT137Y1/ MAT157Y1");
        ArrayList<String> courses =  requirement.getCourses();
        assertTrue(courses.contains("MAT137Y1") || courses.contains("MAT157Y1"));
        assertEquals(1, courses.size());
        assertTrue(requirement.fulfillsRequirement(new ArrayList<>(List.of("MAT137Y1"))));
        assertTrue(requirement.fulfillsRequirement(new ArrayList<>(List.of("MAT157Y1"))));
        assertTrue(requirement.fulfillsRequirement(new ArrayList<>(List.of("MAT137Y1", "MAT157Y1"))));
    }

    @Test
    public void testThreeAndCourses() {
        Requirement requirement = new Requirement("CSC108H1, CSC148H1, CSC165H1");
        ArrayList<String> courses =  requirement.getCourses();
        assertTrue(courses.contains("CSC108H1"));
        assertTrue(courses.contains("CSC148H1"));
        assertTrue(courses.contains("CSC165H1"));
        assertTrue(requirement.fulfillsRequirement(new ArrayList<>(List.of("CSC108H1", "CSC148H1", "CSC165H1"))));
        assertFalse(requirement.fulfillsRequirement(new ArrayList<>(List.of("CSC108H1", "CSC148H1"))));
        assertFalse(requirement.fulfillsRequirement(new ArrayList<>(List.of("CSC108H1", "CSC165H1"))));
    }

    @Test
    public void testThreeOrCourses() {
        Requirement requirement = new Requirement("STA237H1/ STA255H1/ STA257H1");
        ArrayList<String> courses =  requirement.getCourses();
        assertTrue(courses.contains("STA237H1") || courses.contains("STA255H1") ||  courses.contains("STA257H1"));
        assertEquals(1, courses.size());
        assertTrue(requirement.fulfillsRequirement(new ArrayList<>(List.of("STA237H1", "STA255H1"))));
        assertFalse(requirement.fulfillsRequirement(new ArrayList<>(List.of("CSC108H1", "CSC148H1"))));
        assertTrue(requirement.fulfillsRequirement(new ArrayList<>(List.of("STA257H1"))));
    }

    @Test
    public void testThreeMixedCourses1() {
        Requirement requirement = new Requirement("CSC110Y1, CSC111H1, MAT137Y1/ MAT157Y1");
        ArrayList<String> courses =  requirement.getCourses();
        assertTrue(courses.contains("CSC110Y1"));
        assertTrue(courses.contains("CSC111H1"));
        assertTrue(courses.contains("MAT137Y1") || courses.contains("MAT157Y1"));
        assertEquals(3, courses.size());
        assertTrue(requirement.fulfillsRequirement(new ArrayList<>(List.of("CSC110Y1", "CSC111H1", "MAT137Y1"))));
        assertTrue(requirement.fulfillsRequirement(new ArrayList<>(List.of("CSC110Y1", "CSC111H1", "MAT157Y1"))));
        assertFalse(requirement.fulfillsRequirement(new ArrayList<>(List.of("CSC111H1", "MAT137Y1"))));
    }

    @Test
    public void testThreeMixedCourses2() {
        Requirement requirement = new Requirement(
                "MAT235Y1/ MAT237Y1/ MAT257Y1, CSC236H1/ CSC240H1, CSC263H1/ CSC265H1");
        ArrayList<String> courses =  requirement.getCourses();
        assertTrue(courses.contains("MAT235Y1") || courses.contains("MAT237Y1") || courses.contains("MAT257Y1"));
        assertTrue(courses.contains("CSC236H1") || courses.contains("CSC240H1"));
        assertTrue(courses.contains("CSC263H1") || courses.contains("CSC265H1"));
        assertEquals(3, courses.size());
        assertTrue(requirement.fulfillsRequirement(new ArrayList<>(List.of("CSC236H1", "CSC265H1", "MAT257Y1"))));
        assertTrue(requirement.fulfillsRequirement(new ArrayList<>(List.of("CSC236H1", "CSC263H1", "MAT257Y1"))));
    }

    @Test
    public void testNestedCourses1() {
        Requirement requirement = new Requirement("( MAT135H1, MAT136H1)/ MAT137Y1");
        ArrayList<String> courses =  requirement.getCourses();
        assertTrue((courses.contains("MAT135H1") && courses.contains("MAT136H1")) || courses.contains("MAT137Y1"));
        assertTrue(requirement.fulfillsRequirement(new ArrayList<>(List.of("MAT137Y1"))));
        assertTrue(requirement.fulfillsRequirement(new ArrayList<>(List.of("MAT135H1", "MAT136H1"))));
        assertFalse(requirement.fulfillsRequirement(new ArrayList<>(List.of("MAT135H1"))));
    }

    @Test
    public void testNestedCourses2() {
        Requirement requirement = new Requirement("MAT137Y1/ ( MAT135H1, MAT136H1)");
        ArrayList<String> courses =  requirement.getCourses();
        assertTrue((courses.contains("MAT135H1") && courses.contains("MAT136H1")) || courses.contains("MAT137Y1"));
        assertTrue(requirement.fulfillsRequirement(new ArrayList<>(List.of("MAT137Y1"))));
        assertTrue(requirement.fulfillsRequirement(new ArrayList<>(List.of("MAT135H1", "MAT136H1"))));
        assertFalse(requirement.fulfillsRequirement(new ArrayList<>(List.of("MAT135H1"))));
    }
}
