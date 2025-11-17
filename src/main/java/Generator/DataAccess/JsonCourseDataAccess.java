package Generator.DataAccess;

import Generator.UseCase.generate_timetable.GenerateTimetableDataAccessInterface;
import com.google.gson.Gson;
import CourseInfo.Course;
import CourseInfo.Meeting;
import CourseInfo.LectureSection;
import CourseInfo.TutorialSection;
import CourseInfo.PracticalSection;

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
            String key = r.course_code.trim().toUpperCase();

            Course course = coursesByCode.get(key);
            if (course == null) {
                course = new Course(
                        r.course_code,           // courseCode
                        r.course_title,          // courseTitle
                        Integer.parseInt(r.session),               // sessionCode
                        r.credit,                // credit
                        new ArrayList<>(),       // lecture_sections
                        new ArrayList<>(),       // tutorial_sections
                        new ArrayList<>()        // practical_sections
                );
                coursesByCode.put(key, course);
             }

            switch (r.component) {
                case "LEC":
                    course.getLectureSections().add(toLectureSection(r));
                    break;
                case "TUT":
                    course.getTutorialSections().add(toTutorialSection(r));
                    break;
                case "PRA":
                    course.getPracticalSections().add(toPracticalSection(r));
                    break;
                default:
                    break;
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

    private LectureSection toLectureSection(JsonCourseRecord r) {
        ArrayList<Meeting> list = new ArrayList<>();
        if (r.meetings != null) {
            for (JsonMeeting jm : r.meetings) {
                list.add(toDomainMeeting(r, jm));
            }
        }
        return new LectureSection(r.section_code, list);
    }

    private TutorialSection toTutorialSection(JsonCourseRecord r) {
        ArrayList<Meeting> list = new ArrayList<>();
        if (r.meetings != null) {
            for (JsonMeeting jm : r.meetings) {
                list.add(toDomainMeeting(r, jm));
            }
        }
        return new TutorialSection(r.section_code, list);
    }

    private PracticalSection toPracticalSection(JsonCourseRecord r) {
        ArrayList<Meeting> list = new ArrayList<>();
        if (r.meetings != null) {
            for (JsonMeeting jm : r.meetings) {
                list.add(toDomainMeeting(r, jm));
            }
        }
        return new PracticalSection(r.section_code, list);
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
