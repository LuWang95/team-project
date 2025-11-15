package Generator.InterfaceAdapter.AddCourse;

import Generator.View.ViewModel;

public class AddCourseViewModel extends ViewModel<AddCourseState> {
    public AddCourseViewModel() {
        super("Add Course");
        setState(new AddCourseState());
    }
}
