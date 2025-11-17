package Generator.View;

import Generator.InterfaceAdapter.display_timetable.DisplayTimetableController;
import Generator.InterfaceAdapter.display_timetable.DisplayTimetableState;
import Generator.InterfaceAdapter.display_timetable.DisplayTimetableViewModel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

public class DisplayTimetableView extends JPanel implements ActionListener, PropertyChangeListener {
    private final String viewName = "Display Timetable";
    private final DisplayTimetableViewModel displayTimetableViewModel;
    private DisplayTimetableController displayTimetableController = null;

    private final JPanel coursesPanel = new JPanel();
    private final JLabel creditsLabel = new JLabel("Credits: ");

    private final JButton back;

    public DisplayTimetableView(DisplayTimetableViewModel displayTimetableViewModel) {
        this.displayTimetableViewModel = displayTimetableViewModel;
        displayTimetableViewModel.addPropertyChangeListener(this);

        final JLabel title = new JLabel("Timetable");
        title.setAlignmentX(CENTER_ALIGNMENT);

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
        this.add(coursesPanel);
        this.add(creditsLabel);
        this.add(back);
    }

    private void updateCoursesLabel(ArrayList<String> courseTitles, ArrayList<String> courseCodes) {
        coursesPanel.removeAll();
        coursesPanel.add(new JLabel("Courses:"));
        for (int i = 0; i < courseTitles.size(); i++) {
            coursesPanel.add(new JLabel("   " + courseTitles.get(i) + " (" +  courseCodes.get(i) + ")"));
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
    }

    public String getViewName() {
        return viewName;
    }

    public void setDisplayTimetableController(DisplayTimetableController displayTimetableController) {
        this.displayTimetableController = displayTimetableController;
    }
}
