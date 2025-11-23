package Generator.View;

import Generator.InterfaceAdapter.display_timetable.DisplayTimetableController;
import Generator.InterfaceAdapter.display_timetable.DisplayTimetableState;
import Generator.InterfaceAdapter.display_timetable.DisplayTimetableViewModel;
import Generator.UseCase.generate_timetable.TimetableDTO;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.*;

public class DisplayTimetableView extends JPanel implements ActionListener, PropertyChangeListener {
    private final String viewName = "Display Timetable";
    private final DisplayTimetableViewModel displayTimetableViewModel;
    private DisplayTimetableController displayTimetableController = null;

    private final HashMap<Point, Color> fallColorMap = new HashMap<>();
    private final HashMap<Point, Integer> fallAlignMap = new HashMap<>();
    private final HashMap<Point, Color> winterColorMap = new HashMap<>();
    private final HashMap<Point, Integer> winterAlignMap = new HashMap<>();

    private final JTable fallTimetable;
    private final JTable winterTimetable;
    private final JPanel timetablesPanel = new JPanel();
    private final JPanel fallPanel = new JPanel();
    private final JPanel winterPanel = new JPanel();
    private final JPanel coursesPanel = new JPanel();
    private final JLabel creditsLabel = new JLabel("Credits: ");

    private final JPanel bottomButtons = new JPanel();
    private final JButton back;
    private final JButton regenerate;

    public DisplayTimetableView(DisplayTimetableViewModel displayTimetableViewModel) {
        this.displayTimetableViewModel = displayTimetableViewModel;
        displayTimetableViewModel.addPropertyChangeListener(this);

        final JLabel title = new JLabel("Your Timetable");
        title.setAlignmentX(JLabel.CENTER);

        String[][] fallTimetableData = new String[12][6];
        String[][] winterTimetableData = new String[12][6];
        for (int i = 0; i < 12; i++) {
            fallTimetableData[i][0] = (i + 9) + ":00";
            winterTimetableData[i][0] = (i + 9) + ":00";
        }

        String[] columnHeaders = {"Time", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
        fallTimetable = new JTable(fallTimetableData, columnHeaders);
        winterTimetable = new JTable(winterTimetableData, columnHeaders);

        setupTimetable(fallTimetable, fallColorMap, fallAlignMap);
        setupTimetable(winterTimetable, winterColorMap, winterAlignMap);

        timetablesPanel.setLayout(new BoxLayout(timetablesPanel, BoxLayout.X_AXIS));

        fallPanel.setLayout(new BoxLayout(fallPanel, BoxLayout.Y_AXIS));
        final JLabel fallTitle = new JLabel("Fall Timetable");
        fallTitle.setAlignmentX(JLabel.CENTER);
        fallPanel.add(fallTitle);
        fallPanel.add(fallTimetable.getTableHeader());
        fallPanel.add(fallTimetable);
        timetablesPanel.add(fallPanel);

        timetablesPanel.add(new JLabel(" "));

        winterPanel.setLayout(new BoxLayout(winterPanel, BoxLayout.Y_AXIS));
        final JLabel winterTitle = new JLabel("Winter Timetable");
        winterTitle.setAlignmentX(JLabel.CENTER);
        winterPanel.add(winterTitle);
        winterPanel.add(winterTimetable.getTableHeader());
        winterPanel.add(winterTimetable);
        timetablesPanel.add(winterPanel);

        coursesPanel.setLayout(new BoxLayout(coursesPanel, BoxLayout.Y_AXIS));
        coursesPanel.add(new JLabel("Courses:"));
        coursesPanel.setAlignmentX(JLabel.CENTER);
        creditsLabel.setAlignmentX(JLabel.CENTER);

        bottomButtons.setLayout(new BoxLayout(bottomButtons, BoxLayout.X_AXIS));
        bottomButtons.setAlignmentX(JLabel.CENTER);

        back = new JButton("Back");
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayTimetableController.returnToPrefs();
            }
        });
        bottomButtons.add(back);

        regenerate = new JButton("Regenerate");
        regenerate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayTimetableController.regenerateTimetable();
            }
        });
        bottomButtons.add(regenerate);

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.add(title);
        this.add(timetablesPanel);
        this.add(coursesPanel);
        this.add(creditsLabel);
        this.add(bottomButtons);
    }

    private void setupTimetable(JTable timetableTable, HashMap<Point, Color> colorMap,
                                HashMap<Point, Integer> alignMap) {

        timetableTable.setDefaultEditor(Object.class, null);
        timetableTable.setShowHorizontalLines(false);
        timetableTable.setRowHeight(32);
        timetableTable.getColumnModel().getColumn(0).setPreferredWidth(15);

        timetableTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                           boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                Point p = new Point(row, column);

                c.setBackground(colorMap.getOrDefault(p, Color.WHITE));
                super.setHorizontalAlignment(alignMap.getOrDefault(p, JLabel.LEFT));
                return c;
            }
        });
    }

    private void displayCourses(TimetableDTO fallTTB, TimetableDTO winterTTB) {
        // reset timetable colours
        for (int i = 0; i < 12; i++) {
            for (int j = 0; j < 6; j++) {
                fallColorMap.put(new Point(i, j), Color.WHITE);
                fallAlignMap.put(new Point(i, j), JLabel.RIGHT);
                winterColorMap.put(new Point(i, j), Color.WHITE);
                winterAlignMap.put(new Point(i, j), JLabel.RIGHT);
                if (j != 0) {
                    this.fallTimetable.setValueAt("", i, j);
                    this.winterTimetable.setValueAt("", i, j);
                }
            }
        }

        ArrayList<String> courses = new ArrayList<>();
        displayTimetable(fallTTB.getTable(), fallTimetable, fallColorMap, fallAlignMap, courses);
        displayTimetable(winterTTB.getTable(), winterTimetable, winterColorMap, winterAlignMap, courses);
    }

    private void displayTimetable(ArrayList<ArrayList<ArrayList<String>>> table, JTable timetableTable,
                                  HashMap<Point, Color> colorMap, HashMap<Point, Integer> alignMap,
                                  ArrayList<String> courses) {

        for (int i = 0; i < table.size(); i++) {
            for (int j = 0; j < table.get(0).size(); j++) {
                if (!table.get(i).get(j).isEmpty()) {
                    String block = table.get(i).get(j).get(0);
                    String courseCode = block.substring(0, 8);
                    String sessionCode = block.substring(8);
                    if (!courses.contains(courseCode)) {
                        courses.add(courseCode);
                    }

                    String timetableString = courseCode + " " + sessionCode;

                    Color sessionColour;
                    if (sessionCode.contains("LEC")) {
                        sessionColour = chooseColour(false, courses.indexOf(courseCode));
                    }
                    else {
                        sessionColour = chooseColour(true, courses.indexOf(courseCode));
                    }
                    colorMap.replace(new Point(j, i + 1), sessionColour);

                    if (j == 0 || !colorMap.get(new Point (j - 1, i + 1)).equals(sessionColour)) {
                        timetableTable.setValueAt(timetableString, j, i + 1);
                    }
                    alignMap.replace(new Point(j, i + 1), JLabel.CENTER);



                }
            }
        }
    }

    private Color chooseColour(boolean lighter, int id) {
        if (lighter) {
            return Color.getHSBColor(0.1f * id, 0.50f, 0.90f);
        }
        return Color.getHSBColor(0.1f * id, 0.75f, 0.90f);
    }

    private void updateCoursesLabel(ArrayList<String> courseTitles, ArrayList<String> courseCodes) {
        coursesPanel.removeAll();
        coursesPanel.add(new JLabel("Courses:"));
        for (int i = 0; i < courseTitles.size(); i++) {
            coursesPanel.add(new JLabel("     " + courseTitles.get(i) + " (" +  courseCodes.get(i) + ")"));
        }
    }

    private void updateCreditsLabel(ArrayList<Double> credits) {
        double total_credits = 0;
        for (Double credit : credits) {
            total_credits += credit;
        }
        creditsLabel.setText("Total Credits: " + total_credits);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("hello");
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        final DisplayTimetableState state = (DisplayTimetableState) evt.getNewValue();
        updateCoursesLabel(state.getCourseNames(), state.getCourses());
        updateCreditsLabel(state.getCredit());
        displayCourses(state.getFallTimetables().get(state.getFallIndex()),
                state.getWinterTimetables().get(state.getWinterIndex()));
    }

    public String getViewName() {
        return viewName;
    }

    public void setDisplayTimetableController(DisplayTimetableController displayTimetableController) {
        this.displayTimetableController = displayTimetableController;
    }
}
