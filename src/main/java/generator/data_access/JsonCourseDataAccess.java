package generator.data_access;

import courseinfo.*;
import com.google.gson.Gson;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;


public class JsonCourseDataAccess {
    private final Map<String, Course> coursesByCode = new HashMap<>();


    public JsonCourseDataAccess(String jsonFilePath) {
        List<JsonCourseRecord> rawList = loadRawFromJson(jsonFilePath);
        buildDomainCourses(rawList);
    }

    private List<JsonCourseRecord> loadRawFromJson(String jsonFilePath) {
        try (FileReader reader = new FileReader(jsonFilePath)) {
            Gson gson = new Gson();
            JsonCourseRecord[] arr = gson.fromJson(reader, JsonCourseRecord[].class);
            if (arr == null) {
                return new ArrayList<>();
            }
            return Arrays.asList(arr);
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }


    private void buildDomainCourses(List<JsonCourseRecord> rawList) {
        for (JsonCourseRecord r : rawList) {
            String baseCode = r.course_code.trim().toUpperCase();
            String fullCode = baseCode;
            if (r.course_code.charAt(6) == 'H'){
                if(Integer.parseInt(r.session) == 20259){
                    fullCode = baseCode + "F";
                }else{
                    fullCode = baseCode + "S";
                }
            }


            Course course = coursesByCode.get(fullCode);
            if (course == null) {
                course = new Course(
                        fullCode,
                        r.course_title,          // courseTitle
                        Integer.parseInt(r.session),               // sessionCode
                        r.credit,                // credit
                        new ArrayList<>(),       // lecture_sections
                        new ArrayList<>(),       // tutorial_sections
                        new ArrayList<>()        // practical_sections
                );
                coursesByCode.put(fullCode, course);
            }

            ArrayList<Meeting> meetings = toMeetingList(r);

            Section section;
            try {
                section = SectionFactory.createSection(r.section_code, meetings);
            } catch (IllegalArgumentException e) {
                System.err.println("Skipping record due to invalid section code: "
                        + r.section_code + " (" + e.getMessage() + ")");
                continue;
            }
            if (section instanceof LectureSection) {
                course.getLectureSections().add((LectureSection) section);
            } else if (section instanceof TutorialSection) {
                course.getTutorialSections().add((TutorialSection) section);
            } else if (section instanceof PracticalSection) {
                course.getPracticalSections().add((PracticalSection) section);
            }
        }
    }


    private Meeting toDomainMeeting(JsonCourseRecord r, JsonMeeting jm) {
        String instructorName = "";
        if (r.instructors != null && !r.instructors.isEmpty()) {
            instructorName = r.instructors.get(0);
        }

        int date = jm.day_of_week;

        return new Meeting(
                instructorName,    // instructor
                jm.start,          // startTime
                jm.end,            // endTime
                jm.start_min,      // startMinutes
                jm.end_min,        // endMinutes
                jm.building_code,  // buildingCode
                date               // date
        );
    }

    private ArrayList<Meeting> toMeetingList(JsonCourseRecord r) {
        ArrayList<Meeting> list = new ArrayList<>();
        if (r.meetings != null) {
            for (JsonMeeting jm : r.meetings) {
                list.add(toDomainMeeting(r, jm));
            }
        }
        return list;
    }

    public boolean courseExists(String coursesCode) {
        return coursesByCode.containsKey(coursesCode);
    }

    public Course getCoursebyCode(String coursesCode) {
        return coursesByCode.get(coursesCode);
    }

    private static class JsonCourseRecord {
        public String course_code;
        public String course_title;
        public String campus;
        public String session;
        public String department_code;
        public String faculty_code;
        public double credit;
        public String section_code;
        public String component;               // "LEC" / "TUT" / "PRA"
        public ArrayList<String> instructors;
        public ArrayList<JsonMeeting> meetings;
    }

    private static class JsonMeeting {
        public int day_of_week;
        public String day_abbr;
        public String start;
        public String end;
        public int start_min;
        public int end_min;
        public String building_code;
        public String room;
        public String session_code;
        public String repetition;
    }
}
