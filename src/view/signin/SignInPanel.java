package view.signin;

import javax.swing.*;
import java.awt.*;

public class SignInPanel extends JPanel {

    public SignInPanel(Color bgColor) {
        setupPanel();
        setupLogoPanel();
        setupSeparator();
        setupFieldsPanel(bgColor);
    }

    private void setupPanel() {
        setLayout(new GridBagLayout());
        setOpaque(false);
    }

    private void setupLogoPanel() {
        GridBagConstraints constr = new GridBagConstraints();
        constr.fill = GridBagConstraints.BOTH;
        constr.weightx = 1.0; constr.weighty = 1.0;
        constr.gridx = 0;

        add(new LogoPanel(), constr);
    }

    private void setupSeparator() {
        GridBagConstraints constr = new GridBagConstraints();
        constr.fill = GridBagConstraints.VERTICAL;
        constr.weighty = 1.0;
        constr.gridx = 1;

        JPanel separator = new JPanel();
        separator.setPreferredSize(new Dimension(1, 0));
        separator.setBackground(new Color(0, 0, 0, 38));

        add(separator, constr);
    }

    private void setupFieldsPanel(Color bgColor) {
        GridBagConstraints constr = new GridBagConstraints();
        constr.insets = new Insets(16, 16, 16, 16);
        constr.fill = GridBagConstraints.VERTICAL;
        constr.weighty = 1.0;
        constr.gridx = 2;

        add(new SignInFieldsPanel(bgColor), constr);
    }
}
