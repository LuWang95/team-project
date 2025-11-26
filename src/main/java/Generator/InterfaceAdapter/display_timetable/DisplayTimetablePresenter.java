package Generator.InterfaceAdapter.display_timetable;

import Generator.InterfaceAdapter.ViewManagerModel;
import Generator.InterfaceAdapter.set_preferences.SetPreferencesViewModel;
import Generator.UseCase.regenerate_timetable.RegenerateTimetableOutputBoundary;
import Generator.UseCase.return_to_prefs.ReturnToPrefsOutputBoundary;

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
