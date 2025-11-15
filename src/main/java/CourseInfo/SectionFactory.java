package CourseInfo;

import java.util.ArrayList;

public  class SectionFactory {
    public Section createSection(String sectionCode, ArrayList<Meeting> meetings) {
        String sectionType = sectionCode.substring(0, 3);
        switch (sectionType){
            case "LEC":
                return new LectureSection(sectionCode, meetings);
            case "TUT":
                return new TutorialSection(sectionCode, meetings);
            case "PRA":
                return new PracticalSection(sectionCode, meetings);
            default:
                throw new IllegalArgumentException(
                        "Wrong shape type: " + sectionType
                );
        }

    };
}
