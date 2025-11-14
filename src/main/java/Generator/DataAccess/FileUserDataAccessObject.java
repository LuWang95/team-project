package Generator.DataAccess;

import Generator.UseCase.add_course.AddCourseDataAccessInterface;
import CourseInfo.Course;
import Generator.UseCase.remove_course.RemoveCourseDataAccessInterface;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class FileUserDataAccessObject implements AddCourseDataAccessInterface, RemoveCourseDataAccessInterface {
    private final File csvFile;
    private final ArrayList<Course> courses;

    public FileUserDataAccessObject(String csvPath) {
        csvFile = new File(csvPath);
        courses = new ArrayList<>();
        save();

//        need to figure out how to load previous data from file into the view when the program restarts TODO
//        when that figures out the commented out code here will be relevant
//        for now the file just starts anew every time the program restarts

//        if (csvFile.length() == 0) {
//            save();
//        }
//        else {
//            try (BufferedReader reader = new BufferedReader(new FileReader(csvFile))) {
//                String courseLine = reader.readLine();
//                String[] courses = courseLine.split(",");
//                for (String course : courses) {
//                    this.courses.add(new Course(course, null, 0, 0, null, null));
//                }
//            }
//            catch (IOException ex) {
//                throw new RuntimeException(ex);
//            }
//        }
    }

    // rewrites selectedCourses.csv in the following format:
    // courseCode1,courseCode2,courseCode3
    // degreeCode1,degreeCode2,degreeCode3 (not actually, but will later)
    private void save() {
        final BufferedWriter writer;
        try {
            writer = new BufferedWriter(new FileWriter(csvFile));

            StringBuilder courseString = new StringBuilder();
            for (Course course : courses) {
                courseString.append(course.getCourseCode()).append(",");
            }
            if (!courseString.toString().isEmpty()) {
                courseString.deleteCharAt(courseString.length() - 1);
            }
            writer.write(courseString.toString());

            writer.close();
        }
        catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    // checks to see if specified course is already in the courses ArrayList
    @Override
    public boolean existsByName(String course) {
        List<String> courseNames = courses.stream().map(Course::getCourseCode).collect(Collectors.toList());
        return courseNames.contains(course);
    }

    // adds course to the courses ArrayList
    @Override
    public void add(Course course) {
        courses.add(course);
        this.save();
    }

    // removes course from the courses ArrayList
    // the implementation may need to be improved lmao
    // simply using the remove method doesn't work because they'll be different course objects because the current
    // Course class is kinda wack
    @Override
    public void remove(Course course) {
        for (int i = courses.size() - 1; i >= 0; i--) {
            if (courses.get(i).getCourseCode().equals(course.getCourseCode())) {
                courses.remove(i);
                break;
            }
        }
        this.save();
    }
}
