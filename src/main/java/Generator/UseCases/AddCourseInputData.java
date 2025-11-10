package Generator.UseCases;

import CourseInfo.Course;

import java.util.ArrayList;

public class AddCourseInputData {
    public String coursesCode;

    public AddCourseInputData(String coursesCode) {
        this.coursesCode = coursesCode;
    }

    public String getCourseCode() {
        return coursesCode;
    }
}
