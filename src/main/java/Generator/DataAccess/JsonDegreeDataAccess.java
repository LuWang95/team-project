package Generator.DataAccess;

import com.google.gson.Gson;
import CourseInfo.Degree;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class JsonDegreeDataAccess {
    private final Map<String, Degree> degreesByCode = new HashMap<>();

    private static class JsonDegreeRecord {
        public String degreeCode;
        public String degreeName;
        public ArrayList<String> courseCodes;
    }

    public JsonDegreeDataAccess(String jsonFilePath) {
        List<JsonDegreeRecord> rawList = loadRawFromJson(jsonFilePath);
        buildDomainDegrees(rawList);
    }


    private List<JsonDegreeRecord> loadRawFromJson(String jsonFilePath) {
        try (FileReader reader = new FileReader(jsonFilePath)) {
            Gson gson = new Gson();
            JsonDegreeRecord[] arr = gson.fromJson(reader, JsonDegreeRecord[].class);
            if (arr == null) {
                return new ArrayList<>();
            }
            return Arrays.asList(arr);
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
        private void buildDomainDegrees(List<JsonDegreeRecord> rawList) {
            for (JsonDegreeRecord r : rawList) {
                String degree_Code = r.degreeCode;
                Degree degree = degreesByCode.get(degree_Code);
                if (degree == null) {
                    degree = new Degree(
                            degree_Code,
                            r.degreeName,
                            r.courseCodes);
                            for (String course : r.courseCodes) {
                                degree.addCourse(course);
                            }

                    ;
                    degreesByCode.put(degree_Code, degree);
                }

            }}

        public boolean degreeExists(String degreesCode) {
            return degreesByCode.containsKey(degreesCode);
        }

        public Degree getDegreeByCode(String degreeCode) {
            return degreesByCode.get(degreeCode);
        }


    }
