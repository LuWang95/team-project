package Generator.UseCases.DisplayTimeTable;

import CourseInfo.LectureSection;
import CourseInfo.PracticalSection;
import CourseInfo.TutorialSection;

import java.util.ArrayList;

public class DisplayTimeTableInputData {
    private final ArrayList<String> courseCodes = new ArrayList<>();
    private final ArrayList<String> courseNames = new ArrayList<>();
    private final ArrayList<ArrayList<LectureSection>> lectureSections = new ArrayList<>();
    private final ArrayList<ArrayList<TutorialSection>> tutorialSections = new ArrayList<>();
    private final ArrayList<ArrayList<PracticalSection>> practicalSections = new ArrayList<>();
    private final ArrayList<Double> credit = new ArrayList<>();

    public DisplayTimeTableInputData( ArrayList<String> courseCodes,ArrayList<String> courseNames,ArrayList<ArrayList<LectureSection>> lectureSections ,ArrayList<ArrayList<TutorialSection>> tutorialSections , ArrayList<ArrayList<PracticalSection>> practicalSections,ArrayList<Double> credit ) {
        this.courseCodes.addAll(courseCodes);
        this.courseNames.addAll(courseNames);
        this.lectureSections.addAll(lectureSections);
        this.tutorialSections.addAll(tutorialSections);
        this.practicalSections.addAll(practicalSections);
        this.credit.addAll(credit);

    }

    public ArrayList<String> getCourses() {
        return courseCodes;
    }
    public ArrayList<String > getCourseNames() {
        return courseNames;
    }
    public ArrayList<ArrayList<LectureSection>> getLectureSections() {
        return lectureSections;
    }
    public ArrayList<ArrayList<TutorialSection>> getTutorialSections() {
        return tutorialSections;
    }
    public ArrayList<ArrayList<PracticalSection>> getPracticalSections() {
        return practicalSections;
    }
    public ArrayList<Double> getCredit() {
        return credit;
    }
}
