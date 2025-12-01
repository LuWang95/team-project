package Generator.View;

import Generator.DataAccess.FileUserDataAccessObject;
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

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import Generator.InterfaceAdapter.set_preferences.SetPreferencesController;
import Generator.InterfaceAdapter.set_preferences.SetPreferencesState;
import Generator.InterfaceAdapter.set_preferences.SetPreferencesViewModel;

public class SetPreferencesView extends JPanel implements ActionListener, PropertyChangeListener {

    private final String viewName = "Set Preferences";
    private final SetPreferencesViewModel setPreferencesViewModel;
    private SetPreferencesController setPreferencesController = null;

    private final JTextField degreeInputField = new JTextField(15);
    private final JTextField courseInputField = new JTextField(15);
    private final ButtonGroup yearButtons = new ButtonGroup();
    private final ButtonGroup timeButtonGroup = new ButtonGroup();
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

        // Set better background color; alice blue
        this.setBackground(new Color(240, 248, 255));

        // Set up main panel with better spacing
        this.setLayout(new BorderLayout(10, 10));
        this.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Title Panel - Contains both title and subtitle
        final JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setOpaque(false);
        titlePanel.setBorder(new EmptyBorder(0, 0, 20, 0));

        // Main title - CHANGED: Times New Roman font
        final JLabel title = new JLabel("Timetable Builder");
        title.setFont(new Font("Times New Roman", Font.BOLD, 28));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        // UofT blue
        title.setForeground(new Color(0, 42, 92));

        // Subtitle - Times New Roman font, darkish blue color
        final JLabel subtitle = new JLabel("<html><div style='text-align: center;'>"
                + "<span style='font-size: 12px;'>UNIVERSITY OF TORONTO</span><br>"
                + "<span style='font-size: 10px;'>Faculty of Arts & Science</span>"
                + "</div></html>");
        subtitle.setFont(new Font("Times New Roman", Font.PLAIN, 12));
        // Darkish blue
        subtitle.setForeground(new Color(25, 60, 110));
        subtitle.setHorizontalAlignment(SwingConstants.CENTER);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitle.setBorder(new EmptyBorder(5, 0, 0, 0));

        titlePanel.add(title);
        titlePanel.add(subtitle);

        // Main content panel
        final JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setOpaque(false);
        contentPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        final GridBagConstraints gbc = new GridBagConstraints();
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

        generate = new JButton("Generate Timetable");
        generate.setFont(new Font("Times New Roman", Font.BOLD, 14));
        generate.setBackground(new Color(240, 248, 255));
        generate.setForeground(Color.BLACK);
        generate.setFocusPainted(false);
        generate.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0, 42, 92), 2),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        generate.addActionListener(evt -> {
            final SetPreferencesState state = setPreferencesViewModel.getState();
            setPreferencesController.displayTimetable(state.getTimes());
        });

        final JPanel buttonPanel = new JPanel(new GridBagLayout());
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

        final JPanel southPanel = new JPanel(new BorderLayout());
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
        final JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBackground(Color.WHITE);
        leftPanel.setBorder(BorderFactory.createCompoundBorder(
                new TitledBorder(BorderFactory.createLineBorder(new Color(0, 42, 92), 1),
                        "Academic Information",
                        TitledBorder.DEFAULT_JUSTIFICATION,
                        TitledBorder.DEFAULT_POSITION,
                        new Font("Times New Roman", Font.BOLD, 12),
                        new Color(0, 42, 92)),
                new EmptyBorder(10, 10, 10, 10)
        ));

        // Degrees section
        final JPanel degreeSection = new JPanel(new BorderLayout());
        degreeSection.setOpaque(false);
        degreeSection.setBorder(new EmptyBorder(5, 0, 15, 0));

        final JPanel degreeInputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        degreeInputPanel.setOpaque(false);
        degreeInputField.setToolTipText("Enter degree, select year, and press enter");
        degreeInputField.setFont(new Font("Times New Roman", Font.PLAIN, 12));
        degreeInputField.addActionListener(e -> {
            final SetPreferencesState setPreferencesState = setPreferencesViewModel.getState();
            setPreferencesController.addDegree(setPreferencesState.getSelectedDegree());
        });

        final JButton addDegreeBtn = new JButton("Add");
        addDegreeBtn.setFont(new Font("Times New Roman", Font.PLAIN, 12));
        addDegreeBtn.setBackground(new Color(0, 127, 163));
        addDegreeBtn.setForeground(Color.BLACK);
        addDegreeBtn.setFocusPainted(false);
        addDegreeBtn.addActionListener(e -> {
            final SetPreferencesState setPreferencesState = setPreferencesViewModel.getState();
            setPreferencesController.addDegree(setPreferencesState.getSelectedDegree());
        });

        final JLabel degreeLabel = new JLabel("Degree:");
        degreeLabel.setFont(new Font("Times New Roman", Font.PLAIN, 12));
        degreeInputPanel.setBackground(new Color(0, 127, 163));
        degreeInputPanel.add(degreeLabel);
        degreeInputPanel.add(degreeInputField);
        degreeInputPanel.add(addDegreeBtn);

        degreesPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        degreesPanel.setBackground(Color.WHITE);
        degreesPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                "Selected Degrees",
                TitledBorder.DEFAULT_JUSTIFICATION,
                TitledBorder.DEFAULT_POSITION,
                new Font("Times New Roman", Font.PLAIN, 11)));
        degreesPanel.setPreferredSize(new Dimension(400, 120));

        // Year selection
        final JPanel yearPanel = new JPanel();
        yearPanel.setLayout(new BoxLayout(yearPanel, BoxLayout.Y_AXIS));
        yearPanel.setBackground(Color.WHITE);
        yearPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(new Color(180, 180, 180), 1),
                        "Year of Study",
                        TitledBorder.LEFT,
                        TitledBorder.TOP,
                        new Font("Times New Roman", Font.PLAIN, 12)),
                new EmptyBorder(10, 10, 10, 10)
        ));
        yearPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        yearPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));

        final JPanel yearButtonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        yearButtonsPanel.setBackground(Color.WHITE);
        for (int i = 1; i <= 4; i++) {
            JRadioButton radioYear = new JRadioButton(String.valueOf(i));
            radioYear.setBackground(Color.WHITE);
            radioYear.setFont(new Font("Times New Roman", Font.PLAIN, 12)); // CHANGED: Times New Roman
            yearButtonsPanel.add(radioYear);
            yearButtons.add(radioYear);
        }
        yearPanel.add(yearButtonsPanel);

        final JScrollPane degreeScroll = new JScrollPane(degreesPanel);
        degreeScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        degreeScroll.setBorder(null);

        final JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.X_AXIS));
        container.add(degreeInputPanel);
        container.add(yearPanel);
        container.setBackground(Color.WHITE);
        degreeSection.add(container, BorderLayout.CENTER);
        degreeSection.add(degreeScroll, BorderLayout.SOUTH);

        // Courses section
        final JPanel courseSection = new JPanel(new BorderLayout());
        courseSection.setOpaque(false);
        courseSection.setBorder(new EmptyBorder(5, 0, 5, 0));

        final JPanel courseInputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        courseInputPanel.setOpaque(false);
        courseInputField.setToolTipText("Enter course and press Enter");
        courseInputField.setFont(new Font("Times New Roman", Font.PLAIN, 12));
        courseInputField.addActionListener(e -> {
            final SetPreferencesState setPreferencesState = setPreferencesViewModel.getState();
            setPreferencesController.addCourse(setPreferencesState.getSelectedCourse());
        });

        final JButton addCourseBtn = new JButton("Add");
        addCourseBtn.setFont(new Font("Times New Roman", Font.PLAIN, 12));
        addCourseBtn.setBackground(new Color(0, 127, 163));
        addCourseBtn.setForeground(Color.BLACK);
        addCourseBtn.setFocusPainted(false);
        addCourseBtn.addActionListener(e -> {
            final SetPreferencesState setPreferencesState = setPreferencesViewModel.getState();
            setPreferencesController.addCourse(setPreferencesState.getSelectedCourse());
        });

        final JLabel courseLabel = new JLabel("Course:");
        courseLabel.setFont(new Font("Times New Roman", Font.PLAIN, 12));
        courseInputPanel.add(courseLabel);
        courseInputPanel.add(courseInputField);
        courseInputPanel.add(addCourseBtn);

        coursesPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        coursesPanel.setBackground(Color.WHITE);
        coursesPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                "Selected Courses",
                TitledBorder.DEFAULT_JUSTIFICATION,
                TitledBorder.DEFAULT_POSITION,
                new Font("Times New Roman", Font.PLAIN, 11)));
        coursesPanel.setPreferredSize(new Dimension(400, 150));

        final JScrollPane courseScroll = new JScrollPane(coursesPanel);
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
        final JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setBorder(BorderFactory.createCompoundBorder(
                new TitledBorder(BorderFactory.createLineBorder(new Color(0, 42, 92), 1),
                        "Preferences",
                        TitledBorder.DEFAULT_JUSTIFICATION,
                        TitledBorder.DEFAULT_POSITION,
                        new Font("Times New Roman", Font.BOLD, 12),
                        new Color(0, 42, 92)),
                new EmptyBorder(10, 10, 10, 10)
        ));

        // Time preference
        final JPanel timePanel = new JPanel();
        timePanel.setLayout(new BoxLayout(timePanel, BoxLayout.Y_AXIS));
        timePanel.setBackground(Color.WHITE);
        timePanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(new Color(180, 180, 180), 1),
                        "Preferred Time",
                        TitledBorder.LEFT,
                        TitledBorder.TOP,
                        new Font("Times New Roman", Font.PLAIN, 11)),
                new EmptyBorder(10, 10, 10, 10)
        ));
        timePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        timePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));

        final JPanel timeButtonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        timeButtonsPanel.setBackground(Color.WHITE);
        final String[] possibleTimes = {"Morning", "Afternoon", "Evening"};
        for (int i = 0; i < possibleTimes.length; i++) {
            final JRadioButton radioTime = new JRadioButton(possibleTimes[i]);
            radioTime.setBackground(Color.WHITE);
            radioTime.setFont(new Font("Times New Roman", Font.PLAIN, 12));
            timeButtonsPanel.add(radioTime);
            timeButtonGroup.add(radioTime);
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
            final JRadioButton yearButton = (JRadioButton) e.nextElement();
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
     //         setPreferencesState.setSelectedCourse("ECO101H1F");
                setPreferencesViewModel.setState(setPreferencesState);

/*                if (setPreferencesState.getYear()==1){
                    for (String code : setPreferencesState.getDegrees()) {
                        if (code.charAt(3)=='1') {
                            setPreferencesState.setSelectedCourse(code);
                            setPreferencesViewModel.setState(setPreferencesState);
                        }
                    }
                setPreferencesState.setSelectedCourse("ECO101H1F");}
*/
 /*               System.out.println(setPreferencesState.getYear());
                System.out.println("getyear");
                System.out.println(setPreferencesState.getCourses());
                System.out.println("getCourse");
                System.out.println(setPreferencesState.getDegrees());
                System.out.println("getDegree");
*/
            }
        });
    }

    private void addTimeListener() {
        for (JRadioButton timeButton: timeButtons) {
            timeButton.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    final SetPreferencesState setPreferencesState = setPreferencesViewModel.getState();
                    final String timeOfDay = timeButton.getText();
                    if (timeButton.isSelected()) {
                        setPreferencesState.getTimes().clear();
                        setPreferencesState.getTimes().add(timeOfDay);
                    }
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
            final JLabel errorMessageLabel = new JLabel(errorMessage);
            errorMessageLabel.setForeground(Color.RED);
            errorMessageLabel.setFont(new Font("Times New Roman", Font.PLAIN, 11));
            coursesPanel.add(errorMessageLabel);
        }

        for (String course: coursesSelected) {
            final JPanel coursePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 2));
            coursePanel.setBackground(new Color(230, 240, 255));
            coursePanel.setBorder(BorderFactory.createLineBorder(new Color(0, 127, 163), 1));

            final JLabel courseLabel = new JLabel(course);
            courseLabel.setFont(new Font("Times New Roman", Font.PLAIN, 11)); // CHANGED: Times New Roman
            coursePanel.add(courseLabel);

            JButton courseButton = new JButton("✕");
            courseButton.setFont(new Font("Times New Roman", Font.BOLD, 16)); // CHANGED: Times New Roman
            courseButton.setMargin(new Insets(2, 8, 2, 8));
            courseButton.setBackground(new Color(70, 70, 70));
            courseButton.setForeground(Color.WHITE);
            courseButton.setFocusPainted(false);
            courseButton.setBorder(BorderFactory.createLineBorder(new Color(50, 50, 50), 1));
            courseButton.addActionListener(e -> setPreferencesController.removeCourse(course));
            coursePanel.add(courseButton);

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

        if (errorMessage != null) {
            final JLabel errorMessageLabel = new JLabel(errorMessage);
            errorMessageLabel.setForeground(Color.RED);
            errorMessageLabel.setFont(new Font("Times New Roman", Font.PLAIN, 11));
            degreesPanel.add(errorMessageLabel);
        }

        for (String degree: degreesSelected) {
            final JPanel degreePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 2));
            degreePanel.setBackground(new Color(230, 240, 255));
            degreePanel.setBorder(BorderFactory.createLineBorder(new Color(0, 127, 163), 1));

            final JLabel degreeLabel = new JLabel(degree);
            degreeLabel.setFont(new Font("Times New Roman", Font.PLAIN, 11));
            degreePanel.add(degreeLabel);

            final JButton degreeButton = new JButton("✕");
            degreeButton.setFont(new Font("Times New Roman", Font.BOLD, 16));
            degreeButton.setMargin(new Insets(2, 8, 2, 8));
            degreeButton.setBackground(new Color(70, 70, 70));
            degreeButton.setForeground(Color.WHITE);
            degreeButton.setFocusPainted(false);
            degreeButton.setBorder(BorderFactory.createLineBorder(new Color(50, 50, 50), 1));
            degreeButton.addActionListener(e -> setPreferencesController.removeDegree(degree));
            degreePanel.add(degreeButton);

            degreesPanel.add(degreePanel);
        }

        degreesPanel.revalidate();
        degreesPanel.repaint();
  //    displayCourses(degreesSelected);
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
