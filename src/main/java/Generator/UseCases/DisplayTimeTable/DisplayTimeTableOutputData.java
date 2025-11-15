package Generator.UseCases.DisplayTimeTable;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class DisplayTimeTableOutputData {
    private ArrayList<ArrayList<String>[][]> allTimeTables  = new ArrayList<>();


    public DisplayTimeTableOutputData(ArrayList<ArrayList<String>[][]> timeTables) {
        this.allTimeTables = timeTables;
    }

    public ArrayList<ArrayList<String>[][]> getAllTimetables() {
        return allTimeTables;
    }

    public void getAllTimeTables (ArrayList<ArrayList<String>[][]> timeTables) {
        this.allTimeTables = timeTables;
    }
}
