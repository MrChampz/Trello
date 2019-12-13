package view.signin;

import view.common.ClickListener;

import javax.swing.*;
import java.awt.*;

public class SignInPanel extends JPanel {

    private SignInFieldsPanel fieldsPanel;

    public SignInPanel(Color bgColor) {
        setupPanel();
        setupLogoPanel();
        setupSeparator();
        setupFieldsPanel(bgColor);
    }

    public String getEmail() {
        return fieldsPanel.getEmail();
    }

    public String getPassword() {
        return fieldsPanel.getPassword();
    }

    public void setSignInButtonClickListener(ClickListener listener) {
        fieldsPanel.setSignInButtonClickListener(listener);
    }

    public void setSignUpButtonClickListener(ClickListener listener) {
        fieldsPanel.setSignUpButtonClickListener(listener);
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

        fieldsPanel = new SignInFieldsPanel(bgColor);
        add(fieldsPanel, constr);
    }
}
