package Generator.DataAccess;

import CourseInfo.Degree;
import Generator.UseCase.add_course.AddCourseDataAccessInterface;
import CourseInfo.Course;
import Generator.UseCase.add_degree.AddDegreeDataAccessInterface;
import Generator.UseCase.remove_course.RemoveCourseDataAccessInterface;
import Generator.UseCase.remove_degree.RemoveDegreeDataAccessInterface;
import Generator.UseCase.generate_timetable.GenerateTimetableDataAccessInterface;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class FileUserDataAccessObject implements AddCourseDataAccessInterface, RemoveCourseDataAccessInterface,
        AddDegreeDataAccessInterface, RemoveDegreeDataAccessInterface, GenerateTimetableDataAccessInterface {
    private final File csvFile;
    private final ArrayList<Course> courses;
    private final ArrayList<Degree> degrees;
    private final JsonCourseDataAccess jsonAccess;

    public FileUserDataAccessObject(String userDataPath, String timetableDataPath) {
        csvFile = new File(userDataPath);
        courses = new ArrayList<>();
        degrees = new ArrayList<>();
        jsonAccess = new JsonCourseDataAccess(timetableDataPath);
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

            writer.newLine();

            StringBuilder degreeString = new StringBuilder();
            for (Degree degree : degrees) {
                degreeString.append(degree.getDegreeCode()).append(",");
            }
            if (!degreeString.toString().isEmpty()) {
                degreeString.deleteCharAt(degreeString.length() - 1);
            }
            writer.write(degreeString.toString());

            writer.close();
        }
        catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    // checks to see if specified course is already in the courses ArrayList
    @Override
    public boolean courseAlreadyAdded(String course) {
        List<String> courseNames = courses.stream().map(Course::getCourseCode).collect(Collectors.toList());
        return courseNames.contains(course);
    }

    @Override
    public boolean degreeAlreadyAdded(String degree) {
        List<String> degreeNames = degrees.stream().map(Degree::getDegreeCode).collect(Collectors.toList());
        return degreeNames.contains(degree);
    }

    @Override
    public boolean courseExists(String courseCode) {
        return jsonAccess.courseExists(courseCode);
    }

    @Override
    public Course getCoursebyCode(String courseCode) {
        return jsonAccess.getCoursebyCode(courseCode);
    }

    // adds course to the courses ArrayList
    @Override
    public void add(Course course) {
        courses.add(course);
        this.save();
    }

    // adds degree to the degrees ArrayList
    @Override
    public void add(Degree degree) {
        degrees.add(degree);
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

    @Override
    public void remove(Degree degree) {
        for (int i = degrees.size() - 1; i >= 0; i--) {
            if (degrees.get(i).getDegreeCode().equals(degree.getDegreeCode())) {
                degrees.remove(i);
                break;
            }
        }
        this.save();
    }

    @Override
    public ArrayList<Course> getCourses() {
        return courses;
    }
}
