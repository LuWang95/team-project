package Generator.InterfaceAdapter.save_timetable;

import Generator.UseCase.save_timetable.SaveTimetableInputBoundary;
import Generator.UseCase.save_timetable.SaveTimetableInputData;
import Generator.UseCase.generate_timetable.TimetableDTO;

import java.util.List;

public class SaveTimetableController {

    private final SaveTimetableInputBoundary saveTimetableInteractor;

    public SaveTimetableController(SaveTimetableInputBoundary saveTimetableInteractor) {
        this.saveTimetableInteractor = saveTimetableInteractor;
    }

    public void saveTimetables(List<TimetableDTO> fall,
                               List<TimetableDTO> winter,
                               String filename) {

        SaveTimetableInputData input =
                new SaveTimetableInputData(fall, winter, filename);

        saveTimetableInteractor.execute(input);
    }
}
