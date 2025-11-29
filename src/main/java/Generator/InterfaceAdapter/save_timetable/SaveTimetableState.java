package Generator.InterfaceAdapter.save_timetable;

public class SaveTimetableState {
    private String fileName = "";
    private String message = "";
    private boolean success = false;

    public SaveTimetableState() {
    }

    public String getFileName() {
        return fileName;
    }

    public String getMessage() {
        return message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
