package generator.interfaceadapter.display_timetable;

import generator.interfaceadapter.ViewModel;

public class DisplayTimetableViewModel extends ViewModel<DisplayTimetableState> {
    public DisplayTimetableViewModel() {
        super("Display Timetable");
        setState(new DisplayTimetableState());
    }
}
