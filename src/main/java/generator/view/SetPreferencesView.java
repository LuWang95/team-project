package generator.view;

import generator.dataaccess.FileUserDataAccessObject;
import generator.interfaceadapter.set_preferences.SetPreferencesController;
import generator.interfaceadapter.set_preferences.SetPreferencesState;
import generator.interfaceadapter.set_preferences.SetPreferencesViewModel;

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

        // Set better background color
        this.setBackground(new Color(240, 248, 255)); // Alice blue

        // Set up main panel with better spacing
        this.setLayout(new BorderLayout(10, 10));
        this.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Title Panel - Contains both title and subtitle
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setOpaque(false);
        titlePanel.setBorder(new EmptyBorder(0, 0, 20, 0));

        // Main title - CHANGED: Times New Roman font
        final JLabel title = new JLabel("Timetable Builder");
        title.setFont(new Font("Times New Roman", Font.BOLD, 28));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setForeground(new Color(0, 42, 92)); // UofT blue

        // Subtitle - Times New Roman font, darkish blue color
        final JLabel subtitle = new JLabel("<html><div style='text-align: center;'>" +
                "<span style='font-size: 12px;'>UNIVERSITY OF TORONTO</span><br>" +
                "<span style='font-size: 10px;'>Faculty of Arts & Science</span>" +
                "</div></html>");
        subtitle.setFont(new Font("Times New Roman", Font.PLAIN, 12));
        subtitle.setForeground(new Color(25, 60, 110)); // Darkish blue
        subtitle.setHorizontalAlignment(SwingConstants.CENTER);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitle.setBorder(new EmptyBorder(5, 0, 0, 0));

        titlePanel.add(title);
        titlePanel.add(subtitle);

        // Main content panel
        final JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setOpaque(false);
        contentPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
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
        contentPanel.add(leftPanel, gbc);

        gbc.gridx = 1;
        contentPanel.add(rightPanel, gbc);

        // Generate button - CHANGED: Times New Roman font
        generate = new JButton("Generate Timetable");
        generate.setFont(new Font("Times New Roman", Font.BOLD, 14));
        generate.setBackground(new Color(240, 248, 255));
        generate.setForeground(Color.BLACK);
        generate.setFocusPainted(false);
        generate.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0, 42, 92), 2),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        generate.addActionListener(evt -> setPreferencesController.displayTimetable());

        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(new EmptyBorder(20, 0, 10, 0));
        buttonPanel.add(generate);

        // Error label
        noCoursesError.setForeground(Color.RED);
        noCoursesError.setHorizontalAlignment(SwingConstants.CENTER);
        noCoursesError.setBorder(new EmptyBorder(5, 0, 5, 0));

        // Add all components to main panel
        this.add(titlePanel, BorderLayout.NORTH);
        this.add(contentPanel, BorderLayout.CENTER);

        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.setOpaque(false);
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
        leftPanel.setBackground(Color.WHITE);
        leftPanel.setBorder(BorderFactory.createCompoundBorder(
                new TitledBorder(BorderFactory.createLineBorder(new Color(0, 42, 92), 1),
                        "Academic Information",
                        TitledBorder.DEFAULT_JUSTIFICATION,
                        TitledBorder.DEFAULT_POSITION,
                        new Font("Times New Roman", Font.BOLD, 12), // CHANGED: Times New Roman
                        new Color(0, 42, 92)),
                new EmptyBorder(10, 10, 10, 10)
        ));

        // Degrees section
        JPanel degreeSection = new JPanel(new BorderLayout());
        degreeSection.setOpaque(false);
        degreeSection.setBorder(new EmptyBorder(5, 0, 15, 0));

        JPanel degreeInputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        degreeInputPanel.setOpaque(false);
        degreeInputField.setToolTipText("Enter degree, select year, and press enter");
        degreeInputField.setFont(new Font("Times New Roman", Font.PLAIN, 12)); // CHANGED: Times New Roman
        degreeInputField.addActionListener(e -> {
            final SetPreferencesState setPreferencesState = setPreferencesViewModel.getState();
            setPreferencesController.addDegree(setPreferencesState.getSelectedDegree());
        });

        JButton addDegreeBtn = new JButton("Add");
        addDegreeBtn.setFont(new Font("Times New Roman", Font.PLAIN, 12)); // CHANGED: Times New Roman
        addDegreeBtn.setBackground(new Color(0, 127, 163));
        addDegreeBtn.setForeground(Color.BLACK);
        addDegreeBtn.setFocusPainted(false);
        addDegreeBtn.addActionListener(e -> {
            final SetPreferencesState setPreferencesState = setPreferencesViewModel.getState();
            setPreferencesController.addDegree(setPreferencesState.getSelectedDegree());
        });

        JLabel degreeLabel = new JLabel("Degree:");
        degreeLabel.setFont(new Font("Times New Roman", Font.PLAIN, 12));
        degreeInputPanel.setBackground(new Color(0, 127, 163));// CHANGED: Times New Roman
        degreeInputPanel.add(degreeLabel);
        degreeInputPanel.add(degreeInputField);
        degreeInputPanel.add(addDegreeBtn);

        // DEGREES PANEL APPEARANCE
        degreesPanel.setLayout(new BoxLayout(degreesPanel, BoxLayout.Y_AXIS));
        degreesPanel.setBackground(Color.WHITE);
        degreesPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        degreesPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                "Selected Degrees",
                TitledBorder.DEFAULT_JUSTIFICATION,
                TitledBorder.DEFAULT_POSITION,
                new Font("Times New Roman", Font.PLAIN, 11))); // CHANGED: Times New Roman
        degreesPanel.setPreferredSize(new Dimension(400, 120));

        // Year selection
        JPanel yearPanel = new JPanel();
        yearPanel.setLayout(new BoxLayout(yearPanel, BoxLayout.Y_AXIS));
        yearPanel.setBackground(Color.WHITE);
        yearPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(new Color(180, 180, 180), 1),
                        "Year of Study",
                        TitledBorder.LEFT,
                        TitledBorder.TOP,
                        new Font("Times New Roman", Font.PLAIN, 12)), // CHANGED: Times New Roman
                new EmptyBorder(10, 10, 10, 10)
        ));
        yearPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        yearPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));

        JPanel yearButtonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        yearButtonsPanel.setBackground(Color.WHITE);
        for (int i = 1; i <= 4; i++) {
            JRadioButton radioYear = new JRadioButton(String.valueOf(i));
            radioYear.setBackground(Color.WHITE);
            radioYear.setFont(new Font("Times New Roman", Font.PLAIN, 12)); // CHANGED: Times New Roman
            yearButtonsPanel.add(radioYear);
            yearButtons.add(radioYear);
        }
        yearPanel.add(yearButtonsPanel);

        JScrollPane degreeScroll = new JScrollPane(degreesPanel);
        degreeScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        degreeScroll.setBorder(null);

        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.X_AXIS));
        container.add(degreeInputPanel);
        container.add(yearPanel);
        container.setBackground(Color.WHITE);
        degreeSection.add(container, BorderLayout.CENTER);
        degreeSection.add(degreeScroll, BorderLayout.SOUTH);

        // Courses section
        JPanel courseSection = new JPanel(new BorderLayout());
        courseSection.setOpaque(false);
        courseSection.setBorder(new EmptyBorder(5, 0, 5, 0));

        JPanel courseInputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        courseInputPanel.setOpaque(false);
        courseInputField.setToolTipText("Enter course and press Enter");
        courseInputField.setFont(new Font("Times New Roman", Font.PLAIN, 12)); // CHANGED: Times New Roman
        courseInputField.addActionListener(e -> {
            final SetPreferencesState setPreferencesState = setPreferencesViewModel.getState();
            setPreferencesController.addCourse(setPreferencesState.getSelectedCourse());
        });

        JButton addCourseBtn = new JButton("Add");
        addCourseBtn.setFont(new Font("Times New Roman", Font.PLAIN, 12)); // CHANGED: Times New Roman
        addCourseBtn.setBackground(new Color(0, 127, 163));
        addCourseBtn.setForeground(Color.BLACK);
        addCourseBtn.setFocusPainted(false);
        addCourseBtn.addActionListener(e -> {
            final SetPreferencesState setPreferencesState = setPreferencesViewModel.getState();
            setPreferencesController.addCourse(setPreferencesState.getSelectedCourse());
        });

        JLabel courseLabel = new JLabel("Course:");
        courseLabel.setFont(new Font("Times New Roman", Font.PLAIN, 12)); // CHANGED: Times New Roman
        courseInputPanel.add(courseLabel);
        courseInputPanel.add(courseInputField);
        courseInputPanel.add(addCourseBtn);

        // COURSES PANEL APPEARANCE
        coursesPanel.setLayout(new BoxLayout(coursesPanel, BoxLayout.Y_AXIS));
        coursesPanel.setBackground(Color.WHITE);
        coursesPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        coursesPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                "Selected Courses",
                TitledBorder.DEFAULT_JUSTIFICATION,
                TitledBorder.DEFAULT_POSITION,
                new Font("Times New Roman", Font.PLAIN, 11))); // CHANGED: Times New Roman
        coursesPanel.setPreferredSize(new Dimension(400, 150));

        JScrollPane courseScroll = new JScrollPane(coursesPanel);
        courseScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        courseScroll.setBorder(null);

        courseSection.add(courseInputPanel, BorderLayout.NORTH);
        courseSection.add(courseScroll, BorderLayout.CENTER);

        leftPanel.add(degreeSection);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        leftPanel.add(courseSection);

        return leftPanel;
    }

    private JPanel createRightPanel() {
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setBorder(BorderFactory.createCompoundBorder(
                new TitledBorder(BorderFactory.createLineBorder(new Color(0, 42, 92), 1),
                        "Preferences",
                        TitledBorder.DEFAULT_JUSTIFICATION,
                        TitledBorder.DEFAULT_POSITION,
                        new Font("Times New Roman", Font.BOLD, 12), // CHANGED: Times New Roman
                        new Color(0, 42, 92)),
                new EmptyBorder(10, 10, 10, 10)
        ));

        // Time preference
        JPanel timePanel = new JPanel();
        timePanel.setLayout(new BoxLayout(timePanel, BoxLayout.Y_AXIS));
        timePanel.setBackground(Color.WHITE);
        timePanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(new Color(180, 180, 180), 1),
                        "Preferred Time",
                        TitledBorder.LEFT,
                        TitledBorder.TOP,
                        new Font("Times New Roman", Font.PLAIN, 11)), // CHANGED: Times New Roman
                new EmptyBorder(10, 10, 10, 10)
        ));
        timePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        timePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));

        JPanel timeButtonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        timeButtonsPanel.setBackground(Color.WHITE);
        final String[] possibleTimes = {"Morning", "Afternoon", "Evening"};
        for (int i = 0; i < possibleTimes.length; i++) {
            JRadioButton radioTime = new JRadioButton(possibleTimes[i]);
            radioTime.setBackground(Color.WHITE);
            radioTime.setFont(new Font("Times New Roman", Font.PLAIN, 12)); // CHANGED: Times New Roman
            timeButtonsPanel.add(radioTime);
            timeButtons[i] = radioTime;
        }
        timePanel.add(timeButtonsPanel);

        rightPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        rightPanel.add(timePanel);
        rightPanel.add(Box.createVerticalGlue());

        return rightPanel;
    }

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

    private void addYearListener() {
        for (Enumeration<AbstractButton> e = yearButtons.getElements(); e.hasMoreElements();) {
            JRadioButton yearButton = (JRadioButton) e.nextElement();
            yearButton.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    final SetPreferencesState setPreferencesState = setPreferencesViewModel.getState();
                    setPreferencesState.setYear(Integer.parseInt(yearButton.getText()));
                    setPreferencesViewModel.setState(setPreferencesState);
                    FileUserDataAccessObject.Year=setPreferencesState.getYear();
                }
            });
        }
    }

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

    private void displayCourses(ArrayList<String> coursesSelected, String errorMessage) {
        coursesPanel.removeAll();

        if (errorMessage != null) {
            JLabel errorMessageLabel = new JLabel(errorMessage);
            errorMessageLabel.setForeground(Color.RED);
            errorMessageLabel.setFont(new Font("Times New Roman", Font.PLAIN, 11)); // CHANGED: Times New Roman
            errorMessageLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            coursesPanel.add(errorMessageLabel);
            coursesPanel.add(Box.createVerticalStrut(5));
        }

        for (String course : coursesSelected) {
            JPanel coursePanel = new JPanel(new BorderLayout());
            coursePanel.setBackground(new Color(230, 240, 255));
            coursePanel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(0, 127, 163), 1),
                    new EmptyBorder(4, 8, 4, 8)
            ));
            coursePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
            coursePanel.setAlignmentX(Component.LEFT_ALIGNMENT);

            JLabel courseLabel = new JLabel(course);
            courseLabel.setFont(new Font("Times New Roman", Font.PLAIN, 11)); // CHANGED: Times New Roman
            coursePanel.add(courseLabel, BorderLayout.WEST);

            JButton courseButton = new JButton("✕");
            courseButton.setFont(new Font("Times New Roman", Font.BOLD, 16)); // CHANGED: Times New Roman
            courseButton.setMargin(new Insets(2, 8, 2, 8));
            courseButton.setBackground(new Color(70, 70, 70));
            courseButton.setForeground(Color.WHITE);
            courseButton.setFocusPainted(false);
            courseButton.setBorder(BorderFactory.createLineBorder(new Color(50, 50, 50), 1));
            courseButton.addActionListener(e -> setPreferencesController.removeCourse(course));
            coursePanel.add(courseButton, BorderLayout.EAST);

            coursesPanel.add(coursePanel);
            coursesPanel.add(Box.createVerticalStrut(4));
        }

        coursesPanel.revalidate();
        coursesPanel.repaint();
    }

    private void displayDegrees(ArrayList<String> degreesSelected) {
        displayDegrees(degreesSelected, null);
    }

    private void displayDegrees(ArrayList<String> degreesSelected, String errorMessage) {
        degreesPanel.removeAll();

        if (errorMessage != null) {
            JLabel errorMessageLabel = new JLabel(errorMessage);
            errorMessageLabel.setForeground(Color.RED);
            errorMessageLabel.setFont(new Font("Times New Roman", Font.PLAIN, 11)); // CHANGED: Times New Roman
            errorMessageLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            degreesPanel.add(errorMessageLabel);
            degreesPanel.add(Box.createVerticalStrut(5));
        }

        for (String degree : degreesSelected) {
            JPanel degreePanel = new JPanel(new BorderLayout());
            degreePanel.setBackground(new Color(230, 240, 255));
            degreePanel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(0, 127, 163), 1),
                    new EmptyBorder(4, 8, 4, 8)
            ));
            degreePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
            degreePanel.setAlignmentX(Component.LEFT_ALIGNMENT);

            JLabel degreeLabel = new JLabel(degree);
            degreeLabel.setFont(new Font("Times New Roman", Font.PLAIN, 11)); // CHANGED: Times New Roman
            degreePanel.add(degreeLabel, BorderLayout.WEST);

            JButton degreeButton = new JButton("✕");
            degreeButton.setFont(new Font("Times New Roman", Font.BOLD, 16)); // CHANGED: Times New Roman
            degreeButton.setMargin(new Insets(2, 8, 2, 8));
            degreeButton.setBackground(new Color(70, 70, 70));
            degreeButton.setForeground(Color.WHITE);
            degreeButton.setFocusPainted(false);
            degreeButton.setBorder(BorderFactory.createLineBorder(new Color(50, 50, 50), 1));
            degreeButton.addActionListener(e -> setPreferencesController.removeDegree(degree));
            degreePanel.add(degreeButton, BorderLayout.EAST);

            degreesPanel.add(degreePanel);
            degreesPanel.add(Box.createVerticalStrut(4));
        }

        degreesPanel.revalidate();
        degreesPanel.repaint();
        coursesPanel.revalidate();
        coursesPanel.repaint();
    }

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
