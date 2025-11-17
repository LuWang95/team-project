package Generator.View;

import CourseInfo.Meeting;
import CourseInfo.Section; // need to get rid of this !! TODO
import Generator.InterfaceAdapter.display_timetable.DisplayTimetableController;
import Generator.InterfaceAdapter.display_timetable.DisplayTimetableState;
import Generator.InterfaceAdapter.display_timetable.DisplayTimetableViewModel;

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

    private final Map<Point, Color> colorMap = new HashMap<>();
    private final JTable timetableTable;
    private final JPanel coursesPanel = new JPanel();
    private final JLabel creditsLabel = new JLabel("Credits: ");

    private final JButton back;

    public DisplayTimetableView(DisplayTimetableViewModel displayTimetableViewModel) {
        this.displayTimetableViewModel = displayTimetableViewModel;
        displayTimetableViewModel.addPropertyChangeListener(this);

        final JLabel title = new JLabel("Timetable");
        title.setAlignmentX(JComponent.CENTER_ALIGNMENT);

        String[][] timetableData = new String[12][6];
        for (int i = 0; i < 12; i++) {
            timetableData[i][0] = (i + 9) + ":00";
        }
        String[] columnHeaders = {"Time", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
        timetableTable = new JTable(timetableData, columnHeaders);
        timetableTable.setDefaultEditor(Object.class, null);
        timetableTable.setShowHorizontalLines(false);

        timetableTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                           boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                Point p = new Point(row, column);

                c.setBackground(colorMap.getOrDefault(p, Color.WHITE));
                return c;
            }
        });

        coursesPanel.setLayout(new BoxLayout(coursesPanel, BoxLayout.Y_AXIS));
        coursesPanel.add(new JLabel("Courses:"));

        back = new JButton("Back");
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayTimetableController.returnToPrefs();
            }
        });

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.add(title);
        this.add(timetableTable.getTableHeader());
        this.add(timetableTable);
        this.add(coursesPanel);
        this.add(creditsLabel);
        this.add(back);
    }

    private void displayCourses(ArrayList<String> courseCodes,
                                ArrayList<ArrayList<Section>> lectureSections,
                                ArrayList<ArrayList<Section>> tutorialSections,
                                ArrayList<ArrayList<Section>> practicalSections) {

        for (int i = 0; i < courseCodes.size(); i++) {
            String courseCode = courseCodes.get(i);

            if (!lectureSections.get(i).isEmpty()) {
                Section courseLec = lectureSections.get(i).get(0);
                String lectureCode = courseLec.getSectionCode();
                Color lecColour = chooseColour(false);

                ArrayList<Meeting> lecMeetings = courseLec.getMeetings();
                for (Meeting m : lecMeetings) {
                    displayMeetingOnTable(courseCode, lectureCode, m, lecColour);
                }
            }
            if (!tutorialSections.get(i).isEmpty()) {
                Section courseTut = tutorialSections.get(i).get(0);
                String tutorialCode = courseTut.getSectionCode();
                Color tutColour = chooseColour(true);

                ArrayList<Meeting> tutMeetings = courseTut.getMeetings();
                for (Meeting m : tutMeetings) {
                    displayMeetingOnTable(courseCode, tutorialCode, m, tutColour);
                }
            }

            if (!practicalSections.get(i).isEmpty()) {
                Section coursePrac = practicalSections.get(i).get(0);
                String practicalCode = coursePrac.getSectionCode();
                Color pracColour = chooseColour(true);

                ArrayList<Meeting> pracMeetings = coursePrac.getMeetings();
                for (Meeting m : pracMeetings) {
                    displayMeetingOnTable(courseCode, practicalCode, m, pracColour);
                }
            }
        }
    }

    private void displayMeetingOnTable(String courseCode, String sectionCode,
                                       Meeting meeting, Color color) {
        int startMinutes = meeting.getStartMinutes();
        int endMinutes = meeting.getEndMinutes();
        int dayOfWeek = meeting.getDate();

        int rowIndex = (startMinutes / 60) - 9;  // 9am is row 0
        int columnIndex = dayOfWeek + 1;          // +1 to skip time column

        if (rowIndex < 0 || rowIndex >= 12 || columnIndex < 1 || columnIndex > 5) {
            return;
        }

        String displayText = courseCode + " " + sectionCode;
        timetableTable.setValueAt(displayText, rowIndex, columnIndex);
        colorMap.put(new Point(rowIndex, columnIndex), color);

        int durationHours = (endMinutes - startMinutes) / 60;
        for (int j = 1; j < durationHours && (rowIndex + j) < 12; j++) {
            colorMap.put(new Point(rowIndex + j, columnIndex), color);
            timetableTable.setValueAt("", rowIndex + j, columnIndex);
        }
    }


    private Color chooseColour(boolean lighter) {
        float hue = 0.0f;
        if (lighter) {
            while (colorMap.containsValue(Color.getHSBColor(hue, 0.50f, 0.90f))) {
                hue += 0.1f;
            }
            return Color.getHSBColor(hue, 0.50f, 0.90f);
        }
        while (colorMap.containsValue(Color.getHSBColor(hue, 0.75f, 0.90f))) {
            hue += 0.1f;
        }
        return Color.getHSBColor(hue, 0.75f, 0.90f);
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
        displayCourses(state.getCourses(), state.getLectureSections(), state.getTutorialSections(),
                state.getPracticalSections());
    }

    public String getViewName() {
        return viewName;
    }

    public void setDisplayTimetableController(DisplayTimetableController displayTimetableController) {
        this.displayTimetableController = displayTimetableController;
    }
}
