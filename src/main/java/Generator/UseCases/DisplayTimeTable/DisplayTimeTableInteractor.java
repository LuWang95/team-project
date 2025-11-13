package Generator.UseCases.DisplayTimeTable;

public class DisplayTimeTableInteractor implements DisplayTimeTableInputBoundary {
    DisplayTimeTableOutputBoundary displayTimeTableOutputBoundary;

    public DisplayTimeTableInteractor(DisplayTimeTableOutputBoundary displayTimeTableOutputBoundary) {
        this.displayTimeTableOutputBoundary = displayTimeTableOutputBoundary;
    }

    public void execute(DisplayTimeTableInputData inputData) {
        if(inputData.getCourses() != null){
            displayTimeTableOutputBoundary.prepareErrorView("Please add a course");
        }else{
            displayTimeTableOutputBoundary.prepareSuccessView(inputData);
        }


    }


}
