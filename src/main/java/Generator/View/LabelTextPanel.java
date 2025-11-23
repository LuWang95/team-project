package Generator.View;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class LabelTextPanel extends JPanel {
    public LabelTextPanel(JLabel label, JTextField textField) {
        super();
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setOpaque(false);

        // Style label
        label.setFont(new Font("SansSerif", Font.BOLD, 15));
        label.setForeground(new Color(44, 62, 80)); // deep blue
        label.setBorder(new EmptyBorder(0, 0, 0, 10)); // right gap

        // Style text field
        textField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        textField.setMaximumSize(new Dimension(180, 28));
        textField.setPreferredSize(new Dimension(150, 28));
        textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(190, 190, 190), 1),
                BorderFactory.createEmptyBorder(2, 4, 2, 4)
        ));

        // Panel border and padding
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
                BorderFactory.createEmptyBorder(6, 6, 6, 6)
        ));

        add(label);
        add(textField);
        add(Box.createHorizontalGlue()); // push content left if panel is wider
    }
}
