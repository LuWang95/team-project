package generator.use_case.generate_timetable;

import course_info.Course;

import java.util.ArrayList;

public interface GenerateTimetableDataAccessInterface {
    ArrayList<Course> getCourses();

    Course getCoursebyCode(String courseCode);
}
