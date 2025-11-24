package Generator.InterfaceAdapter.display_timetable;

import Generator.InterfaceAdapter.ViewModel;

public class DisplayTimetableViewModel extends ViewModel<DisplayTimetableState> {
    public DisplayTimetableViewModel() {
        super("Display Timetable");
        setState(new DisplayTimetableState());
    }
}
