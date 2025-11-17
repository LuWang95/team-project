package Generator.InterfaceAdapter.AddCourse;

import Generator.InterfaceAdapter.ViewModel;

public class AddCourseViewModel extends ViewModel<AddCourseState> {
    public AddCourseViewModel() {
        super("Add Course");
        setState(new AddCourseState());
    }
}
