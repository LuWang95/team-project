package generator.interface_adapter.save_timetable;

import generator.interface_adapter.ViewModel;

public class SaveTimetableViewModel extends ViewModel<SaveTimetableState> {

    public SaveTimetableViewModel() {
        super("Save Timetable");
        this.setState(new SaveTimetableState());
    }
}
