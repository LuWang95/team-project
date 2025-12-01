package generator.view;

import generator.interface_adapter.display_timetable.DisplayTimetableController;
import generator.interface_adapter.display_timetable.DisplayTimetableState;
import generator.interface_adapter.display_timetable.DisplayTimetableViewModel;
import generator.interface_adapter.save_timetable.SaveTimetableController;
import generator.use_case.generate_timetable.TimetableDTO;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class DisplayTimetableView extends JPanel implements ActionListener, PropertyChangeListener {

    private final String viewName = "Display Timetable";
    private final DisplayTimetableViewModel displayTimetableViewModel;
    private DisplayTimetableController displayTimetableController;

    // Colour palette
    private static final Color PRIMARY_COLOR = new Color(0, 42, 92);
    private static final Color SECONDARY_COLOR = new Color(0, 127, 163);
    private static final Color BACKGROUND_COLOR = new Color(248, 249, 250);
    private static final Color CARD_COLOR = Color.WHITE;
    private static final Color BORDER_COLOR = new Color(218, 220, 224);
    private static final Color TEXT_PRIMARY = new Color(32, 33, 36);
    private static final Color TEXT_SECONDARY = new Color(95, 99, 104);
    private static final Color HOVER_COLOR = new Color(241, 243, 244);
    private static final Color TABLE_HEADER_BG = new Color(245, 247, 250);
    private static final Color TABLE_GRID = new Color(234, 236, 240);

    // Typography
    private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 28);
    private static final Font HEADING_FONT = new Font("Segoe UI", Font.BOLD, 16);
    private static final Font SUBHEADING_FONT = new Font("Segoe UI", Font.BOLD, 14);
    private static final Font BODY_FONT = new Font("Segoe UI", Font.PLAIN, 13);
    private static final Font TABLE_FONT = new Font("Segoe UI", Font.PLAIN, 12);
    private static final Font TABLE_HEADER_FONT = new Font("Segoe UI", Font.BOLD, 12);
    private static final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 13);

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
    private final JLabel creditsLabel = new JLabel("Total Credits: ");

    private final JPanel bottomButtons = new JPanel();
    private final JButton back;
    private final JButton regenerate;

    private SaveTimetableController saveTimetableController;
    private final JButton save;
    private final JButton exportPng;

    public DisplayTimetableView(DisplayTimetableViewModel displayTimetableViewModel) {
        this.displayTimetableViewModel = displayTimetableViewModel;
        displayTimetableViewModel.addPropertyChangeListener(this);

        // Main panel
        this.setBackground(BACKGROUND_COLOR);
        this.setLayout(new BorderLayout(0, 0));
        this.setBorder(new EmptyBorder(20, 40, 20, 40));

        JPanel headerPanel = createHeaderPanel();

        // Base data
        String[][] fallTimetableData = new String[12][6];
        String[][] winterTimetableData = new String[12][6];
        for (int i = 0; i < 12; i++) {
            fallTimetableData[i][0] = (i + 9) + ":00";
            winterTimetableData[i][0] = (i + 9) + ":00";
        }
        String[] columnHeaders =
                {"Time", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};

        fallTimetable = new JTable(fallTimetableData, columnHeaders);
        winterTimetable = new JTable(winterTimetableData, columnHeaders);

        setupTimetable(fallTimetable, fallColorMap, fallAlignMap);
        setupTimetable(winterTimetable, winterColorMap, winterAlignMap);

        // Timetables card layout
        timetablesPanel.setLayout(new GridLayout(1, 2, 24, 0));
        timetablesPanel.setOpaque(false);
        timetablesPanel.setBorder(new EmptyBorder(0, 0, 16, 0));

        // Fall card
        fallPanel.setLayout(new BorderLayout());
        fallPanel.setBackground(CARD_COLOR);
        fallPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                new EmptyBorder(20, 20, 20, 20)
        ));
        JLabel fallTitle = new JLabel("Fall Semester");
        fallTitle.setFont(HEADING_FONT);
        fallTitle.setForeground(PRIMARY_COLOR);
        fallTitle.setBorder(new EmptyBorder(0, 0, 12, 0));

        JPanel fallContentPanel = new JPanel(new BorderLayout());
        fallContentPanel.setOpaque(false);
        fallContentPanel.add(fallTitle, BorderLayout.NORTH);

        JScrollPane fallScrollPane = new JScrollPane(fallTimetable);
        styleScrollPane(fallScrollPane);
        fallContentPanel.add(fallScrollPane, BorderLayout.CENTER);
        fallPanel.add(fallContentPanel);
        timetablesPanel.add(fallPanel);

        // Winter card
        winterPanel.setLayout(new BorderLayout());
        winterPanel.setBackground(CARD_COLOR);
        winterPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                new EmptyBorder(20, 20, 20, 20)
        ));
        JLabel winterTitle = new JLabel("Winter Semester");
        winterTitle.setFont(HEADING_FONT);
        winterTitle.setForeground(PRIMARY_COLOR);
        winterTitle.setBorder(new EmptyBorder(0, 0, 12, 0));

        JPanel winterContentPanel = new JPanel(new BorderLayout());
        winterContentPanel.setOpaque(false);
        winterContentPanel.add(winterTitle, BorderLayout.NORTH);

        JScrollPane winterScrollPane = new JScrollPane(winterTimetable);
        styleScrollPane(winterScrollPane);
        winterContentPanel.add(winterScrollPane, BorderLayout.CENTER);
        winterPanel.add(winterContentPanel);
        timetablesPanel.add(winterPanel);

        // Courses + credits card with scroll
        coursesPanel.setLayout(new BoxLayout(coursesPanel, BoxLayout.Y_AXIS));
        coursesPanel.setBackground(CARD_COLOR);
        coursesPanel.setBorder(new EmptyBorder(0, 0, 0, 0));

        creditsLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        creditsLabel.setForeground(PRIMARY_COLOR);
        creditsLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        creditsLabel.setBorder(new EmptyBorder(10, 0, 0, 0));

        JPanel coursesCard = new JPanel(new BorderLayout());
        coursesCard.setBackground(CARD_COLOR);
        coursesCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                new EmptyBorder(16, 24, 16, 24)
        ));

        JLabel coursesCardTitle = new JLabel("Enrolled Courses");
        coursesCardTitle.setFont(SUBHEADING_FONT);
        coursesCardTitle.setForeground(PRIMARY_COLOR);
        coursesCardTitle.setBorder(new EmptyBorder(0, 0, 12, 0));

        JScrollPane coursesScrollPane = new JScrollPane(coursesPanel);
        coursesScrollPane.setBorder(null);
        coursesScrollPane.setBackground(CARD_COLOR);
        coursesScrollPane.getViewport().setBackground(CARD_COLOR);
        coursesScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        coursesScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        coursesScrollPane.setPreferredSize(new Dimension(0, 150));

        coursesCard.add(coursesCardTitle, BorderLayout.NORTH);
        coursesCard.add(coursesScrollPane, BorderLayout.CENTER);

        // Buttons row
        bottomButtons.setLayout(new FlowLayout(FlowLayout.CENTER, 16, 0));
        bottomButtons.setOpaque(false);
        bottomButtons.setBorder(new EmptyBorder(16, 0, 0, 0));

        back = createSecondaryButton("← Back");
        back.addActionListener(e -> {
            if (displayTimetableController != null) {
                displayTimetableController.returnToPrefs();
            }
        });
        bottomButtons.add(back);

        regenerate = createSecondaryButton("🔄 Regenerate");
        regenerate.addActionListener(e -> {
            if (displayTimetableController != null) {
                displayTimetableController.regenerateTimetable();
            }
        });
        bottomButtons.add(regenerate);

        // Save CSV button with file chooser and success message
        save = createSecondaryButton("💾 Save CSV");
        save.addActionListener(e -> {
            if (saveTimetableController != null) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Save Timetable as CSV");
                fileChooser.setFileFilter(new FileNameExtensionFilter("CSV Files", "csv"));
                fileChooser.setSelectedFile(new File("timetable.csv"));
                fileChooser.setCurrentDirectory(new File(System.getProperty("user.home") + "/Desktop"));

                int userSelection = fileChooser.showSaveDialog(DisplayTimetableView.this);

                if (userSelection == JFileChooser.APPROVE_OPTION) {
                    File fileToSave = fileChooser.getSelectedFile();

                    // Ensure .csv extension
                    String filePath = fileToSave.getAbsolutePath();
                    if (!filePath.toLowerCase().endsWith(".csv")) {
                        filePath = filePath + ".csv";
                    }

                    saveTimetableController.saveTimetable(filePath);

                    JOptionPane.showMessageDialog(
                            DisplayTimetableView.this,
                            "Timetable saved successfully to:\n" + filePath,
                            "Save Successful",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                }
            } else {
                JOptionPane.showMessageDialog(
                        DisplayTimetableView.this,
                        "SaveTimetableController is not set.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });
        bottomButtons.add(save);

        // Export PNG button with file chooser and success message
        exportPng = createSecondaryButton("📸 Export PNG");
        exportPng.addActionListener(e -> exportTimetableAsPng());
        bottomButtons.add(exportPng);

        // Center layout: timetables on top, courses card at bottom
        JPanel centerPanel = new JPanel(new BorderLayout(0, 16));
        centerPanel.setOpaque(false);
        centerPanel.add(timetablesPanel, BorderLayout.NORTH);
        centerPanel.add(coursesCard, BorderLayout.CENTER);

        this.add(headerPanel, BorderLayout.NORTH);
        this.add(centerPanel, BorderLayout.CENTER);
        this.add(bottomButtons, BorderLayout.SOUTH);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setOpaque(false);
        headerPanel.setBorder(new EmptyBorder(0, 0, 20, 0));

        JLabel title = new JLabel("Your Timetable");
        title.setFont(TITLE_FONT);
        title.setForeground(PRIMARY_COLOR);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Review your personalized schedule");
        subtitle.setFont(BODY_FONT);
        subtitle.setForeground(TEXT_SECONDARY);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitle.setBorder(new EmptyBorder(6, 0, 0, 0));

        headerPanel.add(title);
        headerPanel.add(subtitle);
        return headerPanel;
    }

    private void setupTimetable(JTable timetableTable,
                                HashMap<Point, Color> colorMap,
                                HashMap<Point, Integer> alignMap) {

        timetableTable.setDefaultEditor(Object.class, null);
        timetableTable.setFont(TABLE_FONT);
        timetableTable.setForeground(TEXT_PRIMARY);
        timetableTable.setRowHeight(38);
        timetableTable.setShowGrid(true);
        timetableTable.setGridColor(TABLE_GRID);
        timetableTable.setIntercellSpacing(new Dimension(1, 1));
        timetableTable.getColumnModel().getColumn(0).setPreferredWidth(80);
        timetableTable.setSelectionBackground(HOVER_COLOR);
        timetableTable.setSelectionForeground(TEXT_PRIMARY);

        JTableHeader header = timetableTable.getTableHeader();
        header.setFont(TABLE_HEADER_FONT);
        header.setBackground(TABLE_HEADER_BG);
        header.setForeground(TEXT_PRIMARY);
        header.setPreferredSize(new Dimension(header.getWidth(), 40));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, BORDER_COLOR));

        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table,
                                                           Object value,
                                                           boolean isSelected,
                                                           boolean hasFocus,
                                                           int row,
                                                           int column) {
                JLabel label = new JLabel(value.toString());
                label.setFont(TABLE_HEADER_FONT);
                label.setForeground(TEXT_PRIMARY);
                label.setBackground(TABLE_HEADER_BG);
                label.setOpaque(true);
                label.setHorizontalAlignment(JLabel.CENTER);
                label.setBorder(new EmptyBorder(8, 12, 8, 12));
                return label;
            }
        });

        timetableTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table,
                                                           Object value,
                                                           boolean isSelected,
                                                           boolean hasFocus,
                                                           int row,
                                                           int column) {
                Component component = super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);
                Point point = new Point(row, column);

                if (column == 0) {
                    component.setBackground(TABLE_HEADER_BG);
                    component.setForeground(TEXT_SECONDARY);
                    component.setFont(TABLE_FONT);
                } else {
                    Color bgColor = colorMap.getOrDefault(point, Color.WHITE);
                    component.setBackground(bgColor);
                    component.setForeground(TEXT_PRIMARY);
                    component.setFont(new Font("Segoe UI", Font.PLAIN, 11));
                }
                super.setHorizontalAlignment(alignMap.getOrDefault(point, JLabel.CENTER));
                super.setBorder(new EmptyBorder(6, 10, 6, 10));
                return component;
            }
        });
    }

    private void styleScrollPane(JScrollPane scrollPane) {
        scrollPane.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setPreferredSize(new Dimension(0, 380));
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
                new EmptyBorder(11, 24, 11, 24)
        ));

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

    private Color chooseColour(boolean lighter, int id) {
        if (lighter) {
            return Color.getHSBColor(0.1f * id, 0.40f, 0.95f);
        }
        return Color.getHSBColor(0.1f * id, 0.65f, 0.90f);
    }

    private void displayCourses(TimetableDTO fallTTB, TimetableDTO winterTTB) {
        for (int row = 0; row < 12; row++) {
            for (int col = 0; col < 6; col++) {
                Point point = new Point(row, col);
                fallColorMap.put(point, Color.WHITE);
                fallAlignMap.put(point, JLabel.CENTER);
                winterColorMap.put(point, Color.WHITE);
                winterAlignMap.put(point, JLabel.CENTER);
                if (col != 0) {
                    fallTimetable.setValueAt("", row, col);
                    winterTimetable.setValueAt("", row, col);
                }
            }
        }

        ArrayList<String> courses = new ArrayList<>();
        displayTimetable(fallTTB.getTable(), fallTimetable, fallColorMap, fallAlignMap, courses);
        displayTimetable(winterTTB.getTable(), winterTimetable, winterColorMap, winterAlignMap, courses);
    }

    private void displayTimetable(ArrayList<ArrayList<ArrayList<String>>> table,
                                  JTable timetableTable,
                                  HashMap<Point, Color> colorMap,
                                  HashMap<Point, Integer> alignMap,
                                  ArrayList<String> courses) {

        for (int day = 0; day < table.size(); day++) {
            String lastKey = null;

            for (int hour = 0; hour < table.get(day).size(); hour++) {
                if (!table.get(day).get(hour).isEmpty()) {
                    String block = table.get(day).get(hour).get(0);
                    String courseCode = block.substring(0, 8);
                    String sessionCode;
                    if (courseCode.charAt(6) == 'H') {
                        sessionCode = block.substring(9);
                    } else {
                        sessionCode = block.substring(8);
                    }

                    if (!courses.contains(courseCode)) {
                        courses.add(courseCode);
                    }

                    String key = courseCode + "|" + sessionCode;

                    Color sessionColour;
                    if (sessionCode.contains("LEC")) {
                        sessionColour = chooseColour(false, courses.indexOf(courseCode));
                    } else {
                        sessionColour = chooseColour(true, courses.indexOf(courseCode));
                    }

                    int row = hour;
                    int col = day + 1;

                    Point point = new Point(row, col);
                    colorMap.put(point, sessionColour);
                    alignMap.put(point, JLabel.CENTER);

                    if (!key.equals(lastKey)) {
                        String displayHtml = "<html><b>" + courseCode +
                                "</b><br>" + sessionCode + "</html>";
                        timetableTable.setValueAt(displayHtml, row, col);
                    } else {
                        timetableTable.setValueAt("", row, col);
                    }

                    lastKey = key;
                } else {
                    lastKey = null;
                }
            }
        }
    }

    private void updateCoursesCard(ArrayList<String> courseTitles,
                                   ArrayList<String> courseCodes,
                                   ArrayList<Double> credits) {

        coursesPanel.removeAll();

        double totalCredits = 0.0;

        for (int i = 0; i < courseTitles.size(); i++) {
            String title = courseTitles.get(i);
            String code = courseCodes.get(i);
            double credit = (i < credits.size()) ? credits.get(i) : 0.0;
            totalCredits += credit;

            JPanel courseItem = new JPanel(new BorderLayout(8, 0));
            courseItem.setOpaque(false);
            courseItem.setAlignmentX(Component.LEFT_ALIGNMENT);
            courseItem.setBorder(new EmptyBorder(4, 0, 4, 0));

            JPanel colorBox = new JPanel();
            colorBox.setPreferredSize(new Dimension(6, 22));
            colorBox.setBackground(chooseColour(false, i));

            JLabel courseLabel = new JLabel(
                    String.format("%s (%s) – %.1f credits", title, code, credit)
            );
            courseLabel.setFont(BODY_FONT);
            courseLabel.setForeground(TEXT_PRIMARY);

            courseItem.add(colorBox, BorderLayout.WEST);
            courseItem.add(courseLabel, BorderLayout.CENTER);
            coursesPanel.add(courseItem);
        }

        coursesPanel.add(Box.createVerticalStrut(8));

        creditsLabel.setText("Total Credits: " + totalCredits);
        coursesPanel.add(creditsLabel);

        coursesPanel.revalidate();
        coursesPanel.repaint();
    }

    private void exportTimetableAsPng() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Export Timetable as PNG");
        fileChooser.setFileFilter(new FileNameExtensionFilter("PNG Images", "png"));
        fileChooser.setSelectedFile(new File("timetable.png"));
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home") + "/Desktop"));

        int userSelection = fileChooser.showSaveDialog(this);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            if (!file.getName().toLowerCase().endsWith(".png")) {
                file = new File(file.getParentFile(), file.getName() + ".png");
            }

            BufferedImage image = new BufferedImage(
                    timetablesPanel.getWidth(),
                    timetablesPanel.getHeight(),
                    BufferedImage.TYPE_INT_ARGB
            );
            Graphics2D g2 = image.createGraphics();
            timetablesPanel.printAll(g2);
            g2.dispose();

            try {
                ImageIO.write(image, "png", file);
                JOptionPane.showMessageDialog(this,
                        "Timetable exported successfully to:\n" + file.getAbsolutePath(),
                        "Export Successful",
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this,
                        "Error exporting PNG: " + ex.getMessage(),
                        "Export Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // no-op
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        DisplayTimetableState state = (DisplayTimetableState) evt.getNewValue();

        updateCoursesCard(state.getCourseNames(),
                state.getCourses(),
                state.getCredit());

        displayCourses(
                state.getFallTimetables().get(state.getFallIndex()),
                state.getWinterTimetables().get(state.getWinterIndex())
        );
    }

    public String getViewName() {
        return viewName;
    }

    public void setDisplayTimetableController(DisplayTimetableController displayTimetableController) {
        this.displayTimetableController = displayTimetableController;
    }

    public void setSaveTimetableController(SaveTimetableController saveTimetableController) {
        this.saveTimetableController = saveTimetableController;
    }
}
