package view.signin;

import view.common.CenteredMultilineLabel;

import javax.swing.*;
import java.awt.*;

public class LogoPanel extends JPanel {

    public LogoPanel() {
        setupPanel();
        setupLogo();
        setupText();
    }

    private void setupPanel() {
        setLayout(new GridBagLayout());
        setBackground(Color.white);
    }

    private void setupLogo() {
        GridBagConstraints constr = new GridBagConstraints();
        constr.insets = new Insets(16, 16, 25, 16);
        constr.fill = GridBagConstraints.HORIZONTAL;
        constr.weightx = 1.0;
        constr.gridy = 0;

        Icon icon = new ImageIcon("res/trello_icon.png");
        JLabel logoLabel = new JLabel(icon, SwingConstants.CENTER);

        add(logoLabel, constr);
    }

    private void setupText() {
        GridBagConstraints constr = new GridBagConstraints();
        constr.insets = new Insets(0, 16, 16, 16);
        constr.fill = GridBagConstraints.HORIZONTAL;
        constr.weightx = 1.0;
        constr.gridy = 1;

        String text = "O Trello permite trabalhar com mais colaboração e ter mais produtividade.";
        CenteredMultilineLabel textLabel = new CenteredMultilineLabel(text);

        textLabel.setFont(
            new Font("Helvetica", Font.PLAIN, 20),
            new Color(23, 43, 77)
        );

        textLabel.setPreferredSize(new Dimension(150, 110));

        add(textLabel.get(), constr);
    }
}
