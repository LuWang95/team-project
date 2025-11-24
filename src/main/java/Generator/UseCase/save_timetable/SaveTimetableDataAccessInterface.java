package Generator.UseCase.save_timetable;

import Generator.UseCase.generate_timetable.TimetableDTO;

import java.io.IOException;
import java.util.List;

public interface SaveTimetableDataAccessInterface {
    void save(String filePath,
              List<TimetableDTO> fallTimetables,
              List<TimetableDTO> winterTimetables) throws IOException;
}
