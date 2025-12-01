package Generator.DataAccess;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import CourseInfo.Degree;
import com.google.gson.Gson;

public class JsonDegreeDataAccess {
    private final Map<String, Degree> degreesByCode = new HashMap<>();

    private final class JsonDegreeRecord {
        private String degreeCode;
        private String degreeName;
        private ArrayList<String> courseCodes;

        public String getDegreeCode() {
            return degreeCode;
        }

        public String getDegreeName() {
            return degreeName;
        }

        public ArrayList<String> getCourseCodes() {
            return courseCodes;
        }
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
        }
        catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private void buildDomainDegrees(List<JsonDegreeRecord> rawList) {
        for (JsonDegreeRecord r : rawList) {
            final String degreeCode = r.getDegreeCode();
            Degree degree = degreesByCode.get(degreeCode);
            if (degree == null) {
                degree = new Degree(
                        degreeCode,
                        r.getDegreeName(),
                        r.getCourseCodes());
                for (String course : r.getCourseCodes()) {
                    degree.addCourse(course);
                }
                degreesByCode.put(degreeCode, degree);
            }

        }
    }

    /** Make sure the degree exists. */
    public boolean degreeExists(String degreeCode) {
        return degreesByCode.containsKey(degreeCode);
    }

    /** Find the degree using the degree code. */
    public Degree getDegreeByCode(String degreeCode) {
        return degreesByCode.get(degreeCode);
    }

}
