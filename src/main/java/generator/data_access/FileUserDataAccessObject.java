package generator.data_access;

import courseinfo.Degree;
import generator.use_case.add_course.AddCourseDataAccessInterface;
import courseinfo.Course;
import generator.use_case.add_degree.AddDegreeDataAccessInterface;
import generator.use_case.remove_course.RemoveCourseDataAccessInterface;
import generator.use_case.remove_degree.RemoveDegreeDataAccessInterface;
import generator.use_case.generate_timetable.GenerateTimetableDataAccessInterface;

import generator.use_case.generate_timetable.TimetableDTO;               // ✅ NEW
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class FileUserDataAccessObject implements
        AddCourseDataAccessInterface,
        RemoveCourseDataAccessInterface,
        AddDegreeDataAccessInterface,
        RemoveDegreeDataAccessInterface,
        GenerateTimetableDataAccessInterface,
        generator.data_access.SaveTimetableDataAccessInterface {

    private final File csvFile;
    private final ArrayList<Course> courses;
    private final ArrayList<Degree> degrees;
    private final JsonCourseDataAccess jsonAccess;
    private final JsonDegreeDataAccess jsonDegreeAccess;
    public static int Year;

    public FileUserDataAccessObject(String userDataPath, String timetableDataPath, String degreeDataPath) {
        csvFile = new File(userDataPath);
        courses = new ArrayList<>();
        degrees = new ArrayList<>();
        Year = 0;
        jsonAccess = new JsonCourseDataAccess(timetableDataPath);
        jsonDegreeAccess = new JsonDegreeDataAccess(degreeDataPath);
        save();

        // TODO: loading previous data from file if you want persistence between runs
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
        List<String> courseNames = courses.stream()
                .map(Course::getCourseCode)
                .collect(Collectors.toList());
        return courseNames.contains(course);
    }

    @Override
    public boolean degreeAlreadyAdded(String degree) {
        List<String> degreeNames = degrees.stream()
                .map(Degree::getDegreeCode)
                .collect(Collectors.toList());
        return degreeNames.contains(degree);
    }

    @Override
    public boolean courseExists(String courseCode) {
        return jsonAccess.courseExists(courseCode);
    }

    @Override
    public boolean degreeExists(String degree) {
        return jsonDegreeAccess.degreeExists(degree);
    }

    @Override
    public Course getCoursebyCode(String courseCode) {
        return jsonAccess.getCoursebyCode(courseCode);
    }

    @Override
    public Degree getDegreeByCode(String degreeCode) {
        return jsonDegreeAccess.getDegreeByCode(degreeCode);
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

    // ✅ NEW: SaveTimetableDataAccessInterface implementation
    @Override
    public void saveTimetable(TimetableDTO fallTimetable,
                              TimetableDTO winterTimetable,
                              String fileName) throws IOException {

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write("Fall Timetable");
            writer.newLine();
            writeTimetable(writer, fallTimetable);

            writer.newLine();
            writer.write("Winter Timetable");
            writer.newLine();
            writeTimetable(writer, winterTimetable);
        }
    }

    // helper to write a single timetable table
    private void writeTimetable(BufferedWriter writer, TimetableDTO timetable) throws IOException {
        // TimetableDTO#getTable() is assumed to be ArrayList<ArrayList<ArrayList<String>>>
        ArrayList<ArrayList<ArrayList<String>>> table = timetable.getTable();

        for (ArrayList<ArrayList<String>> row : table) {
            for (int col = 0; col < row.size(); col++) {
                ArrayList<String> cell = row.get(col);
                String cellText = String.join("/", cell);
                writer.write(cellText);
                if (col < row.size() - 1) {
                    writer.write(",");
                }
            }
            writer.newLine();
        }
    }
}
