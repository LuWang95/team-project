import CourseInfo.Meeting;
import CourseInfo.Section;

import java.util.ArrayList;

public class TimeTable {
    private ArrayList<String>[][] timeTable = new ArrayList[5][12];

    public TimeTable() {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 12; j++) {
                timeTable[i][j] = new ArrayList<>();
            }
        }
    }

    public ArrayList<String>[][] getTimeTable() {
        return timeTable;
    }

    public void setBlocks(Section section, String courseCode){
        ArrayList<Meeting> meetings = section.getMeetings();
        for (Meeting meeting : meetings) {
            int day = meeting.getDate();
            int startTime = meeting.getStartMinutes() / 60;
            int endTime = meeting.getEndMinutes() / 60;
            for(int i = startTime; i <= endTime; i++){
                timeTable[day][i].add(courseCode + section.getSectionCode());
            }

        }


    }



}
