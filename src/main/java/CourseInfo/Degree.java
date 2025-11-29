package CourseInfo;

import Generator.DataAccess.JsonCourseDataAccess;

import java.util.ArrayList;

// mostly a whatever implementation that works well enough for the set preferences ui.
// i'm leaving it to franklin or smo else to make it better i believe in u lol
public class Degree {
     String degreeCode;
     String degreeName;
     ArrayList<String> courses;


 public Degree(String degreeCode, String degreeName, ArrayList<String> courses) {
        this.degreeCode = degreeCode;
        this.degreeName = degreeName;
        this.courses = new ArrayList<String>();
    }

    public String getDegreeCode() {
        return degreeCode;
    }

    public String getDegreeName() {
        return degreeName;
    }

    public ArrayList<String> getCourses() {
        return courses;
    }

 //   public ArrayList<Course> getReqs() {
   //  for (String course : courses) {
     //    JsonCourseDataAccess jsonCourseDataAccess = new JsonCourseDataAccess(course);
     //}}


    public void addCourse(String course) {
        this.courses.add(course);
    }

}
