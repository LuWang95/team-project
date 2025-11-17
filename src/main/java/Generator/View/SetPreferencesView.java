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
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Enumeration;

public class SetPreferencesView extends JPanel implements ActionListener, PropertyChangeListener {

    private final String viewName = "Set Preferences";
    private final SetPreferencesViewModel setPreferencesViewModel;
    private SetPreferencesController setPreferencesController = null;

    private final JTextField degreeInputField = new JTextField(20);
    private final JTextField courseInputField = new JTextField(20);
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

        // Main title
        final JLabel title = new JLabel("Set Preferences");
        title.setFont(new Font("SansSerif", Font.BOLD, 24));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setBorder(new EmptyBorder(10, 0, 20, 0));

        // Main layout
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBorder(new EmptyBorder(20, 20, 20, 20));
        this.add(title);

        // Content panel with left and right sections
        final JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new GridLayout(1, 2, 20, 0));
        contentPanel.setMaximumSize(new Dimension(900, 500));

        // LEFT PANEL - Degrees and Courses
        final JPanel leftPanel = createLeftPanel();

        // RIGHT PANEL - Year and Time preferences
        final JPanel rightPanel = createRightPanel();

        contentPanel.add(leftPanel);
        contentPanel.add(rightPanel);

        this.add(contentPanel);
        this.add(Box.createRigidArea(new Dimension(0, 20)));

        // Generate button
        generate = new JButton("Generate Time Table");
        generate.setFont(new Font("SansSerif", Font.BOLD, 16));
        generate.setAlignmentX(Component.CENTER_ALIGNMENT);
        generate.setMaximumSize(new Dimension(250, 40));
        generate.addActionListener(evt -> setPreferencesController.displayTimetable());
        this.add(generate);

        // Error label
        noCoursesError.setForeground(Color.RED);
        noCoursesError.setAlignmentX(Component.CENTER_ALIGNMENT);
        noCoursesError.setFont(new Font("SansSerif", Font.PLAIN, 12));
        this.add(Box.createRigidArea(new Dimension(0, 10)));
        this.add(noCoursesError);

        // Add listeners
        addCourseListener();
        addDegreeListener();
        addYearListener();
        addTimeListener();
    }

    private JPanel createLeftPanel() {
        final JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
                new EmptyBorder(15, 15, 15, 15)
        ));

        // Degree section
        JPanel degreeSection = createInputSection("Degree", degreeInputField, degreesPanel);
        degreeInputField.addActionListener(e -> {
            final SetPreferencesState state = setPreferencesViewModel.getState();
            setPreferencesController.addDegree(state.getSelectedDegree());
        });

        // Course section
        JPanel courseSection = createInputSection("Course", courseInputField, coursesPanel);
        courseInputField.addActionListener(e -> {
            final SetPreferencesState state = setPreferencesViewModel.getState();
            setPreferencesController.addCourse(state.getSelectedCourse());
        });

        leftPanel.add(degreeSection);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        leftPanel.add(courseSection);

        return leftPanel;
    }

    private JPanel createInputSection(String labelText, JTextField inputField, JPanel displayPanel) {
        JPanel section = new JPanel();
        section.setLayout(new BoxLayout(section, BoxLayout.Y_AXIS));
        section.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Label and input field
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("SansSerif", Font.BOLD, 14));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);

        inputField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        inputField.setAlignmentX(Component.LEFT_ALIGNMENT);

        section.add(label);
        section.add(Box.createRigidArea(new Dimension(0, 5)));
        section.add(inputField);
        section.add(Box.createRigidArea(new Dimension(0, 10)));

        // Display panel with scroll
        displayPanel.setLayout(new BoxLayout(displayPanel, BoxLayout.Y_AXIS));
        displayPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JScrollPane scrollPane = new JScrollPane(displayPanel);
        scrollPane.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));
        scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        section.add(scrollPane);

        return section;
    }

    private JPanel createRightPanel() {
        final JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
                new EmptyBorder(15, 15, 15, 15)
        ));

        // Year section
        JPanel yearSection = new JPanel();
        yearSection.setLayout(new BoxLayout(yearSection, BoxLayout.Y_AXIS));
        yearSection.setAlignmentX(Component.LEFT_ALIGNMENT);
        yearSection.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                "Year of Study",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("SansSerif", Font.BOLD, 14)
        ));

        JPanel yearButtonsPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        yearButtonsPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        for (int i = 1; i <= 4; i++) {
            JRadioButton radioYear = new JRadioButton("Year " + i);
            radioYear.setActionCommand(String.valueOf(i));
            yearButtonsPanel.add(radioYear);
            yearButtons.add(radioYear);
        }
        yearSection.add(yearButtonsPanel);

        // Time section
        JPanel timeSection = new JPanel();
        timeSection.setLayout(new BoxLayout(timeSection, BoxLayout.Y_AXIS));
        timeSection.setAlignmentX(Component.LEFT_ALIGNMENT);
        timeSection.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                "Preferred Time",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("SansSerif", Font.BOLD, 14)
        ));

        JPanel timeButtonsPanel = new JPanel();
        timeButtonsPanel.setLayout(new BoxLayout(timeButtonsPanel, BoxLayout.Y_AXIS));
        timeButtonsPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        final String[] possibleTimes = {"Morning", "Afternoon", "Evening"};
        for (int i = 0; i < possibleTimes.length; i++) {
            JCheckBox checkTime = new JCheckBox(possibleTimes[i]);
            checkTime.setAlignmentX(Component.LEFT_ALIGNMENT);
            timeButtonsPanel.add(checkTime);
            if (i < possibleTimes.length - 1) {
                timeButtonsPanel.add(Box.createRigidArea(new Dimension(0, 5)));
            }

            // Store reference for listener
            final int index = i;
            checkTime.addItemListener(e -> {
                final SetPreferencesState state = setPreferencesViewModel.getState();
                if (checkTime.isSelected()) {
                    state.getTimes().add(possibleTimes[index]);
                } else {
                    state.getTimes().remove(possibleTimes[index]);
                }
                setPreferencesViewModel.setState(state);
            });
        }
        timeSection.add(timeButtonsPanel);

        rightPanel.add(yearSection);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        rightPanel.add(timeSection);
        rightPanel.add(Box.createVerticalGlue());

        return rightPanel;
    }

    private void addCourseListener() {
        courseInputField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateState();
            }
            @Override
            public void removeUpdate(DocumentEvent e) {
                updateState();
            }
            @Override
            public void changedUpdate(DocumentEvent e) {
                updateState();
            }

            private void updateState() {
                final SetPreferencesState state = setPreferencesViewModel.getState();
                state.setSelectedCourse(courseInputField.getText());
                setPreferencesViewModel.setState(state);
            }
        });
    }

    private void addDegreeListener() {
        degreeInputField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateState();
            }
            @Override
            public void removeUpdate(DocumentEvent e) {
                updateState();
            }
            @Override
            public void changedUpdate(DocumentEvent e) {
                updateState();
            }

            private void updateState() {
                final SetPreferencesState state = setPreferencesViewModel.getState();
                state.setSelectedDegree(degreeInputField.getText());
                setPreferencesViewModel.setState(state);
            }
        });
    }

    private void addYearListener() {
        for (Enumeration<AbstractButton> e = yearButtons.getElements(); e.hasMoreElements();) {
            JRadioButton yearButton = (JRadioButton) e.nextElement();
            yearButton.addItemListener(evt -> {
                if (yearButton.isSelected()) {
                    final SetPreferencesState state = setPreferencesViewModel.getState();
                    state.setYear(Integer.parseInt(yearButton.getActionCommand()));
                    setPreferencesViewModel.setState(state);
                }
            });
        }
    }

    private void addTimeListener() {
        // Removed - now handled in createRightPanel() with checkboxes
    }

    private void displayCourses(ArrayList<String> coursesSelected) {
        displayCourses(coursesSelected, null);
    }

    private void displayCourses(ArrayList<String> coursesSelected, String errorMessage) {
        coursesPanel.removeAll();
        coursesPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        if (errorMessage != null) {
            JLabel errorLabel = new JLabel(errorMessage);
            errorLabel.setForeground(new Color(200, 0, 0));
            errorLabel.setFont(new Font("SansSerif", Font.ITALIC, 12));
            errorLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            errorLabel.setBorder(new EmptyBorder(5, 5, 5, 5));
            coursesPanel.add(errorLabel);
        }

        for (String course : coursesSelected) {
            JPanel coursePanel = createItemPanel(course, () -> setPreferencesController.removeCourse(course));
            coursesPanel.add(coursePanel);
        }

        coursesPanel.revalidate();
        coursesPanel.repaint();
    }

    private void displayDegrees(ArrayList<String> degreesSelected) {
        displayDegrees(degreesSelected, null);
    }

    private void displayDegrees(ArrayList<String> degreesSelected, String errorMessage) {
        degreesPanel.removeAll();
        degreesPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        if (errorMessage != null) {
            JLabel errorLabel = new JLabel(errorMessage);
            errorLabel.setForeground(new Color(200, 0, 0));
            errorLabel.setFont(new Font("SansSerif", Font.ITALIC, 12));
            errorLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            errorLabel.setBorder(new EmptyBorder(5, 5, 5, 5));
            degreesPanel.add(errorLabel);
        }

        for (String degree : degreesSelected) {
            JPanel degreePanel = createItemPanel(degree, () -> setPreferencesController.removeDegree(degree));
            degreesPanel.add(degreePanel);
        }

        degreesPanel.revalidate();
        degreesPanel.repaint();
    }

    private JPanel createItemPanel(String text, Runnable onRemove) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(10, 0));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                new EmptyBorder(5, 10, 5, 10)
        ));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", Font.PLAIN, 13));

        JButton removeButton = new JButton("×");
        removeButton.setFont(new Font("SansSerif", Font.BOLD, 18));
        removeButton.setForeground(Color.RED);
        removeButton.setPreferredSize(new Dimension(30, 25));
        removeButton.setFocusPainted(false);
        removeButton.setBorderPainted(false);
        removeButton.setContentAreaFilled(false);
        removeButton.addActionListener(e -> onRemove.run());

        panel.add(label, BorderLayout.CENTER);
        panel.add(removeButton, BorderLayout.EAST);

        return panel;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        final SetPreferencesState state = (SetPreferencesState) evt.getNewValue();
        displayCourses(state.getCourses(), state.getCourseError());
        displayDegrees(state.getDegrees(), state.getDegreeError());

        if (state.getNoSelectedCoursesError() != null) {
            noCoursesError.setText(state.getNoSelectedCoursesError());
        } else {
            noCoursesError.setText("");
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Not used
    }

    public String getViewName() {
        return viewName;
    }

    public void setSetPreferencesController(SetPreferencesController setPreferencesController) {
        this.setPreferencesController = setPreferencesController;
    }
}
