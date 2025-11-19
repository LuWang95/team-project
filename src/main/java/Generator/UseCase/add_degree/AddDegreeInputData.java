package Generator.UseCase.add_degree;

public class AddDegreeInputData {

    private final String degree;
    private final int year;

    public AddDegreeInputData(String degree, int year) {
        this.degree = degree;
        this.year = year;
    }

    String getDegree() {
        return degree;
    }

    // ADD THIS GETTER
    int getYear() {
        return year;
    }
}
