package Generator.InterfaceAdapter.add_course;
import Generator.InterfaceAdapter.ViewModel;

public class AddCourseViewModel extends ViewModel<AddCourseState> {
    public AddCourseViewModel() {
        super("add course");
        setState(new AddCourseState());
    }
}
