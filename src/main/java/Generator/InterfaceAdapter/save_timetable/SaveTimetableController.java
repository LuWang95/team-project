package Generator.InterfaceAdapter.save_timetable;

import Generator.InterfaceAdapter.display_timetable.DisplayTimetableState;
import Generator.InterfaceAdapter.display_timetable.DisplayTimetableViewModel;
import Generator.UseCase.generate_timetable.TimetableDTO;
import Generator.UseCase.save_timetable.SaveTimetableInputBoundary;
import Generator.UseCase.save_timetable.SaveTimetableInputData;

public class SaveTimetableController {

    private final SaveTimetableInputBoundary saveTimetableInteractor;
    private final DisplayTimetableViewModel displayTimetableViewModel;

    public SaveTimetableController(SaveTimetableInputBoundary saveTimetableInteractor,
                                   DisplayTimetableViewModel displayTimetableViewModel) {
        this.saveTimetableInteractor = saveTimetableInteractor;
        this.displayTimetableViewModel = displayTimetableViewModel;
    }

    /**
     * Saves the currently displayed fall & winter timetables.
     * You can pass any fileName here from the view.
     */
    public void saveTimetable(String fileName) {
        DisplayTimetableState state = displayTimetableViewModel.getState();

        // Current timetables, based on indices already used in regenerate
        TimetableDTO fall =
                state.getFallTimetables().get(state.getFallIndex());
        TimetableDTO winter =
                state.getWinterTimetables().get(state.getWinterIndex());

        SaveTimetableInputData input =
                new SaveTimetableInputData(fall, winter, fileName);

        saveTimetableInteractor.saveTimetable(input);
    }
}
