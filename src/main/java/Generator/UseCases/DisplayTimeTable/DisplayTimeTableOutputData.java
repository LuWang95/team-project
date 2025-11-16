package Generator.UseCases.DisplayTimeTable;

import CourseInfo.TimeTable;


import java.util.ArrayList;

public class DisplayTimeTableOutputData {
    private ArrayList<TimeTableDTO> allTimeTables  = new ArrayList<>();


    public DisplayTimeTableOutputData(ArrayList<TimeTableDTO>  timeTables) {
        this.allTimeTables = timeTables;
    }

    public ArrayList<TimeTableDTO>  getAllTimetables() {
        return allTimeTables;
    }

    public void setAllTimeTables (ArrayList<TimeTableDTO>  timeTables) {
        this.allTimeTables = timeTables;
    }
}
