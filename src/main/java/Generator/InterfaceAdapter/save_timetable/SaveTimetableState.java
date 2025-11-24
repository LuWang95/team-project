package Generator.InterfaceAdapter.save_timetable;

public class SaveTimetableState {

    private String successMessage;
    private String errorMessage;

    public SaveTimetableState() {}

    public String getSuccessMessage() { return successMessage; }

    public void setSuccessMessage(String successMessage) {
        this.successMessage = successMessage;
        this.errorMessage = null;
    }

    public String getErrorMessage() { return errorMessage; }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
        this.successMessage = null;
    }
}
