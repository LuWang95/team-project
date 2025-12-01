package generator.interface_adapter.display_timetable;

import generator.interface_adapter.ViewModel;

public class DisplayTimetableViewModel extends ViewModel<DisplayTimetableState> {
    public DisplayTimetableViewModel() {
        super("Display Timetable");
        setState(new DisplayTimetableState());
    }
}
