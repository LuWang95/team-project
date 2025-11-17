package Generator.UseCase.add_course;

import CourseInfo.Section;

import java.util.ArrayList;

public class AddCourseOutputData {
    private final String courseName;
    private final String courseCode;
    private final ArrayList<Section> lectureSection;
    private final ArrayList<Section> tutorialSection;
    private final ArrayList<Section> practicalSection;
    private final double credit;
    private final String sessionCode;

    public AddCourseOutputData(String courseCode, String courseName,ArrayList<Section> lectureSection,ArrayList<Section> tutorialSection,
                               ArrayList<Section> practicalSection, double credit, String sessionCode) {
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.lectureSection = lectureSection;
        this.tutorialSection = tutorialSection;
        this.practicalSection = practicalSection;
        this.credit = credit;
        this.sessionCode = sessionCode;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getCourseCode() {
        return courseCode;
    }
    public ArrayList<Section> getLectureSection() {
        return lectureSection;
    }
    public ArrayList<Section> getTutorialSection() {
        return tutorialSection;
    }
    public ArrayList<Section> getPracticalSection() {
        return practicalSection;
    }

    public double getCredit() {
        return credit;
    }
    public String getSessionCode() {
        return sessionCode;
    }
}
