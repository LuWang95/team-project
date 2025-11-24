package Generator.View;

import Generator.InterfaceAdapter.set_preferences.SetPreferencesController;
import Generator.InterfaceAdapter.set_preferences.SetPreferencesState;
import Generator.InterfaceAdapter.set_preferences.SetPreferencesViewModel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
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

    private final JTextField degreeInputField = new JTextField(15);
    private final JTextField courseInputField = new JTextField(15);
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

        // Set up main panel with better spacing
        this.setLayout(new BorderLayout(10, 10));
        this.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Title - CHANGED FROM "Set Preferences" TO "Timetable Builder"
        final JLabel title = new JLabel("Timetable Builder");
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setBorder(new EmptyBorder(0, 0, 20, 0));

        // Main content panel
        final JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Left panel - Degrees and Courses
        final JPanel leftPanel = createLeftPanel();
        
        // Right panel - Year and Time preferences
        final JPanel rightPanel = createRightPanel();

        // Add panels to content
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.5;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        contentPanel.add(leftPanel, gbc);

        gbc.gridx = 1;
        contentPanel.add(rightPanel, gbc);

        // Generate button - CHANGED TO WHITE BACKGROUND WITH BLACK TEXT
        generate = new JButton("Generate Timetable");
        generate.setFont(new Font("Arial", Font.BOLD, 14));
        generate.setBackground(Color.WHITE);  // Changed to white
        generate.setForeground(Color.BLACK);  // Changed to black
        generate.setFocusPainted(false);
        generate.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY, 1),  // Simpler border
            BorderFactory.createEmptyBorder(8, 16, 8, 16)
        ));
        generate.addActionListener(evt -> setPreferencesController.displayTimetable());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBorder(new EmptyBorder(20, 0, 10, 0));
        buttonPanel.add(generate);

        // Error label
        noCoursesError.setForeground(Color.RED);
        noCoursesError.setHorizontalAlignment(SwingConstants.CENTER);
        noCoursesError.setBorder(new EmptyBorder(5, 0, 5, 0));

        // Add all components to main panel
        this.add(title, BorderLayout.NORTH);
        this.add(contentPanel, BorderLayout.CENTER);
        
        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.add(noCoursesError, BorderLayout.CENTER);
        southPanel.add(buttonPanel, BorderLayout.SOUTH);
        this.add(southPanel, BorderLayout.SOUTH);

        // Add listeners
        addCourseListener();
        addDegreeListener();
        addYearListener();
        addTimeListener();
    }

    private JPanel createLeftPanel() {
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBorder(BorderFactory.createCompoundBorder(
            new TitledBorder("Academic Information"),
            new EmptyBorder(10, 10, 10, 10)
        ));

        // Degrees section
        JPanel degreeSection = new JPanel(new BorderLayout());
        degreeSection.setBorder(new EmptyBorder(5, 0, 15, 0));
        
        JPanel degreeInputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        degreeInputField.setToolTipText("Enter degree and press Enter");
        degreeInputField.addActionListener(e -> {
            final SetPreferencesState setPreferencesState = setPreferencesViewModel.getState();
            setPreferencesController.addDegree(setPreferencesState.getSelectedDegree());
        });
        
        JButton addDegreeBtn = new JButton("Add");
        addDegreeBtn.addActionListener(e -> {
            final SetPreferencesState setPreferencesState = setPreferencesViewModel.getState();
            setPreferencesController.addDegree(setPreferencesState.getSelectedDegree());
        });

        degreeInputPanel.add(new JLabel("Degree:"));
        degreeInputPanel.add(degreeInputField);
        degreeInputPanel.add(addDegreeBtn);

        degreesPanel.setLayout(new BoxLayout(degreesPanel, BoxLayout.Y_AXIS));
        degreesPanel.setBorder(BorderFactory.createTitledBorder("Selected Degrees"));

        degreeSection.add(degreeInputPanel, BorderLayout.NORTH);
        degreeSection.add(degreesPanel, BorderLayout.CENTER);

        // Courses section
        JPanel courseSection = new JPanel(new BorderLayout());
        courseSection.setBorder(new EmptyBorder(5, 0, 5, 0));
        
        JPanel courseInputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        courseInputField.setToolTipText("Enter course and press Enter");
        courseInputField.addActionListener(e -> {
            final SetPreferencesState setPreferencesState = setPreferencesViewModel.getState();
            setPreferencesController.addCourse(setPreferencesState.getSelectedCourse());
        });
        
        JButton addCourseBtn = new JButton("Add");
        addCourseBtn.addActionListener(e -> {
            final SetPreferencesState setPreferencesState = setPreferencesViewModel.getState();
            setPreferencesController.addCourse(setPreferencesState.getSelectedCourse());
        });

        courseInputPanel.add(new JLabel("Course:"));
        courseInputPanel.add(courseInputField);
        courseInputPanel.add(addCourseBtn);

        coursesPanel.setLayout(new BoxLayout(coursesPanel, BoxLayout.Y_AXIS));
        coursesPanel.setBorder(BorderFactory.createTitledBorder("Selected Courses"));

        courseSection.add(courseInputPanel, BorderLayout.NORTH);
        courseSection.add(coursesPanel, BorderLayout.CENTER);

        leftPanel.add(degreeSection);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        leftPanel.add(courseSection);

        return leftPanel;
    }

    private JPanel createRightPanel() {
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBorder(BorderFactory.createCompoundBorder(
            new TitledBorder("Preferences"),
            new EmptyBorder(10, 10, 10, 10)
        ));

        // Year selection
        JPanel yearPanel = new JPanel();
        yearPanel.setLayout(new BoxLayout(yearPanel, BoxLayout.Y_AXIS));
        yearPanel.setBorder(BorderFactory.createTitledBorder("Year of Study"));
        yearPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel yearButtonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        for (int i = 1; i <= 4; i++) {
            JRadioButton radioYear = new JRadioButton(String.valueOf(i));
            yearButtonsPanel.add(radioYear);
            yearButtons.add(radioYear);
        }
        yearPanel.add(yearButtonsPanel);

        // Time preference
        JPanel timePanel = new JPanel();
        timePanel.setLayout(new BoxLayout(timePanel, BoxLayout.Y_AXIS));
        timePanel.setBorder(BorderFactory.createTitledBorder("Preferred Time"));
        timePanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel timeButtonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        final String[] possibleTimes = {"Morning", "Afternoon", "Evening"};
        for (int i = 0; i < possibleTimes.length; i++) {
            JRadioButton radioTime = new JRadioButton(possibleTimes[i]);
            timeButtonsPanel.add(radioTime);
            timeButtons[i] = radioTime;
        }
        timePanel.add(timeButtonsPanel);

        rightPanel.add(yearPanel);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        rightPanel.add(timePanel);

        return rightPanel;
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
            errorMessageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            coursesPanel.add(errorMessageLabel);
            coursesPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        }

        for (String course: coursesSelected) {
            JPanel coursePanel = new JPanel();
            coursePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
            coursePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
            coursePanel.add(new JLabel(course));

            JButton courseButton = new JButton("Remove");
            courseButton.setMargin(new Insets(2, 8, 2, 8));
            courseButton.addActionListener(e -> setPreferencesController.removeCourse(course));
            coursePanel.add(courseButton);
            coursesPanel.add(coursePanel);
        }
        
        coursesPanel.add(Box.createVerticalGlue());
        coursesPanel.revalidate();
        coursesPanel.repaint();
    }

    private void displayDegrees(ArrayList<String> degreesSelected) {
        displayDegrees(degreesSelected, null);
    }

    // updates degreesPanel when you add a course, triggered by propertyChange(), which is triggered by the Presenter
    // displays an error message if you try to enter a course you already entered
    private void displayDegrees(ArrayList<String> degreesSelected, String errorMessage) {
        degreesPanel.removeAll();

        if (errorMessage != null) {
            JLabel errorMessageLabel = new JLabel(errorMessage);
            errorMessageLabel.setForeground(Color.RED);
            errorMessageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            degreesPanel.add(errorMessageLabel);
            degreesPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        }

        for (String degree: degreesSelected) {
            JPanel degreePanel = new JPanel();
            degreePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
            degreePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
            degreePanel.add(new JLabel(degree));

            JButton degreeButton = new JButton("Remove");
            degreeButton.setMargin(new Insets(2, 8, 2, 8));
            degreeButton.addActionListener(e -> setPreferencesController.removeDegree(degree));
            degreePanel.add(degreeButton);
            degreesPanel.add(degreePanel);
        }
        
        degreesPanel.add(Box.createVerticalGlue());
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
