package CourseInfo;

import java.util.ArrayList;

public class Timetable {
    private ArrayList<String>[][] timeTable = new ArrayList[5][12];

    public Timetable() {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 12; j++) {
                timeTable[i][j] = new ArrayList<>();
            }
        }
    }

    public Timetable(Timetable other) {
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

    public Timetable setBlocks(Section section, String courseCode){
        ArrayList<Meeting> meetings = section.getMeetings();
        for (Meeting meeting : meetings) {
            int day = meeting.getDate() - 1;
            int startTime = (meeting.getStartMinutes() / 60) - 9;
            int endTime = (meeting.getEndMinutes() / 60) - 9;
            for(int i = startTime; i < endTime; i++){
                if (timeTable[day][i].contains(courseCode + section.getSectionCode())) {
                    continue;
                }
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

    public void printTimetable() {
        for (int i = 0; i < 12; i++) {
            for (int j = 0; j < 5; j++) {
                if (timeTable[j][i].isEmpty()) {
                    System.out.print("[              ]");
                }
                else {
                    System.out.print("[" + timeTable[j][i].get(0).substring(0, 6) + " " +
                            timeTable[j][i].get(0).substring(8, 15) + "]");
                }
                System.out.print(" ");
            }
            System.out.println();
        }
    }
}
