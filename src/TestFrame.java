import model.bean.Foto;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

public class TestFrame extends JFrame {

    private JTextField tfNick;
    private JTextField tfName;
    private JTextField tfEmail;
    private JPasswordField pfPass;
    private JLabel lbFoto;

    public TestFrame(String title) {
        setTitle(title);

        JPanel pnRoot = new JPanel();
        pnRoot.setLayout(new BoxLayout(pnRoot, BoxLayout.Y_AXIS));
        pnRoot.add(Box.createRigidArea(new Dimension(0, 30)));

        // JLabel da foto
        lbFoto = new JLabel();
        lbFoto.setPreferredSize(new Dimension(130, 130));
        lbFoto.setAlignmentX(Component.CENTER_ALIGNMENT);

        // JPanel com as informações
        JPanel pnInfo = new JPanel(new GridBagLayout());

        GridBagConstraints constr = new GridBagConstraints();
        constr.insets = new Insets(5, 5, 5, 5);
        constr.anchor = GridBagConstraints.WEST;

        constr.gridx = 0;
        constr.gridy = 0;

        JLabel lbNick = new JLabel("Apelido:");
        JLabel lbName = new JLabel("Nome:");
        JLabel lbEmail = new JLabel("Email:");
        JLabel lbPass = new JLabel("Senha:");

        tfNick = new JTextField(25);
        tfName = new JTextField(25);
        tfEmail = new JTextField(25);
        pfPass = new JPasswordField(25);

        pnInfo.add(lbNick, constr);
        constr.gridx = 1;
        pnInfo.add(tfNick, constr);
        constr.gridx = 0; constr.gridy = 1;

        pnInfo.add(lbName, constr);
        constr.gridx = 1;
        pnInfo.add(tfName, constr);
        constr.gridx = 0; constr.gridy = 2;

        pnInfo.add(lbEmail, constr);
        constr.gridx = 1;
        pnInfo.add(tfEmail, constr);
        constr.gridx = 0; constr.gridy = 3;

        pnInfo.add(lbPass, constr);
        constr.gridx = 1;
        pnInfo.add(pfPass, constr);
        constr.gridx = 0; constr.gridy = 4;

        constr.gridwidth = 2;
        constr.anchor = GridBagConstraints.CENTER;

        pnRoot.add(lbFoto);
        pnRoot.add(pnInfo);

        add(pnRoot);
        pack();
        setSize(400, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public void setNick(String nick) {
        tfNick.setText(nick);
    }

    public void setName(String name) {
        tfName.setText(name);
    }

    public void setEmail(String email) {
        tfEmail.setText(email);
    }

    public void setPass(String pass) {
        pfPass.setText(pass);
    }

    public void setFoto(Foto foto) {
        if (foto == null) return;
        try {
            // Redimensiona a imagem e carrega no label
            Image img = ImageIO.read(new ByteArrayInputStream(foto.getBinary()));
            img = img.getScaledInstance(130, 130, Image.SCALE_SMOOTH);
            lbFoto.setIcon(new ImageIcon(img));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
