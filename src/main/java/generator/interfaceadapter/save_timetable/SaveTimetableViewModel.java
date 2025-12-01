package generator.interfaceadapter.save_timetable;

import generator.interfaceadapter.ViewModel;

public class SaveTimetableViewModel extends ViewModel<SaveTimetableState> {

    public SaveTimetableViewModel() {
        super("Save Timetable");
        this.setState(new SaveTimetableState());
    }
}
