package CourseInfo;

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

    // This constructor is used for the DisplayTimeTable Interactor.
    public TimeTable(TimeTable other) {
        this.timeTable = new ArrayList[5][12];
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 12; j++) {
                this.timeTable[i][j] = new ArrayList<>(other.timeTable[i][j]);
            }
        }
    }

    public ArrayList<String>[][] getTimeTable() {
        return timeTable;
    }

    public TimeTable setBlocks(Section section, String courseCode){
        ArrayList<Meeting> meetings = section.getMeetings();
        for (Meeting meeting : meetings) {
            int day = meeting.getDate() - 1; //Monday has the index of 0.
            int startTime = (meeting.getStartMinutes() / 60) - 1; // 9:00 has the index of 0.
            int endTime = (meeting.getEndMinutes() / 60) - 1;// 11:00 has the index of 11.
            for(int i = startTime; i <= endTime; i++){
                timeTable[day][i].add(courseCode + section.getSectionCode());
            }

        }
        return this;


    }

    public boolean isValid(){
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 12; j++) {
                if (timeTable[i][j].size() > 1) {
                    return false;
                }
            }
        }
        return true;
    }



}
