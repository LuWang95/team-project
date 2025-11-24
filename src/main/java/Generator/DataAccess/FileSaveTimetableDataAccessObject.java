package Generator.DataAccess;

import Generator.UseCase.generate_timetable.TimetableDTO;
import Generator.UseCase.save_timetable.SaveTimetableDataAccessInterface;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileSaveTimetableDataAccessObject implements SaveTimetableDataAccessInterface {

    private final File baseDirectory;
    private final Gson gson;

    public FileSaveTimetableDataAccessObject(String baseDirectoryPath) {
        this.baseDirectory = new File(baseDirectoryPath);
        if (!baseDirectory.exists()) {
            //noinspection ResultOfMethodCallIgnored
            baseDirectory.mkdirs();
        }
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    @Override
    public void save(String filePath,
                     List<TimetableDTO> fallTimetables,
                     List<TimetableDTO> winterTimetables) throws IOException {

        File targetFile = resolveTargetFile(filePath);

        Map<String, Object> payload = new HashMap<>();
        payload.put("fall", fallTimetables);
        payload.put("winter", winterTimetables);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(targetFile))) {
            gson.toJson(payload, writer);
        }
    }

    private File resolveTargetFile(String filePath) {
        File file = new File(filePath);
        if (!file.isAbsolute()) {
            file = new File(baseDirectory, filePath);
        }
        String name = file.getName();
        if (!name.contains(".")) {
            file = new File(file.getParentFile(), name + ".json");
        }
        File parent = file.getParentFile();
        if (parent != null && !parent.exists()) {
            //noinspection ResultOfMethodCallIgnored
            parent.mkdirs();
        }
        return file;
    }
}
