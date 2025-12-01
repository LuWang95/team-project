package generator.use_case.sort_timetable;

import generator.use_case.generate_timetable.TimetableDTO;
import java.util.List;

public class SortTimetableInputData {

    private final List<TimetableDTO> fallTimetables;
    private final List<TimetableDTO> winterTimetables;

    public SortTimetableInputData(List<TimetableDTO> fallTimetables,
                                  List<TimetableDTO> winterTimetables) {
        this.fallTimetables = fallTimetables;
        this.winterTimetables = winterTimetables;
    }

    public List<TimetableDTO> getFallTimetables() {
        return fallTimetables;
    }

    public List<TimetableDTO> getWinterTimetables() {
        return winterTimetables;
    }
}
