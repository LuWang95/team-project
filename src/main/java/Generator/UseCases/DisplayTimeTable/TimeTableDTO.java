package Generator.UseCases.DisplayTimeTable;

import CourseInfo.TimeTable;
import java.util.ArrayList;


/**
 * UI-facing Data Transfer Object for a timetable. (Since we can't use entity that contains entity rules in the output data.
 * Stores a 5×12 grid of course labels using nested lists.
 * It is actually the same as the TimeTable class, whose attribute has Array<String>[][] type.
 * View code should read data from this DTO when rendering the 5×12 schedule grid.
 *   Access pattern:
 *      table.get(day).get(hour) → List<String> of course labels in that slot.
 *      day: 0=Mon, 1=Tue, ..., 4=Fri
 *      hour: 0=9am, 1=10am, ..., 11=9pm  (or whatever mapping your UI uses
 */
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
