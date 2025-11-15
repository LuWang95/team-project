package Generator.DataAccess;

import java.util.ArrayList;

public class JsonCourseRecord {
    public String course_code;
    public String course_title;
    public String campus;
    public String session;
    public String department_code;
    public String faculty_code;
    public double credit;
    public String section_code;
    public String component;             // "LEC" / "TUT" / "PRA"
    public ArrayList<String> instructors;
    public ArrayList<JsonMeeting> meetings;
}

