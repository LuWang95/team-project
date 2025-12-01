package generator.view;

import generator.dataaccess.FileUserDataAccessObject;
import generator.interfaceadapter.load_timetable.LoadTimetableController;
import generator.interfaceadapter.set_preferences.SetPreferencesController;
import generator.interfaceadapter.set_preferences.SetPreferencesState;
import generator.interfaceadapter.set_preferences.SetPreferencesViewModel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Enumeration;

public class SetPreferencesView extends JPanel implements ActionListener, PropertyChangeListener {

    private final String viewName = "Set Preferences";
    private final SetPreferencesViewModel setPreferencesViewModel;
    private SetPreferencesController setPreferencesController = null;
    private LoadTimetableController loadTimetableController = null;

    // Professional Color Palette
    private static final Color PRIMARY_COLOR = new Color(0, 42, 92);
    private static final Color SECONDARY_COLOR = new Color(0, 127, 163);
    private static final Color ACCENT_COLOR = new Color(232, 75, 56);
    private static final Color BACKGROUND_COLOR = new Color(248, 249, 250);
    private static final Color CARD_COLOR = Color.WHITE;
    private static final Color INPUT_BACKGROUND = new Color(255, 255, 255);
    private static final Color BORDER_COLOR = new Color(218, 220, 224);
    private static final Color TEXT_PRIMARY = new Color(32, 33, 36);
    private static final Color TEXT_SECONDARY = new Color(95, 99, 104);
    private static final Color SUCCESS_COLOR = new Color(52, 168, 83);
    private static final Color ERROR_COLOR = new Color(217, 48, 37);
    private static final Color HOVER_COLOR = new Color(241, 243, 244);
    private static final Color SELECTED_ITEM_BG = new Color(232, 240, 254);

    // Typography
    private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 32);
    private static final Font SUBTITLE_FONT = new Font("Segoe UI", Font.PLAIN, 12);
    private static final Font HEADING_FONT = new Font("Segoe UI", Font.BOLD, 14);
    private static final Font BODY_FONT = new Font("Segoe UI", Font.PLAIN, 13);
    private static final Font LABEL_FONT = new Font("Segoe UI", Font.PLAIN, 12);
    private static final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 13);

    private final JTextField degreeInputField = new JTextField(15);
    private final JTextField courseInputField = new JTextField(15);
    private final ButtonGroup yearButtons = new ButtonGroup();
    private final JRadioButton[] timeButtons = new JRadioButton[3];
    private final JCheckBox sortTimetablesCheckbox = new JCheckBox("Sort timetables by walking distance");

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

        // Main panel styling
        this.setBackground(BACKGROUND_COLOR);
        this.setLayout(new BorderLayout(0, 0));
        this.setBorder(new EmptyBorder(30, 40, 30, 40));

        // Title Panel
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setOpaque(false);
        titlePanel.setBorder(new EmptyBorder(0, 0, 30, 0));

        final JLabel title = new JLabel("Timetable Builder");
        title.setFont(TITLE_FONT);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setForeground(PRIMARY_COLOR);

        final JLabel subtitle = new JLabel("<html><div style='text-align: center;'>" +
                "<span style='font-size: 11px; letter-spacing: 1px;'>UNIVERSITY OF TORONTO</span><br>" +
                "<span style='font-size: 10px;'>Faculty of Arts & Science</span>" +
                "</div></html>");
        subtitle.setFont(SUBTITLE_FONT);
        subtitle.setForeground(TEXT_SECONDARY);
        subtitle.setHorizontalAlignment(SwingConstants.CENTER);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitle.setBorder(new EmptyBorder(8, 0, 0, 0));

        titlePanel.add(title);
        titlePanel.add(subtitle);

        // Main content panel
        final JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 0, 0, 20);

        final JPanel leftPanel = createLeftPanel();
        final JPanel rightPanel = createRightPanel();

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.6;
        gbc.weighty = 1;
        contentPanel.add(leftPanel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.4;
        gbc.insets = new Insets(0, 0, 0, 0);
        contentPanel.add(rightPanel, gbc);

        // Generate button with loading screen
        generate = createPrimaryButton("Generate Timetable");
        generate.addActionListener(evt -> showLoadingAndGenerate());

        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(new EmptyBorder(16, 0, 0, 0));
        buttonPanel.add(generate);

        // Error label
        noCoursesError.setForeground(ERROR_COLOR);
        noCoursesError.setFont(LABEL_FONT);
        noCoursesError.setHorizontalAlignment(SwingConstants.CENTER);
        noCoursesError.setBorder(new EmptyBorder(4, 0, 0, 0));

        // South panel assembly
        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.setOpaque(false);
        southPanel.add(noCoursesError, BorderLayout.NORTH);
        southPanel.add(buttonPanel, BorderLayout.CENTER);

        // Add to main
        this.add(titlePanel, BorderLayout.NORTH);
        this.add(contentPanel, BorderLayout.CENTER);
        this.add(southPanel, BorderLayout.SOUTH);

        // Listeners
        addCourseListener();
        addDegreeListener();
        addYearListener();
        addTimeListener();
        addSortListener();
    }

    private void showLoadingAndGenerate() {
        JDialog loadingDialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Generating Timetable", true);
        JPanel loadingPanel = new JPanel();
        loadingPanel.setLayout(new BoxLayout(loadingPanel, BoxLayout.Y_AXIS));
        loadingPanel.setBorder(new EmptyBorder(40, 50, 40, 50));
        loadingPanel.setBackground(Color.WHITE);

        JProgressBar progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        progressBar.setPreferredSize(new Dimension(300, 25));
        progressBar.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel loadingLabel = new JLabel("⏳ Generating your optimal timetable...");
        loadingLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        loadingLabel.setForeground(PRIMARY_COLOR);
        loadingLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subLabel = new JLabel("This may take a few moments");
        subLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subLabel.setForeground(TEXT_SECONDARY);
        subLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        loadingPanel.add(loadingLabel);
        loadingPanel.add(Box.createVerticalStrut(10));
        loadingPanel.add(subLabel);
        loadingPanel.add(Box.createVerticalStrut(20));
        loadingPanel.add(progressBar);

        loadingDialog.add(loadingPanel);
        loadingDialog.setSize(450, 180);
        loadingDialog.setLocationRelativeTo(this);
        loadingDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        loadingDialog.setUndecorated(true);
        loadingDialog.getRootPane().setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 2));

        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() {
                setPreferencesController.displayTimetable();
                return null;
            }

            @Override
            protected void done() {
                loadingDialog.dispose();
            }
        };

        worker.execute();
        loadingDialog.setVisible(true);
    }

    private JPanel createLeftPanel() {
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBackground(CARD_COLOR);
        leftPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                new EmptyBorder(24, 24, 24, 24)
        ));

        JLabel sectionTitle = new JLabel("Academic Information");
        sectionTitle.setFont(HEADING_FONT);
        sectionTitle.setHorizontalAlignment(SwingConstants.LEFT);
        sectionTitle.setForeground(PRIMARY_COLOR);
        sectionTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        sectionTitle.setBorder(new EmptyBorder(0, 0, 20, 0));

        JPanel degreeSection = new JPanel(new BorderLayout());
        degreeSection.setOpaque(false);
        degreeSection.setBorder(new EmptyBorder(0, 0, 20, 0));

        JLabel degreeLabel = new JLabel("Degree");
        degreeLabel.setFont(LABEL_FONT);
        degreeLabel.setForeground(TEXT_PRIMARY);
        degreeLabel.setBorder(new EmptyBorder(0, 0, 6, 0));

        styleTextField(degreeInputField);
        degreeInputField.addActionListener(e -> {
            final SetPreferencesState setPreferencesState = setPreferencesViewModel.getState();
            setPreferencesController.addDegree(setPreferencesState.getSelectedDegree());
        });

        JButton addDegreeBtn = createSecondaryButton("Add");
        addDegreeBtn.addActionListener(e -> {
            final SetPreferencesState setPreferencesState = setPreferencesViewModel.getState();
            setPreferencesController.addDegree(setPreferencesState.getSelectedDegree());
        });

        JPanel yearPanel = new JPanel();
        yearPanel.setLayout(new BoxLayout(yearPanel, BoxLayout.Y_AXIS));
        yearPanel.setBackground(CARD_COLOR);
        yearPanel.setBorder(new EmptyBorder(0, 16, 0, 0));

        JLabel yearLabel = new JLabel("Year of Study");
        yearLabel.setFont(LABEL_FONT);
        yearLabel.setForeground(TEXT_PRIMARY);
        yearLabel.setBorder(new EmptyBorder(0, 0, 8, 0));
        yearLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel yearButtonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 0));
        yearButtonsPanel.setOpaque(false);
        for (int i = 1; i <= 4; i++) {
            JRadioButton radioYear = createStyledRadioButton(String.valueOf(i));
            yearButtonsPanel.add(radioYear);
            yearButtons.add(radioYear);
        }
        yearPanel.add(yearLabel);
        yearPanel.add(yearButtonsPanel);

        JPanel inputRow = new JPanel();
        inputRow.setLayout(new BoxLayout(inputRow, BoxLayout.X_AXIS));
        inputRow.setOpaque(false);
        inputRow.add(degreeInputField);
        inputRow.add(Box.createRigidArea(new Dimension(12, 0)));
        inputRow.add(addDegreeBtn);
        inputRow.add(Box.createRigidArea(new Dimension(20, 0)));
        inputRow.add(yearPanel);

        JPanel degreeInputContainer = new JPanel();
        degreeInputContainer.setLayout(new BoxLayout(degreeInputContainer, BoxLayout.Y_AXIS));
        degreeInputContainer.setOpaque(false);
        degreeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        inputRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        degreeInputContainer.add(degreeLabel);
        degreeInputContainer.add(inputRow);

        degreesPanel.setLayout(new BoxLayout(degreesPanel, BoxLayout.Y_AXIS));
        degreesPanel.setBackground(CARD_COLOR);
        degreesPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JScrollPane degreeScroll = new JScrollPane(degreesPanel);
        degreeScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        degreeScroll.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                new EmptyBorder(12, 12, 12, 12)
        ));
        degreeScroll.setPreferredSize(new Dimension(0, 100));
        degreeScroll.getVerticalScrollBar().setUnitIncrement(16);
        degreeScroll.setBackground(CARD_COLOR);

        degreeSection.add(degreeInputContainer, BorderLayout.NORTH);
        degreeSection.add(degreeScroll, BorderLayout.CENTER);

        JPanel courseSection = new JPanel(new BorderLayout());
        courseSection.setOpaque(false);

        JLabel courseLabel = new JLabel("Courses");
        courseLabel.setFont(LABEL_FONT);
        courseLabel.setForeground(TEXT_PRIMARY);
        courseLabel.setBorder(new EmptyBorder(0, 0, 6, 0));

        styleTextField(courseInputField);
        courseInputField.addActionListener(e -> {
            final SetPreferencesState setPreferencesState = setPreferencesViewModel.getState();
            setPreferencesController.addCourse(setPreferencesState.getSelectedCourse());
        });

        JButton addCourseBtn = createSecondaryButton("Add");
        addCourseBtn.addActionListener(e -> {
            final SetPreferencesState setPreferencesState = setPreferencesViewModel.getState();
            setPreferencesController.addCourse(setPreferencesState.getSelectedCourse());
        });

        JPanel courseInputRow = new JPanel();
        courseInputRow.setLayout(new BoxLayout(courseInputRow, BoxLayout.X_AXIS));
        courseInputRow.setOpaque(false);
        courseInputRow.add(courseInputField);
        courseInputRow.add(Box.createRigidArea(new Dimension(12, 0)));
        courseInputRow.add(addCourseBtn);
        courseInputRow.add(Box.createHorizontalGlue());

        JPanel courseInputContainer = new JPanel();
        courseInputContainer.setLayout(new BoxLayout(courseInputContainer, BoxLayout.Y_AXIS));
        courseInputContainer.setOpaque(false);
        courseLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        courseInputRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        courseInputContainer.add(courseLabel);
        courseInputContainer.add(courseInputRow);

        coursesPanel.setLayout(new BoxLayout(coursesPanel, BoxLayout.Y_AXIS));
        coursesPanel.setBackground(CARD_COLOR);
        coursesPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JScrollPane courseScroll = new JScrollPane(coursesPanel);
        courseScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        courseScroll.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                new EmptyBorder(12, 12, 12, 12)
        ));
        courseScroll.setPreferredSize(new Dimension(0, 140));
        courseScroll.getVerticalScrollBar().setUnitIncrement(16);
        courseScroll.setBackground(CARD_COLOR);

        courseSection.add(courseInputContainer, BorderLayout.NORTH);
        courseSection.add(courseScroll, BorderLayout.CENTER);

        leftPanel.add(sectionTitle);
        leftPanel.add(degreeSection);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        leftPanel.add(courseSection);

        return leftPanel;
    }

    private JPanel createRightPanel() {
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBackground(CARD_COLOR);
        rightPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                new EmptyBorder(24, 24, 24, 24)
        ));

        JLabel sectionTitle = new JLabel("Preferences");
        sectionTitle.setFont(HEADING_FONT);
        sectionTitle.setForeground(PRIMARY_COLOR);
        sectionTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        sectionTitle.setBorder(new EmptyBorder(0, 0, 20, 0));

        JLabel timeLabel = new JLabel("Preferred Time");
        timeLabel.setFont(LABEL_FONT);
        timeLabel.setForeground(TEXT_PRIMARY);
        timeLabel.setBorder(new EmptyBorder(0, 0, 12, 0));
        timeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel timeButtonsPanel = new JPanel();
        timeButtonsPanel.setLayout(new BoxLayout(timeButtonsPanel, BoxLayout.Y_AXIS));
        timeButtonsPanel.setOpaque(false);
        timeButtonsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        final String[] possibleTimes = {"Morning", "Afternoon", "Evening"};
        for (int i = 0; i < possibleTimes.length; i++) {
            JRadioButton radioTime = createStyledRadioButton(possibleTimes[i]);
            radioTime.setAlignmentX(Component.LEFT_ALIGNMENT);
            radioTime.setBorder(new EmptyBorder(6, 0, 6, 0));
            timeButtonsPanel.add(radioTime);
            timeButtons[i] = radioTime;
        }

        sortTimetablesCheckbox.setFont(BODY_FONT);
        sortTimetablesCheckbox.setForeground(TEXT_PRIMARY);
        sortTimetablesCheckbox.setBackground(CARD_COLOR);
        sortTimetablesCheckbox.setFocusPainted(false);
        sortTimetablesCheckbox.setCursor(new Cursor(Cursor.HAND_CURSOR));
        sortTimetablesCheckbox.setAlignmentX(Component.LEFT_ALIGNMENT);
        sortTimetablesCheckbox.setSelected(false);
        sortTimetablesCheckbox.setBorder(new EmptyBorder(12, 0, 12, 0));
        sortTimetablesCheckbox.setToolTipText("Enable to optimize for minimal walking distance (slower generation)");

        JButton loadButton = createLoadTimetableButton();
        loadButton.setAlignmentX(Component.LEFT_ALIGNMENT);

        rightPanel.add(sectionTitle);
        rightPanel.add(timeLabel);
        rightPanel.add(timeButtonsPanel);
        rightPanel.add(Box.createVerticalStrut(20));
        rightPanel.add(sortTimetablesCheckbox);
        rightPanel.add(Box.createVerticalStrut(20));
        rightPanel.add(loadButton);
        rightPanel.add(Box.createVerticalGlue());

        return rightPanel;
    }

    private JButton createLoadTimetableButton() {
        JButton loadButton = new JButton("📂 Load Saved Timetable");
        loadButton.setFont(BUTTON_FONT);
        loadButton.setForeground(SUCCESS_COLOR);
        loadButton.setBackground(Color.WHITE);
        loadButton.setFocusPainted(false);
        loadButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loadButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(SUCCESS_COLOR, 1),
                new EmptyBorder(11, 20, 11, 20)
        ));
        loadButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        loadButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                loadButton.setBackground(new Color(237, 247, 237));
            }

            public void mouseExited(MouseEvent evt) {
                loadButton.setBackground(Color.WHITE);
            }
        });

        loadButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Load Timetable CSV");
            fileChooser.setFileFilter(new FileNameExtensionFilter("CSV Files", "csv"));

            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                String filePath = fileChooser.getSelectedFile().getAbsolutePath();
                if (loadTimetableController != null) {
                    loadTimetableController.loadTimetable(filePath);
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Load Timetable feature is not available.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        return loadButton;
    }

    private void styleTextField(JTextField textField) {
        textField.setFont(BODY_FONT);
        textField.setForeground(TEXT_PRIMARY);
        textField.setBackground(INPUT_BACKGROUND);
        textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                new EmptyBorder(8, 12, 8, 12)
        ));
        textField.setPreferredSize(new Dimension(200, 36));

        textField.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent evt) {
                textField.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(SECONDARY_COLOR, 2),
                        new EmptyBorder(7, 11, 7, 11)
                ));
            }

            public void focusLost(FocusEvent evt) {
                textField.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(BORDER_COLOR, 1),
                        new EmptyBorder(8, 12, 8, 12)
                ));
            }
        });
    }

    private JRadioButton createStyledRadioButton(String text) {
        JRadioButton button = new JRadioButton(text);
        button.setFont(BODY_FONT);
        button.setForeground(TEXT_PRIMARY);
        button.setBackground(CARD_COLOR);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private JButton createPrimaryButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 15));
        button.setForeground(Color.WHITE);
        button.setBackground(PRIMARY_COLOR);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.setPreferredSize(new Dimension(260, 46));
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0, 29, 64), 1),
                new EmptyBorder(12, 30, 12, 30)
        ));

        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                button.setBackground(new Color(0, 33, 73));
            }

            public void mouseExited(MouseEvent evt) {
                button.setBackground(PRIMARY_COLOR);
            }
        });

        return button;
    }

    private JButton createSecondaryButton(String text) {
        JButton button = new JButton(text);
        button.setFont(BUTTON_FONT);
        button.setForeground(SECONDARY_COLOR);
        button.setBackground(Color.WHITE);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(SECONDARY_COLOR, 1),
                new EmptyBorder(7, 20, 7, 20)
        ));
        button.setPreferredSize(new Dimension(70, 36));

        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                button.setBackground(HOVER_COLOR);
            }

            public void mouseExited(MouseEvent evt) {
                button.setBackground(Color.WHITE);
            }
        });

        return button;
    }

    private JButton createRemoveButton() {
        JButton button = new JButton("✕");
        button.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        button.setForeground(TEXT_SECONDARY);
        button.setBackground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setMargin(new Insets(4, 8, 4, 8));
        button.setPreferredSize(new Dimension(28, 28));

        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                button.setForeground(ERROR_COLOR);
                button.setBackground(new Color(254, 242, 242));
            }

            public void mouseExited(MouseEvent evt) {
                button.setForeground(TEXT_SECONDARY);
                button.setBackground(Color.WHITE);
            }
        });

        return button;
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
        for (Enumeration<AbstractButton> e = yearButtons.getElements(); e.hasMoreElements(); ) {
            JRadioButton yearButton = (JRadioButton) e.nextElement();
            yearButton.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    final SetPreferencesState setPreferencesState = setPreferencesViewModel.getState();
                    setPreferencesState.setYear(Integer.parseInt(yearButton.getText()));
                    setPreferencesViewModel.setState(setPreferencesState);
                    FileUserDataAccessObject.Year = setPreferencesState.getYear();
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
        for (JRadioButton timeButton : timeButtons) {
            timeButton.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    final SetPreferencesState setPreferencesState = setPreferencesViewModel.getState();
                    String timeOfDay = timeButton.getText();
                    if (timeButton.isSelected()) {
                        setPreferencesState.getTimes().add(timeOfDay);
                    } else {
                        setPreferencesState.getTimes().remove(timeOfDay);
                    }
                    setPreferencesViewModel.setState(setPreferencesState);
                }
            });
        }
    }

    private void addSortListener() {
        sortTimetablesCheckbox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                final SetPreferencesState setPreferencesState = setPreferencesViewModel.getState();
                setPreferencesState.setSortEnabled(sortTimetablesCheckbox.isSelected());
                setPreferencesViewModel.setState(setPreferencesState);
            }
        });
    }

    private void displayCourses(ArrayList<String> coursesSelected) {
        displayCourses(coursesSelected, null);
    }

    private void displayCourses(ArrayList<String> coursesSelected, String errorMessage) {
        coursesPanel.removeAll();

        if (errorMessage != null) {
            JLabel errorMessageLabel = new JLabel(errorMessage);
            errorMessageLabel.setForeground(ERROR_COLOR);
            errorMessageLabel.setFont(LABEL_FONT);
            errorMessageLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            errorMessageLabel.setBorder(new EmptyBorder(0, 0, 8, 0));
            coursesPanel.add(errorMessageLabel);
        }

        for (String course : coursesSelected) {
            JPanel coursePanel = new JPanel(new BorderLayout(12, 0));
            coursePanel.setBackground(SELECTED_ITEM_BG);
            coursePanel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(210, 227, 252), 1),
                    new EmptyBorder(10, 14, 10, 14)
            ));
            coursePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
            coursePanel.setAlignmentX(Component.LEFT_ALIGNMENT);

            JLabel courseLabel = new JLabel(course);
            courseLabel.setFont(BODY_FONT);
            courseLabel.setForeground(TEXT_PRIMARY);
            coursePanel.add(courseLabel, BorderLayout.CENTER);

            JButton courseButton = createRemoveButton();
            courseButton.addActionListener(e -> setPreferencesController.removeCourse(course));
            coursePanel.add(courseButton, BorderLayout.EAST);

            coursesPanel.add(coursePanel);
            coursesPanel.add(Box.createVerticalStrut(8));
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
            errorMessageLabel.setForeground(ERROR_COLOR);
            errorMessageLabel.setFont(LABEL_FONT);
            errorMessageLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            errorMessageLabel.setBorder(new EmptyBorder(0, 0, 8, 0));
            degreesPanel.add(errorMessageLabel);
        }

        for (String degree : degreesSelected) {
            JPanel degreePanel = new JPanel(new BorderLayout(12, 0));
            degreePanel.setBackground(SELECTED_ITEM_BG);
            degreePanel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(210, 227, 252), 1),
                    new EmptyBorder(10, 14, 10, 14)
            ));
            degreePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
            degreePanel.setAlignmentX(Component.LEFT_ALIGNMENT);

            JLabel degreeLabel = new JLabel(degree);
            degreeLabel.setFont(BODY_FONT);
            degreeLabel.setForeground(TEXT_PRIMARY);
            degreePanel.add(degreeLabel, BorderLayout.CENTER);

            JButton degreeButton = createRemoveButton();
            degreeButton.addActionListener(e -> setPreferencesController.removeDegree(degree));
            degreePanel.add(degreeButton, BorderLayout.EAST);

            degreesPanel.add(degreePanel);
            degreesPanel.add(Box.createVerticalStrut(8));
        }

        degreesPanel.revalidate();
        degreesPanel.repaint();
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
    }

    public String getViewName() {
        return viewName;
    }

    public void setSetPreferencesController(SetPreferencesController setPreferencesController) {
        this.setPreferencesController = setPreferencesController;
    }

    public void setLoadTimetableController(LoadTimetableController loadTimetableController) {
        this.loadTimetableController = loadTimetableController;
    }
}
