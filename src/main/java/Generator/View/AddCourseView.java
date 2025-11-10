package Generator.View;

import Generator.InterfaceAdapter.add_course.AddCourseState;
import Generator.InterfaceAdapter.add_course.AddCourseViewModel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class AddCourseView extends JPanel implements ActionListener, PropertyChangeListener {

    private final String viewName = "add course";
    private final AddCourseViewModel addCourseViewModel;

    private final JTextField degreeInputField = new JTextField(15);
    private final JTextField courseInputField = new JTextField(15);
    private final JTextField yearInputField = new JTextField(15);
    private final JTextField timeInputField = new JTextField(15);

    private final JButton generate;

    public AddCourseView(AddCourseViewModel addCourseViewModel) {
        this.addCourseViewModel = addCourseViewModel;
        addCourseViewModel.addPropertyChangeListener(this);

        final JLabel title = new JLabel("Set Preferences");

        final JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.X_AXIS));
        final JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        final JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));

        final LabelTextPanel degreeInfo = new LabelTextPanel(
                new JLabel("Degree"), degreeInputField);
        final LabelTextPanel courseInfo = new LabelTextPanel(
                new JLabel("Course"), courseInputField);


        final JPanel yearInfo = new JPanel();
        final ButtonGroup yearGroup = new ButtonGroup();
        final JLabel yearTitle = new JLabel("Year of Study");
        yearInfo.add(yearTitle);
        JRadioButton yearButton1 = new JRadioButton();
        JRadioButton yearButton2 = new JRadioButton();
        JRadioButton yearButton3 = new JRadioButton();
        JRadioButton yearButton4 = new JRadioButton();
        yearButton1.setText("1");
        yearButton2.setText("2");
        yearButton3.setText("3");
        yearButton4.setText("4");
        yearInfo.add(yearButton1);
        yearInfo.add(yearButton2);
        yearInfo.add(yearButton3);
        yearInfo.add(yearButton4);
        yearGroup.add(yearButton1);
        yearGroup.add(yearButton2);
        yearGroup.add(yearButton3);
        yearGroup.add(yearButton4);


        final JPanel timeInfo = new JPanel();
        final JLabel timeTitle = new JLabel("Preferred Time");
        timeInfo.add(timeTitle);
        JRadioButton timeButton1 = new JRadioButton();
        JRadioButton timeButton2 = new JRadioButton();
        JRadioButton timeButton3 = new JRadioButton();
        timeButton1.setText("Morning");
        timeButton2.setText("Afternoon");
        timeButton3.setText("Evening");
        timeInfo.add(timeButton1);
        timeInfo.add(timeButton2);
        timeInfo.add(timeButton3);



        generate = new JButton("Generate Time Table");
        generate.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
                        System.out.println("hello world!!!");
                    }
                }
        );

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.add(title);
        leftPanel.add(degreeInfo);
        leftPanel.add(courseInfo);
        infoPanel.add(leftPanel);
        rightPanel.add(yearInfo);
        rightPanel.add(timeInfo);
        infoPanel.add(rightPanel);
        this.add(infoPanel);
        this.add(generate);

    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        final AddCourseState state = (AddCourseState) evt.getNewValue();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JOptionPane.showMessageDialog(this, "sdfjvcxjvk");
    }

    public String getViewName() {
        return viewName;
    }

}
