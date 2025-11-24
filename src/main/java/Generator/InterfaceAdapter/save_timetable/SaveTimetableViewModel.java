package Generator.InterfaceAdapter.save_timetable;

import Generator.InterfaceAdapter.ViewModel;

public class SaveTimetableViewModel extends ViewModel<SaveTimetableState> {

    public SaveTimetableViewModel() {
        // This name is what ViewManagerModel will use if you ever navigate to it
        super("Save Timetable");
        setState(new SaveTimetableState());
    }
}
