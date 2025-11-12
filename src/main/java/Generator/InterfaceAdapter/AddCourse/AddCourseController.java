package Generator.InterfaceAdapter.AddCourse;

import Generator.UseCases.AddCourseInputData;
import Generator.UseCases.AddCourseInteractor;

public class AddCourseController {
    private final AddCourseInteractor addCourseInteractor;

    public AddCourseController(AddCourseInteractor addCourseInteractor) {
        this.addCourseInteractor = addCourseInteractor;
    }

    public void execute(String courseCode){
        AddCourseInputData inputData = new AddCourseInputData(courseCode);
        addCourseInteractor.execute(inputData);
    }


}
