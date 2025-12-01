package generator.usecase.sort_timetable;

import generator.usecase.generate_timetable.TimetableDTO;
import java.util.List;

public class SortTimetableOutputData {

    private final List<TimetableDTO> fallTimetables;
    private final List<TimetableDTO> winterTimetables;

    public SortTimetableOutputData(List<TimetableDTO> fallTimetables,
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
