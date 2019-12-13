package view.signup;

import model.bean.Foto;
import view.common.*;
import view.common.Button;
import view.common.TextField;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class SignUpFieldsPanel extends JPanel {

    private CircularImageView imageView;
    private TextField nickField;
    private TextField nameField;
    private TextField emailField;
    private PasswordField passField;
    private Button signUpButton;

    public SignUpFieldsPanel() throws IOException {
        setupPanel();
        setupExpansor(0);
        setupImageView();
        setupExpansor(2);
        setupNickField();
        setupNameField();
        setupEmailField();
        setupPassField();
        setupExpansor(7);
        setupSignUpButton();
    }

    public String getNick() {
        return nickField.getText();
    }

    public String getNameField() {
        return nameField.getText();
    }

    public String getEmail() {
        return emailField.getText();
    }

    public String getPassword() {
        return passField.getText();
    }

    public Foto getPicture() {
        return imageView.getSource();
    }

    public void setSignUpButtonClickListener(ClickListener listener) {
        signUpButton.setClickListener(listener);
    }

    private void setupPanel() {
        setLayout(new GridBagLayout());
        setOpaque(false);
    }

    private void setupImageView() throws IOException {
        GridBagConstraints constr = new GridBagConstraints();
        constr.fill = GridBagConstraints.HORIZONTAL;
        constr.weightx = 1.0;
        constr.gridy = 1;

        imageView = new CircularImageView("res/felps.jpg", 150);
        imageView.setClickListener(this::onImageViewClick);
        imageView.setCursor(new Cursor(Cursor.HAND_CURSOR));

        add(imageView.get(), constr);
    }

    private void setupNickField() {
        GridBagConstraints constr = new GridBagConstraints();
        constr.insets = new Insets(0, 0, 5, 0);
        constr.fill = GridBagConstraints.HORIZONTAL;
        constr.gridy = 3;

        nickField = new TextField("Apelido");
        nickField.setRound(8);

        add(nickField, constr);
    }

    private void setupNameField() {
        GridBagConstraints constr = new GridBagConstraints();
        constr.insets = new Insets(0, 0, 5, 0);
        constr.fill = GridBagConstraints.HORIZONTAL;
        constr.gridy = 4;

        nameField = new TextField("Nome");
        nameField.setRound(8);

        add(nameField, constr);
    }

    private void setupEmailField() {
        GridBagConstraints constr = new GridBagConstraints();
        constr.insets = new Insets(0, 0, 5, 0);
        constr.fill = GridBagConstraints.HORIZONTAL;
        constr.gridy = 5;

        emailField = new TextField("Email");
        emailField.setRound(8);

        add(emailField, constr);
    }

    private void setupPassField() {
        GridBagConstraints constr = new GridBagConstraints();
        constr.insets = new Insets(0, 0, 5, 0);
        constr.fill = GridBagConstraints.HORIZONTAL;
        constr.gridy = 6;

        passField = new PasswordField("Senha");
        passField.setRound(8);

        add(passField, constr);
    }

    private void setupSignUpButton() {
        GridBagConstraints constr = new GridBagConstraints();
        constr.fill = GridBagConstraints.HORIZONTAL;
        constr.gridy = 8;

        signUpButton = new Button("Cadastrar");
        signUpButton.setPreferredSize(new Dimension(300, 50));
        signUpButton.setFont(new Font("Helvetica", Font.PLAIN, 25));
        signUpButton.setRound(8);

        signUpButton.setColor(new Color(97, 189, 79));
        signUpButton.setHoverColor(new Color(80, 167, 63));
        signUpButton.setClickColor(new Color(75, 158, 59));
        signUpButton.setTextColor(Color.white);

        add(signUpButton.get(), constr);
    }

    private void setupExpansor(int gridy) {
        GridBagConstraints constr = new GridBagConstraints();
        constr.fill = GridBagConstraints.VERTICAL;
        constr.weighty = 1.0;
        constr.gridy = gridy;
        add(new JLabel(), constr);
    }

    private void onImageViewClick() {
        try {
            FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "Arquivos de imagem",
                "jpg", "jpeg", "png", "bmp"
            );

            File dir = FileSystemView.getFileSystemView().getHomeDirectory();
            JFileChooser chooser = new JFileChooser(dir);
            chooser.setDialogTitle("Selecione uma imagem");
            chooser.setAcceptAllFileFilterUsed(false);
            chooser.addChoosableFileFilter(filter);

            int result = chooser.showOpenDialog(null);

            if (result == JFileChooser.APPROVE_OPTION) {
                File imageFile = chooser.getSelectedFile();
                imageView.setImage(imageFile);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
