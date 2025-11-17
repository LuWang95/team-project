package Generator.InterfaceAdapter.display_timetable;

import Generator.InterfaceAdapter.ViewManagerModel;
import Generator.InterfaceAdapter.set_preferences.SetPreferencesViewModel;
import Generator.UseCase.generate_timetable.GenerateTimetableOutputBoundary;
import Generator.UseCase.generate_timetable.GenerateTimetableOutputData;
import Generator.UseCase.return_to_prefs.ReturnToPrefsOutputBoundary;

public class DisplayTimetablePresenter implements ReturnToPrefsOutputBoundary {
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
}
