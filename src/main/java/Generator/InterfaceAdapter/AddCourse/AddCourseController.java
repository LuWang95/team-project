package Generator.InterfaceAdapter.AddCourse;

import Generator.UseCases.AddCourse.AddCourseInputBoundary;
import Generator.UseCases.AddCourse.AddCourseInputData;

public class AddCourseController {
    private final AddCourseInputBoundary addCourseInteractor;

    public AddCourseController(AddCourseInputBoundary addCourseInteractor) {
        this.addCourseInteractor = addCourseInteractor;
    }

    public void execute(String courseCode){
        AddCourseInputData inputData = new AddCourseInputData(courseCode);
        addCourseInteractor.execute(inputData);
    }


}
