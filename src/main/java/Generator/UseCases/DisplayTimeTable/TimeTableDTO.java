package Generator.UseCases.DisplayTimeTable;

import CourseInfo.TimeTable;
import java.util.ArrayList;

public class TimeTableDTO {
    private final ArrayList<ArrayList<ArrayList<String>>> table;

    public TimeTableDTO(ArrayList<ArrayList<ArrayList<String>>> table) {
        this.table = table;
    }

    public ArrayList<ArrayList<ArrayList<String>>> getTable() {
        return table;
    }

    public static TimeTableDTO fromEntity(TimeTable timeTable) {
        ArrayList<String>[][] raw = timeTable.getTimeTable();

        ArrayList<ArrayList<ArrayList<String>>> table = new ArrayList<>();
        for (int day = 0; day < 5; day++) {
            ArrayList<ArrayList<String>> dayList = new ArrayList<>();
            for (int hour = 0; hour < 12; hour++) {
                dayList.add(new ArrayList<>(raw[day][hour]));
            }
            table.add(dayList);
        }
        return new TimeTableDTO(table);
    }
}
