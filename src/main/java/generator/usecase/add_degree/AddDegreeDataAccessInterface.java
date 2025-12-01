package generator.usecase.add_degree;
import courseinfo.Degree;

public interface AddDegreeDataAccessInterface {

    /**
     * checks if the degree is already selected
     * @param degree the degree to look for
     * @return true if the degree is already selected
     */
    boolean degreeAlreadyAdded(String degree);

    /**
     * adds the degree to the list
     * @param degree the degree to be added
     */

    boolean degreeExists(String degree);

    void add(Degree degree);

    Degree getDegreeByCode(String degreeCode);
}
