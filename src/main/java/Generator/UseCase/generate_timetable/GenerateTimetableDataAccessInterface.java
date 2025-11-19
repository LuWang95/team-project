package Generator.UseCase.generate_timetable;

import CourseInfo.Course;

import java.util.ArrayList;

public interface GenerateTimetableDataAccessInterface {
    ArrayList<Course> getCourses();

    // Returns default (no restrictions) if not implemented
    default TimePreferences getUserTimePreferences() {
        return new TimePreferences(); // Default allows all times
    }
}
