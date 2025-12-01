package generator.interfaceadapter.save_timetable;

import generator.interfaceadapter.display_timetable.DisplayTimetableState;
import generator.interfaceadapter.display_timetable.DisplayTimetableViewModel;
import generator.usecase.generate_timetable.TimetableDTO;
import generator.usecase.save_timetable.SaveTimetableInputBoundary;
import generator.usecase.save_timetable.SaveTimetableInputData;

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
