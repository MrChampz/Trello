package view.signin;

import view.common.Button;
import view.common.TextField;
import view.common.PasswordField;

import javax.swing.*;
import java.awt.*;

public class SignInFieldsPanel extends JPanel {

    public SignInFieldsPanel(Color bgColor) {
        setupPanel();
        setupWelcomeLabel();
        setupExpansor(1);
        setupEmailField();
        setupPassField();
        setupSignInButton();
        setupExpansor(5);
        setupSignUpButton(bgColor);
    }

    private void setupPanel() {
        setLayout(new GridBagLayout());
        setOpaque(false);
    }

    private void setupWelcomeLabel() {
        GridBagConstraints constr = new GridBagConstraints();
        constr.fill = GridBagConstraints.HORIZONTAL;
        constr.weightx = 1.0;
        constr.gridy = 0;

        JLabel label = new JLabel("Vamos começar?");
        label.setFont(new Font("Helvetica", Font.BOLD, 30));
        label.setForeground(Color.white);

        add(label, constr);
    }

    private void setupEmailField() {
        GridBagConstraints constr = new GridBagConstraints();
        constr.insets = new Insets(0, 0, 5, 0);
        constr.gridy = 2;

        TextField emailField = new TextField("Email");
        emailField.setRound(8);

        add(emailField, constr);
    }

    private void setupPassField() {
        GridBagConstraints constr = new GridBagConstraints();
        constr.insets = new Insets(0, 0, 5, 0);
        constr.gridy = 3;

        PasswordField passField = new PasswordField("Senha");
        passField.setRound(8);

        add(passField, constr);
    }

    private void setupSignInButton() {
        GridBagConstraints constr = new GridBagConstraints();
        constr.gridy = 4;

        Button signInButton = new Button("Entrar");
        signInButton.setPreferredSize(new Dimension(300, 50));
        signInButton.setFont(new Font("Helvetica", Font.PLAIN, 25));
        signInButton.setRound(8);

        signInButton.setColor(new Color(97, 189, 79));
        signInButton.setHoverColor(new Color(80, 167, 63));
        signInButton.setClickColor(new Color(75, 158, 59));
        signInButton.setTextColor(Color.white);

        add(signInButton.get(), constr);
    }

    private void setupSignUpButton(Color bgColor) {
        GridBagConstraints constr = new GridBagConstraints();
        constr.gridy = 6;

        Button signUpButton = new Button("Cadastrar-se");
        signUpButton.setPreferredSize(new Dimension(300, 50));
        signUpButton.setFont(new Font("Helvetica", Font.PLAIN, 25));
        signUpButton.setRound(8);

        signUpButton.setColor(Color.white);
        signUpButton.setHoverColor(Color.white);
        signUpButton.setClickColor(Color.white);
        signUpButton.setTextColor(bgColor);

        add(signUpButton.get(), constr);
    }

    private void setupExpansor(int gridy) {
        GridBagConstraints constr = new GridBagConstraints();
        constr.fill = GridBagConstraints.VERTICAL;
        constr.weighty = 1.0;
        constr.gridy = gridy;
        add(new JLabel(), constr);
    }
}
