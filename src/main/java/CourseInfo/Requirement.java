/**
 * represents one or more courses to be completed for a degree
 */

package CourseInfo;

import java.util.ArrayList;

public class Requirement {
    private final ArrayList<Object> requirements;
    private final boolean isAnd;

    /**
     * Creates a requirement from a string that is often the format on the academic calendar
     * @param str the string representing the requirement, ex. str = CSC110Y1, CSC111H1, ( MAT137Y1/ MAT157Y1)
     */
    public Requirement(String str) {
        this(str, new ArrayList<>());
    }

    public Requirement (String str, ArrayList<Requirement> bracketedRequirements) {

        requirements = new ArrayList<>();
        str = str.replace(" ", "");

        while (str.contains("(")) {
            final int openingBracketIndex = str.indexOf("(");
            final int closingBracketIndex = findClosingBracket(str, openingBracketIndex);
            bracketedRequirements.add(new Requirement(str.substring(openingBracketIndex + 1, closingBracketIndex)));
            str = str.substring(0, openingBracketIndex) + "r" + str.substring(closingBracketIndex + 1);
        }

        if (str.contains(",") || str.contains(";") || str.contains("and")) {
            isAnd = true;
            String[] strArr;
            if (str.contains(",")) {
                strArr = str.split(",");
            }
            else if (str.contains(";")) {
                strArr = str.split(";");
            }
            else { // str.contains("and")
                strArr = str.split("and");
            }
            addAndRequirements(strArr, bracketedRequirements);
        }

        else if (str.contains("/")){
            isAnd = false;
            addOrRequirements(str.split("/"), bracketedRequirements);

        }
        else { // single course
            isAnd = false;
            requirements.add(str);
        }
    }

    /**
     * adds all the requirements in strArr to this requirement
     * @param strArr the requirements
     * @param bracketedRequirements the nested requirements
     */
    private void addAndRequirements(String[] strArr, ArrayList<Requirement> bracketedRequirements) {
        for (String s: strArr) {
            if (s.equals("r")) {
                requirements.add(bracketedRequirements.get(0));
                bracketedRequirements.remove(0);
            }
            else if (s.contains("/")) {
                requirements.add(new Requirement(s, bracketedRequirements));
            }
            else {
                requirements.add(s);
            }
        }
    }

    /**
     * adds all the requirements in strArr to this requirement
     * @param strArr the requirements
     * @param bracketedRequirements the nested requirements
     */
    private void addOrRequirements(String[] strArr, ArrayList<Requirement> bracketedRequirements) {
        for (String s: strArr) {
            if (s.equals("r")) {
                requirements.add(bracketedRequirements.get(0));
                bracketedRequirements.remove(0);
            }
            else {
                requirements.add(s);
            }
        }
    }

    /**
     * Find the corresponding closing bracket to the opening bracket at openingBracketIndex in str.
     * @param str the string to search through
     * @param openingBracketIndex the index of the opening bracket of which to find its corresponding closing bracket
     * @return the index of the corresponding closing bracket
     */
    private int findClosingBracket(String str, int openingBracketIndex) {
        int numOpenBrackets = 1;
        for (int i = openingBracketIndex + 1; i < str.length(); i++) {
            if (str.charAt(i) == '(') {
                numOpenBrackets++;
            }
            else if (str.charAt(i) == ')') {
                numOpenBrackets--;
                if (numOpenBrackets == 0) {
                    return i;
                }
            }
        }
        return -1;
    }

    /**
     * @return a possible list of courses that, when completed, fulfill this requirement
     */
    public ArrayList<String> getCourses() {

        ArrayList<String> courses = new ArrayList<>();
        if (isAnd) {
            for (Object requirement: requirements) {
                if (requirement instanceof String) {
                    courses.add((String) requirement);
                }
                else {
                    assert requirement instanceof Requirement;
                    courses.addAll(((Requirement) requirement).getCourses());
                }
            }
        }
        else {
            int choice_index = (int) (Math.random() * requirements.size());
            Object requirement = requirements.get(choice_index);
            if (requirement instanceof String) {
                courses.add((String) requirement);
            }
            else {
                assert requirement instanceof Requirement;
                courses.addAll(((Requirement) requirement).getCourses());
            }
        }
        return courses;
    }

    /**
     * @return a string representation of this requirement, almost identical to the format it was constructed with
     */
    public String toString() {
        StringBuilder str = new StringBuilder();
        for (Object requirement: requirements) {
            if (requirement instanceof String) {
                str.append((String) requirement);
            }
            else {
                assert requirement instanceof Requirement;
                str.append("( ").append(requirement).append(")");
            }
            if (isAnd) {
                str.append(", ");
            }
            else {
                str.append("/ ");
            }
        }
        return str.substring(0, str.length() - 2);
    }

    /**
     * Checks if the list of courses fulfill this requirement.
     * @param courses the list of courses
     * @return true if the courses fulfill the requirement, false otherwise
     */
    public boolean fulfillsRequirement(ArrayList<String> courses) {
        for (Object r : requirements) {
            if (r instanceof String) {
                if (!courses.contains(r) && isAnd) {
                    return false;
                }
                else if (courses.contains(r) && !isAnd) {
                    return true;
                }
            } else {
                assert r instanceof Requirement;
                boolean reqFulfilled = ((Requirement) r).fulfillsRequirement(courses);
                if (!reqFulfilled && isAnd) {
                    return false;
                }
                else if (reqFulfilled && !isAnd) {
                    return true;
                }
            }
        }
        return isAnd;
    }
}
