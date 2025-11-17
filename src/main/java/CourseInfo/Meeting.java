package CourseInfo;

public class Meeting {
    String instructor;
    String startTime;
    int date;
    String endTime;
    int startMinutes;
    int endMinutes;
    String buildingCode;

    public Meeting(String instructor, String startTime, String endTime, int startMinutes,
                   int endMinutes, String buildingCode, int date) {
        this.instructor = instructor;
        this.startTime = startTime;
        this.endTime = endTime;
        this.startMinutes = startMinutes;
        this.endMinutes = endMinutes;
        this.buildingCode = buildingCode;
        this.date = date;
    }

    public String getInstructor() {
        return instructor;
    }
    public String getStartTime() {
        return startTime;
    }
    public String getEndTime() {
        return endTime;
    }
    public int getStartMinutes() {
        return startMinutes;
    }
    public int getEndMinutes() {
        return endMinutes;
    }
    public String getBuildingCode() {
        return buildingCode;
    }
    public int getDate() {
        return date;
    }

}
