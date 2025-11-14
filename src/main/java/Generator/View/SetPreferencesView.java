// need to make alignments better TODO

package Generator.View;

import Generator.InterfaceAdapter.set_preferences.SetPreferencesController;
import Generator.InterfaceAdapter.set_preferences.SetPreferencesState;
import Generator.InterfaceAdapter.set_preferences.SetPreferencesViewModel;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

public class SetPreferencesView extends JPanel implements ActionListener, PropertyChangeListener {

    private final String viewName = "set preferences";
    private final SetPreferencesViewModel setPreferencesViewModel;
    private SetPreferencesController setPreferencesController = null;

    private final JTextField degreeInputField = new JTextField("Enter degree", 15);
    private final JTextField courseInputField = new JTextField("Enter courses",15);

    private final ArrayList<String> degreesSelected = new ArrayList<>(); // will get deleted soon! TODO

    private final JPanel degreesPanel = new JPanel();
    private final JPanel coursesPanel = new JPanel();

    private final JButton generate;

    public SetPreferencesView(SetPreferencesViewModel setPreferencesViewModel) {
        this.setPreferencesViewModel = setPreferencesViewModel;
        setPreferencesViewModel.addPropertyChangeListener(this);
        setPreferencesViewModel.getState().setCourses(new ArrayList<>());

        final JLabel title = new JLabel("Set Preferences");
        title.setAlignmentX(JComponent.CENTER_ALIGNMENT);

        // separate selecting degrees/courses onto to the left, and year/time preferences on the right
        final JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.X_AXIS));
        final JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        final JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));

        // creates the selecting degrees section, will be modified soon TODO
        degreeInputField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                degreesSelected.add(degreeInputField.getText()); // gets changed soon TODO
            }
        });
        final LabelTextPanel degreeInfo = new LabelTextPanel(new JLabel("Degree"), degreeInputField);

        // creates the selecting courses section
        // courses get added as you press enter in the text field
        // pressing the X button next to the course removes it from the list
        coursesPanel.setLayout(new BoxLayout(coursesPanel, BoxLayout.Y_AXIS));
        courseInputField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final SetPreferencesState setPreferencesState = setPreferencesViewModel.getState();
                setPreferencesController.addCourse(setPreferencesState.getSelectedCourse());
            }
        });
        final LabelTextPanel courseInfo = new LabelTextPanel(new JLabel("Course"), courseInputField);
        displayCourses(setPreferencesViewModel.getState().getCourses());

        // creates the select year section, years select from 1 to 4
        final JPanel yearInfo = new JPanel();
        final ButtonGroup yearGroup = new ButtonGroup();
        final JLabel yearTitle = new JLabel("Year of Study");
        yearInfo.add(yearTitle);
        for (int i = 1; i <= 4; i++) {
            JRadioButton radioYear = new JRadioButton(String.valueOf(i));
            yearInfo.add(radioYear);
            yearGroup.add(radioYear);
        }

        // creates the select time preferences section
        final JPanel timeInfo = new JPanel();
        final JLabel timeTitle = new JLabel("Preferred Time");
        final String[] possibleTimes = {"Morning", "Afternoon", "Evening"};
        timeInfo.add(timeTitle);
        for (String possibleTime : possibleTimes) {
            JRadioButton radioTime = new JRadioButton(possibleTime);
            timeInfo.add(radioTime);
        }

        // makes sure SetPreferencesState is updated whenever applicable
        addCourseListener();
        addDegreeListener();
        // addYearListener();
        // addTimeListener(); not implemented yet! TODO

        // creates the generate timetable button
        generate = new JButton("Generate Time Table");
        generate.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
                        System.out.println("this does not work at the moment :(");
                    }
                });
        generate.setAlignmentX(JComponent.CENTER_ALIGNMENT);

        // set the layout of the UI
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.add(title);

        leftPanel.add(degreeInfo);
        // leftPanel.add(degreePanel); NOT IMPLEMENTED YET!
        leftPanel.add(courseInfo);
        leftPanel.add(coursesPanel);
        infoPanel.add(leftPanel);

        rightPanel.add(yearInfo);
        rightPanel.add(timeInfo);
        infoPanel.add(rightPanel);

        this.add(infoPanel);
        this.add(generate);

//        testing, will be removed as soon as the UI for this part is done
//        JButton updateme = new JButton("update");
//        updateme.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                System.out.println(setPreferencesViewModel.getState());
//            }
//        });
//        this.add(updateme);
    }

    // ensures that the SetPreferencesState is well-updated with what's in the course TextField
    // that's literally all there is to this function
    private void addCourseListener() {
        courseInputField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                documentListenerHelper();
            }
            @Override
            public void removeUpdate(DocumentEvent e) {
                documentListenerHelper();
            }
            @Override
            public void changedUpdate(DocumentEvent e) {
                documentListenerHelper();
            }

            private void documentListenerHelper() {
                final SetPreferencesState setPreferencesState = setPreferencesViewModel.getState();
                setPreferencesState.setSelectedCourse(courseInputField.getText());
                setPreferencesViewModel.setState(setPreferencesState);
            }
        });
    }

    // see addCourseListener() but for degrees
    private void addDegreeListener() {
        degreeInputField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                documentListenerHelper();
            }
            @Override
            public void removeUpdate(DocumentEvent e) {
                documentListenerHelper();
            }
            @Override
            public void changedUpdate(DocumentEvent e) {
                documentListenerHelper();
            }

            private void documentListenerHelper() {
                final SetPreferencesState setPreferencesState = setPreferencesViewModel.getState();
                setPreferencesState.setSelectedDegree(degreeInputField.getText());
                setPreferencesViewModel.setState(setPreferencesState);
            }
        });
    }

    // need to implement addTimeListener() and addYearListener() TODO

    private void displayCourses(ArrayList<String> coursesSelected) {
        displayCourses(coursesSelected, null);
    }

    // updates CoursesPanel when you add a course, triggered by propertyChange(), which is triggered by the Presenter
    // displays an error message if you try to enter a course you already entered
    private void displayCourses(ArrayList<String> coursesSelected, String errorMessage) {

        coursesPanel.removeAll();

        if (errorMessage != null) {
            JLabel errorMessageLabel = new JLabel(errorMessage);
            errorMessageLabel.setForeground(Color.RED);
            coursesPanel.add(errorMessageLabel);
        }

        for (String course: coursesSelected) {
            JPanel coursePanel = new JPanel();
            coursePanel.setLayout(new BoxLayout(coursePanel, BoxLayout.X_AXIS));
            coursePanel.add(new JLabel(course));

            JButton courseButton = new JButton("X");
            courseButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    setPreferencesController.removeCourse(course);
                }
            });
            coursePanel.add(courseButton);
            coursesPanel.add(coursePanel);
        }
        coursesPanel.revalidate();
        coursesPanel.repaint();
    }

    // whenever the ViewModel changes, this runs
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        final SetPreferencesState state = (SetPreferencesState) evt.getNewValue();
        displayCourses(state.getCourses(), state.getCourseError());
    }

    /**
     * @param e the event to be processed
     * I don't actually know when this happens
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        JOptionPane.showMessageDialog(this, "sdfjvcxjvk");
    }

    public String getViewName() {
        return viewName;
    }


    public void setSetPreferencesController(SetPreferencesController setPreferencesController) {
        this.setPreferencesController = setPreferencesController;
    }
}
