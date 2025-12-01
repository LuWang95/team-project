package generator.interface_adapter.set_preferences;
import generator.interface_adapter.ViewModel;

public class SetPreferencesViewModel extends ViewModel<SetPreferencesState> {
    public SetPreferencesViewModel() {
        super("Set Preferences");
        setState(new SetPreferencesState());
    }
}
