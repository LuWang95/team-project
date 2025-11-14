package Generator.UseCases.DisplayTimeTable;

import java.util.ArrayList;

public class DisplayTimeTableOutputData {
    private ArrayList<String[][]> timeTable;

    public DisplayTimeTableOutputData(ArrayList<String[][]> timeTable) {
        this.timeTable = timeTable;
    }

    public ArrayList<String[][]> getTimeTable() {
        return timeTable;
    }

    public void setTimeTable(ArrayList<String[][]> timeTable) {
        this.timeTable = timeTable;
    }
}
