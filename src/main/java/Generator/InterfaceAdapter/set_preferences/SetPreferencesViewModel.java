package Generator.InterfaceAdapter.set_preferences;
import Generator.InterfaceAdapter.ViewModel;

public class SetPreferencesViewModel extends ViewModel<SetPreferencesState> {
    public SetPreferencesViewModel() {
        super("set preferences");
        setState(new SetPreferencesState());
    }
}
