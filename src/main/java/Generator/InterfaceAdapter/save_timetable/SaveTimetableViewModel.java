package Generator.InterfaceAdapter.save_timetable;

import Generator.InterfaceAdapter.ViewModel;

public class SaveTimetableViewModel extends ViewModel<SaveTimetableState> {

    public SaveTimetableViewModel() {
        super("Save Timetable");
        this.setState(new SaveTimetableState());
    }
}
