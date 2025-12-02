package generator.interface_adapter.display_timetable;

import generator.interface_adapter.ViewManagerModel;
import generator.interface_adapter.set_preferences.SetPreferencesViewModel;
import generator.use_case.regenerate_timetable.RegenerateTimetableOutputBoundary;
import generator.use_case.return_to_prefs.ReturnToPrefsOutputBoundary;

public class DisplayTimetablePresenter implements ReturnToPrefsOutputBoundary, RegenerateTimetableOutputBoundary {
    private final ViewManagerModel viewManagerModel;
    private final DisplayTimetableViewModel displayTimeTableViewModel;
    private final SetPreferencesViewModel setPreferencesViewModel;

    public DisplayTimetablePresenter(ViewManagerModel viewManagerModel,
                                     DisplayTimetableViewModel displayTimeTableViewModel,
                                     SetPreferencesViewModel setPreferencesViewModel) {
        this.viewManagerModel = viewManagerModel;
        this.displayTimeTableViewModel = displayTimeTableViewModel;
        this.setPreferencesViewModel = setPreferencesViewModel;
    }

    @Override
    public void prepareReturnToPrefsSuccessView() {
        viewManagerModel.setState(setPreferencesViewModel.getViewName());
        viewManagerModel.firePropertyChange();
    }

    @Override
    public void prepareRegenerateSuccessView() {
        final DisplayTimetableState displayTimetableState = displayTimeTableViewModel.getState();
        displayTimetableState.changeTimetableIndex();
        displayTimeTableViewModel.firePropertyChange();
    }
}
