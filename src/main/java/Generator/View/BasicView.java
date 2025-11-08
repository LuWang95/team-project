package Generator.View;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;


public class BasicView {
    public String viewName = "BasicView";

    private final JLabel courseNotFoundLabel = new JLabel();
    private final JTextField courseNotFoundTextField = new JTextField();
    private final JTextField courseNameTextField = new JTextField();

    private final JButton addCourseButton = new JButton("Add");
    private final JButton clearButton = new JButton("Clear");

    private final JPanel buttonPanel = new JPanel();
    private final JPanel coursePanel = new JPanel();


}
