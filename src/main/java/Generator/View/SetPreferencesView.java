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
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Enumeration;

public class SetPreferencesView extends JPanel implements ActionListener, PropertyChangeListener {

    private final String viewName = "Set Preferences";
    private final SetPreferencesViewModel setPreferencesViewModel;
    private SetPreferencesController setPreferencesController = null;

    private final JTextField degreeInputField = new JTextField("Enter degree", 15);
    private final JTextField courseInputField = new JTextField("Enter courses",15);
    private final ButtonGroup yearButtons = new ButtonGroup();
    private final JRadioButton[] timeButtons = new JRadioButton[3];

    private final JPanel degreesPanel = new JPanel();
    private final JPanel coursesPanel = new JPanel();

    private final JLabel noCoursesError = new JLabel("");

    private final JButton generate;

    public SetPreferencesView(SetPreferencesViewModel setPreferencesViewModel) {
        this.setPreferencesViewModel = setPreferencesViewModel;
        setPreferencesViewModel.addPropertyChangeListener(this);
        setPreferencesViewModel.getState().setCourses(new ArrayList<>());
        setPreferencesViewModel.getState().setDegrees(new ArrayList<>());
        setPreferencesViewModel.getState().setTimes(new ArrayList<>());

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
        degreesPanel.setLayout(new BoxLayout(degreesPanel, BoxLayout.Y_AXIS));
        degreeInputField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final SetPreferencesState setPreferencesState = setPreferencesViewModel.getState();
                setPreferencesController.addDegree(setPreferencesState.getSelectedDegree());
            }
        });
        final LabelTextPanel degreeInfo = new LabelTextPanel(new JLabel("Degree"), degreeInputField);
        displayDegrees(setPreferencesViewModel.getState().getDegrees());

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
        final JLabel yearTitle = new JLabel("Year of Study");
        yearInfo.add(yearTitle);
        for (int i = 1; i <= 4; i++) {
            JRadioButton radioYear = new JRadioButton(String.valueOf(i));
            yearInfo.add(radioYear);
            yearButtons.add(radioYear);
        }

        // creates the select time preferences section
        final JPanel timeInfo = new JPanel();
        final JLabel timeTitle = new JLabel("Preferred Time");
        final String[] possibleTimes = {"Morning", "Afternoon", "Evening"};
        timeInfo.add(timeTitle);
        for (int i = 0; i < possibleTimes.length; i++) {
            JRadioButton radioTime = new JRadioButton(possibleTimes[i]);
            timeInfo.add(radioTime);
            timeButtons[i] = radioTime;
        }

        // makes sure SetPreferencesState is updated whenever applicable
        addCourseListener();
        addDegreeListener();
        addYearListener();
        addTimeListener();

        // creates the generate timetable button
        generate = new JButton("Generate Time Table");
        generate.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
                        setPreferencesController.displayTimetable();
                    }
                });
        generate.setAlignmentX(JComponent.CENTER_ALIGNMENT);

        // set the layout of the UI
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.add(title);

        leftPanel.add(degreeInfo);
        leftPanel.add(degreesPanel);
        leftPanel.add(courseInfo);
        leftPanel.add(coursesPanel);
        infoPanel.add(leftPanel);

        rightPanel.add(yearInfo);
        rightPanel.add(timeInfo);
        infoPanel.add(rightPanel);

        this.add(infoPanel);
        this.add(generate);
        this.add(noCoursesError);
        noCoursesError.setForeground(Color.RED);

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

    private void addYearListener() {
        for (Enumeration<AbstractButton> e = yearButtons.getElements(); e.hasMoreElements();) {
            JRadioButton yearButton = (JRadioButton) e.nextElement();
            yearButton.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    final SetPreferencesState setPreferencesState = setPreferencesViewModel.getState();
                    setPreferencesState.setYear(Integer.parseInt(yearButton.getText()));
                    setPreferencesViewModel.setState(setPreferencesState);
                }
            });
        }
    }

    private void addTimeListener() {
        for (JRadioButton timeButton: timeButtons) {
            timeButton.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    final SetPreferencesState setPreferencesState = setPreferencesViewModel.getState();
                    String timeOfDay = timeButton.getText();
                    if (timeButton.isSelected())
                        setPreferencesState.getTimes().add(timeOfDay);
                    else
                        setPreferencesState.getTimes().remove(timeOfDay);
                    setPreferencesViewModel.setState(setPreferencesState);
                }
            });
        }
    }

    private void displayCourses(ArrayList<String> coursesSelected) {
        displayCourses(coursesSelected, null);
    }

    // updates coursesPanel when you add a course, triggered by propertyChange(), which is triggered by the Presenter
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

    private void displayDegrees(ArrayList<String> coursesSelected) {
        displayCourses(coursesSelected, null);
    }

    // updates degreesPanel when you add a course, triggered by propertyChange(), which is triggered by the Presenter
    // displays an error message if you try to enter a course you already entered
    private void displayDegrees(ArrayList<String> degreesSelected, String errorMessage) {

        degreesPanel.removeAll();

        if (errorMessage != null) {
            JLabel errorMessageLabel = new JLabel(errorMessage);
            errorMessageLabel.setForeground(Color.RED);
            degreesPanel.add(errorMessageLabel);
        }

        for (String degree: degreesSelected) {
            JPanel degreePanel = new JPanel();
            degreePanel.setLayout(new BoxLayout(degreePanel, BoxLayout.X_AXIS));
            degreePanel.add(new JLabel(degree));

            JButton degreeButton = new JButton("X");
            degreeButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    setPreferencesController.removeDegree(degree);
                }
            });
            degreePanel.add(degreeButton);
            degreesPanel.add(degreePanel);
        }
        degreesPanel.revalidate();
        degreesPanel.repaint();
    }

    // whenever the ViewModel changes, this runs
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        final SetPreferencesState state = (SetPreferencesState) evt.getNewValue();
        displayCourses(state.getCourses(), state.getCourseError());
        displayDegrees(state.getDegrees(), state.getDegreeError());

        if (state.getNoSelectedCoursesError() != null) {
            noCoursesError.setText(state.getNoSelectedCoursesError());
        }
        else {
            noCoursesError.setText("");
        }
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
