package Generator.UseCases;

import CourseInfo.Course;

import java.util.ArrayList;

public class AddCourseInteractor implements AddCourseInputBoundary {

    private final AddCourseDataAccessInterface addCourseDataAccessObject;
    private final AddCourseOutputBoundary addCoursePresenter;

    public AddCourseInteractor(AddCourseDataAccessInterface dao,
                               AddCourseOutputBoundary presenter) {
        this.addCourseDataAccessObject = dao;
        this.addCoursePresenter = presenter;
    }

    @Override
    public void execute(AddCourseInputData inputData) {
        String code = inputData.getCourseCode().trim().toUpperCase();
        if(code.isEmpty()){
            addCoursePresenter.prepareErrorView("Please enter a valid course code");
            return;
        }
        if(addCourseDataAccessObject.alreadyInList(code)){
            addCoursePresenter.prepareErrorView("This course already exists");
        }
        if(!addCourseDataAccessObject.getCourseByCode(code)){
            addCoursePresenter.prepareErrorView("Course not found");
        }else{
            Course courses = addCourseDataAccessObject.getCourse(code);
            AddCourseOutputData addCourseOutputData = new AddCourseOutputData(courses.getCourseCode(), courses.getCourseTitle(),
                    courses.getLectureSections(),courses.getTutorialSections(),courses.getPracticalSections(),courses.getCredit(),courses.getSessionCode());
            addCoursePresenter.prepareSuccessView(addCourseOutputData);

        }


    }
}

