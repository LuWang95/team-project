package Generator.UseCases;

import CourseInfo.Course;

import java.util.ArrayList;

public class AddCourseInputData {
    public ArrayList<Course> courses;

    public AddCourseInputData(ArrayList<Course> courses) {
        this.courses = courses;
    }

    public ArrayList<Course> getCourseNames() {
        return this.courses;
    }
}
