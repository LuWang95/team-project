package CourseInfo;

import java.util.ArrayList;

public class Section{
    String sectionCode;
    ArrayList<Meeting> meetings;

    public Section(String sectionCode, ArrayList<Meeting> meetings) {
        this.sectionCode = sectionCode;
        this.meetings = meetings;
    }
    public String getSectionCode() {
        return sectionCode;
    }
    public ArrayList<Meeting> getMeetings() {
        return meetings;
    }

}