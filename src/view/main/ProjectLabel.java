package view.main;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ProjectLabel extends JPanel {

    public ProjectLabel() {
        setupPanel();
        setupIcon();
        setupLabel();
    }

    private void setupPanel() {
        setLayout(new GridBagLayout());
        setBorder(new EmptyBorder(8, 8, 8, 0));
        setAlignmentY(Component.CENTER_ALIGNMENT);
        setOpaque(false);
    }

    private void setupIcon() {
        GridBagConstraints constr = new GridBagConstraints();
        constr.insets = new Insets(0, 0, 0, 12);
        constr.gridx = 0;

        Icon icon = new ImageIcon("res/ic_projects.png");
        JLabel iconLabel = new JLabel(icon);

        add(iconLabel, constr);
    }

    private void setupLabel() {
        GridBagConstraints constr = new GridBagConstraints();
        constr.fill = GridBagConstraints.HORIZONTAL;
        constr.weightx = 1.0;
        constr.gridx = 1;

        JLabel textLabel = new JLabel("Projetos");
        textLabel.setFont(new Font("Helvetica", Font.BOLD, 16));
        textLabel.setForeground(new Color(23, 43, 77));

        add(textLabel, constr);
    }
}
