package Generator.UseCase.save_timetable;

import Generator.UseCase.generate_timetable.TimetableDTO;

import java.util.List;

public class SaveTimetableInputData {

    private final List<TimetableDTO> fallTimetables;
    private final List<TimetableDTO> winterTimetables;
    private final String filePath;

    public SaveTimetableInputData(List<TimetableDTO> fallTimetables,
                                  List<TimetableDTO> winterTimetables,
                                  String filePath) {
        this.fallTimetables = fallTimetables;
        this.winterTimetables = winterTimetables;
        this.filePath = filePath;
    }

    public List<TimetableDTO> getFallTimetables() {
        return fallTimetables;
    }

    public List<TimetableDTO> getWinterTimetables() {
        return winterTimetables;
    }

    public String getFilePath() {
        return filePath;
    }
}
