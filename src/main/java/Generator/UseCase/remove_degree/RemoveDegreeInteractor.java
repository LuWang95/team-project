package Generator.UseCase.remove_degree;

import CourseInfo.Degree;

public class RemoveDegreeInteractor implements RemoveDegreeInputBoundary {
    private final RemoveDegreeDataAccessInterface removeDegreeDataAccessObject;
    private final RemoveDegreeOutputBoundary removeDegreePresenter;

    public RemoveDegreeInteractor(RemoveDegreeDataAccessInterface removeDegreeDataAccessObject,
                                  RemoveDegreeOutputBoundary removeDegreeOutputBoundary) {
        this.removeDegreeDataAccessObject = removeDegreeDataAccessObject;
        this.removeDegreePresenter = removeDegreeOutputBoundary;
    }

    @Override
    public void execute(RemoveDegreeInputData removeDegreeInputData) {
        final Degree degree = new Degree(removeDegreeInputData.getDegree(), null);
        removeDegreeDataAccessObject.remove(degree);

        final RemoveDegreeOutputData removeDegreeOutputData = new RemoveDegreeOutputData(
                removeDegreeInputData.getDegree());
        removeDegreePresenter.prepareRemoveDegreeSuccessView(removeDegreeOutputData);

    }
}
