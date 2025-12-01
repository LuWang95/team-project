package generator.interfaceadapter.set_preferences;
import generator.interfaceadapter.ViewModel;

public class SetPreferencesViewModel extends ViewModel<SetPreferencesState> {
    public SetPreferencesViewModel() {
        super("Set Preferences");
        setState(new SetPreferencesState());
    }
}
