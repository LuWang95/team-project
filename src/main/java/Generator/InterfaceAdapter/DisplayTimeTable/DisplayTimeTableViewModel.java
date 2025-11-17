package Generator.InterfaceAdapter.DisplayTimeTable;

import Generator.InterfaceAdapter.ViewModel;

public class DisplayTimeTableViewModel extends ViewModel<DisplayTimeTableState> {
    public DisplayTimeTableViewModel() {
        super("Display Time Table");
        setState(new DisplayTimeTableState());
    }
}
