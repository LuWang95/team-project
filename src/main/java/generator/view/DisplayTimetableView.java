package generator.view;

import generator.interfaceadapter.display_timetable.DisplayTimetableController;
import generator.interfaceadapter.display_timetable.DisplayTimetableState;
import generator.interfaceadapter.display_timetable.DisplayTimetableViewModel;
import generator.interfaceadapter.save_timetable.SaveTimetableController;
import generator.usecase.generate_timetable.TimetableDTO;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
    private final JLabel creditsLabel = new JLabel("Credits: ");

    private final JPanel bottomButtons = new JPanel();
    private final JButton back;
    private final JButton regenerate;

    // Save use case controller + button
    private SaveTimetableController saveTimetableController;
    private final JButton save;

    // PNG export button
    private final JButton exportPng;

    public DisplayTimetableView(DisplayTimetableViewModel displayTimetableViewModel) {
        this.displayTimetableViewModel = displayTimetableViewModel;
        displayTimetableViewModel.addPropertyChangeListener(this);

        final JLabel title = new JLabel("Your Timetable");
        title.setAlignmentX(JLabel.CENTER);

        final String[][] fallTimetableData = new String[12][6];
        final String[][] winterTimetableData = new String[12][6];
        for (int i = 0; i < 12; i++) {
            fallTimetableData[i][0] = (i + 9) + ":00";
            winterTimetableData[i][0] = (i + 9) + ":00";
        }

        final String[] columnHeaders = {"Time", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
        fallTimetable = new JTable(fallTimetableData, columnHeaders);
        winterTimetable = new JTable(winterTimetableData, columnHeaders);

        setupTimetable(fallTimetable, fallColorMap, fallAlignMap);
        setupTimetable(winterTimetable, winterColorMap, winterAlignMap);

        timetablesPanel.setLayout(new javax.swing.BoxLayout(
                timetablesPanel, javax.swing.BoxLayout.X_AXIS));

        fallPanel.setLayout(new javax.swing.BoxLayout(
                fallPanel, javax.swing.BoxLayout.Y_AXIS));
        final JLabel fallTitle = new JLabel("Fall Timetable");
        fallTitle.setAlignmentX(JLabel.CENTER);
        fallPanel.add(fallTitle);
        fallPanel.add(fallTimetable.getTableHeader());
        fallPanel.add(fallTimetable);
        timetablesPanel.add(fallPanel);

        timetablesPanel.add(new JLabel(" "));

        winterPanel.setLayout(new javax.swing.BoxLayout(
                winterPanel, javax.swing.BoxLayout.Y_AXIS));
        final JLabel winterTitle = new JLabel("Winter Timetable");
        winterTitle.setAlignmentX(JLabel.CENTER);
        winterPanel.add(winterTitle);
        winterPanel.add(winterTimetable.getTableHeader());
        winterPanel.add(winterTimetable);
        timetablesPanel.add(winterPanel);

        coursesPanel.setLayout(new javax.swing.BoxLayout(
                coursesPanel, javax.swing.BoxLayout.Y_AXIS));
        coursesPanel.add(new JLabel("Courses:"));
        coursesPanel.setAlignmentX(JLabel.CENTER);
        creditsLabel.setAlignmentX(JLabel.CENTER);

        bottomButtons.setLayout(new javax.swing.BoxLayout(
                bottomButtons, javax.swing.BoxLayout.X_AXIS));
        bottomButtons.setAlignmentX(JLabel.CENTER);

        back = new JButton("Back");
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (displayTimetableController != null) {
                    displayTimetableController.returnToPrefs();
                }
            }
        });
        bottomButtons.add(back);

        regenerate = new JButton("Regenerate");
        regenerate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (displayTimetableController != null) {
                    displayTimetableController.regenerateTimetable();
                }
            }
        });
        bottomButtons.add(regenerate);

        // Save timetable as CSV (via use case)
        save = new JButton("Save");
        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (saveTimetableController != null) {
                    saveTimetableController.saveTimetable("saved_timetable.csv");
                } else {
                    JOptionPane.showMessageDialog(
                            DisplayTimetableView.this,
                            "SaveTimetableController is not set.");
                }
            }
        });
        bottomButtons.add(save);

        // Export timetable panel as PNG (pure UI)
        exportPng = new JButton("Export PNG");
        exportPng.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exportTimetableAsPng();
            }
        });
        bottomButtons.add(exportPng);

        this.setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.Y_AXIS));
        this.add(title);
        this.add(timetablesPanel);
        this.add(coursesPanel);
        this.add(creditsLabel);
        this.add(bottomButtons);
    }

    private void setupTimetable(JTable timetableTable,
                                HashMap<Point, Color> colorMap,
                                HashMap<Point, Integer> alignMap) {

        timetableTable.setDefaultEditor(Object.class, null);
        timetableTable.setShowHorizontalLines(false);
        timetableTable.setRowHeight(32);
        timetableTable.getColumnModel().getColumn(0).setPreferredWidth(15);

        timetableTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table,
                                                           Object value,
                                                           boolean isSelected,
                                                           boolean hasFocus,
                                                           int row,
                                                           int column) {
                final Component component = super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);
                final Point point = new Point(row, column);

                component.setBackground(colorMap.getOrDefault(point, Color.WHITE));
                super.setHorizontalAlignment(alignMap.getOrDefault(point, JLabel.LEFT));
                return component;
            }
        });
    }

    private void displayCourses(TimetableDTO fallTTB, TimetableDTO winterTTB) {
        // reset timetable colours
        for (int i = 0; i < 12; i++) {
            for (int j = 0; j < 6; j++) {
                final Point point = new Point(i, j);
                fallColorMap.put(point, Color.WHITE);
                fallAlignMap.put(point, JLabel.RIGHT);
                winterColorMap.put(point, Color.WHITE);
                winterAlignMap.put(point, JLabel.RIGHT);
                if (j != 0) {
                    this.fallTimetable.setValueAt("", i, j);
                    this.winterTimetable.setValueAt("", i, j);
                }
            }
        }

        final ArrayList<String> courses = new ArrayList<>();
        displayTimetable(fallTTB.getTable(), fallTimetable, fallColorMap, fallAlignMap, courses);
        displayTimetable(winterTTB.getTable(), winterTimetable, winterColorMap, winterAlignMap, courses);
    }

    private void displayTimetable(ArrayList<ArrayList<ArrayList<String>>> table,
                                  JTable timetableTable,
                                  HashMap<Point, Color> colorMap,
                                  HashMap<Point, Integer> alignMap,
                                  ArrayList<String> courses) {

        for (int i = 0; i < table.size(); i++) {
            for (int j = 0; j < table.get(0).size(); j++) {
                if (!table.get(i).get(j).isEmpty()) {
                    final String block = table.get(i).get(j).get(0);
                    final String courseCode = block.substring(0, 8);
                    final String sessionCode;
                    if (courseCode.charAt(6) == 'H') {
                        sessionCode = block.substring(9);
                    } else {
                        sessionCode = block.substring(8);
                    }
                    if (!courses.contains(courseCode)) {
                        courses.add(courseCode);
                    }
                    final String timetableString = courseCode + " " + sessionCode;
                    final Color sessionColour;
                    if (sessionCode.contains("LEC")) {
                        sessionColour = chooseColour(false, courses.indexOf(courseCode));
                    } else {
                        sessionColour = chooseColour(true, courses.indexOf(courseCode));
                    }
                    final Point point = new Point(j, i + 1);
                    colorMap.replace(point, sessionColour);

                    final Point leftPoint = new Point(j - 1, i + 1);
                    if (j == 0 || !colorMap.get(leftPoint).equals(sessionColour)) {
                        timetableTable.setValueAt(timetableString, j, i + 1);
                    }
                    alignMap.replace(point, JLabel.CENTER);
                }
            }
        }
    }

    private Color chooseColour(boolean lighter, int id) {
        if (lighter) {
            return Color.getHSBColor(0.1f * id, 0.50f, 0.90f);
        }
        return Color.getHSBColor(0.1f * id, 0.75f, 0.90f);
    }

    private void updateCoursesLabel(ArrayList<String> courseTitles, ArrayList<String> courseCodes) {
        coursesPanel.removeAll();
        coursesPanel.add(new JLabel("Courses:"));
        for (int i = 0; i < courseTitles.size(); i++) {
            coursesPanel.add(new JLabel(
                    "     " + courseTitles.get(i) + " (" + courseCodes.get(i) + ")"));
        }
    }

    private void updateCreditsLabel(ArrayList<Double> credits) {
        double totalCredits = 0;
        for (Double credit : credits) {
            totalCredits += credit;
        }
        creditsLabel.setText("Total Credits: " + totalCredits);
    }

    /**
     * Export the timetable panel as a PNG image.
     * This is a pure UI operation and does not touch any use cases.
     */
    private void exportTimetableAsPng() {
        final JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save timetable as PNG");
        fileChooser.setFileFilter(new FileNameExtensionFilter("PNG Images", "png"));

        final int userSelection = fileChooser.showSaveDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            if (!file.getName().toLowerCase().endsWith(".png")) {
                file = new File(file.getParentFile(), file.getName() + ".png");
            }

            final BufferedImage image = new BufferedImage(
                    timetablesPanel.getWidth(),
                    timetablesPanel.getHeight(),
                    BufferedImage.TYPE_INT_ARGB);

            final Graphics2D graphics2D = image.createGraphics();
            timetablesPanel.printAll(graphics2D);
            graphics2D.dispose();

            try {
                ImageIO.write(image, "png", file);
                JOptionPane.showMessageDialog(this,
                        "Timetable exported to:\n" + file.getAbsolutePath());
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this,
                        "Error saving PNG: " + ex.getMessage());
            }
        }
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
        displayCourses(state.getFallTimetables().get(state.getFallIndex()),
                state.getWinterTimetables().get(state.getWinterIndex()));
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
